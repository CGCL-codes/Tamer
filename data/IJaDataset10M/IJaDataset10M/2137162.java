package net.sf.drawbridge.util;

import java.util.Properties;

public interface StringPropertiesConverter {

    public static final String BEAN_NAME = "StringPropertiesConverter";

    public String convertToString(Properties properties);

    public Properties convertToProperties(String string) throws Exception;
}
