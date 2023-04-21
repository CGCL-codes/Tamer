package com.brrus.icbinabr;

import javax.swing.*;

/**
 * @author arroyoj
 *
 */
public class hello {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String text = "Hello World!";
        printMsg(text);
        printMsg("Are we done Yet?");
        printMsg("Watch the Pretty Window...");
        JFrame f = new JFrame();
        f.setTitle("Hello World Test Application");
        f.setSize(200, 200);
        JPanel p = new JPanel();
        JButton b = new JButton();
        b.setText("Test Button");
        p.add(b);
        f.setContentPane(p);
        f.setVisible(true);
    }

    public static void printMsg(String msg3) {
        System.out.println(msg3);
    }

    public static void doNothing() {
    }
}
