package org.logitest.ui;

import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Icon;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.logitest.ui.util.ApplicationResources;

public class BatchDialogMenuBar extends JMenuBar {

    public BatchDialogMenuBar(BatchDialog parent) {
        this.parent = parent;
        init();
    }

    public void loadResources() {
        ResourceBundle bundle = ResourceBundle.getBundle(ApplicationResources.class.getName());
        fileMenu.setText((String) bundle.getObject("FileMenu.name"));
        fileMenu.setMnemonic(((Character) bundle.getObject("FileMenu.mnemonic")).charValue());
        openMenuItem.setText((String) bundle.getObject("OpenAction.name"));
        openMenuItem.setIcon((Icon) bundle.getObject("OpenAction.icon"));
        openMenuItem.setMnemonic(((Character) bundle.getObject("OpenAction.mnemonic")).charValue());
        openMenuItem.setAccelerator((KeyStroke) bundle.getObject("OpenAction.accelerator"));
        openMenuItem.setToolTipText((String) bundle.getObject("OpenAction.toolTipText"));
        saveMenuItem.setText((String) bundle.getObject("SaveAction.name"));
        saveMenuItem.setIcon((Icon) bundle.getObject("SaveAction.icon"));
        saveMenuItem.setMnemonic(((Character) bundle.getObject("SaveAction.mnemonic")).charValue());
        saveMenuItem.setAccelerator((KeyStroke) bundle.getObject("SaveAction.accelerator"));
        saveMenuItem.setToolTipText((String) bundle.getObject("SaveAction.toolTipText"));
        saveAsMenuItem.setText((String) bundle.getObject("SaveAsAction.name"));
        saveAsMenuItem.setIcon((Icon) bundle.getObject("SaveAsAction.icon"));
        saveAsMenuItem.setMnemonic(((Character) bundle.getObject("SaveAsAction.mnemonic")).charValue());
        saveAsMenuItem.setAccelerator((KeyStroke) bundle.getObject("SaveAsAction.accelerator"));
        saveAsMenuItem.setToolTipText((String) bundle.getObject("SaveAsAction.toolTipText"));
        closeMenuItem.setText((String) bundle.getObject("CloseAction.name"));
        closeMenuItem.setIcon((Icon) bundle.getObject("CloseAction.icon"));
        closeMenuItem.setMnemonic(((Character) bundle.getObject("CloseAction.mnemonic")).charValue());
        closeMenuItem.setAccelerator((KeyStroke) bundle.getObject("CloseAction.accelerator"));
        closeMenuItem.setToolTipText((String) bundle.getObject("CloseAction.toolTipText"));
    }

    private void init() {
        Map actions = parent.getActions();
        fileMenu = new JMenu();
        openMenuItem = fileMenu.add((Action) actions.get("file.open"));
        fileMenu.addSeparator();
        saveMenuItem = fileMenu.add((Action) actions.get("file.save"));
        saveAsMenuItem = fileMenu.add((Action) actions.get("file.saveAs"));
        fileMenu.addSeparator();
        closeMenuItem = fileMenu.add((Action) actions.get("file.close"));
        add(fileMenu);
        loadResources();
    }

    private BatchDialog parent;

    private JMenu fileMenu;

    private JMenuItem openMenuItem;

    private JMenuItem saveMenuItem;

    private JMenuItem saveAsMenuItem;

    private JMenuItem closeMenuItem;
}
