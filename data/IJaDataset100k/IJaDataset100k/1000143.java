package editor.gui;

import editor.util.ClipboardWatcher;
import editor.util.ConfigFileWatcher;
import editor.util.CreateConfig;
import editor.util.Printing;
import editor.util.TextTransfer;
import editor.util.XMLParser;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class MainFrame extends javax.swing.JFrame {

    private static boolean instance_flag = false;

    private static MainFrame mainFrame;

    private static DevOutputFrame devOutputFrame;

    private OptionsFrame optionsFrame;

    /** Creates new form JavaEditorGUI */
    public MainFrame() throws SingeltonException {
        if (instance_flag) {
            throw new SingeltonException("Only one JavaEditorGUI instance is allowed");
        } else {
            instance_flag = true;
        }
        CreateConfig createConfig = new CreateConfig();
        createConfig.createConfigFile();
        initComponents();
        center();
        devOutputFrame = new DevOutputFrame();
        this.disableMenuItems();
        ConfigFileWatcher configFileWatcher = new ConfigFileWatcher();
        configFileWatcher.listenConfigFile();
        JOptionPane.showMessageDialog(this, "This is development build 0.0.3 of Arcturus - JavaEditor! \n It is intended just for testing!", "Arcturus - JavaEditor", JOptionPane.INFORMATION_MESSAGE, null);
    }

    private void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frame = getBounds();
        setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
    }

    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        mainPanel1 = new editor.gui.MainPanel();
        statusBarPanel1 = new editor.gui.StatusBarPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        pageSetupjMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        selectAllMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        compileMenuItem = new javax.swing.JMenuItem();
        runMenuItem = new javax.swing.JMenuItem();
        runParamMenuItem = new javax.swing.JMenuItem();
        runAppletMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        devOutputMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        optionsMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        infoMenuItem = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Arcturus - JavaEditor");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/New24.gif")));
        jButton1.setToolTipText("New");
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Open24.gif")));
        jButton2.setToolTipText("Open");
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Save24.gif")));
        jButton3.setToolTipText("Save");
        jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/About24.gif")));
        jButton4.setToolTipText("About");
        jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        statusBarPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusBarPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE).addComponent(mainPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(mainPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE).addGap(0, 0, 0).addComponent(statusBarPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jMenu1.setText("File");
        newMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/New16.gif")));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(newMenuItem);
        jMenu1.add(jSeparator1);
        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Open16.gif")));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openMenuItem);
        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Save16.gif")));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);
        saveAsMenuItem.setText("Save As");
        saveAsMenuItem.setActionCommand("SaveAs");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsMenuItem);
        jMenu1.add(jSeparator2);
        pageSetupjMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/PageSetup16.gif")));
        pageSetupjMenuItem.setText("Page Setup...");
        pageSetupjMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageSetupjMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(pageSetupjMenuItem);
        printMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Print16.gif")));
        printMenuItem.setText("Print");
        printMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(printMenuItem);
        jMenu1.add(jSeparator3);
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);
        jMenuBar1.add(jMenu1);
        jMenu4.setText("Edit");
        undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Undo16.gif")));
        undoMenuItem.setText("Undo");
        undoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(undoMenuItem);
        redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Redo16.gif")));
        redoMenuItem.setText("Redo");
        redoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(redoMenuItem);
        jMenu4.add(jSeparator4);
        cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Cut16.gif")));
        cutMenuItem.setText("Cut");
        cutMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(cutMenuItem);
        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Copy16.gif")));
        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(copyMenuItem);
        pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/Paste16.gif")));
        pasteMenuItem.setText("Paste");
        pasteMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(pasteMenuItem);
        deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setText("Delete");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(deleteMenuItem);
        jMenu4.add(jSeparator5);
        selectAllMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAllMenuItem.setText("Select All");
        selectAllMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(selectAllMenuItem);
        jMenuBar1.add(jMenu4);
        jMenu2.setText("Tools");
        jMenu2.setFocusable(false);
        compileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        compileMenuItem.setText("Compile");
        compileMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(compileMenuItem);
        runMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        runMenuItem.setText("Run");
        runMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(runMenuItem);
        runParamMenuItem.setText("Run Param");
        runParamMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runParamMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(runParamMenuItem);
        runAppletMenuItem.setText("Run Applet");
        runAppletMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAppletMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(runAppletMenuItem);
        jMenu2.add(jSeparator6);
        devOutputMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        devOutputMenuItem.setText("DevOutput");
        devOutputMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devOutputMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(devOutputMenuItem);
        jMenu2.add(jSeparator7);
        optionsMenuItem.setText("Options");
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(optionsMenuItem);
        jMenuBar1.add(jMenu2);
        jMenu3.setText("Help");
        infoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icons/About16.gif")));
        infoMenuItem.setText("Info");
        infoMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(infoMenuItem);
        jMenuBar1.add(jMenu3);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, 0).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void compileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainPanel1.isAnyTabOpen()) {
            devOutputFrame.compileJava();
        }
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.actionOnExit();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        mainPanel1.actionOnExit();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.save();
    }

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.saveAs();
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.save();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.openFileInNewTab();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.addNewTab();
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.openFileInNewTab();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        Info info = new Info();
        info.displayInfoDialog(this);
    }

    private void infoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        Info info = new Info();
        info.displayInfoDialog(this);
    }

    private void runMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainPanel1.isAnyTabOpen()) {
            devOutputFrame.runJava();
        }
    }

    private void runParamMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainPanel1.isAnyTabOpen()) {
            devOutputFrame.runJavaParameters();
        }
    }

    private void cutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        wa.getJtextArea1().cut();
        pasteMenuItem.setEnabled(true);
    }

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        wa.getJtextArea1().copy();
        pasteMenuItem.setEnabled(true);
    }

    private void selectAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        wa.getJtextArea1().selectAll();
    }

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        optionsFrame = new OptionsFrame();
        optionsFrame.setVisible(true);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        XMLParser xmlParser = new XMLParser();
        PropertiesSetter ps = PropertiesSetter.getPropertiesSetter();
        File file = new File("config.xml");
        String tab_sizeS = xmlParser.returnTagValueByAttribute(file, "id", "tab_size");
        int tab_sizeI = Integer.parseInt(tab_sizeS);
        ps.setTabSize(tab_sizeI);
        mainPanel1.addStartTabs();
        ClipboardWatcher cw = new ClipboardWatcher();
        cw.listenClipboardForTransferableText();
        pageSetupjMenuItem.setEnabled(false);
    }

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainPanel1.addNewTab();
    }

    private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        wa.decrementUndoCounter();
        int undoCounter = wa.getUndoCounter();
        System.out.println("undoCounter = " + wa.getUndoCounter());
        UndoManager undo = wa.getUndoManager();
        try {
            if (undo.canUndo()) {
                undo.undo();
                wa.incrementRedoCounter();
                if (!redoMenuItem.isEnabled()) {
                    enableMenuItemRedo();
                }
            } else {
                wa.setUndoCounter(0);
                if (!redoMenuItem.isEnabled()) {
                    enableMenuItemUndo();
                }
            }
        } catch (CannotUndoException e) {
            System.out.println("CannotUndoException");
        }
        if (!(undoCounter > 0)) {
            if (undoMenuItem.isEnabled()) {
                undoMenuItem.setEnabled(false);
            }
        }
    }

    private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        UndoManager undo = wa.getUndoManager();
        try {
            if (undo.canRedo()) {
                undo.redo();
                wa.decrementRedoCounter();
            } else {
                wa.setRedoCounter(0);
            }
        } catch (CannotRedoException e) {
            System.out.println("CannotRedoException");
        }
        System.out.println("redoCounter = " + wa.getRedoCounter());
        if (!(wa.getRedoCounter() > 0)) {
            if (redoMenuItem.isEnabled()) {
                redoMenuItem.setEnabled(false);
            }
        }
    }

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        wa.getJtextArea1().replaceSelection("");
    }

    private void pageSetupjMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageFormat pf = pj.pageDialog(pj.defaultPage());
    }

    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        Printing printing = new Printing();
        printing.print(wa);
    }

    private void pasteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        TextTransfer textTransfer = new TextTransfer();
        WorkArea wa = (WorkArea) (MainPanel.getJTabbedPaneWithCloseIcons1Instance().getSelectedComponent());
        String clipboardContent = textTransfer.getClipboardContents();
        if (clipboardContent != null) {
            System.out.println(clipboardContent);
            wa.getJtextArea1().paste();
        }
    }

    private void devOutputMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        DevOutputFrame devOutputFrame = MainFrame.getDevOutputFrameInstance();
        if (!devOutputFrame.isVisible()) {
            devOutputFrame.setVisible(true);
        } else {
            devOutputFrame.toFront();
        }
    }

    private void runAppletMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainPanel1.isAnyTabOpen()) {
            devOutputFrame.generateAppletHtml();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    private javax.swing.JMenuItem compileMenuItem;

    private javax.swing.JMenuItem copyMenuItem;

    private javax.swing.JMenuItem cutMenuItem;

    private javax.swing.JMenuItem deleteMenuItem;

    private javax.swing.JMenuItem devOutputMenuItem;

    private javax.swing.JMenuItem exitMenuItem;

    private javax.swing.JMenuItem infoMenuItem;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenu jMenu4;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JSeparator jSeparator4;

    private javax.swing.JSeparator jSeparator5;

    private javax.swing.JSeparator jSeparator6;

    private javax.swing.JSeparator jSeparator7;

    private javax.swing.JToolBar jToolBar1;

    private static editor.gui.MainPanel mainPanel1;

    private javax.swing.JMenuItem newMenuItem;

    private javax.swing.JMenuItem openMenuItem;

    private javax.swing.JMenuItem optionsMenuItem;

    private javax.swing.JMenuItem pageSetupjMenuItem;

    private javax.swing.JMenuItem pasteMenuItem;

    private javax.swing.JMenuItem printMenuItem;

    private javax.swing.JMenuItem redoMenuItem;

    private javax.swing.JMenuItem runAppletMenuItem;

    private javax.swing.JMenuItem runMenuItem;

    private javax.swing.JMenuItem runParamMenuItem;

    private javax.swing.JMenuItem saveAsMenuItem;

    private javax.swing.JMenuItem saveMenuItem;

    private javax.swing.JMenuItem selectAllMenuItem;

    private static editor.gui.StatusBarPanel statusBarPanel1;

    private javax.swing.JMenuItem undoMenuItem;

    public boolean areMenuItemsCutCopyDeleteEnabled() {
        if (copyMenuItem.isEnabled() && cutMenuItem.isEnabled() && deleteMenuItem.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public void disableMenuItemsCutCopyDelete() {
        cutMenuItem.setEnabled(false);
        copyMenuItem.setEnabled(false);
        deleteMenuItem.setEnabled(false);
    }

    public void enableMenuItemsCutCopyDelete() {
        cutMenuItem.setEnabled(true);
        copyMenuItem.setEnabled(true);
        deleteMenuItem.setEnabled(true);
    }

    public boolean isPasteMenuItemEnabled() {
        boolean b = pasteMenuItem.isEnabled();
        return b;
    }

    public void disablePasteMenuItem() {
        pasteMenuItem.setEnabled(false);
    }

    public void enablePasteMenuItem() {
        pasteMenuItem.setEnabled(true);
    }

    public void disableMenuItemSave() {
        saveMenuItem.setEnabled(false);
        jButton3.setEnabled(false);
    }

    public void enableMenuItemSave() {
        saveMenuItem.setEnabled(true);
        jButton3.setEnabled(true);
    }

    public void disableMenuItemRedo() {
        redoMenuItem.setEnabled(false);
    }

    public void enableMenuItemRedo() {
        redoMenuItem.setEnabled(true);
    }

    public void enableMenuItemUndo() {
        undoMenuItem.setEnabled(true);
    }

    public JMenuItem getUndoMenuItem() {
        return undoMenuItem;
    }

    public void disableMenuItemUndo() {
        undoMenuItem.setEnabled(false);
    }

    public void disableMenuItems() {
        saveMenuItem.setEnabled(false);
        saveAsMenuItem.setEnabled(false);
        pageSetupjMenuItem.setEnabled(false);
        printMenuItem.setEnabled(false);
        cutMenuItem.setEnabled(false);
        copyMenuItem.setEnabled(false);
        pasteMenuItem.setEnabled(false);
        selectAllMenuItem.setEnabled(false);
        compileMenuItem.setEnabled(false);
        runMenuItem.setEnabled(false);
        runParamMenuItem.setEnabled(false);
        jButton3.setEnabled(false);
        undoMenuItem.setEnabled(false);
        redoMenuItem.setEnabled(false);
        deleteMenuItem.setEnabled(false);
        runAppletMenuItem.setEnabled(false);
    }

    public void enableMenuItems() {
        saveMenuItem.setEnabled(true);
        saveAsMenuItem.setEnabled(true);
        printMenuItem.setEnabled(true);
        pasteMenuItem.setEnabled(true);
        selectAllMenuItem.setEnabled(true);
        compileMenuItem.setEnabled(true);
        runMenuItem.setEnabled(true);
        runParamMenuItem.setEnabled(true);
        jButton3.setEnabled(true);
        runAppletMenuItem.setEnabled(true);
    }

    public static MainFrame getMainFrameInstance() {
        return mainFrame;
    }

    public static MainPanel getMainPanel1Instance() {
        return mainPanel1;
    }

    public static DevOutputFrame getDevOutputFrameInstance() {
        return devOutputFrame;
    }

    public static StatusBarPanel getStatusBarPanel1Instance() {
        return statusBarPanel1;
    }
}
