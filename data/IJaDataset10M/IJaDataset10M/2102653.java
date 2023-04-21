package voji.vdialog.interfaces;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import voji.vdialog.*;

public class Frame extends java.awt.Frame implements VDialogInterface {

    public Frame() {
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
