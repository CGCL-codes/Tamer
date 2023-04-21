package com.techjedi.dragonbot.simple;

import java.util.List;
import com.techjedi.dragonbot.Location;
import com.techjedi.dragonbot.MenuItem;
import com.techjedi.dragonbot.Player;

/**
 * Simple common base class for realm locations.
 * 
 * @author Doug Bateman
 */
public class SimpleLocation implements Location {

    /**
     * The display name of the location.
     */
    private String name;

    /**
     * The short description of the location.
     */
    private String shortDescription;

    /**
     * The detailed description of the location and scene.
     */
    private String longDescription;

    /**
     * The menu to display for the location.
     */
    private List<MenuItem> menu;

    /**
     * @see com.techjedi.dragonbot.Location#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see com.techjedi.dragonbot.Location#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the short description of the location, usually 1 line.
     * 
     * @return Returns the shortDescription.
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Set the short description of the location, usually 1 line.
     * 
     * @param shortDescription
     *            The shortDescription to set.
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Get the detailed description of the location and scene.
     * 
     * @return Returns the long description
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Set the detailed description of the location and scene.
     * 
     * @param longDescription
     *            The longDescription to set.
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Get the menu to display for the location.
     * 
     * @return Returns the menu.
     */
    public List<MenuItem> getMenu() {
        return menu;
    }

    /**
     * Set the menu to display for the location.
     * 
     * @param menu
     *            The menu to set.
     */
    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
        for (MenuItem item : menu) {
            item.setLocation(this);
        }
    }

    /**
     * Process input from the player while at this location.
     * 
     * @param player
     *            The player sending the input.
     * @param input
     *            The input String, often a menu selection.
     */
    public void processPlayerInput(Player player, String input) {
        char cmd = getCommand(input);
        List<MenuItem> menu = getMenu();
        for (MenuItem item : menu) {
            if (item.getHotKey() == cmd) {
                String args = getArgs(input);
                item.processInput(player, args);
                return;
            }
        }
        showDescription(player);
        showMenu(player);
    }

    /**
     * Sends the player the description for this location.
     */
    public void showDescription(Player player) {
        boolean shortDesc = player.isShowShortDescriptions();
        String desc = (shortDesc) ? getShortDescription() : getLongDescription();
        player.tell(desc);
    }

    /**
     * Display the menu for this location
     * @param player The player receiving the menu.
     */
    public void showMenu(Player player) {
        StringBuffer menuBuf = new StringBuffer();
        for (MenuItem item : menu) {
            if (item.isVisible(player)) {
                String name = item.getDisplayString();
                menuBuf.append(name);
                menuBuf.append("\r\n");
            }
        }
        String menu = menuBuf.toString();
        if (menu.length() >= 0) {
            player.tell(menu);
        }
    }

    /**
     * Get the character for the menu item selected from the input string.
     * @param input User input string.
     * @return The character for the menu item selected.
     */
    private static char getCommand(String input) {
        if ((input == null) || (input.length() <= 0) || ((input.length() >= 2) && (input.charAt(1) != ' '))) {
            return ' ';
        }
        char hotKey = input.charAt(0);
        return hotKey;
    }

    /**
     * Get any arguments passed to the input String, if any.
     * @param input User input string.
     * @return the input after the menu letter, if any.  Empty string if none.
     */
    private static String getArgs(String input) {
        if (input.length() <= 1) return ""; else return input.substring(2);
    }
}
