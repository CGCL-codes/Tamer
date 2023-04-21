package net.sf.proximitydeployment.core;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Counter {

    private static int count = 0;

    private static Properties projectProperties;

    static {
        URL result = Thread.currentThread().getContextClassLoader().getResource("config.properties");
        projectProperties = new Properties();
        try {
            projectProperties.load(result.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBuildNumber() {
        return projectProperties.getProperty("buildnumber");
    }

    public static void incrementCount() {
        count++;
    }

    public static int getCount() {
        return count;
    }
}
