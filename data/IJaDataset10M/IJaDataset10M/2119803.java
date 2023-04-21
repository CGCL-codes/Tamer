package android.widget;

import java.util.Set;
import org.xmlvm.iphone.UIEvent;
import org.xmlvm.iphone.UITouch;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ImageButton extends ImageView {

    public ImageButton(Context c) {
        super(c);
    }

    public ImageButton(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        final OnClickListener theListener = listener;
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    theListener.onClick(ImageButton.this);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean xmlvmTouchesEvent(int action, Set<UITouch> touches, UIEvent event) {
        if (action == MotionEvent.ACTION_DOWN) {
            xmlvmSetDrawableState(PRESSED_STATE_SET);
        } else if (action == MotionEvent.ACTION_UP) {
            xmlvmSetDrawableState(EMPTY_STATE_SET);
        }
        return super.xmlvmTouchesEvent(action, touches, event);
    }
}
