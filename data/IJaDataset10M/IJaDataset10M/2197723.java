package org.chemicalcovers.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import org.chemicalcovers.ChemicalCovers;

/**
 * Class to manipulate Image from file in different formats like JPEG, GIF
 * @author Jean-Yves Beaujean
 *
 */
public class ImageFile {

    public static final String JPEG = "jpg";

    public static final String PNG = "png";

    public static final String BMP = "bmp";

    public static final String GIF = "gif";

    private URL imageURL;

    /** Original image */
    private BufferedImage image = null;

    /** Working copy */
    private BufferedImage workImage = null;

    /**
	 * Constructor
	 * @param fileName
	 */
    public ImageFile(URL imageURL) {
        this.imageURL = imageURL;
        open();
    }

    public Image getImage() {
        return image;
    }

    public Image getWorkImage() {
        return workImage == null ? image : workImage;
    }

    /** 
	 * Read image from fileName
	 */
    private void open() {
        try {
            image = ImageIO.read(imageURL);
        } catch (Exception exception) {
            ChemicalCovers.LOGGER.warning("open error : " + exception);
            image = null;
        }
    }

    public boolean isValid() {
        return image != null;
    }

    /**
	 * List all available image formats for read/write operations
	 */
    public static void listAvalaibleFormats() {
        String[] formatsLecture = ImageIO.getReaderFormatNames();
        String[] formatsEcriture = ImageIO.getWriterFormatNames();
        System.out.println("Read formats : ");
        for (int i = 0; i < formatsLecture.length; i++) {
            System.out.println(formatsLecture[i]);
        }
        System.out.println("Write format");
        for (int i = 0; i < formatsEcriture.length; i++) {
            System.out.println(formatsEcriture[i]);
        }
    }

    /**
	 * Set image in gray mode
	 */
    public void grayscale() {
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        if (workImage == null) workImage = op.filter(image, null); else workImage = op.filter(workImage, null);
    }

    public void blur() {
        float[] matrice = { 0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrice));
        if (workImage == null) workImage = op.filter(image, null); else workImage = op.filter(workImage, null);
    }

    public void scaleToFile(String fileName, String format, int width, int height) throws Exception {
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        File f = new File(fileName);
        ImageIO.write(buf, format, f);
    }

    public void scale(int width, int height) {
        BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        workImage = buf;
    }

    public void scaleMaxWidth(double maxWidth) {
        int nW = 0, nH = 0;
        double ratio = 1;
        if (image.getWidth() > maxWidth) {
            ratio = image.getWidth() / maxWidth;
            nW = (int) maxWidth;
            nH = (int) (image.getHeight() / ratio);
        } else {
            nW = image.getWidth();
            nH = image.getHeight();
        }
        System.out.println("Ratio = " + image.getWidth() + "/" + maxWidth);
        System.out.println("Ratio : " + ratio);
        BufferedImage buf = new BufferedImage(nW, nH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, nW, nH, null);
        g.dispose();
        workImage = buf;
    }

    public void scale(double ratio) {
        int nW = (int) (image.getWidth() / ratio);
        int nH = (int) (image.getHeight() / ratio);
        BufferedImage buf = new BufferedImage(nW, nH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, nW, nH, null);
        g.dispose();
        workImage = buf;
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getWidth() {
        return image.getWidth();
    }

    /**
	 * Save Image with the same fileName
	 * @param newFilename
	 * @param format
	 * @throws Exception
	 *
	public void save(String format) throws Exception 
	{
		ImageIO.write(image, format, new File(fileName));
	}*/
    public void saveAs(String newFilename, String format) throws Exception {
        ImageIO.write(image, format, new File(newFilename));
    }

    /**
	 * Save working copy in the sameFilname
	 * @throws Exception
	 */
    public void saveWork(String newFilename, String format) throws Exception {
        if (workImage != null) {
            File f = new File(newFilename);
            ImageIO.write(workImage, format, f);
        } else {
            System.err.println("ImageFile.saveWork : Unable to save working copy ! Working copy is null...");
        }
    }

    /**
	 * Returns the filename of the image
	 * @return String
	 *
	public String getFileName() {
		return fileName;
	}*/
    public void flush() {
        if (workImage != null) {
            workImage.flush();
            workImage = null;
        }
        if (image != null) {
            image.flush();
            image = null;
        }
    }

    @Override
    public String toString() {
        String aString = "ImageFile : [" + imageURL + "]\n";
        aString += "Width : " + image.getWidth() + "px\n";
        aString += "Height : " + image.getHeight() + "px\n";
        return aString;
    }
}
