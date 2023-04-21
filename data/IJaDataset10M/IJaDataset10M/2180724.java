package org.red5.server.net.rtmpt;

import java.util.Map;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Server;
import org.apache.catalina.Wrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.red5.server.TomcatLoader;
import org.red5.server.api.IServer;
import org.springframework.context.ApplicationContextAware;

/**
 * Loader for the RTMPT server which uses Tomcat.
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class TomcatRTMPTLoader extends TomcatLoader implements ApplicationContextAware {

    protected static Log log = LogFactory.getLog(TomcatRTMPTLoader.class.getName());

    protected Server rtmptServer;

    protected IServer server;

    protected RTMPTHandler handler;

    private Host host;

    private Context context;

    public void setServer(IServer server) {
        log.debug("RTMPT setServer");
        this.server = server;
    }

    public void setRTMPTHandler(RTMPTHandler handler) {
        log.debug("RTMPT setRTMPTHandler");
        this.handler = handler;
    }

    @Override
    public void init() {
        log.info("Loading RTMPT context");
        try {
            getApplicationContext();
        } catch (Exception e) {
            log.error("Error loading tomcat configuration", e);
        }
        host.addChild(context);
        log.debug("Null check - engine: " + (null == engine) + " host: " + (null == host));
        engine.addChild(host);
        embedded.addEngine(engine);
        embedded.addConnector(connector);
        try {
            log.info("Starting RTMPT engine");
            embedded.start();
        } catch (org.apache.catalina.LifecycleException e) {
            log.error("Error loading tomcat", e);
        }
    }

    /**
	 * Set a host
	 * 
	 * @param host
	 */
    public void setHost(Host host) {
        log.debug("RTMPT setHost");
        this.host = host;
    }

    public void setContext(Map<String, String> contextMap) {
        log.debug("RTMPT setContext (map)");
        context = embedded.createContext(contextMap.get("path"), contextMap.get("docBase"));
        context.setReloadable(false);
    }

    /**
	 * Set primary wrapper / servlet
	 * 
	 * @param wrapper
	 */
    public void setWrapper(Wrapper wrapper) {
        log.debug("RTMPT setWrapper");
        context.addChild(wrapper);
    }

    /**
	 * Set servlet mappings
	 * 
	 * @param mappings
	 */
    public void setMappings(Map<String, String> mappings) {
        log.debug("Servlet mappings: " + mappings.size());
        for (String key : mappings.keySet()) {
            context.addServletMapping(mappings.get(key), key);
        }
    }
}
