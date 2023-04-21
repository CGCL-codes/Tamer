package org.effrafax.lightsout.util.observer.implementations;

import java.util.HashMap;
import java.util.Map;
import org.effrafax.lightsout.util.observer.interfaces.Aspect;
import org.effrafax.lightsout.util.observer.interfaces.Handler;
import org.effrafax.lightsout.util.observer.interfaces.Observer;
import org.effrafax.lightsout.util.observer.interfaces.Subject;

/**
 * @author dwanrooy
 * 
 */
public class ObserverDelegate implements Observer {

    private Observer observer = null;

    private Map<Aspect, Handler> aspectToHandler = new HashMap<Aspect, Handler>();

    public ObserverDelegate(Observer observer) {
        setObserver(observer);
    }

    @Override
    public void notify(Aspect aspect, Subject subject) {
        if (this.containsAspect(aspect)) {
            this.getHandler(aspect).handle(this.getObserver(), subject);
        }
    }

    @Override
    public void registerHandler(Aspect aspect, Handler handler) throws IllegalArgumentException {
        if (aspect == null) {
            throw new IllegalArgumentException("aspect should not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler should not be null");
        }
        this.getAspectToHandler().put(aspect, handler);
    }

    /**
	 * @return the aspectToHandler
	 */
    private Map<Aspect, Handler> getAspectToHandler() {
        return aspectToHandler;
    }

    /**
	 * @return the observer
	 */
    private Observer getObserver() {
        return observer;
    }

    /**
	 * @param observer
	 *            the observer to set
	 */
    private void setObserver(Observer observer) throws IllegalArgumentException {
        if (observer == null) {
            throw new IllegalArgumentException("observer should not be null");
        }
        this.observer = observer;
    }

    /**
	 * Determines if there exist an associated {@code Handler} for {@code
	 * aspect}.
	 * 
	 * @param aspect
	 *            The {@code Aspect} under scrutiny.
	 * @return {@code true} if there exist an associated handler for {@code
	 *         aspect}, {@code false} otherwise.
	 */
    private boolean containsAspect(Aspect aspect) {
        return this.getAspectToHandler().containsKey(aspect);
    }

    /**
	 * Returns the associated {@code Handler} for {@code aspect}.
	 * 
	 * @param aspect
	 *            The {@code Aspect} which {@code Handler} is retrieved.
	 * @return The {@code Handler} associated to {@code aspect}.
	 * @throws IllegalStateException
	 *             If no {@code Handler} is associated with {@code aspect}.
	 */
    private Handler getHandler(Aspect aspect) throws IllegalStateException {
        if (!this.containsAspect(aspect)) {
            throw new IllegalStateException(aspect + " has no handler associated.");
        }
        return this.getAspectToHandler().get(aspect);
    }
}
