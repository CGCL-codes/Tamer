package Deroid.FirstUI;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class MyImgView extends ImageView {

    int oldX, oldY;

    int top = 0, left = 0, right = 0, bottom = 0;

    private Intent applicaiton = null;

    private String imagePath = "";

    private int itemIdx = 0;

    private FirstUI ui;

    public MyImgView(Context context, FirstUI ui) {
        super(context);
        this.ui = ui;
    }

    public void setStartXY(int l, int t, int r, int b) {
        setFrame(l, t, r, b);
        setLayoutParams(new AbsoluteLayout.LayoutParams(getWidth(), getHeight(), l, t));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (ui.getModeType() == FirstUI.MODE_MODIFY_MOVE) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    {
                        this.setAlpha(200);
                        left = getLeft();
                        top = getTop();
                        right = getRight();
                        bottom = getBottom();
                        oldX = (int) event.getX();
                        oldY = (int) event.getY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    {
                        this.setAlpha(255);
                        left = getLeft();
                        top = getTop();
                        right = getRight();
                        bottom = getBottom();
                        setLayoutParams(new AbsoluteLayout.LayoutParams(getWidth(), getHeight(), left, top));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    {
                        int x = 0, y = 0, w = 0, h = 0;
                        x = (int) event.getX();
                        y = (int) event.getY();
                        x -= oldX;
                        y -= oldY;
                        w = getWidth();
                        h = getHeight();
                        left += x;
                        top += y;
                        right = left + w;
                        bottom = top + h;
                        setFrame(left, top, right, bottom);
                    }
                    break;
            }
            return super.onTouchEvent(event);
        } else if (ui.getModeType() == FirstUI.MODE_MODIFY_SIZE) {
            this.requestFocusFromTouch();
        } else if (ui.getModeType() == FirstUI.MODE_MODIFY_PROPERTY) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    {
                        ui.setCurrentSelectButton(itemIdx);
                    }
                    break;
            }
            return false;
        }
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                {
                    this.setAlpha(200);
                    left = getLeft();
                    top = getTop();
                    right = getRight();
                    bottom = getBottom();
                    setFrame(left + 1, top + 1, right + 1, bottom + 1);
                }
                break;
            case MotionEvent.ACTION_UP:
                {
                    this.setAlpha(255);
                    left = getLeft();
                    top = getTop();
                    right = getRight();
                    bottom = getBottom();
                    setFrame(left - 1, top - 1, right - 1, bottom - 1);
                    ui.setCurrentSelectButton(getItempIndex());
                    setLayoutParams(new AbsoluteLayout.LayoutParams(getWidth(), getHeight(), left - 1, top - 1));
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ui.getModeType() == FirstUI.MODE_MODIFY_SIZE) {
            left = getLeft();
            top = getTop();
            right = getRight();
            bottom = getBottom();
            switch(keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    bottom -= 1;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    bottom += 1;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    right -= 1;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    right += 1;
                    break;
            }
            setFrame(left, top, right, bottom);
            setLayoutParams(new AbsoluteLayout.LayoutParams(getWidth(), getHeight(), left, top));
            return true;
        }
        return false;
    }

    public void setApplication(Intent app) {
        applicaiton = app;
    }

    public Intent getApplication() {
        return applicaiton;
    }

    public void setImagePath(String path) {
        imagePath = path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setItempIndex(int idx) {
        itemIdx = idx;
    }

    public int getItempIndex() {
        return itemIdx;
    }
}
