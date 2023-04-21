package com.objectwave.viewUtility;

import java.lang.*;
import com.objectwave.event.*;
import com.objectwave.uiWidget.MessageBox;
import javax.swing.JOptionPane;
import java.awt.Frame;

/**
* This will popup a dialog box every time an exception is the
* source for a status event.
*/
public class ExceptionListener implements StatusEventListener {

    Frame f = new Frame();

    public void updateStatus(StatusEvent evt) {
        if (!(evt.getSource() instanceof Exception)) return;
        final Exception e = (Exception) evt.getSource();
        Runnable r = new Runnable() {

            public void run() {
                JOptionPane.showMessageDialog(null, e.toString(), "Exception!", JOptionPane.ERROR_MESSAGE);
            }
        };
        new Thread(r).start();
    }
}
