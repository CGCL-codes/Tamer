package com.siri;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Email {

    public void SendEmail(Context context, String addr, String subject, String text) {
        Uri uri = Uri.parse("mailto:" + addr);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra(Intent.EXTRA_SUBJECT, subject);
        it.putExtra(Intent.EXTRA_TEXT, text);
        it.setComponent(new ComponentName("com.google.android.gm", "com.google.android.gm/.ComposeActivity"));
        context.startActivity(it);
    }
}
