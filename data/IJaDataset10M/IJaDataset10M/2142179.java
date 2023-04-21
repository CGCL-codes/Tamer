package cn.chengdu.in.android.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import cn.chengdu.in.android.R;
import cn.chengdu.in.android.location.LocationUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-9-21
 */
public class MapRange extends Overlay {

    private float mR;

    private Paint mPaint;

    private GeoPoint mPoint;

    public MapRange(GeoPoint point, float r, Context context) {
        super();
        mR = r;
        mPoint = point;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.color_map_range));
        mPaint.setAlpha(50);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
        Projection projection = mapView.getProjection();
        Point point = new Point();
        projection.toPixels(mPoint, point);
        int r = LocationUtils.metersToRadius(mR, mapView, 30.626);
        canvas.drawCircle(point.x, point.y, r, mPaint);
    }
}
