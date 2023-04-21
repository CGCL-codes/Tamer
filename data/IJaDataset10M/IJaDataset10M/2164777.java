package src.gui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import src.Constants;
import src.NitsLoch;

public class AboutFrame extends JFrame {

    private static final long serialVersionUID = Constants.serialVersionUID;

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel lblVersion = new JLabel();

    JLabel lblEngineAuthor = new JLabel();

    JLabel lblMapFormatAuthor = new JLabel();

    JLabel lblWebpage = new JLabel();

    JLabel lblWebsite1 = new JLabel();

    JLabel lblWebsite2 = new JLabel();

    JLabel lblIRCInfo = new JLabel();

    JLabel lblIRCLocation = new JLabel();

    JLabel lblCredits = new JLabel();

    JLabel lblPicture = new JLabel();

    public AboutFrame() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(gridBagLayout1);
        lblVersion.setFont(new java.awt.Font("Dialog", Font.BOLD, 15));
        lblVersion.setForeground(Color.red);
        lblVersion.setText("NitsLoch");
        lblEngineAuthor.setText("Engine by Darren Watts");
        lblMapFormatAuthor.setText("Map Format by Jonathan Irons");
        lblWebpage.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        lblWebpage.setForeground(Color.blue);
        lblWebpage.setToolTipText("");
        lblWebpage.setText("Relevant websites:");
        lblWebsite1.setText("http://http://sourceforge.net/projects/nitsloch/");
        lblWebsite2.setText("http://lochnits.com");
        lblIRCInfo.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        lblIRCInfo.setForeground(Color.blue);
        lblIRCInfo.setText("IRC:");
        lblIRCLocation.setText("#nitsloch on irc.freenode.net");
        lblCredits.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        lblCredits.setForeground(Color.blue);
        lblCredits.setText("Credits:");
        try {
            URL u = NitsLoch.class.getResource("data/lochnits.gif");
            Icon icon = new ImageIcon(u);
            lblPicture.setIcon(icon);
        } catch (Exception ex) {
        }
        this.getContentPane().add(lblIRCLocation, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblIRCInfo, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblWebsite2, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblWebsite1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblWebpage, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblEngineAuthor, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblVersion, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblMapFormatAuthor, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblCredits, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 0, 0, 0), 0, 0));
        this.getContentPane().add(lblPicture, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        lblVersion.setText("NitsLoch v" + src.Constants.version);
        setSize(330, 310);
        setTitle("About NitsLoch");
        src.Constants.centerFrame(this);
        setVisible(true);
    }
}
