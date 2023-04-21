package userinterface;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JFrame;
import ArenaSimulator.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;

/**
 * Main window of the Anagram Game application.
 */
public class MainWindow extends JFrame {

    private XML _xml = new XML();

    private Simulator _simulator = new Simulator();

    private Display _display = new Display(_simulator);

    private String _output = "";

    private boolean _running = false;

    /** Creates new form MainWindow */
    public MainWindow() {
        initComponents();
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.width) / 2));
        _display = new Display(_simulator);
        simulationPanel.add(_display);
        _xml.setOutput(_output);
        outputBox.setText("Welcome to Evolen.");
    }

    public void refresh() {
        if (_running) {
            _simulator.advanceSimulation();
        }
        repaint();
    }

    private void initComponents() {
        fileOpener = new javax.swing.JFileChooser();
        fileSaver = new javax.swing.JFileChooser();
        jSplitPane1 = new javax.swing.JSplitPane();
        splitPane = new javax.swing.JSplitPane();
        simulationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textarea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputBox = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenuBar = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        simulationMenuBar = new javax.swing.JMenu();
        startMenuItem = new javax.swing.JMenuItem();
        stopMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenuBar = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        fileOpener.setDialogTitle("Open File");
        fileOpener.setFileFilter(new userinterface.eemlFileFilter());
        fileSaver.setDialogTitle("Save File");
        fileSaver.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileSaver.setFileFilter(new userinterface.eemlFileFilter());
        setTitle("Evolen");
        setMinimumSize(new java.awt.Dimension(700, 700));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        simulationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulation"));
        simulationPanel.setAutoscrolls(true);
        simulationPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        simulationPanel.setPreferredSize(new java.awt.Dimension(100, 500));
        simulationPanel.setLayout(new javax.swing.BoxLayout(simulationPanel, javax.swing.BoxLayout.LINE_AXIS));
        splitPane.setRightComponent(simulationPanel);
        textarea.setColumns(20);
        textarea.setRows(5);
        textarea.setBorder(javax.swing.BorderFactory.createTitledBorder("EEML"));
        jScrollPane1.setViewportView(textarea);
        splitPane.setLeftComponent(jScrollPane1);
        jSplitPane1.setLeftComponent(splitPane);
        outputBox.setColumns(20);
        outputBox.setEditable(false);
        outputBox.setRows(5);
        outputBox.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));
        jScrollPane2.setViewportView(outputBox);
        jSplitPane1.setRightComponent(jScrollPane2);
        getContentPane().add(jSplitPane1);
        fileMenuBar.setMnemonic('F');
        fileMenuBar.setText("File");
        fileMenuBar.setFocusable(false);
        openMenuItem.setText("Open...");
        openMenuItem.setFocusable(true);
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenuBar.add(openMenuItem);
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenuBar.add(saveMenuItem);
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenuBar.add(saveAsMenuItem);
        fileMenuBar.add(jSeparator2);
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.setToolTipText("Exit the application.");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenuBar.add(exitMenuItem);
        jMenuBar1.add(fileMenuBar);
        simulationMenuBar.setText("Simulation");
        startMenuItem.setText("Start");
        startMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMenuItemActionPerformed(evt);
            }
        });
        simulationMenuBar.add(startMenuItem);
        stopMenuItem.setText("Stop");
        stopMenuItem.setEnabled(false);
        stopMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopMenuItemActionPerformed(evt);
            }
        });
        simulationMenuBar.add(stopMenuItem);
        simulationMenuBar.add(jSeparator1);
        optionsMenuItem.setText("Options...");
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        simulationMenuBar.add(optionsMenuItem);
        jMenuBar1.add(simulationMenuBar);
        helpMenuBar.setText("Help");
        aboutMenuItem.setText("About...");
        aboutMenuItem.setToolTipText("Learn about this application.");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenuBar.add(aboutMenuItem);
        jMenuBar1.add(helpMenuBar);
        setJMenuBar(jMenuBar1);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 515) / 2, (screenSize.height - 452) / 2, 515, 452);
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        new AboutWindow(this).setVisible(true);
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fileOpener.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileOpener.getSelectedFile();
            try {
                textarea.read(new FileReader(file.getAbsolutePath()), null);
            } catch (IOException ex) {
                outputBox.append("\n\nThere was a problem opening the file " + file.getAbsolutePath());
            }
        } else {
            outputBox.append("\n\nFile access was cancelled by the user.");
        }
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fileSaver.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileSaver.getSelectedFile();
            BufferedWriter out;
            String fileName = textarea.getText();
            if (fileName.matches(".eeml")) {
            } else if (fileName.matches("[.][a-zA-Z]*$")) {
                outputBox.append("\n\nPlease specify an .eeml file.");
            } else {
                fileName = fileName + ".eeml";
            }
            try {
                out = new BufferedWriter(new FileWriter(file));
                out.write(textarea.getText());
                out.close();
            } catch (IOException ex) {
                outputBox.append("There is a problem saving the file " + file.getAbsolutePath());
            }
        } else {
            outputBox.append("File access cancelled by user.");
        }
    }

    private void startMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (textarea.getText().isEmpty()) {
            _simulator = new Simulator();
        } else {
            _simulator = (Simulator) _xml.readString(textarea.getText());
        }
        if (_simulator != null) {
            _display.setSimulator(_simulator);
            _running = true;
            textarea.setEditable(false);
            startMenuItem.setEnabled(false);
            stopMenuItem.setEnabled(true);
            openMenuItem.setEnabled(false);
            saveMenuItem.setEnabled(false);
            saveAsMenuItem.setEnabled(false);
            optionsMenuItem.setEnabled(false);
        } else {
            outputBox.append("\n\n" + _xml.getOutput());
        }
    }

    private void stopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        _running = false;
        textarea.setText(_xml.writeString(_simulator));
        textarea.setEditable(true);
        startMenuItem.setEnabled(true);
        stopMenuItem.setEnabled(false);
        openMenuItem.setEnabled(true);
        saveMenuItem.setEnabled(true);
        saveAsMenuItem.setEnabled(true);
        optionsMenuItem.setEnabled(true);
    }

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JMenuItem aboutMenuItem;

    private javax.swing.JMenuItem exitMenuItem;

    private javax.swing.JMenu fileMenuBar;

    private javax.swing.JFileChooser fileOpener;

    private javax.swing.JFileChooser fileSaver;

    private javax.swing.JMenu helpMenuBar;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JPopupMenu.Separator jSeparator1;

    private javax.swing.JPopupMenu.Separator jSeparator2;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JMenuItem openMenuItem;

    private javax.swing.JMenuItem optionsMenuItem;

    private javax.swing.JTextArea outputBox;

    private javax.swing.JMenuItem saveAsMenuItem;

    private javax.swing.JMenuItem saveMenuItem;

    private javax.swing.JMenu simulationMenuBar;

    private javax.swing.JPanel simulationPanel;

    private javax.swing.JSplitPane splitPane;

    private javax.swing.JMenuItem startMenuItem;

    private javax.swing.JMenuItem stopMenuItem;

    private javax.swing.JTextArea textarea;
}
