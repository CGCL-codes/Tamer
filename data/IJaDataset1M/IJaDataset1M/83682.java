package d20chat;

import java.io.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.*;

/**
 * Allows a user to manager profiles.
 * (create, edit, delete, import, export, set a default, load default profile at 
 * program startup, and load a profile to be used in the current session.
 */
public class ProfileManager extends javax.swing.JFrame {

    /**
     * Used to access if profiles should be loaded up on default.
     */
    public D20Chat tempMain;

    private JFileChooser fc = new JFileChooser();

    private JList pList;

    /**
     * directory that profiles are stored in.
     */
    public String profileDir = "profiles/";

    /**
     * Path to get to the profiles.
     */
    public String profilePath = profileDir + "profileList.txt";

    private String defaultProfile = "";

    private String settingsFile = profileDir + "settings.txt";

    private boolean loadDefaultOnStartup = false;

    private JFrame frame = new JFrame();

    /**
     * Class constructor for ProfileManager.
     * Sets up the window, as well as loads information from the profile files.
     */
    public ProfileManager() {
        initComponents();
        Dimension scrn = getToolkit().getScreenSize();
        this.setBounds((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2, getWidth(), getHeight());
        FileFilter filter = new FileNameExtensionFilter("Text File (.txt)", "txt");
        fc.addChoosableFileFilter(filter);
        try {
            boolean profileDirExists = (new File("profiles")).exists();
            if (!profileDirExists) {
                boolean success = (new File("profiles")).mkdir();
                File file = new File(profilePath);
                success = file.createNewFile();
            } else {
                boolean profileFileExists = (new File(profilePath)).exists();
                if (!profileFileExists) {
                    File file = new File(profilePath);
                    boolean success = file.createNewFile();
                }
            }
        } catch (Exception e) {
            System.out.println("Could not create profile directory or file!");
        }
        try {
            BufferedReader bin = new BufferedReader(new FileReader(profilePath));
            String profileName = bin.readLine();
            DefaultListModel listModel = new DefaultListModel();
            while (profileName != null) {
                listModel.addElement(profileName);
                profileName = bin.readLine();
            }
            pList = new JList(listModel);
            pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.profileList.setViewportView(pList);
            bin.close();
        } catch (Exception e) {
            System.err.println("Error reading file " + profilePath);
        }
        this.loadDefault.setSelected(loadDefaultOnStartup);
    }

    private void initComponents() {
        lblExplanation = new javax.swing.JLabel();
        loadDefault = new javax.swing.JCheckBox();
        useProfile = new javax.swing.JButton();
        newProfile = new javax.swing.JButton();
        editProfile = new javax.swing.JButton();
        deleteProfile = new javax.swing.JButton();
        importProfile = new javax.swing.JButton();
        exportProfile = new javax.swing.JButton();
        profileList = new javax.swing.JScrollPane();
        setAsDefault = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        setTitle(" Profile Manager");
        setName("Profile Manager");
        setResizable(false);
        lblExplanation.setText("Select a profile:");
        loadDefault.setText("Load default on program startup");
        loadDefault.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        loadDefault.setMargin(new java.awt.Insets(0, 0, 0, 0));
        loadDefault.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loadDefaultItemStateChanged(evt);
            }
        });
        useProfile.setText("Use Profile");
        useProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useProfileActionPerformed(evt);
            }
        });
        newProfile.setText("New");
        newProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProfileActionPerformed(evt);
            }
        });
        editProfile.setText("Edit");
        editProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileActionPerformed(evt);
            }
        });
        deleteProfile.setText("Delete");
        deleteProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProfileActionPerformed(evt);
            }
        });
        importProfile.setText("Import");
        importProfile.setMaximumSize(new java.awt.Dimension(63, 23));
        importProfile.setMinimumSize(new java.awt.Dimension(63, 23));
        importProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importProfileActionPerformed(evt);
            }
        });
        exportProfile.setText("Export");
        exportProfile.setMaximumSize(new java.awt.Dimension(63, 23));
        exportProfile.setMinimumSize(new java.awt.Dimension(63, 23));
        exportProfile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportProfileActionPerformed(evt);
            }
        });
        setAsDefault.setText("Set as Default");
        setAsDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setAsDefaultActionPerformed(evt);
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
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(lblExplanation).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(profileList, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE).addComponent(setAsDefault, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE).addComponent(loadDefault)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(exportProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(importProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(editProfile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE).addComponent(newProfile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addComponent(deleteProfile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(useProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE).addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGap(10, 10, 10)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblExplanation).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(newProfile).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(editProfile).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(deleteProfile).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(importProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(profileList, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(setAsDefault).addComponent(exportProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(loadDefault).addGap(16, 16, 16).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancel).addComponent(useProfile)).addContainerGap()));
        pack();
    }

    /********************************************************************************
  Method: cancelActionPerformed
  Description: Cancel button action for the profile manager window
  Parameters: evt
  Used by: ProfileManager.java
  Uses: NONE
********************************************************************************/
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    /********************************************************************************
  Method: satAsDefaultActionPerformed
  Description: Allows the user to set the currently selected profile as default
  Parameters: evt
  Used by: ProfileManager.java
  Uses: saveProfileManagerSettings
********************************************************************************/
    private void setAsDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        if (!this.pList.isSelectionEmpty()) {
            defaultProfile = this.getSelectedProfile();
        } else {
            defaultProfile = "";
        }
        this.saveProfileManagerSettings();
    }

    /********************************************************************************
  Method: loadDefaultItemStateChanged
  Description: Saves the load default on program startup option when changed
  Parameters: evt
  Used by: ProfileManager.java
  Uses: saveProfileManagerSettings
********************************************************************************/
    private void loadDefaultItemStateChanged(java.awt.event.ItemEvent evt) {
        if (loadDefault.isSelected()) loadDefaultOnStartup = true; else loadDefaultOnStartup = false;
        this.saveProfileManagerSettings();
    }

    /**
     * Saves all the profile manager settings to file.
     * (default profile and whether to load it on startup)
     *  Used by: setAsDefaultActionPerformed, loadDefaultItemStateChanged,
     *           loadDefaultProfile
     */
    private void saveProfileManagerSettings() {
        try {
            FileOutputStream out;
            PrintStream p;
            out = new FileOutputStream(settingsFile);
            p = new PrintStream(out);
            p.println(defaultProfile);
            p.println(loadDefaultOnStartup);
            p.close();
        } catch (Exception e) {
            System.err.println("Unable to write to file:\n\t" + settingsFile + "!");
        }
    }

    /**
     * Loads in the default profile settings (profile and load on startup).
     * @return returns fals if there is no profile to load
     */
    public boolean loadDefaultProfile() {
        try {
            File settings = new File(settingsFile);
            if (settings.length() > 0) {
                BufferedReader bin = new BufferedReader(new FileReader(settingsFile));
                defaultProfile = bin.readLine();
                String strLoadDefault = bin.readLine();
                loadDefaultOnStartup = Boolean.parseBoolean(strLoadDefault);
                tempMain.loadDefaultProfile = loadDefaultOnStartup;
                bin.close();
            }
        } catch (Exception e) {
            System.err.println("Attempting to load default profile.");
        }
        if (loadDefaultOnStartup == false) return false; else {
            boolean profileFileExists = (new File(profilePath)).exists();
            if (profileFileExists) {
                String defaultProfilePath = profileDir + defaultProfile + ".txt";
                String readUsername = "";
                String readName = "";
                String readEmail = "";
                String readPhone = "";
                String readWebsite = "";
                String readAboutMe = "";
                String readVisible = "";
                try {
                    BufferedReader bin = new BufferedReader(new FileReader(defaultProfilePath));
                    readUsername = bin.readLine();
                    readName = bin.readLine();
                    readEmail = bin.readLine();
                    readPhone = bin.readLine();
                    readWebsite = bin.readLine();
                    readAboutMe = bin.readLine();
                    readVisible = bin.readLine();
                    boolean visible = false;
                    visible = Boolean.parseBoolean(readVisible);
                    tempMain.theUser.userProfile.setProfile(readName, readEmail, readAboutMe, readWebsite, readPhone, visible);
                    tempMain.theUser.setUsername(readUsername);
                    bin.close();
                } catch (Exception e) {
                    System.err.println("Could not read profile: " + readUsername);
                }
                return true;
            } else {
                defaultProfile = "";
                loadDefaultOnStartup = false;
                this.saveProfileManagerSettings();
                return false;
            }
        }
    }

    /**
     * Startup method to perform startup tasks for the profile manager.
     */
    private void startup() {
        if (!loadDefaultOnStartup) this.setVisible(true);
    }

    /********************************************************************************
  Method: importProfileActionPerformed
  Description: Allows a user to import a profile into D20 Chat
  Parameters: evt
  Used by: ProfileManager.java
  Uses: NONE
********************************************************************************/
    private void importProfileActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fc.showOpenDialog(this);
        String readUsername = "";
        String readName = "";
        String readEmail = "";
        String readPhone = "";
        String readWebsite = "";
        String readAboutMe = "";
        String readVisible = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File openedFile = fc.getSelectedFile();
            String openLocation = openedFile.getPath();
            try {
                BufferedReader bin = new BufferedReader(new FileReader(openLocation));
                readUsername = bin.readLine();
                readName = bin.readLine();
                readEmail = bin.readLine();
                readPhone = bin.readLine();
                readWebsite = bin.readLine();
                readAboutMe = bin.readLine();
                readVisible = bin.readLine();
                bin.close();
            } catch (Exception e) {
                System.err.println("Error reading file");
            }
            try {
                BufferedReader bin = new BufferedReader(new FileReader(profilePath));
                boolean profileAdded = false;
                String line = bin.readLine();
                while (line != null) {
                    if (line.compareTo(readUsername) == 0) profileAdded = true;
                    line = bin.readLine();
                }
                if (profileAdded) {
                    String message = "The profile " + readUsername;
                    message = message + " is already being used.\n";
                    message = message + "Do you wish to overwrite it?";
                    int overwrite = JOptionPane.showOptionDialog(frame, message, "Import Profile", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                    if (overwrite == JOptionPane.YES_OPTION) {
                        FileOutputStream out;
                        PrintStream p;
                        String saveLocation = profileDir + readUsername + ".txt";
                        out = new FileOutputStream(saveLocation);
                        p = new PrintStream(out);
                        p.println(readUsername);
                        p.println(readName);
                        p.println(readEmail);
                        p.println(readPhone);
                        p.println(readWebsite);
                        p.println(readAboutMe);
                        p.println(readVisible);
                        p.close();
                        bin.close();
                    }
                } else {
                    FileOutputStream out;
                    PrintStream p;
                    out = new FileOutputStream(profilePath, true);
                    p = new PrintStream(out);
                    p.println(readUsername);
                    p.close();
                    String saveLocation = profileDir + readUsername + ".txt";
                    out = new FileOutputStream(saveLocation);
                    p = new PrintStream(out);
                    p.println(readUsername);
                    p.println(readName);
                    p.println(readEmail);
                    p.println(readPhone);
                    p.println(readWebsite);
                    p.println(readAboutMe);
                    p.println(readVisible);
                    p.close();
                    DefaultListModel listModel = new DefaultListModel();
                    for (int i = 0; i < pList.getModel().getSize(); i++) listModel.addElement(pList.getModel().getElementAt(i).toString());
                    listModel.addElement(readUsername);
                    pList = new JList(listModel);
                    pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    this.profileList.setViewportView(pList);
                    bin.close();
                }
            } catch (Exception e) {
                System.err.println("Unable to write profile to file!");
            }
        }
    }

    /********************************************************************************
  Method: deleteProfileActionPerformed
  Description: Allows a user to delete a profile from the profile manager
  Parameters: evt
  Used by: ProfileManager.java
  Uses: removeProfile
********************************************************************************/
    private void deleteProfileActionPerformed(java.awt.event.ActionEvent evt) {
        if (pList.isSelectionEmpty()) JOptionPane.showMessageDialog(frame, "Please select a profile to delete.", "Profile Manager", JOptionPane.WARNING_MESSAGE); else {
            String profile = this.getSelectedProfile();
            String message = "Are you sure you want to delete profile ";
            message = message + profile + "?";
            int result = JOptionPane.showOptionDialog(frame, message, "Delete Profile", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == JOptionPane.YES_OPTION) this.removeProfile(profile);
        }
    }

    /********************************************************************************
  Method: editProfileActionPerformed
  Description: Allows a user to edit a profile already loaded into D20 Chat
               (whether created on the local machine or imported in)
  Parameters: evt
  Used by: ProfileManager.java
  Uses: EditProfile.java
********************************************************************************/
    private void editProfileActionPerformed(java.awt.event.ActionEvent evt) {
        if (pList.isSelectionEmpty()) JOptionPane.showMessageDialog(frame, "Please select a profile to edit.", "Profile Manager", JOptionPane.WARNING_MESSAGE); else {
            EditProfile editProfile;
            editProfile = new EditProfile();
            editProfile.profileManager = this;
            editProfile.setVisible(true);
        }
    }

    /********************************************************************************
  Method: useProfileActionPerformed
  Description: Loads a selected profile in the profile manager to be used in the
               current session
  Parameters: evt
  Used by: ProfileManager.java
  Uses: indicateUsedProfile
********************************************************************************/
    private void useProfileActionPerformed(java.awt.event.ActionEvent evt) {
        String info;
        if (pList.isSelectionEmpty()) JOptionPane.showMessageDialog(frame, "Please select a profile to use.", "Profile Manager", JOptionPane.WARNING_MESSAGE); else {
            String selectedProfile = this.getSelectedProfile();
            this.indicateUsedProfile();
            String selectedProfilePath = profileDir + selectedProfile + ".txt";
            String readUsername = "";
            String readName = "";
            String readEmail = "";
            String readPhone = "";
            String readWebsite = "";
            String readAboutMe = "";
            String readVisible = "";
            try {
                BufferedReader bin = new BufferedReader(new FileReader(selectedProfilePath));
                readUsername = bin.readLine();
                readName = bin.readLine();
                readEmail = bin.readLine();
                readPhone = bin.readLine();
                readWebsite = bin.readLine();
                readAboutMe = bin.readLine();
                readVisible = bin.readLine();
                Boolean visible = false;
                visible = Boolean.parseBoolean(readVisible);
                tempMain.theUser.userProfile.setProfile(readName, readEmail, readAboutMe, readWebsite, readPhone, visible);
                tempMain.theUser.setUsername(selectedProfile);
                if (tempMain.connected == true) {
                    if (tempMain.theUser.userProfile.getVisible() == true) {
                        info = "/userInfo " + tempMain.theUser.toString();
                        tempMain.OutParse.sendMessage(info);
                    } else {
                        info = "/changeName " + tempMain.theUser.getUsername();
                        tempMain.OutParse.sendMessage(info);
                        info = "/hideProfile ";
                        tempMain.OutParse.sendMessage(info);
                    }
                }
                bin.close();
            } catch (Exception e) {
                System.err.println("Error reading file: " + selectedProfilePath);
            }
            this.setVisible(false);
        }
    }

    /********************************************************************************
  Method: exportProfileActionPerformed
  Description: Allows a user to export the selected profile to file
  Parameters: evt
  Used by: ProfileManager.java
  Uses: NONE
********************************************************************************/
    private void exportProfileActionPerformed(java.awt.event.ActionEvent evt) {
        if (pList.isSelectionEmpty()) JOptionPane.showMessageDialog(frame, "Please select a profile to export.", "Profile Manager", JOptionPane.WARNING_MESSAGE); else {
            int selectedIndex = pList.getSelectedIndex();
            Object selectedProfile = pList.getModel().getElementAt(selectedIndex);
            String username = selectedProfile.toString();
            String filePath = profileDir + username + ".txt";
            String readUsername = "";
            String readName = "";
            String readEmail = "";
            String readPhone = "";
            String readWebsite = "";
            String readAboutMe = "";
            String readVisible = "";
            try {
                BufferedReader bin = new BufferedReader(new FileReader(filePath));
                readUsername = bin.readLine();
                readName = bin.readLine();
                readEmail = bin.readLine();
                readPhone = bin.readLine();
                readWebsite = bin.readLine();
                readAboutMe = bin.readLine();
                readVisible = bin.readLine();
                bin.close();
            } catch (Exception e) {
                System.err.println("Error reading file");
            }
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File savedFile = fc.getSelectedFile();
                String saveLocation = savedFile.getPath();
                if (!saveLocation.endsWith(".txt")) saveLocation = saveLocation + ".txt";
                FileOutputStream out;
                PrintStream p;
                try {
                    out = new FileOutputStream(saveLocation);
                    p = new PrintStream(out);
                    p.println(readUsername);
                    p.println(readName);
                    p.println(readEmail);
                    p.println(readPhone);
                    p.println(readWebsite);
                    p.println(readAboutMe);
                    p.println(readVisible);
                    p.close();
                } catch (Exception e) {
                    System.err.println("Unable to write to file:\n\t" + saveLocation + "!");
                }
            }
        }
    }

    /********************************************************************************
  Method: newProfileActionPerformed
  Description: Allows a user to creat a new profile
  Parameters: evt
  Used by: ProfileManager.java
  Uses: CreatProfile.java
********************************************************************************/
    private void newProfileActionPerformed(java.awt.event.ActionEvent evt) {
        CreateProfile createProfile;
        createProfile = new CreateProfile();
        createProfile.profileManager = this;
        createProfile.setVisible(true);
    }

    /********************************************************************************
  Method: doesProfileExist
  Description: Checks to see if a profile already exists (already loaded into
               D20 Chat - created or imported on the local machine)
  Parameters: profile
  Used by: NONE
  Uses: NONE
********************************************************************************/
    private boolean doesProfileExist(String profile) {
        boolean profileAdded = false;
        try {
            BufferedReader bin = new BufferedReader(new FileReader(profilePath));
            String line = bin.readLine();
            while (line != null) {
                if (line.compareTo(profile) == 0) profileAdded = true;
                line = bin.readLine();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return profileAdded;
    }

    /**
     * Adds a profile to the jList in the profile manager form.
     * @param profileName Name of the profile to be added.
     */
    public void addProfile(String profileName) {
        try {
            FileOutputStream out;
            PrintStream p;
            BufferedReader bin = new BufferedReader(new FileReader(profilePath));
            boolean profileAdded = false;
            String line = bin.readLine();
            while (line != null) {
                if (line.compareTo(profileName) == 0) profileAdded = true;
                line = bin.readLine();
            }
            if (!profileAdded) {
                out = new FileOutputStream(profilePath, true);
                p = new PrintStream(out);
                p.println(profileName);
                p.close();
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < pList.getModel().getSize(); i++) listModel.addElement(pList.getModel().getElementAt(i).toString());
                listModel.addElement(profileName);
                pList = new JList(listModel);
                pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                this.profileList.setViewportView(pList);
            }
            bin.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Returns the selected profile in the profile manager jList.
     * @return username of the selected profile.
     */
    public String getSelectedProfile() {
        int selectedIndex = pList.getSelectedIndex();
        Object selectedProfile = pList.getModel().getElementAt(selectedIndex);
        String username = selectedProfile.toString();
        return username;
    }

    /**
     * Indicates that the selected profile is being used (for user's sake).
     *  Used by: useProfileActionPerformed
     */
    private void indicateUsedProfile() {
        int selectedIndex = pList.getSelectedIndex();
        Object selectedProfile = pList.getModel().getElementAt(selectedIndex);
        String userName = selectedProfile.toString();
        String profileName = userName;
        userName = userName + "   [LOADED]";
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < pList.getModel().getSize(); i++) listModel.addElement(pList.getModel().getElementAt(i).toString());
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).toString().compareTo(profileName) == 0) listModel.remove(i);
        }
        listModel.addElement(userName);
        pList = new JList(listModel);
        pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.profileList.setViewportView(pList);
    }

    /**
     * Removes a profile from the profile manager jList and profileList file.
     * @param profileName name of the profile to be removed.
     */
    public void removeProfile(String profileName) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < pList.getModel().getSize(); i++) listModel.addElement(pList.getModel().getElementAt(i).toString());
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).toString().compareTo(profileName) == 0) listModel.remove(i);
        }
        String oldFile = profileDir + profileName + ".txt";
        boolean success = (new File(oldFile)).delete();
        if (!success) System.out.println("File " + oldFile + " was not deleted!"); else System.out.println("File " + oldFile + " was deleted!");
        pList = new JList(listModel);
        pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.profileList.setViewportView(pList);
        try {
            FileOutputStream out;
            PrintStream p;
            out = new FileOutputStream(profilePath);
            p = new PrintStream(out);
            for (int i = 0; i < pList.getModel().getSize(); i++) {
                p.println(pList.getModel().getElementAt(i).toString());
            }
            p.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private javax.swing.JButton cancel;

    private javax.swing.JButton deleteProfile;

    private javax.swing.JButton editProfile;

    private javax.swing.JButton exportProfile;

    private javax.swing.JButton importProfile;

    private javax.swing.JLabel lblExplanation;

    private javax.swing.JCheckBox loadDefault;

    private javax.swing.JButton newProfile;

    private javax.swing.JScrollPane profileList;

    private javax.swing.JButton setAsDefault;

    private javax.swing.JButton useProfile;
}
