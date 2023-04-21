package il.co.gadiworks.mrnom.framework.impl;

import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import il.co.gadiworks.mrnom.framework.Graphics;
import il.co.gadiworks.mrnom.framework.Pixmap;

public class AndroidGraphics implements Graphics {

    AssetManager assets;

    Bitmap frameBuffer;

    Canvas canvas;

    Paint paint;

    Rect srcRect = new Rect(), dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565) {
            config = Config.RGB_565;
        } else if (format == PixmapFormat.ARGB4444) {
            config = Config.ARGB_4444;
        } else {
            config = Config.ARGB_8888;
        }
        Options options = new Options();
        options.inPreferredConfig = config;
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = this.assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) {
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
            }
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap.getConfig() == Config.RGB_565) {
            format = PixmapFormat.RGB565;
        } else if (bitmap.getConfig() == Config.ARGB_4444) {
            format = PixmapFormat.ARGB4444;
        } else {
            format = PixmapFormat.ARGB8888;
        }
        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        this.canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        this.paint.setColor(color);
        this.canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        this.paint.setColor(color);
        this.canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        this.paint.setColor(color);
        this.paint.setStyle(Style.FILL);
        this.canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        this.srcRect.left = srcX;
        this.srcRect.top = srcY;
        this.srcRect.right = srcX + srcWidth - 1;
        this.srcRect.bottom = srcY + srcHeight - 1;
        this.dstRect.left = x;
        this.dstRect.top = y;
        this.dstRect.right = x + srcWidth - 1;
        this.dstRect.bottom = y + srcHeight - 1;
        this.canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, this.srcRect, this.dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        this.canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
    }

    @Override
    public int getWidth() {
        return this.frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return this.frameBuffer.getHeight();
    }
}
