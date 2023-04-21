package com.arsenal.session.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.arsenal.rtcomm.client.*;
import com.arsenal.message.*;
import com.arsenal.session.client.*;

public class DeleteSessionButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        if (!CreateEditSessionWindow.getInstance().sessionNotChosen()) {
            SessionClientHandler.getInstance().sendMessage(CreateEditSessionWindow.getInstance().getDeleteSessionMessage());
            CreateEditSessionWindow.getInstance().hide();
        }
    }
}
