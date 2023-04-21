package net.nourdine.jp.dialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UIDialog {

    private static UIDialog instance = null;

    private static final String STRING_FILE = "strings.properties";

    private Properties prop = null;

    private UIDialog(Properties prop) {
        this.prop = prop;
    }

    public Properties getProperties() {
        return prop;
    }

    public static UIDialog getInstace() {
        if (instance == null) {
            try {
                InputStream stringsResIS = ClassLoader.getSystemResourceAsStream("META-INF/" + STRING_FILE);
                Properties p = new Properties();
                p.load(stringsResIS);
                instance = new UIDialog(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
