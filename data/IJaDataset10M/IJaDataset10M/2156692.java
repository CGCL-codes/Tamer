package org.htw.osgi.renderservice.essentials;

import java.util.*;
import javax.servlet.ServletRequest;

/**
 * Implementierung eines Dispatchers.
 * 
 * @author  Benjamin Friedrich (<a href="mailto:benjamin_friedrich@gmx.de">mailto:benjamin_friedrich@gmx.de</a>)
 * @version 1.0  Juni 2009
 */
public class Dispatcher implements IDispatcher {

    private final List<IInterceptor> interceptors;

    private boolean interrupted;

    /**
	 * Konstruktor von Dispatcher
	 */
    public Dispatcher() {
        this.interceptors = new ArrayList<IInterceptor>();
        this.interrupted = false;
    }

    @Override
    public final void registerInterceptor(final IInterceptor interceptor) {
        if (!this.interceptors.contains(interceptor)) {
            if (this.interceptors.isEmpty() || !(interceptor instanceof IPriorityInterceptor)) {
                this.interceptors.add(interceptor);
                return;
            }
            if (interceptor instanceof IPriorityInterceptor) {
                final IPriorityInterceptor priorityInterceptor;
                priorityInterceptor = (IPriorityInterceptor) interceptor;
                this.registerPriorityInterceptor(priorityInterceptor);
            }
        }
    }

    /**
	 * Registriert den übergebenen IPriorityInterceptor im Dispatcher
	 * gemäß der Priorität des Interceptors.
	 * 
	 * @param interceptor Interceptor mit Priorität
	 */
    private final void registerPriorityInterceptor(final IPriorityInterceptor interceptor) {
        for (int i = 0; i < this.interceptors.size(); i++) {
            final IPriorityInterceptor tmpInterceptor;
            tmpInterceptor = (IPriorityInterceptor) this.interceptors.get(i);
            if (interceptor.compareTo(tmpInterceptor) < 0) {
                this.interceptors.add(i, interceptor);
                return;
            }
        }
        this.interceptors.add(interceptor);
    }

    @Override
    public final void unregisterInterceptor(final IInterceptor interceptor) {
        if (this.interceptors.contains(interceptor)) {
            this.interceptors.remove(interceptor);
        }
    }

    @Override
    public final void dispatch(final ServletRequest request) {
        this.interrupted = false;
        for (int i = 0; i < this.interceptors.size() && !this.interrupted; i++) {
            final IInterceptor interceptor = this.interceptors.get(i);
            System.err.println("Notify " + interceptor);
            interceptor.processRequest(request);
        }
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
    }
}
