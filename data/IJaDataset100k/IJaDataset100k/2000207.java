package ebiNeutrino.core.gui.Dialogs;

import ebiNeutrinoSDK.EBIPGFactory;
import javax.swing.*;
import java.awt.*;

public class EBILoaderPanel extends JPanel {

    public JProgressBar jProgressBar = null;

    private JPanel content = null;

    /**
	 * This is the default constructor
	 */
    public EBILoaderPanel(final String modul) {
        super();
        setLayout(null);
        initialize();
        setBackground(new Color(244, 244, 244));
        jProgressBar.setString(modul + " " + EBIPGFactory.getLANG("EBI_LANG_LOADING_SPEC_MODULE"));
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        if (content == null) {
            content = new JPanel();
            add(content, null);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            content.setLocation(((int) dim.getWidth() / 2) - 250, ((int) dim.getHeight() / 2) - 240);
            content.setSize(560, 475);
            content.setBackground(new Color(244, 244, 244));
            JLabel jLabel1 = new JLabel();
            jLabel1.setBounds(new java.awt.Rectangle(0, 0, 290, 283));
            jLabel1.setIcon(new ImageIcon("images/ld.gif"));
            jLabel1.setText("");
            JLabel jLabel = new JLabel();
            jLabel.setBounds(new java.awt.Rectangle(146, 39, 408, 34));
            jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
            jLabel.setForeground(new java.awt.Color(0, 111, 255));
            jLabel.setText(EBIPGFactory.getLANG("EBI_LANG_LOADING_TEXT"));
            content.setLayout(null);
            content.add(getJProgressBar(), null);
            content.add(jLabel, null);
            content.add(jLabel1, null);
        }
    }

    /**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
    private JProgressBar getJProgressBar() {
        if (jProgressBar == null) {
            jProgressBar = new JProgressBar();
            jProgressBar.setBounds(new java.awt.Rectangle(17, 133, 518, 21));
            jProgressBar.setIndeterminate(true);
            jProgressBar.setStringPainted(true);
        }
        return jProgressBar;
    }
}
