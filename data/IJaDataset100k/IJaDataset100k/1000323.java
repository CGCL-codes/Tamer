package org.pbjar.jxlayer.plaf.ext.transform;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.jdesktop.jxlayer.JXLayer;
import org.pbjar.jxlayer.plaf.ext.TransformUI;

/**
 * The {@link TransformModel} interface specifies the methods the
 * {@link TransformUI} will use to interrogate a transformation model.
 * 
 * @author Piet Blok
 */
public interface TransformModel {

    /**
     * Add a {@link ChangeListener} that will be notified when the internal
     * state of this model changes.
     * 
     * @param listener
     *            a {@link ChangeListener}
     * @see #removeChangeListener(ChangeListener)
     */
    public abstract void addChangeListener(ChangeListener listener);

    /**
     * Get a preferred {@link AffineTransform}. This method will typically be
     * invoked by programs that calculate a preferred size.
     * <p>
     * The {@code size} argument will be used to compute anchor values for some
     * types of transformations. If the {@code size} argument is {@code null} a
     * value of (0,0) is used for the anchor.
     * </p>
     * 
     * @param size
     *            a {@link Dimension} instance to be used for an anchor or
     *            {@code null}
     * @param layer
     *            the {@link JXLayer}.
     * @return a {@link AffineTransform} instance or {@code null}
     */
    public abstract AffineTransform getPreferredTransform(Dimension size, JXLayer<?> layer);

    /**
     * Get a {@link AffineTransform}. This method will typically be invoked by
     * programs that are about to prepare a {@link Graphics} object.
     * 
     * @param layer
     *            the {@link JXLayer}
     * 
     * @return a {@link AffineTransform} or {@code null}
     */
    public abstract AffineTransform getTransform(JXLayer<? extends JComponent> layer);

    /**
     * Remove a {@link ChangeListener}.
     * 
     * @param listener
     *            a {@link ChangeListener}
     * @see #addChangeListener(ChangeListener)
     */
    public abstract void removeChangeListener(ChangeListener listener);
}
