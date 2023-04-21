package com.cnc.mediaconnect.actionlistener;

import com.cnc.mediaconnect.common.Common;
import com.cnc.mediaconnect1.NavigateCallsScreen;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CallListener implements OnClickListener {

    private Context context;

    private String phone1;

    private String phone2;

    public CallListener(Context context, String phone1, String phone2) {
        super();
        this.context = context;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public void onClick(View v) {
        if (!Common.isNullOrBlank(phone1) && Common.isNullOrBlank(phone2)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone1));
            context.startActivity(callIntent);
            return;
        }
        if (Common.isNullOrBlank(phone1) && !Common.isNullOrBlank(phone2)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone2));
            context.startActivity(callIntent);
            return;
        }
        if (!Common.isNullOrBlank(phone1) && !Common.isNullOrBlank(phone2)) {
            if (!phone1.equals(phone2)) {
                Intent intent = new Intent(context, NavigateCallsScreen.class);
                Bundle data = new Bundle();
                data.putString(Common.ARG0, phone1);
                data.putString(Common.ARG1, phone2);
                intent.putExtras(data);
                context.startActivity(intent);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone1));
                context.startActivity(callIntent);
                return;
            }
        }
    }
}
