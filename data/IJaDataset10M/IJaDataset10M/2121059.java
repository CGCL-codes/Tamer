package architecture.ee.component.core;

import org.apache.commons.lang.math.NumberUtils;
import architecture.common.event.api.EventPublisher;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

/**
 * @author  donghyuck
 */
public abstract class AbstractJdbcApplicationProperties extends ExtendedJdbcDaoSupport implements ApplicationProperties {

    /**
	 */
    private EventPublisher eventPublisher = null;

    public boolean getBooleanProperty(String propertyKey) {
        return Boolean.valueOf(get(propertyKey)).booleanValue();
    }

    public boolean getBooleanProperty(String propertyKey, boolean defaultValue) {
        String value = get(propertyKey);
        if (value != null) return Boolean.valueOf(value).booleanValue(); else return defaultValue;
    }

    public int getIntProperty(String property, int defaultValue) {
        return NumberUtils.toInt(get(property), defaultValue);
    }

    public long getLongProperty(String property, long defaultValue) {
        return NumberUtils.toLong(get(property), defaultValue);
    }

    protected void firePropertyChangeEvent(Object source, ApplicationPropertyChangeEvent.Type eventType, String propertyName, Object oldValue, Object newValue) {
        if (eventPublisher != null) {
            eventPublisher.publish(new ApplicationPropertyChangeEvent(source, eventType, propertyName, oldValue, newValue));
        }
    }

    /**
	 * @return
	 */
    protected EventPublisher getEventPublisher() {
        return eventPublisher;
    }

    /**
	 * method from EventSource interface!
	 */
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
