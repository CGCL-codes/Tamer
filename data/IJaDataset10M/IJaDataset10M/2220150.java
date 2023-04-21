package cn.ac.ntarl.umt.notification;

import java.util.Properties;
import cn.ac.ntarl.umt.config.Config;

public class MailConfig {

    public Properties getProperties() {
        if (prop == null) {
            prop = new Properties();
            Config config = Config.getInstance();
            prop.setProperty("mail.template.path", config.getStringProp("mail.template.path", ""));
            boolean needAuth = config.getBooleanProp("mail.smtp.needauth", false);
            if (needAuth) prop.setProperty("mail.smtp.auth", "true"); else prop.setProperty("mail.smtp.auth", "false");
            prop.setProperty("mail.smtp.host", config.getStringProp("mail.smtp.host", ""));
            prop.setProperty("mail.smtp.username", config.getStringProp("mail.smtp.username", ""));
            prop.setProperty("mail.smtp.password", config.getStringProp("mail.smtp.password", ""));
            prop.setProperty("mail.smtp.from", config.getStringProp("mail.smtp.from", ""));
        }
        return prop;
    }

    public static MailConfig getInstance() {
        if (_instance == null) {
            _instance = new MailConfig();
        }
        return _instance;
    }

    void setProperties(Properties prop) {
        this.prop = prop;
    }

    private static MailConfig _instance;

    private Properties prop;
}
