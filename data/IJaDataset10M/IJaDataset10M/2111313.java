package org.sharp.android.view;

import org.sharp.android.intf.ContentViewer;
import my.library.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TextInputViewProvider implements ContentViewer {

    Context mCtx;

    private String mName;

    private EditText et;

    public TextInputViewProvider(Context ctx, String name) {
        mCtx = ctx;
        mName = name;
    }

    @Override
    public View contentView() {
        LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.text_input, null);
        TextView tv = (TextView) root.findViewById(R.id.name);
        tv.setText(mName);
        et = (EditText) root.findViewById(R.id.text);
        return root;
    }

    public String inputtedText() {
        return et.getText().toString();
    }
}
