package org.wuhsin.canon.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodProxy implements Handler {

    public MethodProxy(@NonNull Object instance, @NonNull Method method) {
        super();
        this.instance = instance;
        this.method = method;
    }

    public void publish(@NonNull Event event) {
        Object[] parameters = { event };
        Object[] emptyParameters = {};
        try {
            if (this.method.getParameterTypes().length == 0) {
                this.method.setAccessible(true);
                this.method.invoke(this.instance, emptyParameters);
            } else {
                this.method.setAccessible(true);
                this.method.invoke(this.instance, parameters);
            }
        } catch (IllegalArgumentException e) {
            logger.error("The method proxy for the " + this.method.getName() + " method attempted to publish an event and failed because the event parameter passed in to it was incorrect. This is a developer error and needs to be dealt with.", e);
        } catch (IllegalAccessException e) {
            logger.error("The method proxy for the " + this.method.getName() + " method attempted to publish an event and failed because the permissions were nopt available to execute the method. This is a developer error and needs to be dealt with.", e);
        } catch (InvocationTargetException e) {
            logger.error("The method proxy for the " + this.method.getName() + " method attempted to publish an event and failed because the method failed in some suprising manner. This is a developer error and needs to be dealt with.", e);
        }
    }

    /**
     * What field is this bound to. All the information and operations for doing
     * the binding is in the Binding class.
     */
    private transient Method method;

    /**
     * The instantiated object that is the container for the method (and its data)
     */
    private transient Object instance;

    /**
     * the standard logger
     */
    private static Logger logger = LoggerFactory.getLogger(MethodProxy.class.getSimpleName());
}
