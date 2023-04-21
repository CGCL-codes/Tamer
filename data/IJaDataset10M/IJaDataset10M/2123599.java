package csheets.ext.style.ui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import csheets.ext.style.StyleExtension;
import csheets.ui.ctrl.UIController;
import csheets.ui.ext.UIExtension;

/**
 * The user interface extension for style.
 * @author Einar Pehrson
 */
public class StyleUIExtension extends UIExtension {

    /** The icon to display with the extension's name */
    private Icon icon;

    /** A menu that provides editing of style */
    private StyleMenu menu;

    /** A toolbar that provides editing of style */
    private StyleToolBar toolBar;

    /**
	 * Creates a new user interface extension for style.
	 * @param extension the extension for which components are provided
	 * @param uiController the user interface controller
	 */
    public StyleUIExtension(StyleExtension extension, UIController uiController) {
        super(extension, uiController);
    }

    /**
	 * Returns an icon to display with the extension's name.
	 * @return an icon with style
	 */
    public Icon getIcon() {
        if (icon == null) icon = new ImageIcon(StyleExtension.class.getResource("res/img/logo.gif"));
        return icon;
    }

    /**
	 * Returns a menu that provides editing of style.
	 * @return a JMenu component
	 */
    public JMenu getMenu() {
        if (menu == null) menu = new StyleMenu(uiController);
        return menu;
    }

    /**
	 * Returns a toolbar that provides editing of style.
	 * @return a JToolBar component
	 */
    public JToolBar getToolBar() {
        if (toolBar == null) toolBar = new StyleToolBar(uiController);
        return toolBar;
    }
}
