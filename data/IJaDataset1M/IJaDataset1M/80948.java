package org.escapek.i18n;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * Manages access to translation messages
 * @author nicolasjouanin
 *
 */
public class MessageService {

    private Properties messages;

    public MessageService() {
        messages = new Properties();
    }

    public MessageService(Properties prop) {
        messages = prop;
    }

    /**
	 * Get a translated message
	 * @param key message key to look for
	 * @return the message or !key! if not found
	 */
    public String getString(String key) {
        String m = messages.getProperty(key);
        if (m == null) return "!" + key + "!";
        return m;
    }

    /**
	 * Get a translated message and formats it
	 * @param key message key to look for
	 * @param arg object to display in format
	 * @return the message or !key! if not found
	 */
    public String getFormattedString(String key, Object arg) {
        String m = messages.getProperty(key);
        if (m == null) return "!" + key + "!";
        if (arg == null) arg = "";
        return MessageFormat.format(m, new Object[] { arg });
    }

    /**
	 * Get a translated message and formats it
	 * @param key message key to look for
	 * @param args[] objects to display in format
	 * @return the message or !key! if not found
	 */
    public String getFormattedString(String key, String[] args) {
        String m = messages.getProperty(key);
        return MessageFormat.format(m, (Object[]) args);
    }
}
