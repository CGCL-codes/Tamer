package de.georg_gruetter.xhsi.panel;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class RadioHeadingArrowsHelper {

    public static void draw_nav1_forward_arrow(Graphics2D g2, int x, int y, int length, int base_width) {
        GeneralPath polyline;
        int arrow_top_y = y - (base_width / 3);
        int arrow_bottom_y = arrow_top_y + length;
        g2.drawLine(x, arrow_top_y, x, arrow_bottom_y);
        g2.drawLine(x - (base_width / 4), arrow_bottom_y - length / 8, x + (base_width / 4), arrow_bottom_y - length / 8);
        polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
        polyline.moveTo(x - (base_width / 2), y);
        polyline.lineTo(x, arrow_top_y);
        polyline.lineTo(x + (base_width / 2), y);
        g2.draw(polyline);
    }

    public static void draw_nav1_backward_arrow(Graphics2D g2, int x, int y, int length, int base_width) {
        GeneralPath polyline;
        int arrow_top_y = y - length + (base_width / 3);
        int arrow_bottom_y = arrow_top_y + length;
        g2.drawLine(x, arrow_top_y, x, arrow_bottom_y);
        polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
        polyline.moveTo(x - (base_width / 2), arrow_bottom_y);
        polyline.lineTo(x, y);
        polyline.lineTo(x + (base_width / 2), arrow_bottom_y);
        g2.draw(polyline);
    }

    public static void draw_nav2_forward_arrow(Graphics2D g2, int x, int y, int length, int base_width) {
        GeneralPath polyline;
        int arrow_top_y = y - (base_width / 4);
        int arrow_bottom_y = arrow_top_y + length;
        polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 9);
        polyline.moveTo(x - (base_width / 2), arrow_bottom_y);
        polyline.lineTo(x - (base_width / 2), arrow_bottom_y - (base_width / 4));
        polyline.lineTo(x - (base_width / 4), arrow_bottom_y - (base_width / 4));
        polyline.lineTo(x - (base_width / 4), y);
        polyline.lineTo(x, arrow_top_y);
        polyline.lineTo(x + (base_width / 4), y);
        polyline.lineTo(x + (base_width / 4), arrow_bottom_y - (base_width / 4));
        polyline.lineTo(x + (base_width / 2), arrow_bottom_y - (base_width / 4));
        polyline.lineTo(x + (base_width / 2), arrow_bottom_y);
        polyline.lineTo(x - (base_width / 2), arrow_bottom_y);
        g2.draw(polyline);
    }

    public static void draw_nav2_backward_arrow(Graphics2D g2, int x, int y, int length, int base_width) {
        GeneralPath polyline;
        int arrow_top_y = y - length + (base_width / 3);
        int arrow_bottom_y = arrow_top_y + length;
        polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 9);
        polyline.moveTo(x - (base_width / 2), arrow_bottom_y);
        polyline.lineTo(x - (base_width / 2), y);
        polyline.lineTo(x - (base_width / 4), y - (base_width / 4));
        polyline.lineTo(x - (base_width / 4), arrow_top_y + (base_width / 4));
        polyline.lineTo(x, arrow_top_y);
        polyline.lineTo(x + (base_width / 4), arrow_top_y + (base_width / 4));
        polyline.lineTo(x + (base_width / 4), y - (base_width / 4));
        polyline.lineTo(x + (base_width / 2), y);
        polyline.lineTo(x + (base_width / 2), arrow_bottom_y);
        polyline.lineTo(x, y);
        polyline.lineTo(x - (base_width / 2), arrow_bottom_y);
        g2.draw(polyline);
    }
}
