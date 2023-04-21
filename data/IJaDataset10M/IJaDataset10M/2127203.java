package io;

import java.io.IOException;
import java.util.Calendar;
import resources.Messages;
import data.Settings;
import interfaces.IProfile;

/**
 * @author kreed
 *
 */
public class SettingsLoadTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        LoadSettings ls = new LoadSettings();
        Settings set = ls.getSettings();
        System.out.println(set);
        set.setDefaultProfile("nu profile");
        try {
            SaveSettings.write(set);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Settings reloaded");
        Settings set2 = ls.getSettings();
        System.out.println(set2);
    }
}
