package com.enerjy.analyzer.java.rules.testfiles.T0080;

import java.awt.Container;
import javax.swing.JPanel;

/**
 * @author Peter Carr
 */
public class PTest20 {
}

class PTest20Top1<T extends Container> {

    T controlPanel = null;

    public <P extends JPanel> void addComponent(P theComponent) {
        controlPanel.add(theComponent);
    }
}
