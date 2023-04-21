package org.jfge.android.graphics;

import java.io.IOException;
import java.util.logging.Logger;
import org.jfge.android.controller.AndroidKeyboardController;
import org.jfge.spi.controller.Controller;
import org.jfge.spi.graphics.Graphics;
import org.jfge.spi.graphics.GraphicsProvider;
import org.jfge.spi.graphics.GraphicsFactory;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * The Class AndroidGraphicsProvider.
 */
@Singleton
public class AndroidGraphicsProvider extends SurfaceView implements GraphicsProvider, SurfaceHolder.Callback {

    /** The logger. */
    private final Logger logger;

    /** The db enabled. */
    private final boolean dbEnabled;

    /** The db image. */
    private Bitmap dbImage;

    /** The graphics. */
    private Graphics graphics;

    /** The paint. */
    private Paint paint;

    /** The controller. */
    private Controller controller;

    /** The matrix. */
    private Matrix matrix;

    /**
	 * Instantiates a new android graphics provider.
	 *
	 * @param logger the logger
	 * @param activity the activity
	 * @param controller the controller
	 * @param height the height
	 * @param width the width
	 * @param dbEnabled the db enabled
	 * @param imageLoader the image loader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Inject
    public AndroidGraphicsProvider(Logger logger, Activity activity, @Named("keyboard.android") Controller controller, @Named("engine.height") int height, @Named("engine.width") int width, @Named("engine.db") boolean dbEnabled, GraphicsFactory imageLoader) throws IOException {
        super(activity);
        this.logger = logger;
        this.dbEnabled = dbEnabled;
        this.controller = controller;
        this.matrix = new Matrix();
        this.dbImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.graphics = new AndroidGraphics(new Canvas(dbImage));
        activity.setContentView(this);
        this.setKeepScreenOn(true);
        getHolder().addCallback(this);
        this.setFocusable(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return ((AndroidKeyboardController) this.controller).onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return ((AndroidKeyboardController) this.controller).onKeyUp(keyCode, event);
    }

    @Override
    public void draw() {
        Canvas c = null;
        try {
            c = this.getHolder().lockCanvas();
            if (c != null) {
                c.drawBitmap(dbImage, matrix, paint);
            }
        } finally {
            if (c != null) {
                this.getHolder().unlockCanvasAndPost(c);
            }
        }
    }

    @Override
    public Graphics getGraphics() {
        return this.graphics;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        logger.info("created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Matrix matrix = new Matrix();
        if (height > width) {
            float scaleFactor = Math.min(((float) width / dbImage.getWidth()), ((float) height) / dbImage.getHeight());
            float translateFactor = (height - scaleFactor * dbImage.getHeight()) / 2;
            matrix.postScale(scaleFactor, scaleFactor);
            matrix.postTranslate(0, translateFactor);
        } else {
            matrix.postScale(((float) width) / dbImage.getWidth(), ((float) height) / dbImage.getHeight());
        }
        this.matrix = matrix;
        logger.info("changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        logger.info("destroyed");
    }
}
