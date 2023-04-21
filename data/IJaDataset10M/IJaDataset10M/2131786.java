package org.apache.sanselan.formats.bmp.writers;

import java.io.IOException;
import org.apache.sanselan.common.BinaryOutputStream;
import com.google.code.appengine.awt.image.BufferedImage;

public abstract class BMPWriter {

    public abstract int getPaletteSize();

    public abstract int getBitsPerPixel();

    public abstract void writePalette(BinaryOutputStream bos) throws IOException;

    public abstract byte[] getImageData(BufferedImage src);
}
