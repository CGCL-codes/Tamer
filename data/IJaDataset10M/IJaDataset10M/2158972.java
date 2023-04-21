package net.sourceforge.ftpowl.controller;

import net.sourceforge.ftpowl.gui.GUI;

/** The main-controller of FTPOwl
 * @author <a href="mailto:admiral_kay@users.sourceforge.net" title="Kay Patzwald">Kay Patzwald </a>
 *
 */
public class FTPOwlMainController {

    private static FTPOwlMainController controller;

    /** Returns an instance of the FTPOwlMainController
	 * @return FTPOwlMainController
	 */
    public static FTPOwlMainController getInstance() {
        if (controller == null) controller = new FTPOwlMainController();
        return controller;
    }

    /** Constructor of FTPOwlMainController
	 *
	 */
    public FTPOwlMainController() {
        new GUI();
    }
}
