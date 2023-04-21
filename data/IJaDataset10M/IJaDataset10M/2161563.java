package org.rjam.base;

import java.util.List;
import java.util.Properties;
import org.rjam.Monitor;
import org.rjam.api.IComponent;
import org.rjam.xml.Token;

public class BaseComponent extends BaseLogging implements IComponent {

    private static final long serialVersionUID = 1L;

    private static final String PROP_LOG_LEVEL = "LogLevel";

    private String name = "UnNamed";

    private Properties properties = new Properties();

    private boolean debug;

    private Token config;

    public BaseComponent() {
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void configure(Token configToken) {
        if (configToken != null) {
            this.config = configToken;
            Token tmp = configToken.getChild("Name");
            if (tmp != null) {
                setName(tmp.getValue());
            }
            tmp = configToken.getChild("Debug");
            if (tmp != null) {
                String val = tmp.getValue();
                if (val != null && val.length() > 0) {
                    setDebug(Character.toLowerCase(val.charAt(0)) == 't');
                }
            }
            List<Token> list = configToken.getChildren("Property");
            if (list != null) {
                for (int idx = 0, sz = list.size(); idx < sz; idx++) {
                    tmp = (Token) list.get(idx);
                    Token tmp2 = tmp.getChild("Name");
                    if (tmp2 != null) {
                        Token tmp3 = tmp.getChild("Value");
                        String name = tmp2.getValue();
                        String val = null;
                        if (tmp3 != null) {
                            val = tmp3.getValue().trim();
                        }
                        setProperty(name, val);
                    }
                }
            }
            String log = getProperty(PROP_LOG_LEVEL);
            if (log != null && log.length() > 0) {
                int level = Logger.getLevel(log);
                if (level != Logger.LEVEL_NONE) {
                    getLogger().setLevel(level);
                    logInfo("Settin log level=" + log);
                }
            }
        }
    }

    public Token getConfig() {
        return config;
    }

    public String getName() {
        return name;
    }

    public String getProperty(String name) {
        String ret = Monitor.getProperty(this.name + "." + name);
        if (ret == null && name != null) {
            ret = properties.getProperty(name);
        }
        return ret;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperty(String name, String value) {
        if (name != null) {
            if (value == null) {
                properties.remove(name);
            } else {
                properties.setProperty(name, value);
            }
        }
    }

    public String getProperty(String name, String defaultValue) {
        String ret = getProperty(name);
        if (ret == null) {
            ret = defaultValue;
        }
        return ret;
    }
}
