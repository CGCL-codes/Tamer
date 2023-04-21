package org.tglman.jsdms;

import java.io.InputStream;
import java.util.Properties;

public abstract class JsdmsFacadeFactory {

    static {
        try {
            Properties prop = new Properties();
            InputStream conf = JsdmsFacadeFactory.class.getClassLoader().getResourceAsStream("META-INF/abstractFactory.properties");
            prop.load(conf);
            Class.forName(prop.getProperty("jsdms.abstractFactory"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static JsdmsFacadeFactory instance;

    public static JsdmsFacadeFactory getInstance() {
        return instance;
    }

    public abstract JsdmsFacade getFacade();
}
