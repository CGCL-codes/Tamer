package seismosurfer.gui.menuitem;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import seismosurfer.gui.GUIConstants;
import seismosurfer.gui.panel.NNQueryPanel;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.gui.WindowSupport;
import com.bbn.openmap.gui.menu.MapHandlerMenuItem;

/**
 * This menu item shows the "Nearest-Neighbour Query"
 * dialog box and handles its events.
 *
 */
public class NNMenuItem extends MapHandlerMenuItem implements GUIConstants {

    private static final long serialVersionUID = -5439438752876535078L;

    protected float lat;

    protected float lon;

    protected boolean popup = false;

    protected WindowSupport ws;

    protected NNQueryPanel nnPanel;

    private NNMenuItemAction action = null;

    public NNMenuItem() {
        super("Nearest-Neighbour");
        setAction(getMenuItemAction("Nearest-Neighbour"));
        nnPanel = new NNQueryPanel(getMenuItemAction());
    }

    /**
     * Stores the coordinates of a given point in the map which
     * will be used as the center of the map.
     * 
     * @param point the LatLonPoint where a click occurred
     */
    public void setClickCoordinates(LatLonPoint point) {
        popup = true;
        lat = point.getLatitude();
        lon = point.getLongitude();
    }

    public void findAndInit(Object someObj) {
        super.findAndInit(someObj);
        if (someObj instanceof MapHandler) {
            ((MapHandler) someObj).add(this);
            ((MapHandler) someObj).add(nnPanel);
        }
        nnPanel.findAndInit(someObj);
    }

    public void findAndUndo(Object someObj) {
        super.findAndUndo(someObj);
        nnPanel.findAndUndo(someObj);
    }

    /**
     * Creates an Action object (specifically, a NNMenuItemAction)
     * that handles the events from the NNQueryPanel.
     * 
     * @param text the name of this Action
     * @return an Action instance
     */
    public Action getMenuItemAction(String text) {
        if (action == null) {
            action = new NNMenuItemAction(text);
        }
        return action;
    }

    /**
     * Creates an Action object (specifically, a NNMenuItemAction)
     * that handles the events from the NNQueryPanel. 
     * 
     * @return an Action instance
     */
    public Action getMenuItemAction() {
        return getMenuItemAction(null);
    }

    class NNMenuItemAction extends AbstractAction {

        private static final long serialVersionUID = -4276024523835791310L;

        public NNMenuItemAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ((command.equals(OK)) && (ws != null)) {
                nnPanel.sendQueryParameters();
                ws.killWindow();
            } else if ((command.equals(CANCEL)) && (ws != null)) {
                ws.killWindow();
            } else {
                if (ws == null) {
                    ws = new WindowSupport(nnPanel, "NN Query");
                }
                MapHandler mh = getMapHandler();
                Frame frame = null;
                if (mh != null) {
                    frame = (Frame) mh.get(java.awt.Frame.class);
                }
                ws.displayInWindow(frame);
                if (popup) {
                    nnPanel.updateDialogValues(lat, lon);
                } else {
                    nnPanel.updateDialogValues();
                }
            }
            popup = false;
        }
    }
}
