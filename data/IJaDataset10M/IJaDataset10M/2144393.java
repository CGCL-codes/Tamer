package net.sf.zcatalog;

import net.sf.zcatalog.xml.jaxb.*;
import java.io.*;
import java.nio.*;
import java.util.zip.Deflater;
import javax.xml.bind.*;
import net.sf.zcatalog.xml.JAXBGlobals;

/**
 * Container class for the interface to the
 * <a href="http://www.xnview.com"> gflSDK library</a>,
 * which is a very compact C library for Windows, Linux
 * and MacOS X that supports more than 300 image 
 * formats. GflSDK is the engine upon which the 
 * popular Xnview image browser is based.
 * @author Alessandro Zigliani
 * @since ZCatalog 0.9
 * @version 0.9
 */
public final class GFL {

    public static final int MAX_PREVIEW_WIDTH = 256, MAX_PREVIEW_HEIGHT = 256, MIN_PREVIEW_WIDTH = 64, MIN_PREVIEW_HEIGHT = 64, DEFAULT_PREVIEW_WIDTH = 128, DEFAULT_PREVIEW_HEIGHT = 128, DEFAULT_QUALITY = 50, MIN_QUALITY = 20, MAX_QUALITY = 80;

    private static volatile int width = DEFAULT_PREVIEW_WIDTH, height = DEFAULT_PREVIEW_HEIGHT, jpegQuality = DEFAULT_QUALITY;

    /** 
     * UnMarshaller for the XML element returned by the JNI library.
     */
    private static Unmarshaller um;

    /**
     * Use fast mode to create image previews.
     */
    private static volatile boolean fast = false;

    /**
     * Has init() been called before?
     */
    private static volatile boolean init = false;

    /**
     * Loads the JNI dynamic link library and calls 
     * initLibrary().
     * @throws IOException if gflSDK returns an error code
     */
    public static void init() throws JAXBException, IOException {
        int errCode;
        if (init) {
            return;
        }
        MiscUtils.loadJNIExt();
        errCode = initLibrary();
        if (errCode == 0) {
            init = true;
        } else {
            String msg;
            msg = "GFLSDK returned error code '" + errCode + "' and therefore " + "could not be initialized. ";
            throw (new IOException(msg));
        }
        um = JAXBGlobals.CONTEXT.createUnmarshaller();
    }

    /**
     * Initialize the <a href="http://www.xnview.com">
     * gflSDK library through a native call.</a>
     * @return the error code of the underlying library
     * (zero if no error) 
     */
    private static native int initLibrary();

    /**
     * Returns the info about the given file name in XML
     * format.
     * @param fileName the canonical representation of the file
     * @param previewMaxWidth max width wanted for the preview
     * @param previewMaxHeight max height wanted for the preview
     * @param previewJPEGQuality quality as defined by the JPEG standard [1-100]
     * @param fast use fast mode ?
     * @return a XML string or null
     */
    private static native String imageInfo(String fileName, int previewMaxWidth, int previewMaxHeight, int previewJPEGQuality, boolean fast);

    /**
     * Get the GFLImage that describes the given file if it's a image file.
     * @param fileName the file name
     * @return an instance of zcatalog.xml.jaxb.GFLImage (unmarshalled)
     * @throws IOException if the file doesn't exist or cannot be located.
     * @throws JAXBException if anything goes wrong with JAXB
     */
    public static GFLImage loadImageInfo(String fileName) throws JAXBException, IOException {
        String info;
        GFLImage img;
        init();
        info = imageInfo(fileName, width, height, jpegQuality, fast);
        img = (GFLImage) um.unmarshal(new StringReader(info));
        return img;
    }

    public static void setPreviewMaxSize(int width, int height) {
        if (width > GFL.MAX_PREVIEW_WIDTH || height > GFL.MAX_PREVIEW_HEIGHT || width < GFL.MIN_PREVIEW_WIDTH || height < GFL.MIN_PREVIEW_HEIGHT) {
            GFL.width = GFL.DEFAULT_PREVIEW_WIDTH;
            GFL.height = GFL.DEFAULT_PREVIEW_HEIGHT;
        } else {
            GFL.width = width;
            GFL.height = height;
        }
    }

    public static void setFastMode(boolean fast) {
        GFL.fast = fast;
    }

    public static void setPreviewQuality(int percent) {
        if (percent > GFL.MAX_QUALITY) {
            percent = GFL.MAX_QUALITY;
        } else if (percent < GFL.MIN_QUALITY) {
            percent = GFL.MIN_QUALITY;
        }
        GFL.jpegQuality = percent;
    }

    public static byte[] xml2bin(char[] encData) {
        int len = encData.length;
        byte[] b = new byte[len];
        while (len > 0) {
            b[--len] = (byte) (encData[len] - 256);
        }
        return b;
    }

    public static void bin2xml(StringBuilder dest, byte[] binData) {
        int len = binData.length;
        char[] c = new char[len];
        while (len > 0) {
            c[--len] = (char) (binData[len] + 256);
        }
        dest.append(c);
    }

    private static native byte[] resizeIcon(byte src[], int sizes[]);

    public static ByteBuffer[] getResizedIcons(byte src[], int sizes[]) throws JAXBException, IOException {
        byte b[];
        ByteBuffer bufs[];
        ByteBuffer buf;
        int i = sizes.length;
        int start = 0;
        int len;
        init();
        bufs = new ByteBuffer[sizes.length];
        b = resizeIcon(src, sizes);
        if (b == null) {
            return bufs;
        }
        while (i > 0) {
            len = sizes[--i];
            buf = ByteBuffer.allocate(len);
            bufs[i] = buf.put(b, start, len);
            start += len;
        }
        return bufs;
    }
}
