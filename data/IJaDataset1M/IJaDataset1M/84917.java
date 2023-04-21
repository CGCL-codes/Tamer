package org.jarp.gui.jhotdraw.io;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.jarp.gui.jhotdraw.editor.DrawingPreview;
import sun.awt.image.codec.JPEGImageEncoderImpl;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.util.StandardStorageFormat;

/**
 * JPEG file format parser.
 *
 * Creation date: (15/10/2001 02:00:00)
 * 
 * @version $Revision: 1.3 $
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo 
 * Sangoi Padilha</a>
 */
public class JPEGStorageFormat extends StandardStorageFormat {

    /**
	 * @see CH.ifa.draw.util.StorageFormat#isRestoreFormat()
	 */
    public boolean isRestoreFormat() {
        return false;
    }

    /**
	 * @see CH.ifa.draw.util.StorageFormat#isStoreFormat()
	 */
    public boolean isStoreFormat() {
        return true;
    }

    /**
	 * @see CH.ifa.draw.util.StandardStorageFormat#createFileDescription()
	 */
    public String createFileDescription() {
        return "Joint Photographic Engineering Group (" + getFileExtension() + ")";
    }

    /**
	 * @see CH.ifa.draw.util.StandardStorageFormat#createFileExtension()
	 */
    protected String createFileExtension() {
        return "jpg";
    }

    /**
	 * @see CH.ifa.draw.util.StorageFormat#restore(java.lang.String)
	 */
    public Drawing restore(String fileName) {
        return null;
    }

    /**
	 * @see CH.ifa.draw.util.StorageFormat#store(java.lang.String, CH.ifa.draw.framework.Drawing)
	 */
    public String store(String fileName, Drawing saveDrawing) throws IOException {
        fileName = adjustFileName(fileName);
        DrawingPreview view = new DrawingPreview();
        view.setDrawing(saveDrawing);
        Component c = view;
        BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        c.paint(g);
        OutputStream fos = new FileOutputStream(fileName);
        JPEGImageEncoderImpl jpeg = new JPEGImageEncoderImpl(fos);
        com.sun.image.codec.jpeg.JPEGEncodeParam jpeg_param = jpeg.getDefaultJPEGEncodeParam(img);
        jpeg_param.setQuality(0.75f, true);
        jpeg.setJPEGEncodeParam(jpeg_param);
        jpeg.encode(img);
        fos.close();
        return fileName;
    }
}
