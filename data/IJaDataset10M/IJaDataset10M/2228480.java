package acide.gui.menuBar.configurationMenu.toolBarMenu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import acide.configuration.menu.AcideMenuConfiguration;
import acide.gui.menuBar.configurationMenu.toolBarMenu.listeners.AcideLoadToolBarMenuItemListener;
import acide.gui.menuBar.configurationMenu.toolBarMenu.listeners.AcideModifyToolBarMenuItemListener;
import acide.gui.menuBar.configurationMenu.toolBarMenu.listeners.AcideNewToolBarMenuItemListener;
import acide.gui.menuBar.configurationMenu.toolBarMenu.listeners.AcideToolBaAsMenuItemListener;
import acide.gui.menuBar.configurationMenu.toolBarMenu.listeners.AcideSaveToolBarMenuItemListener;
import acide.language.AcideLanguageManager;

/**
 * ACIDE - A Configurable IDE tool bar menu.
 * 
 * @version 0.8
 * @see JMenu
 */
public class AcideToolBarMenu extends JMenu {

    /**
	 * ACIDE - A Configurable IDE tool bar menu tool bar menu class serial
	 * version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ACIDE - A Configurable IDE tool bar menu new tool bar menu item name.
	 */
    public static final String NEW_TOOLBAR_NAME = "New Toolbar";

    /**
	 * ACIDE - A Configurable IDE tool bar menu load tool bar menu item name.
	 */
    public static final String LOAD_TOOLBAR_NAME = "Load Toolbar";

    /**
	 * ACIDE - A Configurable IDE tool bar menu modify tool bar menu item name.
	 */
    public static final String MODIFY_TOOLBAR_NAME = "Modify Toolbar";

    /**
	 * ACIDE - A Configurable IDE tool bar menu save tool bar menu item name.
	 */
    public static final String SAVE_TOOLBAR_NAME = "Save Toolbar";

    /**
	 * ACIDE - A Configurable IDE tool bar menu save tool bar as menu item name.
	 */
    public static final String SAVE_TOOLBAR_AS_NAME = "Save Toolbar As";

    /**
	 * ACIDE - A Configurable IDE tool bar menu new tool bar menu item.
	 */
    private JMenuItem _newToolBarMenuItem;

    /**
	 * ACIDE - A Configurable IDE tool bar menu load tool bar menu item.
	 */
    private JMenuItem _loadToolBarMenuItem;

    /**
	 * ACIDE - A Configurable IDE tool bar menu modify tool bar menu item.
	 */
    private JMenuItem _modifyToolBarMenuItem;

    /**
	 * ACIDE - A Configurable IDE tool bar menu save tool bar menu item.
	 */
    private JMenuItem _saveToolBarMenuItem;

    /**
	 * ACIDE - A Configurable IDE tool bar menu save tool bar as menu item.
	 */
    private JMenuItem _saveToolBarAsMenuItem;

    /**
	 * Creates a new ACIDE - A Configurable IDE tool bar menu.
	 */
    public AcideToolBarMenu() {
        buildComponents();
        addComponents();
        setTextOfMenuComponents();
    }

    /**
	 * Adds the components to the ACIDE - A Configurable IDE tool bar menu.
	 */
    private void addComponents() {
        add(_newToolBarMenuItem);
        add(_loadToolBarMenuItem);
        add(_modifyToolBarMenuItem);
        add(_saveToolBarMenuItem);
        add(_saveToolBarAsMenuItem);
    }

    /**
	 * Builds the ACIDE - A Configurable IDE tool bar menu components.
	 */
    private void buildComponents() {
        _newToolBarMenuItem = new JMenuItem();
        _newToolBarMenuItem.setName(NEW_TOOLBAR_NAME);
        _loadToolBarMenuItem = new JMenuItem();
        _loadToolBarMenuItem.setName(LOAD_TOOLBAR_NAME);
        _modifyToolBarMenuItem = new JMenuItem();
        _modifyToolBarMenuItem.setName(MODIFY_TOOLBAR_NAME);
        _saveToolBarMenuItem = new JMenuItem();
        _saveToolBarMenuItem.setName(SAVE_TOOLBAR_NAME);
        _saveToolBarMenuItem.setEnabled(false);
        _saveToolBarAsMenuItem = new JMenuItem();
        _saveToolBarAsMenuItem.setName(SAVE_TOOLBAR_AS_NAME);
    }

    /**
	 * Sets the text of the ACIDE - A Configurable IDE tool bar menu components
	 * with the labels in the selected language to display.
	 */
    public void setTextOfMenuComponents() {
        _newToolBarMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s280"));
        _loadToolBarMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s281"));
        _modifyToolBarMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s282"));
        _saveToolBarMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s283"));
        _saveToolBarAsMenuItem.setText(AcideLanguageManager.getInstance().getLabels().getString("s284"));
    }

    /**
	 * Sets the ACIDE - A Configurable IDE tool bar menu menu item listeners.
	 */
    public void setListeners() {
        _newToolBarMenuItem.addActionListener(new AcideNewToolBarMenuItemListener());
        _loadToolBarMenuItem.addActionListener(new AcideLoadToolBarMenuItemListener());
        _modifyToolBarMenuItem.addActionListener(new AcideModifyToolBarMenuItemListener());
        _saveToolBarMenuItem.addActionListener(new AcideSaveToolBarMenuItemListener());
        _saveToolBarAsMenuItem.addActionListener(new AcideToolBaAsMenuItemListener());
    }

    /**
	 * Updates the ACIDE - A Configurable IDE tool bar menu components
	 * visibility with the menu configuration.
	 */
    public void updateComponentsVisibility() {
        _newToolBarMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(NEW_TOOLBAR_NAME));
        _loadToolBarMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(LOAD_TOOLBAR_NAME));
        _modifyToolBarMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(MODIFY_TOOLBAR_NAME));
        _saveToolBarMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(SAVE_TOOLBAR_NAME));
        _saveToolBarAsMenuItem.setVisible(AcideMenuConfiguration.getInstance().getIsDisplayed(SAVE_TOOLBAR_AS_NAME));
    }

    /**
	 * Returns the ACIDE - A Configurable IDE tool bar menu new tool bar menu
	 * item.
	 * 
	 * @return the ACIDE - A Configurable IDE tool bar menu new tool bar menu
	 *         item.
	 */
    public JMenuItem getNewToolBarMenuItem() {
        return _newToolBarMenuItem;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE tool bar menu load tool bar menu
	 * item.
	 * 
	 * @return the ACIDE - A Configurable IDE tool bar menu load tool bar menu
	 *         item.
	 */
    public JMenuItem getLoadToolBarMenuItem() {
        return _loadToolBarMenuItem;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE tool bar menu modify tool bar menu
	 * item.
	 * 
	 * @return the ACIDE - A Configurable IDE tool bar menu modify tool bar menu
	 *         item.
	 */
    public JMenuItem getModifyToolBarMenuItem() {
        return _modifyToolBarMenuItem;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE tool bar menu save tool bar menu
	 * item
	 * 
	 * @return the ACIDE - A Configurable IDE tool bar menu save tool bar menu
	 *         item
	 */
    public JMenuItem getSaveToolBarMenuItem() {
        return _saveToolBarMenuItem;
    }

    /**
	 * Returns the ACIDE - A Configurable IDE tool bar menu save tool bar as
	 * menu item.
	 * 
	 * @return the ACIDE - A Configurable IDE tool bar menu save tool bar as
	 *         menu item.
	 */
    public JMenuItem getSaveToolBarAsMenuItem() {
        return _saveToolBarAsMenuItem;
    }
}
