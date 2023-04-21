package org.designerator.media.image.util.test;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.designerator.media.image.util.IO;
import org.designerator.media.image.util.test.EdgeDetector.ImagePanel;

public class ImageTools {

    public static int getPixel(BufferedImage im, int x, int y) {
        return im.getRGB(x, y);
    }

    public static void setPixel(BufferedImage im, int x, int y, int argb) {
        im.setRGB(x, y, argb);
    }

    public static int[][] getClusteredDither88() {
        return new int[][] { { 113, 80, 96, 105, 142, 175, 159, 150 }, { 51, 0, 1, 88, 200, 254, 250, 167 }, { 14, 3, 7, 72, 225, 242, 233, 183 }, { 39, 26, 63, 121, 208, 217, 192, 134 }, { 138, 171, 154, 146, 117, 84, 101, 109 }, { 196, 254, 246, 163, 57, 0, 2, 92 }, { 221, 237, 229, 179, 20, 5, 10, 76 }, { 204, 213, 188, 130, 45, 32, 67, 125 } };
    }

    public static int[][] getDispersedDither88() {
        return new int[][] { { 4, 236, 60, 220, 8, 224, 48, 208 }, { 132, 68, 188, 124, 136, 72, 176, 112 }, { 36, 196, 20, 252, 40, 200, 24, 240 }, { 164, 100, 148, 84, 168, 104, 152, 88 }, { 12, 228, 52, 212, 0, 232, 56, 216 }, { 140, 76, 180, 116, 128, 64, 184, 120 }, { 44, 204, 28, 244, 32, 192, 16, 248 }, { 172, 108, 156, 92, 160, 96, 144, 80 } };
    }

    public static BufferedImage orderedDither(Image image, int[][] dither) {
        BufferedImage BI = toBufferedImage(image);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        BufferedImage BO = createImage(w, h, 0);
        int dw = dither.length;
        int dh = dither[0].length;
        for (int y = 0; y < h; y++) {
            int dy = y % dh;
            for (int x = 0; x < w; x++) {
                int pixel = BI.getRGB(x, y);
                int red = (pixel & 0x00FF0000) >> 16;
                int green = (pixel & 0x0000FF00) >> 8;
                int blue = (pixel & 0x000000FF);
                int dx = x % dw;
                int tmp = dither[dy][dx];
                if (red >= tmp) red = 255; else red = 0;
                if (green >= tmp) green = 255; else green = 0;
                if (blue >= tmp) blue = 255; else blue = 0;
                BO.setRGB(x, y, (red << 16) + (green << 8) + blue);
            }
        }
        return BO;
    }

