package net.sf.moviekebab.startup;

import javax.swing.JOptionPane;

/**
 * Just displays a "Hello, world" dialog and quits.
 * Useful for testing packaging.
 *
 * @author Laurent Caillette
 */
public class Hello {

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Hello, world!");
        System.exit(0);
    }
}
