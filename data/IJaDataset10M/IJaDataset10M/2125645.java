package fr.esrf.tangoatk.widget.util.interlock.shape;

import java.awt.*;

/** ---------- Storage4 class ---------- */
public class Storage4 {

    private static int[][] xPolys = null;

    private static int[][] yPolys = null;

    private static Color sColor0 = new Color(204, 204, 204);

    private static int[][] xOrgPolys = { { -27, -27, 21, 21 }, { -27, -18, 27, 21 }, { 21, 21, 27, 27 }, { 14, 18, 18, 14 } };

    private static int[][] yOrgPolys = { { -23, 26, 26, -23 }, { -23, -27, -27, -23 }, { -23, 26, 19, -27 }, { -20, -20, -15, -15 } };

    public static void paint(Graphics g, Color backColor, int x, int y, double size) {
        if (xPolys == null) {
            xPolys = new int[xOrgPolys.length][];
            yPolys = new int[yOrgPolys.length][];
            for (int i = 0; i < xOrgPolys.length; i++) {
                xPolys[i] = new int[xOrgPolys[i].length];
                yPolys[i] = new int[yOrgPolys[i].length];
            }
        }
        for (int i = 0; i < xOrgPolys.length; i++) {
            for (int j = 0; j < xOrgPolys[i].length; j++) {
                xPolys[i][j] = (int) ((double) xOrgPolys[i][j] * size + 0.5) + x;
                yPolys[i][j] = (int) ((double) yOrgPolys[i][j] * size + 0.5) + y;
            }
        }
        g.setColor(backColor);
        g.fillPolygon(xPolys[0], yPolys[0], xPolys[0].length);
        g.setColor(Color.black);
        g.drawPolygon(xPolys[0], yPolys[0], xPolys[0].length);
        g.setColor(backColor);
        g.fillPolygon(xPolys[1], yPolys[1], xPolys[1].length);
        g.setColor(Color.black);
        g.drawPolygon(xPolys[1], yPolys[1], xPolys[1].length);
        g.setColor(backColor);
        g.fillPolygon(xPolys[2], yPolys[2], xPolys[2].length);
        g.setColor(Color.black);
        g.drawPolygon(xPolys[2], yPolys[2], xPolys[2].length);
        g.setColor(Color.black);
        g.drawLine((int) (-25.0 * size + 0.5) + x, (int) (23.0 * size + 0.5) + y, (int) (12.0 * size + 0.5) + x, (int) (23.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-25.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-25.0 * size + 0.5) + x, (int) (22.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-25.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (12.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (12.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (12.0 * size + 0.5) + x, (int) (23.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-25.0 * size + 0.5) + x, (int) (23.0 * size + 0.5) + y, (int) (-23.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-23.0 * size + 0.5) + x, (int) (19.0 * size + 0.5) + y, (int) (-23.0 * size + 0.5) + x, (int) (-19.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-23.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y, (int) (12.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-22.0 * size + 0.5) + x, (int) (-10.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (-10.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-22.0 * size + 0.5) + x, (int) (0.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (0.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-22.0 * size + 0.5) + x, (int) (10.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (10.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-19.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-19.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-15.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-15.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-11.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-11.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-7.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-7.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-3.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (-3.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (1.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (1.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (5.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (5.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (9.0 * size + 0.5) + x, (int) (-20.0 * size + 0.5) + y, (int) (9.0 * size + 0.5) + x, (int) (20.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-20.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-21.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-20.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-21.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-20.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-21.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-20.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-21.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-16.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-17.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-16.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-17.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-16.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-17.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-16.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-17.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-12.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-13.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-12.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-13.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-12.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-13.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-12.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-13.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-8.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-9.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-8.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-9.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-8.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-9.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-8.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-9.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-4.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-5.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-4.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-5.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-4.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-5.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (-4.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-5.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (0.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (-1.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (0.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (-1.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (0.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (-1.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (0.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (-1.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (4.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (3.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (4.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (3.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (4.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (3.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (4.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (3.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (8.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (7.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (8.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (7.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (8.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (7.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (8.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (7.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (12.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (-18.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (12.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (12.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (2.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (12.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y, (int) (11.0 * size + 0.5) + x, (int) (12.0 * size + 0.5) + y);
        g.setColor(sColor0);
        g.fillPolygon(xPolys[3], yPolys[3], xPolys[3].length);
        g.setColor(Color.black);
        g.drawPolygon(xPolys[3], yPolys[3], xPolys[3].length);
        g.setColor(Color.black);
        g.drawLine((int) (14.0 * size + 0.5) + x, (int) (-12.0 * size + 0.5) + y, (int) (18.0 * size + 0.5) + x, (int) (-12.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (14.0 * size + 0.5) + x, (int) (-10.0 * size + 0.5) + y, (int) (18.0 * size + 0.5) + x, (int) (-10.0 * size + 0.5) + y);
        g.setColor(Color.black);
        g.drawLine((int) (14.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y, (int) (18.0 * size + 0.5) + x, (int) (-8.0 * size + 0.5) + y);
    }

    public static void setBoundRect(int x, int y, double size, Rectangle bound) {
        bound.setRect((int) (-27.0 * size + 0.5) + x, (int) (-27.0 * size + 0.5) + y, (int) (55.0 * size + 0.5), (int) (54.0 * size + 0.5));
    }
}
