package test;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import jphotoshop.tool.SimplePoint;

/**
 *
 * @author liuke
 * @email:  soulnew@gmail.com
 */
public class TryTest {

    int dir = 3;

    boolean[][] blImage = { { true, true, true, true, true }, { true, true, true, true, true }, { false, false, false, false, false }, { true, true, true, true, true } };

    ArrayList<SimplePoint> getPoint = new ArrayList();

    public TryTest() {
        System.out.println(Integer.toBinaryString(8 & 4));
    }

    private Shape Bl2Shape(SimplePoint point) {
        System.out.println("start row:" + point.y + "  col:" + point.x);
        SimplePoint willReadPoint;
        GeneralPath p = new GeneralPath();
        willReadPoint = new SimplePoint(point.x, point.y);
        dir = 3;
        getPoint.add(point);
        do {
            for (int loop = 0; loop < 4; loop++) {
                if (nextDir(willReadPoint)) {
                    break;
                }
                dir = (dir + 1) % 4;
            }
            System.out.println("row:" + willReadPoint.y + "  col:" + willReadPoint.x);
        } while (!point.equals(willReadPoint));
        return p;
    }

    private boolean nextDir(SimplePoint point) {
        int x = point.x;
        int y = point.y;
        int tempy = 0;
        int tempx = 0;
        if (dir == 0) {
            tempy = x + 1;
            tempx = y;
        } else if (dir == 1) {
            tempy = x;
            tempx = y - 1;
        } else if (dir == 2) {
            tempy = x - 1;
            tempx = y;
        } else if (dir == 3) {
            tempy = x;
            tempx = y + 1;
        }
        if (tempx < 0 || tempx >= blImage.length || tempy < 0 || tempy >= blImage[0].length) {
            return false;
        } else {
            if (blImage[tempx][tempy]) {
                point.x = tempy;
                point.y = tempx;
                dir = (dir - 1);
                if (dir < 0) {
                    dir = dir + 4;
                }
                return true;
            }
        }
        return false;
    }

    private SimplePoint getMinOne() {
        for (int y = 0; y < blImage.length; y++) {
            for (int x = 0; x < blImage[0].length; x++) {
                if (blImage[y][x]) {
                    return new SimplePoint(x, y);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int a = 1;
        System.out.print(--a);
    }
}
