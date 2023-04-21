package jset;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class JavaListenerData extends HashMap<String, List<String>> {

    private static final String LISTENER_FILE = "Listeners.properties";

    private static final long serialVersionUID = 1L;

    private static JavaListenerData instance;

    private JavaListenerData() {
    }

    public static JavaListenerData getInstance() {
        if (instance == null) {
            Properties props = new Properties();
            instance = new JavaListenerData();
            try {
                props.load(new FileInputStream(LISTENER_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Object key : props.keySet()) {
                String[] tokens = props.getProperty(key.toString()).split("\\|");
                instance.put(key.toString(), Arrays.asList(tokens));
            }
        }
        return instance;
    }
}
