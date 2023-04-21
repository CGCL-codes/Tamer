package android.demo.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MsgItemView extends TextView {

    private Paint marginPaint;

    private Paint linePaint;

    private int paperColor;

    private int margin;

    public MsgItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MsgItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MsgItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Resources myResources = getResources();
        marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        marginPaint.setColor(myResources.getColor(R.color.msg_margin));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(myResources.getColor(R.color.msg_lines));
        paperColor = myResources.getColor(R.color.msg_paper);
        margin = (int) myResources.getDimension(R.dimen.msg_margin);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(paperColor);
        canvas.drawLine(0, 0, getMeasuredHeight(), 0, linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);
        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);
        canvas.save();
        canvas.translate(margin, 0);
        super.onDraw(canvas);
        canvas.restore();
    }
}
