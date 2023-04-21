package org.opensourcephysics.controls;

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import javax.swing.*;
import org.opensourcephysics.display.*;
import org.opensourcephysics.tools.FontSizer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.opensourcephysics.tools.ToolsRes;
import java.util.Locale;

/**
 *  A frame with menu items for saving and loading control parameters
 *
 * @author       Wolfgang Christian
 * @version 1.0
 */
public abstract class ControlFrame extends OSPFrame implements Control {

    static final int MENU_SHORTCUT_KEY_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    protected Object model;

    protected JMenuItem[] languageItems;

    protected JMenu languageMenu;

    protected JMenu fileMenu;

    protected JMenu editMenu;

    protected JMenu displayMenu;

    protected JMenuItem readItem, clearItem, printFrameItem, saveFrameAsEPSItem;

    protected JMenuItem saveAsItem;

    protected JMenuItem copyItem;

    protected JMenuItem inspectItem;

    protected JMenuItem logToFileItem;

    protected JMenuItem sizeUpItem;

    protected JMenuItem sizeDownItem;

    protected OSPApplication ospApp;

    protected XMLControlElement xmlDefault;

    protected ControlFrame(String title) {
        super(title);
        createMenuBar();
        setName("controlFrame");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        if (!OSPRuntime.appletMode) {
            setJMenuBar(menuBar);
        }
        fileMenu = new JMenu(ControlsRes.getString("ControlFrame.File"));
        editMenu = new JMenu(ControlsRes.getString("ControlFrame.Edit"));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        readItem = new JMenuItem(ControlsRes.getString("ControlFrame.Load_XML"));
        saveAsItem = new JMenuItem(ControlsRes.getString("ControlFrame.Save_XML"));
        inspectItem = new JMenuItem(ControlsRes.getString("ControlFrame.Inspect_XML"));
        clearItem = new JMenuItem(ControlsRes.getString("ControlFrame.Clear_XML"));
        copyItem = new JMenuItem(ControlsRes.getString("ControlFrame.Copy"));
        printFrameItem = new JMenuItem(DisplayRes.getString("DrawingFrame.PrintFrame_menu_item"));
        saveFrameAsEPSItem = new JMenuItem(DisplayRes.getString("DrawingFrame.SaveFrameAsEPS_menu_item"));
        JMenu printMenu = new JMenu(DisplayRes.getString("DrawingFrame.Print_menu_title"));
        if (OSPRuntime.applet == null) fileMenu.add(readItem);
        if (OSPRuntime.applet == null) fileMenu.add(saveAsItem);
        fileMenu.add(inspectItem);
        fileMenu.add(clearItem);
        if (OSPRuntime.applet == null) fileMenu.add(printMenu);
        printMenu.add(printFrameItem);
        printMenu.add(saveFrameAsEPSItem);
        editMenu.add(copyItem);
        copyItem.setAccelerator(KeyStroke.getKeyStroke('C', MENU_SHORTCUT_KEY_MASK));
        copyItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                copy();
            }
        });
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke('S', MENU_SHORTCUT_KEY_MASK));
        saveAsItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveXML();
            }
        });
        inspectItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                inspectXML();
            }
        });
        readItem.setAccelerator(KeyStroke.getKeyStroke('L', MENU_SHORTCUT_KEY_MASK));
        readItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadXML((String) null);
            }
        });
        clearItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                xmlDefault = null;
            }
        });
        printFrameItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PrintUtils.printComponent(ControlFrame.this);
            }
        });
        saveFrameAsEPSItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    PrintUtils.saveComponentAsEPS(ControlFrame.this);
                } catch (IOException ex) {
                }
            }
        });
        loadDisplayMenu();
        JMenu helpMenu = new JMenu(ControlsRes.getString("ControlFrame.Help"));
        menuBar.add(helpMenu);
        JMenuItem aboutItem = new JMenuItem(ControlsRes.getString("ControlFrame.About"));
        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OSPRuntime.showAboutDialog(ControlFrame.this);
            }
        });
        helpMenu.add(aboutItem);
        JMenuItem sysItem = new JMenuItem(ControlsRes.getString("ControlFrame.System"));
        sysItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ControlUtils.showSystemProperties(true);
            }
        });
        helpMenu.add(sysItem);
        JMenuItem showItem = new JMenuItem(ControlsRes.getString("ControlFrame.Display_All_Frames"));
        showItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                org.opensourcephysics.display.GUIUtils.showDrawingAndTableFrames();
            }
        });
        helpMenu.add(showItem);
        helpMenu.addSeparator();
        JMenuItem logItem = new JMenuItem(ControlsRes.getString("ControlFrame.Message_Log"));
        logItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OSPLog.getOSPLog().setVisible(true);
            }
        });
        if (OSPRuntime.applet == null) helpMenu.add(logItem);
        logToFileItem = new JCheckBoxMenuItem(ControlsRes.getString("ControlFrame.Log_to_File"));
        logToFileItem.setSelected(false);
        logToFileItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                OSPLog.getOSPLog().setLogToFile(item.isSelected());
            }
        });
        helpMenu.add(logToFileItem);
        validate();
    }

    /**
    * Adds a Display menu to the menu bar. Overrides OSPFrame method.
    *
    * @return the display menu
    */
    protected JMenu loadDisplayMenu() {
        JMenuBar menuBar = getJMenuBar();
        if (menuBar == null) {
            return null;
        }
        displayMenu = super.loadDisplayMenu();
        if (displayMenu == null) {
            displayMenu = new JMenu();
            displayMenu.setText(ControlsRes.getString("ControlFrame.Display"));
            menuBar.add(displayMenu);
        }
        languageMenu = new JMenu();
        languageMenu.setText(ControlsRes.getString("ControlFrame.Language"));
        Action languageAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                String language = e.getActionCommand();
                OSPLog.finest("setting language to " + language);
                Locale[] locales = OSPRuntime.getInstalledLocales();
                for (int i = 0; i < locales.length; i++) {
                    if (language.equals(locales[i].getDisplayName())) {
                        ToolsRes.setLocale(locales[i]);
                        return;
                    }
                }
            }
        };
        Locale[] locales = OSPRuntime.getInstalledLocales();
        ButtonGroup languageGroup = new ButtonGroup();
        languageItems = new JMenuItem[locales.length];
        for (int i = 0; i < locales.length; i++) {
            languageItems[i] = new JRadioButtonMenuItem(locales[i].getDisplayName(locales[i]));
            languageItems[i].setActionCommand(locales[i].getDisplayName());
            languageItems[i].addActionListener(languageAction);
            languageMenu.add(languageItems[i]);
            languageGroup.add(languageItems[i]);
        }
        for (int i = 0; i < locales.length; i++) {
            if (locales[i].getLanguage().equals(ToolsRes.getLanguage())) {
                languageItems[i].setSelected(true);
            }
        }
        if (OSPRuntime.isAuthorMode() || !OSPRuntime.isLauncherMode()) {
            displayMenu.add(languageMenu);
        }
        JMenu fontMenu = new JMenu(DisplayRes.getString("DrawingFrame.Font_menu_title"));
        displayMenu.add(fontMenu);
        JMenuItem sizeUpItem = new JMenuItem(ControlsRes.getString("ControlFrame.Increase_Font_Size"));
        sizeUpItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontSizer.levelUp();
            }
        });
        fontMenu.add(sizeUpItem);
        final JMenuItem sizeDownItem = new JMenuItem(ControlsRes.getString("ControlFrame.Decrease_Font_Size"));
        sizeDownItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FontSizer.levelDown();
            }
        });
        fontMenu.add(sizeDownItem);
        fontMenu.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                sizeDownItem.setEnabled(FontSizer.getLevel() > 0);
            }
        });
        return displayMenu;
    }

    /**
    * Refreshes the user interface in response to display changes such as Language.
    */
    protected void refreshGUI() {
        super.refreshGUI();
        createMenuBar();
    }

    /** Saves a file containing the control parameters to the disk. */
    public void save() {
        ControlUtils.saveToFile(this, ControlFrame.this);
    }

    /** Loads a file containing the control parameters from the disk. */
    public void readParameters() {
        ControlUtils.loadParameters((Control) this, ControlFrame.this);
    }

    /** Copies the data in the table to the system clipboard */
    public void copy() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(this.toString());
        clipboard.setContents(stringSelection, stringSelection);
    }

    public void saveXML() {
        JFileChooser chooser = OSPRuntime.getChooser();
        if (chooser == null) {
            return;
        }
        String oldTitle = chooser.getDialogTitle();
        chooser.setDialogTitle(ControlsRes.getString("ControlFrame.Save_XML_Data"));
        int result = chooser.showSaveDialog(null);
        chooser.setDialogTitle(oldTitle);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.exists()) {
                int selected = JOptionPane.showConfirmDialog(null, ControlsRes.getString("ControlFrame.Replace_existing") + file.getName() + ControlsRes.getString("ControlFrame.question_mark"), ControlsRes.getString("ControlFrame.Replace_File"), JOptionPane.YES_NO_CANCEL_OPTION);
                if (selected != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            OSPRuntime.chooserDir = chooser.getCurrentDirectory().toString();
            String fileName = file.getAbsolutePath();
            if ((fileName == null) || fileName.trim().equals("")) {
                return;
            }
            int i = fileName.toLowerCase().lastIndexOf(".xml");
            if (i != fileName.length() - 4) {
                fileName += ".xml";
            }
            XMLControl xml = new XMLControlElement(getOSPApp());
            xml.write(fileName);
        }
    }

    public void loadXML(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                loadXML(args[i]);
            }
        }
    }

    public void loadXML(XMLControlElement xml) {
        if (OSPApplication.class.isAssignableFrom(xml.getObjectClass())) {
            xmlDefault = xml;
            xmlDefault.loadObject(getOSPApp());
        } else {
            JOptionPane.showMessageDialog(this, "XML is for " + xml.getObjectClass() + ".", "Incorrect XML Object Type", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadXML(String fileName) {
        if ((fileName == null) || fileName.trim().equals("")) {
            loadXML();
            return;
        }
        loadXML(new XMLControlElement(fileName));
    }

    public void loadXML() {
        JFileChooser chooser = OSPRuntime.getChooser();
        if (chooser == null) {
            return;
        }
        String oldTitle = chooser.getDialogTitle();
        chooser.setDialogTitle(ControlsRes.getString("ControlFrame.Load_XML_Data"));
        int result = chooser.showOpenDialog(null);
        chooser.setDialogTitle(oldTitle);
        if (result == JFileChooser.APPROVE_OPTION) {
            OSPRuntime.chooserDir = chooser.getCurrentDirectory().toString();
            String fileName = chooser.getSelectedFile().getAbsolutePath();
            loadXML(new XMLControlElement(fileName));
        }
    }

    public void inspectXML() {
        XMLControl xml = new XMLControlElement(getOSPApp());
        XMLTreePanel treePanel = new XMLTreePanel(xml);
        JDialog dialog = new JDialog((java.awt.Frame) null, true);
        dialog.setContentPane(treePanel);
        dialog.setSize(new Dimension(600, 300));
        dialog.setVisible(true);
    }

    /**
    * Gets the OSP Application that is controlled by this frame.
    * @return
    */
    public OSPApplication getOSPApp() {
        if (ospApp == null) {
            ospApp = new OSPApplication(this, model);
        }
        return ospApp;
    }
}
