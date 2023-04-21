package org.rubato.composer;

import static org.rubato.xml.XMLConstants.NAME_ATTR;
import static org.rubato.xml.XMLConstants.RUBETTE;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import org.rubato.base.Repository;
import org.rubato.base.Rubette;
import org.rubato.composer.dialogs.JModuleDialog;
import org.rubato.composer.dialogs.JProgress;
import org.rubato.composer.dialogs.denotators.JDenotatorDialog;
import org.rubato.composer.dialogs.forms.JFormDialog;
import org.rubato.composer.dialogs.morphisms.JMorphismDialog;
import org.rubato.composer.dialogs.scheme.JSchemeDialog;
import org.rubato.composer.dialogs.scheme.JSchemeEditor;
import org.rubato.composer.icons.Icons;
import org.rubato.composer.network.*;
import org.rubato.composer.objectbrowser.ObjectBrowser;
import org.rubato.composer.preferences.JPreferences;
import org.rubato.composer.preferences.UserPreferences;
import org.rubato.composer.rubette.JRubette;
import org.rubato.composer.rubette.RubetteModel;
import org.rubato.rubettes.builtin.MacroRubette;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;

/**
 * The main window of Rubato Composer.
 *  
 * @author Gérard Milmeister
 */
public class JComposer extends JFrame implements Observer {

    public JComposer() {
        updateTitle(false);
        setJMenuBar(createMenuBar());
        JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane secondSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(createToolBar(), BorderLayout.NORTH);
        secondSplitPane.setRightComponent(createRightPanel());
        secondSplitPane.setLeftComponent(createCenterPanel());
        secondSplitPane.setResizeWeight(1.0);
        secondSplitPane.setContinuousLayout(true);
        secondSplitPane.setOneTouchExpandable(true);
        topSplitPane.setTopComponent(secondSplitPane);
        topSplitPane.setBottomComponent(createBottomPanel());
        topSplitPane.setResizeWeight(1.0);
        topSplitPane.setContinuousLayout(true);
        topSplitPane.setOneTouchExpandable(true);
        add(topSplitPane, BorderLayout.CENTER);
        setEnabledAll(true);
    }

    /**
     * Sets the current Rubette manager.
     */
    public void setRubetteManager(RubetteManager manager) {
        this.manager = manager;
    }

    /**
     * Returns the current Rubette manager.
     */
    public RubetteManager getRubetteManager() {
        return manager;
    }

    /**
     * Adds the list of AbstractRubettes to the JRubetteList.
     */
    public void addRubettePrototypes(List<Rubette> rubettes) {
        for (Rubette rubette : rubettes) {
            addRubettePrototype(rubette);
        }
    }

    /**
     * Adds the specified AbstractRubette to the JRubetteList.
     */
    public void addRubettePrototype(Rubette rubette) {
        rubette.init();
        rubetteList.addRubette(rubette);
    }

    /**
     * Removes the specified AbstractRubette from the JRubetteList.
     */
    public void removeRubettePrototype(Rubette rubette) {
        rubetteList.removeRubette(rubette);
    }

    private final Insets insets = new Insets(3, 3, 3, 3);

