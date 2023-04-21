package org.furthurnet.furi;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
* The ActionViewAbout class performs the operation of displaying the About
* dialog box describing this program.
*/
public class ActionViewAbout extends ActionBase {

    public ActionViewAbout(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public ActionViewAbout() {
    }

    public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(mFrame, "Furthur Network v" + Res.getStr("Program.Version") + "\n\n" + "Copyright (C) 2001-2005 Furthur Network\n" + "All Rights Reserved\n\n" + "Website: " + Res.getStr("Program.Url"), "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refresh() {
        setEnabled(true);
    }
}
