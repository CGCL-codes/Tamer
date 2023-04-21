package net.sf.gham.core.gui;

import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import net.sf.gham.plugins.commands.file.DownloadAction;
import net.sf.gham.plugins.commands.file.ExitAction;
import net.sf.gham.plugins.commands.file.ImportAction;
import net.sf.gham.plugins.commands.file.LoadTeamAction;
import net.sf.gham.plugins.commands.help.AboutAction;
import net.sf.gham.plugins.commands.options.PreferencesAction;
import net.sf.gham.plugins.commands.tools.DownloadOldMatchesAction;
import net.sf.gham.plugins.commands.tools.DownloadOldYouthMatchesAction;
import net.sf.gham.plugins.commands.tools.filemanager.FileManagerAction;
import net.sf.gham.plugins.commands.tools.matchrelive.MatchReliveAction;
import net.sf.gham.plugins.commands.tools.youthviewer.YouthViewAction;
import net.sf.jtwa.Messages;

/**
 * @author fabio
 *
 */
public class MenubarCreator {

    private MenubarCreator() {
    }

    private static MenubarCreator singleton = new MenubarCreator();

    public static MenubarCreator singleton() {
        return singleton;
    }

    private javax.swing.JMenu fileMenu;

    private javax.swing.JMenu toolsMenu;

    private javax.swing.JMenu helpMenu;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JMenu optionsMenu;

    public JMenuBar createMenuBar() {
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        optionsMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        toolsMenu = new javax.swing.JMenu();
        toolsMenu.setMnemonic('T');
        toolsMenu.setText(Messages.getString("Tools"));
        toolsMenu.add(new MatchReliveAction());
        toolsMenu.add(new DownloadOldMatchesAction());
        toolsMenu.add(new DownloadOldYouthMatchesAction());
        toolsMenu.add(new YouthViewAction());
        toolsMenu.add(new FileManagerAction());
        fileMenu.setMnemonic('F');
        fileMenu.setText(Messages.getString("File"));
        fileMenu.add(new DownloadAction());
        fileMenu.add(new LoadTeamAction());
        fileMenu.add(new JSeparator());
        fileMenu.add(new ImportAction());
        fileMenu.add(new JSeparator());
        fileMenu.add(new ExitAction());
        optionsMenu.setMnemonic('O');
        optionsMenu.setText(Messages.getString("Options"));
        optionsMenu.add(new PreferencesAction());
        helpMenu.setMnemonic('H');
        helpMenu.setText(Messages.getString("Help"));
        helpMenu.add(new AboutAction());
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }
}
