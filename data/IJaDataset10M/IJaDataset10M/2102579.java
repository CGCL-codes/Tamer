package viewer.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viewer.core.MapWindowChangeListener;
import viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class ZoomAction extends ViewerAction implements MapWindowChangeListener {

    private static final long serialVersionUID = 365467138876660750L;

    static final Logger logger = LoggerFactory.getLogger(ZoomAction.class);

    private static String FILE_IN = "res/images/zoom-in.png";

    private static String FILE_OUT = "res/images/zoom-out.png";

    private boolean in;

    /**
	 * Create a new ZoomAction.
	 * 
	 * @param viewer
	 *            the viewer this action is about.
	 * @param in
	 *            true to zoom in, false to zoom out.
	 */
    public ZoomAction(Viewer viewer, boolean in) {
        super(viewer, in ? FILE_IN : FILE_OUT);
        this.in = in;
        viewer.getMapWindow().addZoomListener(this);
    }

    @Override
    public Object getValue(String key) {
        if (key == Action.SMALL_ICON) {
            return icon;
        } else if (key.equals(Action.NAME)) {
            return String.format("zoom %s", in ? "in" : "out");
        } else if (key.equals(Action.SHORT_DESCRIPTION)) {
            return String.format("zoom %s", in ? "in" : "out");
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (in) {
            getViewer().getMapWindow().zoomIn();
        } else {
            getViewer().getMapWindow().zoomOut();
        }
        getViewer().repaint();
    }

    @Override
    public boolean isEnabled() {
        return (in && getViewer().getZoomLevel() < getViewer().getMaxZoomLevel()) || (!in && getViewer().getZoomLevel() > getViewer().getMinZoomLevel());
    }

    @Override
    public void changed() {
        firePropertyChange("enabled", null, null);
    }
}
