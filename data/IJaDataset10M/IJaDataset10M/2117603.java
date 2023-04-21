package coat.elements;

import coat.Graph;
import coat.ICanvas;
import coat.Transformer;

/**
 * Any IDrawable that is fixed on screen - such like a logo or a line legend.
 *
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since Jun 2001
 */
public interface IFixedDrawable extends IDrawable {

    /**
   * Indicates whether this and the other IFixedDrawable can collaborate.
   * e.g. Axis can collaborate with another Axis (trivial).
   * 
   * <p>Future versions may allow a collaboration of Legend and Logo.
   *
   * @return Whether the two IDrawable's are compatible.
   * @param draw The other IDrawable that might be compatible for mass draw.
   */
    public boolean isCompatible(IFixedDrawable draw);

    /**
   * Forces fixed drawables to collaborate.
   * Used e.g. for drawing axis in overlay modus.
   * 
   * <p>WARNING: some fields in aoPartnerDraws may be null if
   * no collaborating partner exists.
   * 
   * <p>A good IFixedDrawable should support a null Transformer.
   *
   * @param canvas The graphics object to paint on.
   * @param rectD The device (pixel) space available for paint.
   * @param rectU The user space that has to be painted.
   * @param trans For linear (standard) or logarithmic display.
   * @param aoPartnerDraws The partners for the mass drawing.
   * @param aoPartnerGraphs The graphs of the partners (user space, transformer).
   * @return The device space left.
   */
    public java.awt.Rectangle massDraw(ICanvas canvas, java.awt.Rectangle rectD, java.awt.geom.Rectangle2D.Double rectU, Transformer trans, IFixedDrawable[] aoPartnerDraws, Graph[] aoPartnerGraphs);

    /**
   * Indicates whether the given part of the drawable is visible or not.
   * 
   * <p>Invisible components should not be included in <code>massDraw()</code>
   * operations.

   * @param part The part to make visible/invisible.
   * @return Whether the part is visible.
   */
    public boolean isPartVisible(int part);

    /**
   * Sets a part of the drawable visible or invisible, e.g. single axis of an axis
   * system, the complete line legend, or others.
   * 
   * <p>The given "part" is to be defined by the actual drawable.
   * 
   * <p>Invisible components should not be included in <code>massDraw()</code>
   * operations.
   * 
   * @param part The part to make visible/invisible.
   * @param isVisible Whether to make the part visible.
   */
    public void setPartVisible(int part, boolean isVisible);
}
