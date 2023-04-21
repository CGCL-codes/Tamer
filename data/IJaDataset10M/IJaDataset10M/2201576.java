package net.sf.jlibdc1394.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import net.sf.jlibdc1394.Camera;

/**
 * 
 * @author     Alexander Bieber <fleque@users.sourceforge.net>
 */
public class JInternalFrameCamSettings extends JInternalFrame {

    /**
	 * 
	 */
    private Camera theCamera;

    private JTabbedPaneCamSettings tabbedPaneControls;

    public JInternalFrameCamSettings(Camera theCamera) {
        super();
        this.getContentPane().setLayout(new BorderLayout());
        setSize(new Dimension(200, 60));
        this.theCamera = theCamera;
        this.setResizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        tabbedPaneControls = new JTabbedPaneCamSettings(theCamera);
        this.getContentPane().add(tabbedPaneControls, BorderLayout.CENTER);
    }

    public void dispose() {
        super.dispose();
    }
}
