package org.coos.plugin.logserver.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.coos.actorframe.ActorSM;
import org.coos.messaging.util.Log;
import org.coos.messaging.util.LogFactory;

public class LogServerSM extends ActorSM {

    static Log LOG = LogFactory.getLog(LogServerSM.class);

    public static final String CONFIG_FILE_KEY = "logback.configurationFile";

    LogbackServerNode logbackServerNode;

    public LogServerSM() {
        setBehaviorClass(new LogServerCS("LogServer"));
    }

    protected void initInstance() {
        super.initInstance();
        setVisible(true);
    }

    /**
	 * Configure the Logback Server.
	 * 
	 * @param configuration
	 *            name of configuration file/resource
	 * @throws IOException
	 *             if configuration not found or could not be read
	 * @throws Exception
	 *             if parsing or unknown exception
	 */
    protected void configure(String configuration) throws IOException, Exception {
        InputStream is = getResource(configuration);
        logbackServerNode = new LogbackServerNode(this, is);
    }

    protected void processLoggingEvent(byte[] serializedEvent) {
        if (logbackServerNode == null) {
            LOG.warn("Using existing logback configuration.");
            logbackServerNode = new LogbackServerNode(this);
        }
        logbackServerNode.logTheEvent(serializedEvent);
    }

    private InputStream getResource(String resourceName) throws IOException {
        InputStream is = null;
        String baseName;
        if (resourceName.isEmpty()) {
            return null;
        }
        if (resourceName.startsWith("/")) {
            baseName = resourceName.substring(1);
        } else {
            baseName = resourceName;
            resourceName = "/" + resourceName;
        }
        try {
            File file = new File(".", baseName);
            URL url = file.toURI().toURL();
            is = url.openStream();
            if (is != null) {
                LOG.info("Found configuration " + url);
                return is;
            }
        } catch (Exception ignore) {
        }
        try {
            File file = new File(resourceName);
            URL url = file.toURI().toURL();
            is = url.openStream();
            if (is != null) {
                LOG.info("Found configuration " + url);
                return is;
            }
        } catch (Exception ignore) {
        }
        try {
            is = this.getClass().getResourceAsStream(resourceName);
            if (is != null) {
                LOG.info("Found configuration as resource " + resourceName);
                return is;
            }
        } catch (Exception ignore) {
        }
        throw new IOException("Cannot find " + resourceName);
    }
}
