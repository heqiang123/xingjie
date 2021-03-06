package com.eros.erosplugingt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.eros.erosplugingt.manager.PushManager;
import com.eros.framework.manager.ManagerFactory;
import com.eros.framework.manager.impl.ParseManager;
import com.eros.framework.utils.SharePreferenceUtil;
import com.igexin.sdk.PushConsts;

/**
 * Created by Carry on 2017/11/15.
 */

public class GTPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                //收到透传消息
                byte[] payloads = bundle.getByteArray("payload");
                String content = null;
                try {
                    content = new String(payloads, "UTF-8");
                    Log.e("GPush", "payload>>>>>>>>" + content);
                    if (!TextUtils.isEmpty(content)) {
                        ParseManager parseManager = ManagerFactory.getManagerService
                                (ParseManager.class);
                        JSONObject payload = parseManager.parseObject(content);
                        Log.e("GPush----", "payloa--------" + payload);

                        if (payload != null) {
                            String data = payload.getString("payload");
                            Log.e("GPush", "payloa--------" + data);

                            ManagerFactory.getManagerService(PushManager.class).handlePush
                                    (context, data);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case PushConsts.GET_CLIENTID:
                //收到clientId
                String mCid = bundle.getString("clientid");
                Log.e("GPush", "cid>>>>>>>>" + mCid);
                //保存cid
                if (!TextUtils.isEmpty(mCid)) {
                    SharePreferenceUtil.setClientId(context, mCid);
                }
                break;
            default:
                break;
        }
    }
}
