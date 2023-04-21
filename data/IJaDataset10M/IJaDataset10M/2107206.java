package test.xito.dazzle.dialog;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import org.xito.dialog.*;

public class PropsLayout {

    public static void main(String args[]) {
        JFrame f = new JFrame("PropsLayout");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TablePanel contentPane = new TablePanel(new TableLayout(PropsLayout.class.getResource("props_layout.html")));
        contentPane.setPaintBorderLines(true);
        f.getContentPane().add(contentPane);
        contentPane.add("description", new JLabel("This is a test of a property editor type layout"));
        contentPane.add("prop_lbl_1", new JLabel("Property 1:"));
        contentPane.add("prop_lbl_2", new JLabel("This is Property 2:"));
        contentPane.add("prop_lbl_3", new JLabel("My Property 3:"));
        contentPane.add("prop_1", new JTextField("Property Value 1"));
        contentPane.add("prop_2", new JTextField("Property Value 2"));
        contentPane.add("prop_3", new JTextField("Property Value 3"));
        contentPane.add("separator", new JSeparator(JSeparator.HORIZONTAL));
        f.pack();
        f.setVisible(true);
    }
}
