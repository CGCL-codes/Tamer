package com.mortennobel.imagescaling;

import junit.framework.TestCase;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;
import java.io.IOException;
import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.imagescaling.AdvancedResizeOp;

/**
 * Test case on bugreport and code by Murugesh Ramachandran 
 */
public class Issue5 extends TestCase {

    public void testSRGB() throws Exception {
        try {
            String[] fileTypes = { "jpg", "png", "JPG", "GIF" };
            boolean bFlag = false;
            bFlag = false;
            int fixlength = 1600;
            int calcHeight = 0;
            int calcWidth = 0;
            BufferedImage bufferedImage = readImage(getClass().getResourceAsStream("pic_010b(17MB).jpg"));
            if (bufferedImage.getHeight() > bufferedImage.getWidth()) {
                if (bufferedImage.getHeight() > 1600) {
                    calcWidth = fixlength * bufferedImage.getWidth() / bufferedImage.getHeight();
                    calcHeight = 1600;
                    bFlag = true;
                } else {
                    calcWidth = bufferedImage.getWidth();
                    calcHeight = bufferedImage.getHeight();
                }
            } else {
                if (bufferedImage.getWidth() > 1600) {
                    calcHeight = fixlength * bufferedImage.getHeight() / bufferedImage.getWidth();
                    calcWidth = 1600;
                    bFlag = true;
                } else {
                    calcWidth = bufferedImage.getWidth();
                    calcHeight = bufferedImage.getHeight();
                }
            }
            System.out.println(calcHeight);
            BufferedImage rescaledImage = createResizedCopy(bufferedImage, calcWidth, calcHeight);
            int res = JOptionPane.showConfirmDialog(null, "Do you see a scaled image?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(rescaledImage));
            assertEquals(JOptionPane.YES_OPTION, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Workaround for sRGB color space bug on JRE 1.5
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4705399
	 *  
	 * @param source (inputstream or File)
	 * @return fixed buffered image
	 * @throws IOException
	 */
    public static BufferedImage readImage(Object source) throws IOException {
        ImageInputStream stream = ImageIO.createImageInputStream(source);
        ImageReader reader = ImageIO.getImageReaders(stream).next();
        reader.setInput(stream);
        ImageReadParam param = reader.getDefaultReadParam();
        ImageTypeSpecifier typeToUse = null;
        for (Iterator i = reader.getImageTypes(0); i.hasNext(); ) {
            ImageTypeSpecifier type = (ImageTypeSpecifier) i.next();
            if (type.getColorModel().getColorSpace().isCS_sRGB()) typeToUse = type;
        }
        if (typeToUse != null) param.setDestinationType(typeToUse);
        BufferedImage b = reader.read(0, param);
        reader.dispose();
        stream.close();
        return b;
    }

    public static BufferedImage createResizedCopy(BufferedImage originalImage, int scaledWidth, int scaledHeight) {
        ResampleOp resampleOp = new ResampleOp(scaledWidth, scaledHeight);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
        BufferedImage rescaledTomato = resampleOp.filter(originalImage, null);
        return rescaledTomato;
    }
}
