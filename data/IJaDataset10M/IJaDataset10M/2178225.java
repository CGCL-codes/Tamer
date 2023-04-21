package org.opengpx.lib.map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ZoomControls;

/**
 * 
 * @author OsmAnd Team (https://code.google.com/p/osmand/)
 *
 */
public class OsmZoomControls extends ZoomControls {

    public OsmZoomControls(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OsmZoomControls(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) getChildAt(0).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        layoutParams = (android.widget.LinearLayout.LayoutParams) getChildAt(1).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        super.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }
}
