package elementareArrayFunction;

import java.awt.*;
import java.io.Serializable;
import mapRepresentation.*;

public class elemantaryArrayFunction implements Serializable {

    public static void drawLine(int[][] map, vPoint P1, vPoint P2, int value) {
        int c, M;
        int x = (int) P1.m_iXStartPos;
        int y = (int) P1.m_iYStartPos;
        int D = 0;
        int HX = (int) (P2.m_iXStartPos - P1.m_iXStartPos);
        int HY = (int) (P2.m_iYStartPos - P1.m_iYStartPos);
        int xInc = 1;
        int yInc = 1;
        if (HX < 0) {
            xInc = -1;
            HX = -HX;
        }
        if (HY < 0) {
            yInc = -1;
            HY = -HY;
        }
        if (HY <= HX) {
            c = 2 * HX;
            M = 2 * HY;
            while (true) {
                map[x][y] = value;
                if (x == P2.m_iXStartPos) {
                    break;
                }
                x += xInc;
                D += M;
                if (D > HX) {
                    y += yInc;
                    D -= c;
                }
            }
        } else {
            c = 2 * HY;
            M = 2 * HX;
            while (true) {
                map[x][y] = value;
                if (y == P2.m_iYStartPos) {
                    break;
                }
                y += yInc;
                D += M;
                if (D > HY) {
                    x += xInc;
                    D -= c;
                }
            }
        }
    }

    public static void printMap(int[][] map, int sizeX, int sizeY) {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                System.out.print(map[x][y] + " ");
            }
            System.out.println();
        }
    }

    public static void drawMap(int[][] map, int sizeX, int sizeY, int banchmark, Graphics g) {
        int value = 0;
        Color colSave;
        colSave = g.getColor();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                value = map[x][y];
                if (value != 0) {
                    g.setColor(new Color(value % 255, value % 255, value % 255));
                    g.drawOval(x, y, 1, 1);
                }
            }
        }
        g.setColor(colSave);
    }

    public static void buildLargeObsticales(int[][] inMap, int xSizeMap, int ySizeMap) {
        int id = 0;
        int helpId = 0;
        for (int x = 1; x < xSizeMap - 1; x++) {
            for (int y = 1; y < ySizeMap - 1; y++) {
                if (inMap[x][y] != 0) {
                    id = inMap[x][y];
                    if (inMap[x + 1][y] != 0) inMap[x + 1][y] = id;
                    if (inMap[x - 1][y] != 0) inMap[x - 1][y] = id;
                    if (inMap[x][y + 1] != 0) inMap[x][y + 1] = id;
                    if (inMap[x][y - 1] != 0) inMap[x][y - 1] = id;
                    if (inMap[x + 1][y + 1] != 0) inMap[x + 1][y + 1] = id;
                    if (inMap[x + 1][y - 1] != 0) inMap[x + 1][y - 1] = id;
                    if (inMap[x - 1][y + 1] != 0) inMap[x - 1][y + 1] = id;
                    if (inMap[x - 1][y - 1] != 0) inMap[x - 1][y - 1] = id;
                }
            }
        }
        for (int y = 1; y < ySizeMap - 1; y++) {
            for (int x = 1; x < xSizeMap - 1; x++) {
                if (inMap[x][y] != 0) {
                    id = inMap[x][y];
                    if (inMap[x + 1][y] != 0) inMap[x + 1][y] = id;
                    if (inMap[x - 1][y] != 0) inMap[x - 1][y] = id;
                    if (inMap[x][y + 1] != 0) inMap[x][y + 1] = id;
                    if (inMap[x][y - 1] != 0) inMap[x][y - 1] = id;
                    if (inMap[x + 1][y + 1] != 0) inMap[x + 1][y + 1] = id;
                    if (inMap[x + 1][y - 1] != 0) inMap[x + 1][y - 1] = id;
                    if (inMap[x - 1][y + 1] != 0) inMap[x - 1][y + 1] = id;
                    if (inMap[x - 1][y - 1] != 0) inMap[x - 1][y - 1] = id;
                }
            }
        }
    }
}
