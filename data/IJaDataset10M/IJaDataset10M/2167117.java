package com.zycus.dotproject.ui;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.zycus.dotproject.bo.BOProject;
import com.zycus.dotproject.bo.UserType;
import com.zycus.dotproject.ui.event.ProjectSelectionListener;
import com.zycus.dotproject.util.ApplicationContext;
import com.zycus.dotproject.util.MenuUtility;

public class DotProjectMenubar extends JMenuBar implements ProjectSelectionListener {

    private JMenu fileMenu = null;

    private JMenuItem saveMenu = null;

    private JMenuItem exitMenu = null;

    private JMenu editMenu = null;

    private JMenuItem cut = null;

    private JMenuItem copy = null;

    private JMenuItem paste = null;

    private JMenuItem delete = null;

    private JMenu manageMenu = null;

    private JMenuItem companyMenu = null;

    private JMenuItem divisionMenu = null;

    private JMenuItem projectMenu = null;

    private JMenuItem forumMenu = null;

    private JMenuItem userMenu = null;

    private JMenu toolsMenu = null;

    private JMenuItem settingsMenu = null;

    private JMenuItem reportsMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem keyHelpMenu = null;

    private JMenuItem aboutMenu = null;

    public DotProjectMenubar() {
        initComponents();
    }

    private void initComponents() {
        add(fileMenu = MenuUtility.getMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(saveMenu = MenuUtility.getMenuItem("Save", new DotProjectActionListener.SaveProjectAction()));
        fileMenu.add(exitMenu = MenuUtility.getMenuItem("Exit", new DotProjectActionListener.ExitAction()));
        saveMenu.setMnemonic('S');
        saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK, true));
        saveMenu.setIcon(IconHelper.getSaveIcon());
        saveMenu.setEnabled(false);
        exitMenu.setMnemonic('x');
        exitMenu.setIcon(IconHelper.getCloseIcon());
        add(manageMenu = MenuUtility.getMenu("Manage"));
        manageMenu.setMnemonic('M');
        if (ApplicationContext.getCurrentUser().getUserType() == UserType.Administrator || ApplicationContext.getCurrentUser().getUserType() == UserType.CEO || ApplicationContext.getCurrentUser().getUserType() == UserType.Director) {
        }
        manageMenu.add(projectMenu = MenuUtility.getMenuItem("Project", new DotProjectActionListener.ProjectManagementAction()));
        add(toolsMenu = MenuUtility.getMenu("Tools"));
        toolsMenu.setMnemonic('T');
        toolsMenu.add(settingsMenu = MenuUtility.getMenuItem("Settings", new DotProjectActionListener.SettingAction()));
        add(helpMenu = MenuUtility.getMenu("?"));
        helpMenu.add(keyHelpMenu = MenuUtility.getMenuItem("Key Shortcut", new DotProjectActionListener.ShortcutKeyAction()));
        helpMenu.add(aboutMenu = MenuUtility.getMenuItem("About US", new DotProjectActionListener.AboutUSAction()));
        ApplicationContext.addProjectSelectionListener(this);
    }

    public void projectSelected(BOProject project) {
        saveMenu.setEnabled(project != null);
    }
}
