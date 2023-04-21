package bl.zk.code.image.old;

import java.awt.image.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

class Tune {

    public static void main(String args[]) {
        try {
            BufferedImage in = ImageIO.read(new File("pic178.jpg"));
            BufferedImage bi = in.getSubimage(100, 100, 100, 100);
            int x = bi.getWidth();
            int y = bi.getHeight();
            for (int i = 0; i < x; i += 10) {
                for (int j = 0; j < y; j += 10) {
                    int all = bi.getRGB(i, j);
                    int red = (all >> 16) & 0xff;
                    int green = (all >> 8) & 0xff;
                    int blue = all & 0xff;
                    System.out.println(i + ", " + j + ": " + red + ", " + green + ", " + blue);
                }
            }
            ImageIO.write(in, "jpg", new File("debug.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
