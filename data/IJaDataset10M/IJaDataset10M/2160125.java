package com.fortuityframework.spring.broker;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import com.fortuityframework.core.dispatch.EventListenerLocator;
import com.fortuityframework.core.dispatch.NullEventListenerLocator;
import com.fortuityframework.core.dispatch.broker.PriorityQueueEventBroker;

/**
 * Priority Queue Event broker for use with the Spring framework. To use this broker, only a single line of configuration
 * is needed in your Spring context.
 * 
 * {@code <bean id="eventBroker" class="com.fortuityframework.spring.broker.SpringPriorityQueueEventBroker"></bean>}
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class SpringPriorityQueueEventBroker extends PriorityQueueEventBroker implements ApplicationListener {

    private EventListenerLocator chainedLocator;

    /**
	 * Create a new Spring Priority Queue Event Broker
	 */
    public SpringPriorityQueueEventBroker() {
        this.chainedLocator = new NullEventListenerLocator();
    }

    /**
	 * Create a new Spring Priority Queue Event Broker that chains events to another locator after processing Spring
	 * @param chainedLocator The locator to chain events to
	 */
    public SpringPriorityQueueEventBroker(EventListenerLocator chainedLocator) {
        super();
        this.chainedLocator = chainedLocator;
    }

    /**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
            SpringEventListenerLocator locator = new SpringEventListenerLocator();
            locator.onApplicationEvent(event);
            locator.setChainedLocator(chainedLocator);
            setEventListenerLocator(locator);
        }
    }
}
