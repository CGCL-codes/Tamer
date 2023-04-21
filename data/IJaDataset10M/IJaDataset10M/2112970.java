package parentalcontroller.child.GUI;

import parentalcontroller.child.R;
import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;

public class SMSReceiveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsreceivelayout);
        TextView phoneNum = (TextView) findViewById(R.id.tvsmsreceive);
        TextView view = (TextView) findViewById(R.id.etsmsreceive);
        String[] messages = getIntent().getStringArrayExtra("msgset");
        String msg = "";
        String[] sp;
        String currentMessage = messages[0];
        int i = 1;
        while (null != currentMessage) {
            sp = currentMessage.split(":");
            phoneNum.setText("Form : " + sp[0]);
            msg += "Msg : " + sp[1] + "\n";
            currentMessage = messages[i];
            sendSMSParent(sp[0], "From : " + sp[0] + msg);
            i++;
        }
        view.setText(msg);
    }

    private void sendSMSParent(String smsNumberToSend, String smsTextToSend) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            smsTextToSend += "Generated msg:\"" + smsTextToSend + "\"by childroid";
            smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, null, null);
        } catch (IllegalArgumentException ix) {
        }
    }
}
