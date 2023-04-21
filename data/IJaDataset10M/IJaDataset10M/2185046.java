package org.crossmobile.ios2a;

import android.app.Activity;
import org.crossmobile.ios2a.transf.RichTransformation;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import org.xmlvm.iphone.CGAffineTransform;
import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.CGSize;
import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UIScreen;
import org.crossmobile.ios2a.transf.IOSAnimation;

public class IOSView extends ViewGroup implements IOSChild {

    public static final float STATUSBAR_HEIGHT = 20;

    private static int androidBar;

    private static float xratio;

    private static float yratio;

    public static float getScale() {
        return (xratio + yratio) / 2;
    }

    private RichTransformation transformation = null;

    private float deltaY = 0;

    public static void updateBarMetrics(Activity activity) {
        Rect rectgle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
        androidBar = rectgle.top;
    }

    public static void updateRatios() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) MainActivity.current.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        CGRect appframe = UIScreen.mainScreen().getApplicationFrame();
        xratio = (float) metrics.widthPixels / appframe.size.width;
        yratio = (float) (metrics.heightPixels - (UIApplication.sharedApplication().isStatusBarHidden() ? 0 : androidBar)) / appframe.size.height;
    }

    public IOSView(Context ctx) {
        super(ctx);
        setStaticTransformationsEnabled(true);
    }

    public float getAlpha() {
        return transformation == null ? 1 : transformation.getAlpha();
    }

    public void setAlpha(float alpha) {
        transformation = RichTransformation.setAlpha(transformation, alpha);
        invalidate();
    }

    public CGAffineTransform getTransform() {
        return transformation == null ? CGAffineTransform.identity() : transformation.getTransformation();
    }

    public void setTransform(CGAffineTransform transform) {
        transformation = RichTransformation.setTransform(transformation, transform);
        invalidate();
    }

    /** 
     * This method is a call back for animations, so that they can set the parameters,
     * to be ready to redraw when the animation finishes.
     * This method is called just when committing animations, before any animation is performed.
     * No invalidate() is necessary, since an animation is pending.
     * @param params The actual IOSAnimation animation
     */
    public void setTransformationParameters(IOSAnimation params) {
        transformation = RichTransformation.setParameters(params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int howmany = getChildCount();
        for (int i = 0; i < howmany; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                IOSView.LayoutParams ipar = (IOSView.LayoutParams) child.getLayoutParams();
                child.layout((int) (ipar.xAndr + 0.5f), (int) (ipar.yAndr - deltaY + 0.5f), (int) (ipar.xAndr + ipar.wAndr + 0.5f), (int) (ipar.yAndr - deltaY + ipar.hAndr + 0.5f));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;
                IOSView.LayoutParams lp = (IOSView.LayoutParams) child.getLayoutParams();
                childRight = (int) (lp.xAndr + child.getMeasuredWidth() + 0.5f);
                childBottom = (int) (lp.yAndr + child.getMeasuredHeight() + 0.5f);
                maxWidth = Math.max(Math.max(maxWidth, childRight), (int) (lp.wAndr + 0.5f));
                maxHeight = Math.max(Math.max(maxHeight, childBottom), (int) (lp.hAndr + 0.5f));
            }
        }
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    Transformation childTransformation = new Transformation();

    RectF invalidateRegion = new RectF();

    public static void getInvalidateRegion(int left, int top, int right, int bottom, RectF invalidate, Transformation transformation) {
        invalidate.set(left, top, right, bottom);
        transformation.getMatrix().mapRect(invalidate);
        invalidate.inset(-1.0f, -1.0f);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = false;
        final int cl = child.getLeft();
        final int ct = child.getTop();
        final int cr = child.getRight();
        final int cb = child.getBottom();
        childTransformation.clear();
        Transformation transformToApply = null;
        final IOSAnimation a = (IOSAnimation) child.getAnimation();
        boolean concatMatrix = false;
        if (a != null) {
            final RectF region = invalidateRegion;
            final boolean initialized = a.isInitialized();
            if (!initialized) a.initialize(cr - cl, cb - ct, getWidth(), getHeight());
            more = a.getTransformation(drawingTime, childTransformation);
            transformToApply = childTransformation;
            concatMatrix = a.willChangeTransformationMatrix();
            if (more) if (!a.willChangeBounds()) invalidate(cl, ct, cr, cb); else {
                getInvalidateRegion(0, 0, cr - cl, cb - ct, region, transformToApply);
                final int left = cl + (int) region.left;
                final int top = ct + (int) region.top;
                invalidate(left, top, left + (int) region.width(), top + (int) region.height());
            }
        } else {
            final boolean hasTransform = getChildStaticTransformation(child, childTransformation);
            if (hasTransform) {
                final int transformType = childTransformation.getTransformationType();
                transformToApply = transformType != Transformation.TYPE_IDENTITY ? childTransformation : null;
                concatMatrix = (transformType & Transformation.TYPE_MATRIX) != 0;
                final RectF region = invalidateRegion;
                getInvalidateRegion(0, 0, cr - cl, cb - ct, region, transformToApply);
                final int left = cl + (int) region.left;
                final int top = ct + (int) region.top;
                invalidate(left, top, left + (int) region.width(), top + (int) region.height());
            }
        }
        child.computeScroll();
        final int sx = child.getScrollX();
        final int sy = child.getScrollY();
        final int restoreTo = canvas.save();
        canvas.translate(cl - sx, ct - sy);
        float alpha = 1.0f;
        if (transformToApply != null) {
            if (concatMatrix) {
                int transX = -sx;
                int transY = -sy;
                canvas.translate(-transX, -transY);
                canvas.concat(transformToApply.getMatrix());
                canvas.translate(transX, transY);
            }
            alpha = transformToApply.getAlpha();
            if (alpha < 1.0f) {
                final int multipliedAlpha = (int) (255 * alpha);
                canvas.saveLayerAlpha(sx, sy, sx + cr - cl, sy + cb - ct, multipliedAlpha, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            }
        }
        child.draw(canvas);
        canvas.restoreToCount(restoreTo);
        if (a != null && !more) finishAnimatingView(child, a);
        return more;
    }

    private void finishAnimatingView(final View view, Animation animation) {
        if (animation != null && !animation.getFillAfter()) view.clearAnimation();
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new IOSView.LayoutParams(CGRect.Zero());
    }

    @Override
    public IOSView getIOSView() {
        return this;
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (!(child instanceof IOSView)) return super.getChildStaticTransformation(child, t);
        IOSView view = (IOSView) child;
        if (view.transformation == null) return false; else {
            t.set(view.transformation);
            return true;
        }
    }

    public void updateStatusBarDelta() {
        deltaY = UIApplication.sharedApplication().isStatusBarHidden() ? 0 : y2Android(STATUSBAR_HEIGHT);
    }

    public static class LayoutParams extends AbsListView.LayoutParams {

        private float xAndr;

        private float yAndr;

        private float wAndr;

        private float hAndr;

        public LayoutParams(CGRect frame) {
            this(x2Android(frame.origin.x), y2Android(frame.origin.y), x2Android(frame.size.width), y2Android(frame.size.height));
        }

        public LayoutParams(CGSize size) {
            this(0, 0, x2Android(size.width), y2Android(size.height));
        }

        private LayoutParams(float xAndr, float yAndr, float wAndr, float hAndr) {
            super((int) (wAndr + 0.9f), (int) (hAndr + 0.9f));
            this.xAndr = xAndr;
            this.yAndr = yAndr;
            this.wAndr = wAndr;
            this.hAndr = hAndr;
        }

        public CGRect getFrame() {
            return new CGRect(x2IOS(xAndr), y2IOS(yAndr), x2IOS(wAndr), y2IOS(hAndr));
        }

        @Override
        public String toString() {
            return "[x=" + x2IOS(xAndr) + " y=" + y2IOS(yAndr) + " width=" + x2IOS(wAndr) + " height=" + y2IOS(hAndr) + "]";
        }
    }

    public static float x2Android(float fromIOS) {
        return fromIOS * xratio;
    }

    public static float y2Android(float fromIOS) {
        return fromIOS * yratio;
    }

    public static float x2IOS(float fromAndroid) {
        return fromAndroid / xratio;
    }

    public static float y2IOS(float fromAndroid) {
        return fromAndroid / yratio;
    }

    public static float androidBarHeight() {
        return UIApplication.sharedApplication().getKeyWindow().xm_base().deltaY;
    }
}
