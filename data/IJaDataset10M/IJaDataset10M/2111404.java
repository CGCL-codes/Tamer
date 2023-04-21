package entities;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class utils {

    public static BufferedImage rotate(BufferedImage img, float angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, 5);
        Graphics2D g = dimg.createGraphics();
        g.rotate(angle, w / 2, h / 2);
        g.drawImage(img, null, 0, 0);
        dimg = makeColorTransparent(dimg, Color.black);
        return dimg;
    }

    public static BufferedImage makeColorTransparent(BufferedImage image, Color color) {
        BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for (int i = 0; i < dimg.getHeight(); i++) {
            for (int j = 0; j < dimg.getWidth(); j++) {
                if (dimg.getRGB(j, i) == color.getRGB()) {
                    dimg.setRGB(j, i, 0x8F1C1C);
                }
            }
        }
        return dimg;
    }

    public static BufferedImage loadTranslucentImage(BufferedImage loaded, float transperancy) {
        BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = aimg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));
        g.drawImage(loaded, null, 0, 0);
        g.dispose();
        return aimg;
    }

    public static BufferedImage horizontalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, 4);
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dimg;
    }

    public static BufferedImage verticalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, 4);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        dimg = makeColorTransparent(dimg, Color.black);
        return dimg;
    }

    public static BufferedImage[] splitImage(String img, int cols, int rows) {
        return splitImage(loadImage(img), cols, rows);
    }

    public static BufferedImage[] splitImage(BufferedImage img, int cols, int rows) {
        int w = img.getWidth() / cols;
        int h = img.getHeight() / rows;
        int num = 0;
        BufferedImage imgs[] = new BufferedImage[rows * cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                imgs[num] = new BufferedImage(w, h, 4);
                Graphics2D g = imgs[num].createGraphics();
                g.drawImage(img, 0, 0, w, h, w * x, h * y, w * x + w, h * y + h, null);
                g.dispose();
                num++;
            }
        }
        System.out.print(imgs);
        return imgs;
    }

    public static BufferedImage loadImage(String ref) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(ref));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimg;
    }

    public static int width(BufferedImage img) {
        return img.getWidth();
    }

    public static int height(BufferedImage img) {
        return img.getHeight();
    }
}
