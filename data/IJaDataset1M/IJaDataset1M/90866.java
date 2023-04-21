package javag.gui.widgets.border;

import java.awt.Color;
import java.awt.Graphics2D;
import javag.gui.Position;
import javag.gui.Size;

public class BorderShadow extends Border {

    private Color color;

    private int thickness;

    private Position position;

    public BorderShadow(Color color, int thickness, Position position) {
        setColor(color);
        setThicknessAndPosition(thickness, position);
    }

    protected void paintBorder(Graphics2D g) {
        g.setColor(color);
        Size size = getSize();
        if (position == Position.TOP) {
            g.fillRect(0, 0, size.getWidth(), getThickness());
        } else if (position == Position.BOTTOM) {
            g.fillRect(0, size.getHeight() - getThickness(), size.getWidth(), getThickness());
        } else if (position == Position.LEFT) {
            g.fillRect(0, 0, getThickness(), size.getHeight());
        } else if (position == Position.RIGHT) {
            g.fillRect(size.getWidth() - getThickness(), 0, getThickness(), size.getHeight());
        } else if (position == Position.TOP_LEFT) {
            g.fillRect(0, 0, size.getWidth() - getThickness(), getThickness());
            g.fillRect(0, getThickness(), getThickness(), size.getHeight() - getThickness() - getThickness());
        } else if (position == Position.TOP_RIGHT) {
            g.fillRect(getThickness(), 0, size.getWidth() - getThickness(), getThickness());
            g.fillRect(size.getWidth() - getThickness(), getThickness(), getThickness(), size.getHeight() - getThickness() - getThickness());
        } else if (position == Position.BOTTOM_LEFT) {
            g.fillRect(0, size.getHeight() - getThickness(), size.getWidth() - getThickness(), getThickness());
            g.fillRect(0, getThickness(), getThickness(), size.getHeight() - getThickness() - getThickness());
        } else if (position == Position.BOTTOM_RIGHT) {
            g.fillRect(getThickness(), size.getHeight() - getThickness(), size.getWidth() - getThickness(), getThickness());
            g.fillRect(size.getWidth() - getThickness(), getThickness(), getThickness(), size.getHeight() - getThickness() - getThickness());
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setThicknessAndPosition(int thickness, Position position) {
        invalidateLayout();
        this.thickness = thickness;
        this.position = position;
        if (position == Position.TOP) {
            setSetBorderSize(thickness, 0, 0, 0);
        } else if (position == Position.BOTTOM) {
            setSetBorderSize(0, 0, 0, thickness);
        } else if (position == Position.LEFT) {
            setSetBorderSize(0, thickness, 0, 0);
        } else if (position == Position.RIGHT) {
            setSetBorderSize(0, 0, thickness, 0);
        } else if (position == Position.TOP_LEFT) {
            setSetBorderSize(thickness, thickness, 0, 0);
        } else if (position == Position.TOP_RIGHT) {
            setSetBorderSize(thickness, 0, thickness, 0);
        } else if (position == Position.BOTTOM_LEFT) {
            setSetBorderSize(0, thickness, 0, thickness);
        } else if (position == Position.BOTTOM_RIGHT) {
            setSetBorderSize(0, 0, thickness, thickness);
        } else {
            throw new IllegalArgumentException("Position was set to an illegal value.");
        }
    }

    public int getThickness() {
        return thickness;
    }
}
