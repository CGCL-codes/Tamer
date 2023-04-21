package jeevlet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {

    private static Properties props = null;

    public static Properties getProps(String propertyFilePath) throws IOException {
        if (props == null) {
            props = new Properties();
            File propFile = new File(propertyFilePath);
            InputStream stream = new FileInputStream(propFile);
            props.load(stream);
        }
        return props;
    }
}