    public static BufferedImage floydSteinberg(Image image) {
        BufferedImage BI = toBufferedImage(image);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        double[][] e = new double[h][w];
        double[][] b = new double[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                e[y][x] = (BI.getRGB(x, y) & 0xFF) / 256.0;
            }
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
            }
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float f = (BI.getRGB(x, y) & 0xFF) / 256;
            }
        }
        return BI;
    }

    public static BufferedImage convolutionFilter(BufferedImage image, float[] coeffs) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int size = (int) Math.sqrt(coeffs.length);
        Kernel kernel = new Kernel(size, size, coeffs);
        ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage BO = cop.filter(image, null);
        return BO;
    }

    public static BufferedImage sobelXFilter(BufferedImage image) {
        float coeffs[] = { 1, 2, 1, 0, 0, 0, -1, -2, -1 };
        return convolutionFilter(image, coeffs);
    }

    public static BufferedImage sobelYFilter(BufferedImage image) {
        float coeffs[] = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
        return convolutionFilter(image, coeffs);
    }

    public static BufferedImage gauss3Filter(BufferedImage image) {
        float a = 1.0F / 16.0F;
        float coeffs[] = { 1 * a, 2 * a, 1 * a, 2 * a, 4 * a, 2 * a, 1 * a, 2 * a, 1 * a };
        return convolutionFilter(image, coeffs);
    }

    public static BufferedImage gauss5Filter(BufferedImage image) {
        float coeffs[] = { 2, 7, 12, 7, 2, 7, 31, 52, 31, 7, 15, 52, 127, 52, 15, 7, 31, 52, 31, 7, 2, 7, 12, 7, 2 };
        float sum = 0;
        for (int i = 0; i < coeffs.length; i++) {
            sum += coeffs[i];
        }
        for (int i = 0; i < coeffs.length; i++) {
            coeffs[i] /= sum;
        }
        return convolutionFilter(image, coeffs);
    }

    public static BufferedImage addFilter(Image i1, Image i2) {
        BufferedImage tmp1 = getClone(i1);
        BufferedImage tmp2 = getClone(i2);
        int w = Math.min(tmp1.getWidth(), tmp2.getWidth());
        int h = Math.min(tmp1.getHeight(), tmp2.getHeight());
        BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = tmp1.getRGB(x, y);
                int p2 = tmp2.getRGB(x, y);
                int r = (p1 & 0x00FF0000) + (p2 & 0x00FF0000);
                int g = (p1 & 0x0000FF00) + (p2 & 0x0000FF00);
                int b = (p1 & 0x000000FF) + (p2 & 0x000000FF);
                if (r >= 0x00FF0000) r = 0x00FF0000;
                if (g >= 0x0000FF00) g = 0x0000FF00;
                if (b >= 0x000000FF) b = 0x000000FF;
                BI.setRGB(x, y, (r | g | b));
            }
        }
        return BI;
    }

    public static BufferedImage mult(Image i1, double factor) {
        BufferedImage tmp1 = getClone(i1);
        int w = tmp1.getWidth();
        int h = tmp1.getHeight();
        BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        if (factor <= 0.0) {
            msg("-E- mult: factor must be positive!");
            factor = Math.abs(factor);
        }
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = tmp1.getRGB(x, y);
                double fr = (p1 & 0x00FF0000) * factor;
                double fg = (p1 & 0x0000FF00) * factor;
                double fb = (p1 & 0x000000FF) * factor;
                int r = 0;
                int g = 0;
                int b = 0;
                if (fr >= 0x00FF0000) r = 0x00FF0000; else r = ((int) fr) & 0x00FF0000;
                if (fg >= 0x0000FF00) g = 0x0000FF00; else g = ((int) fg) & 0x0000FF00;
                if (fb >= 0x000000FF) b = 0x000000FF; else b = ((int) fb) & 0x000000FF;
                BI.setRGB(x, y, (r | g | b));
            }
        }
        return BI;
    }

    public static BufferedImage add(Image i1, int offset) {
        BufferedImage tmp1 = getClone(i1);
        int w = tmp1.getWidth();
        int h = tmp1.getHeight();
        BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        offset = offset & 0xFF;
        int p2 = (offset << 16) + (offset << 8) + offset;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = tmp1.getRGB(x, y);
                int r = (p1 & 0x00FF0000) + (p2 & 0x00FF0000);
                int g = (p1 & 0x0000FF00) + (p2 & 0x0000FF00);
                int b = (p1 & 0x000000FF) + (p2 & 0x000000FF);
                if (r >= 0x00FF0000) r = 0x00FF0000;
                if (g >= 0x0000FF00) g = 0x0000FF00;
                if (b >= 0x000000FF) b = 0x000000FF;
                BI.setRGB(x, y, (r | g | b));
            }
        }
        return BI;
    }

    public static BufferedImage andFilter(Image i1, Image i2) {
        BufferedImage tmp1 = getClone(i1);
        BufferedImage tmp2 = getClone(i2);
        int w = Math.min(tmp1.getWidth(), tmp2.getWidth());
        int h = Math.min(tmp1.getHeight(), tmp2.getHeight());
        BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = tmp1.getRGB(x, y) & tmp2.getRGB(x, y);
                BI.setRGB(x, y, pixel);
            }
        }
        return BI;
    }

    public static BufferedImage grayFilter(Image inputImage) {
        BufferedImage BI = getClone(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = BI.getRGB(x, y);
                int alpha = rgb & 0xff000000;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                int gray = ((307 * red + 604 * green + 113 * blue) >> 10) & 0xff;
                int aggg = (alpha | gray << 16 | gray << 8 | gray);
                BI.setRGB(x, y, aggg);
            }
        }
        return BI;
    }

    public static BufferedImage invertFilter(Image inputImage) {
        BufferedImage BI = getClone(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = BI.getRGB(x, y);
                int alpha = rgb & 0xff000000;
                int red = 0x00ff0000 - (rgb & 0x00ff0000);
                int green = 0x0000ff00 - (rgb & 0x0000ff00);
                int blue = 0x000000ff - (rgb & 0x000000ff);
                BI.setRGB(x, y, alpha | red | green | blue);
            }
        }
        return BI;
    }

    public static BufferedImage redFilter(Image inputImage) {
        BufferedImage BI = getClone(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = BI.getRGB(x, y);
                BI.setRGB(x, y, pixel & 0xFFFF0000);
            }
        }
        return BI;
    }

    public static BufferedImage greenFilter(Image inputImage) {
        BufferedImage BI = getClone(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = BI.getRGB(x, y);
                BI.setRGB(x, y, pixel & 0xFF00FF00);
            }
        }
        return BI;
    }

    public static BufferedImage blueFilter(Image inputImage) {
        BufferedImage BI = getClone(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = BI.getRGB(x, y);
                BI.setRGB(x, y, pixel & 0xFF0000FF);
            }
        }
        return BI;
    }

    public static BufferedImage resize(Image inputImage, int ww, int hh) {
        BufferedImage BI = toBufferedImage(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        double scale_x = 1.0 * ww / w;
        double scale_y = 1.0 * hh / h;
        AffineTransform trafo = AffineTransform.getScaleInstance(scale_x, scale_y);
        AffineTransformOp op = new AffineTransformOp(trafo, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage BO = op.filter(BI, null);
        return BO;
    }

    /**
   * calculate a histogram of pixel values and return a 4x256 matrix
   * with rows brightness / red / green / blue.
   */
    public static double[][] histogram(Image inputImage) {
        double[][] histo = new double[4][256];
        BufferedImage BI = toBufferedImage(inputImage);
        int w = BI.getWidth(null);
        int h = BI.getHeight(null);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = BI.getRGB(x, y);
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                int gray = ((307 * red + 604 * green + 113 * blue) >> 10) & 0xff;
                histo[0][gray]++;
                histo[1][red]++;
                histo[2][green]++;
                histo[3][blue]++;
            }
        }
        return histo;
    }

    /**
   * convert an arbitrary image to a BufferedImage
   */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        } else {
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            if ((w < 0) || (h < 0)) {
                System.err.println("-E- getBufferedImage: invalid: " + image);
                return null;
            }
            int[] pixels = new int[w * h];
            PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
            try {
                pg.grabPixels();
            } catch (InterruptedException e) {
                System.err.println("interrupted waiting for pixels!");
                return null;
            }
            if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                System.err.println("image fetch aborted or errored");
                return null;
            }
            BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j < h; j++) {
                int row = j * w;
                for (int i = 0; i < w; i++) {
                    tmp.setRGB(i, j, pixels[row + i]);
                }
            }
            return tmp;
        }
    }

    public static BufferedImage getClone(Image image) {
        try {
            BufferedImage BI = toBufferedImage(image);
            int w = BI.getWidth(null);
            int h = BI.getHeight(null);
            long t1 = System.currentTimeMillis();
            BufferedImage BO = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    BO.setRGB(x, y, BI.getRGB(x, y));
                }
            }
            long t2 = System.currentTimeMillis();
            return BO;
        } catch (Throwable t) {
            msg("-E- internal: " + t);
            t.printStackTrace();
            return null;
        }
    }

    private static double linear(double x, double xstart, double xend) {
        return xstart + (x - xstart) / (xend - xstart);
    }

    public static BufferedImage createColorTest(int w, int h) {
        try {
            BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int h4 = h / 4;
            for (int y = 0; y < h4; y++) {
                for (int x = 0; x < w; x++) {
                    int tmp = (int) (255 * linear(x, 0, w));
                    BI.setRGB(x, y, (tmp << 16) + (tmp << 8) + tmp);
                }
            }
            for (int y = h4; y < 2 * h4; y++) {
                for (int x = 0; x < w; x++) {
                    int tmp = (int) (255 * linear(x, 0, w));
                    BI.setRGB(x, y, (tmp << 16));
                }
            }
            for (int y = 2 * h4; y < 3 * h4; y++) {
                for (int x = 0; x < w; x++) {
                    int tmp = (int) (255 * linear(x, 0, w));
                    BI.setRGB(x, y, (tmp << 8));
                }
            }
            for (int y = 3 * h4; y < 4 * h4; y++) {
                for (int x = 0; x < w; x++) {
                    int tmp = (int) (255 * linear(x, 0, w));
                    BI.setRGB(x, y, (tmp));
                }
            }
            return BI;
        } catch (Throwable t) {
            msg("-E- internal: " + t);
            t.printStackTrace();
            return null;
        }
    }

    public static BufferedImage createImage(int w, int h, int argb) {
        try {
            BufferedImage BI = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    BI.setRGB(x, y, argb);
                }
            }
            return BI;
        } catch (Throwable t) {
            msg("-E- internal: " + t);
            t.printStackTrace();
            return null;
        }
    }

    public void printImageInfo(BufferedImage image) {
        if (image == null) {
            msg("-I- null image");
            return;
        }
        msg("-I- image " + image.toString());
    }

    public static void writeImage(BufferedImage image, String filename) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            String format = "png";
            int dot = filename.indexOf(".");
            if (dot >= 0) {
                format = filename.substring(dot + 1);
            }
            writeImage(image, format, bos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeImage(BufferedImage image, String format, OutputStream os) {
        try {
            ImageIO.write(image, format, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(String filename) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
            return loadImage(bis);
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage loadImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (Exception e) {
            return null;
        }
    }

    public static BufferedImage loadImage(InputStream is) {
        try {
            return ImageIO.read(is);
        } catch (Exception e) {
            return null;
        }
    }

    public static void msg(String msg) {
        System.out.println(msg);
    }

    public static void main(String args[]) {
        ImageTools tools = new ImageTools();
        BufferedImage lena_gray = IO.loadBufImage("i:/eigene bilder/test2/contrast1.jpg");
        BufferedImage lena_sobelx = tools.sobelXFilter(lena_gray);
        BufferedImage lena_sobely = tools.sobelXFilter(lena_gray);
        BufferedImage lena_sobelxy = tools.andFilter(lena_sobelx, lena_sobely);
        displayImage(lena_sobelxy);
    }

    public static void displayImage(BufferedImage img) {
        JFrame fr = new JFrame();
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImagePanel pan = new ImagePanel(img);
        pan.setSize(256, 256);
        fr.getContentPane().add(pan);
        fr.pack();
        fr.setSize(256, 256);
        fr.show();
    }

    static class ImagePanel extends JComponent {

        protected BufferedImage image;

        public ImagePanel() {
        }

        public ImagePanel(BufferedImage img) {
            image = img;
        }

        public void setImage(BufferedImage img) {
            image = img;
        }

        public void paintComponent(Graphics g) {
            Rectangle rect = this.getBounds();
            if (image != null) {
                g.drawImage(image, 0, 0, rect.width, rect.height, this);
            }
        }
    }
}
