package com.cnc.mediaconnect.actionlistener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SendTextListener implements OnClickListener {

    private Context context;

    private String email;

    public SendTextListener(Context context, String email) {
        super();
        this.context = context;
        this.email = email;
    }

    public void onClick(View v) {
        if (!email.replace(" ", "").equals("")) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("html/text");
            String aEmailList[] = { email };
            String aEmailCCList[] = {};
            String aEmailBCCList[] = {};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
            emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
            emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Send a mai;");
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
    }
}
