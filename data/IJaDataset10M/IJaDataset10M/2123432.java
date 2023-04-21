package com.l2fprod.skinbuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 * SynthTest. <br>
 * 
 */
public class SynthTest {

    public SynthTest() {
        super();
    }

    public static void main(String[] args) throws Exception {
        SynthLookAndFeel lnf = new SynthLookAndFeel();
        lnf.load(Class.forName("com.l2fprod.styles.simple.Dummy").getResourceAsStream("synth.xml"), Class.forName("com.l2fprod.styles.simple.Dummy"));
        UIManager.setLookAndFeel(lnf);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame internal = new JInternalFrame("The title");
        internal.setResizable(true);
        internal.setIconifiable(true);
        internal.setMaximizable(true);
        internal.setClosable(true);
        internal.setSize(200, 150);
        internal.setVisible(true);
        internal.setLayout(new BorderLayout());
        internal.add("North", new JTextField());
        desktop.add(internal);
        frame.add("Center", desktop);
        JButton redButton = new JButton("I'm a button, my background is Color.RED");
        redButton.setBackground(Color.red);
        redButton.setOpaque(true);
        redButton.setContentAreaFilled(false);
        frame.add("South", redButton);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
