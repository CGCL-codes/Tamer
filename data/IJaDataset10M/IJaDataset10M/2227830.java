package org.pbjar.jxlayer.plaf.ext.transform;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.LayerUI;
import org.pbjar.jxlayer.plaf.ext.TransformUI;

/**
 * A specialized layout manager for {@link JXLayer} in combination with the
 * {@link TransformUI}.
 * <p>
 * It extends {@link DefaultLayerLayout} and, as long as no enabled
 * {@link TransformUI} is set to {@link JXLayer}, will act exactly the same as
 * its super class.
 * </p>
 * <p>
 * However, when the above conditions are all true, its behavior becomes
 * different:
 * <ol>
 * <li>
 * Instead of setting the view's size to the layer's calculated inner area, it
 * will set the view's size to its preferred size.</li>
 * <li>
 * Instead of setting the view's bounds to the calculated inner area, it will
 * center the view in that inner area. This may result in some parts of the view
 * formally obscured, or, some parts of the inner area not covered by the view.</li>
 * <li>
 * The preferred size will first be computed by the super implementation. Then,
 * before returning, the calculated size will be transformed with the
 * {@link AffineTransform} returned by
 * {@link TransformUI#getPreferredTransform(Dimension, JXLayer)};</li>
 * <li>
 * The minimum size will first be computed by the super implementation. Then,
 * before returning, the calculated size will be transformed with the
 * {@link AffineTransform} returned by
 * {@link TransformUI#getPreferredTransform(Dimension, JXLayer)};</li>
 * </ol>
 * </p>
 * 
 * @see JXLayer#getView()
 * @see JXLayer#getGlassPane()
 * @see TransformUI
 */
public class TransformLayout extends DefaultLayerLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TransformLayout() {
    }

    /**
     * Overridden to apply a different layout when the {@link LayerUI} is an
     * instance of {@link TransformUI}. If this is not the case, the super
     * implementation will be invoked.
     */
    @Override
    public void layoutContainer(Container parent) {
        JXLayer<?> layer = (JXLayer<?>) parent;
        LayerUI<?> layerUI = layer.getUI();
        if (layerUI instanceof TransformUI) {
            JComponent view = (JComponent) layer.getView();
            JComponent glassPane = layer.getGlassPane();
            if (view != null) {
                Rectangle innerArea = new Rectangle();
                SwingUtilities.calculateInnerArea(layer, innerArea);
                view.setSize(view.getPreferredSize());
                Rectangle viewRect = new Rectangle(0, 0, view.getWidth(), view.getHeight());
                int x = (int) Math.round(innerArea.getCenterX() - viewRect.getCenterX());
                int y = (int) Math.round(innerArea.getCenterY() - viewRect.getCenterY());
                viewRect.translate(x, y);
                view.setBounds(viewRect);
            }
            if (glassPane != null) {
                glassPane.setLocation(0, 0);
                glassPane.setSize(layer.getWidth(), layer.getHeight());
            }
            return;
        }
        super.layoutContainer(parent);
    }

    /**
     * Overridden to apply a preferred transform on the {@link Dimension} object
     * returned from the super implementation.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return transform(parent, super.minimumLayoutSize(parent));
    }

    /**
     * Overridden to apply a preferred transform on the {@link Dimension} object
     * returned from the super implementation.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return transform(parent, super.preferredLayoutSize(parent));
    }

    @SuppressWarnings("unchecked")
    private Dimension transform(Container parent, Dimension size) {
        JXLayer<JComponent> layer = (JXLayer<JComponent>) parent;
        LayerUI<?> ui = layer.getUI();
        if (ui instanceof TransformUI) {
            TransformUI transformUI = (TransformUI) ui;
            AffineTransform transform = transformUI.getPreferredTransform(size, layer);
            if (transform != null) {
                Area area = new Area(new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight()));
                area.transform(transform);
                Rectangle2D bounds = area.getBounds2D();
                size.setSize(bounds.getWidth(), bounds.getHeight());
            }
        }
        return size;
    }
}
