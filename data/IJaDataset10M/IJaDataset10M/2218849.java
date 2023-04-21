package edu.belmont.mth.visigraph.views.display;

import java.awt.*;
import java.awt.geom.*;
import edu.belmont.mth.visigraph.models.*;
import edu.belmont.mth.visigraph.settings.*;
import edu.belmont.mth.visigraph.utilities.*;

/**
 * @author Cameron Behar
 */
public class CaptionDisplayView {

    public static RoundRectangle2D.Double getEditorRectangle(Caption c) {
        return new RoundRectangle2D.Double(c.x.get() - 25.0, c.y.get() + 14.0, 20.0, 20.0, 10.0, 10.0);
    }

    public static RoundRectangle2D.Double getHandleRectangle(Caption c) {
        return new RoundRectangle2D.Double(c.x.get() - 25.0, c.y.get() - 10.0, 20.0, 20.0, 10.0, 10.0);
    }

    public static void paint(Graphics2D g2D, GraphSettings s, Caption c) {
        g2D.setColor(c.isSelected.get() ? ColorUtilities.blend(UserSettings.instance.captionText.get(), UserSettings.instance.selectedCaptionLine.get()) : UserSettings.instance.captionText.get());
        Font oldFont = g2D.getFont();
        g2D.setFont(new Font(oldFont.getFamily(), oldFont.getStyle(), (int) Math.round(c.size.get())));
        String[] lines = c.text.get().split("\\n");
        for (int i = 0; i < lines.length; ++i) g2D.drawString(lines[i], (c.x.get().floatValue()), (float) (c.y.get() + (i + 0.9f) * c.size.get() - 10));
        g2D.setFont(oldFont);
        if (s.showCaptionHandles.get()) {
            RoundRectangle2D.Double handle = getHandleRectangle(c);
            Point2D.Double center = new Point2D.Double(handle.x + handle.width / 2.0, handle.y + handle.height / 2.0);
            Color fill = (c.isSelected.get() ? ColorUtilities.blend(UserSettings.instance.captionButtonFill.get(), UserSettings.instance.selectedCaptionLine.get()) : UserSettings.instance.captionButtonFill.get());
            Color line = (c.isSelected.get() ? ColorUtilities.blend(UserSettings.instance.captionText.get(), UserSettings.instance.selectedCaptionLine.get()) : UserSettings.instance.captionButtonLine.get());
            RadialGradientPaint radial = new RadialGradientPaint(center, (float) (handle.width / 2.0), new float[] { 0f, 1f }, new Color[] { Color.white, fill });
            g2D.setPaint(radial);
            g2D.fill(handle);
            g2D.setColor(line);
            g2D.draw(handle);
            Path2D.Double path = new Path2D.Double();
            path.moveTo(+4 + center.x, +3 + center.y);
            path.lineTo(+4 + center.x, +1 + center.y);
            path.lineTo(+1 + center.x, +1 + center.y);
            path.lineTo(+1 + center.x, +4 + center.y);
            path.lineTo(+3 + center.x, +4 + center.y);
            path.lineTo(+0.0 + center.x, +7.5 + center.y);
            path.lineTo(-3 + center.x, +4 + center.y);
            path.lineTo(-1 + center.x, +4 + center.y);
            path.lineTo(-1 + center.x, +1 + center.y);
            path.lineTo(-4 + center.x, +1 + center.y);
            path.lineTo(-4 + center.x, +3 + center.y);
            path.lineTo(-7.5 + center.x, +0.0 + center.y);
            path.lineTo(-4 + center.x, -3 + center.y);
            path.lineTo(-4 + center.x, -1 + center.y);
            path.lineTo(-1 + center.x, -1 + center.y);
            path.lineTo(-1 + center.x, -4 + center.y);
            path.lineTo(-3 + center.x, -4 + center.y);
            path.lineTo(+0.0 + center.x, -7.5 + center.y);
            path.lineTo(+3 + center.x, -4 + center.y);
            path.lineTo(+1 + center.x, -4 + center.y);
            path.lineTo(+1 + center.x, -1 + center.y);
            path.lineTo(+4 + center.x, -1 + center.y);
            path.lineTo(+4 + center.x, -3 + center.y);
            path.lineTo(+7.5 + center.x, +0.0 + center.y);
            Stroke oldStroke = g2D.getStroke();
            g2D.setColor(line);
            g2D.setStroke(new BasicStroke(0));
            g2D.fill(path);
            g2D.setStroke(oldStroke);
        }
        if (s.showCaptionEditors.get() && c.isSelected.get()) {
            RoundRectangle2D.Double editor = getEditorRectangle(c);
            Point2D.Double center = new Point2D.Double(editor.x + editor.width / 2.0, editor.y + editor.height / 2.0);
            Color fill = (c.isSelected.get() ? ColorUtilities.blend(UserSettings.instance.captionButtonFill.get(), UserSettings.instance.selectedCaptionLine.get()) : UserSettings.instance.captionButtonFill.get());
            Color line = (c.isSelected.get() ? ColorUtilities.blend(UserSettings.instance.captionText.get(), UserSettings.instance.selectedCaptionLine.get()) : UserSettings.instance.captionButtonLine.get());
            RadialGradientPaint radial = new RadialGradientPaint(center, (float) (editor.width / 2.0), new float[] { 0f, 1f }, new Color[] { Color.white, fill });
            g2D.setPaint(radial);
            g2D.fill(editor);
            g2D.setColor(line);
            g2D.draw(editor);
            Path2D.Double path = new Path2D.Double();
            path.moveTo(+7 + center.x, +7 + center.y);
            path.lineTo(+3 + center.x, +7 + center.y);
            path.lineTo(+0 + center.x, -3 + center.y);
            path.lineTo(-3 + center.x, +7 + center.y);
            path.lineTo(-7 + center.x, +7 + center.y);
            path.lineTo(-2 + center.x, -7 + center.y);
            path.lineTo(+2 + center.x, -7 + center.y);
            path.moveTo(+3 + center.x, +4 + center.y);
            path.lineTo(-3 + center.x, +4 + center.y);
            path.lineTo(-3 + center.x, +1 + center.y);
            path.lineTo(+3 + center.x, +1 + center.y);
            Stroke oldStroke = g2D.getStroke();
            g2D.setColor(line);
            g2D.setStroke(new BasicStroke(0));
            g2D.fill(path);
            g2D.setStroke(oldStroke);
        }
    }

    public static boolean wasEditorClicked(Caption c, Point p, double scale) {
        RoundRectangle2D.Double editor = getEditorRectangle(c);
        editor.x -= UserSettings.instance.captionEditorClickMargin.get() / scale;
        editor.y -= UserSettings.instance.captionEditorClickMargin.get() / scale;
        editor.width += 2.0 * UserSettings.instance.captionEditorClickMargin.get() / scale;
        editor.height += 2.0 * UserSettings.instance.captionEditorClickMargin.get() / scale;
        return editor.contains(p);
    }

    public static boolean wasHandleClicked(Caption c, Point p, double scale) {
        RoundRectangle2D.Double handle = getHandleRectangle(c);
        handle.x -= UserSettings.instance.captionHandleClickMargin.get() / scale;
        handle.y -= UserSettings.instance.captionHandleClickMargin.get() / scale;
        handle.width += 2.0 * UserSettings.instance.captionHandleClickMargin.get() / scale;
        handle.height += 2.0 * UserSettings.instance.captionHandleClickMargin.get() / scale;
        return handle.contains(p);
    }

    public static boolean wasHandleSelected(Caption c, Rectangle r) {
        return getHandleRectangle(c).intersects(r);
    }
}
