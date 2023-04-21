package org.pushingpixels.substance.api.painter.border;

import java.awt.*;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

/**
 * Border painter interface for <b>Substance</b> look and feel. This class is
 * part of officially supported API.<br>
 * <br>
 * 
 * Starting from version 4.0, the borders of some controls (buttons, check
 * boxes, tabs, scroll bars etc) are painted by border painters. Up until
 * version 4.0 this has been done by gradient painters (
 * {@link SubstanceFillPainter}) instead. Note that a custom gradient painter
 * may continue painting the borders, but these will be overriden by the current
 * border painter.
 * 
 * @author Kirill Grouchnikov
 * @since version 4.0
 */
public interface SubstanceBorderPainter extends SubstanceTrait {

    /**
	 * Paints the control border.
	 * 
	 * @param g
	 *            Graphics.
	 * @param c
	 *            Component.
	 * @param width
	 *            Width of a UI component.
	 * @param height
	 *            Height of a UI component.
	 * @param contour
	 *            Contour of a UI component.
	 * @param innerContour
	 *            Inner contour of a UI component. May be ignored if the
	 *            specific implementation paints only the outside border.
	 * @param borderScheme
	 *            The border color scheme.
	 */
    public void paintBorder(Graphics g, Component c, int width, int height, Shape contour, Shape innerContour, SubstanceColorScheme borderScheme);

    /**
	 * Returns boolean indication whether this border painter is painting the
	 * inner contours.
	 * 
	 * @return <code>true</code> if this border painter is painting the inner
	 *         contours, <code>false</code> otherwise.
	 */
    public boolean isPaintingInnerContour();
}
