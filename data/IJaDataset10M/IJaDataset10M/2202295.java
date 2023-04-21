package org.jpos.util;

/**
 * LogSources can choose to extends this SimpleLogSource
 *
 * @author apr@cs.com.uy
 * @version $Id: SimpleLogSource.java 2854 2010-01-02 10:34:31Z apr $
 * @see LogSource
 */
public class SimpleLogSource implements LogSource {

    protected Logger logger;

    protected String realm;

    public SimpleLogSource() {
        super();
        logger = null;
        realm = null;
    }

    public SimpleLogSource(Logger logger, String realm) {
        setLogger(logger, realm);
    }

    public void setLogger(Logger logger, String realm) {
        this.logger = logger;
        this.realm = realm;
    }

    public String getRealm() {
        return realm;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void info(String detail) {
        Logger.log(new LogEvent(this, "info", detail));
    }

    public void info(String detail, Object obj) {
        LogEvent evt = new LogEvent(this, "info", detail);
        evt.addMessage(obj);
        Logger.log(evt);
    }

    public void warning(String detail) {
        Logger.log(new LogEvent(this, "warning", detail));
    }

    public void warning(String detail, Object obj) {
        LogEvent evt = new LogEvent(this, "warning", detail);
        evt.addMessage(obj);
        Logger.log(evt);
    }

    public void error(String detail) {
        Logger.log(new LogEvent(this, "error", detail));
    }

    public void error(String detail, Object obj) {
        LogEvent evt = new LogEvent(this, "error", detail);
        evt.addMessage(obj);
        Logger.log(evt);
    }
}
