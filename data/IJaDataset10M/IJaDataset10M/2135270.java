package gui;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import apapl.messaging.*;
import java.util.ArrayList;

public class MessageTab extends JTable implements MessageListener {

    private MessagesModel model;

    private boolean doUpdate = false;

    public MessageTab() {
        model = new MessagesModel();
        ListSelectionModel sm = getSelectionModel();
        update();
    }

    public void messageSent(APLMessage message) {
        model.addMessage(message);
        if (isShowing()) update();
    }

    private void update() {
        setModel(model);
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            System.out.println("oops");
        }
        Runnable update = new Runnable() {

            public void run() {
                updateUI();
            }
        };
        SwingUtilities.invokeLater(update);
    }
}
