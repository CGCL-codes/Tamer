package org.xmlcml.cmlimpl.jumbo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class JumboEvent extends org.xmlcml.cml.mvc.MoleculeEvent {

    protected String message;

    public JumboEvent(Object source) {
        super(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        String s = "JumboEvent";
        s += "[messsage = " + message + "]";
        return s;
    }
}
