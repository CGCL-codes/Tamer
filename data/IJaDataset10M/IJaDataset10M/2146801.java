package net.sourceforge.ondex.ovtk2.ui.dialog;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import net.sourceforge.ondex.ovtk2.config.Config;
import net.sourceforge.ondex.ovtk2.ui.OVTK2Desktop;
import net.sourceforge.ondex.ovtk2.ui.OVTK2Desktop.Position;
import net.sourceforge.ondex.ovtk2.ui.OVTK2HyperlinkListener;
import net.sourceforge.ondex.ovtk2.util.ErrorDialog;

/**
 * Shown when the OVTK2 is opened.
 * 
 * @author taubertj
 * @version 18.02.2010
 */
public class WelcomeDialog extends JInternalFrame implements ItemListener, HyperlinkListener {

    /**
	 * Provides background image functionality.
	 * 
	 * @author taubertj
	 * @version 14.05.2008
	 */
    private class Background extends JPanel {

        /**
		 * generated
		 */
        private static final long serialVersionUID = 6202280312345066463L;

        /**
		 * wrapped ImageIcon as background
		 */
        private ImageIcon icon = null;

        /**
		 * Sets background ImageIcon.
		 * 
		 * @param icon
		 *            ImageIcon for background
		 */
        public Background(ImageIcon icon) {
            this.icon = icon;
            this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension d = this.getSize();
            g.drawImage(icon.getImage(), 0, 0, d.width, d.height, this);
        }
    }

    /**
	 * Singleton pattern for WelcomeDialog.
	 */
    private static WelcomeDialog instance = null;

    /**
	 * generated
	 */
    private static final long serialVersionUID = -5639988006359640211L;

    /**
	 * Returns instance of WelcomeDialog for singelton pattern.
	 * 
	 * @return WelcomeDialog
	 */
    public static WelcomeDialog getInstance(OVTK2Desktop desktop) {
        if (instance == null) instance = new WelcomeDialog(desktop);
        return instance;
    }

    /**
	 * Setup GUI for welcome dialog.
	 * 
	 * @param desktop
	 *            current OVTK2Desktop
	 */
    private WelcomeDialog(OVTK2Desktop desktop) {
        super(Config.language.getProperty("Welcome.Title"), true, true, true, true);
        this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        String s = File.separator;
        String path = Config.ovtkDir + s + "themes" + s + Config.config.getProperty("Program.Theme") + s + "icons" + s + "wizard.png";
        if (!new File(path).exists()) {
            ErrorDialog.show(new IOException("File not found " + path));
        }
        this.setFrameIcon(createImageIcon(path));
        path = Config.ovtkDir + s + "themes" + s + Config.config.getProperty("Program.Theme") + s + "images" + s + "welcome.png";
        if (!new File(path).exists()) {
            ErrorDialog.show(new IOException("File not found " + path));
        }
        ImageIcon icon = createImageIcon(path);
        JPanel contentPane = new Background(icon);
        this.setContentPane(contentPane);
        JEditorPane text = new JEditorPane();
        text.setOpaque(false);
        text.setEditable(false);
        File helpLocation = new File(Config.ovtkDir + s + "help" + s + Config.config.getProperty("Program.Language") + s + "welcome.html");
        URL helpURL = null;
        try {
            helpURL = helpLocation.toURI().toURL();
        } catch (MalformedURLException mue) {
            ErrorDialog.show(mue);
        }
        if (helpURL != null) {
            try {
                text.setPage(helpURL);
            } catch (IOException e) {
                ErrorDialog.show(e);
            }
        } else {
            System.err.println("Couldn't find file: " + helpLocation.getAbsolutePath());
        }
        text.addHyperlinkListener(new OVTK2HyperlinkListener(desktop));
        text.addHyperlinkListener(this);
        JCheckBox checkBox = new JCheckBox(Config.language.getProperty("Welcome.CheckBox"));
        checkBox.addItemListener(this);
        checkBox.setSelected(true);
        checkBox.setOpaque(false);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(text).addComponent(checkBox));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(text).addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(checkBox)));
        this.pack();
        desktop.display(this, Position.centered);
    }

    /**
	 * Returns an ImageIcon, or null if the path was invalid.
	 */
    protected ImageIcon createImageIcon(String path) {
        File imgLocation = new File(path);
        URL imageURL = null;
        try {
            imageURL = imgLocation.toURI().toURL();
        } catch (MalformedURLException mue) {
            System.err.println(mue.getMessage());
        }
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e.getDescription().startsWith("ovtk2://") || e.getDescription().equals("close")) {
                instance.setVisible(false);
            }
        }
    }

    /**
	 * Change show preferences.
	 */
    @Override
    public void itemStateChanged(ItemEvent ie) {
        Properties properties = new Properties();
        try {
            File file = new File(Config.ovtkDir + File.separator + "config.xml");
            if (!file.exists()) {
                ErrorDialog.show(new IOException("File not found " + file.getAbsolutePath()));
            }
            properties.loadFromXML(new FileInputStream(file));
            if (ie.getStateChange() == ItemEvent.DESELECTED) {
                properties.setProperty("Welcome.Show", "false");
            } else {
                properties.setProperty("Welcome.Show", "true");
            }
            properties.storeToXML(new FileOutputStream(file), "Modified by " + Config.config.getProperty("Program.Name") + " - Version " + Config.config.getProperty("Program.Version"));
            Config.config.setProperty("Welcome.Show", properties.getProperty("Welcome.Show"));
        } catch (Exception e) {
            ErrorDialog.show(e);
        }
    }
}
