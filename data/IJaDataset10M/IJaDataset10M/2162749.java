package business.webtempl;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * @author Alin Nistor
 * @version 1.0 
 *  date: 28.10.2008 
 */
public class ScreenShot {

    private static int ORDER = 1;

    public static String takeAPictureToWebPageAndSaveIt(String absolutePath, String picd, String browser, int wait) {
        try {
            Process p = Runtime.getRuntime().exec(browser + " " + absolutePath);
            Thread.sleep(wait);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            String imagefile = "img" + ORDER++ + ".gif";
            ImageIO.write(image, "gif", new File(picd + "/" + imagefile));
            p.destroy();
            return imagefile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false.gif";
    }
}
