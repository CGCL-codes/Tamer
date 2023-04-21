package cbr2.util;

import java.io.*;
import java.util.*;

public class CBRConfig extends Object {

    private static Properties props_;

    private static boolean initialized_;

    public static void main(String[] argv) {
        FileInputStream fis;
        if (argv.length == 0) {
            System.err.println("Please specify options file.");
            return;
        }
        props_ = new Properties();
        try {
            fis = new FileInputStream(argv[0]);
            props_.load(fis);
        } catch (Exception e) {
            System.err.println("Error opening options file:");
            e.printStackTrace();
        }
        if (initialized_ == false) {
            initialized_ = true;
            System.out.println("Config file set to " + argv[0]);
        } else {
            System.out.println("Config file changed to " + argv[0]);
        }
    }

    public static boolean isInitialized() {
        return initialized_;
    }

    public static String getProperty(String key) {
        if (initialized_ == false) {
            throw new IllegalStateException("Not initialized!");
        }
        return (String) props_.get(key);
    }
}
