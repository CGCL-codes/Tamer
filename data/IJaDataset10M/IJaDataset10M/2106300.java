package org.zkoss.zkplus.spring;

import org.zkoss.zkplus.util.ThreadLocals;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;
import org.zkoss.zk.ui.event.EventThreadResume;
import org.zkoss.zk.ui.event.EventThreadCleanup;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.lang.Classes;
import org.zkoss.lang.SystemException;
import org.zkoss.util.logging.Log;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>Listener to make sure each ZK thread got the same ThreadLocal value of the 
 * spring's org.springframework.transaction.support.TransactionSynchronizationManager;
 * especially those thread bound resources.
 * </p>
 * <p>
 * This listener is used with Spring Framework (version 1.2.8+) "thread" bounded
 * resources.
 *
 * <pre><code>
 * 	&lt;listener>
 *		&lt;description>Spring TransactionSynchronizationManager handler&lt;/description>
 *		&lt;listener-class>org.zkoss.zkplus.spring.SpringTransactionSynchronizationListener&lt;/listener-class>
 *	&lt;/listener>
 * </code></pre>
 * </p>
 *
 * @author henrichen
 */
public class SpringTransactionSynchronizationListener implements EventThreadInit, EventThreadCleanup, EventThreadResume {

    private static final Log log = Log.lookup(SpringTransactionSynchronizationListener.class);

    private Object[] _threadLocals = null;

    private final boolean _enabled;

    public SpringTransactionSynchronizationListener() {
        final WebApp app = Executions.getCurrent().getDesktop().getWebApp();
        _enabled = app.getConfiguration().isEventThreadEnabled();
    }

    public void prepare(Component comp, Event evt) {
        if (_enabled) {
            getThreadLocals();
        }
    }

    public boolean init(Component comp, Event evt) {
        if (_enabled) {
            setThreadLocals();
        }
        return true;
    }

    public void cleanup(Component comp, Event evt, List errs) {
        if (_enabled) {
            getThreadLocals();
        }
    }

    public void complete(Component comp, Event evt) {
        if (_enabled) {
            setThreadLocals();
        }
    }

    public void beforeResume(Component comp, Event evt) {
        if (_enabled) {
            getThreadLocals();
        }
    }

    public void afterResume(Component comp, Event evt) {
        if (_enabled) {
            setThreadLocals();
        }
    }

    public void abortResume(Component comp, Event evt) {
    }

    private void getThreadLocals() {
        try {
            Class cls = Classes.forNameByThread("org.springframework.transaction.support.TransactionSynchronizationManager");
            _threadLocals = new Object[7];
            _threadLocals[0] = getThreadLocal(cls, "resources").get();
            _threadLocals[1] = getThreadLocal(cls, "synchronizations").get();
            _threadLocals[2] = getThreadLocal(cls, "currentTransactionName").get();
            _threadLocals[3] = getThreadLocal(cls, "currentTransactionReadOnly").get();
            _threadLocals[4] = getThreadLocal(cls, "actualTransactionActive").get();
            try {
                cls = Classes.forNameByThread("org.springframework.orm.hibernate3.SessionFactoryUtils");
                _threadLocals[5] = getThreadLocal(cls, "deferredCloseHolder").get();
            } catch (ClassNotFoundException ex) {
            }
            cls = Classes.forNameByThread("org.springframework.transaction.interceptor.TransactionAspectSupport");
            try {
                _threadLocals[6] = getThreadLocal(cls, "transactionInfoHolder").get();
            } catch (SystemException ex) {
                if (ex.getCause() instanceof NoSuchFieldException) {
                    _threadLocals[6] = getThreadLocal(cls, "currentTransactionInfo").get();
                } else {
                    throw ex;
                }
            }
        } catch (ClassNotFoundException ex) {
            throw UiException.Aide.wrap(ex);
        }
    }

    private void setThreadLocals() {
        if (_threadLocals != null) {
            try {
                Class cls = Classes.forNameByThread("org.springframework.transaction.support.TransactionSynchronizationManager");
                getThreadLocal(cls, "resources").set(_threadLocals[0]);
                getThreadLocal(cls, "synchronizations").set(_threadLocals[1]);
                getThreadLocal(cls, "currentTransactionName").set(_threadLocals[2]);
                getThreadLocal(cls, "currentTransactionReadOnly").set(_threadLocals[3]);
                getThreadLocal(cls, "actualTransactionActive").set(_threadLocals[4]);
                try {
                    cls = Classes.forNameByThread("org.springframework.orm.hibernate3.SessionFactoryUtils");
                    getThreadLocal(cls, "deferredCloseHolder").set(_threadLocals[5]);
                } catch (ClassNotFoundException ex) {
                }
                cls = Classes.forNameByThread("org.springframework.transaction.interceptor.TransactionAspectSupport");
                try {
                    getThreadLocal(cls, "transactionInfoHolder").set(_threadLocals[6]);
                } catch (SystemException ex) {
                    if (ex.getCause() instanceof NoSuchFieldException) {
                        getThreadLocal(cls, "currentTransactionInfo").set(_threadLocals[6]);
                    } else {
                        throw ex;
                    }
                }
                _threadLocals = null;
            } catch (ClassNotFoundException ex) {
                throw UiException.Aide.wrap(ex);
            }
        }
    }

    private ThreadLocal getThreadLocal(Class cls, String fldname) {
        return ThreadLocals.getThreadLocal(cls, fldname);
    }
}
