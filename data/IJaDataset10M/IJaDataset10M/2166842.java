package com.ununbium.Scripting;

import com.ununbium.Scripting.panel.OutputViewerPanel;
import com.ununbium.Scripting.panel.ScriptParamsPanel;
import com.ununbium.Scripting.panel.ScriptPropertiesPanel;
import com.ununbium.Scripting.panel.SearchReplacePanel;
import com.ununbium.Scripting.panel.ConfigSettingsPanel;
import com.ununbium.Util.ScriptFilter;
import com.ununbium.Util.apple.*;
import com.wpg.proxy.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.security.*;
import org.apache.log4j.Logger;

public class ScriptGUI extends JPanel implements ActionListener, ItemListener, ApplicationListener {

    static JFrame rootFrame;

    JPanel contentPane;

    static JToolBar toolBar;

    static JTextArea output = null;

    public static JTextPane script = null;

    static String currentConfigFile = "script-config.xml";

    public static Script currentScript = null;

    public static DefaultStyledDocument scriptDoc = null;

    public static Config config = null;

    static Proxy proxy = null;

    HttpMessageHandler httpML = null;

    private File keyFile = null;

    private static File openFileAfterInit = null;

    private HashMap<String, Action> actions = null;

    protected UndoManager undo = new UndoManager();

    protected Action undoAction;

    protected Action redoAction;

    JFileChooser chooser = null;

    public static Logger logger = Logger.getLogger(ScriptGUI.class);

