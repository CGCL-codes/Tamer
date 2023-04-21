package edu.drexel.sd0910.ece01.aqmon.gui.wizard.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import edu.drexel.sd0910.ece01.aqmon.config.ConfigKeys;
import edu.drexel.sd0910.ece01.aqmon.gui.MainModel;
import edu.drexel.sd0910.ece01.aqmon.util.ImageUtils;
import edu.drexel.sd0910.ece01.aqmon.util.OSUtils;

/**
 * Wizard - Welcome panel. This is the first panel shown during the setup
 * wizard. The Cancel button will return to this panel.
 * 
 * @author Kyle O'Connor
 * 
 */
@SuppressWarnings("serial")
public class WelcomePanel extends JPanel {

    private JLabel blankSpace;

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jLabel6;

    private JLabel jLabel7;

    private JLabel jLabel8;

    private JLabel jLabel9;

    private JLabel labelLink;

    private JLabel space;

    private JLabel jLabel10;

    private JLabel welcomeTitle;

    private JPanel contentPanel;

    private JPanel imagePanel;

    private JLabel iconLabel;

    private ImageIcon icon;

    protected MainModel model;

    public WelcomePanel(MainModel aModel) {
        this.model = aModel;
        iconLabel = new JLabel();
        imagePanel = getImagePanel();
        contentPanel = getContentPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        icon = ImageUtils.createImageIcon("/clouds.png", "Welcome Panel");
        setLayout(new BorderLayout());
        if (icon != null) iconLabel.setIcon(icon);
        iconLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        add(iconLabel, BorderLayout.WEST);
        JPanel secondaryPanel = new JPanel(new BorderLayout());
        secondaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        secondaryPanel.add(contentPanel, BorderLayout.NORTH);
        secondaryPanel.add(imagePanel, BorderLayout.SOUTH);
        add(secondaryPanel, BorderLayout.CENTER);
    }

    private JPanel getImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(ImageUtils.createImageIcon("/EPICS_Logo.gif", "EPICS Logo")), BorderLayout.WEST);
        panel.add(new JLabel(ImageUtils.createImageIcon("/Drexel_ECE_Logo.jpg", "Drexel Univ. ECE Dept. Logo")), BorderLayout.EAST);
        panel.add(new JLabel(ImageUtils.createImageIcon("/CleanAirCouncil_Logo.png", "Clean Air Council Logo")), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getContentPanel() {
        JPanel contentPanel1 = new JPanel();
        JPanel jPanel1 = new JPanel();
        welcomeTitle = new JLabel();
        Font descriptionFont = new Font(welcomeTitle.getFont().getFontName(), Font.PLAIN, 16);
        blankSpace = new JLabel();
        jLabel1 = new JLabel();
        jLabel1.setFont(descriptionFont);
        jLabel2 = new JLabel();
        jLabel2.setFont(descriptionFont);
        jLabel3 = new JLabel();
        jLabel3.setFont(descriptionFont);
        jLabel4 = new JLabel();
        jLabel4.setFont(descriptionFont);
        jLabel5 = new JLabel();
        jLabel5.setFont(descriptionFont);
        jLabel7 = new JLabel();
        jLabel7.setFont(descriptionFont);
        jLabel6 = new JLabel();
        jLabel6.setFont(descriptionFont);
        jLabel8 = new JLabel();
        jLabel8.setFont(descriptionFont);
        jLabel9 = new JLabel();
        jLabel9.setFont(descriptionFont);
        labelLink = new JLabel();
        labelLink.setFont(descriptionFont);
        initHyperlinkComponent();
        space = new JLabel();
        space.setFont(descriptionFont);
        jLabel10 = new JLabel();
        jLabel10.setFont(descriptionFont);
        contentPanel1.setLayout(new BorderLayout());
        welcomeTitle.setFont(new Font(welcomeTitle.getFont().getFontName(), Font.BOLD, 24));
        welcomeTitle.setText("Welcome to the Air Quality Sensor Network that you have deployed.");
        contentPanel1.add(welcomeTitle, BorderLayout.NORTH);
        jPanel1.setLayout(new GridLayout(0, 1));
        jPanel1.add(blankSpace);
        jLabel1.setText("This application will be used to connect to the air quality sensor network developed");
        jPanel1.add(jLabel1);
        jLabel2.setText("by Team ECE-01 of Drexel University's Senior Design Project 2009-2010.");
        jPanel1.add(jLabel2);
        jLabel3.setText("");
        jPanel1.add(jLabel3);
        jLabel4.setText("We will use the following screens to configure and setup the sensor network and ");
        jPanel1.add(jLabel4);
        jLabel5.setText("data collection.  The collected air quality measurements will be uploaded to the web");
        jPanel1.add(jLabel5);
        jLabel7.setText("to make the data more accessible. Please make sure you have an internet connection available.");
        jPanel1.add(jLabel7);
        jLabel6.setText("");
        jPanel1.add(jLabel6);
        jLabel10.setText("This system was designed for use in projects that raise the community�s awareness of air quality.");
        jPanel1.add(jLabel10);
        jPanel1.add(labelLink);
        space.setText("");
        jPanel1.add(space);
        jPanel1.add(jLabel8);
        jLabel9.setText("Press the 'Next' button to continue...");
        jPanel1.add(jLabel9);
        contentPanel1.add(jPanel1, BorderLayout.CENTER);
        return contentPanel1;
    }

    private void initHyperlinkComponent() {
        labelLink.setText("Click here to learn more about this system and access the data on the web.");
        labelLink.setForeground(Color.BLUE);
        labelLink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                Cursor linkCursor = new Cursor(Cursor.HAND_CURSOR);
                labelLink.setCursor(linkCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                labelLink.setCursor(normalCursor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                labelLink.setForeground(Color.RED);
                OSUtils.browseToURI(model.getProperties().getProperty(ConfigKeys.WEBSITE_URL));
                labelLink.setForeground(Color.BLUE);
            }
        });
    }
}
