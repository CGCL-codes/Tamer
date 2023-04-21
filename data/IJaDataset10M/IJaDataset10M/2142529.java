package com.tapper.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.tapper.dialogs.*;

public class AboutBoxAction extends AbstractAction {

    private static AboutBoxAction theOnlyOne = null;

    public static AboutBoxAction getInstance() {
        if (theOnlyOne == null) {
            theOnlyOne = new AboutBoxAction();
        }
        return theOnlyOne;
    }

    private AboutBoxAction() {
        super("About Tapper");
    }

    public void actionPerformed(ActionEvent e) {
        AboutBoxDialog.getInstance().setVisible(true);
    }
}
