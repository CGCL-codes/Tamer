package net.adrianromero.tpv.forms;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class AppLocal {

    private static List<ResourceBundle> m_messages;

    static {
        m_messages = new LinkedList<ResourceBundle>();
        m_messages.add(ResourceBundle.getBundle("net/adrianromero/tpv/i18n/messages"));
        m_messages.add(ResourceBundle.getBundle("com/openbravo/i18n/messageserp"));
    }

    /** Creates a new instance of AppLocal */
    private AppLocal() {
    }

    public static String getIntString(String sKey) {
        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : m_messages) {
                try {
                    return r.getString(sKey);
                } catch (MissingResourceException e) {
                }
            }
            return "** " + sKey + " **";
        }
    }

    public static String getIntString(String sKey, Object... sValues) {
        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : m_messages) {
                try {
                    return MessageFormat.format(r.getString(sKey), sValues);
                } catch (MissingResourceException e) {
                }
            }
            StringBuffer sreturn = new StringBuffer();
            sreturn.append("** ");
            sreturn.append(sKey);
            for (Object value : sValues) {
                sreturn.append(" < ");
                sreturn.append(value.toString());
            }
            sreturn.append("** ");
            return sreturn.toString();
        }
    }
}
