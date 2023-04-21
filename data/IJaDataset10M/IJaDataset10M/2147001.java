package ru.amse.jsynchro.net;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadConfig {

    public static final String CONFIG_UPDATE = "update_config_period";

    public static final String USERS_FOLDER = "users_folder";

    public static final String USERS_ACCOUNT = "users_account_file";

    public static final String PORT = "port";

    public static Map<String, String> load(String configName) {
        try {
            DataInput di = new DataInputStream(new FileInputStream(configName));
            Map<String, String> configMap = new HashMap<String, String>();
            String config = null;
            while ((config = di.readLine()) != null) {
                configMap.put(config.substring(0, config.indexOf(" ") - 1), config.substring(config.indexOf(" ") + 1, config.length()));
            }
            return configMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unused")
    private static boolean isValid() {
        return true;
    }
}
