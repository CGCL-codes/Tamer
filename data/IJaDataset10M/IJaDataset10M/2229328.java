package com.loribel.commons.swing.border;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.loribel.commons.swing.GB_Frame;

/**
 * Demo for {@link GB_CompTitledBorder_Demo}.
 *
 * @author Gr�gory Borelli
 */
public class GB_CompTitledPanel_Demo extends GB_CompTitledPanel {

    public GB_CompTitledPanel_Demo() {
        super();
        init();
    }

    private void init() {
        JCheckBox l_check = new JCheckBox("titleWithCheck");
        this.setTitleComponent(l_check);
        JPanel l_contentPane = getContentPane();
        l_contentPane.setLayout(new FlowLayout());
        l_contentPane.add(new JLabel("label..."));
        l_contentPane.add(new JTextField("text field..."));
        l_contentPane.add(new JButton("Button"));
    }

    public static void main(String[] p) {
        JComponent l_demo = new GB_CompTitledPanel_Demo();
        l_demo = new JScrollPane(l_demo);
        GB_Frame l_frame = new GB_Frame("Test GB_CompTitledPanel");
        l_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l_frame.setSize(new Dimension(800, 600));
        l_frame.centerWithScreen();
        l_frame.setMainPanel(l_demo);
        l_frame.setVisible(true);
    }
}
