package com.pcmsolutions.device.EMU.E4.zcommands.icons;

import com.pcmsolutions.device.EMU.E4.gui.colors.UIColors;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * User: paulmeehan
 * Date: 03-May-2004
 * Time: 18:39:11
 */
public class ReverseIcon extends ParameterZCommandIcon {

    public static final ReverseIcon INSTANCE = new ReverseIcon(true);

    private boolean left;

    ReverseIcon(boolean left) {
        this.left = left;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints hints = g2d.getRenderingHints();
        g2d.setRenderingHints(UIColors.iconRH);
        GeneralPath p = new GeneralPath();
        final float rx = x + inset;
        final float ry = y + inset;
        p.moveTo(rx, ry + (height * 3) / 4);
        p.lineTo(rx + width, ry + (height * 3) / 4);
        p.lineTo(rx + width, ry + height / 4);
        p.lineTo(rx, ry + height / 4);
        g2d.setPaint(c2);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(p);
        Point2D point;
        if (left) point = p.getCurrentPoint(); else point = p.getCurrentPoint();
        int aw = 3;
        g2d.drawLine((int) point.getX() + aw, (int) point.getY() - aw, (int) point.getX(), (int) point.getY());
        g2d.drawLine((int) point.getX(), (int) point.getY(), (int) point.getX() + aw, (int) point.getY() + aw);
        g2d.setRenderingHints(hints);
    }
}
