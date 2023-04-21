package com.legstar.cixs.jbossesb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;
import com.legstar.coxb.host.HostContext;
import com.legstar.coxb.transform.AbstractTransformers;

/**
 * Actions derived from this class deal with esb messages which content is
 * pure zos data.
 * <p/>
 * The zos data is either a formatted LegStarMessage (if the correspondent on zos
 * is LegStar-aware) or raw zos data which is the case when the correspondent does
 * not use LegStar (case of WMQ for instance).
 * <p/>
 * Usage of LegStarMessage or not is determined dynamically, therefore a single
 * service can service both LegStar-aware and non LegStar-aware correspondents
 * at the same time.
 * <p/>
 * Esb messages, in and out, are assumed to hold content at the default
 * body location.
 */
public abstract class AbstractHostTransformerAction extends AbstractLegStarAction {

    /** Name of property holding the mainframe character set. */
    public static final String HOST_CHARSET_PROPERTY = "hostCharset";

    /** Mainframe character set. */
    private String mHostCharset;

    /** Name of property holding keyname to append on objects. */
    public static final String MAP_KEY_NAME_PROPERTY = "mapKeyName";

    /** Key name used when java objects are wrapped in a java.util.Map entry.
     * Null value means java object is not wrapped to a map entry */
    private String mMapKeyName;

    /** A boolean property indicating that peer expects host data to be wrapped as a LegStarMessage. */
    public static final String IS_LEGSTAR_MESSAGING = "isLegStarMessaging";

    /** Logger. */
    private final Log _log = LogFactory.getLog(getClass());

    /**
     * Standard constructor. The configuration parameters supported are:
     * <ul>
     *  <li>hostCharset: The mainframe character set. Default=IBM01140</li>
     *  <li>mapKeyName: Key name used to map Java objects. Default=none</li>
     * </ul>
     * @param config parameters setup in jboss-esb.xml
     * @throws ConfigurationException if configuration parameters are invalid
     */
    public AbstractHostTransformerAction(final ConfigTree config) throws ConfigurationException {
        super(config);
        mHostCharset = config.getAttribute(HOST_CHARSET_PROPERTY, HostContext.getDefaultHostCharsetName());
        mMapKeyName = config.getAttribute(MAP_KEY_NAME_PROPERTY);
        if (_log.isDebugEnabled()) {
            _log.debug("Deploying LegStar Transformer action with parameters:");
            _log.debug(HOST_CHARSET_PROPERTY + " : " + mHostCharset);
            _log.debug(MAP_KEY_NAME_PROPERTY + " : " + mMapKeyName);
        }
    }

    /**
     * @return the transformers set to use for java to host transformations
     */
    public abstract AbstractTransformers getTransformers();

    /**
     * Replaces the default body of an esb message with new content.
     * @param content the new content
     * @param esbMessage the esb message
     * @return the esb message with new content
     */
    public Message changeMessageContent(final Message esbMessage, final Object content) {
        esbMessage.getBody().add(content);
        return esbMessage;
    }

    /**
     * Checks an ESB message properties to detect if peer is expecting host data
     * to be wrapped as a LegStarMessage.
     * @param esbMessage the ESB message containing host data
     * @return true if there is proof that upstream action requested LegStarMessage wrapping
     */
    public static boolean isLegStarMessaging(final Message esbMessage) {
        Properties properties = esbMessage.getProperties();
        if (properties != null) {
            Boolean isLegStarMessage = (Boolean) esbMessage.getProperties().getProperty(IS_LEGSTAR_MESSAGING);
            if (isLegStarMessage != null && isLegStarMessage) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mark an ESB Message to signal if the peer requests the host data to be
     * wrapped in a LegStarMessage or not.
     * @param esbMessage the ESB message to mark
     * @param isLegStarMessaging whether to mark with true or false
     */
    public static void setLegStarMessaging(final Message esbMessage, final boolean isLegStarMessaging) {
        esbMessage.getProperties().setProperty(IS_LEGSTAR_MESSAGING, Boolean.valueOf(isLegStarMessaging));
    }

    /**
     * Gives a chance for a message to carry the mainframe character set.
     * @param message the esb message
     * @return the Target Mainframe character set from the
     * message properties or the configured one
     */
    public String getHostCharset(final Message message) {
        if (message.getProperties() != null) {
            return (String) message.getProperties().getProperty(HOST_CHARSET_PROPERTY, mHostCharset);
        }
        return getHostCharset();
    }

    /**
     * @return the Mainframe character set
     */
    public String getHostCharset() {
        if (mHostCharset == null || mHostCharset.length() == 0) {
            return HostContext.getDefaultHostCharsetName();
        }
        return mHostCharset;
    }

    /**
     * @return the key name used when java objects are wrapped in a java.util.Map entry
     */
    public String getMapKeyName() {
        return mMapKeyName;
    }
}