    private void addToolBarButton(JToolBar toolBar, Action action) {
        JButton b = new JButton(action);
        b.setText(null);
        b.setMargin(insets);
        toolBar.add(b);
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        addToolBarButton(toolBar, newAction);
        addToolBarButton(toolBar, openAction);
        addToolBarButton(toolBar, addAction);
        addToolBarButton(toolBar, saveAction);
        addToolBarButton(toolBar, saveasAction);
        toolBar.addSeparator();
        addToolBarButton(toolBar, newNetworkAction);
        addToolBarButton(toolBar, closeNetworkAction);
        toolBar.addSeparator();
        addToolBarButton(toolBar, runAction);
        contToggleButton = new JToggleButton(Icons.runContIcon);
        contToggleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                toggleContinuousRun();
            }
        });
        contToggleButton.setSelected(contRun);
        contToggleButton.setText(null);
        contToggleButton.setMargin(insets);
        toolBar.add(contToggleButton);
        addToolBarButton(toolBar, stopAction);
        toolBar.addSeparator();
        addToolBarButton(toolBar, moduleAction);
        addToolBarButton(toolBar, moduleMorphismAction);
        addToolBarButton(toolBar, denotatorAction);
        addToolBarButton(toolBar, formAction);
        addToolBarButton(toolBar, schemeAction);
        addToolBarButton(toolBar, schemeEditAction);
        addToolBarButton(toolBar, prefAction);
        toolBar.addSeparator();
        addToolBarButton(toolBar, quitAction);
        return toolBar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(Messages.getString("JComposer.file"));
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem newItem = new JMenuItem(newAction);
        newItem.setMnemonic(KeyEvent.VK_N);
        fileMenu.add(newItem);
        allActions.add(newAction);
        JMenuItem openItem = new JMenuItem(openAction);
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        fileMenu.add(openItem);
        allActions.add(openAction);
        JMenuItem addItem = new JMenuItem(addAction);
        addItem.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(addItem);
        allActions.add(addAction);
        JMenuItem revertItem = new JMenuItem(revertAction);
        revertItem.setMnemonic(KeyEvent.VK_R);
        fileMenu.add(revertItem);
        allActions.add(revertAction);
        JMenuItem saveItem = new JMenuItem(saveAction);
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        fileMenu.add(saveItem);
        allActions.add(saveAction);
        JMenuItem saveasItem = new JMenuItem(saveasAction);
        saveasItem.setMnemonic(KeyEvent.VK_A);
        saveasItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
        fileMenu.add(saveasItem);
        allActions.add(saveasAction);
        fileMenu.addSeparator();
        JMenu recentFilesItem = new JMenu("Recent Files");
        fileMenu.add(recentFilesItem);
        recentFiles = new RecentFiles(recentFilesItem);
        recentFiles.setAction(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (loseProject()) {
                    open(recentFiles.getSelectedFile());
                }
            }
        });
        fileMenu.addSeparator();
        JMenuItem quitItem = new JMenuItem(quitAction);
        quitItem.setMnemonic(KeyEvent.VK_Q);
        quitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        fileMenu.add(quitItem);
        allActions.add(quitAction);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        final JMenuItem cutItem = new JMenuItem(cutAction);
        cutItem.setMnemonic(KeyEvent.VK_T);
        cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        editMenu.add(cutItem);
        allActions.add(cutAction);
        final JMenuItem copyItem = new JMenuItem(copyAction);
        copyItem.setMnemonic(KeyEvent.VK_C);
        copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        editMenu.add(copyItem);
        allActions.add(copyAction);
        final JMenuItem pasteItem = new JMenuItem(pasteAction);
        pasteItem.setMnemonic(KeyEvent.VK_P);
        pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        editMenu.add(pasteItem);
        allActions.add(pasteAction);
        editMenu.addMenuListener(new MenuListener() {

            public void menuCanceled(MenuEvent e) {
                cutAction.setEnabled(true);
                copyAction.setEnabled(true);
                pasteAction.setEnabled(true);
            }

            public void menuDeselected(MenuEvent e) {
                cutAction.setEnabled(true);
                copyAction.setEnabled(true);
                pasteAction.setEnabled(true);
            }

            public void menuSelected(MenuEvent e) {
                updateEditMenu();
            }
        });
        menuBar.add(editMenu);
        JMenu rubMenu = new JMenu(Messages.getString("JComposer.rubette"));
        rubMenu.setMnemonic(KeyEvent.VK_R);
        JMenuItem addRubetteItem = new JMenuItem(addRubetteAction);
        addRubetteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
        addRubetteItem.setMnemonic(KeyEvent.VK_A);
        rubMenu.add(addRubetteItem);
        allActions.add(addRubetteAction);
        menuBar.add(rubMenu);
        JMenu netMenu = new JMenu(Messages.getString("JComposer.network"));
        netMenu.setMnemonic(KeyEvent.VK_N);
        JMenuItem newNetItem = new JMenuItem(newNetworkAction);
        newNetItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newNetItem.setMnemonic(KeyEvent.VK_N);
        netMenu.add(newNetItem);
        allActions.add(newNetworkAction);
        JMenuItem closeNetItem = new JMenuItem(closeNetworkAction);
        closeNetItem.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        closeNetItem.setMnemonic(KeyEvent.VK_C);
        netMenu.add(closeNetItem);
        allActions.add(closeNetworkAction);
        JMenuItem runItem = new JMenuItem(runAction);
        runItem.setAccelerator(KeyStroke.getKeyStroke("F9"));
        runItem.setMnemonic(KeyEvent.VK_R);
        netMenu.add(runItem);
        allActions.add(runAction);
        JMenuItem stopItem = new JMenuItem(stopAction);
        stopItem.setAccelerator(KeyStroke.getKeyStroke("F10"));
        stopItem.setMnemonic(KeyEvent.VK_S);
        netMenu.add(stopItem);
        allActions.add(stopAction);
        menuBar.add(netMenu);
        JMenu toolsMenu = new JMenu(Messages.getString("JComposer.tools"));
        toolsMenu.setMnemonic(KeyEvent.VK_T);
        JMenuItem moduleItem = new JMenuItem(moduleAction);
        moduleItem.setAccelerator(KeyStroke.getKeyStroke("alt shift M"));
        moduleItem.setMnemonic(KeyEvent.VK_O);
        toolsMenu.add(moduleItem);
        allActions.add(moduleAction);
        JMenuItem moduleMorphismItem = new JMenuItem(moduleMorphismAction);
        moduleMorphismItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift M"));
        moduleMorphismItem.setMnemonic(KeyEvent.VK_M);
        toolsMenu.add(moduleMorphismItem);
        allActions.add(moduleMorphismAction);
        JMenuItem denotatorItem = new JMenuItem(denotatorAction);
        denotatorItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift D"));
        denotatorItem.setMnemonic(KeyEvent.VK_D);
        toolsMenu.add(denotatorItem);
        allActions.add(denotatorAction);
        JMenuItem formItem = new JMenuItem(formAction);
        formItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift F"));
        formItem.setMnemonic(KeyEvent.VK_F);
        toolsMenu.add(formItem);
        allActions.add(formAction);
        JMenuItem browseItem = new JMenuItem(browseAction);
        browseItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift B"));
        browseItem.setMnemonic(KeyEvent.VK_B);
        toolsMenu.add(browseItem);
        allActions.add(browseAction);
        JMenuItem schemeItem = new JMenuItem(schemeAction);
        schemeItem.setAccelerator(KeyStroke.getKeyStroke("alt shift S"));
        schemeItem.setMnemonic(KeyEvent.VK_S);
        toolsMenu.add(schemeItem);
        allActions.add(schemeAction);
        JMenuItem schemeEditItem = new JMenuItem(schemeEditAction);
        schemeEditItem.setMnemonic(KeyEvent.VK_E);
        toolsMenu.add(schemeEditItem);
        allActions.add(schemeEditAction);
        JMenuItem prefItem = new JMenuItem(prefAction);
        prefItem.setMnemonic(KeyEvent.VK_P);
        toolsMenu.add(prefItem);
        allActions.add(prefAction);
        menuBar.add(toolsMenu);
        JMenu helpMenu = new JMenu(Messages.getString("JComposer.help"));
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem aboutItem = new JMenuItem(aboutAction);
        aboutItem.setMnemonic(KeyEvent.VK_A);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        bottomPanel.setLayout(new BorderLayout());
        statusBar = new JLabel(" ");
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        bottomPanel.add(statusBar, BorderLayout.SOUTH);
        bottomTabs = new JTabbedPane();
        bottomPanel.add(bottomTabs, BorderLayout.CENTER);
        problemList = new JProblemList();
        bottomTabs.addTab(Messages.getString("JComposer.problems"), problemList);
        messageLog = new JMessageLog();
        bottomTabs.addTab(Messages.getString("JComposer.messagelog"), messageLog);
        bottomPanel.setPreferredSize(new Dimension(0, 150));
        return bottomPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        rightPanel.setLayout(new BorderLayout());
        rightTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        rightPanel.add(rightTabs, BorderLayout.CENTER);
        rubetteList = new JRubetteList(this);
        rubetteList.addAddButtonAction(addRubetteAction);
        rightTabs.addTab(Messages.getString("JComposer.rubettes"), rubetteList);
        networkList = new JNetworkList();
        networkList.addNewButtonAction(newNetworkAction);
        networkList.setShowAction(showNetworkAction);
        rightTabs.addTab(Messages.getString("JComposer.networks"), networkList);
        rightPanel.setPreferredSize(new Dimension(200, 0));
        return rightPanel;
    }

    private JComponent createCenterPanel() {
        networkContainer = new JNetworkContainer(this, networkList);
        return networkContainer;
    }

    protected void updateEditMenu() {
        JNetwork jnetwork = getCurrentJNetwork();
        if (jnetwork == null) {
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
        } else {
            boolean hasSelection = jnetwork.getSelection() != null;
            cutAction.setEnabled(hasSelection);
            copyAction.setEnabled(hasSelection);
            pasteAction.setEnabled(networkClip != null);
        }
    }

    /**
     * Returns the list showing all Rubettes.
     */
    public JRubetteList getJRubetteList() {
        return rubetteList;
    }

    /**
     * Shows an error dialog with the specified message.
     * Also displays the message in the status bar. 
     */
    public void showErrorDialog(String msg, Object... objects) {
        msg = TextUtils.replaceStrings(msg, objects);
        Frame frame = JOptionPane.getFrameForComponent(this);
        setStatusError(msg, objects);
        JOptionPane.showMessageDialog(frame, msg, TITLE_STRING + ": " + Messages.getString("JComposer.error"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an error message in the status bar.
     */
    public void setStatusError(String msg, Object... objects) {
        setStatus(TextUtils.replaceStrings(msg, objects), STATUS_ERROR);
        bottomTabs.setSelectedIndex(1);
    }

    /**
     * Displays a warning message in the status bar.
     */
    public void setStatusWarning(String msg, Object... objects) {
        setStatus(TextUtils.replaceStrings(msg, objects), STATUS_WARNING);
    }

    /**
     * Displays an information message in the status bar.
     */
    public void setStatusInfo(String msg, Object... objects) {
        setStatus(TextUtils.replaceStrings(msg, objects), STATUS_INFO);
    }

    /**
     * Sets the status bar to <code>msg</code>.
     * @param msg the message to show in the status bar
     * @param type the kind of message, STATUS_INFO, STATUS_ERROR or STATUS_WARNING
     */
    public void setStatus(String msg, int type) {
        if (statusTimer != null && statusTimer.isRunning()) {
            statusTimer.stop();
        }
        if (type >= 0 && type < statusColor.length) {
            statusBar.setForeground(statusColor[type]);
        } else {
            statusBar.setForeground(statusColor[STATUS_INFO]);
        }
        statusBar.setText(msg);
        messageLog.addMessage(msg, type);
        statusTimer.start();
    }

    /**
     * Clears the status bar.
     */
    public void clearStatus() {
        statusBar.setText(" ");
    }

    /**
     * Updates title of the main window.
     * @param changed true iff the content has changed
     */
    private void updateTitle(boolean changed) {
        if (currentFile == null) {
            setTitle(TITLE_STRING + (changed ? "*" : ""));
        } else {
            setTitle(TITLE_STRING + ": " + currentFile.getName() + (changed ? "*" : ""));
        }
    }

    /**
     * Discards the current project.
     * Prompts the user to save current project if not empty.
     */
    protected void newProject() {
        if (!loseProject()) {
            return;
        }
        discardProject();
        setStatusInfo(Messages.getString("JComposer.newprojectcreated"));
    }

    /**
     * Discards the current project.
     * All networks and user created Rubettes are removed.
     */
    private void discardProject() {
        networkContainer.clear();
        manager.clear();
        rep.initGlobalRepository();
        for (Rubette rubette : manager.getRubettes()) {
            rubette.init();
        }
        currentFile = null;
        setChanged(false);
    }

    /**
     * Shows dialog asking the user if he wants to save current project.
     * Saves the project according to answer.
     * @return true iff the current project can be discarded
     */
    public boolean loseProject() {
        if (hasChanged && !networkContainer.isEmpty()) {
            int i = JOptionPane.showConfirmDialog(this, Messages.getString("JComposer.dosaveproject"));
            if (i == JOptionPane.OK_OPTION) {
                save();
                return true;
            } else if (i == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Opens a project file replacing the current project.
     * Prompts the user to save if the current project has changed.
     * Prompts the user for the file to load.
     */
    protected void open() {
        if (!loseProject()) {
            return;
        }
        if (fileChooser == null) {
            createFileChooser();
        }
        fileChooser.setCurrentDirectory(getCurrentDirectory());
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            open(selectedFile);
        }
    }

    /**
     * Opens a project file replacing the current project.
     * Prompts the user to save if the current project has changed.
     */
    public void open(File file) {
        Cursor cursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            try {
                file = file.getCanonicalFile();
            } catch (IOException e) {
                logger.warning("Could not get canonical filename of " + file);
                return;
            }
            XMLReader reader = new XMLReader(file);
            reader.parse();
            if (reader.hasError()) {
                for (String error : reader.getErrors()) {
                    setStatusError(error);
                }
                showErrorDialog(Messages.getString("JComposer.couldnotloadfile"), file.getName());
            } else {
                discardProject();
                rep.setSchemeCode(reader.getSchemeCode());
                for (String name : reader.getModuleNames()) {
                    rep.registerModule(name, reader.getModule(name));
                }
                for (String name : reader.getModuleElementNames()) {
                    rep.registerModuleElement(name, reader.getModuleElement(name));
                }
                for (String name : reader.getModuleMorphismNames()) {
                    rep.registerModuleMorphism(name, reader.getModuleMorphism(name));
                }
                if (!rep.register(reader.getForms(), reader.getDenotators())) {
                    setStatusError(Messages.getString("JComposer.couldnotregister"));
                    return;
                }
                for (Rubette arubette : reader.getRubettes()) {
                    manager.addRubette(arubette);
                }
                for (NetworkModel model : reader.getNetworks()) {
                    JNetwork jnetwork = model.createJNetwork(this);
                    if (jnetwork != null) {
                        addJNetwork(jnetwork, model.getName());
                    } else {
                        setStatusError(Messages.getString("JComposer.couldnotaddnetwork"), model.getName());
                    }
                }
                currentFile = file;
                setCurrentDirectory(file.getParentFile());
                if (fileChooser == null) {
                    createFileChooser();
                }
                fileChooser.setCurrentDirectory(getCurrentDirectory());
                messageLog.clearMessages();
                setStatusInfo(Messages.getString("JComposer.projectedfileloaded"), currentFile.getName());
                setChanged(false);
                recentFiles.activate(file);
            }
        } catch (IOException e) {
            logger.warning(TextUtils.replaceStrings("Could not load file", file.getAbsoluteFile()));
            setStatusError(Messages.getString("JComposer.filenotfound"), file.getName());
        }
        setCursor(cursor);
    }

    /**
     * Adds definitions (rubettes, networks, denotators, forms) from file.
     * Prompts the user for the file to load.
     */
    protected void addDefinitions() {
        if (fileChooser == null) {
            createFileChooser();
        }
        fileChooser.setCurrentDirectory(getCurrentDirectory());
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Reader r = new FileReader(selectedFile);
                XMLReader reader = new XMLReader(r);
                reader.parse();
                if (reader.hasError()) {
                    for (String error : reader.getErrors()) {
                        System.out.println(error);
                    }
                } else {
                    if (!rep.register(reader.getForms(), reader.getDenotators())) {
                        setStatusError("Could not register forms and denotators.");
                        return;
                    }
                    for (NetworkModel model : reader.getNetworks()) {
                        JNetwork jnetwork = model.createJNetwork(this);
                        if (jnetwork != null) {
                            addJNetwork(jnetwork, model.getName());
                        } else {
                            setStatusError(Messages.getString("JComposer.couldnotaddnetwork"), model.getName());
                        }
                    }
                    setCurrentDirectory(fileChooser.getCurrentDirectory());
                    setChanged(true);
                    setStatusInfo(Messages.getString("JComposer.definitionsloaded"), selectedFile.getName());
                }
            } catch (FileNotFoundException e) {
                setStatusError(Messages.getString("JComposer.filenotfound"), selectedFile.getName());
            }
        }
    }

    /**
     * Reverts to the file stored on disk.
     */
    protected void revert() {
        if (currentFile != null) {
            open(currentFile);
        }
    }

    /**
     * Saves the current project to disk.
     * Asks if the current project has changed.
     */
    protected void save() {
        if (currentFile == null) {
            saveas();
        } else {
            try {
                XMLWriter writer = new XMLWriter(currentFile, true);
                writer.open();
                rep.toXML(writer);
                for (Rubette arubette : manager.getRubettes()) {
                    if (arubette instanceof MacroRubette) {
                        String name = arubette.getName();
                        writer.openBlock(RUBETTE, NAME_ATTR, name);
                        arubette.toXML(writer);
                        writer.closeBlock();
                    }
                }
                networkContainer.toXML(writer);
                writer.close();
                setChanged(false);
                recentFiles.activate(currentFile);
            } catch (IOException e) {
                setStatusError(Messages.getString("JComposer.couldnotsavefile"), currentFile.getName());
            }
        }
    }

    /**
     * Saves the current project to disk under a new name.
     * Asks if the current project has changed.
     */
    protected void saveas() {
        if (fileChooser == null) {
            createFileChooser();
        }
        fileChooser.setCurrentDirectory(getCurrentDirectory());
        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(DOT_EXTENSION)) {
                try {
                    selectedFile = new File(selectedFile.getCanonicalPath() + DOT_EXTENSION);
                } catch (IOException e) {
                }
            }
            if (selectedFile.exists()) {
                String text = TextUtils.replaceStrings(Messages.getString("JComposer.wantoverwrite"), selectedFile.getName());
                int i = JOptionPane.showConfirmDialog(this, text);
                if (i != JOptionPane.OK_OPTION) {
                    return;
                }
            }
            currentFile = selectedFile;
            setCurrentDirectory(fileChooser.getCurrentDirectory());
            save();
        }
    }

    /**
     * Leaves Rubato Composer.
     */
    public void quit() {
        if (hasChanged && userPrefs.getAskBeforeLeaving() && !loseProject()) {
            return;
        }
        if (userPrefs.getGeometrySaved()) {
            Rectangle r = getBounds();
            userPrefs.setGeometry(r.x, r.y, r.width, r.height);
        }
        logger.info("Stopping Rubato Composer");
        System.exit(0);
    }

    /**
     * Returns the current directory.
     */
    public File getCurrentDirectory() {
        if (currentDirectory == null) {
            currentDirectory = userPrefs.getCurrentDirectory();
        }
        return currentDirectory;
    }

    /**
     * Sets the current directory to <code>file</code>.
     * Additionally the user preference for current directory is set.
     */
    public void setCurrentDirectory(File file) {
        currentDirectory = file;
        userPrefs.setCurrentDirectory(currentDirectory);
    }

    /**
     * Adds the Rubette selected in the Rubette list to the visible JNetwork.
     */
    public void addJRubette() {
        JNetwork jnetwork = getCurrentJNetwork();
        if (jnetwork == null) {
            setStatusError(Messages.getString("JComposer.novisiblenetwork"));
            return;
        }
        JRubette jrubette = rubetteList.getCurrentRubette();
        if (jrubette == null) {
            setStatusError(Messages.getString("JComposer.norubetteselected"));
            return;
        }
        addJRubette(jnetwork, jrubette);
    }

    /**
     * Creates a new Rubette from <code>rubette</code> in <code>jnetwork</code>.
     */
    public void addJRubette(JNetwork jnetwork, Rubette rubette) {
        addJRubette(jnetwork, rubetteList.createJRubette(rubette));
    }

    /**
     * Adds the Rubette selected in the list to the visible JNetwork.
     */
    public void addJRubette(JNetwork jnetwork, JRubette jrubette) {
        if (!jnetwork.canAdd(jrubette)) {
            return;
        }
        JViewport viewport = ((JViewport) jnetwork.getParent());
        Point loc = viewport.getViewPosition();
        Dimension extent = viewport.getExtentSize();
        int xr = (int) ((Math.random() * 2 - 1) * 30);
        int yr = (int) ((Math.random() * 2 - 1) * 30);
        int x = loc.x + extent.width / 2 - jrubette.getWidth() / 2 + xr;
        int y = loc.y + extent.height / 2 - jrubette.getHeight() / 2 + yr;
        jrubette.moveJRubette(x, y);
        getCurrentJNetwork().addRubette(jrubette);
        setChanged(true);
    }

    /**
     * Creates a new JNetwork.
     */
    public void newJNetwork() {
        JNetwork jnetwork = networkContainer.newJNetwork();
        setChanged(true);
        setStatusInfo(Messages.getString("JComposer.networkadded"), jnetwork.getName());
    }

    /**
     * Adds <code>jnetwork</code> and give it the specified name.
     */
    private void addJNetwork(JNetwork jnetwork, String name) {
        networkContainer.addJNetwork(jnetwork, name);
        setChanged(true);
    }

    /**
     * Creates a new MacroRubette view based on the specified model.
     * @param networkModel the model to base the view on
     * @param jnetwork the JNetwork that the MacroRubette resides in
     */
    public void addJMacroRubetteView(NetworkModel networkModel, JNetwork jnetwork) {
        networkContainer.addJMacroRubetteView(networkModel, jnetwork);
    }

    /**
     * Removes the visible JNetwork.
     */
    public void removeJNetwork() {
        JNetwork jnetwork = getCurrentJNetwork();
        if (jnetwork != null) {
            removeJNetwork(jnetwork);
        }
    }

    /**
     * Removes the specified JNetwork.
     */
    public void removeJNetwork(JNetwork jnetwork) {
        String name = jnetwork.getName();
        networkContainer.removeJNetwork(jnetwork);
        setChanged(true);
        setStatusInfo(Messages.getString("JComposer.networkremoved"), name);
    }

    /**
     * Removes the JNetwork that has the specified NetworkModel.
     */
    public void removeJNetworkForModel(NetworkModel networkModel) {
        networkContainer.removeJNetworkForModel(networkModel);
    }

    /**
     * Makes the specified JNetwork visible.
     */
    protected void showJNetwork(JNetwork jnetwork) {
        networkContainer.showJNetwork(jnetwork);
    }

    /**
     * Gives the specified JNetwork visible a new name.
     */
    public void renameJNetwork(JNetwork jnetwork, String name) {
        networkContainer.renameJNetwork(jnetwork, name);
        setChanged(true);
    }

    /**
     * Update all JNetworks.
     * Essentially updates their titles.
     */
    public void refreshNetworks() {
        networkContainer.refresh();
    }

    /**
     * Returns the currently visible JNetwork.
     */
    protected JNetwork getCurrentJNetwork() {
        return networkContainer.getCurrentJNetwork();
    }

    /**
     * Enables/disables the controls.
     * Controls, expect for the stop button, are disabled during running.
     */
    private void setEnabledAll(boolean b) {
        for (Action action : allActions) {
            action.setEnabled(b);
        }
        stopAction.setEnabled(!b);
    }

    /**
     * Finish the current running.
     * Problems are added to the problem list and buttons are reenabled.
     */
    public void finishRun() {
        problemList.addProblems(runner.getProblems());
        running = false;
        showProgressWindow(false);
        logger.info("Finished running network");
        setEnabledAll(true);
        setCursor(cursor);
    }

    /**
     * Starts running of the currently visible JNetwork.
     * Dependencies are computed, the problem list is cleared
     * and buttons, except the stop button, are disabled.
     * A new Runner process is created.
     */
    public void startRun() {
        cursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        JNetwork network = getCurrentJNetwork();
        if (network != null) {
            network.computeDependencyTree();
            if (runner == null) {
                runner = new Runner(this);
            }
            problemList.clearProblems();
            runner.setNetwork(network.getModel());
            setEnabledAll(false);
            running = true;
            resetProgressWindow(0);
            showProgressWindow(true);
            logger.info("Started to run network");
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    runner.start();
                }
            });
        }
    }

    /**
     * Runs the rubettes in <code>runList</code> in the
     * given order.
     */
    public void startPartialRun(ArrayList<RubetteModel> runList) {
        if (contRun) {
            JNetwork network = getCurrentJNetwork();
            if (network != null) {
                cursor = getCursor();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (runner == null) {
                    runner = new Runner(this);
                }
                problemList.clearProblems();
                runner.setList(network.getModel(), runList);
                setEnabledAll(false);
                running = true;
                resetProgressWindow(0);
                showProgressWindow(true);
                logger.info("Started to partially run network");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        runner.start();
                    }
                });
            }
        }
    }

    protected void toggleContinuousRun() {
        contRun = !contRun;
        contToggleButton.setSelected(contRun);
    }

    /**
     * Returns true iff there is a network currently running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Removes all problems for the given JRubette from the problem list.
     */
    public void removeProblemsFor(JRubette jrubette) {
        problemList.removeProblemsFor(jrubette);
    }

    public void showModuleBuilder() {
        if (moduleBuilder == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            moduleBuilder = new JModuleDialog(frame, false, true);
            moduleBuilder.setLocationRelativeTo(this);
        } else {
            moduleBuilder.reset();
        }
        moduleBuilder.setVisible(true);
    }

    public void showModuleMorphismBuilder() {
        if (moduleMorphismBuilder == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            moduleMorphismBuilder = new JMorphismDialog(frame, false, true, null, null);
            moduleMorphismBuilder.setLocationRelativeTo(this);
        } else {
            moduleMorphismBuilder.reset();
        }
        moduleMorphismBuilder.setVisible(true);
    }

    public void showDenotatorBuilder() {
        if (denotatorBuilder == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            denotatorBuilder = new JDenotatorDialog(frame, false, true);
            denotatorBuilder.setLocationRelativeTo(this);
        } else {
            denotatorBuilder.reset();
        }
        denotatorBuilder.setVisible(true);
    }

    public void showFormBuilder() {
        if (formBuilder == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            formBuilder = new JFormDialog(frame, false, true);
            formBuilder.setLocationRelativeTo(this);
        } else {
            formBuilder.reset();
        }
        formBuilder.setVisible(true);
    }

    public void showObjectBrowser() {
        if (objectBrowser == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            objectBrowser = new ObjectBrowser(frame);
            objectBrowser.setLocationRelativeTo(this);
        } else {
            objectBrowser.reset();
        }
        objectBrowser.setVisible(true);
    }

    public void showSchemeDialog() {
        if (schemeDialog == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            schemeDialog = new JSchemeDialog(frame);
            schemeDialog.setLocationRelativeTo(this);
        }
        schemeDialog.setVisible(true);
    }

    public void showSchemeEditor() {
        if (schemeEditor == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            schemeEditor = new JSchemeEditor(frame);
            schemeEditor.setLocationRelativeTo(this);
        }
        schemeEditor.setVisible(true);
    }

    private void createProgressWindow() {
        if (progress == null) {
            Frame frame = JOptionPane.getFrameForComponent(this);
            progress = new JProgress(frame, this);
            progress.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }
    }

    public void showProgressWindow(boolean show) {
        if (userPrefs.getShowProgress()) {
            createProgressWindow();
            progress.setTitle("Running network " + getCurrentJNetwork().getName());
            progress.setLocationRelativeTo(this);
            progress.setVisible(show);
        }
    }

    public void resetProgressWindow(int max) {
        if (userPrefs.getShowProgress()) {
            createProgressWindow();
            progress.reset(max);
        }
    }

    public void makeProgress(int value) {
        if (userPrefs.getShowProgress()) {
            progress.makeProgress(value);
        }
    }

    public void addProgressMessage(String msg) {
        if (userPrefs.getShowProgress()) {
            progress.addMessage(msg);
        }
    }

    public void update(Observable o, Object arg) {
        setChanged(true);
    }

    public void setChanged(boolean b) {
        hasChanged = b;
        updateTitle(b);
    }

    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }

    private void createFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".rbo");
            }

            public String getDescription() {
                return Messages.getString("JComposer.rubatofiles");
            }
        });
    }

    private abstract class ComposerAction extends AbstractAction {

        public ComposerAction(String name, ImageIcon icon, String tooltip) {
            super(name, icon);
            putValue(Action.SHORT_DESCRIPTION, tooltip);
        }
    }

    private Action newAction = new ComposerAction(Messages.getString("JComposer.new"), Icons.newIcon, Messages.getString("JComposer.newproject")) {

        public void actionPerformed(ActionEvent e) {
            newProject();
        }
    };

    private Action openAction = new ComposerAction(Messages.getString("JComposer.open"), Icons.openIcon, Messages.getString("JComposer.openproject")) {

        public void actionPerformed(ActionEvent e) {
            open();
        }
    };

    private Action addAction = new ComposerAction(Messages.getString("JComposer.adddefinitions"), Icons.addIcon, Messages.getString("JComposer.adddefinitionsfile")) {

        public void actionPerformed(ActionEvent e) {
            addDefinitions();
        }
    };

    private Action revertAction = new ComposerAction(Messages.getString("JComposer.revert"), Icons.revertIcon, Messages.getString("JComposer.reverttofile")) {

        public void actionPerformed(ActionEvent e) {
            revert();
        }
    };

    private Action saveAction = new ComposerAction(Messages.getString("JComposer.save"), Icons.saveIcon, "Save project") {

        public void actionPerformed(ActionEvent e) {
            save();
        }
    };

    private Action saveasAction = new ComposerAction(Messages.getString("JComposer.saveas"), Icons.saveasIcon, Messages.getString("JComposer.saveasnewfile")) {

        public void actionPerformed(ActionEvent e) {
            saveas();
        }
    };

    private Action quitAction = new ComposerAction(Messages.getString("JComposer.quit"), Icons.quitIcon, Messages.getString("JComposer.leavecomposer")) {

        public void actionPerformed(ActionEvent e) {
            quit();
        }
    };

    protected Action cutAction = new ComposerAction("Cut", Icons.cutIcon, "Cut") {

        public void actionPerformed(ActionEvent e) {
            if (getCurrentJNetwork() != null) {
                getCurrentJNetwork().cutSelection();
            }
        }
    };

    protected Action copyAction = new ComposerAction("Copy", Icons.copyIcon, "Copy") {

        public void actionPerformed(ActionEvent e) {
            if (getCurrentJNetwork() != null) {
                NetworkClip clip = getCurrentJNetwork().copy();
                if (clip != null) {
                    networkClip = clip;
                }
            }
        }
    };

    protected Action pasteAction = new ComposerAction("Paste", Icons.pasteIcon, "Paste") {

        public void actionPerformed(ActionEvent e) {
            if (getCurrentJNetwork() != null && networkClip != null) {
                getCurrentJNetwork().paste(networkClip);
            }
        }
    };

    private Action addRubetteAction = new ComposerAction(Messages.getString("JComposer.addrubette"), null, Messages.getString("JComposer.addnewinstance")) {

        public void actionPerformed(ActionEvent e) {
            addJRubette();
        }
    };

    private Action newNetworkAction = new ComposerAction(Messages.getString("JComposer.newnetwork"), Icons.newnetIcon, Messages.getString("JComposer.createnewnetwork")) {

        public void actionPerformed(ActionEvent e) {
            newJNetwork();
        }
    };

    private Action closeNetworkAction = new ComposerAction(Messages.getString("JComposer.closenetwork"), Icons.closeIcon, Messages.getString("JComposer.closediscardnetwork")) {

        public void actionPerformed(ActionEvent e) {
            JNetwork jnetwork = getCurrentJNetwork();
            if (jnetwork != null && !jnetwork.isEmpty()) {
                int res = JOptionPane.showOptionDialog(JComposer.this, Messages.getString("JComposer.shouldclosenetwork"), Messages.getString("JComposer.closenetworktitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (res != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            removeJNetwork();
        }
    };

    private Action showNetworkAction = new ComposerAction(Messages.getString("JComposer.shownetwork"), null, Messages.getString("JComposer.shownetwork")) {

        public void actionPerformed(ActionEvent e) {
            showJNetwork(networkList.getCurrentNetwork());
        }
    };

    private Action runAction = new ComposerAction(Messages.getString("JComposer.run"), Icons.runIcon, Messages.getString("JComposer.runnetwork")) {

        public void actionPerformed(ActionEvent e) {
            startRun();
        }
    };

    private Action stopAction = new ComposerAction(Messages.getString("JComposer.stop"), Icons.stopIcon, Messages.getString("JComposer.stopnetwork")) {

        public void actionPerformed(ActionEvent e) {
            runner.stop();
        }
    };

    private Action moduleAction = new ComposerAction(Messages.getString("JComposer.modulebuilder"), Icons.moduleIcon, Messages.getString("JComposer.createmodules")) {

        public void actionPerformed(ActionEvent e) {
            showModuleBuilder();
        }
    };

    private Action moduleMorphismAction = new ComposerAction(Messages.getString("JComposer.morphismbuilder"), Icons.morphIcon, Messages.getString("JComposer.createmorphisms")) {

        public void actionPerformed(ActionEvent e) {
            showModuleMorphismBuilder();
        }
    };

    private Action denotatorAction = new ComposerAction(Messages.getString("JComposer.denotatorbuilder"), Icons.denoIcon, Messages.getString("JComposer.createdenotators")) {

        public void actionPerformed(ActionEvent e) {
            showDenotatorBuilder();
        }
    };

    private Action formAction = new ComposerAction(Messages.getString("JComposer.formbuilder"), Icons.formIcon, Messages.getString("JComposer.createforms")) {

        public void actionPerformed(ActionEvent e) {
            showFormBuilder();
        }
    };

    private Action browseAction = new ComposerAction(Messages.getString("JComposer.objectbrowser"), Icons.browseIcon, Messages.getString("JComposer.browseobjects")) {

        public void actionPerformed(ActionEvent e) {
            showObjectBrowser();
        }
    };

    private Action schemeAction = new ComposerAction(Messages.getString("JComposer.schemeinteraction"), Icons.schemeIcon, Messages.getString("JComposer.openschemeinteraction")) {

        public void actionPerformed(ActionEvent e) {
            showSchemeDialog();
        }
    };

    private Action schemeEditAction = new ComposerAction(Messages.getString("JComposer.schemeeditor"), Icons.schemeEditIcon, Messages.getString("JComposer.openschemeeditor")) {

        public void actionPerformed(ActionEvent e) {
            showSchemeEditor();
        }
    };

    private Action prefAction = new ComposerAction(Messages.getString("JComposer.preferences"), Icons.prefIcon, Messages.getString("JComposer.editpreferences")) {

        public void actionPerformed(ActionEvent e) {
            if (preferences == null) {
                preferences = new JPreferences(JOptionPane.getFrameForComponent(JComposer.this));
            }
            preferences.setVisible(true);
        }
    };

    private Action aboutAction = new ComposerAction(Messages.getString("JComposer.aboutrubato"), null, "") {

        private Timer timer = null;

        public void actionPerformed(ActionEvent e) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point centerPoint = ge.getCenterPoint();
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new Timer(10000, new ActionListener() {

                public void actionPerformed(ActionEvent ev) {
                    aboutWindow.setVisible(false);
                }
            });
            timer.start();
            if (aboutWindow == null) {
                aboutWindow = new JWindow() {

                    private final JPanel splashPanel;

                    private final ImageIcon image = Icons.splashIcon;

                    private final Dimension dim = new Dimension(image.getIconWidth(), image.getIconHeight());

                    {
                        splashPanel = new JPanel();
                        splashPanel.setOpaque(false);
                        splashPanel.setLayout(null);
                        splashPanel.setMinimumSize(dim);
                        splashPanel.setPreferredSize(dim);
                        splashPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
                        JLabel splashLabel = new JLabel(Icons.splashIcon);
                        splashPanel.add(splashLabel);
                        splashLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
                        JTextArea messageArea = new JTextArea("Copyright (C) 2006 Gérard Milmeister");
                        messageArea.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
                        messageArea.setForeground(new Color(0.59f, 0.67f, 0.99f));
                        messageArea.setOpaque(false);
                        messageArea.setBounds(10, 250, 380, 50);
                        splashPanel.add(messageArea);
                        splashLabel.setBounds(0, 0, image.getIconWidth(), image.getIconHeight());
                        splashPanel.add(splashLabel);
                        setContentPane(splashPanel);
                        addMouseListener(new MouseAdapter() {

                            public void mousePressed(MouseEvent ev) {
                                setVisible(false);
                            }
                        });
                        pack();
                    }
                };
            }
            aboutWindow.setLocation(centerPoint.x - (aboutWindow.getWidth()) / 2, centerPoint.y - (aboutWindow.getHeight()) / 2);
            aboutWindow.setVisible(true);
        }
    };

    private boolean hasChanged = false;

    private JLabel statusBar;

    public static final int STATUS_INFO = 0;

    public static final int STATUS_WARNING = 1;

    public static final int STATUS_ERROR = 2;

    public static final Color[] statusColor = new Color[] { Color.BLACK, new Color(215, 117, 6), Color.RED };

    private Action statusClearAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
            clearStatus();
        }
    };

    private Timer statusTimer = new Timer(STATUS_DELAY, statusClearAction);

    private static final int STATUS_DELAY = 5000;

    private Cursor cursor;

    private LinkedList<Action> allActions = new LinkedList<Action>();

    private JTabbedPane bottomTabs;

    private JTabbedPane rightTabs;

    private JNetworkContainer networkContainer;

    private JRubetteList rubetteList;

    protected JNetworkList networkList;

    private JProblemList problemList;

    private JMessageLog messageLog;

    private JModuleDialog moduleBuilder = null;

    private JMorphismDialog moduleMorphismBuilder = null;

    private JDenotatorDialog denotatorBuilder = null;

    private JFormDialog formBuilder = null;

    private ObjectBrowser objectBrowser = null;

    private JSchemeDialog schemeDialog = null;

    private JSchemeEditor schemeEditor = null;

    protected JPreferences preferences = null;

    private UserPreferences userPrefs = UserPreferences.getUserPreferences();

    protected RecentFiles recentFiles = null;

    protected JWindow aboutWindow = null;

    protected JProgress progress = null;

    protected Runner runner = null;

    private boolean running = false;

    private boolean contRun = false;

    private JToggleButton contToggleButton;

    protected NetworkClip networkClip = null;

    private RubetteManager manager = null;

    private File currentFile = null;

    private File currentDirectory = null;

    private JFileChooser fileChooser = null;

    private static final Dimension PREFERRED_SIZE = new Dimension(900, 800);

    private static final String TITLE_STRING = "Rubato Composer";

    private static final String EXTENSION = "rbo";

    private static final String DOT_EXTENSION = "." + EXTENSION;

    private static final Repository rep = Repository.systemRepository();

    private final Logger logger = Logger.getLogger("org.rubato.composer.jcomposer");

    private static final long serialVersionUID = 7565581918838451751L;
}
