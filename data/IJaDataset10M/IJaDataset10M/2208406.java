package eks.grafik;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import dk.nordfalk.android.elementer.R;

/**
 *
 * @author Jacob Nordfalk
 */
public class Grafikdemo2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MinGrafik minGrafik = new MinGrafik(this);
        minGrafik.setBackgroundResource(R.drawable.logo);
        setContentView(minGrafik);
    }
}

class MinGrafik extends View {

    float rotation = -45;

    public MinGrafik(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas c) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);
        int blå = Color.BLUE;
        blå = 0xff0000ff;
        blå = Color.argb(255, 0, 0, 255);
        blå = 255 * 256 * 256 * 256 + 255;
        paint.setColor(blå);
        c.drawCircle(20, 20, 15, paint);
        paint.setAntiAlias(true);
        c.drawCircle(60, 20, 15, paint);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        Path trekantPath = new Path();
        trekantPath.moveTo(0, -10);
        trekantPath.lineTo(5, 0);
        trekantPath.lineTo(-5, 0);
        trekantPath.close();
        paint.setStyle(Paint.Style.STROKE);
        trekantPath.offset(100, 25);
        c.drawPath(trekantPath, paint);
        trekantPath.offset(40, 0);
        paint.setStyle(Paint.Style.FILL);
        c.drawPath(trekantPath, paint);
        trekantPath.offset(40, 0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        c.drawPath(trekantPath, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(30);
        c.drawText("streg - Style.STROKE", 15, 85, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        c.drawText("fyldt - Style.FILL", 15, 120, paint);
        DashPathEffect dashPath = new DashPathEffect(new float[] { 20, 5 }, 1);
        paint.setPathEffect(dashPath);
        paint.setStrokeWidth(5);
        c.drawLine(10, 130, 300, 150, paint);
        paint.setPathEffect(null);
        paint.setColor(Color.argb(128, 0, 255, 0));
        paint.setTextSize(48);
        String roteretTekst = "Tryk for at rotere";
        Rect tekstomrids = new Rect();
        paint.getTextBounds(roteretTekst, 0, roteretTekst.length(), tekstomrids);
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        c.rotate(rotation, x + tekstomrids.centerX(), y + tekstomrids.centerY());
        c.drawText(roteretTekst, x, y, paint);
        c.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rotation = event.getX();
        invalidate();
        return true;
    }
}
