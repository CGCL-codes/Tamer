package org.jdrawing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.jdrawing.event.DrawingModelEvent;

/**
 * Default implementation of <code>DrawingModelCache</code> interface.
 * This implementation does not do any optimizations, except for 
 * optimizations supported by {@link IndexedDrawingModel} interface.
 */
public class DefaultDrawingModelCache extends AbstractDrawingModelCache {

    private Rectangle2D cachedModelBounds = null;

    public void setModel(DrawingModel model) {
        if (getModel() != model) {
            super.setModel(model);
            cachedModelBounds = null;
        }
    }

    public void setRenderer(DrawingRenderer renderer) {
        if (getRenderer() != renderer) {
            super.setRenderer(renderer);
            cachedModelBounds = null;
        }
    }

    public Rectangle2D getModelBounds2D() {
        if (cachedModelBounds != null) return (Rectangle2D) cachedModelBounds.clone();
        DrawingRenderer renderer = getRenderer();
        if (renderer == null) throw new IllegalStateException("Drawing renderer is not set");
        DrawingModel model = getModel();
        if (model == null) throw new IllegalStateException("Drawing model is not set");
        Rectangle2D result = new Rectangle2D.Double();
        Enumeration e = model.elements();
        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            Rectangle2D bounds = renderer.getElementRenderer(element).getElementBounds2D(element);
            if (bounds == null) continue;
            if (bounds.isEmpty()) continue;
            if (!result.isEmpty()) result.add(bounds); else result.setRect(bounds);
        }
        cachedModelBounds = result;
        return result;
    }

    public Object[] getElementsForRectangle(Rectangle2D rectangle) {
        DrawingRenderer renderer = getRenderer();
        if (renderer == null) throw new IllegalStateException("Drawing renderer is not set");
        DrawingModel model = getModel();
        if (model == null) throw new IllegalStateException("Drawing model is not set");
        List result = new LinkedList();
        Enumeration e;
        if (model instanceof IndexedDrawingModel) e = ((IndexedDrawingModel) model).elements(rectangle); else e = model.elements();
        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            if (renderer.getElementRenderer(element).elementIntersects(element, rectangle)) result.add(element);
        }
        return result.toArray(new Object[result.size()]);
    }

    public Object[] getElementsForPoint(Point2D point) {
        DrawingRenderer renderer = getRenderer();
        if (renderer == null) throw new IllegalStateException("Drawing renderer is not set");
        DrawingModel model = getModel();
        if (model == null) throw new IllegalStateException("Drawing model is not set");
        List result = new LinkedList();
        Enumeration e;
        if (model instanceof IndexedDrawingModel) e = ((IndexedDrawingModel) model).elements(point); else e = getModel().elements();
        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            if (renderer.getElementRenderer(element).elementContains(element, point)) result.add(element);
        }
        return result.toArray(new Object[result.size()]);
    }

    public void elementsAdded(DrawingModelEvent event) {
        cachedModelBounds = null;
        super.elementsAdded(event);
    }

    public void elementsRemoved(DrawingModelEvent event) {
        cachedModelBounds = null;
        super.elementsRemoved(event);
    }

    public void elementsChanged(DrawingModelEvent event) {
        cachedModelBounds = null;
        super.elementsChanged(event);
    }

    public void modelChanged(DrawingModelEvent event) {
        cachedModelBounds = null;
        super.modelChanged(event);
    }
}
