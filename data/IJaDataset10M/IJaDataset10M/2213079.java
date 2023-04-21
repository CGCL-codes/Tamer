package com.directthought.elasticweb.client;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * This class creates the basic frame and parses command line params.
 * 
 * @author <a href="mailto:dak@directThought.com">David Kavanagh</a>
 */
public class ElasticWebMonitor {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("ElasticWeb Monitor");
        final ElasticWebControls controls = new ElasticWebControls(frame);
        Dimension size = controls.getPreferredSize();
        frame.setSize(size.width + 7, size.height + 27);
        int x = UserPrefs.getInstance().getWindowX();
        int y = UserPrefs.getInstance().getWindowY();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        if (x == -1 && y == -1) {
            x = (dim.width - frame.getSize().width) / 2;
            y = (dim.height - frame.getSize().height) / 2;
        }
        frame.setLocation(x, y);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                controls.cleanup();
                Point loc = frame.getLocation();
                UserPrefs.getInstance().setWindowX(loc.x);
                UserPrefs.getInstance().setWindowY(loc.y);
                System.exit(0);
            }
        });
        frame.setContentPane(controls);
        frame.setVisible(true);
        controls.mainLoop();
    }
}
