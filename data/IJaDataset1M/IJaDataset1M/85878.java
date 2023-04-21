package com.xlg.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;
import com.xlg.common.utils.CommonUtil;

/**
 * 抢购画廊处理
 */
public class QiangGouGallery extends Gallery {

    public QiangGouGallery(Context context) {
        super(context);
    }

    public QiangGouGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QiangGouGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
	 * 翻页
	 */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float defRightX = this.getWidth() * 2;
        float defLeftX = -defRightX;
        float tmpX = 0f;
        if (velocityX > 0) {
            tmpX = velocityX > defRightX ? defRightX : velocityX;
        } else if (velocityX < 0) {
            tmpX = velocityX < defLeftX ? defLeftX : velocityX;
        }
        return super.onFling(e1, e2, tmpX, velocityY);
    }

    /**
	 * 整理抢购画廊中图片显示大小
	 */
    public LayoutParams getImageViewLayoutParams() {
        int picWidth = (getWidth() - CommonUtil.dip2px(20)) / 3;
        return new LayoutParams(picWidth, picWidth);
    }
}
