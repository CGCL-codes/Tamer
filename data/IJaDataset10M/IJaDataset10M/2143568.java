package com.loribel.commons.swing.border;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.loribel.commons.swing.GB_Frame;

/**
 * Demo for {@link GB_CompTitledBorder_Demo}.
 *
 * @author Gr�gory Borelli
 */
public class DemoT1 extends JPanel {

    public DemoT1() {
        super();
        init();
    }

    GridBagLayout layout;

    private void init() {
        layout = new GridBagLayout();
        this.setLayout(layout);
        Insets insets = new Insets(5, 5, 5, 5);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = insets;
        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(new JButton("Test..."));
        p1.setOpaque(false);
        p1.setBorder(BorderFactory.createTitledBorder("                 "));
        this.add(p1, c);
        JCheckBox check = new JCheckBox("My check box");
        check.setOpaque(true);
        check.setBackground(Color.RED);
        check.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        c.insets = new Insets(0, 10, 0, 5);
        c.fill = c.NONE;
        c.anchor = c.NORTHWEST;
        this.add(check, c);
    }

    public static void main(String[] p) {
        JComponent l_demo = new DemoT1();
        GB_Frame l_frame = new GB_Frame("Test CompTitledBorder");
        l_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l_frame.setSize(new Dimension(800, 600));
        l_frame.centerWithScreen();
        l_frame.setMainPanel(l_demo);
        l_frame.setVisible(true);
    }
}
