package com.mindtree.techworks.insight.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.mindtree.techworks.insight.Controller;
import com.mindtree.techworks.insight.InsightConstants;
import com.mindtree.techworks.insight.gui.widgets.StatusBar;
import com.mindtree.techworks.insight.preferences.util.PreferenceInterpreter;

/**
*
* The <code>LocateAction</code> class is an AbstractAction derivative that
* locates the selected LogEvent
*
* @author  Antony 
* @version 1.0, 04/10/25
* @see     com.mindtree.techworks.insight.gui.Insight
*/
public class LocateAction extends AbstractAction {

    /**
	 * Used for object serialization
	 */
    private static final long serialVersionUID = -3578056532882836648L;

    /**
	 * The LocateAction instance
	 */
    private static LocateAction instance;

    /**
	 * The Controller instance
	 */
    private Controller controller;

    /**
	 * Private constructor for this class to prevent multiple instances
	 * @param controller 
	 */
    private LocateAction(Controller controller) {
        this.controller = controller;
    }

    /**
	 * Accessor method for getting the singleton instance of this class
	 * @param controller 
	 */
    public static synchronized LocateAction getInstance(Controller controller) {
        if (instance == null) {
            instance = new LocateAction(controller);
        }
        return instance;
    }

    /**
	 * Overriden super class method. 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        if (controller.getCurrentLogEventModel() == null) {
            return;
        }
        long eventSequenceNumber = controller.getCurrentLogEventModel().getLogEventIndex();
        if (eventSequenceNumber < 0) {
            StatusBar.getInstance().setDisplayText(0, InsightConstants.getLiteral("ERROR_LOCATE_FAILURE"), false);
            return;
        }
        long eventsPerPage = PreferenceInterpreter.getCachePageSize();
        int pageNumber = (int) ((eventSequenceNumber - 1) / eventsPerPage + 1);
        if (!controller.getCurrentLogEventModel().equals(controller.getRootLogEventModel())) {
            controller.clearCurrentModel();
        }
        controller.setCurrentLogEventModel(controller.getRootLogEventModel(), pageNumber, eventSequenceNumber);
    }
}
