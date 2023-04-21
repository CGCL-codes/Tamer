package voji.vdialog.interfaces;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import voji.vdialog.*;

public class Dialog extends java.awt.Dialog implements VDialogInterface {

    public Dialog() {
        super(VMain.getOwner());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                hide();
            }
        });
    }

    public void close() {
        hide();
    }

    public Container getContentPane() {
        return this;
    }
}
