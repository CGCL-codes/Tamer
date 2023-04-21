package org.enclojure.clojure.debugger;

import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.api.debugger.Session;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.DebuggerEngineProvider;

/**
 *
 * @author ericthor
 */
public class ClojureEngineProvider extends DebuggerEngineProvider {

    private DebuggerEngine.Destructor desctuctor;

    private Session session;

    public ClojureEngineProvider(ContextProvider contextProvider) {
        session = (Session) contextProvider.lookupFirst(null, Session.class);
    }

    public String[] getLanguages() {
        return new String[] { "Clojure" };
    }

    public String getEngineTypeID() {
        return JPDADebugger.ENGINE_ID;
    }

    public Object[] getServices() {
        return new Object[0];
    }

    public void setDestructor(DebuggerEngine.Destructor desctuctor) {
        this.desctuctor = desctuctor;
    }

    public DebuggerEngine.Destructor getDestructor() {
        return desctuctor;
    }

    public Session getSession() {
        return session;
    }
}
