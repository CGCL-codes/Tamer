package blms.controller.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import blms.controller.handler.HistoryHandler;

/**
 * This class represents a form in order to obtain from the user the information
 * necessary to search for a certain player history in the system
 */
public class HistoryForm extends ActionForm {

    private HistoryHandler historyHandler = new HistoryHandler();

    /**
     * This method is responsable for get the handler
     * of the form of data for the history 
     * @return the win/loss result handler
     */
    public HistoryHandler getHistoryHandler() {
        return historyHandler;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        historyHandler = new HistoryHandler();
    }
}
