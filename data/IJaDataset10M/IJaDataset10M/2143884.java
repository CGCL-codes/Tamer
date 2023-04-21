package com.mp3explorer.gui.menus;

import com.mp3explorer.util.ResourceManager;
import java.awt.Dimension;
import javax.swing.JToolBar;

/**
 * ApplicationToolbar
 */
public class ApplicationToolbar extends JToolBar {

    private static ResourceManager RM = ResourceManager.instance();

    private static Dimension SEP_SIZE = new Dimension(25, 5);

    /**
     * Constructor
     * @param appMenu The application menu
     */
    public ApplicationToolbar(ApplicationMenu appMenu) {
        super(RM.getLabel("common.toolbar.name"));
        this.setFloatable(true);
        this.setRollover(true);
        add(appMenu.itmFilePlay);
        add(appMenu.itmFileEnqueue);
        add(appMenu.itmFileProperties);
        add(appMenu.itmFileDelete);
        addSeparator(SEP_SIZE);
        add(appMenu.itmEditCopy);
        add(appMenu.itmEditSearch);
        addSeparator(SEP_SIZE);
        add(appMenu.itmDisplayArtist);
        add(appMenu.itmDisplayGenre);
        add(appMenu.itmDisplayAlbum);
    }
}
