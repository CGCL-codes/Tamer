package cn.chengdu.in.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.chengdu.in.android.R;

/**
 * 用于UserInfo
 * @author Declan.z(declan.zhang@gmail.com)
 * @date 2011-4-14
 */
public class ArrowButton extends RelativeLayout {

    private Button mButton;

    private TextView mTitle;

    private TextView mCount;

    public ArrowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.button_arrow, this);
        mButton = (Button) findViewById(R.id.btn);
        mTitle = (TextView) findViewById(R.id.title);
        mCount = (TextView) findViewById(R.id.count);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.icdAttr);
        String title = array.getString(R.styleable.icdAttr_title);
        mTitle.setText(title);
        mButton.setId(this.getId());
        array.recycle();
    }

    public void setCount(String count) {
        mCount.setText(count);
    }

    public void setOnButtonClickListener(OnClickListener listener) {
        mButton.setOnClickListener(listener);
    }
}
