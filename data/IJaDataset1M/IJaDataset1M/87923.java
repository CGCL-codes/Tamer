package com.sen.imageslider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

public class SlideShowEffect {

    private CustomImageView view;

    public static final int SPEED = 400;

    public static final int WIDTH_BETWEEN_IMAGES = 20;

    public static int DISPLAY_HEIGHT;

    public static int DISPLAY_WIDTH;

    private ImageContainer images;

    private Bitmap preparedNextImage, preparedPrevImage;

    private Paint paint = new Paint();

    private CustomScroller mScroller;

    private boolean isLoaded = true;

    private boolean needToPrepareNext;

    private boolean needToPreparePrev;

    public SlideShowEffect(Context context, ImageContainer images, CustomImageView view) {
        this.images = images;
        this.view = view;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DISPLAY_WIDTH = display.getWidth();
        DISPLAY_HEIGHT = display.getHeight();
        mScroller = new CustomScroller(context);
        mScroller.extendDuration(SPEED);
        needToPrepareNext = true;
        needToPreparePrev = true;
    }

    public void toLeft(int fromXDelta) {
        if (images.isFirst() != true) {
            if (!mScroller.isFinished()) mScroller.abortAnimation();
            mScroller.setOnEndListner(new Runnable() {

                public void run() {
                    view.setImageBitmap(images.getPrevImage());
                    view.setOffset(0);
                    needToPreparePrev = true;
                    needToPrepareNext = true;
                    view.updateRender();
                }
            });
            mScroller.startScroll(fromXDelta, 0, (DISPLAY_WIDTH + WIDTH_BETWEEN_IMAGES), 0);
            checkScroll();
        } else toBack(fromXDelta);
    }

    public void toRight(int fromXDelta) {
        if (images.isLast() != true) {
            if (!mScroller.isFinished()) mScroller.abortAnimation();
            mScroller.setOnEndListner(new Runnable() {

                public void run() {
                    view.setImageBitmap(images.getNextImage());
                    view.setOffset(0);
                    needToPreparePrev = true;
                    needToPrepareNext = true;
                    view.updateRender();
                }
            });
            mScroller.startScroll(-fromXDelta, 0, -(DISPLAY_WIDTH + WIDTH_BETWEEN_IMAGES), 0);
            checkScroll();
        } else toBack(-fromXDelta);
    }

    public void toBack(int fromXDelta) {
        abortAnimation();
        mScroller.startScroll(fromXDelta, 0, 0, 0);
        checkScroll();
    }

    public void moveTo(int delta) {
        view.setOffset(delta);
        view.invalidate();
    }

    public String getCurrentImagePath() {
        return images.getCurrentImagePath();
    }

    public void render(Bitmap scaledBitmap, float offset, Canvas canvas) {
        Bitmap bmp;
        boolean canProc = true;
        if (offset < 0) {
            if (preparedNextImage == null || needToPrepareNext) {
                if (getPreparedNextImage(scaledBitmap) == null) canProc = false;
            }
            bmp = preparedNextImage;
        } else {
            if (preparedPrevImage == null || needToPreparePrev) {
                if (getPreparedPrevImage(scaledBitmap) == null) canProc = false;
            }
            bmp = preparedPrevImage;
        }
        if (!canProc) {
            canvas.drawBitmap(scaledBitmap, (DISPLAY_WIDTH - scaledBitmap.getWidth()) / 2, (DISPLAY_HEIGHT - scaledBitmap.getHeight()) / 2, paint);
        } else {
            if (offset < 0) canvas.drawBitmap(bmp, offset, (DISPLAY_HEIGHT - bmp.getHeight()) / 2, paint); else canvas.drawBitmap(bmp, -(DISPLAY_WIDTH + WIDTH_BETWEEN_IMAGES) + offset, (DISPLAY_HEIGHT - bmp.getHeight()) / 2, paint);
        }
    }

    private Bitmap getPreparedNextImage(Bitmap scaledBitmap) {
        Bitmap nextBmp = null;
        needToPrepareNext = false;
        if (images.tryGetNextImage() != null) nextBmp = SlideShowEffect.this.view.getScaledImage(images.tryGetNextImage()); else {
            nextBmp = null;
            needToPrepareNext = true;
        }
        Bitmap bmp = Bitmap.createBitmap(DISPLAY_WIDTH * 2 + WIDTH_BETWEEN_IMAGES, nextBmp != null && scaledBitmap.getHeight() < nextBmp.getHeight() ? nextBmp.getHeight() : scaledBitmap.getHeight(), scaledBitmap.getConfig());
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(scaledBitmap, (DISPLAY_WIDTH - scaledBitmap.getWidth()) / 2, (bmp.getHeight() - scaledBitmap.getHeight()) / 2, paint);
        if (nextBmp != null) canvas.drawBitmap(nextBmp, DISPLAY_WIDTH + WIDTH_BETWEEN_IMAGES + (DISPLAY_WIDTH - nextBmp.getWidth()) / 2, (bmp.getHeight() - nextBmp.getHeight()) / 2, paint);
        if (preparedNextImage != null) {
            preparedNextImage.recycle();
        }
        preparedNextImage = bmp;
        return bmp;
    }

    private Bitmap getPreparedPrevImage(Bitmap scaledBitmap) {
        Bitmap prevBmp = null;
        needToPreparePrev = false;
        if (images.tryGetPrevImage() != null) prevBmp = SlideShowEffect.this.view.getScaledImage(images.tryGetPrevImage()); else {
            prevBmp = null;
            needToPreparePrev = true;
        }
        Bitmap bmp = Bitmap.createBitmap(DISPLAY_WIDTH * 2 + WIDTH_BETWEEN_IMAGES, prevBmp != null && scaledBitmap.getHeight() < prevBmp.getHeight() ? prevBmp.getHeight() : scaledBitmap.getHeight(), scaledBitmap.getConfig());
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(scaledBitmap, DISPLAY_WIDTH + WIDTH_BETWEEN_IMAGES + (DISPLAY_WIDTH - scaledBitmap.getWidth()) / 2, (bmp.getHeight() - scaledBitmap.getHeight()) / 2, paint);
        if (prevBmp != null) canvas.drawBitmap(prevBmp, (DISPLAY_WIDTH - prevBmp.getWidth()) / 2, (bmp.getHeight() - prevBmp.getHeight()) / 2, paint);
        if (preparedPrevImage != null) {
            preparedPrevImage.recycle();
        }
        preparedPrevImage = bmp;
        return bmp;
    }

    public void checkScroll() {
        if (mScroller.computeScrollOffset()) {
            isLoaded = false;
            int x = mScroller.getX();
            view.setOffset(x);
            view.invalidate();
        } else if (!isLoaded) {
            isLoaded = true;
            mScroller.onEnd();
        }
    }

    public void abortAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            mScroller.onEnd();
        }
    }

    public int getCurrentPosition() {
        return images.getCurrentPosition();
    }

    public void update() {
        needToPrepareNext = true;
        needToPreparePrev = true;
    }

    public void cleanAll() {
        if (preparedNextImage != null) {
            preparedNextImage.recycle();
            preparedNextImage = null;
        }
        if (preparedPrevImage != null) {
            preparedPrevImage.recycle();
            preparedPrevImage = null;
        }
    }

    public void updateCurrentImage(Bitmap scaledBitmap, Integer pos) {
        images.updateCurrentImage(scaledBitmap, pos);
    }
}
