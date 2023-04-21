package fr.ign.cogit.geoxygene.util.viewer;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This class defines the status bar of the ObjectViewer's GUI.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 * 
 */
class ObjectViewerStatusBar extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2425962277258109509L;

    public static final String DEFAULT_STATUS = "Ready.";

    public ObjectViewerInterface objectViewerInterface;

    private JLabel statusJLabel;

    public ObjectViewerStatusBar(ObjectViewerInterface objectViewerInterface) {
        this.objectViewerInterface = objectViewerInterface;
        this.statusJLabel = new JLabel(ObjectViewerStatusBar.DEFAULT_STATUS, SwingConstants.LEFT);
        this.add(this.statusJLabel, BorderLayout.WEST);
    }

    public void setObjectViewerInterface(ObjectViewerInterface objectViewerInterface) {
        this.objectViewerInterface = objectViewerInterface;
    }

    public ObjectViewerInterface getObjectViewerInterface() {
        return this.objectViewerInterface;
    }

    public void setText(String text) {
        this.statusJLabel.setText(text);
    }

    /**
   * @return the Label of the status bar.
   */
    public JLabel getStatusJLabel() {
        return this.statusJLabel;
    }

    /**
   * @param label
   */
    public void setStatusJLabel(JLabel label) {
        this.statusJLabel = label;
    }
}
