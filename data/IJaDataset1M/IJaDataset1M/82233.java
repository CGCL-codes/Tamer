package com.google.code.drift.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.code.drift.R;

public class ChatItemLeft extends ChatItem {

    private Context context;

    public ChatItemLeft(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.chat_content_left, null);
        userNick = (TextView) view.findViewById(R.id.chat_other_nick);
        userIcon = (ImageView) view.findViewById(R.id.chat_other_icon);
        contentView = (TextView) view.findViewById(R.id.chat_other_text_content);
        addView(view);
    }
}
