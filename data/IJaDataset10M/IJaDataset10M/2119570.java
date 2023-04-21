package org.photovault.image;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderableOp;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.CropDescriptor;
import javax.media.jai.operator.RenderableDescriptor;
import org.testng.annotations.Test;

/**
     This is a test case for problem in crop boundary value rounding in JAI
     renderable image chain. See #224 for more information.
     */
public class Test_RenderableCrop {

    /** Creates a new instance of Test_RenderableCrop */
    public Test_RenderableCrop() {
    }

    @Test
    public void testRenderableCropBug() {
        RenderedOp src = ConstantDescriptor.create(2560.0f, 1920.0f, new Byte[] { (byte) 0x80, (byte) 0x80, (byte) 0x80 }, null);
        RenderableOp rSrc = RenderableDescriptor.createRenderable(src, null, 128, 0.0f, 0.0f, 1.0f, null);
        AffineTransform xform = getRotateXform(20.0f, rSrc.getWidth(), rSrc.getHeight());
        ParameterBlockJAI rotParams = new ParameterBlockJAI("affine");
        rotParams.addSource(rSrc);
        rotParams.setParameter("transform", xform);
        rotParams.setParameter("interpolation", Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        RenderingHints hints = new RenderingHints(null);
        hints.put(JAI.KEY_INTERPOLATION, new InterpolationBilinear());
        RenderableOp rotated = JAI.createRenderable("affine", rotParams, hints);
        ParameterBlockJAI cropParams = new ParameterBlockJAI("crop");
        cropParams.addSource(rotated);
        float cropX = 0.2f;
        float cropW = rotated.getMinX() + rotated.getWidth() - cropX;
        float cropY = 0.3f;
        float cropH = rotated.getMinY() + rotated.getHeight() - cropY;
        Rectangle2D.Float cropRect = new Rectangle2D.Float(cropX, cropY, cropW, cropH);
        Rectangle2D.Float srcRect = new Rectangle2D.Float(rotated.getMinX(), rotated.getMinY(), rotated.getWidth(), rotated.getHeight());
        if (!srcRect.contains(cropRect)) {
            Rectangle2D.intersect(srcRect, cropRect, cropRect);
            if (!srcRect.contains(cropRect)) {
                final float margin = 5E-3f;
                if (srcRect.getMinX() > cropRect.getMinX()) {
                    cropX += margin;
                }
                if (srcRect.getMinY() > cropRect.getMinY()) {
                    cropY += margin;
                }
                if (srcRect.getMaxX() < cropRect.getMaxX()) {
                    cropW -= margin;
                }
                if (srcRect.getMaxY() < cropRect.getMaxY()) {
                    cropH -= margin;
                }
            }
        }
        cropParams.setParameter("x", cropX);
        cropParams.setParameter("y", cropY);
        cropParams.setParameter("width", cropW);
        cropParams.setParameter("height", cropH);
        CropDescriptor cdesc = new CropDescriptor();
        StringBuffer msg = new StringBuffer();
        RenderableOp croppedImage = JAI.createRenderable("crop", cropParams, hints);
        ParameterBlockJAI pbXlate = new ParameterBlockJAI("translate");
        pbXlate.addSource(croppedImage);
        pbXlate.setParameter("xTrans", (-croppedImage.getMinX()));
        pbXlate.setParameter("yTrans", (-croppedImage.getMinY()));
        RenderableOp xformCroppedImage = JAI.createRenderable("translate", pbXlate);
        RenderedImage rendered = xformCroppedImage.createScaledRendering(200, 175, null);
    }

    public static AffineTransform getRotateXform(double rot, double curWidth, double curHeight) {
        AffineTransform at = new AffineTransform();
        at.rotate(rot * Math.PI / 180.0);
        Rectangle2D bounds = getBounds(at, curWidth, curHeight);
        at.preConcatenate(AffineTransform.getTranslateInstance(-bounds.getMinX(), -bounds.getMinY()));
        return at;
    }

    private static Rectangle2D getBounds(AffineTransform xform, double w, double h) {
        double[] corners = { 0.0f, 0.0f, 0.0f, h, w, h, w, 0.0f };
        xform.transform(corners, 0, corners, 0, 4);
        double minX = corners[0];
        double maxX = corners[0];
        double minY = corners[1];
        double maxY = corners[1];
        for (int n = 2; n < corners.length; n += 2) {
            if (corners[n + 1] < minY) {
                minY = corners[n + 1];
            }
            if (corners[n + 1] > maxY) {
                maxY = corners[n + 1];
            }
            if (corners[n] < minX) {
                minX = corners[n];
            }
            if (corners[n] > maxX) {
                maxX = corners[n];
            }
        }
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
}
