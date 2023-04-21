package com.android.page;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.*;
import android.util.AttributeSet;

public class BookView1 extends View {

    private Paint markerPaint;

    private Paint textPaint;

    private Paint circlePaint;

    private String northString;

    private String eastString;

    private String southString;

    private String westString;

    private int textHeight;

    /** Get or set the bearing displayed by the compass **/
    public void setBearing(float _bearing) {
        bearing = _bearing;
    }

    public float getBearing() {
        return bearing;
    }

    private float bearing;

    /** Constructors **/
    public BookView1(Context context) {
        super(context);
        initCompassView();
    }

    public BookView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public BookView1(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initCompassView();
    }

    /** Initialize the Class variables **/
    protected void initCompassView() {
        setFocusable(true);
        Resources r = this.getResources();
        northString = r.getString(R.string.cardinal_north);
        eastString = r.getString(R.string.cardinal_east);
        southString = r.getString(R.string.cardinal_south);
        westString = r.getString(R.string.cardinal_west);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(R.color.background_color);
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(r.getColor(R.color.marker_color));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(r.getColor(R.color.text_color));
        textHeight = (int) textPaint.measureText("yY");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int px = getMeasuredWidth() / 2;
        int py = getMeasuredHeight() / 2;
        int radius = Math.min(px, py);
        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.save();
        canvas.rotate(-bearing, px, py);
        int textWidth = (int) textPaint.measureText("W");
        int cardinalX = px - textWidth / 2;
        int cardinalY = py - radius + textHeight;
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);
            canvas.save();
            canvas.translate(0, textHeight);
            if (i % 6 == 0) {
                String dirString = "";
                switch(i) {
                    case (0):
                        {
                            dirString = northString;
                            int arrowY = 2 * textHeight;
                            canvas.drawLine(px, arrowY, px - 5, 3 * textHeight, markerPaint);
                            canvas.drawLine(px, arrowY, px + 5, 3 * textHeight, markerPaint);
                            break;
                        }
                    case (6):
                        dirString = eastString;
                        break;
                    case (12):
                        dirString = southString;
                        break;
                    case (18):
                        dirString = westString;
                        break;
                }
                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
            } else if (i % 3 == 0) {
                String angle = String.valueOf(i * 15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int) (px - angleTextWidth / 2);
                int angleTextY = py - radius + textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            canvas.restore();
            canvas.rotate(15, px, py);
        }
        canvas.restore();
    }
}
