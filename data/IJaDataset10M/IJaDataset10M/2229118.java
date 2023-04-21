package seismosurfer.gui.menuitem;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import seismosurfer.gui.GUIConstants;
import seismosurfer.gui.panel.RangePanel;
import com.bbn.openmap.MapHandler;
import com.bbn.openmap.gui.WindowSupport;
import com.bbn.openmap.gui.menu.MapHandlerMenuItem;

/**
 * This menu item shows the "Range Query"
 * dialog box and handles its events.
 *
 */
public class RangeMenuItem extends MapHandlerMenuItem implements GUIConstants {

    private static final long serialVersionUID = -1263618637719529087L;

    protected WindowSupport ws;

    protected RangePanel rangePanel;

    private RangeMenuItemAction action = null;

    public RangeMenuItem() {
        super("Range");
        setAction(getMenuItemAction("Range"));
        rangePanel = new RangePanel(getMenuItemAction());
    }

    /**
     * Creates an Action object (specifically, a RangeMenuItemAction)
     * that handles the events from the RangePanel.
     * 
     * @param text the name of this Action
     * @return an Action instance
     */
    public Action getMenuItemAction(String text) {
        if (action == null) {
            action = new RangeMenuItemAction(text);
        }
        return action;
    }

    /**
     * Creates an Action object (specifically, a RangeMenuItemAction)
     * that handles the events from the RangePanel. 
     * 
     * @return an Action instance
     */
    public Action getMenuItemAction() {
        return getMenuItemAction(null);
    }

    class RangeMenuItemAction extends AbstractAction {

        private static final long serialVersionUID = 8790537982939171721L;

        public RangeMenuItemAction(String text) {
            super(text);
        }

        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ((command.equals(OK)) && (ws != null)) {
                rangePanel.sendQueryParameters();
                ws.killWindow();
            } else if ((command.equals(CANCEL)) && (ws != null)) {
                ws.killWindow();
            } else {
                if (ws == null) {
                    ws = new WindowSupport(rangePanel, "Range Query");
                }
                MapHandler mh = getMapHandler();
                Frame frame = null;
                if (mh != null) {
                    frame = (Frame) mh.get(java.awt.Frame.class);
                }
                ws.displayInWindow(frame);
                rangePanel.updateDialogValues();
            }
        }
    }

    public void findAndInit(Object someObj) {
        super.findAndInit(someObj);
        if (someObj instanceof MapHandler) {
            ((MapHandler) someObj).add(this);
            ((MapHandler) someObj).add(rangePanel);
        }
        rangePanel.findAndInit(someObj);
    }

    public void findAndUndo(Object someObj) {
        super.findAndUndo(someObj);
        rangePanel.findAndUndo(someObj);
    }
}
