package com.pyrphoros.erddb.gui.windows.main.menu;

import javax.swing.*;
import com.pyrphoros.erddb.Designer;
import com.pyrphoros.erddb.gui.windows.main.menu.actions.ThemeSelectAction;
import com.pyrphoros.erddb.laf.Themes.THEME;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 */
public class ThemeMenu extends JMenu {

    private Hashtable<JCheckBoxMenuItem, THEME> themeList;

    private static Logger logger = Logger.getLogger(ThemeMenu.class);

    /**
	 *
	 */
    public ThemeMenu() {
        super(Designer.getResource("gui.main.menu.theme"));
        themeList = new Hashtable<JCheckBoxMenuItem, THEME>();
        ButtonGroup bg = new ButtonGroup();
        JCheckBoxMenuItem item;
        item = new JCheckBoxMenuItem(Designer.getResource("gui.main.menu.theme.default"));
        item.addActionListener(new ThemeSelectAction(this));
        bg.add(item);
        item.setState(Designer.getConfig().getCurrentTheme() == THEME.DEFAULT_THEME);
        themeList.put(item, THEME.DEFAULT_THEME);
        this.add(item);
        item = new JCheckBoxMenuItem(Designer.getResource("gui.main.menu.theme.midnight"));
        item.addActionListener(new ThemeSelectAction(this));
        bg.add(item);
        item.setState(Designer.getConfig().getCurrentTheme() == THEME.MIDNIGHT_THEME);
        themeList.put(item, THEME.MIDNIGHT_THEME);
        this.add(item);
    }

    public void setSelectedTheme(JCheckBoxMenuItem item) {
        logger.trace("setSelectedTheme(JCheckBoxMenuItem)");
        item.setState(true);
        THEME theme = themeList.get(item);
        Designer.getConfig().setCurrentTheme(theme);
        JOptionPane.showMessageDialog(Designer.getMainFrame(), Designer.getResource("gui.warning.restartrequired"), "Notice", JOptionPane.INFORMATION_MESSAGE);
    }
}
