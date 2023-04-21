package org.schwiet.LincolnLog.ui.painters;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import org.schwiet.spill.painter.Painter;

/**
 *
 * @author sethschwiethale
 */
public class TransFormPainter implements Painter<Component> {

    private static final Color OUTER = new Color(200, 235, 240);

    private static final Color CENTER = new Color(131, 157, 173);

    private static final Color SHADE_LITE = new Color(89, 81, 89, 0);

    private static final Color SHADE_DARK = new Color(10, 10, 10, 120);

    private static final Color CURVE_LITE = new Color(170, 215, 230);

    private static final Color CURVE_DARK = new Color(70, 75, 88);

    private static final Color CURVE_MID = new Color(100, 120, 140);

    private static final Color RAD_CENTER = new Color(211, 247, 253, 150);

    private static final Color RAD_OUTER = new Color(255, 255, 255, 0);

    private LinearGradientPaint left_shadow = new LinearGradientPaint(0, 0, 6, 0, new float[] { 0.0f, .2f, .75f, 1.0f }, new Color[] { new Color(45, 45, 45, 170), new Color(100, 100, 100, 130), new Color(100, 100, 100, 50), new Color(230, 230, 230, 230) });

    private static final float[] STOPS = { 0.0f, 1.0f }, CURVE_STOPS = { 0.0f, 0.4f, .7f, 1.0f }, CURVE_STOPS2 = { 0.0f, .5f, .75f, 1.0f };

    private static final Color[] COLORS = { CENTER, OUTER }, CURVE_COLORS = { CURVE_LITE, CURVE_MID, CURVE_LITE, CURVE_DARK }, CURVE_COLORS2 = { CURVE_MID, CURVE_LITE, CURVE_MID, CURVE_LITE }, RAD_COLORS = { RAD_CENTER, RAD_OUTER };

    private LinearGradientPaint gradient, curve_shadow, curve_shine, circle;

    private GradientPaint shadow = new GradientPaint(0, 0, SHADE_DARK, 0, 4, SHADE_LITE);

    private int cachedHeight = -1;

    private int cachedWidth = -1;

    public void paint(Graphics2D graphics, Component objectToPaint, int width, int height) {
        if (gradient == null || curve_shadow == null || curve_shine == null || cachedWidth != width || cachedHeight != height) {
            gradient = new LinearGradientPaint(0, 0, 0, height, STOPS, COLORS);
            curve_shadow = new LinearGradientPaint(0, 0, width, 0, CURVE_STOPS, CURVE_COLORS);
            curve_shine = new LinearGradientPaint(0, 0, width, 0, CURVE_STOPS2, CURVE_COLORS2);
            circle = new LinearGradientPaint(0, 0, 200, 0, STOPS, RAD_COLORS);
            cachedWidth = width;
            cachedHeight = height;
        }
        graphics.setPaint(gradient);
        graphics.fillRect(0, 0, width, height);
        graphics.setPaint(shadow);
        graphics.fillRect(0, 0, width, 5);
        graphics.setPaint(curve_shadow);
        graphics.drawLine(0, height - 1, width, height - 1);
        graphics.setPaint(curve_shine);
        graphics.drawLine(0, height - 2, width, height - 2);
        graphics.setPaint(circle);
        graphics.fillRect(0, 0, 210, height);
        graphics.setPaint(left_shadow);
        graphics.fillRect(0, 0, 6, height - 2);
    }
}
