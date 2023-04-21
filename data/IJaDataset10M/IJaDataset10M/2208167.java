package de.jaret.util.ui.timebars.swing.renderer;

import javax.swing.JComponent;
import de.jaret.util.ui.timebars.model.TimeBarRowHeader;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;

/**
 * Renderer for row headers.
 * 
 * @author Peter Kliem
 * @version $Id: HeaderRenderer.java 800 2008-12-27 22:27:33Z kliem $
 */
public interface HeaderRenderer {

    /**
     * Provide a JComponent configured to render the header object supplied as value.
     * 
     * @param tbv the calling TimeBarViewer
     * @param value the header object to render
     * @param isSelected if true draw the selectd state
     * @return a configured JComponent ready to be painted
     */
    JComponent getHeaderRendererComponent(TimeBarViewer tbv, TimeBarRowHeader value, boolean isSelected);

    /**
     * Return the width required by the header renderer. The value will only be read once by the TimeBarViewer when the
     * rendderer ist set. There is no support for dynamic change of the header width initiated by the renderer.
     * 
     * @return the width of the header
     */
    int getWidth();
}
