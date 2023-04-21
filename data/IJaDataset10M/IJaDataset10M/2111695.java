package org.systemsbiology.apps.gui.client.constants;

/**
 * Sub menu items for the Administrative menu.
 * 
 * @author Vagisha Sharma
 * @author Mark Christiansen
 * @version 1.0
 */
public class AdminSubMenuCategory extends MenuCategory {

    /**
	 * Sub Menu Category for Mass Spec Machines 
	 */
    public static final AdminSubMenuCategory MASS_SPEC = new AdminSubMenuCategory("Mass Spectrometry Instruments");

    /**
     * Sub Menu Category for Organisms
     */
    public static final AdminSubMenuCategory ORGANISMS = new AdminSubMenuCategory("Organisms");

    /**
     * Sub Menu Category for Projects
     */
    public static final AdminSubMenuCategory PROJECTS = new AdminSubMenuCategory("Projects");

    /**
     * Sub Menu Category for Users
     */
    public static final AdminSubMenuCategory USERS = new AdminSubMenuCategory("Users");

    private AdminSubMenuCategory(String name) {
        super(name);
    }
}
