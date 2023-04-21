package net.sourceforge.aglets.examples.patterns;

import java.awt.Frame;

/**
 * Class PopUpMessageWindow is a popup window for error messages.
 * 
 * @version 1.00 96/07/22
 * @author Yariv Aridor
 */
class PopUpMessageWindow extends MessageDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -161851958524536808L;

    PopUpMessageWindow(Frame parent, String title, String message) {
        super(parent, title, message);
        this.setButtons(GeneralDialog.OKAY);
    }
}
