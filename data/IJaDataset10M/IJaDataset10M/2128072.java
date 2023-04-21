package org.enclojure.nbmodule;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Action which shows CljREPLTopWindow component.
 */
public class CljREPLTopWindowAction extends AbstractAction {

    public CljREPLTopWindowAction() {
        super(NbBundle.getMessage(CljREPLTopWindowAction.class, "CTL_CljREPLTopWindowAction"));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = CljREPLTopWindowTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
