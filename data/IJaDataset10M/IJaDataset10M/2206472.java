package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import java.util.Random;

public class Canvas2DRandomShapesWithAlphaActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CanvasView(this));
    }

    class CanvasView extends View {

        Paint paint;

        Random random = new Random();

        public CanvasView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            for (int i = 0; i < 10; i++) {
                paint = new Paint();
                paint.setARGB(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
                canvas.drawLine(random.nextInt(canvas.getWidth()), random.nextInt(canvas.getHeight()), random.nextInt(canvas.getWidth()), random.nextInt(canvas.getHeight()), paint);
                canvas.drawCircle(random.nextInt(canvas.getWidth() - 30), random.nextInt(canvas.getHeight() - 30), random.nextInt(30), paint);
                canvas.drawRect(random.nextInt(canvas.getWidth()), random.nextInt(canvas.getHeight()), random.nextInt(canvas.getWidth()), random.nextInt(canvas.getHeight()), paint);
            }
        }
    }
}
