package javablock.flowchart;

import java.awt.*;
import java.awt.geom.*;

public interface FlowElement {

    public boolean isEditable();

    public BlockEditor getEditor();

    public void delete();

    public void shape();

    public boolean contains(double x, double y);

    public boolean intersects(Shape s);

    public Rectangle2D bound2D();

    public Rectangle bound();

    public void draw(Graphics2D g2d);

    public void draw(Graphics2D g2d, boolean drawFull);

    public void drawSelection(Graphics2D g2d);

    public boolean highLight(Graphics2D g2d);

    public void drawShadow(Graphics2D g2d);
}
