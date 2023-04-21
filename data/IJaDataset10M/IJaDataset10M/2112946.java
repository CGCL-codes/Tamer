package agrobot.navigo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class NavigationView extends View {

    private float direction = 0;

    private Paint lines = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean firstDraw;

    public NavigationView(Context context) {
        super(context);
        init();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        lines.setStyle(Paint.Style.STROKE);
        lines.setStrokeWidth(1);
        lines.setColor(Color.YELLOW);
        text.setColor(Color.WHITE);
        text.setTextSize(15);
        firstDraw = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cxCompass = getMeasuredWidth() / 2;
        int cyCompass = getMeasuredHeight() / 2;
        float radiusCompass;
        float temp;
        if (cxCompass > cyCompass) {
            radiusCompass = (float) (cyCompass * 0.8);
            temp = (float) (radiusCompass + cyCompass * 0.2);
            cxCompass = (int) temp;
        } else {
            radiusCompass = (float) (cxCompass * 0.8);
            temp = (float) (radiusCompass + cxCompass * 0.2);
            cyCompass = (int) temp;
        }
        lines.setColor(Color.rgb(0x80, 0x80, 0x80));
        lines.setStrokeWidth(2);
        lines.setStyle(Style.FILL);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), lines);
        lines.setColor(Color.rgb(0x85, 0x98, 0x7C));
        lines.setStyle(Style.FILL);
        canvas.drawRect(3, 3, getMeasuredWidth() - 3, getMeasuredHeight() - 3, lines);
        lines.setStyle(Style.STROKE);
        lines.setColor(0xFF000040);
        canvas.drawCircle(cxCompass, cyCompass, radiusCompass, lines);
        canvas.drawCircle(cxCompass, cyCompass, (float) (radiusCompass * 0.66), lines);
        canvas.drawCircle(cxCompass, cyCompass, (float) (radiusCompass * 0.33), lines);
        if (!firstDraw) {
            canvas.drawLine(cxCompass, cyCompass, (float) (cxCompass + radiusCompass * Math.sin((double) (-direction) * 3.14 / 180)), (float) (cyCompass - radiusCompass * Math.cos((double) (-direction) * 3.14 / 180)), lines);
            Typeface mFace = Typeface.createFromAsset(getContext().getAssets(), "DS-DIGI.TTF");
            text.setColor(0xFF000040);
            text.setTypeface(mFace);
            text.setTextSize(20);
            canvas.drawText("N", cxCompass - text.getTextSize() / 2, cyCompass - (radiusCompass + 3 * lines.getStrokeWidth()), text);
            Rect rect = new Rect(495, 95, 495 + 85, 95 + 85);
            final RectF rectF = new RectF(rect);
            canvas.drawRoundRect(rectF, 5, 5, lines);
            text.setTextSize(48);
            canvas.drawText("22�13 45", 500, 100, text);
        }
    }

    public void updateDirection(float dir) {
        firstDraw = false;
        direction = 52;
        invalidate();
    }
}
