package jpicedt.graphic.event;

import java.util.EventListener;

/**
 * Interface for an observer to receive notifications of changes made to a zoom factor in an instance of PECanvas.
 * @author Sylvain Reynal
 * @version $Id: ZoomListener.java,v 1.6 2011/07/23 05:24:03 vincentb1 Exp $
 * @since jpicedt 1.3.2
 */
public interface ZoomListener extends EventListener {

    /** called when the zoom changed in the sourcing PECanvas */
    public void zoomUpdate(ZoomEvent e);
}
