package gunit.container;

import java.awt.*;
import java.awt.event.*;

/**
 * <code>AWTTestContainer</code> is an implementation of 
 * <code>TestContainer</code> that uses AWT Frame as the container
 */
public class AWTTestContainer implements gunit.framework.TestContainer {

    Frame testContainer;

    public AWTTestContainer() {
        this.testContainer = new Frame("GUnit:AWT Test Frame");
        showTestContainer();
    }

    private static final int DEFAULT_WIDTH = 640;

    private static final int DEFAULT_HEIGHT = 442;

    public void showTestContainer() {
        this.testContainer.setLayout(new BorderLayout());
        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;
        String dim = System.getProperty("gunit.awt.frame.window.size", "640x442");
        if (dim != null && dim.length() > 0) {
            int index = dim.indexOf("x");
            if (index > 0) {
                try {
                    width = Integer.parseInt(dim.substring(0, index));
                    height = Integer.parseInt(dim.substring(index + 1));
                } catch (Exception ex) {
                }
            }
        }
        this.testContainer.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        if (isJ2SE()) this.testContainer.setSize(width, height);
        this.testContainer.setVisible(true);
    }

    public Container getContainer() {
        return this.testContainer;
    }

    public static boolean isJ2SE() {
        try {
            Class.forName("javax.naming.Context");
            return true;
        } catch (Exception ex) {
        }
        return false;
    }
}
