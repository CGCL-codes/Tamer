package org.columba.core.gui.globalactions;

import java.awt.event.ActionEvent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.gui.profiles.ProfileManagerDialog;

/**
* Opens profile management dialog.
*
* @author fdietz
*/
public class OpenProfileManagerDialogAction extends AbstractColumbaAction {

    /**
   * @param frameMediator
   * @param name
   */
    public OpenProfileManagerDialogAction(IFrameMediator frameMediator) {
        super(frameMediator, "Profile Manager...");
    }

    public void actionPerformed(ActionEvent evt) {
        new ProfileManagerDialog(getFrameMediator());
    }
}
