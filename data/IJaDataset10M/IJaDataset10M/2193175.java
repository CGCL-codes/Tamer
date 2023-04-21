package be.vds.jtbdive.client.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import be.vds.jtbdive.client.view.core.LogBookApplFrame;
import be.vds.jtbdive.client.view.utils.UIAgent;

public class CheckUpdateAction extends AbstractAction {

    private static final long serialVersionUID = 8087691657727396391L;

    private LogBookApplFrame logBookApplFrame;

    public CheckUpdateAction(LogBookApplFrame logBookApplFrame) {
        super("update.check");
        this.logBookApplFrame = logBookApplFrame;
        putValue(Action.SMALL_ICON, UIAgent.getInstance().getIcon(UIAgent.ICON_UPDATE_16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logBookApplFrame.checkForUpdate(true, true, false, false);
    }
}
