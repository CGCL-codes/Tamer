package org.imogene.android.maps.overlay;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class LongClickableOverlay extends Overlay {

    public interface OnLongClickListener {

        public boolean onLongClick(MapView mapView, IGeoPoint point);
    }

    private OnLongClickListener mListener;

    public LongClickableOverlay(Context context, OnLongClickListener listener) {
        super(context);
        mListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
    }

    @Override
    public boolean onLongPress(MotionEvent e, MapView mapView) {
        if (mListener != null) {
            final IGeoPoint point = mapView.getProjection().fromPixels(e.getX(), e.getY());
            return mListener.onLongClick(mapView, point);
        }
        return super.onLongPress(e, mapView);
    }
}
