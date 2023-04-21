package com.nullfish.app.jfd2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class ComboBoxTest {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame frame = new JFrame();
            final JComboBox combo = new JComboBox();
            combo.addItem("1");
            combo.addItem("2");
            combo.addItem("3");
            combo.addItem("4");
            combo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    System.out.println(arg0);
                    System.out.println(combo.getSelectedItem());
                }
            });
            combo.setEditable(true);
            System.out.println(combo.getEditor());
            frame.getContentPane().add(combo);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
