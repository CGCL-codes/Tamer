package partyplanner.app.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    int supplyBrought = -1;

    String number = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String message = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                number += "SMS from " + msgs[i].getOriginatingAddress();
                message += msgs[i].getMessageBody().toString();
                if (message.contains("pp01bs00")) {
                    supplyBrought = 0;
                } else if (message.contains("pp01bs01")) {
                    supplyBrought = 1;
                } else if (message.contains("pp01bs02")) {
                    supplyBrought = 2;
                } else if (message.contains("pp01bs03")) {
                    supplyBrought = 3;
                } else if (message.contains("pp01bs04")) {
                    supplyBrought = 4;
                } else if (message.contains("pp01bs05")) {
                    supplyBrought = 5;
                } else if (message.contains("pp01bs06")) {
                    supplyBrought = 6;
                } else if (message.contains("pp01bs07")) {
                    supplyBrought = 7;
                } else if (message.contains("pp01bs08")) {
                    supplyBrought = 8;
                }
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 
     * @return the number value representing the supply that has been brought
     */
    public int getSupplyBrought() {
        return supplyBrought;
    }

    /**
     * 
     * @return phone number of sender
     */
    public String getNumber() {
        return number;
    }
}
