package com.bluemarsh.jswat.core.runtime;

import java.io.File;
import java.util.Iterator;

/**
 * AbstractRuntimeManager provides an abstract RuntimeManager for concrete
 * implementations to subclass.
 *
 * @author Nathan Fiedler
 */
public abstract class AbstractRuntimeManager implements RuntimeManager {

    /** The prefix for runtime identifiers. */
    protected static final String ID_PREFIX = "JRE_";

    /** List of runtime listeners. */
    private RuntimeListener runtimeListeners;

    @Override
    public void addRuntimeListener(RuntimeListener listener) {
        if (listener != null) {
            synchronized (this) {
                runtimeListeners = RuntimeEventMulticaster.add(runtimeListeners, listener);
            }
        }
    }

    @Override
    public JavaRuntime findByBase(String base) {
        JavaRuntime rt = null;
        Iterator<JavaRuntime> iter = iterateRuntimes();
        File basedir = new File(base);
        while (iter.hasNext()) {
            JavaRuntime r = iter.next();
            File bd = new File(r.getBase());
            if (bd.equals(basedir)) {
                rt = r;
                if (r.isValid()) {
                    break;
                }
            }
        }
        return rt;
    }

    @Override
    public JavaRuntime findById(String id) {
        if (id != null && id.length() > 0) {
            Iterator<JavaRuntime> iter = iterateRuntimes();
            while (iter.hasNext()) {
                JavaRuntime runtime = iter.next();
                if (id.equals(runtime.getIdentifier())) {
                    return runtime;
                }
            }
        }
        return null;
    }

    /**
     * Sends the given event to all of the registered listeners.
     *
     * @param  e  event to be dispatched.
     */
    protected void fireEvent(RuntimeEvent e) {
        RuntimeListener listeners;
        synchronized (this) {
            listeners = runtimeListeners;
        }
        if (listeners != null) {
            e.getType().fireEvent(e, listeners);
        }
    }

    @Override
    public String generateIdentifier() {
        Iterator<JavaRuntime> iter = iterateRuntimes();
        if (!iter.hasNext()) {
            return ID_PREFIX + '1';
        } else {
            int max = 0;
            while (iter.hasNext()) {
                JavaRuntime runtime = iter.next();
                String id = runtime.getIdentifier();
                id = id.substring(ID_PREFIX.length());
                try {
                    int i = Integer.parseInt(id);
                    if (i > max) {
                        max = i;
                    }
                } catch (NumberFormatException nfe) {
                }
            }
            max++;
            return ID_PREFIX + max;
        }
    }

    @Override
    public void removeRuntimeListener(RuntimeListener listener) {
        if (listener != null) {
            synchronized (this) {
                runtimeListeners = RuntimeEventMulticaster.remove(runtimeListeners, listener);
            }
        }
    }
}
