package org.mbari.vcr.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Test program used for developing the VCR controls. This is a stand alone
 * application. To use run <i>java org.mbari.vcr.ui.VCRConsole</i> at the
 * command line.</p>
 *
 * @author  : $Author: hohonuuli $
 * @version : $Revision: 332 $
 */
public class VCRApp {

    private static final Log log = LogFactory.getLog(VCRApp.class);

    /**
	 * @uml.property  name="frame"
	 * @uml.associationEnd  
	 */
    private JFrame frame;

    /** Construct the application */
    public VCRApp() {
        initialize();
    }

    /**
	 * @return  the frame
	 * @uml.property  name="frame"
	 */
    protected JFrame getFrame() {
        if (frame == null) {
            frame = new VCRConsoleFrame();
        }
        return frame;
    }

    private void initialize() {
        JFrame f = getFrame();
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = f.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        f.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        f.setVisible(true);
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.info("Unable to set look and feel", e);
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new VCRApp();
            }
        });
    }
}