    private void createActionTable(JTextComponent textCom) {
        actions = new HashMap<String, Action>();
        Action[] actionsArray = textCom.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            actions.put((String) actionsArray[i].getValue(Action.NAME), actionsArray[i]);
        }
    }

    private Action getActionByName(String name) {
        return (Action) (actions.get(name));
    }

    public JMenuBar createMenuBar() {
        logger.trace("called: createMenuBar()");
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("File Menu");
        menuBar.add(menu);
        menuItem = new JMenuItem("New...", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Save", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Save As...", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Close Script", KeyEvent.VK_C);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        if (!System.getProperty("os.name").startsWith("Mac")) {
            menu.addSeparator();
            menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(menu);
        menuItem = new JMenuItem("Undo", KeyEvent.VK_Z);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undoAction = new AbstractAction("Undo") {

            public void actionPerformed(ActionEvent a) {
                try {
                    undo.undo();
                } catch (CannotUndoException ex) {
                    logger.warn("Cannot Undo Exception: " + ex);
                }
            }
        };
        undoAction.setEnabled(false);
        menuItem.setAction(undoAction);
        menu.add(menuItem);
        menuItem = new JMenuItem("Redo", KeyEvent.VK_Y);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        redoAction = new AbstractAction("Redo") {

            public void actionPerformed(ActionEvent a) {
                try {
                    undo.redo();
                } catch (CannotRedoException ex) {
                    logger.warn("Cannot Redo Exception: " + ex);
                }
            }
        };
        redoAction.setEnabled(false);
        menuItem.setAction(redoAction);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Cut", KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        Action action = new AbstractAction("Cut") {

            public void actionPerformed(ActionEvent a) {
                script.cut();
            }
        };
        menuItem.setAction(action);
        menu.add(menuItem);
        menuItem = new JMenuItem("Copy", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        action = new AbstractAction("Copy") {

            public void actionPerformed(ActionEvent a) {
                script.copy();
            }
        };
        menuItem.setAction(action);
        menu.add(menuItem);
        menuItem = new JMenuItem("Paste", KeyEvent.VK_V);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        action = new AbstractAction("Paste") {

            public void actionPerformed(ActionEvent a) {
                script.paste();
            }
        };
        menuItem.setAction(action);
        menu.add(menuItem);
        menuItem = new JMenuItem("Search and Replace", KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        if (!System.getProperty("os.name").startsWith("Mac")) {
            menu.addSeparator();
            menuItem = new JMenuItem("Config Settings", KeyEvent.VK_P);
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        menu = new JMenu("Script");
        menu.setMnemonic(KeyEvent.VK_B);
        menuBar.add(menu);
        menuItem = new JMenuItem("Record Script", KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Build/Compile", KeyEvent.VK_B);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Run Script", KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Log Viewer", KeyEvent.VK_V);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Script Properties...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Edit Parameters...", KeyEvent.VK_E);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        for (int i = 0; i < config.plugins.size(); i++) {
            ScriptPlugin sp = config.plugins.elementAt(i);
            if (sp.isActive()) {
                JMenu m = sp.getMenuItems();
                if (m != null) {
                    menu.add(m);
                }
            }
        }
        menu = new JMenu("Insert");
        menu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(menu);
        menuItem = new JMenuItem("Start Transaction", KeyEvent.VK_S);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Stop Transaction", KeyEvent.VK_S);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        menuItem = new JMenuItem("Sleep Timer", KeyEvent.VK_S);
        menuItem.addActionListener(this);
        menu.add(menuItem);
        for (int i = 0; i < config.plugins.size(); i++) {
            ScriptPlugin sp = config.plugins.elementAt(i);
            if (sp.isActive()) {
                JMenu m = sp.getHelpers();
                if (m != null) {
                    menu.add(m);
                }
            }
        }
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(menu);
        if (!System.getProperty("os.name").startsWith("Mac")) {
            menuItem = new JMenuItem("About VolumLab Scripting Tool...", KeyEvent.VK_A);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        logger.trace("finished: createMenuBar()");
        return menuBar;
    }

    private JButton makeNavButton(String imageName, String action, String toolTip, String altText) {
        String imgLocation = "/toolbarButtonGraphics/" + imageName + "16.gif";
        URL imageURL = this.getClass().getResource(imgLocation);
        JButton button = new JButton();
        button.setActionCommand(action);
        button.setToolTipText(toolTip);
        button.addActionListener(this);
        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            button.setText(altText);
            logger.warn("ToolBar Resource Not Found: " + imgLocation);
        }
        return button;
    }

    public JToolBar createToolBar() {
        JToolBar tool = new JToolBar("VolumLab Scripting Tool Bar");
        JButton button = null;
        tool.setFloatable(true);
        tool.setRollover(true);
        button = makeNavButton("media/Play", "Run Script", "Run the current script", "Run");
        tool.add(button);
        button = makeNavButton("media/Stop", "Stop Script", "Stop the current running script", "Stop");
        tool.add(button);
        button = makeNavButton("media/Movie", "Record Script", "Start Recording Script via Proxy", "Record");
        tool.add(button);
        tool.addSeparator();
        button = makeNavButton("general/Save", "Save", "Save the script", "Save");
        tool.add(button);
        button = makeNavButton("development/Application", "Build/Compile", "Compile and package the script", "Build");
        tool.add(button);
        tool.addSeparator();
        button = makeNavButton("table/RowInsertAfter", "Edit Parameters...", "Manage Parameters", "Parameters");
        tool.add(button);
        button = makeNavButton("general/Preferences", "Script Properties...", "Manage Script Properties", "Properties");
        tool.add(button);
        tool.addSeparator();
        button = makeNavButton("general/Add", "Start Transaction", "Insert Start Transaction Call", "Start Tran");
        tool.add(button);
        button = makeNavButton("general/Stop", "Stop Transaction", "Insert Stop Transaction Call", "Stop Tran");
        tool.add(button);
        button = makeNavButton("general/TipOfTheDay", "Sleep Timer", "Insert Sleep Call", "Sleep");
        tool.add(button);
        button = makeNavButton("general/Replace", "Verify Returned Text", "Insert Verify Returned Text Call", "Verify Return");
        tool.add(button);
        tool.addSeparator();
        return tool;
    }

    public Container createContentPane() {
        logger.trace("called: createContentPane()");
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        contentPane.setMinimumSize(new Dimension(640, 480));
        Dimension minimumSize = new Dimension(0, 0);
        toolBar = createToolBar();
        contentPane.add(toolBar, BorderLayout.PAGE_START);
        scriptDoc = new DefaultStyledDocument();
        scriptDoc.addUndoableEditListener(new Undoer());
        script = new JTextPane(scriptDoc);
        createActionTable(script);
        script.setEditable(true);
        JScrollPane scriptPane = new JScrollPane(script);
        scriptPane.setMinimumSize(minimumSize);
        scriptPane.setPreferredSize(new Dimension(400, 400));
        output = new JTextArea(10, 10);
        output.setEditable(false);
        JScrollPane outputPane = new JScrollPane(output);
        outputPane.setMinimumSize(minimumSize);
        outputPane.setPreferredSize(new Dimension(20, 10));
        if (System.getProperty("os.name").startsWith("Mac")) {
            scriptPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            outputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            outputPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        }
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scriptPane, outputPane);
        contentPane.add(splitPane, BorderLayout.CENTER);
        if (System.getProperty("os.name").startsWith("Mac")) {
            contentPane.add(Box.createVerticalStrut(15), BorderLayout.PAGE_END);
        }
        logger.trace("finished: createContentPane()");
        return contentPane;
    }

    private static void createAndShowGUI() {
        logger.trace("called: createAndShowGUI()");
        if (System.getProperty("os.name").startsWith("Mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.brushMetalLook", "true");
            System.setProperty("apple.awt.showGrowBox", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Script Editor");
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        rootFrame = new JFrame("VolumLab Script Editor");
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ScriptGUI scriptGUI = new ScriptGUI();
        if (System.getProperty("os.name").startsWith("Mac")) {
            logger.trace("Mac OS Found, setting Application Callbacks");
            Application appleApp = Application.getApplication();
            appleApp.addApplicationListener(ApplicationListenerHandler.getApplicationEventListener(scriptGUI));
            appleApp.setEnabledPreferencesMenu(true);
        }
        rootFrame.setContentPane(scriptGUI.createContentPane());
        rootFrame.setJMenuBar(scriptGUI.createMenuBar());
        rootFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closeScript(false);
                config.saveSettings(currentConfigFile);
                System.exit(0);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                script.requestFocus();
            }
        });
        rootFrame.pack();
        rootFrame.setSize(config.xRes, config.yRes);
        rootFrame.setVisible(true);
        if (openFileAfterInit != null) {
            currentScript = Script.openFile(openFileAfterInit);
        }
        logger.trace("finished: createAndShowGUI()");
    }

    public static void wipeClean() {
        try {
            logger.trace("Clearing editor window of all text: " + scriptDoc.getEndPosition().getOffset() + " characters");
            scriptDoc.remove(0, scriptDoc.getEndPosition().getOffset() - 1);
        } catch (javax.swing.text.BadLocationException e) {
            logger.warn("Oops, an exception was thrown while trying to erase the buffer, my bad: " + e);
        }
    }

    public static void appendScript(String s) {
        try {
            int location = ScriptGUI.scriptDoc.getEndPosition().getOffset();
            logger.trace("Attempting to insert string at location: " + location);
            scriptDoc.insertString(location, s, null);
            script.setCaretPosition(location);
            logger.trace("Finished insert string at location: " + location);
        } catch (javax.swing.text.BadLocationException e) {
            logger.error("Oops, calculation gone wrong, looks like the position is not being calculated correctly: Exception: " + e);
        }
    }

    public void insertStartTran() {
        try {
            String tranName = (String) JOptionPane.showInputDialog(this, "Please enter a name for this Transaction:", "New Transaction", JOptionPane.INFORMATION_MESSAGE, null, null, "");
            if (tranName == null || tranName.length() == 0) {
                throw new IllegalArgumentException("Transaction name can not be blank!");
            } else if (tranName.equals("_RunTime_")) {
                throw new IllegalArgumentException("Transaction can not step on _RunTime_ transaction!");
            }
            appendScript("\nstartTransaction(\"" + tranName + "\");\n");
        } catch (Exception e) {
            logger.warn("Error creating start Transaction: Exception: " + e);
        }
    }

    public void insertStopTran() {
        try {
            appendScript("if(stopTransaction()) return;\n\n");
        } catch (Exception e) {
            logger.warn("Error creating stop Transaction: Exception: " + e);
        }
    }

    public void insertSleep() {
        try {
            String secs = (String) JOptionPane.showInputDialog(this, "Please enter sleep time in seconds:", "New Sleep", JOptionPane.INFORMATION_MESSAGE, null, null, "");
            appendScript("sleep(" + new Integer(secs) + ");\n");
        } catch (Exception e) {
            logger.warn("Error creating sleep call: Exception: " + e);
        }
    }

    public void insertVerifyReturn() {
        boolean errorOnFind = false;
        try {
            String text = (String) JOptionPane.showInputDialog(this, "Please enter a search Regular Expression:", "New Verify Return Text", JOptionPane.INFORMATION_MESSAGE, null, null, "");
            appendScript("verifyReturn(\"" + text + "\"" + (errorOnFind == true ? ", errorIfFound" : "") + ");\n");
        } catch (Exception e) {
            logger.warn("Error creating verify returned text call: Exception: " + e);
        }
    }

    public static void outputText(String s) {
        output.append(s);
        output.setCaretPosition(output.getDocument().getLength());
    }

    private void createScript(boolean startRecorder) {
        ScriptPropertiesPanel.makeWindow(null, startRecorder);
    }

    public static void createScriptCallBack(Script newScript, boolean startRecorder) {
        logger.trace("createScriptCallBack called with script: " + newScript.name);
        if (newScript != null) {
            currentScript = newScript;
        }
    }

    public void startRecorderProxy() {
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            if (keyFile == null) {
                try {
                    keyFile = File.createTempFile("proxy", "keystore");
                    keyFile.deleteOnExit();
                    DataInputStream keyIS = new DataInputStream(this.getClass().getResourceAsStream("/com/wpg/proxy-keystore"));
                    FileOutputStream fo = new FileOutputStream(keyFile);
                    byte[] b = new byte[1];
                    while (keyIS.read(b, 0, 1) != -1) {
                        fo.write(b, 0, 1);
                    }
                    keyIS.close();
                    fo.close();
                    logger.debug("proxy key extracted for ssl support");
                } catch (Exception ex) {
                    logger.error("Error creating a temporary file for proxy keystore ssl use: Exception: " + ex, ex);
                    return;
                }
            }
            proxy = new Proxy(java.net.InetAddress.getByName(config.address), config.port, 50, keyFile.getPath(), "password".toCharArray(), "password".toCharArray());
        } catch (java.net.UnknownHostException uhe) {
            logger.warn("Exception initializing proxy: " + uhe);
        } catch (Exception ex) {
            logger.warn("Exception initializing proxy: " + ex);
        }
        if (config.proxy != null) {
            ProxyRegistry.addRequestProcessor(new ProxyMessageProcessor(config.proxy));
        }
        ProxyRegistry.addHandler(currentScript.getPlugin().getProxyRecorder());
        proxy.start();
        logger.info("Recording Proxy started on: " + config.address + ":" + config.port);
    }

    private boolean checkForNullScript(String errorMessage) {
        if (currentScript == null) {
            logger.warn(errorMessage);
            JOptionPane.showMessageDialog(ScriptGUI.rootFrame, errorMessage, "Warning", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    /**
	* actionPerformed(ActionEvent event) is for the ActionListener Interface
	**/
    public void actionPerformed(ActionEvent e) {
        logger.trace("Action detected: Event source: " + e.getActionCommand());
        if (chooser == null) {
            chooser = new JFileChooser(new File(System.getProperty("user.dir")));
            ScriptFilter scriptFilter = new ScriptFilter();
            chooser.addChoosableFileFilter(scriptFilter);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(scriptFilter);
        }
        if (e.getActionCommand().equals("New...")) {
            if (currentScript != null) {
                logger.trace("Current Script is not null, implement the save prompt");
                int response = JOptionPane.showConfirmDialog((Component) null, "Would You Like To Save This Script Before Closing it?", "Save Script Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                logger.trace("Response is: " + response);
                if (response == 0) {
                    currentScript.saveToFile();
                    logger.info("Script saved");
                } else if (response == -1 || response == 2) {
                    logger.info("New Script Cancelled");
                    return;
                } else {
                    logger.info("Do not save, and continue");
                }
            }
            createScript(false);
            return;
        }
        if (e.getActionCommand().equals("Edit Parameters...")) {
            if (checkForNullScript("User attempted to manage parameters for a null script")) return;
            ScriptParamsPanel.makeWindow();
            return;
        }
        if (e.getActionCommand().equals("Search and Replace")) {
            SearchReplacePanel.makeWindow();
            return;
        }
        if (e.getActionCommand().equals("Config Settings")) {
            ConfigSettingsPanel.makeWindow();
            return;
        }
        if (e.getActionCommand().equals("Script Properties...")) {
            if (checkForNullScript("User attempted to manage properties for a null script")) return;
            ScriptPropertiesPanel.makeWindow(currentScript);
            return;
        }
        boolean doSaveAs = false;
        if (e.getActionCommand().equals("Save")) {
            if (checkForNullScript("User attempted to save a null script")) return;
            if (currentScript.jarFileName == null) {
                doSaveAs = true;
            } else {
                currentScript.saveToFile();
                return;
            }
        }
        if (e.getActionCommand().equals("Save As...") || doSaveAs) {
            if (checkForNullScript("User attempted to save a null script")) return;
            int retval = chooser.showSaveDialog(contentPane);
            if (retval == JFileChooser.APPROVE_OPTION) {
                currentScript.saveToFile(chooser.getSelectedFile().getAbsoluteFile().getPath());
            }
            return;
        }
        if (e.getActionCommand().equals("Open...")) {
            int retval = chooser.showOpenDialog(contentPane);
            if (retval == JFileChooser.APPROVE_OPTION) {
                closeScript(false);
                currentScript = Script.openFile(chooser.getSelectedFile().getAbsoluteFile());
            }
            return;
        }
        if (e.getActionCommand().equals("Close Script")) {
            closeScript(false);
            return;
        }
        if (e.getActionCommand().equals("Exit")) {
            closeScript(false);
            System.exit(0);
            return;
        }
        if (e.getActionCommand().equals("Record Script")) {
            if (currentScript == null) {
                createScript(true);
            }
            if (currentScript == null) {
                logger.warn("Please create a new script before recording a user session");
            } else {
                startRecorderProxy();
            }
            return;
        }
        if (e.getActionCommand().equals("Build/Compile")) {
            if (checkForNullScript("Please open or create a new script before trying to compile one")) {
                return;
            }
            if (currentScript.jarFileName == null) {
                int retval = chooser.showSaveDialog(contentPane);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    currentScript.saveToFile(chooser.getSelectedFile().getAbsoluteFile().getPath());
                } else {
                    logger.info("Save Cancelled");
                    return;
                }
            }
            currentScript.compile(false);
            currentScript.saveToFile();
            return;
        }
        if (e.getActionCommand().equals("Run Script")) {
            if (checkForNullScript("Please open or create a new script before trying to run one")) {
                return;
            }
            if (currentScript.jarFileName == null) {
                int retval = chooser.showSaveDialog(contentPane);
                if (retval == JFileChooser.APPROVE_OPTION) {
                    currentScript.saveToFile(chooser.getSelectedFile().getAbsoluteFile().getPath());
                } else {
                    logger.info("Save Cancelled");
                    return;
                }
            }
            if (currentScript.compile(true)) {
                currentScript.saveToFile();
                currentScript.execute();
            }
            return;
        }
        if (e.getActionCommand().equals("Log Viewer")) {
            if (checkForNullScript("Please open or create a new script before trying to view the logs")) {
                return;
            }
            OutputViewerPanel.makeWindow(currentScript);
            return;
        }
        if (e.getActionCommand().equals("Start Transaction")) {
            insertStartTran();
            return;
        }
        if (e.getActionCommand().equals("Stop Transaction")) {
            insertStopTran();
            return;
        }
        if (e.getActionCommand().equals("Sleep Timer")) {
            insertSleep();
            return;
        }
        if (e.getActionCommand().equals("Verify Returned Text")) {
            insertVerifyReturn();
            return;
        }
        if (e.getActionCommand().equals("Stop Script")) {
            if (checkForNullScript("User attempted to stop a null script")) {
                return;
            }
            if (proxy != null) {
                proxy.shutdown();
                proxy = null;
                logger.info("Recording Proxy has been stopped");
            }
            currentScript.stopExecution();
            return;
        }
    }

    public static void closeScript(boolean force) {
        if (currentScript != null && scriptDoc.getLength() > 0 && !force) {
            logger.trace("Current Script is not null, implement the save prompt");
            int response = JOptionPane.showConfirmDialog((Component) null, "Would You Like To Save This Script Before Closing it?", "Save Script Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            logger.trace("Response is: " + response);
            if (response == 0) {
                currentScript.saveToFile();
                currentScript.close();
                logger.info("Script saved");
            } else if (response == -1 || response == 2) {
                logger.info("Close Script Cancelled");
                return;
            } else {
                logger.info("Do not save, and continue");
                currentScript.close();
            }
        }
        wipeClean();
        if (currentScript != null && force) currentScript.close();
        currentScript = null;
    }

    public void itemStateChanged(ItemEvent e) {
    }

    public static void setOpenFileAfterInit(File f) {
        openFileAfterInit = f;
    }

    class Undoer implements UndoableEditListener {

        public Undoer() {
            undo.die();
            updateUndo();
        }

        public void undoableEditHappened(UndoableEditEvent event) {
            UndoableEdit edit = event.getEdit();
            undo.addEdit(edit);
            updateUndo();
        }
    }

    protected void updateUndo() {
        if (undo.canUndo()) {
            undoAction.setEnabled(true);
            undoAction.putValue(Action.NAME, undo.getUndoPresentationName());
        } else {
            if (undoAction != null) {
                undoAction.setEnabled(false);
                undoAction.putValue(Action.NAME, "Undo");
            }
        }
        if (undo.canRedo()) {
            redoAction.setEnabled(true);
            redoAction.putValue(Action.NAME, undo.getRedoPresentationName());
        } else {
            if (redoAction != null) {
                redoAction.setEnabled(false);
                redoAction.putValue(Action.NAME, "Redo");
            }
        }
    }

    /*************************
	* ApplicationListener Interface Code
	**************************/
    public void handleAbout(Object ev) {
    }

    public void handleOpenApplication(Object ev) {
    }

    public void handleOpenFile(Object ev) {
        ApplicationEvent event = new ApplicationEvent(ev);
        logger.debug("Calling handleOpenFile: " + event.getFilename());
        closeScript(false);
        currentScript = Script.openFile(new File(event.getFilename()));
        event.setHandled(true);
    }

    public void handlePreferences(Object ev) {
        logger.debug("Calling handlePreferences");
        ApplicationEvent event = new ApplicationEvent(ev);
        ConfigSettingsPanel.makeWindow();
        event.setHandled(true);
    }

    public void handlePrintFile(Object ev) {
    }

    public void handleQuit(Object ev) {
        logger.debug("Calling handleQuit");
        ApplicationEvent event = new ApplicationEvent(ev);
        closeScript(false);
        event.setHandled(true);
        System.exit(0);
    }

    public void handleReOpenApplication(Object ev) {
    }

    public static void main(String[] args) {
        ScriptGUI scriptTool = new ScriptGUI();
        if (args.length > 0) ScriptGUI.setOpenFileAfterInit(new File(args[0]));
        try {
            config = new Config(currentConfigFile, scriptTool);
        } catch (Exception e) {
            logger.fatal("Error reading the configuration file! Exception: " + e, e);
            System.exit(-1);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if (currentScript != null) currentScript.stopExecution();
                closeScript(true);
                config.saveSettings(currentConfigFile);
                if (proxy != null) {
                    proxy.shutdown();
                    proxy = null;
                    logger.info("Recording Proxy has been stopped");
                }
            }
        });
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    logger.fatal("Error: setting look and feel: " + e);
                }
                createAndShowGUI();
            }
        });
    }
}
