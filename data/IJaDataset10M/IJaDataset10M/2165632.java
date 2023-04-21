package org.pushingpixels.substance.internal.painter;

import java.awt.*;
import java.util.List;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.painter.overlay.SubstanceOverlayPainter;

/**
 * Contains utility methods related to overlay painters. This class is for
 * internal use only.
 * 
 * @author Kirill Grouchnikov
 */
public class OverlayPainterUtils {

    /**
	 * Paints all registered overlays on the specified component. Overlay
	 * painters are registered with
	 * {@link SubstanceSkin#addOverlayPainter(SubstanceOverlayPainter, DecorationAreaType...)}
	 * API.
	 * 
	 * @param g
	 *            Graphics context.
	 * @param c
	 *            Component.
	 * @param skin
	 *            Component skin.
	 * @param decorationAreaType
	 *            Component decoration area type.
	 */
    public static void paintOverlays(Graphics g, Component c, SubstanceSkin skin, DecorationAreaType decorationAreaType) {
        List<SubstanceOverlayPainter> overlayPainters = skin.getOverlayPainters(decorationAreaType);
        if (overlayPainters.size() == 0) return;
        for (SubstanceOverlayPainter overlayPainter : overlayPainters) {
            Graphics2D g2d = (Graphics2D) g.create();
            overlayPainter.paintOverlay(g2d, c, decorationAreaType, c.getWidth(), c.getHeight(), skin);
            g2d.dispose();
        }
    }
}
