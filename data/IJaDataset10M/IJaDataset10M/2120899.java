package com.sun.imageio.plugins.wbmp;

import java.util.Locale;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ServiceRegistry;
import java.io.IOException;
import javax.imageio.ImageReader;
import javax.imageio.IIOException;

public class WBMPImageReaderSpi extends ImageReaderSpi {

    private static String[] writerSpiNames = { "com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi" };

    private static String[] formatNames = { "wbmp", "WBMP" };

    private static String[] entensions = { "wbmp" };

    private static String[] mimeType = { "image/vnd.wap.wbmp" };

    private boolean registered = false;

    public WBMPImageReaderSpi() {
        super("Sun Microsystems, Inc.", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.wbmp.WBMPImageReader", STANDARD_INPUT_TYPE, writerSpiNames, true, null, null, null, null, true, WBMPMetadata.nativeMetadataFormatName, "com.sun.imageio.plugins.wbmp.WBMPMetadataFormat", null, null);
    }

    public void onRegistration(ServiceRegistry registry, Class<?> category) {
        if (registered) {
            return;
        }
        registered = true;
    }

    public String getDescription(Locale locale) {
        return "Standard WBMP Image Reader";
    }

    public boolean canDecodeInput(Object source) throws IOException {
        if (!(source instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream stream = (ImageInputStream) source;
        byte[] b = new byte[3];
        stream.mark();
        stream.readFully(b);
        stream.reset();
        return ((b[0] == (byte) 0) && b[1] == 0 && ((b[2] & 0x8f) != 0 || (b[2] & 0x7f) != 0));
    }

    public ImageReader createReaderInstance(Object extension) throws IIOException {
        return new WBMPImageReader(this);
    }
}
