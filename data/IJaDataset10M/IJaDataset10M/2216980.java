package com.netbreeze.bbowl.gui;

import com.netbreeze.bbowl.*;
import com.netbreeze.bbowl.test.*;
import com.netbreeze.swing.*;
import com.netbreeze.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import org.apache.log4j.*;

/**
 * The top-level GUI for the BeanBowl application.
 * It also contains the main(...) method.
 *
 * @author Henrik Kniberg
 */
public class BeanBowlGUI extends JFrame implements PropertyChangeListener {

    static BeanBowlGUI defaultFrame = null;

    static Category cat = Category.getInstance(BeanBowlGUI.class);

    BeanBowl bowl;

    BeanBowlContext context;

    File file = null;

    JMenuBar menuBar;

    FileMenu fileMenu;

    BeanBowlPanel panel;

    JToolBar toolbar;

    BeanMenu selectedMenu;

    Action saveAction = new SaveAction();

    Action openAction = new OpenAction();

    Action saveAsAction = new SaveAsAction();

    Action newAction = new NewAction();

    Action aboutAction = new AboutAction();

    public static void main(String[] args) {
        LogManager.initLog4J();
        cat.info("Starting bean bowl...");
        SplashWindow splash = new SplashWindow(Icons.loadIcon("splash.jpg"));
        splash.show();
        try {
            BeanBowlGUI frame = new BeanBowlGUI();
            SwingEnvironment.setBeansContext(frame.getContext());
            frame.setSize(800, 600);
            com.netbreeze.util.Utility.centerWindow(frame);
            frame.show();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cat.info("Bean bowl is now running!");
        } catch (Exception err) {
            cat.error("Bean bowl could not be started", err);
        }
        splash.dispose();
    }

    /**
   * Creates a new BeanBowlGUI that shows the given bowl
   */
    public BeanBowlGUI(BeanBowl bowl) {
        String version = BeanBowlGUI.class.getPackage().getImplementationVersion();
        if (version == null) {
            setTitle("Bean bowl");
        } else {
            setTitle("Bean bowl version " + version);
        }
        try {
            setIconImage(Icons.loadImage("mainFrame.gif"));
        } catch (Throwable err) {
        }
        this.context = new BeanBowlContext(this);
        setBowl(bowl);
    }

    /**
   * Creates a new BeanBowlGUI that shows a new BeanBowl
   */
    public BeanBowlGUI() {
        this(new BeanBowl());
    }

    public BeanBowlPanel getBeanBowlPanel() {
        return panel;
    }

    public JDesktopPane getDesk() {
        return panel.getDesk();
    }

    public static BeanBowlGUI getDefaultFrame() {
        return defaultFrame;
    }

    public BeanBowlContext getContext() {
        return context;
    }

    /**
   * The current bean bowl being displayed
   */
    public BeanBowl getBowl() {
        return bowl;
    }

    /**
   * Sets the bowl to be displayed
   */
    private void setBowl(BeanBowl newBowl) {
        BeanBowl oldBowl = bowl;
        if (newBowl != oldBowl) {
            this.bowl = newBowl;
            this.panel = new BeanBowlPanel(context);
            getContentPane().removeAll();
            initGUI();
            invalidate();
            validate();
            if (oldBowl != null) oldBowl.removePropertyChangeListener(this);
            if (newBowl != null) newBowl.addPropertyChangeListener(this);
            updateSelectedMenu();
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == bowl) {
            if (evt.getPropertyName().equals("selected")) {
                updateSelectedMenu();
            }
        }
    }

    protected void processEvent(AWTEvent e) {
        if (e.getID() == Event.WINDOW_DESTROY) {
            cat.info("Shutting down bean bowl...");
            try {
                Settings.saveToFile();
            } catch (Exception err) {
                cat.warn("Warning - failed to save settings: " + err.getMessage(), err);
            }
            removeAll();
            dispose();
            cat.info("Bean bowl is now shut down!");
        }
        super.processEvent(e);
    }

    void openBowl() {
        FileDialog dialog = new FileDialog(this, "Load bean bowl", FileDialog.LOAD);
        dialog.show();
        String fileName = dialog.getFile();
        String directory = dialog.getDirectory();
        if (fileName != null) {
            openBowl(new File(directory, fileName));
        }
    }

    void openBowl(File file) {
        if (file.exists()) {
            try {
                setBowl(BeanBowl.load(file));
                Settings.addRecentFile(file);
                fileMenu.refreshRecentFileList();
            } catch (Exception err) {
                context.showError("Opening failed", err);
            }
        } else {
            context.showError("File does not exist: " + file.getPath(), null);
        }
    }

