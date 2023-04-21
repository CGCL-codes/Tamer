package visualgraph.gui;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

public class CircleDecoration implements Decoration {

    private float m_endX;

    private float m_endY;

    private float m_directionX;

    private float m_directionY;

    private float m_radiusDirectionX;

    private float m_radiusDirectionY;

    private float m_radius = 2.0f;

    private Ellipse2D m_shape;

    public CircleDecoration() {
        m_shape = new Ellipse2D.Float();
    }

    public CircleDecoration(float radius) {
        this();
        m_radius = radius;
    }

    public void setPoint(float x, float y) {
        m_endX = x;
        m_endY = y;
    }

    public void setDirection(float x, float y) {
        m_directionX = x;
        m_directionY = y;
        double length = Math.sqrt(x * x + y * y);
        m_radiusDirectionX = (float) (x * 1.0 / length * m_radius);
        m_radiusDirectionY = (float) (y * 1.0 / length * m_radius);
    }

    public Shape getDecorationPath() {
        float safety_factor = 0.8f;
        m_shape.setFrame(m_endX + m_radiusDirectionX * safety_factor - m_radius, m_endY + m_radiusDirectionY * safety_factor - m_radius, m_radius + m_radius, m_radius + m_radius);
        return m_shape;
    }
}
