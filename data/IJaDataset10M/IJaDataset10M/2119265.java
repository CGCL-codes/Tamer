package javamorph;

import java.io.*;
import java.awt.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * File belongs to javamorph (Merging of human-face-pictures).
 * Copyright (C) 2009 - 2010  Claus Wimmer
 * See file ".../help/COPYING" for details!
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA * 
 *
 * @version 1.5
 * <br/>
 * @author claus.erhard.wimmer@googlemail.com
 * <br/>
 * Program: JavaMorph.
 * <br/>
 * Class: CMorphOperator.
 * <br/>
 * License: GPLv2.
 * <br/>
 * Description: Morph the result from left input to right input depending on
 * the ratio parameter.
 * <br/>
 * Hint: Writes the result into the working directory.
 */
public class CMorphOperator implements Runnable {

    /** Application's main class. */
    private static CMain parent;

    /** 
     * If <code>0.0</code then output is the left image, if <code>1.0</code>
     * then output is the right image. Every value between them leads to a
     * merged image.
     */
    private static double ratio;

    /** Current point coordinates of the left image. */
    private static Point left_point = new Point();

    /** Current point coordinates of the right image. */
    private static Point right_point = new Point();

    /** Current point coordinates of the result image. */
    private static Point result_point = new Point();

    /** RGB value of the current left pixel. */
    private static int left_pixel;

    /** RGB value of the current right pixel. */
    private static int right_pixel;

    /** RGB value of the current result pixel. */
    private static int result_pixel;

    /** Transformation matrix from result to left point. */
    private static CTransform left_trafo;

    /** Transformation matrix from result to right point. */
    private static CTransform right_trafo;

    /** Index of the current triangle within all three lists. */
    private static int t_idx;

    /** List of result points situated within the current result triangle. */
    private static Point withins[];

    /** Polygon clip ratio of the current left pixel. */
    private static double left_ratio;

    /** Polygon clip ratio of the current right pixel. */
    private static double right_ratio;

    /** If <code>true</code> the user forces the morph process to abort. */
    private static boolean f_break;

    /** Instance of the progress bar. */
    private static CProgress progress;

    /**
     * Initialize static class components.
     * 
     * @param parent Main JFrame.
     * @param progress Progress bar.
     */
    public static void morph(CMain parent, CProgress progress) {
        CMorphOperator.parent = parent;
        CMorphOperator.progress = progress;
    }

    /**
     * Enable abort of the morph process forced by user.
     */
    public static void doBreak() {
        f_break = true;
    }

    /**
     * Thread API. Starts morph batch for a number of intermediate pictures
     * with increasing ratio value.
     */
    public void run() {
        f_break = false;
        try {
            for (int i = 0; (i <= CConfig.NUM_OF_MORPH_STEPS) && (!f_break); ++i) {
                for (int x = 0; x < CConfig.result_image.getWidth(); ++x) {
                    for (int y = 0; y < CConfig.result_image.getHeight(); ++y) {
                        CConfig.result_image.setRGB(x, y, 0x0);
                    }
                }
                ratio = ((double) i / CConfig.NUM_OF_MORPH_STEPS);
                genResultTriangles();
                for (t_idx = 0; t_idx < CConfig.result_triangles.size(); ++t_idx) {
                    triangle();
                }
                File f = new File(CStrings.getOutput(i));
                ImageIO.write(CConfig.result_image, "jpg", f);
                progress.setProgress(i, 0, CConfig.NUM_OF_MORPH_STEPS);
                Thread.sleep(1);
            }
            progress.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Can't save result. Please see console output!");
        }
    }

    /**
     * Make a weighted average mesh depending on the current ratio.
     */
    private static void genResultTriangles() {
        CConfig.result_triangles.clear();
        for (int i = 0; i < CConfig.left_triangles.size(); ++i) {
            CTriangle r = CConfig.left_triangles.get(i), s = CConfig.right_triangles.get(i), t = new CTriangle(merge(r.getPoints()[0], s.getPoints()[0]), merge(r.getPoints()[1], s.getPoints()[1]), merge(r.getPoints()[2], s.getPoints()[2]));
            CConfig.result_triangles.add(t);
        }
    }

    /** Merge two points weighted by ratio.
     * 
     * @param p1 First point.
     * @param p2 Second point.
     * @return Point on a line between them.
     */
    private static Point merge(Point p1, Point p2) {
        return new Point((int) (p1.x * (1.0 - ratio) + p2.x * ratio), (int) (p1.y * (1.0 - ratio) + p2.y * ratio));
    }

    /**
     * Merge all points of a triangle.
     */
    private static void triangle() {
        CTriangle result = CConfig.result_triangles.get(t_idx);
        left_trafo = CGeo.getTrafo(CConfig.left_triangles.get(t_idx), result);
        right_trafo = CGeo.getTrafo(CConfig.right_triangles.get(t_idx), result);
        withins = result.getWithins();
        for (Point p : withins) {
            result_point = p;
            left_point = CGeo.getOrigin_(result_point, left_trafo);
            right_point = CGeo.getOrigin_(result_point, right_trafo);
            merge();
        }
    }

    /**
     * Merge (left.pixel, right.pixel)->(result.pixel). Result depends on
     * ratio value & both polygon matrixes.
     */
    private static void merge() {
        try {
            left_pixel = CConfig.left_image.getRGB(left_point.x, left_point.y);
            right_pixel = CConfig.right_image.getRGB(right_point.x, right_point.y);
            left_ratio = CConfig.left_clip[left_point.x][left_point.y];
            right_ratio = CConfig.right_clip[right_point.x][right_point.y];
            double t1 = left_ratio, t2 = 1.0 - left_ratio, t3 = 1.0 - right_ratio, t4 = right_ratio, fl = t3 + (1.0 - ratio) * (t1 - t3), fr = t2 + ratio * (t4 - t2);
            int l_r = (left_pixel & 0xffff0000) >> 16, r_r = (right_pixel & 0xffff0000) >> 16, l_g = (left_pixel & 0xff00ff00) >> 8, r_g = (right_pixel & 0xff00ff00) >> 8, l_b = left_pixel & 0xff0000ff, r_b = right_pixel & 0xff0000ff, r = (int) (l_r * fl + r_r * fr), g = (int) (l_g * fl + r_g * fr), b = (int) (l_b * fl + r_b * fr);
            result_pixel = (0xff000000) | (r << 16) | (g << 8) | b;
            CConfig.result_image.setRGB(result_point.x, result_point.y, result_pixel);
        } catch (Exception e) {
        }
    }
}
