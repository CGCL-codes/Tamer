package rockchip.graphics;

import android.graphics.drawable.Drawable;
import android.graphics.PixelFormat;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;

class FastBitmapDrawable extends Drawable {

    private Bitmap mBitmap;

    FastBitmapDrawable(Bitmap b) {
        mBitmap = b;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0.0f, 0.0f, null);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public int getMinimumWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getMinimumHeight() {
        return mBitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
