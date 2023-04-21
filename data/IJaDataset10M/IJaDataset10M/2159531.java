package jp.ac.nitech.matlab.najm.server;

import java.io.FileInputStream;
import java.util.Properties;

public class SSetting {

    private Properties props;

    private void init() {
        props = new Properties();
        try {
            props.load(new FileInputStream("conf/server.conf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSetting(String key) {
        return this.props.getProperty(key);
    }

    private static SSetting instance = new SSetting();

    private SSetting() {
        init();
    }

    public static SSetting getInstance() {
        return instance;
    }
}
