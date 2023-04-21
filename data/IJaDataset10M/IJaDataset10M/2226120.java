package org.apache.sanselan.formats.pnm;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageFormat;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.awt.image.DataBuffer;

public abstract class FileInfo {

    protected final int width, height;

    protected final boolean RAWBITS;

    public FileInfo(int width, int height, boolean RAWBITS) {
        this.width = width;
        this.height = height;
        this.RAWBITS = RAWBITS;
    }

    public abstract int getNumComponents();

    public abstract int getBitDepth();

    public abstract ImageFormat getImageType();

    public abstract String getImageTypeDescription();

    public abstract String getMIMEType();

    public abstract int getColorType();

    public abstract int getRGB(WhiteSpaceReader wsr) throws IOException;

    public abstract int getRGB(InputStream is) throws IOException;

    protected void newline() {
    }

    public void readImage(BufferedImage bi, InputStream is) throws IOException {
        DataBuffer buffer = bi.getRaster().getDataBuffer();
        if (!RAWBITS) {
            WhiteSpaceReader wsr = new WhiteSpaceReader(is);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = getRGB(wsr);
                    buffer.setElem(y * width + x, rgb);
                }
                newline();
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = getRGB(is);
                    buffer.setElem(y * width + x, rgb);
                }
                newline();
            }
        }
    }

    public void dump() {
    }
}
