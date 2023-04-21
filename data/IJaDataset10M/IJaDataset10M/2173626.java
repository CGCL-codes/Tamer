package gpsxml.gui;

import gpsxml.ArchiveHandler;
import javax.swing.JMenuBar;

/**
 *  Creates and holds the menus for this application.
 * @author PLAYER, Keith Ralph
 */
public class MainMenuBar extends JMenuBar {

    private FileMenu fileMenu;

    private ServerMenu serverMenu;

    private HelpMenu helpMenu;

    /** Creates a new instance of MenuBar */
    public MainMenuBar(ArchiveHandler archiveHandler) {
        fileMenu = new FileMenu(archiveHandler);
        serverMenu = new ServerMenu(archiveHandler);
        helpMenu = new HelpMenu(archiveHandler);
        fileMenu.addMenu(this);
        serverMenu.addMenu(this);
        helpMenu.addMenu(this);
    }

    public void enableServerMenu(boolean enable) {
        serverMenu.setServerMenuEnabled(enable);
    }
}
