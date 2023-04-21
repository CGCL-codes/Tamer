package mercenary;

import java.awt.image.*;

/**
 *
 * @author Juho Peltonen
 */
public class CollisionDetectionTest {

    public static void main(String[] args) {
        BufferedImage img1 = null, img2 = null;
        try {
            java.net.URL url = GfxTest.class.getResource("/img/coll_test.png");
            img1 = javax.imageio.ImageIO.read(url);
            img2 = javax.imageio.ImageIO.read(url);
        } catch (Exception e) {
            System.out.println("ERROR!: " + e);
        }
        boolean b = CollisionDetection.perPixelTest(img1, 20, 20, img2, 10, 10);
        System.out.println(b);
    }
}
