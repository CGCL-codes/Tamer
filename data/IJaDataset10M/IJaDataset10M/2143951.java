package test.project;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/** Implement onClickListener to dismiss dialog when OK Button is pressed */
public class CustomizeDialog extends Dialog implements OnClickListener {

    Button okButton;

    public CustomizeDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        okButton = (Button) findViewById(R.id.OkButton);
        okButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == okButton) dismiss();
    }
}
