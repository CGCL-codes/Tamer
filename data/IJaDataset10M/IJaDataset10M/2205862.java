package org.jbubblebreaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * AboutBox
 * @author Sven Strickroth
 */
@SuppressWarnings("serial")
public class AboutBox extends MyJDialog implements ActionListener {

    private static final String icon = "jbubblebreaker.png";

    /**
	 * Create and show the AboutBox
	 */
    public AboutBox() {
        super(Localization.getI18n().tr("About jBubbleBreaker"), icon, 332, 210);
        setModal(true);
        if (JBubbleBreaker.isApplicationMode() == false) {
            setSize(332, 227);
        }
        setLayout(null);
        final PicturePanel titlePanel = new PicturePanel("jBubbleBreaker-text.png", 219, 43);
        titlePanel.setLocation(64, 10);
        titlePanel.setToolTipText("jBubbleBreaker");
        getContentPane().add(titlePanel);
        PicturePanel panel = new PicturePanel(icon, 32, 32);
        panel.setBounds(10, 10, 32, 32);
        getContentPane().add(panel);
        JLabel lblVersion = new JLabel(Localization.getI18n().tr("Version ") + JBubbleBreaker.getVersion());
        lblVersion.setBounds(64, 56, 252, 14);
        getContentPane().add(lblVersion);
        JButton okButton = new JButton(Localization.getI18n().tr("OK"));
        okButton.setBounds(221, 152, 95, 23);
        okButton.addActionListener(this);
        getContentPane().add(okButton);
        JLabel lblCopyright = new JLabel("Coded by Sven Strickroth, 2007 - 2010");
        lblCopyright.setVerticalAlignment(SwingConstants.TOP);
        lblCopyright.setBounds(64, 76, 250, 25);
        getContentPane().add(lblCopyright);
        JLabel lblComment = new JLabel("<html>GPLv3 BubbleBreaker Java clone,<br>Hosted on SourceForge.net:<br>" + JBubbleBreaker.getProjectHomepage() + "</html>");
        lblComment.setVerticalAlignment(SwingConstants.TOP);
        lblComment.setBounds(64, 99, 250, 50);
        getContentPane().add(lblComment);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent arg0) {
        dispose();
    }
}
