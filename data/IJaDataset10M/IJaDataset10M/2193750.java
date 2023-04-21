package com.elibera.ccs.img;

import java.util.Locale;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

/**
 * @author meisi
 *
 */
public class MyJPEGImageWriteParam extends JPEGImageWriteParam {

    public MyJPEGImageWriteParam() {
        super(Locale.getDefault());
    }

    public void setCompressionQuality(float quality) {
        if (quality < 0.0F || quality > 1.0F) {
            throw new IllegalArgumentException("Quality out-of-bounds!");
        }
        this.compressionQuality = 256 - (quality * 256);
    }
}
