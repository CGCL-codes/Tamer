package howbuy.android.palmfund.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 解决键盘问题
 * 
 * @author yescpu
 * 
 */
public class ResizeLinerLayout extends LinearLayout {

    public ResizeLinerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnResizeListener mListener;

    public interface OnResizeListener {

        void OnResize(int w, int h, int oldw, int oldh);
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mListener != null) {
            mListener.OnResize(w, h, oldw, oldh);
        }
    }
}
