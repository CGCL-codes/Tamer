package es.iiia.shapegrammar.model;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.eclipse.ui.views.properties.IPropertySource;
import org.w3c.dom.Element;
import es.iiia.shapegrammar.shape.ShapeModel;

public abstract class GeometryModel extends NodeModel implements Cloneable, IAffineTransformable {

    protected Rectangle bounds;

    public abstract GeometryModel clone();

    public abstract Element getXml();

    public abstract ArrayList<Point2D> getPoints();

    private Color color;

    private int thickness;

    public GeometryModel() {
        super();
    }

    public GeometryModel(Element config) {
        this();
        if (config.getAttribute("id") != null && config.getAttribute("id").length() > 0) {
            this.setId(Integer.parseInt(config.getAttribute("id")));
            ShapeGrammarModel.ACTIVE_SHAPE_GRAMMAR.updateTicket(this.getId());
        }
        if (config.getAttribute("color") != null && config.getAttribute("color").length() > 0) {
            String[] rgb = config.getAttribute("color").split(",");
            this.color = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        }
    }

    public Point2D getCenter() {
        return new Point2D.Double(this.getBounds().getCenterX(), this.getBounds().getCenterY());
    }

    public Point2D getLocation() {
        return new Point2D.Double(this.getBounds().getCenterX(), this.getBounds().getCenterY());
    }

    public void setLocation(Point2D p) {
        this.translate(p.getX() - getLocation().getX(), p.getY() - getLocation().getY());
    }

    public Color getColor() {
        if (color == null) {
            color = Color.black;
        }
        return color;
    }

    protected String getColorString() {
        return getColor().getRed() + "," + getColor().getGreen() + "," + getColor().getBlue();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public void translate(double x, double y) {
        for (NodeModel mdl : this.children) {
            mdl.translate(x, y);
        }
        this.fireChangeListeners(null, this.children);
    }

    @Override
    public void scale(double x, double y) {
        for (NodeModel mdl : this.children) {
            mdl.scale(x, y);
        }
        this.fireChangeListeners(null, this.children);
    }

    @Override
    public void rotate(Point2D center, double angle) {
        for (NodeModel mdl : this.children) {
            mdl.rotate(center, angle);
        }
        this.fireChangeListeners(null, this.children);
    }

    @Override
    public void transform(AffineTransform transform) {
        for (NodeModel mdl : this.children) {
            mdl.transform(transform);
        }
        this.fireChangeListeners(null, this.children);
    }

    protected void fireChangeListeners(Object oldValue, Object newValue) {
        this.reset();
        this.getListeners().firePropertyChange(PROPERTY_LAYOUT, oldValue, newValue);
        if (this.getParent() instanceof ShapeModel) {
            this.getParent().getListeners().firePropertyChange(PROPERTY_LAYOUT, oldValue, newValue);
        }
    }

    @Override
    public IPropertySource getPropertySource() {
        return new GeometryPropertySource(this);
    }

    protected void reset() {
        this.bounds = null;
    }

    public void setEditorId(String shapeEditorID) {
    }
}
