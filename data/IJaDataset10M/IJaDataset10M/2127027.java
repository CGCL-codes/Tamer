package timeKeeper.gui.action;

import java.awt.event.ActionEvent;
import java.awt.Frame;
import javax.swing.AbstractAction;
import base.resources.Resources;
import timeKeeper.gui.dialogs.SessionTimesDialog;

/**
 * Class shows a Menu entry and implements the action to a dialog for input start
 * and up to four target times
 */
public class SessionTimesAction extends AbstractAction {

    private final Frame oFrame;

    private SessionTimesDialog oSessionTimes;

    /**
     * Constructor configures a new Menu entry
     * @param fr
     */
    public SessionTimesAction(final Frame fr) {
        super(Resources.getText("timekeeper.menu.time.action.session_times"));
        oFrame = fr;
    }

    public void actionPerformed(ActionEvent e) {
        if (oSessionTimes == null) {
            oSessionTimes = new SessionTimesDialog(oFrame);
        }
        oSessionTimes.setVisible(true);
    }
}
