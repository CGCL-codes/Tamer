package com.jmonkey.universal.sqltools.action;

import com.jmonkey.universal.sqltools.Main;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 *  Description of the Class
 *
 *@author     <A HREF="mailto:brillpappin@hotmail.com">Brill Pappin</A>
 *@created    October 21, 2001
 *@version    0.1 Revision 0
 */
public final class VersionAction extends AbstractAction {

    private Main _OWNER = null;

    /**
	 *  Constructor for the VersionAction object
	 *
	 *@param  main  Description of Parameter
	 *@since        0.1.0
	 */
    public VersionAction(Main main) {
        super("Check Version...", main.getIcon("server.16"));
        _OWNER = main;
        this.setEnabled(false);
    }

    /**
	 *  Description of the Method
	 *
	 *@param  e  Description of Parameter
	 *@since     0.1.0
	 */
    public void actionPerformed(ActionEvent e) {
        _OWNER.doVersion();
    }
}
