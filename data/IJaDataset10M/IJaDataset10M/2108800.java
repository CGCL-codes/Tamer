package net.sourceforge.g2destiny.model.grid;

import java.awt.Color;
import net.sourceforge.g2destiny.model.g2d.G2DModel;

/**
 * Grid model for a g2destiny game.
 * 
 * @author Jeff D. Conrad
 * @since 02/14/2010
 */
public class GridModel extends G2DModel {

    private static final String GRID_TYPE_POLAR = "polar";

    private static final String GRID_TYPE_RECTILINEAR = "rectilinear";

    private String type;

    private Double minorRadius;

    private Double majorRadius;

    private Double minorAngle;

    private Double majorAngle;

    private Color axisColor = new Color(150, 150, 150);

    private Color majorColor = new Color(100, 100, 100);

    private Color minorColor = new Color(50, 50, 50);

    public GridModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTypePolar() {
        return GRID_TYPE_POLAR.equals(type);
    }

    public boolean isTypeRectilinear() {
        return GRID_TYPE_RECTILINEAR.equals(type);
    }

    public Double getMinorRadius() {
        return minorRadius;
    }

    public void setMinorRadius(Double minorRadius) {
        this.minorRadius = minorRadius;
    }

    public Double getMajorRadius() {
        return majorRadius;
    }

    public void setMajorRadius(Double majorRadius) {
        this.majorRadius = majorRadius;
    }

    public Double getMinorAngle() {
        return minorAngle;
    }

    public void setMinorAngle(Double minorAngle) {
        this.minorAngle = minorAngle;
    }

    public Double getMajorAngle() {
        return majorAngle;
    }

    public void setMajorAngle(Double majorAngle) {
        this.majorAngle = majorAngle;
    }

    public Color getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public Color getMajorColor() {
        return majorColor;
    }

    public void setMajorColor(Color majorColor) {
        this.majorColor = majorColor;
    }

    public Color getMinorColor() {
        return minorColor;
    }

    public void setMinorColor(Color minorColor) {
        this.minorColor = minorColor;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("GridModel[");
        buffer.append(super.toString());
        buffer.append(" type = ").append(type);
        buffer.append(" axisColor = ").append(axisColor);
        buffer.append(" minorColor = ").append(minorColor);
        buffer.append(" majorColor = ").append(majorColor);
        buffer.append(" minorRadius = ").append(minorRadius);
        buffer.append(" majorRadius = ").append(majorRadius);
        buffer.append(" minorAngle = ").append(minorAngle);
        buffer.append(" majorAngle = ").append(majorAngle);
        buffer.append("]");
        return buffer.toString();
    }
}
