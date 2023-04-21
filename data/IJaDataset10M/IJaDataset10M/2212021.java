package org.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author ������� �A�[�����[�j���O ��
 *
 */
public class ChangeTaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Toast.makeText(context, "onReceive", Toast.LENGTH_LONG).show();
        }
    }
}
