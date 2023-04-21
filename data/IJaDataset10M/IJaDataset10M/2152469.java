package com.timothy.android.view;

import java.util.List;
import com.timothy.android.util.NumberUtil;
import com.timothy.answer.ChoiceResult;
import com.timothy.chart.CanvasSetting;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class Bar2DViewVer extends BaseView implements SurfaceHolder.Callback {

    private static final String LOG_TAG = "Bar2DViewVer";

    protected SurfaceHolder mHolder;

    protected Canvas canvas;

    protected int canvasHeight;

    protected int canvasWidth;

    public Bar2DViewVer(Context context, BaseBean bean) {
        super(context, bean);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(LOG_TAG, "---------------------Bar2DViewVer:surfaceCreated---------------------");
        mHolder = getHolder();
        mHolder.addCallback(this);
        canvas = mHolder.lockCanvas();
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        CanvasSetting setting = new CanvasSetting(canvasHeight, canvasWidth, Color.BLACK, 1);
        canvas.drawColor(setting.getBgColor());
        Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor(questionTextColor));
        mPaint.setTextSize(questionTextSize);
        int queX = setting.getLeftMargin();
        int queY = setting.getTopMargin();
        Log.i(LOG_TAG, "Bar2DViewVer:queX:" + queX);
        Log.i(LOG_TAG, "Bar2DViewVer:queY:" + queY);
        canvas.drawText(baseBean.getQuestionText(), queX, queY, mPaint);
        canvas.save();
        List<ChoiceResult> crList = baseBean.getCrList();
        int canvasNoMarginWidth = canvasWidth - setting.getLeftMargin() - setting.getRightMargin();
        double barWidthInclSpace = (canvasNoMarginWidth * 1.0) / crList.size();
        int barWidth = (int) (barWidthInclSpace * 0.9);
        int spaceWidth = (int) (barWidthInclSpace * 0.1);
        if (spaceWidth < 1) spaceWidth = 1;
        Log.i(LOG_TAG, "Bar2DViewVer:canvasNoMarginWidth:" + canvasNoMarginWidth);
        Log.i(LOG_TAG, "Bar2DViewVer:barWidthInclSpace:" + barWidthInclSpace);
        Log.i(LOG_TAG, "Bar2DViewVer:barWidth:" + barWidth);
        Log.i(LOG_TAG, "Bar2DViewVer:spaceWidth:" + spaceWidth);
        setting.setSpace(spaceWidth);
        for (int i = 0; i < crList.size(); i++) {
            ChoiceResult cr = crList.get(i);
            int votes = cr.getCount();
            double votesShare = cr.getShare();
            Log.i(LOG_TAG, "Bar2DViewVer:votes:" + votes);
            Log.i(LOG_TAG, "Bar2DViewVer:votesShare:" + votesShare);
            int barHeigth = (int) (votesShare * (canvasHeight - setting.getTopMargin() - setting.getBottomMargin()));
            Log.i(LOG_TAG, "Bar2DViewVer:barHeigth:" + barHeigth);
            int tlX = (int) (setting.getLeftMargin() + i * (barWidth + setting.getSpace()));
            int tlY = canvasHeight - setting.getBottomMargin() - barHeigth;
            int brX = tlX + barWidth;
            int brY = canvasHeight - setting.getBottomMargin();
            mPaint.setColor(Color.parseColor(chartColorArr[i % 8]));
            Log.i(LOG_TAG, "Bar2DViewVer:fullColor=" + chartColorArr[i % 8]);
            canvas.drawRect(tlX, tlY, brX, brY, mPaint);
            Log.i(LOG_TAG, "Bar2DViewVer:tlX,tlY,brX,brY=" + tlX + "," + tlY + "," + brX + "," + brY + ",");
            canvas.save();
            mPaint.setTextSize(choiceTextSize);
            mPaint.setColor(Color.parseColor(choiceTextColor));
            canvas.drawText(cr.getContent(), tlX, canvasHeight - setting.getBottomMargin() / 2, mPaint);
            Log.i(LOG_TAG, "Bar2DViewVer:ChoiceText=" + cr.getContent());
            canvas.save();
            mPaint.setTextSize(answerTextSize);
            mPaint.setColor(Color.parseColor(answerTextColor));
            canvas.drawText(String.valueOf(cr.getCount()), tlX, tlY - 20, mPaint);
            canvas.drawText(NumberUtil.formatData1Deci(cr.getShare() * 100) + "%", tlX, tlY - 40, mPaint);
            canvas.save();
            Log.i(LOG_TAG, "-------------------------------------------------------------------");
        }
        canvas.restore();
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
