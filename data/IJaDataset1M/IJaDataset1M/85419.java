package core.math;

import java.awt.Point;
import java.awt.Polygon;
import core.ImageFunctions;

/**
 * Based on code provided by Convex Hull Plus by Gabriel Landini, 12/Sep/2004.
 * G.Landini at bham. ac. uk
 * 
 * @author Jens Bache-Wiig
 * 
 */
public class ConvexAnalyzer {

    /**
	 * A method that calculates the solidity of any continuous non-zero region
	 * 
	 * Solidity = area/convex area
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
    public static double getSolidity(short[] data, int width, int height) {
        Polygon pout = getConvexRegion(data, width, height);
        int cnt = 0;
        int convcnt = 0;
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (data[xx + yy * width] != 0) {
                    cnt++;
                }
                if (pout.contains(xx, yy)) convcnt++;
            }
        }
        if (convcnt == 0) return 0;
        return (cnt / (double) convcnt);
    }

    public static Polygon getConvexRegion(short[] data, int width, int height) {
        int counter = 0;
        int i, j, k = 0, m;
        for (j = 0; j < width; j++) {
            for (i = 0; i < height; i++) {
                if (data[j + i * width] != 0) counter++;
            }
        }
        int[] x = new int[counter + 1];
        int[] y = new int[counter + 1];
        for (j = 0; j < width; j++) {
            for (i = 0; i < height; i++) {
                if (data[j + i * width] != 0) {
                    x[k] = i;
                    y[k] = j;
                    k++;
                }
            }
        }
        int n = counter, min = 0, ney = 0, h, h2, dx, dy, temp, ax, ay;
        double minangle, th, t, v, zxmi = 0;
        for (i = 1; i < n; i++) {
            if (y[i] < y[min]) min = i;
        }
        temp = x[0];
        x[0] = x[min];
        x[min] = temp;
        temp = y[0];
        y[0] = y[min];
        y[min] = temp;
        min = 0;
        for (i = 1; i < n; i++) {
            if (y[i] == y[0]) {
                ney++;
                if (x[i] < x[min]) min = i;
            }
        }
        temp = x[0];
        x[0] = x[min];
        x[min] = temp;
        temp = y[0];
        y[0] = y[min];
        y[min] = temp;
        min = 0;
        m = -1;
        x[n] = x[min];
        y[n] = y[min];
        if (ney > 0) minangle = -1; else minangle = 0;
        while (min != n + 0) {
            m = m + 1;
            temp = x[m];
            x[m] = x[min];
            x[min] = temp;
            temp = y[m];
            y[m] = y[min];
            y[min] = temp;
            min = n;
            v = minangle;
            minangle = 360.0;
            h2 = 0;
            for (i = m + 1; i < n + 1; i++) {
                dx = x[i] - x[m];
                ax = Math.abs(dx);
                dy = y[i] - y[m];
                ay = Math.abs(dy);
                if (dx == 0 && dy == 0) t = 0.0; else t = (double) dy / (double) (ax + ay);
                if (dx < 0) t = 2.0 - t; else {
                    if (dy < 0) t = 4.0 + t;
                }
                th = t * 90.0;
                if (th > v) {
                    if (th < minangle) {
                        min = i;
                        minangle = th;
                        h2 = dx * dx + dy * dy;
                    } else {
                        if (th == minangle) {
                            h = dx * dx + dy * dy;
                            if (h > h2) {
                                min = i;
                                h2 = h;
                            }
                        }
                    }
                }
            }
            zxmi = zxmi + Math.sqrt(h2);
        }
        m++;
        int[] hx = new int[m];
        int[] hy = new int[m];
        for (i = 0; i < m; i++) {
            hx[i] = x[i];
            hy[i] = y[i];
        }
        Polygon pout = new Polygon(hy, hx, hx.length);
        return pout;
    }

    /**
	 * A method that calculates the convexity of any continuous region
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
    public static double getConvexity(short[] data, int width, int height) {
        Polygon pout = getConvexRegion(data, width, height);
        int cnt = 0;
        int convcnt = 0;
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (data[xx + yy * width] != 0) {
                    cnt++;
                }
                if (pout.contains(xx, yy)) convcnt++;
            }
        }
        Point start = null;
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (data[xx + yy * width] != 0) {
                    start = new Point(xx, yy);
                    xx = width;
                    yy = height;
                }
            }
        }
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (data[xx + yy * width] != 0) data[xx + yy * width] = (short) 150;
            }
        }
        if (start == null) {
            System.out.println("No region found!");
            return 0.0;
        }
        int count1 = ImageFunctions.countOutlinePixels(data, start.x, start.y, width, height, (short) 100);
        short[] data2 = new short[data.length];
        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (pout.contains(xx, yy)) data2[xx + yy * width] = (short) 0x150; else data2[xx + yy * width] = 0;
            }
        }
        int count2 = ImageFunctions.countOutlinePixels(data2, start.x, start.y, width, height, (short) 100);
        return (count2 / (double) count1);
    }
}
