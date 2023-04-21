package org.njo.webapp.root.utility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;
import org.njo.webapp.root.model.dao.MessageResourcesDAO;

/**
 * 从数据库中取得消息资源.
 *
 * @author yu.peng
 * @version 0.01
 */
public class DBMessageResources extends PropertyMessageResources {

    /**
     * Construct a new PropertyMessageResources according to the
     * specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     */
    public DBMessageResources(MessageResourcesFactory factory, String config) {
        super(factory, config);
        log.trace("Initializing, config='" + config + "'");
    }

    /**
     * Construct a new PropertyMessageResources according to the
     * specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     * @param returnNull The returnNull property we should initialize with
     */
    public DBMessageResources(MessageResourcesFactory factory, String config, boolean returnNull) {
        super(factory, config, returnNull);
        log.trace("Initializing, config='" + config + "', returnNull=" + returnNull);
    }

    /**
     * The <code>Log</code> instance for this class.
     */
    protected static final Log log = LogFactory.getLog(DBMessageResources.class);

    /**
     * The cache of messages we have accumulated over time, keyed by the
     * value calculated in <code>messageKey()</code>.
     */
    protected HashMap<String, String> dbMessages = new HashMap<String, String>();

    protected boolean alreadyLoaded = false;

    /**
     * Returns a text message for the specified key, for the default Locale.
     * A null string result will be returned by this method if no relevant
     * message resource is found for this key or Locale, if the
     * <code>returnNull</code> property is set.  Otherwise, an appropriate
     * error message will be returned.
     * <p>
     * This method must be implemented by a concrete subclass.
     *
     * @param locale The requested message Locale, or <code>null</code>
     *  for the system default Locale
     * @param key The message key to look up
     * @return text message for the specified key and locale
     */
    public String getMessage(Locale locale, String key) {
        if (log.isDebugEnabled()) {
            log.debug("getMessage(" + locale + "," + key + ")");
        }
        String localeKey = null;
        String messageKey = null;
        String message = null;
        if (!alreadyLoaded) {
            synchronized (dbMessages) {
                loadMessages();
            }
        }
        localeKey = localeKey(locale);
        messageKey = messageKey(localeKey, key);
        message = (String) dbMessages.get(messageKey);
        if (message != null) {
            return (message);
        }
        if (!defaultLocale.equals(locale)) {
            localeKey = localeKey(defaultLocale);
            messageKey = messageKey(localeKey, key);
            message = (String) dbMessages.get(messageKey);
            if (message != null) {
                return (message);
            }
        }
        localeKey = "";
        messageKey = messageKey(localeKey, key);
        message = (String) dbMessages.get(messageKey);
        if (message != null) {
            return (message);
        }
        return super.getMessage(locale, key);
    }

    public void reset() {
        alreadyLoaded = false;
        this.formats.clear();
        this.messages.clear();
        this.locales.clear();
        this.dbMessages.clear();
    }

    /**
     * 从数据库中读取消息.
     * 
     * @param localeKey Locale key for the messages to be retrieved
     */
    protected void loadMessages() {
        if (alreadyLoaded) {
            return;
        }
        synchronized (dbMessages) {
            Connection connection = null;
            try {
                connection = SystemContext.openConnection();
                MessageResourcesDAO messageResourcesDAO = new MessageResourcesDAO(connection);
                List messageList = messageResourcesDAO.selectAllRecordsI18N(1, Integer.MAX_VALUE);
                for (int i = 0; i < messageList.size(); i++) {
                    String[] msg = (String[]) messageList.get(i);
                    String messageKey = nullToEmpty(msg[0]);
                    String messageLocaleKey = nullToEmpty(msg[1]);
                    if ("default".equals(messageLocaleKey)) {
                        messageLocaleKey = "";
                    }
                    String messageContent = nullToEmpty(msg[2]);
                    String originalKey = messageKey(messageLocaleKey, messageKey);
                    if (log.isTraceEnabled()) {
                        log.trace("  Saving message key '" + originalKey);
                    }
                    dbMessages.put(originalKey, messageContent);
                }
            } catch (SQLException e) {
                if (log.isErrorEnabled()) {
                    log.error("loadMessages SQLException.", e);
                }
            } finally {
                try {
                    if (connection != null) {
                        SystemContext.closeConnection(connection);
                    }
                } catch (Throwable tw) {
                }
            }
        }
        alreadyLoaded = true;
    }

    private static String nullToEmpty(String key) {
        if (key == null) {
            return "";
        }
        return key.trim();
    }
}
