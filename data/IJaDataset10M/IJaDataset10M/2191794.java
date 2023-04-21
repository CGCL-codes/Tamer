package com.liferay.portal.kernel.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <a href="ImageProcessor.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface ImageProcessor {

    public static final String TYPE_BMP = "bmp";

    public static final String TYPE_GIF = "gif";

    public static final String TYPE_JPEG = "jpg";

    public static final String TYPE_PNG = "png";

    public static final String TYPE_TIFF = "tiff";

    public static final String TYPE_NOT_AVAILABLE = "na";

    public BufferedImage convertImageType(BufferedImage sourceImage, int type);

    public void encodeGIF(RenderedImage renderedImage, OutputStream os) throws IOException;

    public void encodeWBMP(RenderedImage renderedImage, OutputStream os) throws InterruptedException, IOException;

    public BufferedImage getBufferedImage(RenderedImage renderedImage);

    public ImageBag read(File file) throws IOException;

    public ImageBag read(byte[] bytes) throws IOException;

    public RenderedImage scale(RenderedImage renderedImage, int maxHeight, int maxWidth);
}
