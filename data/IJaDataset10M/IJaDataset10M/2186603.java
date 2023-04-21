package client.gui.listeners;

import client.gui.*;
import java.awt.event.*;
import javax.swing.*;

public class LogInChkCreate extends MainActionListener {

    public LogInChkCreate(Object obj) {
        component = obj;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (((WLogIn) component).isChecked()) {
            ((WLogIn) component).getCreateCharWindow().setVisible(true);
        } else {
            ((WLogIn) component).getCreateCharWindow().setVisible(false);
        }
    }
}
