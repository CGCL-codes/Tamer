package traffic.console.graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import traffic.basic.Config;

public class ImageLoader {

    public static int count = 0;

    public static double scale = 1.0;

    public static BufferedImage systemIcon = null;

    private static ImageLoader instance = null;

    private ImageLoader() {
        count = Config.getInteger("traffic.vehicle.count", 4);
        scale = Config.getDouble("traffic.vehicle.scale", 1.0);
    }

    public static synchronized ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
            systemIcon = loadImageByName("icon.png");
        }
        return instance;
    }

    public static BufferedImage loadImage(int index) throws IOException {
        try {
            if (index == 4) {
                return ImageIO.read(new File("./image/bar.png"));
            }
            if (index >= 0 && index < count) {
                return ImageIO.read(new File("./image/" + (index + 1) + ".gif"));
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadImageByName(String name) {
        try {
            return ImageIO.read(new File("./image/" + name));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
