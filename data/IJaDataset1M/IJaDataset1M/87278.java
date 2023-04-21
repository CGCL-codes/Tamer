package org.omg.tacsit.ui;

import org.omg.tacsit.ui.viewport.AddViewportAction;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.omg.tacsit.common.ui.ExitAction;
import org.omg.tacsit.common.util.Factory;
import org.omg.tacsit.controller.TacsitController;
import org.omg.tacsit.controller.ViewportManager;
import org.omg.tacsit.ui.viewport.EntityViewport;

/**
 * The menu bar that provides the standard options for a TacsitFrame.
 * <p>
 * TODO: It would be better to externalize this from the TacsitFrame, and provide a Factory method that creates a
 * menu.  That way, the Tacsit Frame isn't tied to specific actions, and some of the awkward passthroughs would
 * be eliminated.
 * @author Matthew Child
 */
public class TacsitMenuBar extends JMenuBar {

    private AddViewportAction addViewportAction;

    private Map<String, JMenu> nameToMenu;

    /**
   * Creates a new instance.
   */
    public TacsitMenuBar() {
        nameToMenu = new HashMap();
        addFileMenu();
        addViewportMenu();
        addEntityMenu();
    }

    private void addFileMenu() {
        JMenu fileMenu = addMenu("File");
        fileMenu.add(new ExitAction());
    }

    private void addViewportMenu() {
        JMenu viewportMenu = addMenu("Viewport");
        addViewportAction = new AddViewportAction("Add Viewport", null);
        viewportMenu.add(addViewportAction);
    }

    private void addEntityMenu() {
        addMenu("Entity");
    }

    private JMenu addMenu(String menuName) {
        JMenu newMenu = new JMenu(menuName);
        nameToMenu.put(menuName, newMenu);
        add(newMenu);
        return newMenu;
    }

    private JMenu lazyGetMenu(String menuName) {
        JMenu menu;
        if (!nameToMenu.containsKey(menuName)) {
            menu = addMenu(menuName);
        } else {
            menu = nameToMenu.get(menuName);
        }
        return menu;
    }

    /**
   * Adds a menu item to a particular named menu.
   * @param menuName The name of the menu to add to.
   * @param menuItem The menu item to add.
   */
    public void addMenuItem(String menuName, JMenuItem menuItem) {
        JMenu menuForAction = lazyGetMenu(menuName);
        menuForAction.add(menuItem);
    }

    /**
   * Adds an action to a particular named menu.
   * @param menuName The name of the menu to add to.
   * @param action The action to add.
   */
    public void addAction(String menuName, Action action) {
        JMenu menuForAction = lazyGetMenu(menuName);
        menuForAction.add(action);
    }

    /**
   * Sets the factory used for creating new Viewports.
   * @param viewportFactory The factory used for creating new Viewports.
   */
    public void setViewportFactory(Factory<? extends EntityViewport> viewportFactory) {
        addViewportAction.setViewportFactory(viewportFactory);
    }

    /**
   * Gets the factory used for creating new Viewports.
   * @return The factory used for creating new Viewports.
   */
    public Factory<? extends EntityViewport> getViewportFactory() {
        return addViewportAction.getViewportFactory();
    }

    /**
   * Sets the TacsitController the menu actions should operate on.
   * @param tacsitController The tacsit controller to operate on.
   */
    public void setTacsitController(TacsitController tacsitController) {
        ViewportManager viewportManager = (tacsitController != null) ? tacsitController.getViewportManager() : null;
        addViewportAction.setViewportManager(viewportManager);
    }
}
