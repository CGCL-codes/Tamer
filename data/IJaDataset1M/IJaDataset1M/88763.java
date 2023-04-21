package uk.ac.bath.gui;

import uk.ac.bath.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.*;

class TextTweaker_1 implements ActionListener {

    Tweakable t;

    JTextField textField;

    TextTweaker_1(TweakerPanel p, Tweakable t) {
        this.t = t;
        int len = t.getMaximum().toString().length();
        textField = new JTextField(String.valueOf(t.getNumber()), len);
        textField.addActionListener(this);
        p.add(new JLabel(t.getLabel()), textField);
    }

    public void actionPerformed(ActionEvent e) {
        t.set(textField.getText());
        textField.setText(t.toString());
    }
}
