package au.jSummit.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.border.*;
import au.jSummit.Core.*;

public class ProfilePanel extends JPanel implements ActionListener, Serializable, KeyListener {

    private JPanel paProfileSelect;

    private JPanel paProfileDetails;

    private JComboBox cbProfile;

    private JCheckBox cbAudio;

    private JCheckBox cbVideo;

    private JCheckBox cbWhiteboard;

    private String sCurrentPanel;

    private JButton buEdit;

    private JTextField txUserName = new JTextField(200);

    private JTextField txRealName = new JTextField(200);

    private JTextField txCompany = new JTextField(200);

    private JTextField txEmail = new JTextField(200);

    private JTextField txLocation = new JTextField(200);

    private JTextField txConnection = new JTextField(200);

    private JTextField txImageName = new JTextField(180);

    private JPanel pImageText = new JPanel();

    private JButton buImage;

    private Vector profiles;

    private Person currentProfile;

    private ProfileFrame profileFrame;

    public ProfilePanel(ProfileFrame pf) {
        Color bkgndcolor = Globals.BGCOLOR;
        Color buttoncolor = Globals.BUTCOLOR;
        Font fLabel = Globals.LABELFONT;
        Color fontcolor = Globals.TEXTCOLOR;
        profileFrame = pf;
        setBackground(bkgndcolor);
        setLayout(new GridBagLayout());
        paProfileSelect = new JPanel();
        paProfileDetails = new JPanel();
        paProfileSelect.setBackground(bkgndcolor);
        paProfileDetails.setBackground(bkgndcolor);
        Border etched = BorderFactory.createEtchedBorder();
        Border titledSelect = BorderFactory.createTitledBorder(etched, "Profile Selection");
        Border titledDetails = BorderFactory.createTitledBorder(etched, "Profile Description");
        Border titledOptions = BorderFactory.createTitledBorder(etched, "Options");
        paProfileSelect.setBorder(titledSelect);
        paProfileDetails.setBorder(titledDetails);
        paProfileDetails.setLayout(new GridBagLayout());
        JLabel laUserName = new JLabel("User Name: ", JLabel.RIGHT);
        laUserName.setForeground(fontcolor);
        JLabel laRealName = new JLabel("Real Name: ", JLabel.RIGHT);
        laRealName.setForeground(fontcolor);
        JLabel laCompany = new JLabel("Company: ", JLabel.RIGHT);
        laCompany.setForeground(fontcolor);
        JLabel laEmail = new JLabel("E-Mail: ", JLabel.RIGHT);
        laEmail.setForeground(fontcolor);
        JLabel laLocation = new JLabel("Location: ", JLabel.RIGHT);
        laLocation.setForeground(fontcolor);
        JLabel laConnection = new JLabel("Connection: ", JLabel.RIGHT);
        laConnection.setForeground(fontcolor);
        laConnection.setVisible(false);
        JLabel laImageName = new JLabel("Profile Image: ", JLabel.RIGHT);
        laImageName.setForeground(fontcolor);
        txUserName = new JTextField("", 200);
        txUserName.setEditable(false);
        txUserName.addKeyListener(this);
        txRealName = new JTextField("", 200);
        txRealName.setEditable(false);
        txRealName.addKeyListener(this);
        txCompany = new JTextField("", 200);
        txCompany.setEditable(false);
        txCompany.addKeyListener(this);
        txEmail = new JTextField("", 200);
        txEmail.setEditable(false);
        txEmail.addKeyListener(this);
        txLocation = new JTextField("", 200);
        txLocation.setEditable(false);
        txLocation.addKeyListener(this);
        txConnection = new JTextField("", 200);
        txConnection.setEditable(false);
        txConnection.setVisible(false);
        txImageName = new JTextField("", 160);
        txImageName.setEditable(false);
        Image anImage = getToolkit().createImage("au/jSummit/images/button.png");
        ImageIcon anIconSummit = new ImageIcon(anImage);
        buImage = new JButton(anIconSummit);
        buImage.setBorder(new EmptyBorder(0, 0, 0, 0));
        buImage.addActionListener(this);
        buImage.setEnabled(false);
        pImageText.setLayout(new BorderLayout());
        pImageText.add(buImage, BorderLayout.EAST);
        pImageText.add(txImageName, BorderLayout.CENTER);
        buEdit = new JButton("Edit");
        buEdit.addActionListener(this);
        buEdit.setBackground(buttoncolor);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addc(laUserName, constraints, 0, 1, 5, 1, paProfileDetails);
        addc(laRealName, constraints, 0, 2, 1, 1, paProfileDetails);
        addc(laCompany, constraints, 0, 3, 1, 1, paProfileDetails);
        addc(laEmail, constraints, 0, 4, 1, 1, paProfileDetails);
        addc(laLocation, constraints, 0, 5, 1, 1, paProfileDetails);
        addc(laImageName, constraints, 0, 6, 1, 1, paProfileDetails);
        constraints.weightx = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addc(txUserName, constraints, 1, 1, 5, 1, paProfileDetails);
        addc(txRealName, constraints, 1, 2, 5, 1, paProfileDetails);
        addc(txCompany, constraints, 1, 3, 5, 1, paProfileDetails);
        addc(txEmail, constraints, 1, 4, 5, 1, paProfileDetails);
        addc(txLocation, constraints, 1, 5, 5, 1, paProfileDetails);
        addc(pImageText, constraints, 1, 6, 5, 1, paProfileDetails);
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.NONE;
        addc(buEdit, constraints, 7, 6, 1, 1, paProfileDetails);
        JPanel paSelectOptions = new JPanel();
        paSelectOptions.setBackground(bkgndcolor);
        paSelectOptions.setBorder(titledOptions);
        paSelectOptions.setLayout(new GridBagLayout());
        constraints.weightx = 1;
        constraints.weighty = 1;
        cbAudio = new JCheckBox("Audio");
        cbAudio.setBackground(bkgndcolor);
        cbVideo = new JCheckBox("Video");
        cbVideo.setBackground(bkgndcolor);
        cbWhiteboard = new JCheckBox("Whiteboard");
        cbWhiteboard.setBackground(bkgndcolor);
        constraints.fill = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        paSelectOptions.add(cbAudio, constraints);
        constraints.gridx = 2;
        paSelectOptions.add(cbVideo, constraints);
        constraints.gridx = 3;
        paSelectOptions.add(cbWhiteboard, constraints);
        JLabel laCurrent = new JLabel("Current: ", JLabel.LEFT);
        laCurrent.setForeground(fontcolor);
        profiles = new Vector();
        getUserProfiles();
        cbProfile = new JComboBox();
        for (int i = 0; i < profiles.size(); i++) {
            if (i == 0) currentProfile = (Person) profiles.elementAt(i);
            Person tmp = (Person) profiles.elementAt(i);
            cbProfile.addItem(tmp.getUsername());
        }
        cbProfile.setEditable(false);
        cbProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Person pCurrentProfile;
                for (int i = 0; i < profiles.size(); i++) {
                    pCurrentProfile = (Person) (profiles.elementAt(i));
                    String sProfileName = pCurrentProfile.getUsername();
                    String sComboBox = (String) cbProfile.getSelectedItem();
                    if (sProfileName.equals(sComboBox)) {
                        txUserName.setText(pCurrentProfile.getUsername());
                        txRealName.setText(pCurrentProfile.getRealname());
                        txCompany.setText(pCurrentProfile.getCompany());
                        txEmail.setText(pCurrentProfile.getEmail());
                        txLocation.setText(pCurrentProfile.getLocation());
                        txConnection.setText(pCurrentProfile.getConnSpeed());
                        txImageName.setText(pCurrentProfile.getImageFilename());
                        currentProfile = pCurrentProfile;
                        txUserName.setEditable(false);
                        txRealName.setEditable(false);
                        txCompany.setEditable(false);
                        txEmail.setEditable(false);
                        txLocation.setEditable(false);
                    }
                    if (currentProfile.getUsername().length() > 9 && currentProfile.getUsername().substring(0, 9).equals("<default>")) profileFrame.setbuNextFalse(); else profileFrame.setbuNextTrue();
                }
                buEdit.setText("Edit");
            }
        });
        if (profiles.size() > 0) {
            cbProfile.setSelectedIndex(0);
        } else profileFrame.setbuNextFalse();
        JButton buNewProfile = new JButton("New");
        buNewProfile.setBackground(buttoncolor);
        buNewProfile.addActionListener(this);
        paProfileSelect.setLayout(new GridBagLayout());
        constraints.gridx = 1;
        paProfileSelect.add(laCurrent, constraints);
        constraints.gridx = 2;
        paProfileSelect.add(cbProfile, constraints);
        constraints.gridx = 3;
        paProfileSelect.add(buNewProfile, constraints);
        JPanel paBlank = new JPanel();
        paBlank.setBackground(bkgndcolor);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(paProfileSelect, constraints);
        constraints.gridy = 2;
        add(paProfileDetails, constraints);
        constraints.gridy = 5;
        add(paBlank, constraints);
    }

    public void addc(Component c, GridBagConstraints constraints, int x, int y, int w, int h, JPanel panel) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        panel.add(c, constraints);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton bu = (JButton) e.getSource();
            String arg = bu.getText();
            if (arg.equals("New")) {
                txUserName.setEditable(true);
                txRealName.setEditable(true);
                txCompany.setEditable(true);
                txEmail.setEditable(true);
                txLocation.setEditable(true);
                txUserName.setText("");
                txRealName.setText("");
                txCompany.setText("");
                txEmail.setText("");
                txLocation.setText("");
                txImageName.setText("");
                buEdit.setText("Save");
            } else if (arg.equals("Edit")) {
                txUserName.setEditable(true);
                txRealName.setEditable(true);
                txCompany.setEditable(true);
                txEmail.setEditable(true);
                txLocation.setEditable(true);
                buEdit.setText("Save");
            } else if (arg.equals("Save")) {
                txUserName.setEditable(false);
                txRealName.setEditable(false);
                txCompany.setEditable(false);
                txEmail.setEditable(false);
                txLocation.setEditable(false);
                txConnection.setEditable(false);
                Person pCurrentProfile;
                boolean boFound = false;
                for (int i = 0; i < profiles.size(); i++) {
                    pCurrentProfile = (Person) (profiles.elementAt(i));
                    String sProfileName = pCurrentProfile.getUsername();
                    String sComboBox = (String) cbProfile.getSelectedItem();
                    if (sProfileName.equals(txUserName.getText())) {
                        pCurrentProfile.setUsername(txUserName.getText());
                        pCurrentProfile.setRealname(txRealName.getText());
                        pCurrentProfile.setCompany(txCompany.getText());
                        pCurrentProfile.setEmail(txEmail.getText());
                        pCurrentProfile.setLocation(txLocation.getText());
                        pCurrentProfile.setConnSpeed(txConnection.getText());
                        pCurrentProfile.setImageFilename("au/jSummit/images/profiles" + txImageName.getText());
                        writeUserProfiles();
                        boFound = true;
                        currentProfile = pCurrentProfile;
                    }
                }
                if (!boFound) {
                    Person newProfile = new Person();
                    newProfile.setUsername(txUserName.getText());
                    newProfile.setRealname(txRealName.getText());
                    newProfile.setCompany(txCompany.getText());
                    newProfile.setEmail(txEmail.getText());
                    newProfile.setLocation(txLocation.getText());
                    newProfile.setConnSpeed(txConnection.getText());
                    profiles.add(newProfile);
                    cbProfile.addItem(newProfile.getUsername());
                    cbProfile.setSelectedIndex(cbProfile.getItemCount() - 1);
                    writeUserProfiles();
                    currentProfile = newProfile;
                }
                buEdit.setText("Edit");
            }
        }
    }

    public void getUserProfiles() {
        try {
            FileInputStream fisProfile = new FileInputStream("profile.dat");
            ObjectInputStream ois = new ObjectInputStream(fisProfile);
            try {
                for (; ; ) profiles.add((Person) ois.readObject());
            } catch (IOException e) {
            }
        } catch (Exception e) {
            profileFrame.setbuNextFalse();
            System.err.println("Exception: " + e.toString());
        }
    }

    public void writeUserProfiles() {
        try {
            FileOutputStream fosProfile = new FileOutputStream("profile.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fosProfile);
            for (int i = 0; i < profiles.size(); i++) {
                Person oCurrentProfile = (Person) profiles.elementAt(i);
                oos.writeObject(oCurrentProfile);
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.toString());
        }
    }

    public Person getCurrentProfile() {
        return currentProfile;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (e.getSource() instanceof JTextField) {
            if ((JTextField) e.getSource() == txUserName) {
                String message = txUserName.getText();
                if (message.length() > 24) txUserName.setText(message.substring(0, 24));
            } else if ((JTextField) e.getSource() == txRealName) {
                String message = txRealName.getText();
                if (message.length() > 24) txRealName.setText(message.substring(0, 24));
            } else if ((JTextField) e.getSource() == txCompany) {
                String message = txCompany.getText();
                if (message.length() > 24) txCompany.setText(message.substring(0, 24));
            } else if ((JTextField) e.getSource() == txEmail) {
                String message = txEmail.getText();
                if (message.length() > 36) txEmail.setText(message.substring(0, 36));
            } else if ((JTextField) e.getSource() == txLocation) {
                String message = txLocation.getText();
                if (message.length() > 24) txLocation.setText(message.substring(0, 24));
            }
        }
    }
}
