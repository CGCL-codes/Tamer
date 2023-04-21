package org.sgodden.echo.ext20.testapp;

import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import org.sgodden.echo.ext20.Button;
import org.sgodden.echo.ext20.Menu;
import org.sgodden.echo.ext20.MenuItem;
import org.sgodden.echo.ext20.Panel;
import org.sgodden.echo.ext20.Toolbar;

/**
 * Example of nested menus.
 * @author rcharlton
 *
 */
@SuppressWarnings("serial")
public class MenuTest extends Panel {

    private int invocationCount = 0;

    /**
	 * A simple menu with s 2 levels.
	 */
    public MenuTest() {
        super("Menu Test");
        createToolbar();
    }

    private MenuItem subMenuItem;

    /**
	 * Create the toolbar for the panel 
	 */
    private void createToolbar() {
        Toolbar toolbar = new Toolbar();
        setToolbar(toolbar);
        Button button1 = new Button("Button1");
        button1.isHoverMenu(true);
        toolbar.add(button1);
        Menu menu = new Menu();
        button1.setMenu(menu);
        MenuItem mi = new MenuItem("Update the sub-menu below");
        menu.add(mi);
        MenuItem mi2 = new MenuItem("Menu item with sub-menu");
        menu.add(mi2);
        final Menu subMenu = new Menu();
        mi2.add(subMenu);
        subMenuItem = new MenuItem("Sub-menu item");
        subMenu.add(subMenuItem);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                subMenu.remove(subMenuItem);
                subMenuItem = new MenuItem("Sub-menu item: update " + (++invocationCount));
                subMenu.add(subMenuItem);
            }
        });
    }
}
