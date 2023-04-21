package lib.Field;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Keyron
 */
public class Map {

    BufferedImage map;

    public Map(String ref) throws IOException {
        map = ImageIO.read(new File(ref));
    }

    public BufferedImage getMap() {
        return map;
    }

    private boolean is_out_of_bounds(Point p) {
        if (p.x >= 0 || p.y >= 0 || p.x <= map.getWidth() || p.y <= map.getHeight()) {
            return false;
        }
        return true;
    }

    private int[] get_rgb(Point p) {
        int[] lst = new int[4];
        int pixel = map.getRGB(p.x, p.y);
        lst[0] = (pixel >> 24) & 0xff;
        lst[1] = (pixel >> 16) & 0xff;
        lst[2] = (pixel >> 8) & 0xff;
        lst[3] = (pixel) & 0xff;
        return lst;
    }

    private boolean valute_px(Point p, boolean flying) {
        int red = get_rgb(p)[1];
        if (red <= 200 && flying) {
            return true;
        }
        if (red >= 40 && red <= 160) {
            return true;
        }
        return false;
    }

    private boolean there_is_a_wall(Point c, Point n) {
        int pc = get_rgb(c)[1];
        int pn = get_rgb(n)[1];
        if (Math.abs(pc - pn) >= 30) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is_accessible(Point o, Point p, boolean flying) {
        if (!is_out_of_bounds(p)) {
            return false;
        }
        if (!valute_px(p, flying)) {
            return false;
        }
        if (there_is_a_wall(o, p) && flying == false) {
            return false;
        }
        return true;
    }

    Point get_citadel_position(int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
