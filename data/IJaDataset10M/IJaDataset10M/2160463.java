package gui_loadingFrames;

import gui_mainFrame.PatientWindow_tabbedPane;
import java.awt.Color;
import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import language.Messages;
import main.ISettings;

/**
 * 
 * @author debous
 * @Create 02/04/2011
 * @lastUpdate 04/04/2011
 * 
 * Loading frame
 */
public class LoadingFrame extends JFrame implements ISettings {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8847245078801030501L;

    private JLabel labelLogo;

    /**
	 * Constructor
	 * @param patient The patient
	 */
    public LoadingFrame() {
        super("Asclepius " + VERSION);
        this.createComponents();
        this.createPanels();
    }

    /**
	 * Create components
	 */
    private void createComponents() {
        this.labelLogo = new JLabel();
        ImageIcon icon = createImageIcon(PATH_ASCLEPIUS_LOGO);
        labelLogo.setIcon(icon);
        labelLogo.setBackground(Color.WHITE);
        labelLogo.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
	 * Get an icon
	 * @param path The icon path
	 * @return an icon
	 */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = PatientWindow_tabbedPane.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println(Messages.getInstance().getString("About.15") + path);
            return null;
        }
    }

    /**
	 * Create all panels
	 */
    private void createPanels() {
        Container paneNewWindow = this.getContentPane();
        paneNewWindow.add(labelLogo);
        this.setSize(700, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setVisible(true);
    }
}
