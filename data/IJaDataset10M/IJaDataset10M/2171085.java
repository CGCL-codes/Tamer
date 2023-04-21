package org.azrul.mewit.client;

import com.wavemaker.runtime.RuntimeAccess;

/**
 * This is a client-facing service class.  All
 * public methods will be exposed to the client.  Their return
 * values and parameters will be passed to the client or taken
 * from the client, respectively.  This will be a singleton
 * instance, shared between all requests. 
 * 
 * To log, call the superclass method log(LOG_LEVEL, String) or log(LOG_LEVEL, String, Exception).
 * LOG_LEVEL is one of FATAL, ERROR, WARN, INFO and DEBUG to modify your log level.
 * For info on these levels, look for tomcat/log4j documentation
 */
public class Session extends com.wavemaker.runtime.javaservice.JavaServiceSuperClass {

    public Session() {
        super(INFO);
    }

    public String sampleJavaOperation() {
        String result = null;
        try {
            log(INFO, "Starting sample operation");
            result = "Hello World";
            log(INFO, "Returning " + result);
        } catch (Exception e) {
            log(ERROR, "The sample java service operation has failed", e);
        }
        return result;
    }

    public String getCurrentUserId() {
        String userId = (String) RuntimeAccess.getInstance().getSession().getAttribute("USER_ID");
        return userId;
    }

    public String getCurrentSessionId() {
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        return sessionId;
    }

    public void setCurrentSelectedItem(String itemId) {
        RuntimeAccess.getInstance().getSession().setAttribute("CURRENT_ITEM_ID", itemId);
    }
}
