package com.szxys.mhub.ui.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * 曲线图控件类
 * @author xak
 *
 */
public class GraphView extends View implements OnGestureListener {

    protected static final float SCALE_FACT = 0.1f;

    private static final int MARGIN = 30;

    private static final String TAG = "GraphView";

    private static final float DRAG_MIN = 1.0f;

    private static final float SCALE_MIN = 10;

    protected static final int MES_UPDATE = 0;

    protected static final int MES_INVALIDATE = 1;

    private static final int HALF_AXIX_WIDTH = 1;

    private static final float PRECISION = 0.01f;

    private static final float RADIUS = 5;

    private static final float DEFAULT_TEXT_SIXE = 12;

    protected static float mDensity;

    protected static float mDragMin;

    protected static float mScaleMin;

    private Grid mGrid;

    private GraphViewConfig mConfig;

    private Axis mAxisX;

    private Axis mAxisYLeft;

    private Axis mAxisYRight;

    private Paint mPaint;

    private float mXspacePre;

    private float mYspacePre;

    private float mXPre;

    private float mYPre;

    private Handler mHandler;

    private SlidingRunnable mSlideRunnable;

    private GestureDetector mGestureDetector;

    private ArrayList<Curve> mCurves;

    private ArrayList<Label> mLabel;

    private ArrayList<Line> mLines;

    private ArrayList<Rectz> mRects;

    private boolean mIsScale;

    private boolean mIsSizeInit;

    private Bitmap mBitCurve;

    private float mCurOffsetPreX;

    private float mMargin;

    private int mWidth;

    private int mHeight;

    /**
	 * 默认构造函数
	 * @param context
	 */
    public GraphView(Context context) {
        super(context);
        initGraphView(context);
    }

    /**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGraphView(context);
    }

    /**
	 * 添加曲线
	 * @param curve 曲线对象
	 */
    public void addCurve(Curve curve) {
        mCurves.add(curve);
        if (!mConfig.isRealTime && mIsSizeInit) {
            preCurveDatas();
        }
    }

    /**
	 * 设置控件属性
	 * @param config 曲线图控件属性对象
	 */
    public void setConfig(GraphViewConfig config) {
        mConfig = config;
        if (mConfig.isRealTime) {
            mConfig.isSupportDrag = false;
        }
        initAxisX();
    }

    /**
	 * 设置X轴坐标
	 * @param startCoordinate  开始坐标
	 * @param endCoordinate    结束坐标
	 */
    public void setAxisXCoordinate(double startCoordinate, double endCoordinate) {
        Log.v(TAG, "set AxixX");
        Log.v(TAG, "The start is:" + startCoordinate + ";and the end is:" + endCoordinate);
        mAxisX.setCoordinate(startCoordinate, endCoordinate);
    }

    /**
	 * 	设置X轴坐标，当当坐标模式为{@link #MODE_TIME}时，坐标值为时间的毫秒表示形式
	 * @param startCoordinate 开始坐标，
	 * @param endCoordinate   结束坐标
	 * @param graduation      坐标刻度，每一刻度所代表的值
	 */
    public void setAxisXCoordinate(double startCoordinate, double endCoordinate, double graduation) {
        Log.v(TAG, "set AxisXCoordinate");
        mAxisX.setCoordinate(startCoordinate, endCoordinate);
        mAxisX.graduation = graduation;
    }

    /**
	 * 设置左侧Y轴坐标
	 * @param startCoordinate  开始坐标
	 * @param endCoordinate    结束坐标
	 */
    public void setAxisYLeftCoordinate(double startCoordinate, double endCoordinate) {
        Log.v(TAG, "set AxixYleft");
        mAxisYLeft.startValue = startCoordinate;
        mAxisYLeft.setCoordinate(startCoordinate, endCoordinate);
    }

    /**
	 * 设置右侧Y轴坐标；只有在双坐标模式下才有效
	 * @param startCoordinate  开始坐标
	 * @param endCoordinate    结束坐标
	 */
    public void setAxisYRightCoordinate(double startCoordinate, double endCoordinate) {
        Log.v(TAG, "set AxixYRight");
        mAxisYRight.setCoordinate(startCoordinate, endCoordinate);
    }

