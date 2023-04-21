package net.sourceforge.circuitsmith.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.circuitsmith.layers.EdaLayerList;
import net.sourceforge.circuitsmith.panes.EdaDrawingPane;

public class EdaSnapNode extends EdaObject {

    private static final float SIZE = 1e-10f;

    private static final float DRAW_SIZE = 7.5f;

    public enum NodeShape {

        SQUARE, ROUND
    }

    ;

    private NodeShape m_shape = NodeShape.SQUARE;

    public EdaSnapNode() {
        super();
        setLayerName("Part");
        setShowOnPart(false);
        setAllowLayerChange(false);
    }

    public EdaSnapNode(NodeShape ns) {
        super();
        setLayerName("Part");
        setShowOnPart(false);
        setAllowLayerChange(false);
        setNodeShape(ns);
    }

    public EdaSnapNode(EdaSnapNode o) {
        super(o);
        setShowOnPart(false);
        setAllowLayerChange(false);
        setNodeShape(o.getNodeShape());
    }

    @Override
    public EdaObject makeCopy() {
        return new EdaSnapNode(this);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform t, final DrawMode mode, final EdaLayerList layers) {
        if (!isLayerVisible(layers)) {
            return;
        }
        DrawMode mode2 = mode;
        configureGraphics(g, mode2, layers);
        AffineTransform t2 = new AffineTransform(t);
        t2.concatenate(getTransform());
        Point2D p = t2.transform(new Point2D.Float(0, 0), new Point2D.Float());
        float x = (float) (p.getX());
        float y = (float) (p.getY());
        if (m_shape == NodeShape.ROUND) {
            g.fill(new Ellipse2D.Float(x - DRAW_SIZE / 2, y - DRAW_SIZE / 2, DRAW_SIZE, DRAW_SIZE));
        } else {
            g.fill(new Rectangle2D.Float(x - DRAW_SIZE / 2, y - DRAW_SIZE / 2, DRAW_SIZE, DRAW_SIZE));
        }
    }

    @Override
    public boolean getBounds(final Rectangle2D.Float r, float scale) {
        float size = DRAW_SIZE / scale;
        r.setRect(getX0() - size / 2, getY0() - size / 2, size, size);
        return true;
    }

    @Override
    public float distanceTo(Point2D.Float p) {
        return (float) getLocation().distance(p);
    }

    @Override
    public String getObjectName() {
        return ("Snap node");
    }

    @Override
    public final void applyAction(final EdaObjectAction action) {
    }

    @Override
    public void setPanelPropertyValues(EdaObjectPropertyKey p, Object value, EdaDrawingPane pane) {
        switch(p) {
            default:
                super.setPanelPropertyValues(p, value, pane);
                break;
        }
    }

    @Override
    public Point2D.Float getClosestSnapPoint(Point2D.Float p) {
        return getLocation();
    }

    public void setNodeShape(NodeShape ns) {
        m_shape = ns;
    }

    public NodeShape getNodeShape() {
        return m_shape;
    }
}
