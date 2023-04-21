package org.esme.kroak.core;

import java.util.ArrayList;

public class Preference {

    private ArrayList<String> preferences;

    public static int POS_USER_NAME = 0;

    public static int POS_USER_ADRESS = 1;

    public static int POS_USER_HOOK = 2;

    public static int POS_USER_AVATAR = 3;

    public static int POS_USER_PROXY = 4;

    public static int POS_USER_SAVE_FILE = 5;

    public static int POS_USER_POLICE = 6;

    public static int POS_USER_FONT = 7;

    public static int POS_USER_WRITECOLOR = 8;

    public static int POS_USER_SKIN = 9;

    public static int POS_USER_EMOTICON = 10;

    private static int MAX_SIZE_USEFUL = 5;

    private static int MAX_SIZE = 11;

    public Preference() {
        preferences = new ArrayList<String>(MAX_SIZE);
        for (int i = 0; i < MAX_SIZE_USEFUL; i++) preferences.add(i, "");
        preferences.add(POS_USER_SAVE_FILE, "Preferences.txt");
        preferences.add(POS_USER_POLICE, " ");
        preferences.add(POS_USER_FONT, " ");
        preferences.add(POS_USER_WRITECOLOR, " ");
        preferences.add(POS_USER_SKIN, " ");
        preferences.add(POS_USER_EMOTICON, " ");
    }

    /**
	 * 
	 * @param index: l'emplacement
	 * @param element: la liste des parametres
	 */
    public void setElement(int index, String element) {
        if (index > MAX_SIZE) {
            return;
        }
        preferences.set(index, element);
    }

    /**
	 * 
	 * @param index: l'emplacement
	 * @return l'element: a l'emplacement index
	 */
    public String getElement(int index) {
        if (index > MAX_SIZE) {
            return "0";
        }
        return preferences.get(index);
    }
}