    /**
	 * 为曲线添加单个坐标值;只有在实时摸下有效
	 * @param x    x坐标值
	 * @param y    y坐标值
	 */
    public void add(float x, float y) {
        if (mCurves.get(0).dataQueue.size() == mCurves.get(0).maxPoint) {
            mCurves.get(0).dataQueue.removeFirst();
        }
        mCurves.get(0).dataQueue.addLast(new float[] { (float) mAxisX.dataConversion(x, mGrid.xRank), (float) mAxisYLeft.dataConversion(y, mGrid.yRank) });
        invalidate();
    }

    /**
	 * 在指定位置绘制文本
	 * @param text 需绘制的文本
	 * @param x	   绘制位置的X坐标值
	 * @param y  绘制位置Y坐标值
	 */
    public void drawText(String text, float x, float y) {
        Label lable = new Label(text, x, y);
        mLabel.add(lable);
    }

    /**
	 * 在指定位置绘制文本
	 * @param text 需绘制的文本
	 * @param x	   绘制位置的X坐标值
	 * @param y  绘制位置Y坐标值
	 * @param color 文本颜色
	 */
    public void drawText(String text, float x, float y, int color) {
        Label lable = new Label(text, x, y, color);
        mLabel.add(lable);
    }

    /**
	 * 在指定位置绘制文本
	 * @param text 需绘制的文本
	 * @param x	   绘制位置的X坐标值
	 * @param y  绘制位置Y坐标值
	 * @param color 文本颜色
	 * @param size 文字大小
	 */
    public void drawText(String text, float x, float y, int color, int size) {
        Label lable = new Label(text, x, y, color, size);
        mLabel.add(lable);
    }

    /**
	 * 在指定位置绘制直线
	 * @param xStart 直线起点X坐标
	 * @param yStart 直线起点Y坐标
	 * @param xEnd   直线终点X坐标
	 * @param yEnd   直线终点Y坐标
	 */
    public void drawLine(float xStart, float yStart, float xEnd, float yEnd) {
        Line line = new Line(xStart, yStart, xEnd, yEnd);
        mLines.add(line);
    }

    /**
	* 在指定位置绘制直线
	 * @param xStart 直线起点X坐标
	 * @param yStart 直线起点Y坐标
	 * @param xEnd   直线终点X坐标
	 * @param yEnd   直线终点Y坐标
	 * @param color  直线颜色
	 */
    public void drawLine(float xStart, float yStart, float sEnd, float yEnd, int color) {
        Line line = new Line(xStart, yStart, sEnd, yEnd, color);
        mLines.add(line);
    }

    /**
	 * 在指定位置绘制矩形
	 * @param xStart 矩形起点X坐标
	 * @param yStart 矩形起点Y坐标
	 * @param xEnd   矩形终点X坐标
	 * @param yEnd   矩形终点Y坐标
	 */
    public void drawRect(float xStart, float yStart, float xEnd, float yEnd) {
        Rectz rect = new Rectz(xStart, yStart, xEnd, yEnd);
        mRects.add(rect);
    }

    /**
	* 在指定位置绘制矩形
	 * @param xStart 矩形起点X坐标
	 * @param yStart 矩形起点Y坐标
	 * @param xEnd   矩形终点X坐标
	 * @param yEnd   矩形终点Y坐标
	 * @param color  矩形颜色
	 */
    public void drawRect(float xStart, float yStart, float sEnd, float yEnd, int color) {
        Rectz rect = new Rectz(xStart, yStart, sEnd, yEnd, color);
        mRects.add(rect);
    }

    private void initView() {
        Log.v(TAG, "init GraphView...");
        mMargin = (mDensity * MARGIN);
        setLongClickable(true);
        initHandler();
        mSlideRunnable = new SlidingRunnable(mHandler);
        mGrid = new Grid();
        mPaint = new Paint();
        mPaint.setTextSize(DEFAULT_TEXT_SIXE * mDensity);
        mPaint.setAntiAlias(true);
        mAxisYLeft = new AxisLinearY();
        mAxisYRight = new AxisLinearY();
        mConfig = new GraphViewConfig();
    }

