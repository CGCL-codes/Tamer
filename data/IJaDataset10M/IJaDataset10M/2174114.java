package org.jtweet;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class TwitterOptions {

    public static final int TIMELINE_PUBLIC = 0, TIMELINE_FRIEND = 1, TIMELINE_USER = 2;

    public static final int[] TIMELINE_ARRAY = { TIMELINE_PUBLIC, TIMELINE_FRIEND, TIMELINE_USER };

    public static final int UPDATE_1M = 60000, UPDATE_3M = 180000, UPDATE_5M = 300000, UPDATE_10M = 600000, UPDATE_15M = 900000, UPDATE_30M = 1800000, UPDATE_60M = 3600000;

    public static final int[] UPDATE_ARRAY = { UPDATE_1M, UPDATE_3M, UPDATE_5M, UPDATE_10M, UPDATE_15M, UPDATE_30M, UPDATE_60M };

    private static final String PREF = "/TwitterOptions";

    private static final String USERNAME = "username", PASSWORD = "password", UPDATE_FREQ = "updateFreq", SHOW_TYPE = "showType", MAX_DISPLAY = "maxDisplay", PLAY_SOUND = "playSound", SOUND_FILE = "soundFile", PREF_SET = "prefSet";

    private static boolean prefSet;

    public static String username, password;

    public static int updateFreq, showType, maxDisplay;

    public static boolean playSound;

    public static String soundFile;

    private TwitterOptions() {
    }

    public static void loadDefaultOptions() {
        username = "";
        password = "";
        updateFreq = UPDATE_1M;
        showType = TIMELINE_PUBLIC;
        maxDisplay = 2;
        playSound = false;
        soundFile = "sounds/notify_original.aiff";
    }

    public static void loadOptions() {
        Preferences pref = Preferences.userRoot().node(PREF);
        prefSet = pref.getBoolean(PREF_SET, false);
        if (prefSet) {
            username = pref.get(USERNAME, "");
            password = pref.get(PASSWORD, "");
            updateFreq = pref.getInt(UPDATE_FREQ, UPDATE_1M);
            showType = pref.getInt(SHOW_TYPE, TIMELINE_PUBLIC);
            maxDisplay = pref.getInt(MAX_DISPLAY, 2);
            playSound = pref.getBoolean(PLAY_SOUND, false);
            soundFile = pref.get(SOUND_FILE, "sounds/notify_original.aiff");
        } else {
            loadDefaultOptions();
        }
    }

    public static void saveOptions() {
        Preferences pref = Preferences.userRoot().node(PREF);
        try {
            pref.clear();
            pref.put(USERNAME, username);
            pref.put(PASSWORD, password);
            pref.putInt(UPDATE_FREQ, updateFreq);
            pref.putInt(SHOW_TYPE, showType);
            pref.putInt(MAX_DISPLAY, maxDisplay);
            pref.putBoolean(PLAY_SOUND, playSound);
            pref.put(SOUND_FILE, soundFile);
            pref.putBoolean(PREF_SET, true);
            pref.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}
