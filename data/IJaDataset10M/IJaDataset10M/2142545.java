package com.towerfense;

import java.awt.BorderLayout;
import javax.swing.JFrame;

class TowerFrame extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4540500854002411471L;

    public TowerFrame() {
        setLayout(new BorderLayout());
        add("Center", new FenseCanvas());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        setVisible(true);
    }
}
