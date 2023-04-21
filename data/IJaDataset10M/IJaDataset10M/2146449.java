package acide.gui.menuBar.configurationMenu.menuMenu.gui.filePanel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import acide.configuration.menu.AcideMenuConfiguration;
import acide.configuration.menu.AcideMenuItemInformation;
import acide.gui.mainWindow.AcideMainWindow;
import acide.gui.menuBar.fileMenu.AcideFileMenu;

/**
 * ACIDE - A Configurable IDE file menu panel.
 * 
 * @version 0.8
 * @see JPanel
 */
public class AcideFileMenuPanel extends JPanel {

    /**
	 * ACIDE - A Configurable IDE file menu panel serial version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ACIDE - A Configurable IDE file menu panel file menu instance.
	 */
    private AcideFileMenu _fileMenu = AcideMainWindow.getInstance().getMenu().getFileMenu();

    /**
	 * ACIDE - A Configurable IDE file menu panel components.
	 */
    private HashMap<String, JCheckBox> _components = new HashMap<String, JCheckBox>();

    /**
	 * Creates a new ACIDE - A Configurable IDE file menu panel.
	 */
    public AcideFileMenuPanel() {
        super(new GridLayout(0, 2));
        initComponents();
    }

    /**
	 * Builds and adds the components to the ACIDE - A Configurable IDE file
	 * menu panel.
	 */
    public void initComponents() {
        for (int index = 0; index < _fileMenu.getItemCount(); index++) {
            JMenuItem menuItem = null;
            try {
                menuItem = _fileMenu.getItem(index);
                if (menuItem != null) {
                    _components.put(menuItem.getName(), new JCheckBox(menuItem.getText()));
                    add(_components.get(menuItem.getName()));
                }
            } catch (ClassCastException exception) {
                JMenu menu = (JMenu) _fileMenu.getMenuComponent(index);
                if (menu != null) {
                    _components.put(menu.getName(), new JCheckBox(menu.getText()));
                    add(_components.get(menu.getName()));
                }
            }
        }
    }

    /**
	 * Sets the check box values from the menu item list of the menu
	 * configuration.
	 */
    public void setCheckBoxesFromMenuItemList() {
        for (int index = 0; index < _fileMenu.getItemCount(); index++) {
            JMenuItem menuItem = null;
            try {
                menuItem = _fileMenu.getItem(index);
                if (menuItem != null) {
                    _components.get(menuItem.getName()).setSelected(AcideMenuConfiguration.getInstance().getIsDisplayed(menuItem.getName()));
                }
            } catch (ClassCastException exception) {
                JMenu menu = (JMenu) _fileMenu.getMenuComponent(index);
                if (menu != null) {
                    _components.get(menu.getName()).setSelected(AcideMenuConfiguration.getInstance().getIsDisplayed(menu.getName()));
                }
            }
        }
    }

    /**
	 * Adds the file menu information to the menu item list, based on the window
	 * check box values.
	 * 
	 * @param menuItemList
	 *            menu item list to be generated.
	 */
    public void addFileMenuInformation(ArrayList<AcideMenuItemInformation> menuItemList) {
        for (int index = 0; index < _fileMenu.getItemCount(); index++) {
            JMenuItem menuItem = null;
            try {
                menuItem = _fileMenu.getItem(index);
                if (menuItem != null) {
                    menuItemList.add(new AcideMenuItemInformation(menuItem.getName(), _components.get(menuItem.getName()).isSelected()));
                }
            } catch (ClassCastException exception) {
                JMenu menu = (JMenu) _fileMenu.getMenuComponent(index);
                if (menu != null) {
                    menuItemList.add(new AcideMenuItemInformation(menu.getName(), _components.get(menu.getName()).isSelected()));
                }
            }
        }
    }

    /**
	 * Marks as selected all the ACIDE - A Configurable IDE file menu panel
	 * components.
	 */
    public void selectAll() {
        for (int index = 0; index < _fileMenu.getItemCount(); index++) {
            JMenuItem menuItem = null;
            try {
                menuItem = _fileMenu.getItem(index);
                if (menuItem != null) {
                    _components.get(menuItem.getName()).setSelected(true);
                }
            } catch (ClassCastException exception) {
                JMenu menu = (JMenu) _fileMenu.getMenuComponent(index);
                if (menu != null) {
                    _components.get(menu.getName()).setSelected(true);
                }
            }
        }
    }

    /**
	 * Marks as selected none of the ACIDE - A Configurable IDE file menu panel
	 * components.
	 */
    public void selectNone() {
        for (int index = 0; index < AcideMainWindow.getInstance().getMenu().getFileMenu().getItemCount(); index++) {
            JMenuItem menuItem = null;
            try {
                menuItem = AcideMainWindow.getInstance().getMenu().getFileMenu().getItem(index);
                if (menuItem != null) {
                    _components.get(menuItem.getName()).setSelected(false);
                }
            } catch (ClassCastException exception) {
                JMenu menu = (JMenu) AcideMainWindow.getInstance().getMenu().getFileMenu().getMenuComponent(index);
                if (menu != null) {
                    _components.get(menu.getName()).setSelected(false);
                }
            }
        }
    }
}
