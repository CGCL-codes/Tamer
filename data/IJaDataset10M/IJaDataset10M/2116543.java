package d20chat;

import java.io.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Allows a user to create a new profile.
 */
public class CreateProfile extends javax.swing.JFrame {

    private D20Chat tempMain;

    /**
     * Current instance of ProfileManager.
     * Used to fetch variables such as directories.
     */
    public ProfileManager profileManager;

    /**
     * CreateProfile constructor.
     * Constructor for creat profile class. Displays a window that allows the user
     * to enter information to be saved in a profile.
     */
    public CreateProfile() {
        initComponents();
        Dimension scrn = getToolkit().getScreenSize();
        this.setBounds((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2, getWidth(), getHeight());
    }

    private void initComponents() {
        lblUsername = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        phone = new javax.swing.JTextField();
        lblWebsite = new javax.swing.JLabel();
        website = new javax.swing.JTextField();
        lblAboutMe = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        aboutMe = new javax.swing.JTextArea();
        visible = new javax.swing.JCheckBox();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        setTitle("Create New Profile");
        setLocationByPlatform(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });
        lblUsername.setForeground(java.awt.Color.darkGray);
        lblUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUsername.setText("Username:");
        lblName.setForeground(java.awt.Color.darkGray);
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblName.setText("Name:");
        lblEmail.setForeground(java.awt.Color.darkGray);
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("Email:");
        lblPhone.setForeground(java.awt.Color.darkGray);
        lblPhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPhone.setText("Phone:");
        lblWebsite.setForeground(java.awt.Color.darkGray);
        lblWebsite.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblWebsite.setText("Website:");
        lblAboutMe.setForeground(java.awt.Color.darkGray);
        lblAboutMe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAboutMe.setText("About Me:");
        aboutMe.setColumns(20);
        aboutMe.setLineWrap(true);
        aboutMe.setRows(5);
        aboutMe.setWrapStyleWord(true);
        jScrollPane1.setViewportView(aboutMe);
        visible.setForeground(java.awt.Color.darkGray);
        visible.setText("Make my profile visible to others");
        visible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        visible.setMargin(new java.awt.Insets(0, 0, 0, 0));
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblAboutMe).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(lblWebsite).addComponent(lblPhone).addComponent(lblEmail).addComponent(lblUsername).addComponent(lblName)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(website, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE).addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE).addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE).addComponent(visible).addGroup(layout.createSequentialGroup().addComponent(save).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE).addComponent(cancel))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblUsername).addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(16, 16, 16).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblName)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblEmail)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblPhone).addComponent(phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(website, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblWebsite)).addGap(16, 16, 16).addComponent(lblAboutMe).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15).addComponent(visible).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(save).addComponent(cancel)).addContainerGap()));
        pack();
    }

    private void formKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) this.setVisible(false);
    }

    /********************************************************************************
  Method: saveActionPerformed
  Description: Saves the newly created profile to file and adds it to the 
               profileList file
  Parameters: evt
  Used by: CreateProfile.java
  Uses: ProfileManager.addProfile
********************************************************************************/
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {
        String username = this.username.getText();
        String name = this.name.getText();
        String email = this.email.getText();
        String phone = this.phone.getText();
        String website = this.website.getText();
        String aboutMe = this.aboutMe.getText();
        boolean visible = this.visible.isSelected();
        String saveLocation = profileManager.profileDir + username + ".txt";
        FileOutputStream out;
        PrintStream p;
        try {
            BufferedReader bin = new BufferedReader(new FileReader(profileManager.profilePath));
            boolean profileAdded = false;
            String line = bin.readLine();
            while (line != null) {
                if (line.compareTo(username) == 0) {
                    profileAdded = true;
                }
                line = bin.readLine();
            }
            bin.close();
            if (!profileAdded) {
                out = new FileOutputStream(saveLocation);
                p = new PrintStream(out);
                p.println(username);
                p.println(name);
                p.println(email);
                p.println(phone);
                p.println(website);
                p.println(aboutMe);
                p.println(visible);
                p.close();
                profileManager.addProfile(username);
                this.setVisible(false);
            } else {
                String message = "Profile " + username + " already exists!\n";
                message = message + "Either edit " + username;
                message = message + " or enter a different username.";
                JOptionPane.showMessageDialog(null, message, "Profile Manager", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /********************************************************************************
  Method: cancelActionPerformed
  Description: Closes the create profile window
  Parameters: evt
  Used by: CreateProfile.java
  Uses: NONE
********************************************************************************/
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private javax.swing.JTextArea aboutMe;

    private javax.swing.JButton cancel;

    private javax.swing.JTextField email;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblAboutMe;

    private javax.swing.JLabel lblEmail;

    private javax.swing.JLabel lblName;

    private javax.swing.JLabel lblPhone;

    private javax.swing.JLabel lblUsername;

    private javax.swing.JLabel lblWebsite;

    private javax.swing.JTextField name;

    private javax.swing.JTextField phone;

    private javax.swing.JButton save;

    private javax.swing.JTextField username;

    private javax.swing.JCheckBox visible;

    private javax.swing.JTextField website;
}
