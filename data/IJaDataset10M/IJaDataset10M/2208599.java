package com.jxva.graph.strategy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import com.jxva.graph.GraphException;

/**
 * 图形处理高质量算法
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2008-11-27 14:52:43 by Jxva
 */
public class HighQualityGraph extends AbstractGraph implements ImageObserver {

    public BufferedImage resize(BufferedImage srcBi, double width, double height) throws GraphException {
        BufferedImage bi = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        bi.getGraphics().drawImage(srcBi, 0, 0, (int) width, (int) height, this);
        return bi;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return ((infoflags & ALLBITS) == 0);
    }
}
