package net.dnsalias.pcb.texteditor.gui;

import javax.swing.*;

public class UpdateThread implements Runnable {

    private JLabel label;

    private String str;

    public UpdateThread(JLabel label, String str) {
        this.label = label;
        this.str = str;
        javax.swing.SwingUtilities.invokeLater(this);
    }

    public void run() {
        synchronized (label) {
            if (str != null && !(str.equals(""))) {
                label.setText(str);
            }
        }
    }
}
