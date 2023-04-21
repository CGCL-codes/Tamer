package becta.viewer.controls;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.plaf.basic.BasicButtonUI;
import becta.viewer.accessibility.AccessibilityColor;
import becta.viewer.drawing.AnnotationStroke;

public class ExtendedButton extends ToggleButtonModel {

    public ExtendedButton(Object data) {
        this.data = data;
    }

    private Object data;

    public static ExtendedButtonUI ButtonUI = new ExtendedButtonUI();

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

class ExtendedButtonUI extends BasicButtonUI {

    static BasicStroke highlightStroke = new BasicStroke(3);

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        ExtendedButton b = (ExtendedButton) ((JRadioButton) c).getModel();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(AccessibilityColor.window);
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
        Stroke old = g2.getStroke();
        if (b.isSelected()) {
            g.setColor(AccessibilityColor.selectionFillColor);
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            g2.setStroke(highlightStroke);
            g.setColor(AccessibilityColor.selectionBorderColor);
        } else if (c.hasFocus()) {
            g2.setStroke(highlightStroke);
            g.setColor(AccessibilityColor.selectionBorderColor);
        } else {
            g.setColor(AccessibilityColor.controlShadow);
        }
        g2.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
        g2.setStroke(old);
        if (b.getData() instanceof AnnotationStroke) {
            AnnotationStroke astroke = (AnnotationStroke) b.getData();
            BasicStroke stroke = new BasicStroke(astroke.getWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(stroke);
            if (astroke.getColour().getAlpha() != 255) g2.setColor(new Color(astroke.getColour().getRed(), astroke.getColour().getGreen(), astroke.getColour().getBlue())); else g2.setColor(astroke.getColour());
            g2.drawLine(7, 10, 15, 10);
            g2.setStroke(oldStroke);
            if (astroke.getColour().getAlpha() != 255) {
                g2.setColor(new Color(0, 0, 0, 51));
                Polygon a = new Polygon();
                a.addPoint(0, 0);
                a.addPoint(c.getWidth(), 0);
                a.addPoint(0, c.getHeight());
                g2.fillPolygon(a);
            }
        } else {
            g2.setColor((Color) b.getData());
            g2.fillOval(2, 2, c.getWidth() - 4, c.getHeight() - 4);
        }
    }
}
