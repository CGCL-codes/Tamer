package com.dukesoftware.utils.test.jnlp.sample;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class HelloJWS {

    private static final long serialVersionUID = 1L;

    public static void main(String argv[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel("HelloJWS");
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
