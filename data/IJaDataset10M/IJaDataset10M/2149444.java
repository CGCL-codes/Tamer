package org.wisigoth.chat.client.utils;

public class WisigothClientUtilities {

    public static String getHomeFolder() {
        String home = System.getenv("USERPROFILE");
        if (home == null) {
            home = System.getProperty("user.home");
        }
        return home;
    }
}
