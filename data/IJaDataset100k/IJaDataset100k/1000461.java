package seismosurfer.gui.menuitem;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import seismosurfer.gui.GUIConstants;
import seismosurfer.gui.panel.DBQueryPanel;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.gui.WindowSupport;
import com.bbn.openmap.gui.menu.MapHandlerMenuItem;

/**
 * This menu item shows the "Distance-Based Query"
 * dialog box and handles its events.
 *
 */
public class DBMenuItem extends MapHandlerMenuItem implements GUIConstants {

    private static final long serialVersionUID = 4376634902865428743L;

    protected WindowSupport ws;

    protected DBQueryPanel dbPanel;

    protected float lat;

    protected float lon;

    protected boolean popup = false;

    private DBMenuItemAction action = null;

    public DBMenuItem() {
        super("Distance-Based");
        setAction(getMenuItemAction("Distance-Based"));
        dbPanel = new DBQueryPanel(getMenuItemAction());
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
            ((MapHandler) someObj).add(dbPanel);
        }
        dbPanel.findAndInit(someObj);
    }

    /**
     * Creates an Action object (specifically, a DBMenuItemAction)
     * that handles the events from the DBQueryPanel.
     * 
     * @param text the name of this Action
     * @return an Action instance
     */
    public Action getMenuItemAction(String text) {
        if (action == null) {
            action = new DBMenuItemAction(text);
        }
        return action;
    }

    /**
     * Creates an Action object (specifically, a DBMenuItemAction)
     * that handles the events from the DBQueryPanel. 
     * 
     * @return an Action instance
     */
    public Action getMenuItemAction() {
        return getMenuItemAction(null);
    }

    class DBMenuItemAction extends AbstractAction {

        private static final long serialVersionUID = 663976833476682713L;

        public DBMenuItemAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ((command.equals(OK)) && (ws != null)) {
                dbPanel.sendQueryParameters();
                ws.killWindow();
            } else if ((command.equals(CANCEL)) && (ws != null)) {
                ws.killWindow();
            } else {
                if (ws == null) {
                    ws = new WindowSupport(dbPanel, "Distance Query");
                }
                MapHandler mh = getMapHandler();
                Frame frame = null;
                if (mh != null) {
                    frame = (Frame) mh.get(java.awt.Frame.class);
                }
                ws.displayInWindow(frame);
                if (popup) {
                    dbPanel.updateDialogValues(lat, lon);
                } else {
                    dbPanel.updateDialogValues();
                }
            }
            popup = false;
        }
    }

    public void findAndUndo(Object someObj) {
        super.findAndUndo(someObj);
        dbPanel.findAndUndo(someObj);
    }
}
