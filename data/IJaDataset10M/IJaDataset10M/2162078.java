package com.vish.gui;

import java.io.*;
import java.awt.*;
import com.vish.base.*;

/**
 * Insert the type's description here.
 * Creation date: (3/11/2002 1:08:38 PM)
 * @author: suresh
 */
public class VISHFrame extends java.awt.Frame {

    private java.awt.Panel ivjContentsPane = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private java.awt.ScrollPane ivjScrollPane1 = null;

    private java.awt.Button ivjExitButton = null;

    private java.awt.Button ivjGoButton = null;

    private java.awt.ScrollPane ivjScrollPane2 = null;

    private java.awt.List ivjDirectoryList = null;

    private java.awt.List ivjFileList = null;

    private java.awt.Label ivjDirectoryLabel = null;

    private java.awt.Label ivjFilterLabel = null;

    private java.awt.Label ivjMachineLabel = null;

    private java.awt.Panel ivjControlPanel = null;

    private java.awt.TextField ivjDirectoryTextField = null;

    private java.awt.TextField ivjFilterTextField = null;

    private java.awt.TextField ivjMachineTextField = null;

    private java.awt.List ivjCommandList = null;

    private VISHRemoteMachine remoteMachine = null;

    private java.awt.MenuBar ivjVISHFrameMenuBar = null;

    private java.awt.Menu ivjMenu = null;

    private java.awt.Label ivjTitleLabel1 = null;

    private java.awt.Label ivjTitleLabel2 = null;

    private java.awt.Label ivjTitleLabel3 = null;

    private java.awt.Label ivjTitleLabel4 = null;

    private java.awt.Label ivjDirectoryDescription = null;

