package org.netbeams.sim.ysi;

import java.util.ArrayList;

/**
 * 
 * @author Teresa L. Johnson <gamma.particle@gmail.com>
 *
 */
public class SondeMainMenu extends SondeMenu {

    private static final String MAIN_MENU_TITLE = "-----------------Main------------------";

    private String mainMenuItem1 = "1-Run";

    private String mainMenuItem2 = "2-Calibrate";

    private String mainMenuItem3 = "3-File";

    private String mainMenuItem4 = "4-Status";

    private String mainMenuItem5 = "5-System";

    private String mainMenuItem6 = "6-Report";

    private String mainMenuItem7 = "7-Sensor";

    private String mainMenuItem8 = "8-Advanced";

    private ArrayList<String> mainMenuItems;

    public SondeMainMenu() {
        super();
        this.initMenuItems();
    }

    public String getMenuTitle() {
        return MAIN_MENU_TITLE;
    }

    public ArrayList<String> getMenuItems() {
        return mainMenuItems;
    }

    public void initMenuItems() {
        mainMenuItems = new ArrayList<String>();
        mainMenuItems.add(mainMenuItem1);
        mainMenuItems.add(mainMenuItem2);
        mainMenuItems.add(mainMenuItem3);
        mainMenuItems.add(mainMenuItem4);
        mainMenuItems.add(mainMenuItem5);
        mainMenuItems.add(mainMenuItem6);
        mainMenuItems.add(mainMenuItem7);
        mainMenuItems.add(mainMenuItem8);
    }

    public void displayMenu() {
        System.out.println(MAIN_MENU_TITLE);
        System.out.println(mainMenuItem1 + "                    " + mainMenuItem5);
        System.out.println(mainMenuItem2 + "              " + mainMenuItem6);
        System.out.println(mainMenuItem3 + "                   " + mainMenuItem7);
        System.out.println(mainMenuItem4 + "                 " + mainMenuItem8);
        System.out.println();
        System.out.println(super.getMenuPrompt());
    }

    public static final void main(String[] args) {
        SondeMainMenu menu = new SondeMainMenu();
        menu.displayMenu();
    }
}
