package test.jtable;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class Test_001 {

    /**
	 * @param args
	 */
    public static void main_01(String[] args) {
        int i = 3 | (2);
        System.out.println(i);
        i = -(i | ~3);
        System.out.println(i);
    }

    public static void main(String[] args) throws Exception {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D canvas = (Graphics2D) image.getGraphics();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setColor(Color.white);
        canvas.setBackground(Color.white);
        canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));
        canvas.fillRect(0, 0, 100, 100);
        canvas.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        canvas.setColor(Color.black);
        canvas.drawLine(10, 10, 20, 20);
        canvas.dispose();
        OutputStream ostream = new FileOutputStream("c://test.png");
        ImageIO.write(image, "png", ostream);
        ostream.flush();
    }

    public void test() throws Exception {
        BufferedImage img = null;
        OutputStream os = null;
        ImageIO.write(convertRGBAToIndexed(img), "gif", os);
    }

    public static BufferedImage convertRGBAToIndexed(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
        Graphics g = dest.getGraphics();
        g.setColor(new Color(231, 20, 189));
        g.fillRect(0, 0, dest.getWidth(), dest.getHeight());
        dest = makeTransparent(dest, 0, 0);
        dest.createGraphics().drawImage(src, 0, 0, null);
        return dest;
    }

    public static BufferedImage makeTransparent(BufferedImage image, int x, int y) {
        ColorModel cm = image.getColorModel();
        if (!(cm instanceof IndexColorModel)) {
            return image;
        }
        IndexColorModel icm = (IndexColorModel) cm;
        WritableRaster raster = image.getRaster();
        int pixel = raster.getSample(x, y, 0);
        int size = icm.getMapSize();
        byte[] reds = new byte[size];
        byte[] greens = new byte[size];
        byte[] blues = new byte[size];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        IndexColorModel icm2 = new IndexColorModel(8, size, reds, greens, blues, pixel);
        return new BufferedImage(icm2, raster, image.isAlphaPremultiplied(), null);
    }
}
