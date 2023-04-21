package sts.gui.actions;

import javax.swing.*;
import sts.hibernate.*;
import sts.framework.*;
import sts.gui.*;
import kellinwood.meshi.manager.MFEvent;

/**
 *
 * @author ken
 */
public class CloseRegattaAction extends BaseRegattaEnabledAction {

    /** Singleton instance. */
    private static CloseRegattaAction me;

    /** Private constructor enforces singleton pattern. */
    private CloseRegattaAction() {
        this.putValue(Action.NAME, "Close Regatta");
    }

    /** Returns a reference to the only isntance of this class. */
    public static CloseRegattaAction onlyInstance() {
        if (me == null) me = new CloseRegattaAction();
        return me;
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        Framework.onlyInstance().closeRegatta();
    }
}