    private void initHandler() {
        mHandler = new Handler() {

            Bundle data;

            @Override
            public void handleMessage(Message msg) {
                Log.v(TAG, "HandleMessage:The msg is:" + msg.what);
                super.handleMessage(msg);
                switch(msg.what) {
                    case MES_UPDATE:
                        data = msg.getData();
                        Log.v(TAG, "The xOffset is:" + data.getFloat("xOffset"));
                        scrollView(data.getFloat("xOffset"), data.getFloat("yOffset"));
                        invalidate();
                        break;
                    case MES_INVALIDATE:
                        invalidate();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mConfig.isSupportDrag) {
            return true;
        }
        int action = event.getAction();
        Log.v(TAG, "The action is:" + action);
        if (action == MotionEvent.ACTION_MOVE) {
            if (!mIsScale) {
                dragView(event);
            } else {
                scaleView(event);
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            mSlideRunnable.setRunFlag(false);
            mXPre = event.getX();
            mYPre = event.getY();
        } else if (action == MotionEvent.ACTION_UP) {
        } else if (mConfig.isSupportScale) {
            int actionCode = action & MotionEvent.ACTION_MASK;
            Log.v(TAG, "the actionCode is:" + actionCode);
            if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
                mIsScale = true;
                mXspacePre = Math.abs(event.getX(1) - event.getX());
                mYspacePre = Math.abs(event.getY(1) - event.getY());
            } else if (actionCode == MotionEvent.ACTION_POINTER_UP) {
                mIsScale = false;
                mXPre = event.getX();
                mYPre = event.getY();
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    private void sliding(float velocityX, float velocityY) {
        mSlideRunnable.setXY(velocityX, velocityY);
        mSlideRunnable.setRunFlag(true);
        Thread tmpThread = new Thread(mSlideRunnable);
        tmpThread.start();
    }

    private void scaleView(MotionEvent event) {
        float xSpace = Math.abs(event.getX(1) - event.getX());
        float ySpace = Math.abs(event.getY(1) - event.getY());
        int xScale = (int) (xSpace - mXspacePre);
        int yScale = (int) (ySpace - mYspacePre);
        scale(xSpace, ySpace, xScale, yScale);
    }

    private void scale(float xSpace, float ySpace, int xScale, int yScale) {
        float xOffset = mGrid.xRank.doScale(xScale);
        float yOffset = mGrid.yRank.doScale(yScale);
        if (Math.abs(xOffset) > 0.1) {
            mXspacePre = xSpace;
        }
        if (Math.abs(yOffset) > 0.1) {
            mYspacePre = ySpace;
        }
        Log.v(TAG, "The scroll off is:" + yOffset);
        scrollView(xOffset, -yOffset);
    }

    private void dragView(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float xOffset = x - mXPre;
        float yOffset = y - mYPre;
        scrollView(xOffset, yOffset);
        mXPre = x;
        mYPre = y;
    }

    /**
	 * 
	 * @param xOffset 向右偏移量
	 * @param yOffset 向下偏移量
	 */
    private void scrollView(float xOffset, float yOffset) {
        boolean isNeedInvalidate = false;
        if (mGrid.xRank.doScroll(xOffset)) {
            isNeedInvalidate = true;
        }
        if (mGrid.yRank.doScroll(-yOffset)) {
            isNeedInvalidate = true;
        }
        if (isNeedInvalidate) {
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mIsSizeInit = true;
        Log.v(TAG, "onSizeChanged:The w is:" + w + "and the h is:" + h);
        if (mAxisX == null) {
            initAxisX();
        }
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
        mGrid.xRank.len = (int) (w - mMargin * 2);
        mGrid.yRank.len = (int) (h - mMargin * 2);
        Log.v(TAG, "The yRank's len is:" + mGrid.yRank.len);
        Log.v(TAG, "The xLength" + mGrid.xRank.len);
        mBitCurve = Bitmap.createBitmap(mGrid.xRank.len, mGrid.yRank.len, Config.ARGB_4444);
        mBitCurve.setDensity((int) (mDensity * 160));
        mGrid.yRank.setSize(mGrid.yRank.size);
        mAxisX.x = mMargin;
        mAxisX.y = mGrid.yRank.len + mMargin / 2;
        if (mAxisX.graduation > PRECISION) {
            mGrid.xRank.setDefaultSize((float) (mGrid.xRank.len / mAxisX.length / mAxisX.graduation));
        }
        mGrid.xRank.setSize(mGrid.xRank.size);
        mAxisX.graduation = mAxisX.length / mGrid.xRank.count;
        mAxisYLeft.graduation = mAxisYLeft.length / mGrid.yRank.count;
        Log.v(TAG, "The y's length is:" + mGrid.yRank.len + ";and the count is:" + mGrid.yRank.count + ";The graduation is:" + mAxisYLeft.graduation);
        if (mConfig.isSupportDoubleCoordinate) {
            mAxisYRight.graduation = mAxisYRight.length / mGrid.yRank.count;
            mAxisYRight.x = mMargin + mGrid.xRank.len;
        }
        Log.v(TAG, "The curve's count is:" + mCurves.size());
        if (!mConfig.isRealTime) {
            preCurveDatas();
        }
        Log.v(TAG, "Thread out");
    }

    private void initAxisX() {
        switch(mConfig.xAxisMode) {
            case GraphViewConfig.MODE_LINEAR:
                mAxisX = new AxisLinearX();
                break;
            case GraphViewConfig.MODE_STRING:
                break;
            case GraphViewConfig.MODE_TIME:
                mAxisX = new AxisTime();
                break;
            default:
                break;
        }
    }

    private void preCurveDatas() {
        new Thread() {

            public void run() {
                for (Curve cur : mCurves) {
                    if (!cur.isStartConversion) {
                        datasConversionToShow(cur);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = MES_INVALIDATE;
                msg.sendToTarget();
                for (Curve cur : mCurves) {
                    if (cur.isPreOver && !cur.isStartConversionAll) {
                        dataConversionAll(cur);
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (mConfig.isRealTime) {
            updateRealTime();
        }
        Log.v(TAG, "The xRank count is:" + mGrid.xRank.count + ";and he yRank count is:" + mGrid.yRank.count);
        Log.v(TAG, "The start line is:" + mGrid.xRank.start + ";and the start row is:" + mGrid.yRank.start);
        Log.v(TAG, "The xOffset is:" + mGrid.xRank.offset + ";and the yOffset is:" + mGrid.yRank.offset);
        Log.v(TAG, "The xCellSize is:" + mGrid.xRank.size + ";and the yCellSize is:" + mGrid.yRank.size);
        if (mConfig.isSupportGrid) {
            drawGrid(canvas);
        }
        DrawAxis(canvas);
        if (!mConfig.isRealTime) {
            drawNewPart(canvas);
        }
        drawCur(canvas);
        drawRects(canvas);
        drawLines(canvas);
        drawLabel(canvas);
    }

    private void initGraphView(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        Log.v(TAG, "The density is:" + mDensity);
        mScaleMin = mDensity * SCALE_MIN;
        mDragMin = mDensity * DRAG_MIN;
        mGestureDetector = new GestureDetector(this);
        mCurves = new ArrayList<Curve>();
        mLabel = new ArrayList<Label>();
        mLines = new ArrayList<Line>();
        mRects = new ArrayList<Rectz>();
        initView();
    }

    private void drawRects(Canvas canvas) {
        for (Rectz r : mRects) {
            mPaint.setColor(r.color);
            canvas.drawRect(r.xStart * mDensity, r.yStart * mDensity, r.xEnd * mDensity, r.yEnd * mDensity, mPaint);
        }
    }

    private void drawLines(Canvas canvas) {
        for (Line l : mLines) {
            mPaint.setColor(l.color);
            canvas.drawLine(l.xStart * mDensity, l.yStart * mDensity, l.xEnd * mDensity, l.yEnd * mDensity, mPaint);
        }
    }

    private void drawLabel(Canvas canvas) {
        for (Label lab : mLabel) {
            mPaint.setTextSize(lab.size * mDensity);
            mPaint.setColor(lab.color);
            canvas.drawText(lab.text, lab.x * mDensity, lab.y * mDensity, mPaint);
        }
        mPaint.setTextSize(12 * mDensity);
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(mConfig.background);
        if (mConfig.haveRoundCorners) {
            RectF rect = new RectF(0, 0, mWidth, mHeight);
            canvas.drawRoundRect(rect, RADIUS * mDensity, RADIUS * mDensity, mPaint);
        } else {
            canvas.drawColor(mConfig.background);
        }
    }

    private void updateRealTime() {
        Curve cur = mCurves.get(0);
        if (cur.dataQueue.size() == cur.maxPoint) {
            float off = cur.dataQueue.get(cur.maxPoint - 2)[0] - cur.dataQueue.getLast()[0];
            Log.v(TAG, "The offset is" + off);
            mGrid.xRank.doScroll(off);
        }
    }

    private void drawCur(Canvas can) {
        float dx = -(mGrid.xRank.start * mGrid.xRank.size - mGrid.xRank.offset);
        float dy = mGrid.yRank.start * mGrid.yRank.size - mGrid.yRank.offset;
        can.save();
        can.clipRect(mGrid.getRect());
        can.translate(dx + mMargin, dy + mMargin);
        for (Curve cur : mCurves) {
            cur.doDraw(can, mPaint);
        }
        can.restore();
    }

    private void drawNewPart(Canvas can) {
        float dx = mGrid.xRank.start * mGrid.xRank.size - mGrid.xRank.offset;
        float dxoffset = dx - mCurOffsetPreX;
        mCurOffsetPreX = dx;
        Log.v(TAG, "drawNewPart:The dx is:" + dx);
        if (dxoffset < 0) {
            for (Curve cur : mCurves) {
                for (int i = cur.showStart; i > -1; i--) {
                    if (i < 2) {
                        cur.showStart = 0;
                        break;
                    }
                    if (cur.data[i - 2] - dx < -mGrid.xRank.size || i == 0) {
                        cur.showStart = i;
                        break;
                    }
                    cur.dataQueue.addFirst(new float[] { (float) cur.data[i - 2], (float) cur.data[i - 1] });
                    mPaint.setStrokeWidth(10);
                    Log.e(TAG, "THe point 's x is:" + (cur.data[i - 2] - dx));
                    i--;
                }
                while (!cur.dataQueue.isEmpty()) {
                    if (cur.dataQueue.getLast()[0] - dx > mGrid.xRank.len + mGrid.xRank.size) {
                        cur.dataQueue.removeLast();
                        cur.showEnd -= 2;
                    } else {
                        break;
                    }
                }
            }
        } else if (dxoffset > 0) {
            for (Curve cur : mCurves) {
                Log.v(TAG, "drawNewPart:The data's length is:" + cur.data.length);
                for (int i = cur.showEnd; i < cur.data.length; i++) {
                    Log.e(TAG, "The x of the data is:" + (cur.data[i] - dx));
                    if (cur.data[i] - dx > mGrid.xRank.len + mGrid.xRank.size || i == cur.data.length - 1) {
                        Log.v(TAG, "drawNewPart:The showEnd is:" + i);
                        cur.showEnd = i;
                        break;
                    }
                    Log.v(TAG, "toLeft:The x is:" + (cur.data[i] + dx) + ";the y is:" + cur.data[i + 1]);
                    cur.dataQueue.add(new float[] { (float) cur.data[i], (float) cur.data[i + 1] });
                    mPaint.setStrokeWidth(10);
                    i++;
                }
                while (!cur.dataQueue.isEmpty()) {
                    if (cur.dataQueue.getFirst()[0] - dx < -mGrid.xRank.size) {
                        cur.dataQueue.removeFirst();
                        cur.showStart += 2;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void DrawAxis(Canvas canvas) {
        Log.v(TAG, "The scale is:" + mGrid.yRank.scale);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(HALF_AXIX_WIDTH << 1);
        canvas.drawLine(mMargin, mGrid.yRank.len + mMargin - HALF_AXIX_WIDTH, mGrid.xRank.len + mMargin, mGrid.yRank.len + mMargin - HALF_AXIX_WIDTH, mPaint);
        canvas.drawLine(mMargin, mMargin, mMargin, mGrid.yRank.len + mMargin, mPaint);
        if (mConfig.isSupportDoubleCoordinate) {
            canvas.drawLine(mGrid.xRank.len + mMargin - HALF_AXIX_WIDTH, mMargin, mGrid.xRank.len + mMargin - HALF_AXIX_WIDTH, mGrid.yRank.len + mMargin, mPaint);
        }
        canvas.save();
        canvas.translate(0, mMargin);
        mAxisYLeft.doDraw(canvas, mGrid.yRank);
        if (mConfig.isSupportDoubleCoordinate) {
            mAxisYRight.doDraw(canvas, mGrid.yRank);
        }
        mAxisX.doDraw(canvas, mGrid.xRank);
        canvas.restore();
    }

    private void drawGrid(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mGrid.getRect());
        canvas.translate(mMargin, mMargin);
        canvas.drawColor(Color.WHITE);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(0);
        Log.v(TAG, "The yRank.len is:" + mGrid.yRank.len);
        for (int i = 0; i < mGrid.yRank.count; i++) {
            canvas.drawLine(0, mGrid.yRank.len - (i * mGrid.yRank.size + mGrid.yRank.offset), mGrid.xRank.len, mGrid.yRank.len - (i * mGrid.yRank.size + mGrid.yRank.offset), mPaint);
        }
        for (int i = 0; i < mGrid.xRank.count; i++) {
            canvas.drawLine(i * mGrid.xRank.size + mGrid.xRank.offset, 0, i * mGrid.xRank.size + mGrid.xRank.offset, mGrid.yRank.len, mPaint);
        }
        canvas.restore();
    }

    private void dataConversionAll(Curve cur) {
        cur.isStartConversionAll = true;
        for (int i = cur.showEnd + 2; i < cur.data.length; i++) {
            cur.data[i] = mAxisX.dataConversion(cur.data[i], mGrid.xRank);
            i++;
        }
        if (cur.yAxlxType == Curve.TYPE_LEFT) {
            for (int i = cur.showEnd + 1; i < cur.data.length; i++) {
                cur.data[i] = mAxisYLeft.dataConversion(cur.data[i], mGrid.yRank);
                i++;
            }
        } else {
            for (int i = cur.showEnd + 1; i < cur.data.length; i++) {
                cur.data[i] = mAxisYRight.dataConversion(cur.data[i], mGrid.yRank);
                i++;
            }
        }
        for (int i = 0; i < cur.data.length; i++) {
            Log.v(TAG, "The data of the cur is:" + cur.data[i] + ":" + cur.data[++i]);
        }
    }

    private void datasConversionToShow(Curve cur) {
        cur.isStartConversion = true;
        cur.showStart = 0;
        cur.showEnd = cur.data.length;
        for (int i = 0; i < cur.data.length; i++) {
            cur.data[i] = mAxisX.dataConversion(cur.data[i], mGrid.xRank);
            if (cur.data[i] > mGrid.xRank.len + mGrid.xRank.size) {
                cur.showEnd = i;
                break;
            }
            i++;
        }
        if (cur.yAxlxType == Curve.TYPE_LEFT) {
            for (int i = 1; i < cur.showEnd; i++) {
                cur.data[i] = mAxisYLeft.dataConversion(cur.data[i], mGrid.yRank);
                i++;
            }
        } else {
            for (int i = 1; i < cur.showEnd; i++) {
                cur.data[i] = mAxisYRight.dataConversion(cur.data[i], mGrid.yRank);
                i++;
            }
        }
        for (int i = 0; i < cur.showEnd; i++) {
            Log.v(TAG, "show:" + cur.data[i]);
            cur.dataQueue.add(new float[] { (float) cur.data[i], (float) cur.data[++i] });
        }
        cur.isPreOver = true;
    }

    public class AxisTime extends Axis {

        private static final int UNITS = 60;

        public AxisTime() {
            skip = 4;
        }

        public void setCoordinate(double startCoordinate, double endCoordinate) {
            startValue = startCoordinate;
            length = endCoordinate - startCoordinate;
        }

        @Override
        public void doDraw(Canvas canvas, Ranks rank) {
            Log.v(TAG, "The graduateion is" + graduation);
            long gra = (long) (graduation * rank.scaleFact);
            if (gra < UNITS) {
                format = new SimpleDateFormat("ss秒SSS");
            } else if (gra < 1000 * UNITS) {
                format = new SimpleDateFormat("mm分ss");
            } else if (gra < 1000 * UNITS * UNITS * UNITS) {
                format = new SimpleDateFormat("HH时mm");
            } else if (gra < 1000 * UNITS * UNITS * UNITS * 24) {
                format = new SimpleDateFormat("dd日HH");
            } else if (gra < 1000 * UNITS * UNITS * UNITS * 24 * 365) {
                format = new SimpleDateFormat("MM月dd");
            } else {
                format = new SimpleDateFormat("yy年MM");
            }
            mPaint.setColor(Color.BLUE);
            for (int i = 0; i < rank.count - 1; i++) {
                Log.v(TAG, "The x is:" + (startValue + (rank.start + i) * graduation * rank.scaleFact) + "The x is:" + String.valueOf(format.format(new Date((long) (startValue + (rank.start + i) * graduation * rank.scaleFact)))));
                if ((i % skip + rank.start % skip) % skip == 0) {
                    canvas.drawPoint(x + i * rank.size + rank.offset, mGrid.yRank.len - 2, mPaint);
                    canvas.drawText(String.valueOf(format.format(new Date((long) (startValue + (rank.start + i) * graduation * rank.scaleFact)))), x + i * rank.size + rank.offset, y, mPaint);
                }
            }
        }
    }

    public abstract class AxisLinear extends Axis {

        public void setCoordinate(double startCoordinate, double endCoordinate) {
            startValue = startCoordinate;
            length = endCoordinate - startCoordinate;
        }

        public AxisLinear() {
            format = new DecimalFormat("0");
        }
    }

    private class AxisLinearY extends AxisLinear {

        @Override
        public void doDraw(Canvas canvas, Ranks rank) {
            for (int i = 0; i < rank.count; i++) {
                canvas.drawText(String.valueOf(format.format(startValue + (rank.start + i) * graduation * rank.scaleFact)), x, y + rank.len - (i * rank.size + rank.offset), mPaint);
            }
        }
    }

    public class AxisLinearX extends AxisLinear {

        public AxisLinearX() {
            skip = 2;
        }

        @Override
        public void doDraw(Canvas canvas, Ranks rank) {
            mPaint.setColor(Color.BLUE);
            for (int i = 0; i < rank.count - 1; i++) {
                if (i % skip == Math.abs(rank.start) % skip) {
                    canvas.drawPoint(x + i * rank.size + rank.offset, mGrid.yRank.len - 2, mPaint);
                    canvas.drawText(String.valueOf(format.format(startValue + (rank.start + i) * graduation * rank.scaleFact)), x + i * rank.size + rank.offset, y, mPaint);
                }
            }
        }
    }

    private class Grid {

        public Ranks yRank;

        public Ranks xRank;

        public Grid() {
            xRank = new Ranks();
            yRank = new Ranks();
        }

        public Rect getRect() {
            Rect tmpRect = new Rect((int) mMargin, (int) mMargin, (int) (xRank.len + mMargin), (int) (yRank.len + mMargin));
            return tmpRect;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.v(TAG, "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.v(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.v(TAG, "onFling");
        Log.v(TAG, "The vx is:" + velocityX);
        sliding(velocityX, velocityY);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(TAG, "on key down");
        if (keyCode == KeyEvent.KEYCODE_Q) {
            scale(0, 0, 0, (int) (15 * mDensity));
        } else if (keyCode == KeyEvent.KEYCODE_W) {
            scale(0, 0, 0, (int) (-15 * mDensity));
        }
        return super.onKeyDown(keyCode, event);
    }
}
