package com.fanfq.livewallpaper.esdd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class Block extends ESDDAnimal {

    private static final int TOTAL_FRAMES_IN_SPRITE = 2;

    private static final int CLOWN_FISH_FPS = 2;

    private Point point;

    private boolean isFlagged;

    private int mColor;

    public Block(Context context, ESDD esdd, Point point) {
        super(context, esdd);
        createBlock(context, esdd, point);
    }

    public void createBlock(Context context, ESDD esdd, Point point) {
        this.point = point;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Config.BLOCK_STYLE == 20) {
            options.inSampleSize = 2;
        }
        options.inPurgeable = true;
        setFlagged(false);
        Bitmap mBitmap = null;
        switch((int) (Math.random() * 4)) {
            case 0:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.o11, options);
                this.mColor = 0;
                break;
            case 1:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.g11, options);
                this.mColor = 1;
                break;
            case 2:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.r11, options);
                this.mColor = 2;
                break;
            case 3:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.y11, options);
                this.mColor = 3;
                break;
        }
        this.initialize(mBitmap, point);
    }

    public void moveBlock(Context context, ESDD esdd, Point point, int index) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Config.BLOCK_STYLE == 20) {
            options.inSampleSize = 2;
        }
        options.inPurgeable = true;
        setFlagged(false);
        Bitmap mBitmap = null;
        switch(index) {
            case 0:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.o11, options);
                this.mColor = 0;
                break;
            case 1:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.g11, options);
                this.mColor = 1;
                break;
            case 2:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.r11, options);
                this.mColor = 2;
                break;
            case 3:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.y11, options);
                this.mColor = 3;
                break;
            case 4:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.block, options);
                this.mColor = 4;
                break;
        }
        this.initialize(mBitmap, point);
    }

    public void openBlock(Context context, ESDD esdd, Point point) {
        this.setFlagged(true);
        Bitmap mBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Config.BLOCK_STYLE == 20) {
            options.inSampleSize = 2;
        }
        switch(this.mColor) {
            case 0:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.o22, options);
                break;
            case 1:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.g22, options);
                break;
            case 2:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.r22, options);
                break;
            case 3:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.y22, options);
                break;
            case 4:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.block, options);
                break;
        }
        this.initialize(mBitmap, point);
    }

    public void backBlock(Context context, ESDD esdd, Point point) {
        this.setFlagged(false);
        Bitmap mBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Config.BLOCK_STYLE == 20) {
            options.inSampleSize = 2;
        }
        switch(this.mColor) {
            case 0:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.o11, options);
                break;
            case 1:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.g11, options);
                break;
            case 2:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.r11, options);
                break;
            case 3:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.y11, options);
                break;
            case 4:
                mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.block, options);
                break;
        }
        this.initialize(mBitmap, point);
    }

    public void deleteBlock(Context context, ESDD esdd, Point point) {
        this.setFlagged(false);
        this.mColor = 4;
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.block, options);
        this.initialize(mBitmap, point);
    }

    public void setFlagged(boolean b) {
        isFlagged = b;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int getColor() {
        return mColor;
    }

    public Point getPiont() {
        return this.point;
    }

    public void render(Canvas canvas) {
        super.render(canvas);
    }
}
