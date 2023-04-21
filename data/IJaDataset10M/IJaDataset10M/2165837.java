package com.track.views;

import java.util.ArrayList;
import com.track.mapview;
import com.track.select;
import com.track.mapping.handlers.MapUIThreadHandler;
import com.track.mapping.tiles.BackgroundTile;
import com.track.mapping.tiles.Tiles;
import com.track.threads.SurfaceViewThread;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MapTileView extends SurfaceView implements SurfaceHolder.Callback {

    public static MapTileView instance;

    private static SurfaceViewThread mThread;

    public static SurfaceHolder mHolder;

    private static Handler mHandler;

    private Tiles tileEngine = new Tiles();

    private static final int BACKGROUND_COLOR = 0xFF000000;

    private static Shader mBackgroundShader;

    private static Paint mBackgroundPaint;

    private static final int TILE_SIZE = 256;

    private static ArrayList<BackgroundTile> mBackgroundTiles;

    private static int mZoom = 10;

    private static Matrix mMatrix;

    private static float[] nInitValues = { 0, 0, TILE_SIZE, TILE_SIZE, 0, 0, 0, 0, 0, 0 };

    private static Matrix mHistoryMatrix;

    private static final RectF WINDOW_BOUNDS = new RectF(0, 0, 300, 500);

    private float canvasX = 0;

    private float canvasY = 0;

    private float[] velocity = { 0, 0, 0, 0 };

    private Bitmap mBitmap;

    private Bitmap[] mBitmapSet;

    private static int bWH = 256;

    private Boolean isNavigable = false;

    SurfaceHolder holder;

    public static final int MAP_UI_REST = 0;

    public static final int MAP_UI_MOVING = 1;

    public static final int MAP_UI_ERROR = -1;

    public MapTileView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

            @Override
            public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
                return super.sendMessageAtTime(msg, uptimeMillis);
            }
        };
        mMatrix = new Matrix();
        mMatrix.setValues(nInitValues);
        mHistoryMatrix = new Matrix();
        mHistoryMatrix.setValues(nInitValues);
        mBackgroundShader = new BitmapShader(makeBitmapGrid(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setShader(mBackgroundShader);
        mThread = new SurfaceViewThread(context, mHolder, mHandler, this);
    }

    protected final Bitmap makeBitmapGrid() {
        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.argb(255, 255, 200, 200));
        c.drawRect(0, 0, 255, 255, p);
        return bm;
    }

    protected final Paint makeBackgroundTiles() {
        mBackgroundPaint.getShader().setLocalMatrix(mMatrix);
        return mBackgroundPaint;
    }

    public static MapTileView getInstance() {
        return instance;
    }

    /**
     * 
     *  this is a method reachable from the outside to refresh the map canvas
     *  no changes
     */
    public void updateMap(Canvas c) {
        synchronized (mHolder) {
            draw(c);
        }
    }

    public void drawMapTiles() {
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);
        canvas.drawPaint(makeBackgroundTiles());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        Boolean mCurDown = action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE;
        int N = event.getHistorySize();
        if (mCurDown) {
            for (int i = 0; i < N; i++) {
                mMatrix.preConcat(mHistoryMatrix);
                mMatrix.setTranslate(event.getHistoricalX(i) + canvasX, event.getHistoricalY(i) + canvasY);
            }
            mMatrix.preConcat(mHistoryMatrix);
            mMatrix.setTranslate(event.getX(), event.getY());
            canvasX = event.getX();
            canvasY = event.getY();
        } else {
        }
        recalculateMap();
        return true;
    }

    /**
     * 
     *  attempt to determine the center of the application window and reload the needed tiles to create a map
     */
    private final void recalculateMap() {
        double lat = 40.684628;
        double lon = -73.957658;
        Log.d("TILES", "http://tile.openstreetmap.org/" + getTileNumber(lat, lon) + ".png");
    }

    public static String getTileNumber(final double lat, final double lon) {
        int xtile = (int) Math.floor((lon + 180) / 360 * (1 << mZoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << mZoom));
        return ("" + mZoom + "/" + xtile + "/" + ytile);
    }

    ;

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mThread.mSetToRun = true;
        mThread.runThread();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}

;