    private java.awt.PopupMenu commandMenu = new java.awt.PopupMenu("VISH Commands");

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == VISHFrame.this.getExitButton()) exitButtonClicked();
            if (e.getSource() == VISHFrame.this.getGoButton()) goButtonClicked();
        }

        ;

        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getSource() == VISHFrame.this.getDirectoryList()) directoryListMouseClickedConnection(e);
            if (e.getSource() == VISHFrame.this.getFileList()) fileListMouseClickedConnection(e);
        }

        ;

        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        ;

        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        ;

        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        ;

        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowClosed(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowClosing(java.awt.event.WindowEvent e) {
            if (e.getSource() == VISHFrame.this) connEtoC1(e);
        }

        ;

        public void windowDeactivated(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowDeiconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowIconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowOpened(java.awt.event.WindowEvent e) {
        }

        ;
    }

    ;

    /**
 * VISHFrame constructor comment.
 */
    public VISHFrame() {
        super();
        initialize();
    }

    /**
 * VISHFrame constructor comment.
 * @param title java.lang.String
 */
    public VISHFrame(String title) {
        this();
        setTitle(title);
    }

    /**
 * Insert the method's description here.
 * Creation date: (3/14/2002 4:34:56 PM)
 * @param fileList java.util.Vector
 */
    public VISHFrame(java.util.Vector fileList, VISHRemoteMachine remoteMachine) {
        this("VISH 0.1");
        this.remoteMachine = remoteMachine;
        lodGUIData(fileList);
    }

    private void connEtoC1(java.awt.event.WindowEvent arg1) {
        try {
            this.dispose();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private PopupMenu createPopupMenu() {
        PopupMenu pop = new PopupMenu("VISH");
        pop.addSeparator();
        pop.add("Open");
        pop.add("Open with");
        return pop;
    }

    /**
 * Comment
 */
    private void directoryListMouseClicked(java.awt.event.MouseEvent arg1) {
    }

    private void directoryListMouseClickedConnection(java.awt.event.MouseEvent arg1) {
        try {
            this.directoryListMouseClicked(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Comment
 */
    private void exitButtonActionEvents() {
        dispose();
    }

    private void exitButtonClicked() {
        try {
            this.exitButtonActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Comment
 */
    private void fileListMouseClicked(java.awt.event.MouseEvent event) {
        if (event.getModifiers() == 4) {
            PopupMenu pop = createPopupMenu();
            add(pop);
            pop.show(getFileList(), event.getX() + 10, event.getY());
        }
    }

    private void fileListMouseClickedConnection(java.awt.event.MouseEvent arg1) {
        try {
            this.fileListMouseClicked(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private java.awt.List getCommandList() {
        if (ivjCommandList == null) {
            try {
                ivjCommandList = new java.awt.List();
                ivjCommandList.setName("CommandList");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCommandList;
    }

    private java.awt.Panel getContentsPane() {
        if (ivjContentsPane == null) {
            try {
                ivjContentsPane = new java.awt.Panel();
                ivjContentsPane.setName("ContentsPane");
                ivjContentsPane.setLayout(new java.awt.GridBagLayout());
                ivjContentsPane.setBackground(java.awt.SystemColor.control);
                java.awt.GridBagConstraints constraintsScrollPane1 = new java.awt.GridBagConstraints();
                constraintsScrollPane1.gridx = 1;
                constraintsScrollPane1.gridy = 2;
                constraintsScrollPane1.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScrollPane1.weightx = 1.0;
                constraintsScrollPane1.weighty = 1.0;
                constraintsScrollPane1.insets = new java.awt.Insets(1, 6, 1, 2);
                getContentsPane().add(getScrollPane1(), constraintsScrollPane1);
                java.awt.GridBagConstraints constraintsScrollPane2 = new java.awt.GridBagConstraints();
                constraintsScrollPane2.gridx = 2;
                constraintsScrollPane2.gridy = 2;
                constraintsScrollPane2.gridwidth = 4;
                constraintsScrollPane2.fill = java.awt.GridBagConstraints.BOTH;
                constraintsScrollPane2.weightx = 1.0;
                constraintsScrollPane2.weighty = 1.0;
                constraintsScrollPane2.insets = new java.awt.Insets(0, 3, 2, 4);
                getContentsPane().add(getScrollPane2(), constraintsScrollPane2);
                java.awt.GridBagConstraints constraintsControlPanel = new java.awt.GridBagConstraints();
                constraintsControlPanel.gridx = 1;
                constraintsControlPanel.gridy = 3;
                constraintsControlPanel.gridwidth = 5;
                constraintsControlPanel.fill = java.awt.GridBagConstraints.BOTH;
                constraintsControlPanel.ipadx = 146;
                constraintsControlPanel.insets = new java.awt.Insets(1, 7, 6, 4);
                getContentsPane().add(getControlPanel(), constraintsControlPanel);
                java.awt.GridBagConstraints constraintsTitleLabel1 = new java.awt.GridBagConstraints();
                constraintsTitleLabel1.gridx = 2;
                constraintsTitleLabel1.gridy = 1;
                constraintsTitleLabel1.ipadx = -5;
                constraintsTitleLabel1.ipady = -6;
                constraintsTitleLabel1.insets = new java.awt.Insets(16, 287, 3, 0);
                getContentsPane().add(getTitleLabel1(), constraintsTitleLabel1);
                java.awt.GridBagConstraints constraintsTitleLabel2 = new java.awt.GridBagConstraints();
                constraintsTitleLabel2.gridx = 3;
                constraintsTitleLabel2.gridy = 1;
                constraintsTitleLabel2.ipadx = -10;
                constraintsTitleLabel2.ipady = -6;
                constraintsTitleLabel2.insets = new java.awt.Insets(16, 0, 3, 0);
                getContentsPane().add(getTitleLabel2(), constraintsTitleLabel2);
                java.awt.GridBagConstraints constraintsTitleLabel3 = new java.awt.GridBagConstraints();
                constraintsTitleLabel3.gridx = 4;
                constraintsTitleLabel3.gridy = 1;
                constraintsTitleLabel3.ipadx = -15;
                constraintsTitleLabel3.ipady = -6;
                constraintsTitleLabel3.insets = new java.awt.Insets(16, 0, 3, 0);
                getContentsPane().add(getTitleLabel3(), constraintsTitleLabel3);
                java.awt.GridBagConstraints constraintsTitleLabel4 = new java.awt.GridBagConstraints();
                constraintsTitleLabel4.gridx = 5;
                constraintsTitleLabel4.gridy = 1;
                constraintsTitleLabel4.ipadx = -2;
                constraintsTitleLabel4.ipady = -6;
                constraintsTitleLabel4.insets = new java.awt.Insets(16, 0, 3, 12);
                getContentsPane().add(getTitleLabel4(), constraintsTitleLabel4);
                java.awt.GridBagConstraints constraintsDirectoryDescription = new java.awt.GridBagConstraints();
                constraintsDirectoryDescription.gridx = 1;
                constraintsDirectoryDescription.gridy = 1;
                constraintsDirectoryDescription.ipadx = 410;
                constraintsDirectoryDescription.insets = new java.awt.Insets(19, 7, 0, 7);
                getContentsPane().add(getDirectoryDescription(), constraintsDirectoryDescription);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjContentsPane;
    }

    private java.awt.Panel getControlPanel() {
        if (ivjControlPanel == null) {
            try {
                ivjControlPanel = new java.awt.Panel();
                ivjControlPanel.setName("ControlPanel");
                ivjControlPanel.setLayout(new java.awt.GridBagLayout());
                ivjControlPanel.setBackground(java.awt.SystemColor.control);
                java.awt.GridBagConstraints constraintsGoButton = new java.awt.GridBagConstraints();
                constraintsGoButton.gridx = 5;
                constraintsGoButton.gridy = 2;
                constraintsGoButton.ipadx = 26;
                constraintsGoButton.insets = new java.awt.Insets(1, 6, 7, 0);
                getControlPanel().add(getGoButton(), constraintsGoButton);
                java.awt.GridBagConstraints constraintsExitButton = new java.awt.GridBagConstraints();
                constraintsExitButton.gridx = 6;
                constraintsExitButton.gridy = 2;
                constraintsExitButton.ipadx = 23;
                constraintsExitButton.insets = new java.awt.Insets(1, 1, 7, 8);
                getControlPanel().add(getExitButton(), constraintsExitButton);
                java.awt.GridBagConstraints constraintsFilterTextField = new java.awt.GridBagConstraints();
                constraintsFilterTextField.gridx = 4;
                constraintsFilterTextField.gridy = 2;
                constraintsFilterTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsFilterTextField.weightx = 1.0;
                constraintsFilterTextField.ipadx = 39;
                constraintsFilterTextField.insets = new java.awt.Insets(1, 2, 7, 6);
                getControlPanel().add(getFilterTextField(), constraintsFilterTextField);
                java.awt.GridBagConstraints constraintsFilterLabel = new java.awt.GridBagConstraints();
                constraintsFilterLabel.gridx = 4;
                constraintsFilterLabel.gridy = 1;
                constraintsFilterLabel.ipadx = 22;
                constraintsFilterLabel.ipady = -3;
                constraintsFilterLabel.insets = new java.awt.Insets(6, 2, 5, 6);
                getControlPanel().add(getFilterLabel(), constraintsFilterLabel);
                java.awt.GridBagConstraints constraintsDirectoryTextField = new java.awt.GridBagConstraints();
                constraintsDirectoryTextField.gridx = 3;
                constraintsDirectoryTextField.gridy = 2;
                constraintsDirectoryTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsDirectoryTextField.weightx = 1.0;
                constraintsDirectoryTextField.ipadx = 159;
                constraintsDirectoryTextField.insets = new java.awt.Insets(1, 1, 7, 2);
                getControlPanel().add(getDirectoryTextField(), constraintsDirectoryTextField);
                java.awt.GridBagConstraints constraintsDirectoryLabel = new java.awt.GridBagConstraints();
                constraintsDirectoryLabel.gridx = 3;
                constraintsDirectoryLabel.gridy = 1;
                constraintsDirectoryLabel.ipadx = 53;
                constraintsDirectoryLabel.insets = new java.awt.Insets(8, 3, 0, 68);
                getControlPanel().add(getDirectoryLabel(), constraintsDirectoryLabel);
                java.awt.GridBagConstraints constraintsMachineTextField = new java.awt.GridBagConstraints();
                constraintsMachineTextField.gridx = 2;
                constraintsMachineTextField.gridy = 2;
                constraintsMachineTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsMachineTextField.weightx = 1.0;
                constraintsMachineTextField.ipadx = 176;
                constraintsMachineTextField.insets = new java.awt.Insets(2, 5, 6, 0);
                getControlPanel().add(getMachineTextField(), constraintsMachineTextField);
                java.awt.GridBagConstraints constraintsMachineLabel = new java.awt.GridBagConstraints();
                constraintsMachineLabel.gridx = 2;
                constraintsMachineLabel.gridy = 1;
                constraintsMachineLabel.ipadx = 78;
                constraintsMachineLabel.insets = new java.awt.Insets(5, 3, 3, 1);
                getControlPanel().add(getMachineLabel(), constraintsMachineLabel);
                java.awt.GridBagConstraints constraintsCommandList = new java.awt.GridBagConstraints();
                constraintsCommandList.gridx = 1;
                constraintsCommandList.gridy = 1;
                constraintsCommandList.gridheight = 2;
                constraintsCommandList.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCommandList.insets = new java.awt.Insets(5, 3, 2, 3);
                getControlPanel().add(getCommandList(), constraintsCommandList);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjControlPanel;
    }

    private java.awt.Label getDirectoryDescription() {
        if (ivjDirectoryDescription == null) {
            try {
                ivjDirectoryDescription = new java.awt.Label();
                ivjDirectoryDescription.setName("DirectoryDescription");
                ivjDirectoryDescription.setText("");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDirectoryDescription;
    }

    private java.awt.Label getDirectoryLabel() {
        if (ivjDirectoryLabel == null) {
            try {
                ivjDirectoryLabel = new java.awt.Label();
                ivjDirectoryLabel.setName("DirectoryLabel");
                ivjDirectoryLabel.setText("Directory");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDirectoryLabel;
    }

    private java.awt.List getDirectoryList() {
        if (ivjDirectoryList == null) {
            try {
                ivjDirectoryList = new java.awt.List(25);
                ivjDirectoryList.setName("DirectoryList");
                ivjDirectoryList.setBackground(java.awt.SystemColor.control);
                ivjDirectoryList.setBounds(0, 0, 124, 64);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDirectoryList;
    }

    private java.awt.TextField getDirectoryTextField() {
        if (ivjDirectoryTextField == null) {
            try {
                ivjDirectoryTextField = new java.awt.TextField();
                ivjDirectoryTextField.setName("DirectoryTextField");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDirectoryTextField;
    }

    private java.awt.Button getExitButton() {
        if (ivjExitButton == null) {
            try {
                ivjExitButton = new java.awt.Button();
                ivjExitButton.setName("ExitButton");
                ivjExitButton.setLabel("Exit");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjExitButton;
    }

    private java.awt.List getFileList() {
        if (ivjFileList == null) {
            try {
                ivjFileList = new java.awt.List();
                ivjFileList.setName("FileList");
                ivjFileList.setBackground(java.awt.SystemColor.control);
                ivjFileList.setBounds(0, 0, 124, 64);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjFileList;
    }

    private java.awt.Label getFilterLabel() {
        if (ivjFilterLabel == null) {
            try {
                ivjFilterLabel = new java.awt.Label();
                ivjFilterLabel.setName("FilterLabel");
                ivjFilterLabel.setText("Filter");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjFilterLabel;
    }

    private java.awt.TextField getFilterTextField() {
        if (ivjFilterTextField == null) {
            try {
                ivjFilterTextField = new java.awt.TextField();
                ivjFilterTextField.setName("FilterTextField");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjFilterTextField;
    }

    private java.awt.Button getGoButton() {
        if (ivjGoButton == null) {
            try {
                ivjGoButton = new java.awt.Button();
                ivjGoButton.setName("GoButton");
                ivjGoButton.setLabel("Go");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjGoButton;
    }

    private java.awt.Label getMachineLabel() {
        if (ivjMachineLabel == null) {
            try {
                ivjMachineLabel = new java.awt.Label();
                ivjMachineLabel.setName("MachineLabel");
                ivjMachineLabel.setText("Machine/IP address");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMachineLabel;
    }

    private java.awt.TextField getMachineTextField() {
        if (ivjMachineTextField == null) {
            try {
                ivjMachineTextField = new java.awt.TextField();
                ivjMachineTextField.setName("MachineTextField");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMachineTextField;
    }

    private java.awt.Menu getMenu() {
        if (ivjMenu == null) {
            try {
                ivjMenu = new java.awt.Menu();
                ivjMenu.setLabel("Not yet");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMenu;
    }

    private java.awt.ScrollPane getScrollPane1() {
        if (ivjScrollPane1 == null) {
            try {
                ivjScrollPane1 = new java.awt.ScrollPane();
                ivjScrollPane1.setName("ScrollPane1");
                getScrollPane1().add(getDirectoryList(), getDirectoryList().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjScrollPane1;
    }

    private java.awt.ScrollPane getScrollPane2() {
        if (ivjScrollPane2 == null) {
            try {
                ivjScrollPane2 = new java.awt.ScrollPane();
                ivjScrollPane2.setName("ScrollPane2");
                getScrollPane2().add(getFileList(), getFileList().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjScrollPane2;
    }

    private java.awt.Label getTitleLabel1() {
        if (ivjTitleLabel1 == null) {
            try {
                ivjTitleLabel1 = new java.awt.Label();
                ivjTitleLabel1.setName("TitleLabel1");
                ivjTitleLabel1.setAlignment(java.awt.Label.RIGHT);
                ivjTitleLabel1.setFont(new java.awt.Font("serif", 0, 18));
                ivjTitleLabel1.setText("VI");
                ivjTitleLabel1.setForeground(new java.awt.Color(125, 0, 43));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTitleLabel1;
    }

    private java.awt.Label getTitleLabel2() {
        if (ivjTitleLabel2 == null) {
            try {
                ivjTitleLabel2 = new java.awt.Label();
                ivjTitleLabel2.setName("TitleLabel2");
                ivjTitleLabel2.setFont(new java.awt.Font("serif", 0, 18));
                ivjTitleLabel2.setText("rtual");
                ivjTitleLabel2.setForeground(new java.awt.Color(47, 0, 0));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTitleLabel2;
    }

    private java.awt.Label getTitleLabel3() {
        if (ivjTitleLabel3 == null) {
            try {
                ivjTitleLabel3 = new java.awt.Label();
                ivjTitleLabel3.setName("TitleLabel3");
                ivjTitleLabel3.setFont(new java.awt.Font("serif", 0, 18));
                ivjTitleLabel3.setText("SH");
                ivjTitleLabel3.setForeground(new java.awt.Color(126, 1, 43));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTitleLabel3;
    }

    private java.awt.Label getTitleLabel4() {
        if (ivjTitleLabel4 == null) {
            try {
                ivjTitleLabel4 = new java.awt.Label();
                ivjTitleLabel4.setName("TitleLabel4");
                ivjTitleLabel4.setFont(new java.awt.Font("serif", 0, 18));
                ivjTitleLabel4.setText("aring");
                ivjTitleLabel4.setForeground(new java.awt.Color(47, 0, 0));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTitleLabel4;
    }

    private java.awt.MenuBar getVISHFrameMenuBar() {
        if (ivjVISHFrameMenuBar == null) {
            try {
                ivjVISHFrameMenuBar = new java.awt.MenuBar();
                ivjVISHFrameMenuBar.add(getMenu());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjVISHFrameMenuBar;
    }

    /**
 * Comment
 */
    private void goButtonActionEvents() {
    }

    private void goButtonClicked() {
        try {
            this.goButtonActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        exception.printStackTrace(System.out);
    }

    private void initConnections() throws java.lang.Exception {
        this.addWindowListener(ivjEventHandler);
        getExitButton().addActionListener(ivjEventHandler);
        getGoButton().addActionListener(ivjEventHandler);
        getDirectoryList().addMouseListener(ivjEventHandler);
        getFileList().addMouseListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("VISHFrame");
            setResizable(false);
            setMenuBar(getVISHFrameMenuBar());
            setLayout(new java.awt.BorderLayout());
            setSize(876, 547);
            setTitle("VISH 0.1");
            add(getContentsPane(), "Center");
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Insert the method's description here.
 * Creation date: (3/14/2002 4:55:23 PM)
 * @param guiData java.util.Vector
 */
    private void lodGUIData(java.util.Vector guiData) {
        getDirectoryList().add("[.]");
        for (int i = 0; i < guiData.size(); i++) {
            File file = (File) guiData.get(i);
            if (file.isDirectory()) {
                getDirectoryList().add(file.getName());
            } else {
                getFileList().add(file.getName());
            }
        }
        getDirectoryList().select(0);
    }

    /**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            VISHFrame aVISHFrame;
            aVISHFrame = new VISHFrame();
            aVISHFrame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aVISHFrame.show();
            java.awt.Insets insets = aVISHFrame.getInsets();
            aVISHFrame.setSize(aVISHFrame.getWidth() + insets.left + insets.right, aVISHFrame.getHeight() + insets.top + insets.bottom);
            aVISHFrame.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of java.awt.Frame");
            exception.printStackTrace(System.out);
        }
    }
}
