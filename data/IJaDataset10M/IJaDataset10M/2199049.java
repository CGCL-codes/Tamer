package com.menegatos.mdaog.gui;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author  Athanasios Menegatos
 */
public class DaoGenerator extends javax.swing.JFrame {

    private ErrorDialog errorDialog;

    private String currentFilename;

    private Properties props;

    private FileFilter propertiesFilter = new PropertiesFileFilter("properties", "Properties Files");

    private JFileChooser browseFileChooser;

    private EditorKit htmlEditorKit = new HTMLEditorKit();

    private Properties config;

    private ArrayList recentFiles;

    private static JFrame frame;

    /** Creates new form NewGui */
    public DaoGenerator() {
        try {
            Image image = ImageIO.read(getClass().getResourceAsStream("/com/menegatos/mdaog/gui/mdaog2.gif"));
            this.setIconImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        loadConfig();
        browseFileChooser = new JFileChooser();
        DocumentListener requiredInputListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent evt) {
                updateGenerateAvailability();
            }

            public void removeUpdate(DocumentEvent evt) {
                updateGenerateAvailability();
            }

            public void insertUpdate(DocumentEvent evt) {
                updateGenerateAvailability();
            }
        };
        jdbcUrlField.getDocument().addDocumentListener(requiredInputListener);
        jdbcDriverField.getDocument().addDocumentListener(requiredInputListener);
        packageField.getDocument().addDocumentListener(requiredInputListener);
        jdbcDataSourceNameField.getDocument().addDocumentListener(requiredInputListener);
        sourceDirectoryField.getDocument().addDocumentListener(requiredInputListener);
    }

    private void loadConfig() {
        InputStream configIn = null;
        String configFile = System.getProperty("user.home") + File.separatorChar + "menegatosDao.conf";
        recentFiles = new ArrayList(5);
        fileMenu.add(new JSeparator());
        try {
            configIn = new FileInputStream(configFile);
            config = new Properties();
            config.load(configIn);
            int count = 1;
            for (int i = 1; i < 6; i++) {
                String rf = config.getProperty("recentFile" + i);
                if (rf != null) {
                    File f = new File(rf);
                    if (!recentFiles.contains(f)) {
                        recentFiles.add(f);
                        JMenuItem menuItem = new JMenuItem();
                        menuItem.setText(count++ + ". " + f.getName());
                        menuItem.setMnemonic(menuItem.getText().charAt(0));
                        menuItem.setFont(openMenuItem.getFont());
                        menuItem.addActionListener(new java.awt.event.ActionListener() {

                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                String sidx = ((JMenuItem) evt.getSource()).getText();
                                sidx = sidx.substring(0, sidx.indexOf("."));
                                int idx = 0;
                                try {
                                    idx = Integer.parseInt(sidx) - 1;
                                } catch (Exception e) {
                                }
                                File f = (File) recentFiles.get(idx);
                                if (f != null) {
                                    try {
                                        loadFile(f.getCanonicalPath());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        fileMenu.add(menuItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (configIn != null) try {
                configIn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFile(String fileToOpen) {
        InputStream propsIn = null;
        currentFilename = fileToOpen;
        try {
            propsIn = new FileInputStream(fileToOpen);
            props = new Properties();
            props.load(propsIn);
            jdbcDriverField.setText(props.getProperty("JdbcDriver"));
            jdbcUrlField.setText(props.getProperty("JdbcUrl"));
            dbPasswordField.setText(props.getProperty("dbPassword"));
            dbUsernameField.setText(props.getProperty("dbUsername"));
            jdbcDataSourceNameField.setText(props.getProperty("jdbcDataSourceName"));
            packageField.setText(props.getProperty("package"));
            sourceDirectoryField.setText(props.getProperty("sourceDirectory"));
            addRecentFile(fileToOpen);
            statusBarText.setText(fileToOpen);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                propsIn.close();
            } catch (Exception e) {
            }
        }
    }

    public void addRecentFile(String filename) {
        File f = new File(filename);
        boolean exists = false;
        if (recentFiles == null) recentFiles = new ArrayList(5);
        for (int i = 0; i < recentFiles.size(); i++) {
            File ef = (File) recentFiles.get(i);
            try {
                if (ef.getCanonicalPath().equalsIgnoreCase(f.getCanonicalPath())) {
                    exists = true;
                    recentFiles.remove(i);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        recentFiles.add(0, f);
        int count = fileMenu.getItemCount();
        ArrayList itemsToRemove = new ArrayList();
        for (int i = 0; i < count; i++) {
            JMenuItem item = (JMenuItem) fileMenu.getItem(i);
            if (item != null && item instanceof JMenuItem && item.getText().indexOf(".") == 1) {
                itemsToRemove.add(item);
            }
        }
        for (int i = 0; i < itemsToRemove.size(); i++) {
            fileMenu.remove((JMenuItem) itemsToRemove.get(i));
        }
        for (int i = 0; i < recentFiles.size(); i++) {
            f = (File) recentFiles.get(i);
            JMenuItem menuItem = new JMenuItem();
            menuItem.setText((i + 1) + ". " + f.getName());
            menuItem.setMnemonic(menuItem.getText().charAt(0));
            menuItem.setFont(openMenuItem.getFont());
            menuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String sidx = ((JMenuItem) evt.getSource()).getText();
                    sidx = sidx.substring(0, sidx.indexOf("."));
                    int idx = 0;
                    try {
                        idx = Integer.parseInt(sidx) - 1;
                    } catch (Exception e) {
                    }
                    File f = (File) recentFiles.get(idx);
                    if (f != null) {
                        try {
                            loadFile(f.getCanonicalPath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            fileMenu.add(menuItem);
        }
    }

    public void saveConfig() {
        if (recentFiles != null) {
            if (config == null) config = new Properties();
            for (int i = 1; i <= recentFiles.size(); i++) {
                try {
                    config.setProperty("recentFile" + i, ((File) recentFiles.get(i - 1)).getCanonicalPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            OutputStream os = null;
            try {
                os = new FileOutputStream(System.getProperty("user.home") + File.separatorChar + "menegatosDao.conf");
                config.store(os, "Configuration file for Menegatos PostgreSQL Dao Generator");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initComponents() {
        fileChooser = new javax.swing.JFileChooser();
        aboutDialog = new javax.swing.JDialog();
        aboutButtonPane = new javax.swing.JPanel();
        aboutOkButton = new javax.swing.JButton();
        aboutPane = new javax.swing.JPanel();
        aboutTextPane = new javax.swing.JTextPane();
        titlePane = new javax.swing.JPanel();
        iconLabel = new javax.swing.JLabel();
        titleTextPane = new javax.swing.JPanel();
        titleHead = new javax.swing.JTextPane();
        titleHead2 = new javax.swing.JTextPane();
        tableChooserDialog = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tablePane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableList = new javax.swing.JList();
        viewPane = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        viewsList = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        tableChooserButtonsPane = new javax.swing.JPanel();
        tableChooserOkButton = new javax.swing.JButton();
        tableChooserCancelButton = new javax.swing.JButton();
        statusDialog = new javax.swing.JDialog();
        statusTitle = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        statusMessage = new javax.swing.JTextPane();
        statusProgressBar = new javax.swing.JProgressBar();
        splitPane = new javax.swing.JSplitPane();
        settingsPane = new javax.swing.JPanel();
        labelsPane = new javax.swing.JPanel();
        jdbcDriverLabe = new javax.swing.JLabel();
        jdbcUrlLabel = new javax.swing.JLabel();
        dbUsernameLabel = new javax.swing.JLabel();
        dbPasswordLabel = new javax.swing.JLabel();
        jdbcDataSourceNameLabel = new javax.swing.JLabel();
        dbPackageLabel = new javax.swing.JLabel();
        dbSourceDirectoryLabel = new javax.swing.JLabel();
        inputPane = new javax.swing.JPanel();
        jdbcDriverField = new javax.swing.JTextField();
        jdbcUrlField = new javax.swing.JTextField();
        dbUsernameField = new javax.swing.JTextField();
        dbPasswordField = new javax.swing.JPasswordField();
        jdbcDataSourceNameField = new javax.swing.JTextField();
        packageField = new javax.swing.JTextField();
        fileInputPane = new javax.swing.JPanel();
        sourceDirectoryField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        messageScrollPane = new javax.swing.JScrollPane();
        messageText = new javax.swing.JTextArea();
        statusBarPane = new javax.swing.JPanel();
        statusBarText = new javax.swing.JTextPane();
        statusBarProgressPane = new javax.swing.JPanel();
        statusBarProgressBar = new javax.swing.JProgressBar();
        toolBar = new javax.swing.JToolBar();
        fileToolBar = new javax.swing.JToolBar();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        actionsToolBar = new javax.swing.JToolBar();
        generateButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        helpToolBar = new javax.swing.JToolBar();
        helpButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        actionsMenu = new javax.swing.JMenu();
        generateMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();
        fileChooser.setFileFilter(propertiesFilter);
        fileChooser.setFont(new java.awt.Font("SansSerif", 0, 12));
        fileChooser.setAutoscrolls(true);
        fileChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });
        aboutDialog.setTitle("About MDAOG");
        aboutDialog.setModal(true);
        aboutDialog.setResizable(false);
        aboutOkButton.setText("OK");
        aboutOkButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutOkButtonActionPerformed(evt);
            }
        });
        aboutButtonPane.add(aboutOkButton);
        aboutDialog.getContentPane().add(aboutButtonPane, java.awt.BorderLayout.SOUTH);
        aboutPane.setLayout(new java.awt.BorderLayout());
        aboutPane.setBorder(new javax.swing.border.EtchedBorder());
        aboutTextPane.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        aboutTextPane.setBorder(new javax.swing.border.EtchedBorder());
        aboutTextPane.setEditable(false);
        aboutTextPane.setText("\n     MDAOG generates Java source files for use in \n    connecting to a JDBC database, using the Data \n    Access Object design pattern. The code generated \n    is intended for use in J2EE web applications that \n    need to persist data in a database.\n\n    MDAOG is distributed under the Apache License v2\n\n    For More Information visit http://mdaog.sourceforge.net\n");
        aboutPane.add(aboutTextPane, java.awt.BorderLayout.CENTER);
        aboutDialog.getContentPane().add(aboutPane, java.awt.BorderLayout.CENTER);
        titlePane.setLayout(new java.awt.BorderLayout(10, 0));
        iconLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/mdaog.gif")));
        titlePane.add(iconLabel, java.awt.BorderLayout.WEST);
        titleTextPane.setLayout(new java.awt.GridLayout(2, 0));
        titleHead.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        titleHead.setEditable(false);
        titleHead.setFont(new java.awt.Font("Dialog", 1, 36));
        titleHead.setText("MDAOG");
        titleTextPane.add(titleHead);
        titleHead2.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        titleHead2.setEditable(false);
        titleHead2.setFont(new java.awt.Font("Dialog", 1, 14));
        titleHead2.setText("Java DAO Code Generator\nFor PostgreSQL Databases");
        titleTextPane.add(titleHead2);
        titlePane.add(titleTextPane, java.awt.BorderLayout.CENTER);
        aboutDialog.getContentPane().add(titlePane, java.awt.BorderLayout.NORTH);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/genSide.png")));
        jLabel5.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        tableChooserDialog.getContentPane().add(jLabel5, java.awt.BorderLayout.WEST);
        jPanel2.setLayout(new java.awt.BorderLayout());
        tablePane.setLayout(new java.awt.BorderLayout());
        jLabel1.setText("Select Which Tables To Generate:");
        tablePane.add(jLabel1, java.awt.BorderLayout.NORTH);
        tableList.setFont(new java.awt.Font("Dialog", 0, 12));
        jScrollPane1.setViewportView(tableList);
        tablePane.add(jScrollPane1, java.awt.BorderLayout.SOUTH);
        jPanel2.add(tablePane, java.awt.BorderLayout.NORTH);
        viewPane.setLayout(new java.awt.BorderLayout());
        viewsList.setFont(new java.awt.Font("Dialog", 0, 12));
        jScrollPane2.setViewportView(viewsList);
        viewPane.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        jLabel2.setText("Select which VIEWS to Generate:");
        viewPane.add(jLabel2, java.awt.BorderLayout.NORTH);
        jPanel2.add(viewPane, java.awt.BorderLayout.CENTER);
        tableChooserDialog.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
        tableChooserOkButton.setText("Generate");
        tableChooserOkButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableChooserOkButtonActionPerformed(evt);
            }
        });
        tableChooserButtonsPane.add(tableChooserOkButton);
        tableChooserCancelButton.setText("Cancel");
        tableChooserCancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableChooserCancelButtonActionPerformed(evt);
            }
        });
        tableChooserButtonsPane.add(tableChooserCancelButton);
        tableChooserDialog.getContentPane().add(tableChooserButtonsPane, java.awt.BorderLayout.SOUTH);
        statusDialog.setTitle("Status");
        statusTitle.setEditable(false);
        statusTitle.setOpaque(false);
        statusDialog.getContentPane().add(statusTitle, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        statusMessage.setEditable(false);
        statusMessage.setOpaque(false);
        jPanel1.add(statusMessage);
        statusProgressBar.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.add(statusProgressBar);
        statusDialog.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MDAOG - Data Access Object Generator");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        splitPane.setDividerLocation(135);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setFocusable(false);
        splitPane.setOneTouchExpandable(true);
        settingsPane.setLayout(new java.awt.BorderLayout(12, 12));
        settingsPane.setFocusable(false);
        settingsPane.setMaximumSize(new java.awt.Dimension(2147483647, 125));
        settingsPane.setMinimumSize(new java.awt.Dimension(460, 135));
        settingsPane.setPreferredSize(new java.awt.Dimension(460, 135));
        labelsPane.setLayout(new java.awt.GridLayout(7, 1));
        labelsPane.setFocusable(false);
        labelsPane.setMinimumSize(new java.awt.Dimension(125, 96));
        labelsPane.setPreferredSize(new java.awt.Dimension(125, 96));
        jdbcDriverLabe.setFont(new java.awt.Font("Dialog", 0, 12));
        jdbcDriverLabe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jdbcDriverLabe.setText("JDBC Driver");
        jdbcDriverLabe.setFocusable(false);
        labelsPane.add(jdbcDriverLabe);
        jdbcUrlLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        jdbcUrlLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jdbcUrlLabel.setText("JDBC URL");
        labelsPane.add(jdbcUrlLabel);
        dbUsernameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        dbUsernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dbUsernameLabel.setText("DB Username");
        labelsPane.add(dbUsernameLabel);
        dbPasswordLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        dbPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dbPasswordLabel.setText("DB Password");
        labelsPane.add(dbPasswordLabel);
        jdbcDataSourceNameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        jdbcDataSourceNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jdbcDataSourceNameLabel.setText("DataSource Name");
        labelsPane.add(jdbcDataSourceNameLabel);
        dbPackageLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        dbPackageLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dbPackageLabel.setText("Package");
        labelsPane.add(dbPackageLabel);
        dbSourceDirectoryLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        dbSourceDirectoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dbSourceDirectoryLabel.setText("Source Directory");
        labelsPane.add(dbSourceDirectoryLabel);
        settingsPane.add(labelsPane, java.awt.BorderLayout.WEST);
        inputPane.setLayout(new java.awt.GridLayout(7, 0, 5, 0));
        jdbcDriverField.setToolTipText("The JDBC Driver Class (ex: org.postgresql.Driver)");
        inputPane.add(jdbcDriverField);
        jdbcUrlField.setToolTipText("The JDBC URL to access the database (ex: jdbc:postgresql://hostname/databasename");
        inputPane.add(jdbcUrlField);
        dbUsernameField.setToolTipText("The username for the database");
        inputPane.add(dbUsernameField);
        dbPasswordField.setToolTipText("The password for the database (if any)");
        inputPane.add(dbPasswordField);
        jdbcDataSourceNameField.setToolTipText("The username for the database");
        inputPane.add(jdbcDataSourceNameField);
        packageField.setToolTipText("The datasourceName  ex: jdbc/myAppDSN ");
        inputPane.add(packageField);
        fileInputPane.setLayout(new java.awt.BorderLayout());
        sourceDirectoryField.setToolTipText("The directory where you want the generated files to go");
        fileInputPane.add(sourceDirectoryField, java.awt.BorderLayout.CENTER);
        browseButton.setText("Browse...");
        browseButton.setMaximumSize(new java.awt.Dimension(500, 230));
        browseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        fileInputPane.add(browseButton, java.awt.BorderLayout.EAST);
        inputPane.add(fileInputPane);
        settingsPane.add(inputPane, java.awt.BorderLayout.CENTER);
        splitPane.setLeftComponent(settingsPane);
        messageScrollPane.setMinimumSize(new java.awt.Dimension(400, 300));
        messageScrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
        messageText.setEditable(false);
        messageText.setAutoscrolls(false);
        messageText.setFocusable(false);
        messageScrollPane.setViewportView(messageText);
        splitPane.setBottomComponent(messageScrollPane);
        getContentPane().add(splitPane, java.awt.BorderLayout.CENTER);
        statusBarPane.setLayout(new javax.swing.BoxLayout(statusBarPane, javax.swing.BoxLayout.X_AXIS));
        statusBarText.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusBarText.setEditable(false);
        statusBarText.setFont(new java.awt.Font("Dialog", 1, 10));
        statusBarText.setFocusable(false);
        statusBarText.setMinimumSize(new java.awt.Dimension(300, 14));
        statusBarText.setOpaque(false);
        statusBarText.setPreferredSize(new java.awt.Dimension(300, 14));
        statusBarPane.add(statusBarText);
        statusBarProgressPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        statusBarProgressPane.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusBarProgressBar.setFont(new java.awt.Font("Dialog", 0, 10));
        statusBarProgressBar.setFocusable(false);
        statusBarProgressBar.setMinimumSize(new java.awt.Dimension(80, 14));
        statusBarProgressPane.add(statusBarProgressBar);
        statusBarPane.add(statusBarProgressPane);
        getContentPane().add(statusBarPane, java.awt.BorderLayout.SOUTH);
        toolBar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setMargin(new java.awt.Insets(4, 4, 4, 4));
        fileToolBar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        fileToolBar.setBorder(null);
        fileToolBar.setFloatable(false);
        fileToolBar.setRollover(true);
        fileToolBar.setMargin(new java.awt.Insets(0, 12, 0, 12));
        openButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/fileopen.png")));
        openButton.setBorderPainted(false);
        openButton.setOpaque(false);
        openButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rfileopen.png")));
        openButton.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rfileopen.png")));
        openButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rfileopen.png")));
        openButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });
        fileToolBar.add(openButton);
        saveButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/filesave.png")));
        saveButton.setBorderPainted(false);
        saveButton.setEnabled(false);
        saveButton.setOpaque(false);
        saveButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rfilesave.png")));
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        fileToolBar.add(saveButton);
        saveAsButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        saveAsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/filesaveas.png")));
        saveAsButton.setBorderPainted(false);
        saveAsButton.setEnabled(false);
        saveAsButton.setOpaque(false);
        saveAsButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rfilesaveas.png")));
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });
        fileToolBar.add(saveAsButton);
        jLabel3.setEnabled(false);
        jLabel3.setFocusable(false);
        jLabel3.setMaximumSize(new java.awt.Dimension(25, 0));
        jLabel3.setMinimumSize(new java.awt.Dimension(25, 0));
        jLabel3.setPreferredSize(new java.awt.Dimension(25, 0));
        fileToolBar.add(jLabel3);
        toolBar.add(fileToolBar);
        actionsToolBar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        actionsToolBar.setBorder(null);
        actionsToolBar.setFloatable(false);
        actionsToolBar.setRollover(true);
        generateButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        generateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/edit.png")));
        generateButton.setBorderPainted(false);
        generateButton.setEnabled(false);
        generateButton.setOpaque(false);
        generateButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/redit.png")));
        generateButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });
        actionsToolBar.add(generateButton);
        jLabel4.setMaximumSize(new java.awt.Dimension(5000, 0));
        jLabel4.setMinimumSize(new java.awt.Dimension(20, 0));
        actionsToolBar.add(jLabel4);
        toolBar.add(actionsToolBar);
        helpToolBar.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        helpToolBar.setBorder(null);
        helpToolBar.setFloatable(false);
        helpToolBar.setRollover(true);
        helpButton.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.shadow"));
        helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/help.png")));
        helpButton.setBorderPainted(false);
        helpButton.setOpaque(false);
        helpButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/iconstb/rhelp.png")));
        helpButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });
        helpToolBar.add(helpButton);
        toolBar.add(helpToolBar);
        getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);
        fileMenu.setMnemonic('f');
        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        fileMenu.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });
        openMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        openMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/fileopen.png")));
        openMenuItem.setMnemonic('o');
        openMenuItem.setLabel("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);
        saveMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        saveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/filesave.png")));
        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);
        saveAsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        saveAsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/filesaveas.png")));
        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(jSeparator2);
        exitMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/exit.png")));
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        actionsMenu.setMnemonic('a');
        actionsMenu.setText("Actions");
        actionsMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        generateMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        generateMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/gen.png")));
        generateMenuItem.setMnemonic('g');
        generateMenuItem.setText("Generate Dao Sources");
        generateMenuItem.setEnabled(false);
        generateMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateMenuItemActionPerformed(evt);
            }
        });
        actionsMenu.add(generateMenuItem);
        menuBar.add(actionsMenu);
        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");
        helpMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        helpMenu.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        helpMenu.setIconTextGap(1);
        contentsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        contentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/help.png")));
        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        contentsMenuItem.setEnabled(false);
        helpMenu.add(contentsMenuItem);
        helpMenu.add(jSeparator1);
        aboutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menegatos/mdaog/gui/m.png")));
        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setLabel("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        pack();
    }

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        generateMenuItemActionPerformed(evt);
    }

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        saveAsMenuItemActionPerformed(evt);
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        saveMenuItemActionPerformed(evt);
    }

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
    }

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
        openMenuItemActionPerformed(evt);
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void tableChooserOkButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tableChooserDialog.setVisible(false);
        statusTitle.setText("Generating Java Sources");
        statusMessage.setText("Preparing to Generate Java Sources");
        statusMessage.setMinimumSize(new Dimension(300, 50));
        statusMessage.setPreferredSize(statusMessage.getMinimumSize());
        Point p = this.getLocation();
        int x = p.x + this.getWidth() / 2 - statusDialog.getWidth() / 2;
        int y = p.y + this.getHeight() / 2 - statusDialog.getHeight() / 2;
        statusDialog.setLocation(x, y);
        statusDialog.pack();
        statusDialog.setVisible(true);
        com.menegatos.mdaog.dao.DAOGenerator daog = new com.menegatos.mdaog.dao.DAOGenerator();
        daog.setJdbcDriver(jdbcDriverField.getText());
        daog.setJdbcUrl(jdbcUrlField.getText());
        daog.setDbPassword(dbPasswordField.getText());
        daog.setDbUser(dbUsernameField.getText());
        daog.setDsnName(jdbcDataSourceNameField.getText());
        daog.setPackageName(packageField.getText());
        daog.setBasePath(sourceDirectoryField.getText());
        ArrayList tablesToGenerate = new ArrayList();
        Object[] tables = tableList.getSelectedValues();
        Object[] views = viewsList.getSelectedValues();
        if (tables != null) {
            for (int i = 0; i < tables.length; i++) {
                tablesToGenerate.add((String) tables[i]);
            }
        }
        if (views != null) {
            for (int i = 0; i < views.length; i++) {
                tablesToGenerate.add((String) views[i]);
            }
        }
        String newline = "\n";
        messageText.append("Starting DAO Generation..." + newline);
        messageText.append("JDBC Driver: " + jdbcDriverField.getText() + newline);
        messageText.append("JDBC URL: " + jdbcUrlField.getText() + newline);
        messageText.append("DB Username: " + dbUsernameField.getText() + newline);
        messageText.append("DB Password: " + dbPasswordField.getText() + newline);
        messageText.append("Package: " + packageField.getText() + newline);
        messageText.append("Source Directory: " + sourceDirectoryField.getText() + newline);
        messageText.append(newline);
        try {
            PipedInputStream piOut = new PipedInputStream();
            PipedOutputStream poOut = new PipedOutputStream(piOut);
            System.setOut(new PrintStream(poOut, true));
            PipedInputStream piErr = new PipedInputStream();
            PipedOutputStream poErr = new PipedOutputStream(piErr);
            System.setErr(new PrintStream(poErr, true));
            new ReaderThread(piOut).start();
            new ReaderThread(piErr).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        daog.setTablesToGenerate((String[]) tablesToGenerate.toArray(new String[tablesToGenerate.size()]));
        int numTables = tablesToGenerate.size();
        ProgressThread pt = new ProgressThread(daog, numTables);
        pt.start();
    }

    private void tableChooserCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        tableChooserDialog.setVisible(false);
    }

    private void aboutOkButtonActionPerformed(java.awt.event.ActionEvent evt) {
        aboutDialog.setVisible(false);
    }

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser saveAsFileChooser = fileChooser;
        saveAsFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = saveAsFileChooser.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            currentFilename = saveAsFileChooser.getSelectedFile().getAbsolutePath();
            statusBarText.setText(currentFilename);
            if (!currentFilename.endsWith(".properties")) currentFilename += ".properties";
            setProperties();
            OutputStream os = null;
            try {
                os = new FileOutputStream(currentFilename);
                props.store(os, "Saved Configuration for Menegatos Postgresql JDBC Generator.  For More Info visit http://www.menegatos.com");
                addRecentFile(currentFilename);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        aboutDialog.pack();
        Point p = this.getLocation();
        int adX = p.x + this.getWidth() / 2 - aboutDialog.getWidth() / 2;
        int adY = p.y + this.getHeight() / 2 - aboutDialog.getHeight() / 2;
        aboutDialog.setLocation(adX, adY);
        aboutDialog.setVisible(true);
    }

    private void generateMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        LookupThread luThread = new LookupThread(this);
        luThread.start();
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser openFileChooser = fileChooser;
        PropertiesFileFilter propertiesFilter = new PropertiesFileFilter("properties", "Properties Files");
        openFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = openFileChooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            String fileToOpen = openFileChooser.getSelectedFile().getAbsolutePath();
            loadFile(fileToOpen);
        }
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser directoryChooser = browseFileChooser;
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (sourceDirectoryField.getText() != null && sourceDirectoryField.getText().length() > 0) {
            try {
                directoryChooser.setCurrentDirectory(new File(sourceDirectoryField.getText()));
            } catch (Exception e) {
            }
        }
        int retVal = directoryChooser.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            sourceDirectoryField.setText(directoryChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (props != null && currentFilename != null) {
            setProperties();
            OutputStream os = null;
            try {
                os = new FileOutputStream(currentFilename);
                props.store(os, "Saved Configuration for Menegatos Postgresql JDBC Generator.  For More Info visit http://www.menegatos.com");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            saveAsMenuItemActionPerformed(evt);
        }
    }

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void updateGenerateAvailability() {
        if (jdbcDriverField.getText() == null || jdbcDriverField.getText().length() == 0 || jdbcUrlField.getText() == null || jdbcUrlField.getText().length() == 0 || packageField.getText() == null || packageField.getText().length() == 0 || jdbcDataSourceNameField.getText() == null || jdbcDataSourceNameField.getText().length() == 0 || sourceDirectoryField.getText() == null || sourceDirectoryField.getText().length() == 0) {
            generateMenuItem.setEnabled(false);
            generateButton.setEnabled(false);
            saveButton.setEnabled(false);
            saveAsButton.setEnabled(false);
        } else {
            generateMenuItem.setEnabled(true);
            generateButton.setEnabled(true);
            saveButton.setEnabled(true);
            saveAsButton.setEnabled(true);
        }
    }

    private void setProperties() {
        if (props == null) props = new Properties();
        props.setProperty("JdbcDriver", jdbcDriverField.getText());
        props.setProperty("JdbcUrl", jdbcUrlField.getText());
        props.setProperty("dbPassword", dbPasswordField.getText());
        props.setProperty("dbUsername", dbUsernameField.getText());
        props.setProperty("jdbcDataSourceName", jdbcDataSourceNameField.getText());
        props.setProperty("package", packageField.getText());
        props.setProperty("sourceDirectory", sourceDirectoryField.getText());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String antialising = "swing.aatext";
        if (null == System.getProperty(antialising)) System.setProperty(antialising, "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame = new DaoGenerator();
                frame.setVisible(true);
            }
        });
    }

    public void dispose() {
        saveConfig();
        super.dispose();
        System.exit(0);
    }

    private javax.swing.JPanel aboutButtonPane;

    private javax.swing.JDialog aboutDialog;

    private javax.swing.JMenuItem aboutMenuItem;

    private javax.swing.JButton aboutOkButton;

    private javax.swing.JPanel aboutPane;

    private javax.swing.JTextPane aboutTextPane;

    private javax.swing.JMenu actionsMenu;

    private javax.swing.JToolBar actionsToolBar;

    private javax.swing.JButton browseButton;

    private javax.swing.JMenuItem contentsMenuItem;

    private javax.swing.JLabel dbPackageLabel;

    private javax.swing.JPasswordField dbPasswordField;

    private javax.swing.JLabel dbPasswordLabel;

    private javax.swing.JLabel dbSourceDirectoryLabel;

    private javax.swing.JTextField dbUsernameField;

    private javax.swing.JLabel dbUsernameLabel;

    private javax.swing.JMenuItem exitMenuItem;

    private javax.swing.JFileChooser fileChooser;

    private javax.swing.JPanel fileInputPane;

    private javax.swing.JMenu fileMenu;

    private javax.swing.JToolBar fileToolBar;

    private javax.swing.JButton generateButton;

    private javax.swing.JMenuItem generateMenuItem;

    private javax.swing.JButton helpButton;

    private javax.swing.JMenu helpMenu;

    private javax.swing.JToolBar helpToolBar;

    private javax.swing.JLabel iconLabel;

    private javax.swing.JPanel inputPane;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JTextField jdbcDataSourceNameField;

    private javax.swing.JLabel jdbcDataSourceNameLabel;

    private javax.swing.JTextField jdbcDriverField;

    private javax.swing.JLabel jdbcDriverLabe;

    private javax.swing.JTextField jdbcUrlField;

    private javax.swing.JLabel jdbcUrlLabel;

    private javax.swing.JPanel labelsPane;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JScrollPane messageScrollPane;

    private javax.swing.JTextArea messageText;

    private javax.swing.JButton openButton;

    private javax.swing.JMenuItem openMenuItem;

    private javax.swing.JTextField packageField;

    private javax.swing.JButton saveAsButton;

    private javax.swing.JMenuItem saveAsMenuItem;

    private javax.swing.JButton saveButton;

    private javax.swing.JMenuItem saveMenuItem;

    private javax.swing.JPanel settingsPane;

    private javax.swing.JTextField sourceDirectoryField;

    private javax.swing.JSplitPane splitPane;

    private javax.swing.JPanel statusBarPane;

    private javax.swing.JProgressBar statusBarProgressBar;

    private javax.swing.JPanel statusBarProgressPane;

    private javax.swing.JTextPane statusBarText;

    private javax.swing.JDialog statusDialog;

    private javax.swing.JTextPane statusMessage;

    private javax.swing.JProgressBar statusProgressBar;

    private javax.swing.JTextPane statusTitle;

    private javax.swing.JPanel tableChooserButtonsPane;

    private javax.swing.JButton tableChooserCancelButton;

    private javax.swing.JDialog tableChooserDialog;

    private javax.swing.JButton tableChooserOkButton;

    private javax.swing.JList tableList;

    private javax.swing.JPanel tablePane;

    private javax.swing.JTextPane titleHead;

    private javax.swing.JTextPane titleHead2;

    private javax.swing.JPanel titlePane;

    private javax.swing.JPanel titleTextPane;

    private javax.swing.JToolBar toolBar;

    private javax.swing.JPanel viewPane;

    private javax.swing.JList viewsList;

    class TableListItem extends JPanel {

        private JCheckBox checkBox;

        private String tableName;

        TableListItem(String tableName) {
            super();
            BoxLayout bl = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(bl);
            setOpaque(false);
            checkBox = new JCheckBox();
            checkBox.setOpaque(false);
            add(checkBox);
            this.tableName = tableName;
            add(new JLabel(tableName));
            setAlignmentX(LEFT_ALIGNMENT);
            setMinimumSize(new Dimension(150, 18));
            setPreferredSize(new Dimension(150, 18));
            pack();
        }

        public JCheckBox getCheckBox() {
            return checkBox;
        }

        public String getTableName() {
            return tableName;
        }
    }

    final class LookupThread extends Thread {

        private boolean error = false;

        private boolean finished = false;

        private Frame parent;

        public LookupThread(Frame parent) {
            this.parent = parent;
        }

        public boolean hasError() {
            return error;
        }

        public boolean isFinished() {
            return finished;
        }

        public void run() {
            StatusBarProgressThread spt = new StatusBarProgressThread("Connecting...");
            spt.start();
            try {
                sleep(100);
            } catch (Exception e) {
            }
            com.menegatos.mdaog.dao.DAOGenerator daog = new com.menegatos.mdaog.dao.DAOGenerator();
            daog.setJdbcDriver(jdbcDriverField.getText());
            daog.setJdbcUrl(jdbcUrlField.getText());
            daog.setDbPassword(dbPasswordField.getText());
            daog.setDbUser(dbUsernameField.getText());
            daog.setPackageName(packageField.getText());
            daog.setBasePath(sourceDirectoryField.getText());
            tableList.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            tableList.setBackground(Color.white);
            tableList.setOpaque(true);
            tableList.setMinimumSize(new Dimension(200, 150));
            DefaultListModel tablesModel = new DefaultListModel();
            DefaultListModel viewsModel = new DefaultListModel();
            String[] tableNames = null;
            String[] viewNames = null;
            boolean error = false;
            try {
                tableNames = daog.getTables();
                if (tableNames != null) {
                    for (int i = 0; i < tableNames.length; i++) {
                        tablesModel.addElement(tableNames[i]);
                    }
                }
                tableList.setModel(tablesModel);
                viewNames = daog.getViews();
                if (viewNames != null) {
                    for (int i = 0; i < viewNames.length; i++) {
                        viewsModel.addElement(viewNames[i]);
                    }
                }
                viewsList.setModel(viewsModel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error retrieving tables: " + e.getMessage(), "Error retreiving tables", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
            if (!error) {
                tableChooserDialog.pack();
                Point p = parent.getLocation();
                int tcX = p.x + parent.getWidth() / 2 - tableChooserDialog.getWidth() / 2;
                int tcY = p.y + parent.getHeight() / 2 - tableChooserDialog.getHeight() / 2;
                tableChooserDialog.setLocation(tcX, tcY);
                statusBarProgressBar.setIndeterminate(false);
                statusBarProgressBar.setMaximum(100);
                statusBarProgressBar.setValue(100);
                tableChooserDialog.setVisible(true);
            }
            spt.finish();
        }
    }

    final class StatusBarProgressThread extends Thread {

        String message;

        boolean finished;

        public StatusBarProgressThread(String message) {
            this.message = message;
            statusBarProgressBar.setString(message);
            statusBarProgressBar.setIndeterminate(true);
            statusBarProgressBar.setStringPainted(true);
            finished = false;
        }

        public void run() {
            while (!finished) {
                try {
                    sleep(100);
                } catch (Exception e) {
                }
            }
        }

        public void finish() {
            try {
                sleep(100);
            } catch (Exception e) {
            }
            statusBarProgressBar.setIndeterminate(false);
            statusBarProgressBar.setString("");
            statusBarProgressBar.setMinimum(0);
            statusBarProgressBar.setMaximum(100);
            statusBarProgressBar.setValue(0);
            finished = true;
        }
    }

    final class ProgressThread extends Thread {

        com.menegatos.mdaog.dao.DAOGenerator daog;

        int numTables;

        public ProgressThread(com.menegatos.mdaog.dao.DAOGenerator daog, int numTables) {
            this.daog = daog;
            this.numTables = numTables;
        }

        public void run() {
            statusProgressBar.setMinimum(0);
            statusProgressBar.setMaximum(numTables);
            statusProgressBar.setValue(0);
            statusProgressBar.setStringPainted(true);
            daog.start();
            while (daog.isAlive()) {
                try {
                    statusMessage.setText(daog.getCurOp());
                    sleep(100);
                    statusProgressBar.setValue(daog.getCurIdx());
                } catch (Exception e) {
                }
            }
            statusDialog.setVisible(false);
        }
    }

    class ReaderThread extends Thread {

        PipedInputStream pi;

        ReaderThread(PipedInputStream pi) {
            this.pi = pi;
        }

        public void run() {
            final byte[] buf = new byte[1024];
            try {
                while (true) {
                    final int len = pi.read(buf);
                    if (len == -1) {
                        break;
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            messageText.append(new String(buf, 0, len));
                            messageText.setCaretPosition(messageText.getDocument().getLength());
                        }
                    });
                }
            } catch (IOException e) {
            }
        }
    }
}

