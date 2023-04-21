package saf.demo;

import saf.core.ui.actions.AbstractSAFAction;
import java.awt.event.ActionEvent;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2005/11/21 18:55:57 $
 */
public class OpenDirectoryAction extends AbstractSAFAction<DemoApplication> {

    public void actionPerformed(ActionEvent e) {
        workspace.getApplicationMediator().openDirectory();
    }
}
