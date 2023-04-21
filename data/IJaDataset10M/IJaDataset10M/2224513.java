package org.eyrene.jplayer.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class JPlayerGUI extends JFrame implements JPlayerUI {

    /**
     * Constructor
     * 
     */
    public JPlayerGUI() {
        super("JPlayer");
        init();
    }

    /**
     * Init method
     */
    private void init() {
    }

    public void start() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
    }

    public void exit() {
        System.exit(0);
    }
}
