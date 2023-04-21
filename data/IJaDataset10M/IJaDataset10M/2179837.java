package net.sf.graphiti.ui.figure.shapes;

import net.sf.graphiti.ui.figure.PolygonPortAnchor;
import net.sf.graphiti.ui.figure.VertexFigure;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Polygon;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * This class provides an abstract polygon shape for hexagon, losange and
 * triangle shapes.
 * 
 * @author Matthieu Wipliez
 * 
 */
public abstract class AbstractPolygonShape extends Polygon implements IShape {

    /**
	 * Creates a new abstract polygon shape.
	 */
    public AbstractPolygonShape() {
        setLayoutManager(new GridLayout(2, false));
        setFill(true);
    }

    @Override
    public ConnectionAnchor getConnectionAnchor(VertexFigure figure, String portName, boolean isOutput) {
        return new PolygonPortAnchor(figure, portName, isOutput);
    }

    @Override
    public IShape newShape() {
        try {
            return getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void paintFigure(Graphics graphics) {
        GradientPattern.paintFigure(this, getBackgroundColor(), getBounds(), graphics);
    }

    @Override
    public void paintSuperFigure(Graphics graphics) {
        super.paintFigure(graphics);
    }

    @Override
    public abstract void setDimension(Dimension dim);

    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }
}
