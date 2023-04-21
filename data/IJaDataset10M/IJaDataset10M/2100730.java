package tools;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyManager {

    ResourceBundle bundle;

    Properties prop;

    String propertyfile;

    public PropertyManager(String propertyfile) {
        prop = new Properties();
        try {
            this.propertyfile = propertyfile;
            prop.load(new FileInputStream(propertyfile));
        } catch (Exception e) {
        }
    }
}
