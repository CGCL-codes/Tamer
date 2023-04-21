package com.fortuityframework.spring.broker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.dispatch.EventListener;
import com.fortuityframework.core.dispatch.EventListenerLocator;
import com.fortuityframework.core.dispatch.NullEventListenerLocator;
import com.fortuityframework.core.event.Event;

/**
 * EventListenerLocator that uses Spring to find responders to various events.
 * 
 * @author Jeroen Steenbeeke
 * 
 */
class SpringEventListenerLocator implements ApplicationListener, EventListenerLocator {

    private Map<Class<? extends Event<?>>, List<EventListener>> listeners;

    private EventListenerLocator chainedLocator;

    /**
	 * Create a new SpringEventListenerLocator. 
	 * 
	 */
    public SpringEventListenerLocator() {
        listeners = new HashMap<Class<? extends Event<?>>, List<EventListener>>();
        chainedLocator = new NullEventListenerLocator();
    }

    /**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            ApplicationContext context = ((ApplicationContextEvent) event).getApplicationContext();
            for (String beanDefinitionName : context.getBeanDefinitionNames()) {
                Class<?> type = context.getType(beanDefinitionName);
                if (type != null && type.getMethods() != null) {
                    for (Method m : type.getMethods()) {
                        registerMethodAsListener(context, beanDefinitionName, m);
                    }
                }
            }
        }
    }

    private void registerMethodAsListener(ApplicationContext context, String beanDefinitionName, Method m) {
        OnFortuityEvent eventRef = m.getAnnotation(OnFortuityEvent.class);
        if (eventRef != null) {
            Class<?>[] paramTypes = m.getParameterTypes();
            if (paramTypes.length == 1 && EventContext.class.isAssignableFrom(paramTypes[0])) {
                Class<? extends Event<?>>[] events = getEvents(eventRef);
                for (Class<? extends Event<?>> eventClass : events) {
                    registerListener(eventClass, beanDefinitionName, m, context);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Event<?>>[] getEvents(OnFortuityEvent eventRef) {
        Class<? extends Event<?>>[] events = (Class<? extends Event<?>>[]) eventRef.value();
        return events;
    }

    private void registerListener(Class<? extends Event<?>> eventClass, String beanName, Method method, ApplicationContext context) {
        if (!listeners.containsKey(eventClass)) {
            listeners.put(eventClass, new LinkedList<EventListener>());
        }
        listeners.get(eventClass).add(new SpringEventListener(beanName, method, context));
    }

    @Override
    public List<EventListener> getEventListeners(Class<? extends Event<?>> eventClass) {
        List<EventListener> result = new LinkedList<EventListener>();
        Class<?> next = eventClass;
        while (listeners.containsKey(next)) {
            result.addAll(listeners.get(eventClass));
            if (!Event.class.isAssignableFrom(next.getSuperclass())) {
                break;
            }
            next = next.getSuperclass();
        }
        result.addAll(chainedLocator.getEventListeners(eventClass));
        return result;
    }

    /**
	 * Chains another locator to add a different channel of event handling
	 * @param chainedLocator The locator to chain
	 */
    public void setChainedLocator(EventListenerLocator chainedLocator) {
        this.chainedLocator = chainedLocator;
    }
}
