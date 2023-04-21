package org.octave.graphics;

import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.TextAttribute;
import java.awt.Container;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Utils {

    public static void crossProduct(double ax, double ay, double az, double bx, double by, double bz, double[] res) {
        crossProduct(ax, ay, az, bx, by, bz, res, 0, 1);
    }

    public static void crossProduct(double ax, double ay, double az, double bx, double by, double bz, double[] res, int offset, int ldr) {
        res[offset + 0 * ldr] += (ay * bz - az * by);
        res[offset + 1 * ldr] += (az * bx - ax * bz);
        res[offset + 2 * ldr] += (ax * by - ay * bx);
    }

    public static void printCpuTime() {
        System.out.println(java.lang.management.ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
    }

    public static double[][] getAlphaData(ArrayProperty adata, RadioProperty mapping, AxesObject axes) {
        if (adata.getNDims() != 2) return null;
        if (mapping.is("none")) {
            if (adata.isType("double")) return adata.asDoubleMatrix();
        } else if (mapping.is("direct")) {
            double[] amap = axes.getFigure().Alphamap.getArray();
            if (adata.isType("double")) {
                double[][] aa = adata.asDoubleMatrix();
                double[][] res = new double[aa.length][aa[0].length];
                for (int i = 0; i < res.length; i++) for (int j = 0; j < res[i].length; j++) res[i][j] = amap[(int) Math.min(Math.max(1, aa[i][j]), amap.length) - 1];
                return res;
            } else if (adata.isType("integer")) {
                int[][] aa = adata.asIntMatrix();
                double[][] res = new double[aa.length][aa[0].length];
                for (int i = 0; i < res.length; i++) for (int j = 0; j < res[i].length; j++) res[i][j] = amap[Math.min(Math.max(0, aa[i][j]), amap.length - 1)];
                return res;
            }
        } else if (mapping.is("scaled")) {
            double[] amap = axes.getFigure().Alphamap.getArray();
            double[] alim = axes.ALim.getArray();
            if (adata.isType("double")) {
                double[][] aa = adata.asDoubleMatrix();
                double[][] res = new double[aa.length][aa[0].length];
                for (int i = 0; i < aa.length; i++) for (int j = 0; j < aa[0].length; j++) {
                    double s = (aa[i][j] - alim[0]) / (alim[1] - alim[0]);
                    res[i][j] = amap[(int) Math.round((amap.length - 1) * s)];
                }
                return res;
            }
        }
        return null;
    }

    public static float getFontSize(DoubleProperty FontSize, RadioProperty FontUnits, double h) {
        float fs = 12;
        if (FontUnits.is("points")) fs = FontSize.floatValue(); else if (FontUnits.is("normalized")) fs = (float) (FontSize.doubleValue() * h * 72.0 / Utils.getScreenResolution()); else if (FontUnits.is("inches")) fs = FontSize.floatValue() * 72; else if (FontUnits.is("centimeters")) fs = (FontSize.floatValue() / 2.54f) * 72; else System.out.println("Warning: ignoring FontUnits (" + FontUnits.getValue() + ")");
        return fs;
    }

    public static Font getFont(StringProperty FontName, DoubleProperty FontSize, RadioProperty FontUnits, RadioProperty FontAngle, RadioProperty FontWeight, double h) {
        Map map = new HashMap();
        map.put(TextAttribute.FAMILY, FontName.toString());
        map.put(TextAttribute.POSTURE, FontAngle.is("normal") ? TextAttribute.POSTURE_REGULAR : TextAttribute.POSTURE_OBLIQUE);
        map.put(TextAttribute.WEIGHT, FontWeight.is("normal") ? TextAttribute.WEIGHT_REGULAR : FontWeight.is("light") ? TextAttribute.WEIGHT_LIGHT : FontWeight.is("demi") ? TextAttribute.WEIGHT_SEMIBOLD : TextAttribute.WEIGHT_BOLD);
        float fs = getFontSize(FontSize, FontUnits, h);
        map.put(TextAttribute.SIZE, new Float(Math.round(fs * Utils.getScreenResolution() / 72.0)));
        return new Font(map);
    }

    public static Rectangle getScreenRectangle() {
        return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static int getScreenResolution() {
        return Toolkit.getDefaultToolkit().getScreenResolution();
    }

    public static double[] convertPosition(double[] pos, String fromUnits, String toUnits, Component parent) {
        double[] p = null;
        boolean isContainer = (parent != null && parent instanceof Container);
        if (fromUnits.equalsIgnoreCase("pixels")) p = (double[]) pos.clone(); else if (fromUnits.equalsIgnoreCase("normalized")) {
            Insets ir = (isContainer ? ((Container) parent).getInsets() : new Insets(0, 0, 0, 0));
            Rectangle r = (parent != null ? parent.getBounds() : getScreenRectangle());
            int w = r.width - ir.left - ir.right, h = r.height - ir.top - ir.bottom;
            p = new double[] { pos[0] * w + 1, pos[1] * h + 1, pos[2] * w, pos[3] * h };
        } else if (fromUnits.equalsIgnoreCase("characters")) {
            FontMetrics fm = (parent != null ? parent.getFontMetrics(Font.decode("")) : Toolkit.getDefaultToolkit().getFontMetrics(Font.decode("")));
            double f = 1.0;
            double w = fm.charWidth('x') * f, h = fm.getHeight() * f;
            p = new double[] { pos[0] * w + 1, pos[1] * h + 1, pos[2] * w, pos[3] * h };
        } else if (fromUnits.equalsIgnoreCase("points")) {
            double f = getScreenResolution() / 72.0;
            p = new double[] { pos[0] * f + 1, pos[1] * f + 1, pos[2] * f, pos[3] * f };
        } else if (fromUnits.equalsIgnoreCase("inches")) {
            double f = getScreenResolution();
            p = new double[] { pos[0] * f + 1, pos[1] * f + 1, pos[2] * f, pos[3] * f };
        } else if (fromUnits.equalsIgnoreCase("centimeters")) {
            double f = getScreenResolution() / 2.54;
            p = new double[] { pos[0] * f + 1, pos[1] * f + 1, pos[2] * f, pos[3] * f };
        }
        if (!toUnits.equalsIgnoreCase("pixels")) {
            if (toUnits.equalsIgnoreCase("normalized")) {
                Insets ir = (isContainer ? ((Container) parent).getInsets() : new Insets(0, 0, 0, 0));
                Rectangle r = (parent != null ? parent.getBounds() : getScreenRectangle());
                int w = r.width - ir.left - ir.right, h = r.height - ir.top - ir.bottom;
                p[0] = (p[0] - 1) / w;
                p[1] = (p[1] - 1) / h;
                p[2] /= w;
                p[3] /= h;
            } else if (toUnits.equalsIgnoreCase("characters")) {
                FontMetrics fm = (parent != null ? parent.getFontMetrics(Font.decode("")) : Toolkit.getDefaultToolkit().getFontMetrics(Font.decode("")));
                double f = 1.0;
                double w = fm.charWidth('x') * f, h = fm.getHeight() * f;
                p[0] = (p[0] - 1) / w;
                p[1] = (p[1] - 1) / h;
                p[2] /= w;
                p[3] /= h;
            } else if (toUnits.equalsIgnoreCase("inches")) {
                double f = getScreenResolution();
                p[0] = (p[0] - 1) / f;
                p[1] = (p[1] - 1) / f;
                p[2] /= f;
                p[3] /= f;
            } else if (toUnits.equalsIgnoreCase("centimeters")) {
                double f = getScreenResolution() / 2.54;
                p[0] = (p[0] - 1) / f;
                p[1] = (p[1] - 1) / f;
                p[2] /= f;
                p[3] /= f;
            } else if (toUnits.equalsIgnoreCase("inches")) {
                double f = getScreenResolution() / 72.0;
                p[0] = (p[0] - 1) / f;
                p[1] = (p[1] - 1) / f;
                p[2] /= f;
                p[3] /= f;
            }
        }
        return p;
    }

    private static Color getUIColor(String name, Color def) {
        try {
            return (Color) UIManager.get(name);
        } catch (Exception e) {
            return def;
        }
    }

    public static Color getBackgroundColor() {
        return getUIColor("control", Color.lightGray);
    }

    public static Color getShadowColor() {
        return getUIColor("controlShadow", Color.gray);
    }

    public static Color getHighlightColor() {
        return getUIColor("controlLtHighlight", Color.white);
    }

    public static String join(String[] items, String sepstr) {
        String new_str = "";
        for (int i = 0; i < items.length; i++) {
            if (i != 0) new_str += sepstr;
            new_str += items[i];
        }
        return new_str;
    }

    public static Icon loadIcon(String name) {
        return new ImageIcon(Utils.class.getResource("/org/octave/graphics/images/" + name + ".png"));
    }

    public static boolean isNaNorInf(double x) {
        return (Double.isInfinite(x) || Double.isNaN(x));
    }

    public static boolean isNaNorInf(double x, double y, double z) {
        return (Double.isInfinite(x) || Double.isNaN(x) || Double.isInfinite(y) || Double.isNaN(y) || Double.isInfinite(z) || Double.isNaN(z));
    }

    private static java.text.DecimalFormat hFmt = new java.text.DecimalFormat("0.0000");

    public static String handleToString(double handle) {
        return hFmt.format(handle);
    }

    private static double[][] pentagramPoints;

    public static double[][] getPentagramPoints() {
        if (pentagramPoints == null) {
            pentagramPoints = new double[10][2];
            double f = Math.sin(Math.PI / 10) / Math.sin(3 * Math.PI / 10);
            boolean flag = true;
            for (int i = 0; i < 10; i++, flag = !flag) {
                double ang = Math.PI / 2 + 2 * i * Math.PI / 10;
                double r = (flag ? 1 : f);
                pentagramPoints[i][0] = r * Math.cos(ang);
                pentagramPoints[i][1] = r * Math.sin(ang);
            }
        }
        return pentagramPoints;
    }

    private static double[][] hexagramPoints;

    public static double[][] getHexagramPoints() {
        if (hexagramPoints == null) {
            hexagramPoints = new double[12][2];
            double f = 1 / Math.sqrt(3);
            boolean flag = true;
            for (int i = 0; i < 12; i++, flag = !flag) {
                double ang = Math.PI / 2 + 2 * i * Math.PI / 12;
                double r = (flag ? 1 : f);
                hexagramPoints[i][0] = r * Math.cos(ang);
                hexagramPoints[i][1] = r * Math.sin(ang);
            }
        }
        return hexagramPoints;
    }
}
