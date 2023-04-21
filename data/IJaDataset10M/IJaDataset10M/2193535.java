package org.silentsquare.codejam.y2008.qualification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Algorithm
 * THIS DOES NOT WORK
 * 
 * @author wjfang
 *
 */
public class FlySwatter2 {

    private static final String PATH = "./src/org/silentsquare/codejam/y2008/qualification/";

    private Scanner in;

    private PrintWriter out;

    public void solveSmall() throws IOException {
        _solve("small");
    }

    public void solveLarge() throws IOException {
        _solve("large");
    }

    public void _solve(String name) throws IOException {
        System.out.println("Solving the " + name + " dataset ...");
        long begin = System.currentTimeMillis();
        in = new Scanner(new File(PATH + "C-" + name + "-practice.in"));
        out = new PrintWriter(new BufferedWriter(new FileWriter(PATH + "C-" + name + "-practice.out")));
        int tests = in.nextInt();
        for (int i = 0; i < tests; i++) {
            readTestCase();
            double p = solve();
            out.printf("Case #%d: %f\n", (i + 1), p);
            System.out.printf("Case #%d: %f\n", (i + 1), p);
        }
        in.close();
        out.close();
        System.out.println("Solving the " + name + " dataset: " + (System.currentTimeMillis() - begin) + "ms");
    }

    double f, R, t, r, g;

    private void readTestCase() throws IOException {
        f = in.nextDouble();
        R = in.nextDouble();
        t = in.nextDouble();
        r = in.nextDouble();
        g = in.nextDouble();
    }

    private double solve() {
        double quarter = Math.PI * R * R * 0.25;
        double safe = 0;
        double ll = Math.ceil(R / (g + 2 * r));
        for (double i = 0; i < ll; i++) {
            for (double j = 0; j < ll; j++) {
                safe += safeArea(i, j);
            }
        }
        return 1.0 - safe / quarter;
    }

    class Point {

        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        Point(Point p, double dx, double dy) {
            this.x = p.x + dx;
            this.y = p.y + dy;
        }

        boolean withinRing() {
            return x * x + y * y <= (R - t) * (R - t);
        }
    }

    private double safeArea(double i, double j) {
        Point leftbottom = new Point(r + i * (g + 2 * r), r + j * (g + 2 * r));
        Point lefttop = new Point(leftbottom, 0, g);
        Point rightbottom = new Point(leftbottom, g, 0);
        Point righttop = new Point(leftbottom, g, g);
        if (!leftbottom.withinRing()) return 0; else if (righttop.withinRing()) return (g - f) * (g - f); else {
            int n = 10000;
            double d = (g - f) / n;
            double hit = 0;
            for (int ii = 0; ii < n; ii++) {
                for (int jj = 0; jj < n; jj++) {
                    Point p = new Point(leftbottom, f + d * ii + d / 2, f + d * jj + d / 2);
                    if (p.x * p.x + p.y * p.y <= (R - t - f) * (R - t - f)) hit++;
                }
            }
            return (g - f) * (g - f) * hit / (n * n);
        }
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        new FlySwatter2().solveSmall();
    }
}
