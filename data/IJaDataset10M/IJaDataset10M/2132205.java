package auo.cms.test;

import shu.math.*;
import java.util.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FRCResolutionTester {

    public static void main(String[] args) {
        test3(args);
    }

    static double mindist;

    static double[] minxy = null;

    static double[] averagexy = new double[] { 5, 8 };

    static int[] getBestRGB(int r, int g, int b) {
        return getBestRGB(r, g, b, 0);
    }

    static int[] getBestRGB(int r, int g, int b, int quadrant) {
        if (r == 0 && g == 0 && b == 0) {
            return new int[] { 0, 0, 0 };
        }
        if ((r != 0 && g == 0 && b == 0) || (r == 0 && g != 0 && b == 0)) {
            quadrant = 0;
        }
        mindist = Double.MAX_VALUE;
        double[] preminxy = null;
        minxy = null;
        int[] bestRGB = null;
        for (int rr = 0; rr < (r != 0 ? 2 : 1); rr++) {
            for (int gg = 0; gg < (g != 0 ? 2 : 1); gg++) {
                for (int bb = 0; bb < (b != 0 ? 2 : 1); bb++) {
                    int rrr = r != 0 ? r * 2 - 1 + rr : 0;
                    int ggg = g != 0 ? g * 2 - 1 + gg : 0;
                    int bbb = b != 0 ? b * 2 - 1 + bb : 0;
                    double[] xy = getxy(rrr, ggg, bbb);
                    int nowQuadrant = getQuadrant(xy[0], xy[1]);
                    double dist = Math.sqrt(Maths.sqr(averagexy[0] * xy[0]) + Maths.sqr(averagexy[1] * xy[1]));
                    if ((nowQuadrant == quadrant && 0 != nowQuadrant && -1 != nowQuadrant) || 0 == quadrant) {
                        if (preminxy != null) {
                            if (dist < mindist) {
                                mindist = dist;
                                minxy = preminxy = xy;
                                bestRGB = new int[] { rrr, ggg, bbb };
                            }
                        } else {
                            mindist = dist;
                            minxy = preminxy = xy;
                            bestRGB = new int[] { rrr, ggg, bbb };
                        }
                    }
                }
            }
        }
        return bestRGB;
    }

    public static void test3(String[] args) {
        int[] bestRGB0 = getBestRGB(3, 1, 3, 1);
        int nullcount = 0;
        double totaldist = 0;
        for (int r = 0; r <= 3; r++) {
            for (int g = 0; g <= 3; g++) {
                for (int b = 0; b <= 3; b++) {
                    int[] bestRGB = getBestRGB(r, g, b, 3);
                    System.out.println(r + " " + g + " " + b + " " + Arrays.toString(bestRGB) + " \t" + Arrays.toString(minxy) + " " + (minxy != null ? getQuadrant(minxy[0], minxy[1]) : ""));
                    if (null == bestRGB) {
                        nullcount++;
                    } else {
                        totaldist += mindist;
                    }
                }
            }
        }
        System.out.println("nullcount: " + nullcount);
        System.out.println("totaldist: " + totaldist);
    }

    static double[] getxy(int r, int g, int b) {
        int[] rdist = new int[] { 4, 0 };
        int[] gdist = new int[] { 0, 6 };
        int[] bdist = new int[] { -4, -8 };
        double x = 0, y = 0;
        if (r == 1 || r == 3 || r == 5) {
            int rr = (r + 1) / 2;
            x -= (rdist[0] / 4.) * rr;
        } else if (r == 2 || r == 4 || r == 6) {
            int rr = 4 - r / 2;
            x += (rdist[0] / 4.) * rr;
        }
        if (g == 1 || g == 3 || g == 5) {
            int rr = (g + 1) / 2;
            y -= (gdist[1] / 4.) * rr;
        } else if (g == 2 || g == 4 || g == 6) {
            int rr = 4 - g / 2;
            y += (gdist[1] / 4.) * rr;
        }
        if (b == 1 || b == 3 || b == 5) {
            int rr = (b + 1) / 2;
            x -= (bdist[0] / 4.) * rr;
            y -= (bdist[1] / 4.) * rr;
        } else if (b == 2 || b == 4 || b == 6) {
            int rr = 4 - b / 2;
            x += (bdist[0] / 4.) * rr;
            y += (bdist[1] / 4.) * rr;
        }
        return new double[] { x, y };
    }

    public static void test2(String[] args) {
        int index = 1;
        for (int r = 0; r <= 6; r++) {
            for (int g = 0; g <= 6; g++) {
                for (int b = 0; b <= 6; b++) {
                    double[] xy = getxy(r, g, b);
                    double x = xy[0], y = xy[1];
                    System.out.println(index++ + ": x:" + x + "\ty:" + y + " \t" + getQuadrant(x, y));
                }
            }
        }
    }

    public static void test1(String[] args) {
        int[] rdist = new int[] { 4, 0 };
        int[] gdist = new int[] { 0, 6 };
        int[] bdist = new int[] { -4, -8 };
        int index = 1;
        for (int r = 0; r <= 3; r++) {
            for (int g = 0; g <= 3; g++) {
                for (int b = 0; b <= 3; b++) {
                    for (int rr = 0; rr <= (r == 2 ? 1 : 0); rr++) {
                        for (int gg = 0; gg <= (g == 2 ? 1 : 0); gg++) {
                            for (int bb = 0; bb <= (b == 2 ? 1 : 0); bb++) {
                                String rt = (r == 2) ? (rr == 0) ? "R+" : "R-" : "   ";
                                String gt = (g == 2) ? (gg == 0) ? "G+" : "G-" : "   ";
                                String bt = (b == 2) ? (bb == 0) ? "B+" : "B-" : "   ";
                                double x = 0, y = 0;
                                if (r == 1 || r == 3) {
                                    x += rdist[0] / 4. * (r - 2);
                                }
                                if (g == 1 || g == 3) {
                                    y += gdist[1] / 4. * (g - 2);
                                }
                                if (b == 1 || b == 3) {
                                    x += (bdist[0] / 4.) * (b - 2);
                                    y += (bdist[1] / 4.) * (b - 2);
                                }
                                if (r == 2) {
                                    x += (rr * 2 - 1) * rdist[0] / 2.;
                                }
                                if (g == 2) {
                                    y += (gg * 2 - 1) * gdist[1] / 2.;
                                }
                                if (b == 2) {
                                    x += (1 - bb * 2) * bdist[0] / 2.;
                                    y += (1 - bb * 2) * bdist[1] / 2.;
                                }
                                int quadrant = getQuadrant(x, y);
                                if (-1 != quadrant && 0 != quadrant) {
                                    System.out.print((index++) + ": " + r + " " + g + " " + b + " " + rt + gt + bt);
                                    System.out.println("\t" + quadrant + "\tx:" + x + " y:" + y);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static int getQuadrant(double x, double y) {
        if (x == 0 && y == 0) {
            return 0;
        } else if (x > 0 && y > 0) {
            return 1;
        } else if (x < 0 && y > 0) {
            return 2;
        } else if (x < 0 && y < 0) {
            return 3;
        } else if (x > 0 && y < 0) {
            return 4;
        } else {
            return -1;
        }
    }
}
