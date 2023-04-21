package minh.app.mbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MBookView extends View {

    private static int DEFAULT_FLIP_VALUE = 20;

    private static int FLIP_SPEED = 30;

    private boolean isNextPage = true;

    private long mMoveDelay = 1000 / 30;

    private float xTouchValue = DEFAULT_FLIP_VALUE, yTouchValue = DEFAULT_FLIP_VALUE;

    class FlippingHandler extends Handler {

        public void handleMessage(Message msg) {
            MBookView.this.flip();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    private FlippingHandler flippingHandler;

    private Bitmap page[] = new Bitmap[3];

    private int width;

    private int height;

    private float oldTouchX, oldTouchY;

    private boolean flipping = false;

    private boolean next;

    private Point A, B, C, D, E, F;

    private Bitmap visiblePage;

    private Bitmap invisiblePage;

    private Paint flipPagePaint;

    private boolean flip = false;

    private Context context;

    private int loadedPages = 0;

    private long timeToLoad = 0;

    private boolean onloading = true;

    private boolean onMoving = false;

    public MBookView(Context context) {
        super(context);
        this.context = context;
        init();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public MBookView(Context context, int width, int height, int topPadding, int leftPadding, TextPaint myTextPaint) {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
        page[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.page1);
        page[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.page2);
        page[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.page3);
        init();
    }

    private void init() {
        flippingHandler = new FlippingHandler();
        flipPagePaint = new Paint();
        flipPagePaint.setColor(Color.TRANSPARENT);
        flipPagePaint.setShadowLayer(5, -5, 5, 0x44000000);
        A = new Point(10, 0);
        B = new Point(width, height);
        C = new Point(width, 0);
        D = new Point(0, 0);
        E = new Point(0, 0);
        F = new Point(0, 0);
        xTouchValue = DEFAULT_FLIP_VALUE;
        yTouchValue = DEFAULT_FLIP_VALUE;
        visiblePage = page[0];
        invisiblePage = page[1];
        onMoving = false;
        flipping = false;
        loadData();
    }

    private void loadData() {
        onloading = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!onloading) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldTouchX = event.getX();
                oldTouchY = event.getY();
                flip = true;
                if (oldTouchX > (width >> 1)) {
                    xTouchValue = DEFAULT_FLIP_VALUE;
                    yTouchValue = DEFAULT_FLIP_VALUE;
                    next = true;
                } else {
                    next = false;
                    xTouchValue = width;
                    yTouchValue = DEFAULT_FLIP_VALUE;
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (onMoving) {
                    xTouchValue = width - A.x;
                    onMoving = false;
                }
                flipping = true;
                flip();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                onMoving = true;
                float xMouse = event.getX();
                float yMouse = event.getY();
                xTouchValue -= (xMouse - oldTouchX) / 1;
                yTouchValue -= yMouse - oldTouchY;
                if (xMouse < oldTouchX) {
                    if (!next) {
                        flip = false;
                    }
                    next = true;
                } else {
                    if (next) {
                        flip = false;
                    }
                    next = false;
                }
                oldTouchX = event.getX();
                oldTouchY = event.getY();
                this.invalidate();
            }
        }
        return true;
    }

    public void flip() {
        if (flipping) {
            if (xTouchValue > width || xTouchValue < DEFAULT_FLIP_VALUE) {
                flipping = false;
                if (!flipping) {
                    if (next) {
                        Log.i("aaa", "day1");
                        swap2PageNext();
                    } else {
                        Log.i("aaa", "day2");
                        swap2PagePre();
                    }
                    flip = false;
                    xTouchValue = DEFAULT_FLIP_VALUE;
                    yTouchValue = DEFAULT_FLIP_VALUE;
                }
                return;
            }
            if (next) {
                xTouchValue += FLIP_SPEED;
            } else {
                xTouchValue -= FLIP_SPEED;
            }
            this.invalidate();
            flippingHandler.sleep(mMoveDelay);
        }
    }

    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        if (flipping) {
            pointGenerate(xTouchValue, width, height);
        } else {
            pointGenerate(xTouchValue, width, height);
        }
        Paint paint = new Paint();
        canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(visiblePage, 0, 0, paint);
        Path pathX = pathOfTheMask();
        canvas.clipPath(pathX);
        canvas.drawBitmap(invisiblePage, 0, 0, paint);
        canvas.restore();
        Path pathX2 = pathOfFlippedPaper();
        canvas.drawPath(pathX2, flipPagePaint);
        pathX = null;
        pathX2 = null;
        paint = null;
    }

    private Path pathOfTheMask() {
        Path path = new Path();
        path.moveTo(A.x, A.y);
        path.lineTo(B.x, B.y);
        path.lineTo(C.x, C.y);
        path.lineTo(D.x, D.y);
        path.lineTo(A.x, A.y);
        return path;
    }

    private Path pathOfFlippedPaper() {
        Path path = new Path();
        path.moveTo(A.x, A.y);
        path.lineTo(D.x, D.y);
        path.lineTo(E.x, E.y);
        path.lineTo(F.x, F.y);
        path.lineTo(A.x, A.y);
        return path;
    }

    private void pointGenerate(float distance, int width, int height) {
        float xA = width - distance;
        float yA = height;
        float xD = 0;
        float yD = 0;
        if (xA > width / 2) {
            xD = width;
            yD = height - (width - xA) * height / xA;
        } else {
            xD = 2 * xA;
            yD = 0;
        }
        double a = (height - yD) / (xD + distance - width);
        double alpha = Math.atan(a);
        double _cos = Math.cos(2 * alpha), _sin = Math.sin(2 * alpha);
        float xE = (float) (xD + _cos * (width - xD));
        float yE = (float) -(_sin * (width - xD));
        float xF = (float) (width - distance + _cos * distance);
        float yF = (float) (height - _sin * distance);
        if (xA > width / 2) {
            xE = xD;
            yE = yD;
        }
        A.x = xA;
        A.y = yA;
        D.x = xD;
        D.y = yD;
        E.x = xE;
        E.y = yE;
        F.x = xF;
        F.y = yF;
    }

    float oldxF = 0, oldyF = 0;

    private void pointGenerateII(float xTouch, float yTouch, int width, int height) {
        float yA = height;
        float xD = width;
        float xF = width - xTouch + 0.1f;
        float yF = height - yTouch + 0.1f;
        if (A.x == 0) {
            xF = Math.min(xF, oldxF);
            yF = Math.max(yF, oldyF);
        }
        float deltaX = width - xF;
        float deltaY = height - yF;
        float BH = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY) / 2);
        double tangAlpha = deltaY / deltaX;
        double alpha = Math.atan(tangAlpha);
        double _cos = Math.cos(alpha), _sin = Math.sin(alpha);
        float xA = (float) (width - (BH / _cos));
        float yD = (float) (height - (BH / _sin));
        xA = Math.max(0, xA);
        if (xA == 0) {
            oldxF = xF;
            oldyF = yF;
        }
        float xE = xD;
        float yE = yD;
        if (yD < 0) {
            xD = width + (float) (tangAlpha * yD);
            yE = 0;
            xE = width + (float) (Math.tan(2 * alpha) * yD);
        }
        A.x = xA;
        A.y = yA;
        D.x = xD;
        D.y = Math.max(0, yD);
        E.x = xE;
        E.y = yE;
        F.x = xF;
        F.y = yF;
    }

    private void swap2Page1() {
        Bitmap temp = visiblePage;
        visiblePage = invisiblePage;
        invisiblePage = temp;
        temp = null;
    }

    private void swap2PageNext() {
        Bitmap temp = visiblePage;
        int index = 0;
        if (visiblePage == page[0]) {
            invisiblePage = page[1];
            index = 1;
        } else if (visiblePage == page[1]) {
            index = 3;
            invisiblePage = page[2];
        } else if (visiblePage == page[2]) {
            index = 0;
            invisiblePage = page[0];
        }
        visiblePage = invisiblePage;
        try {
            invisiblePage = page[index + 1];
        } catch (Exception e) {
        }
        temp = null;
    }

    private void swap2PagePre() {
        Bitmap temp = visiblePage;
        int index = 0;
        if (visiblePage == page[2]) {
            invisiblePage = page[1];
            index = 1;
        } else if (visiblePage == page[1]) {
            invisiblePage = page[0];
            index = 0;
        } else if (invisiblePage == page[0]) {
            invisiblePage = page[2];
            index = 2;
        }
        try {
            invisiblePage = page[index - 1];
        } catch (Exception e) {
            invisiblePage = page[2];
        }
        temp = null;
    }
}
