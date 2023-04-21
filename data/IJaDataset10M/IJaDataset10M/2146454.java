package sears.search.gui;

import java.awt.Graphics2D;
import javax.swing.JViewport;
import sears.gui.glassPane.DefaultViewportGlassPane;
import sears.gui.glassPane.ViewportGlassPane;
import sears.search.core.PillManager;

/**
 * An object of this class represents a virtual glass pane over a viewport
 * <br>It allows to display <code>Pill</code> objects over it graphics context.
 * @see DefaultViewportGlassPane
 */
public class SearchViewportGlassPane extends DefaultViewportGlassPane {

    private static final long serialVersionUID = 2163386443816906875L;

    private PillManager pm;

    /**
	 * Creates a new virtual glass pane
	 * 
	 * @param viewport		the view port, needed for display the virtual glass pane
	 * @param pillManager	a pill manager, to manage pill position over the glass pane
	 * 
	 * @throws 				NullPointerException if view port is null
	 * @throws 				NullPointerException if pillMananger is null
	 * 
	 * @see ViewportGlassPane#ViewportGlassPane(JViewport)
	 * @see DefaultViewportGlassPane
	 */
    public SearchViewportGlassPane(JViewport viewport, PillManager pillManager) {
        super(viewport);
        if (pillManager == null) {
            throw new NullPointerException("PillManager object cannot be null");
        }
        pm = pillManager;
    }

    protected void paintChildrenWithGraphics(Graphics2D gr) {
        pm.paintPill(gr);
    }

    /**
	 * Wrap method, calls the <code>repaint()</code> method
	 * <br>Avoid confusion ( and makes sure a repaint is needed )
	 */
    public void updatePill() {
        repaint();
    }
}
