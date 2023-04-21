package com.shimaging.source;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.awt.RenderingHints;
import java.awt.image.IndexColorModel;
import javax.media.jai.JAI;
import javax.media.jai.ImageLayout;

/**
 * An ImageSource that reads TIFF images, uses an IndexedColorModel with valid 
 * values for Black and White, where White pixels are TRANSPARENT.
 * 
 * This lets you read B&W TIFF images, and do some advanced things with 
 * compositing.
 */
public class JAITiffTranslucentSource extends JAITiffImageSource {

    protected JAITiffTranslucentSource() {
        byte[] legalBits = { (byte) 255, (byte) 0 };
        ImageLayout layout = new ImageLayout();
        layout.setColorModel(new IndexColorModel(1, 2, legalBits, legalBits, legalBits, 0));
        addRenderingHints(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
    }

    public JAITiffTranslucentSource(final File f) throws IOException {
        this();
        open(f);
    }

    public JAITiffTranslucentSource(final InputStream stream, final String name) throws IOException {
        this();
        open(stream, name);
    }
}
