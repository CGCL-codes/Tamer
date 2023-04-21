package org.formaria.batik.ext.awt;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 * Provides the actual implementation for the LinearGradientPaint
 * This is where the pixel processing is done.
 * 
 * @author Nicholas Talian, Vincent Hardy, Jim Graham, Jerry Evans
 * @author <a href="mailto:vincent.hardy@eng.sun.com">Vincent Hardy</a>
 * @version $Id: LinearGradientPaintContext.java,v 1.2 2006/08/31 09:28:43 val Exp $
 * @see java.awt.PaintContext
 * @see java.awt.Paint
 * @see java.awt.GradientPaint
 */
final class LinearGradientPaintContext extends MultipleGradientPaintContext {

    /**
     * The following invariants are used to process the gradient value from 
     * a device space coordinate, (X, Y):
     * g(X, Y) = dgdX*X + dgdY*Y + gc
     */
    private float dgdX, dgdY, gc, pixSz;

    private static final int DEFAULT_IMPL = 1;

    private static final int ANTI_ALIAS_IMPL = 3;

    private int fillMethod;

    /** 
     * Constructor for LinearGradientPaintContext.
     *
     *  @param cm {@link ColorModel} that receives
     *  the <code>Paint</code> data. This is used only as a hint.
     *
     *  @param deviceBounds the device space bounding box of the 
     *  graphics primitive being rendered
     *
     *  @param userBounds the user space bounding box of the 
     *  graphics primitive being rendered
     * 
     *  @param t the {@link AffineTransform} from user
     *  space into device space (gradientTransform should be 
     *  concatenated with this)
     *
     *  @param hints the hints that the context object uses to choose
     *  between rendering alternatives
     *
     *  @param dStart gradient start point, in user space
     *
     *  @param dEnd gradient end point, in user space
     *
     *  @param fractions the fractions specifying the gradient distribution
     *
     *  @param colors the gradient colors
     *
     *  @param cycleMethod either NO_CYCLE, REFLECT, or REPEAT
     *
     *  @param colorSpace which colorspace to use for interpolation, 
     *  either SRGB or LINEAR_RGB
     *
     */
    public LinearGradientPaintContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform t, RenderingHints hints, Point2D dStart, Point2D dEnd, float[] fractions, Color[] colors, MultipleGradientPaint.CycleMethodEnum cycleMethod, MultipleGradientPaint.ColorSpaceEnum colorSpace) throws NoninvertibleTransformException {
        super(cm, deviceBounds, userBounds, t, hints, fractions, colors, cycleMethod, colorSpace);
        Point2D.Float start = new Point2D.Float((float) dStart.getX(), (float) dStart.getY());
        Point2D.Float end = new Point2D.Float((float) dEnd.getX(), (float) dEnd.getY());
        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float dSq = dx * dx + dy * dy;
        float constX = dx / dSq;
        float constY = dy / dSq;
        dgdX = a00 * constX + a10 * constY;
        dgdY = a01 * constX + a11 * constY;
        float dgdAbs = Math.abs(dgdX);
        float dgdYAbs = Math.abs(dgdY);
        if (dgdAbs > dgdYAbs) pixSz = dgdAbs; else pixSz = dgdYAbs;
        gc = (a02 - start.x) * constX + (a12 - start.y) * constY;
        Object colorRend = hints.get(RenderingHints.KEY_COLOR_RENDERING);
        Object rend = hints.get(RenderingHints.KEY_RENDERING);
        fillMethod = DEFAULT_IMPL;
        if ((cycleMethod == MultipleGradientPaint.REPEAT) || hasDiscontinuity) {
            if (rend == RenderingHints.VALUE_RENDER_QUALITY) fillMethod = ANTI_ALIAS_IMPL;
            if (colorRend == RenderingHints.VALUE_COLOR_RENDER_SPEED) fillMethod = DEFAULT_IMPL; else if (colorRend == RenderingHints.VALUE_COLOR_RENDER_QUALITY) fillMethod = ANTI_ALIAS_IMPL;
        }
    }

    protected void fillHardNoCycle(int[] pixels, int off, int adjust, int x, int y, int w, int h) {
        final float initConst = (dgdX * x) + gc;
        for (int i = 0; i < h; i++) {
            float g = initConst + dgdY * (y + i);
            final int rowLimit = off + w;
            if (dgdX == 0) {
                final int val;
                if (g <= 0) val = gradientUnderflow; else if (g >= 1) val = gradientOverflow; else {
                    int gradIdx = 0;
                    while (gradIdx < gradientsLength - 1) {
                        if (g < fractions[gradIdx + 1]) break;
                        gradIdx++;
                    }
                    float delta = (g - fractions[gradIdx]);
                    float idx = ((delta * GRADIENT_SIZE_INDEX) / normalizedIntervals[gradIdx]) + 0.5f;
                    val = gradients[gradIdx][(int) idx];
                }
                while (off < rowLimit) {
                    pixels[off++] = val;
                }
            } else {
                int gradSteps;
                int preGradSteps;
                final int preVal, postVal;
                float gradStepsF;
                float preGradStepsF;
                if (dgdX >= 0) {
                    gradStepsF = ((1 - g) / dgdX);
                    preGradStepsF = (float) Math.ceil((0 - g) / dgdX);
                    preVal = gradientUnderflow;
                    postVal = gradientOverflow;
                } else {
                    gradStepsF = ((0 - g) / dgdX);
                    preGradStepsF = (float) Math.ceil((1 - g) / dgdX);
                    preVal = gradientOverflow;
                    postVal = gradientUnderflow;
                }
                if (gradStepsF > w) gradSteps = w; else gradSteps = (int) gradStepsF;
                if (preGradStepsF > w) preGradSteps = w; else preGradSteps = (int) preGradStepsF;
                final int gradLimit = off + gradSteps;
                if (preGradSteps > 0) {
                    final int preGradLimit = off + preGradSteps;
                    while (off < preGradLimit) {
                        pixels[off++] = preVal;
                    }
                    g += dgdX * preGradSteps;
                }
                if (dgdX > 0) {
                    int gradIdx = 0;
                    while (gradIdx < gradientsLength - 1) {
                        if (g < fractions[gradIdx + 1]) break;
                        gradIdx++;
                    }
                    while (off < gradLimit) {
                        float delta = (g - fractions[gradIdx]);
                        final int[] grad = gradients[gradIdx];
                        double stepsD = Math.ceil((fractions[gradIdx + 1] - g) / dgdX);
                        int steps;
                        if (stepsD > w) steps = w; else steps = (int) stepsD;
                        int subGradLimit = off + steps;
                        if (subGradLimit > gradLimit) subGradLimit = gradLimit;
                        int idx = (int) (((delta * GRADIENT_SIZE_INDEX) / normalizedIntervals[gradIdx]) * (1 << 16)) + (1 << 15);
                        int step = (int) (((dgdX * GRADIENT_SIZE_INDEX) / normalizedIntervals[gradIdx]) * (1 << 16));
                        while (off < subGradLimit) {
                            pixels[off++] = grad[idx >> 16];
                            idx += step;
                        }
                        g += dgdX * stepsD;
                        gradIdx++;
                    }
                } else {
                    int gradIdx = gradientsLength - 1;
                    while (gradIdx > 0) {
                        if (g > fractions[gradIdx]) break;
                        gradIdx--;
                    }
                    while (off < gradLimit) {
                        float delta = (g - fractions[gradIdx]);
                        final int[] grad = gradients[gradIdx];
                        double stepsD = Math.ceil(delta / -dgdX);
                        int steps;
                        if (stepsD > w) steps = w; else steps = (int) stepsD;
                        int subGradLimit = off + steps;
                        if (subGradLimit > gradLimit) subGradLimit = gradLimit;
                        int idx = (int) (((delta * GRADIENT_SIZE_INDEX) / normalizedIntervals[gradIdx]) * (1 << 16)) + (1 << 15);
                        int step = (int) (((dgdX * GRADIENT_SIZE_INDEX) / normalizedIntervals[gradIdx]) * (1 << 16));
                        while (off < subGradLimit) {
                            pixels[off++] = grad[idx >> 16];
                            idx += step;
                        }
                        g += dgdX * stepsD;
                        gradIdx--;
                    }
                }
                while (off < rowLimit) {
                    pixels[off++] = postVal;
                }
            }
            off += adjust;
        }
    }

    protected void fillSimpleNoCycle(int[] pixels, int off, int adjust, int x, int y, int w, int h) {
        final float initConst = (dgdX * x) + gc;
        final float step = dgdX * fastGradientArraySize;
        final int fpStep = (int) (step * (1 << 16));
        final int[] grad = gradient;
        for (int i = 0; i < h; i++) {
            float g = initConst + dgdY * (y + i);
            g *= fastGradientArraySize;
            g += 0.5;
            final int rowLimit = off + w;
            float check = dgdX * fastGradientArraySize * w;
            if (check < 0) check = -check;
            if (check < .3) {
                final int val;
                if (g <= 0) val = gradientUnderflow; else if (g >= fastGradientArraySize) val = gradientOverflow; else val = grad[(int) g];
                while (off < rowLimit) {
                    pixels[off++] = val;
                }
            } else {
                int gradSteps;
                int preGradSteps;
                final int preVal, postVal;
                if (dgdX > 0) {
                    gradSteps = (int) ((fastGradientArraySize - g) / step);
                    preGradSteps = (int) Math.ceil(0 - g / step);
                    preVal = gradientUnderflow;
                    postVal = gradientOverflow;
                } else {
                    gradSteps = (int) ((0 - g) / step);
                    preGradSteps = (int) Math.ceil((fastGradientArraySize - g) / step);
                    preVal = gradientOverflow;
                    postVal = gradientUnderflow;
                }
                if (gradSteps > w) gradSteps = w;
                final int gradLimit = off + gradSteps;
                if (preGradSteps > 0) {
                    if (preGradSteps > w) preGradSteps = w;
                    final int preGradLimit = off + preGradSteps;
                    while (off < preGradLimit) {
                        pixels[off++] = preVal;
                    }
                    g += step * preGradSteps;
                }
                int fpG = (int) (g * (1 << 16));
                while (off < gradLimit) {
                    pixels[off++] = grad[fpG >> 16];
                    fpG += fpStep;
                }
                while (off < rowLimit) {
                    pixels[off++] = postVal;
                }
            }
            off += adjust;
        }
    }

    protected void fillSimpleRepeat(int[] pixels, int off, int adjust, int x, int y, int w, int h) {
        final float initConst = (dgdX * x) + gc;
        float step = (dgdX - (int) dgdX) * fastGradientArraySize;
        if (step < 0) step += fastGradientArraySize;
        final int[] grad = gradient;
        for (int i = 0; i < h; i++) {
            float g = initConst + dgdY * (y + i);
            g = g - (int) g;
            if (g < 0) g += 1;
            g *= fastGradientArraySize;
            g += 0.5;
            final int rowLimit = off + w;
            while (off < rowLimit) {
                int idx = (int) g;
                if (idx >= fastGradientArraySize) {
                    g -= fastGradientArraySize;
                    idx -= fastGradientArraySize;
                }
                pixels[off++] = grad[idx];
                g += step;
            }
            off += adjust;
        }
    }

    protected void fillSimpleReflect(int[] pixels, int off, int adjust, int x, int y, int w, int h) {
        final float initConst = (dgdX * x) + gc;
        final int[] grad = gradient;
        for (int i = 0; i < h; i++) {
            float g = initConst + dgdY * (y + i);
            g = g - 2 * ((int) (g / 2.0f));
            float step = dgdX;
            if (g < 0) {
                g = -g;
                step = -step;
            }
            step = step - 2 * ((int) step / 2.0f);
            if (step < 0) step += 2.0;
            final int reflectMax = 2 * fastGradientArraySize;
            g *= fastGradientArraySize;
            g += 0.5;
            step *= fastGradientArraySize;
            final int rowLimit = off + w;
            while (off < rowLimit) {
                int idx = (int) g;
                if (idx >= reflectMax) {
                    g -= reflectMax;
                    idx -= reflectMax;
                }
                if (idx <= fastGradientArraySize) pixels[off++] = grad[idx]; else pixels[off++] = grad[reflectMax - idx];
                g += step;
            }
            off += adjust;
        }
    }

    /**
     * Return a Raster containing the colors generated for the graphics
     * operation.  This is where the area is filled with colors distributed
     * linearly.
     *
     * @param x,y,w,h The area in device space for which colors are
     * generated.
     *
     */
    protected void fillRaster(int[] pixels, int off, int adjust, int x, int y, int w, int h) {
        final float initConst = (dgdX * x) + gc;
        if (fillMethod == ANTI_ALIAS_IMPL) {
            for (int i = 0; i < h; i++) {
                float g = initConst + dgdY * (y + i);
                final int rowLimit = off + w;
                while (off < rowLimit) {
                    pixels[off++] = indexGradientAntiAlias(g, pixSz);
                    g += dgdX;
                }
                off += adjust;
            }
        } else if (!isSimpleLookup) {
            if (cycleMethod == MultipleGradientPaint.NO_CYCLE) {
                fillHardNoCycle(pixels, off, adjust, x, y, w, h);
            } else {
                for (int i = 0; i < h; i++) {
                    float g = initConst + dgdY * (y + i);
                    final int rowLimit = off + w;
                    while (off < rowLimit) {
                        pixels[off++] = indexIntoGradientsArrays(g);
                        g += dgdX;
                    }
                    off += adjust;
                }
            }
        } else {
            if (cycleMethod == MultipleGradientPaint.NO_CYCLE) fillSimpleNoCycle(pixels, off, adjust, x, y, w, h); else if (cycleMethod == MultipleGradientPaint.REPEAT) fillSimpleRepeat(pixels, off, adjust, x, y, w, h); else fillSimpleReflect(pixels, off, adjust, x, y, w, h);
        }
    }
}
