package net.sf.freecol.client.gui.action;

import java.awt.event.ActionEvent;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;

/**
 * An action for displaying a Military Report.
 */
public class ReportMilitaryAction extends FreeColAction {

    public static final String id = "reportMilitaryAction";

    /**
     * Creates this action.
     * @param freeColClient The main controller object for the client.
     * @param gui 
     */
    ReportMilitaryAction(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui, id);
    }

    /**
     * Applies this action.
     * @param e The <code>ActionEvent</code>.
     */
    public void actionPerformed(ActionEvent e) {
        gui.showReportMilitaryPanel();
    }
}
