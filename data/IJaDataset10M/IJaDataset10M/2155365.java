package net.sourceforge.circuitsmith.layers;

import java.awt.Color;
import java.io.Serializable;

/**
 * represents a drawing layer.
 */
public class EdaLayer implements Serializable {

    private Color m_color;

    private Color m_invertedColor;

    private String m_name;

    private float lineWidth;

    private boolean visible;

    public EdaLayer(final String name, float width, float color) {
        setColor(new Color((int) color));
        m_name = name;
        lineWidth = width;
        visible = true;
    }

    public EdaLayer makeCopy() {
        return new EdaLayer(getName(), getWidth(), getColor().getRGB());
    }

    public String getName() {
        return m_name;
    }

    public Color getColor() {
        return m_color;
    }

    public final Color getInvertedColor() {
        return m_invertedColor;
    }

    public void setColor(final Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color must not be null.");
        }
        m_color = color;
        m_invertedColor = new Color(~m_color.getRGB());
        m_invertedColor = Color.green;
    }

    public void setName(String s) {
        m_name = s;
    }

    public float getWidth() {
        return lineWidth;
    }

    public void setWidth(float w) {
        lineWidth = w;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }
}
