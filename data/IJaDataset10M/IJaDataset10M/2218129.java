package com.googlecode.springeventmanager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import com.googlecode.springeventmanager.support.EventManagerUtils;

/**
 * Abstract class for registering {@link EventListener}s
 * with the {@link EventManager}.  By default the 
 * {@link ApplicationContext} is searched for a bean by
 * the name of "eventManager".  One of the "eventManagerBeanName"
 * and "eventManager" properties must be set.
 * 
 */
public abstract class AbstractEventListenerRegistrar implements InitializingBean, ApplicationContextAware {

    private ApplicationContext appCtx;

    private String eventManagerBeanName = "eventManager";

    private EventManager eventManager;

    /**
     * {@inheritDoc}
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.appCtx = applicationContext;
    }

    /**
     * {@inheritDoc}
     */
    public void afterPropertiesSet() throws Exception {
        eventManager = EventManagerUtils.findEventManager(appCtx, eventManagerBeanName);
        Assert.notNull(eventManager, "One of eventManager or eventManagerBeanName is required");
        registerEventListeners(eventManager);
    }

    /**
     * Called when the sublcass should register
     * {@link EventListener}s.
     * @param eventManager the event manager to register with
     * @throws Exception on error
     */
    protected abstract void registerEventListeners(EventManager eventManager) throws Exception;

    /**
     * The name of the {@link EventManager} bean 
     * within the {@link ApplicationContext}.
     * @param eventManagerBeanName the eventManagerBeanName to set
     */
    public void setEventManagerBeanName(String eventManagerBeanName) {
        this.eventManagerBeanName = eventManagerBeanName;
    }

    /**
     * The {@link EventManager} to register events with.
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
