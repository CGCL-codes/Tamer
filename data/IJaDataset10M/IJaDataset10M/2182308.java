package com.jedifact.jeditor.appui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import com.jedifact.jeditor.domain.Interchange;

public class JeditorApplication extends SingleFrameApplication {

    private final JDesktopPane desktop = new JDesktopPane();

    private final JMenuBar menuBar = new JMenuBar();

    private boolean fileClosable = false;

    private boolean fileSavable = false;

    private boolean fileSavableAs = false;

    private JeditorDocumentFrame selectedFrame = null;

    public static void main(String[] args) {
        Application.launch(JeditorApplication.class, args);
    }

    @Override
    protected void startup() {
        getMainFrame().setContentPane(desktop);
        getMainFrame().setJMenuBar(menuBar);
        menuBar.add(setupFileMenu());
        menuBar.add(setupEditMenu());
        menuBar.add(setupToolsMenu());
        menuBar.add(setupHelpMenu());
        show(getMainView());
    }

    private JMenu setupFileMenu() {
        JMenu menu = new JMenu();
        menu.setName("fileMenu");
        menu.add(new JMenuItem(action("newFile")));
        menu.add(new JMenuItem(action("openFile")));
        menu.addSeparator();
        menu.add(new JMenuItem(action("closeFile")));
        menu.add(new JMenuItem(action("closeAllFiles")));
        menu.addSeparator();
        menu.add(new JMenuItem(action("saveFile")));
        menu.add(new JMenuItem(action("saveAsFile")));
        menu.addSeparator();
        menu.add(new JMenuItem(action("exitFile")));
        menu.add(Box.createHorizontalGlue());
        return menu;
    }

    private JMenu setupEditMenu() {
        JMenu menu = new JMenu();
        menu.setName("editMenu");
        menu.setEnabled(false);
        return menu;
    }

    private JMenu setupToolsMenu() {
        JMenu menu = new JMenu();
        menu.setName("toolsMenu");
        menu.setEnabled(false);
        return menu;
    }

    private JMenu setupHelpMenu() {
        JMenu menu = new JMenu();
        menu.setName("helpMenu");
        menu.setEnabled(false);
        return menu;
    }

    @Action
    public void newFile() {
        System.out.println("New file!");
        createFrame(null);
    }

    @Action
    public void openFile() {
        System.out.println("Open file!");
        JFileChooser fc = new JFileChooser();
        int retVal = fc.showOpenDialog(getMainFrame());
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            createFrame(file);
        }
    }

    @Action(enabledProperty = "fileSavable")
    public void saveFile() {
        System.out.println("TODO");
    }

    @Action(enabledProperty = "fileSavableAs")
    public void saveAsFile() {
        System.out.println("TODO");
    }

    @Action(enabledProperty = "fileClosable")
    public void closeFile() {
        System.out.println("Close file!");
    }

    @Action(enabledProperty = "fileClosable")
    public void closeAllFiles() {
        System.out.println("Close all files!");
    }

    @Action
    public void exitFile() {
        System.out.println("Exit!");
        exit();
    }

    private void createFrame(File file) {
        final JeditorDocumentFrame frame = new JeditorDocumentFrame(file);
        desktop.add(frame);
        frame.addPropertyChangeListener("jeditor.document.selected", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("prop change: " + evt.toString());
                setSelectedFrameProperties((JeditorDocumentFrame) evt.getSource());
            }
        });
        try {
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    private javax.swing.Action action(String name) {
        return getContext().getActionMap().get(name);
    }

    public void setSelectedFrameProperties(JeditorDocumentFrame frame) {
        selectedFrame = frame;
        setFileClosable(true);
        setFileSavable(true);
        setFileSavableAs(true);
    }

    public boolean isFileClosable() {
        return fileClosable;
    }

    public void setFileClosable(boolean fileClosable) {
        boolean oldClosable = fileClosable;
        fileClosable = true;
        firePropertyChange("fileClosable", oldClosable, fileClosable);
    }

    public boolean isFileSavable() {
        return fileSavable;
    }

    public void setFileSavable(boolean fileSavable) {
        boolean oldSavableAs = fileSavableAs;
        fileSavableAs = true;
        firePropertyChange("fileSavableAs", oldSavableAs, fileSavableAs);
    }

    public boolean isFileSavableAs() {
        return fileSavableAs;
    }

    public void setFileSavableAs(boolean fileSavableAs) {
        boolean oldSavable = fileSavable;
        fileSavable = true;
        firePropertyChange("fileSavable", oldSavable, fileSavable);
    }
}
