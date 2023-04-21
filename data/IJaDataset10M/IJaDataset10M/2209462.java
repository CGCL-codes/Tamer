package com.cirnoworks.cis.impl.awt;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import com.cirnoworks.cis.data.ImageFrame;
import com.cirnoworks.common.ResourceFetcher;

/**
 * @author cloudee
 * 
 */
public final class AWTFrameFactory {

    private final Paintable target;

    /**
	 * @param target
	 */
    public AWTFrameFactory(Paintable target) {
        super();
        this.target = target;
    }

    public AWTFrame createtFrame(ResourceFetcher resourceFetcher, ImageFrame param) throws IOException {
        InputStream is = null;
        try {
            URL u = resourceFetcher.getResource(param.getFile());
            if (u != null) is = u.openStream();
            if (is == null) {
                throw new IOException("找不到资源文件" + param.getFile());
            }
            BufferedImage src = ImageIO.read(is);
            int sw, sh, sx, sy;
            float tw, th;
            float ax, ay;
            float dx, dy;
            sw = param.getSliceW();
            sh = param.getSliceH();
            sx = param.getSliceX();
            sy = param.getSliceY();
            tw = param.getScaleToW();
            th = param.getScaleToH();
            ax = param.getRotateCenterX();
            ay = param.getRotateCenterY();
            dx = param.getDrawCenterX();
            dy = param.getDrawCenterY();
            if (sw == 0) {
                sw = src.getWidth();
            }
            if (tw == 0) {
                tw = sw;
            }
            if (sh == 0) {
                sh = src.getHeight();
            }
            if (th == 0) {
                th = sh;
            }
            if (ax == 0) {
                ax = dx;
            }
            if (ay == 0) {
                ay = dy;
            }
            BufferedImage image = target.createBufferedImage(sw, sh);
            AffineTransform at = new AffineTransform();
            at.translate(-sx, -sy);
            Graphics2D g2 = image.createGraphics();
            g2.drawRenderedImage(src, at);
            g2.dispose();
            AWTFrame ret = new AWTFrame(target, image, tw / (double) sw, th / (double) sh);
            ret.setW(tw);
            ret.setH(th);
            ret.setArchX(ax);
            ret.setArchY(ay);
            ret.setCenterX(dx);
            ret.setCenterY(dy);
            return ret;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
