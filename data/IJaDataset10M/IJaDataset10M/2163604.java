package org.atricore.idbus.idojos.serializedsessionstore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.kernel.main.session.exceptions.SSOSessionException;
import org.atricore.idbus.kernel.main.session.BaseSession;
import org.atricore.idbus.idojos.memorysessionstore.MemorySessionStore;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Session Store implementation which uses Java Serialization to persist Single Sign-On
 * user sessions.
 * It allows to reconstruct the session state after a system shutdown.
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Id: SerializedSessionStore.java 1040 2009-03-05 00:56:52Z gbrigand $
 *
 * @org.apache.xbean.XBean element="serialized-store"
 */
public class SerializedSessionStore extends MemorySessionStore {

    private static final Log logger = LogFactory.getLog(SerializedSessionStore.class);

    private HashMap _sessions;

    private boolean _loaded;

    private String _serializedFile = "sso_sessions.josso";

    public SerializedSessionStore() {
        super();
        _loaded = false;
    }

    public int getSize() throws SSOSessionException {
        checkLoad();
        return super.getSize();
    }

    public String[] keys() throws SSOSessionException {
        checkLoad();
        return super.keys();
    }

    public BaseSession[] loadAll() throws SSOSessionException {
        checkLoad();
        return super.loadAll();
    }

    public BaseSession load(String id) throws SSOSessionException {
        checkLoad();
        return super.load(id);
    }

    public BaseSession[] loadByUsername(String name) throws SSOSessionException {
        checkLoad();
        return super.loadByUsername(name);
    }

    public BaseSession[] loadByLastAccessTime(Date time) throws SSOSessionException {
        checkLoad();
        return super.loadByLastAccessTime(time);
    }

    public BaseSession[] loadByValid(boolean valid) throws SSOSessionException {
        checkLoad();
        return super.loadByValid(valid);
    }

    public void remove(String id) throws SSOSessionException {
        checkLoad();
        super.remove(id);
        synchronized (this) {
            _sessions.remove(id);
        }
        saveSerializedSessions();
    }

    public void clear() throws SSOSessionException {
        checkLoad();
        super.clear();
        synchronized (this) {
            _sessions.clear();
        }
        saveSerializedSessions();
    }

    public void save(BaseSession session) throws SSOSessionException {
        checkLoad();
        super.save(session);
        synchronized (this) {
            _sessions.put(session.getId(), session);
        }
        saveSerializedSessions();
    }

    private void saveSerializedSessions() throws SSOSessionException {
        try {
            FileOutputStream out = new FileOutputStream(getSerializedFile());
            ObjectOutputStream s = new ObjectOutputStream(out);
            synchronized (this) {
                s.writeObject(_sessions);
            }
            s.flush();
            s.close();
            out.close();
        } catch (IOException e) {
            throw new SSOSessionException(e.getMessage(), e);
        }
    }

    private void checkLoad() throws SSOSessionException {
        if (_loaded) return;
        load();
    }

    private synchronized void load() throws SSOSessionException {
        if (_loaded) return;
        FileInputStream in = null;
        ObjectInputStream s = null;
        logger.info("Loading serialized sessions from file : " + getSerializedFile());
        try {
            in = new FileInputStream(getSerializedFile());
            s = new ObjectInputStream(in);
            _sessions = (HashMap) s.readObject();
            Iterator i = _sessions.values().iterator();
            while (i.hasNext()) {
                BaseSession ssoSession = (BaseSession) i.next();
                super.save(ssoSession);
            }
        } catch (FileNotFoundException e) {
            if (logger.isDebugEnabled()) logger.debug("[checkLoad()] FileNotFoundeException : " + getSerializedFile());
            _sessions = new HashMap();
            saveSerializedSessions();
        } catch (Exception e) {
            logger.warn("Can't load serialized sessions from " + getSerializedFile() + " : " + e.getMessage() != null ? e.getMessage() : e.toString(), e);
            _sessions = new HashMap();
            saveSerializedSessions();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                if (logger.isDebugEnabled()) logger.debug("I/O after loading ... " + e.getMessage(), e);
            }
            _loaded = true;
        }
    }

    public void setSerializedFile(String serializedFile) {
        _serializedFile = serializedFile;
    }

    public String getSerializedFile() {
        return _serializedFile;
    }
}