    void newBowl() {
        setBowl(new BeanBowl());
        file = null;
        checkControls();
    }

    void saveBowl() {
        if (file == null) {
            saveBowlAs();
        } else {
            saveBowl(file);
        }
    }

    void saveBowlAs() {
        FileDialog dialog = new FileDialog(this, "Save bean bowl", FileDialog.SAVE);
        dialog.setFile("mybowl.ser");
        dialog.show();
        String fileName = dialog.getFile();
        String directory = dialog.getDirectory();
        cat.debug("fileName = " + fileName);
        cat.debug("directory = " + directory);
        if (fileName != null) {
            saveBowl(new File(directory, fileName));
        }
    }

    void saveBowl(File file) {
        cat.debug("saveBowl(" + file.getAbsoluteFile() + ")");
        this.file = file;
        try {
            bowl.save(file);
        } catch (NotSerializableException err) {
            context.showError("This bowl contains an unserializable object", err);
        } catch (Exception err) {
            context.showError("Saving failed", err);
        }
        checkControls();
    }

    private void updateSelectedMenu() {
        if (selectedMenu != null) {
            menuBar.remove(selectedMenu);
            selectedMenu = null;
        }
        Object selected = bowl.getSelectedBean();
        if (selected != null) {
            selectedMenu = new BeanMenu(selected);
            menuBar.add(selectedMenu);
        }
        invalidate();
        validate();
        repaint();
    }

    void checkControls() {
        saveAction.setEnabled(file != null);
    }

    /**
   * Creates and initialized the GUI components
   * within the BeanBowlGUI. Should only be called once.
   */
    private void initGUI() {
        if (defaultFrame == null) {
            defaultFrame = this;
        }
        getContentPane().setLayout(new BorderLayout());
        panel = new BeanBowlPanel(context);
        menuBar = new JMenuBar();
        fileMenu = new FileMenu();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        toolbar = new MyToolBar();
        toolbar.setFloatable(true);
        getContentPane().add("Center", panel);
        getContentPane().add("North", toolbar);
        checkControls();
    }

    class SaveAction extends AbstractAction {

        SaveAction() {
            super("Save", Icons.saveBowl);
        }

        public void actionPerformed(ActionEvent evt) {
            saveBowl();
        }
    }

    class SaveAsAction extends AbstractAction {

        SaveAsAction() {
            super("Save as...", Icons.saveBowlAs);
        }

        public void actionPerformed(ActionEvent evt) {
            saveBowlAs();
        }
    }

    class NewAction extends AbstractAction {

        NewAction() {
            super("New", Icons.newBowl);
        }

        public void actionPerformed(ActionEvent evt) {
            newBowl();
        }
    }

    class OpenAction extends AbstractAction {

        OpenAction() {
            super("Open", Icons.openBowl);
        }

        public void actionPerformed(ActionEvent evt) {
            openBowl();
        }
    }

    class AboutAction extends AbstractAction {

        AboutAction() {
            super("About Bean Bowl...", Icons.about);
        }

        public void actionPerformed(ActionEvent evt) {
            setEnabled(false);
            SplashWindow splash = new SplashWindow(Icons.loadIcon("splash.jpg"), true);
            splash.show();
            splash.addWindowListener(new WindowAdapter() {

                public void windowClosed(WindowEvent e) {
                    setEnabled(true);
                }
            });
        }
    }

    class RecentFileAction extends AbstractAction {

        File file;

        RecentFileAction(File file) {
            super(file.getName(), Icons.recentFile);
            this.file = file;
        }

        public void actionPerformed(ActionEvent evt) {
            openBowl(file);
        }
    }

    class FileMenu extends JMenu {

        Vector recentFiles = new Vector();

        FileMenu() {
            super("File");
            addItems();
        }

        private void addItems() {
            add(newAction);
            add(openAction);
            addSeparator();
            add(saveAction);
            add(saveAsAction);
            addSeparator();
            recentFiles = new Vector();
            Iterator it = Settings.getRecentFiles();
            while (it.hasNext()) {
                File file = (File) it.next();
                Action a = new RecentFileAction(file);
                recentFiles.addElement(a);
                add(a);
            }
        }

        public void refreshRecentFileList() {
            removeAll();
            addItems();
        }
    }

    class MyToolBar extends JToolBar {

        MyToolBar() {
            super();
            add(newAction);
            add(openAction);
            addSeparator();
            add(saveAction);
            add(saveAsAction);
            addSeparator();
            add(aboutAction);
        }
    }
}
