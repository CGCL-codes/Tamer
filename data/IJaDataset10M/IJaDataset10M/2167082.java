package com.legstar.cixs.jbossesb.model.options;

import java.util.Map;
import java.util.Properties;
import com.legstar.codegen.CodeGenMakeException;
import com.legstar.codegen.models.AbstractPropertiesModel;

/**
 * Set of parameters needed for JBoss Messaging transport.
 */
public class JbmTransportParameters extends AbstractPropertiesModel {

    /** The default suffix for JBM Queue name receiving requests.*/
    public static final String DEFAULT_REQUEST_QUEUE_SUFFIX = "_Request_gw";

    /** The default suffix for JBM Queue name receiving replies.*/
    public static final String DEFAULT_REPLY_QUEUE_SUFFIX = "_Request_gw_reply";

    /** The JBM Queue name receiving replies.*/
    private String _replyQueue;

    /** The JBM Queue name receiving requests.*/
    private String _requestQueue;

    /** JBM request queue. */
    public static final String JBM_REQUEST_QUEUE = "jbmRequestQueue";

    /** JBM replay queue. */
    public static final String JBM_REPLY_QUEUE = "jbmReplyQueue";

    /**
     * Default constructor.
     */
    public JbmTransportParameters() {
    }

    /**
     * Construct from properties.
     * @param props a set of properties
     */
    public JbmTransportParameters(final Properties props) {
        super(props);
        setRequestQueue(getString(props, JBM_REQUEST_QUEUE, null));
        setReplyQueue(getString(props, JBM_REPLY_QUEUE, null));
    }

    /**
     * Default values are dependent on the service name.
     * This special method allows the caller to set deafult values.
     * @param serviceName the service name
     */
    public void initialize(final String serviceName) {
        if (getRequestQueue() == null || getRequestQueue().length() == 0) {
            setRequestQueue(serviceName + DEFAULT_REQUEST_QUEUE_SUFFIX);
        }
        if (getReplyQueue() == null || getReplyQueue().length() == 0) {
            setReplyQueue(serviceName + DEFAULT_REPLY_QUEUE_SUFFIX);
        }
    }

    /**
     * JBM parameters are expected by templates to come from a parameters map.
     * @param parameters a parameters map to which JBM parameters must be added
     */
    public void add(final Map<String, Object> parameters) {
        parameters.put(JBM_REQUEST_QUEUE, getRequestQueue());
        parameters.put(JBM_REPLY_QUEUE, getReplyQueue());
    }

    /**
     * Check that parameters are set correctly.
     * Generates default values if needed.
     * @throws CodeGenMakeException if parameters are missing or wrong
     */
    public void check() throws CodeGenMakeException {
        if (getRequestQueue() == null || getRequestQueue().length() == 0) {
            throw new CodeGenMakeException("You must specify a JBoss Messaging Target request queue");
        }
        if (getReplyQueue() == null || getReplyQueue().length() == 0) {
            throw new CodeGenMakeException("You must specify a JBoss Messaging Target reply queue");
        }
    }

    /**
     * @return the JBM Queue name receiving requests
     */
    public String getRequestQueue() {
        return _requestQueue;
    }

    /**
     * @param requestQueue the JBM Queue name receiving requests
     */
    public void setRequestQueue(final String requestQueue) {
        _requestQueue = requestQueue;
    }

    /**
     * @return the JBM Queue name receiving replies
     */
    public String getReplyQueue() {
        return _replyQueue;
    }

    /**
     * @param replyQueue the JBM Queue name receiving replies
     */
    public void setReplyQueue(final String replyQueue) {
        _replyQueue = replyQueue;
    }

    /**
     * @return a properties file holding the values of this object fields
     */
    public Properties toProperties() {
        Properties props = super.toProperties();
        putString(props, JBM_REQUEST_QUEUE, getRequestQueue());
        putString(props, JBM_REPLY_QUEUE, getReplyQueue());
        return props;
    }
}
