package de.fhkl.mHelloWorld.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import de.fhkl.helloWorld.HelloWorldProperties;
import de.fhkl.helloWorld.implementation.actions.ProfileRequester;
import de.fhkl.helloWorld.implementation.actions.messages.MessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageManager;
import de.fhkl.helloWorld.interfaces.actions.messages.IMessageListener;
import de.fhkl.helloWorld.interfaces.model.account.configuration.SendMailConnection;
import de.fhkl.helloWorld.interfaces.model.account.hCard.HCard;
import de.fhkl.helloWorld.interfaces.model.account.profile.Contact;
import de.fhkl.helloWorld.interfaces.model.account.profile.EncryptedSubProfile;
import de.fhkl.helloWorld.interfaces.model.account.profile.RelationShipType;
import de.fhkl.helloWorld.interfaces.model.account.profile.SubProfile;
import de.fhkl.helloWorld.interfaces.model.attribute.Attribute;
import de.fhkl.helloWorld.interfaces.model.attribute.AttributeList;
import de.fhkl.helloWorld.interfaces.util.protocols.SendMailProtocol;
import de.fhkl.mHelloWorld.implementation.mail.MailReceiverTest;
import de.fhkl.mHelloWorld.implementation.mail.MessageBean;
import de.fhkl.mHelloWorld.implementation.mail.MessageSender;
import de.fhkl.mHelloWorld.implementation.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MessageWriter extends HelloWorldBasic implements Runnable {

    private static final String I = "======================= [HELLO-WORLD] " + "MailWriter" + ": ";

    private int mContactId;

    private SendMailConnection mSendConf;

    private MessageSender mSender;

    private MessageBean mMessageBean;

    private MessageManager mMessageManager;

    private Bundle mUserBundle;

    private String mHost;

    private String mPort;

    private String mUser;

    private String mPassword;

    private String mFrom;

    private EditText mTo;

    private EditText mSubject;

    private EditText mBody;

    private Button mSend;

    private TextView mResult;

    private String mPublicKeyUrl;

    private SendMailProtocol mProtocol;

    private ProgressDialog mProgress;

    private boolean mIsSent;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.write_message);
        mUserBundle = getIntent().getExtras();
        mTo = (EditText) this.findViewById(R.id.mail_send_to);
        mSubject = (EditText) this.findViewById(R.id.mail_send_subject);
        mBody = (EditText) this.findViewById(R.id.mail_send_body);
        mSend = (Button) this.findViewById(R.id.mail_send_button_send);
        mResult = (TextView) this.findViewById(R.id.mail_send_result_field);
        mSendConf = HelloWorldBasic.getAccount().getConfiguration().getSendMailConncetions().get(0);
        setReceiverData();
        mSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                mProgress = ProgressDialog.show(MessageWriter.this, "Sending Email in Progress", "To: " + mTo.getText().toString(), true, false);
                Log.i(I, "Starting a new Thread");
                Thread thread = new Thread(MessageWriter.this);
                thread.start();
            }
        });
    }

    public void run() {
        try {
            Looper.prepare();
            Log.i(I, "Try to sendMail();");
            mIsSent = sendMail();
            Log.i(I, "mIsSent = " + mIsSent);
        } catch (Exception e) {
            Log.e("sendMail-Exception", e.getMessage(), e);
        } finally {
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            mProgress.dismiss();
            giveResponseToUser();
        }
    };

    private void giveResponseToUser() {
        if (mIsSent) {
            Toast.makeText(MessageWriter.this, "Mail successfully sent to: " + mTo.getText().toString() + "!", Toast.LENGTH_SHORT).show();
            mResult.setText("Mail successfully sent!");
        } else {
            mResult.setText("Uups, that did not work. Please check your" + " data.");
        }
    }

    private boolean sendMail() {
        try {
            mProtocol = mSendConf.getProtocol();
            mHost = mSendConf.getHost().getValue();
            mPort = mSendConf.getPort().getValue();
            mUser = mSendConf.getUsername().getValue();
            mPassword = mSendConf.getPassword().getValue();
            mFrom = mSendConf.getSenderAddress().getValue();
        } catch (Exception e) {
            Log.i(I, "Exception in setting up Sender-Data!");
        }
        Log.i(I, "Create new MessageSender with Data from View");
        mSender = new MessageSender(mHost, mUser, mPassword, Integer.valueOf(mPort), mProtocol);
        Log.i(I, "Create new MessageBean with Data from View");
        mMessageBean = new MessageBean();
        mMessageBean.setRecipientsAddress(mTo.getText().toString());
        mMessageBean.setSenderAddress(mFrom);
        mMessageBean.setSubject(HelloWorldProperties.getString("MailSubjectTextMessageIdentifier"));
        String body = "Subject: " + mSubject.getText() + "\n" + mBody.getText();
        mMessageBean.setText(body);
        return mSender.sendMessage(mMessageBean);
    }

    private boolean setReceiverData() {
        try {
            mTo.setText(mUserBundle.getString("mailAddress"));
            Log.i(I, "To: " + mTo.getText());
        } catch (Exception e) {
            mTo.setText("No Email-Address available!");
            return false;
        }
        try {
            mPublicKeyUrl = mUserBundle.getString("pubKeyUrl");
            Log.i(I, "PublicKey-Url: " + mPublicKeyUrl);
        } catch (Exception e) {
            Toast.makeText(MessageWriter.this, "No PublicKey-Url was found!:", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait sending Email ...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;
    }
}