final class ErrorDialog extends javax.swing.JDialog {

    private String errorMessage;

    private JTextPane textPane;

    public ErrorDialog(Frame owner) {
        super(owner);
        setTitle("Error");
        setModal(true);
        getContentPane().setLayout(new BorderLayout(20, 20));
        JLabel title = new JLabel();
        Font font = title.getFont();
        font = font.deriveFont(Font.BOLD, 18.0f);
        title.setFont(font);
        title.setText("Error");
        title.setHorizontalAlignment(title.CENTER);
        title.setMinimumSize(new Dimension(200, 20));
        title.setPreferredSize(title.getMinimumSize());
        getContentPane().add(new JLabel("Error"), BorderLayout.NORTH);
        textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setContentType("text/plain");
        textPane.setMaximumSize(new Dimension(220, 300));
        textPane.setPreferredSize(new Dimension(200, 100));
        getContentPane().add(textPane, BorderLayout.CENTER);
        JPanel buttonsPane = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonsPane.add(okButton);
        getContentPane().add(buttonsPane, BorderLayout.SOUTH);
        pack();
    }

    public void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        textPane.setText(errorMessage);
        textPane.setMaximumSize(new Dimension(220, 300));
        textPane.setPreferredSize(new Dimension(200, 100));
        this.pack();
    }

    public void setVisible(boolean b) {
        if (b) {
            Point p = getParent().getLocation();
            int x = p.x + getParent().getWidth() / 2 - this.getWidth() / 2;
            int y = p.y + getParent().getHeight() / 2 - this.getHeight() / 2;
            this.setLocation(x, y);
        }
        super.setVisible(b);
    }
}
