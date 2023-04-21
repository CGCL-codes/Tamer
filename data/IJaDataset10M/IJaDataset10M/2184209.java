package khall.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;

/**
 * @author keith 
 *
 */
public class ImageAverager {

    private File image;

    /**
	 * 
	 */
    public ImageAverager() {
        super();
    }

    /**
	 * @return
	 */
    public File getImage() {
        return image;
    }

    /**
	 * @param file
	 */
    public void setImage(File file) {
        image = file;
    }

    /**
	 * get the avg rgb values of an image from pos x, y for width and height
	 * @param bufferedImage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
    public ColorPixel getAreaAverage(BufferedImage bufferedImage, int x, int y, int width, int height) throws Exception {
        long average = 0;
        long avgr = 0;
        long avgg = 0;
        long avgb = 0;
        int[] pixels = new int[width * height];
        PixelGrabber pix = new PixelGrabber(bufferedImage, x, y, width, height, pixels, 0, width);
        pix.grabPixels();
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int pixel = pixels[h * width + w];
                avgr += (long) ((pixel >> 16) & 0xff);
                avgg += (long) ((pixel >> 8) & 0xff);
                avgb += (long) ((pixel) & 0xff);
            }
        }
        int r = (int) (avgr / (width * height));
        int g = (int) (avgb / (width * height));
        int b = (int) (avgg / (width * height));
        float[] hsb = Color.RGBtoHSB(r, g, b, null);
        ColorPixel cp = new ColorPixel(r, g, b, hsb);
        return cp;
    }

    /**
	 * find the avg rbg values of a jpeg image
	 * @return
	 * @throws Exception
	 */
    public ColorPixel getJpegImageAverage() throws Exception {
        BufferedImage bufferedImage = JpegImageUtility.loadImage(image);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return getAreaAverage(bufferedImage, 0, 0, width, height);
    }

    /**
	 * find the avg rgb values for an area of a jpeg image at pos x,y for a specific width and height
	 * @param areaimage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
    public ColorPixel getJpegImageAreaAverage(BufferedImage areaimage, int x, int y, int width, int height) throws Exception {
        BufferedImage bufferedImage;
        if (areaimage != null) {
            bufferedImage = areaimage;
        } else {
            bufferedImage = JpegImageUtility.loadImage(image);
        }
        int imagewidth = bufferedImage.getWidth();
        int imageheight = bufferedImage.getHeight();
        int scanwidth = width;
        if ((x + scanwidth) > imagewidth) {
            if (x < imagewidth) {
                scanwidth = imagewidth - x;
            } else {
                throw new Exception("x doesn't fit image ");
            }
        }
        int scanheight = height;
        if ((y + scanheight) > imageheight) {
            if (y < imageheight) {
                scanheight = imageheight - y;
            } else {
                throw new Exception("y doesn't fit image ");
            }
        }
        return getAreaAverage(bufferedImage, x, y, scanwidth, scanheight);
    }

    /**
	 * find if area is single color as described by colorPixel 
	 * @param bufferedImage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param colorPixel
	 * @return
	 * @throws Exception
	 */
    public boolean isAreaSingleColor(int[] pixels, int x, int y, int width, int height, ColorPixel colorPixel) throws Exception {
        int r;
        int g;
        int b;
        boolean singleColor = true;
        compare: for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int pixel = pixels[h * width + w];
                ColorPixel testColor = new ColorPixel();
                testColor.setR(((pixel >> 16) & 0xff));
                testColor.setG(((pixel >> 8) & 0xff));
                testColor.setB(((pixel) & 0xff));
                if (!testColor.equals(colorPixel)) {
                    singleColor = false;
                    break compare;
                }
            }
        }
        return singleColor;
    }

    /**
	 * get the avg rgb values of an image from pos x, y for width and height
	 * @param bufferedImage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
    public boolean getAreaAverageSingleColor(BufferedImage bufferedImage, int x, int y, int width, int height, ColorPixel testColor, ColorPixel returnColor) throws Exception {
        long average = 0;
        long avgr = 0;
        long avgg = 0;
        long avgb = 0;
        int[] pixels = new int[width * height];
        PixelGrabber pix = new PixelGrabber(bufferedImage, x, y, width, height, pixels, 0, width);
        pix.grabPixels();
        if (isAreaSingleColor(pixels, x, y, width, height, testColor)) {
            returnColor.setR(testColor.getR());
            returnColor.setG(testColor.getG());
            returnColor.setB(testColor.getB());
            returnColor.sortPriority();
            return true;
        }
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int pixel = pixels[h * width + w];
                avgr += (long) ((pixel >> 16) & 0xff);
                avgg += (long) ((pixel >> 8) & 0xff);
                avgb += (long) ((pixel) & 0xff);
            }
        }
        returnColor.setR((int) (avgr / (width * height)));
        returnColor.setB((int) (avgb / (width * height)));
        returnColor.setG((int) (avgg / (width * height)));
        returnColor.sortPriority();
        return false;
    }

    /**
	 * find the avg rgb values for an area of a jpeg image at pos x,y for a specific width and height
	 * @param areaimage
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
    public boolean getJpegImageAreaAverageSingleColor(BufferedImage areaimage, int x, int y, int width, int height, ColorPixel testColor, ColorPixel returnColor) throws Exception {
        BufferedImage bufferedImage;
        if (areaimage != null) {
            bufferedImage = areaimage;
        } else {
            bufferedImage = JpegImageUtility.loadImage(image);
        }
        int imagewidth = bufferedImage.getWidth();
        int imageheight = bufferedImage.getHeight();
        int scanwidth = width;
        if ((x + scanwidth) > imagewidth) {
            if (x < imagewidth) {
                scanwidth = imagewidth - x;
            } else {
                throw new Exception("x doesn't fit image ");
            }
        }
        int scanheight = height;
        if ((y + scanheight) > imageheight) {
            if (y < imageheight) {
                scanheight = imageheight - y;
            } else {
                throw new Exception("y doesn't fit image ");
            }
        }
        return getAreaAverageSingleColor(bufferedImage, x, y, scanwidth, scanheight, testColor, returnColor);
    }
}
