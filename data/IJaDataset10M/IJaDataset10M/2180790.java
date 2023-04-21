package com.jd.oifilemanager.filemanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.commonsware.android.EMusicDownloader.R;

public class IconifiedTextView extends LinearLayout {

    private TextView mText;

    private TextView mInfo;

    private ImageView mIcon;

    public IconifiedTextView(Context context, IconifiedText aIconifiedText) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.filelist_item, this, true);
        mIcon = (ImageView) findViewById(R.id.icon);
        mText = (TextView) findViewById(R.id.text);
        mInfo = (TextView) findViewById(R.id.info);
    }

    public void setText(String words) {
        mText.setText(words);
    }

    public void setInfo(String info) {
        mInfo.setText(info);
    }

    public void setIcon(Drawable bullet) {
        mIcon.setImageDrawable(bullet);
    }
}
