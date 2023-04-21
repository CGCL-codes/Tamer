package test.check;

import javax.swing.JPanel;

/**
 * Base interface for test application panels that wish to contribute a control
 * panel to the control task pane container.
 * 
 * @author Kirill Grouchnikov
 */
public interface Controllable {

    /**
	 * Returns the control panel.
	 * 
	 * @return The control panel.
	 */
    public JPanel getControlPanel();
}
