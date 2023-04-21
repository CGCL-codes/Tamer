package org.yournamehere.client.admin;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import org.yournamehere.client.ScrumManagerPanel;

/**
 * Generate a pannel with the product list.
 * @author David
 */
public class UserPanel extends ScrumManagerPanel {

    /**
     * constructor of the class.
     */
    public UserPanel(String name, String group, Boolean stored, Canvas canvasPrincipal) {
        this.canvasPrincipal = canvasPrincipal;
        showPersonWindow(name, group, stored);
    }

    /**
     * show the informations about the given person
     * @param idItemSelected
     * @param name
     * @param group
     * @param stored
     */
    private void showPersonWindow(String name, String group, Boolean stored) {
        windowPrincipal = new Window();
        final int widthWindowPrincipal = 240;
        final int heightWindowPrincipal = 150;
        configureWindow(windowPrincipal, widthWindowPrincipal, heightWindowPrincipal, constants.personDetails());
        windowPrincipal.addItem(panelUser(name, group, stored, widthWindowPrincipal, heightWindowPrincipal));
        canvasPrincipal.addChild(windowPrincipal);
        canvasPrincipal.show();
    }

    /**
     * generate a pane with the given information
     * @param name
     * @param group
     * @param store
     * @param width
     * @param height
     * @return
     */
    private HTMLPane panelUser(final String name, final String group, boolean store, final int width, final int height) {
        HTMLPane htmlPane = new HTMLPane();
        String nameItem = "<u><b>" + constants.nameUser() + ": </b></u>" + name;
        String groupItem = "<u><b>" + constants.group() + ": </b></u>" + group;
        String storeItem = "<u><b>" + constants.store() + ": </b></u>" + store;
        htmlPane.setContents("<p align = \"center\"><br/><br/>" + nameItem + "<br/><br/><br/>" + groupItem + "<br/><br/><br/>" + storeItem + "</p>");
        htmlPane.setWidth(width);
        htmlPane.setHeight(height);
        return htmlPane;
    }
}
