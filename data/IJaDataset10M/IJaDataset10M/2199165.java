package org.systemsbiology.apps.corragui.client.widget;

import org.systemsbiology.apps.corragui.client.constants.MenuCategory;

/**
 * Mediator class for passing messages between a IMenu widget and its related IDetails widget.
 * @author Vagisha Sharma
 * @version 1.0
 */
public class WidgetsMediator {

    private final IGuiMediator guiMediator;

    private IMenu menu;

    private IDetails details;

    /**
	 * Creates an new WidgetsMediator
	 * @param guiMediator -- main mediator between the MainMenu and DetailsPanel widgets.
	 */
    public WidgetsMediator(IGuiMediator guiMediator) {
        this.guiMediator = guiMediator;
    }

    /**
	 * Sets the IMenu for this mediator.
	 * @param menu
	 */
    public void setMenu(IMenu menu) {
        this.menu = menu;
    }

    protected IMenu getMenu() {
        return this.menu;
    }

    /**
	 * Sets the IDetails for this mediator.
	 * @param details
	 */
    public void setDetails(IDetails details) {
        this.details = details;
    }

    protected IDetails getDetails() {
        return this.details;
    }

    /**
	 * Informs the IDetails widget to clear itself.
	 */
    public void clearCurrentDetails() {
        if (details != null) details.clearDetails();
    }

    /**
	 * Informs the IDetails widgets of the sub-menu item selection.
	 * @param menuItem
	 */
    public void onMenuItemSelected(MenuCategory menuItem) {
        if (details != null) this.details.showDetails(menuItem);
    }

    /**
	 * Informs the IDetails widgets of the main menu item selection. 
	 */
    public void onMenuSelected() {
        if (details != null) this.guiMediator.onMenuSelected(menu.getMenuCategory());
    }

    /**
	 * Propagates exceptions encountered during request processing to the main widget mediator.
	 * @param caught
	 */
    public void propagateException(Throwable caught) {
        this.guiMediator.onException(caught);
    }
}
