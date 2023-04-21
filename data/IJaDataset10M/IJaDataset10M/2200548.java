package si.mk.k3.view2d;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import si.mk.k3.model.model2d.SVGElement;

/**
 * This class contains drawing information for one 2D primitive -
 * descendent of class Shape, for example Rectangle2D, Ellipse2D, ...
 */
public class SVGElementView {

    protected Shape m_shape;

    private Stroke m_stroke;

    private Paint m_strokePaint;

    private Paint m_fillPaint;

    private String m_text;

    private Font m_font;

    private float m_x;

    private float m_y;

    public SVGElementView(SVGElement e2d) {
        m_stroke = null;
        m_strokePaint = e2d.getStrokeColor();
        if (m_strokePaint != null) {
            m_stroke = new BasicStroke((float) e2d.getStrokeWidth());
        }
        m_fillPaint = e2d.getFillColor();
        m_shape = e2d.getShape();
        m_text = e2d.getText();
        m_font = e2d.getFont();
        m_x = (float) e2d.getTextX();
        m_y = (float) e2d.getTextY();
    }

    void paint(Graphics2D g) {
        if (m_fillPaint != null) {
            g.setPaint(m_fillPaint);
            g.fill(m_shape);
        }
        if (m_stroke != null) {
            g.setStroke(m_stroke);
            g.setPaint(m_strokePaint);
        }
        if (m_shape != null) {
            g.draw(m_shape);
        }
        if (m_text != null) {
            g.setFont(m_font);
            g.drawString(m_text, m_x, m_y);
        }
    }

    public Shape getShape() {
        return m_shape;
    }
}
