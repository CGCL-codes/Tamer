package org.mobicents.ssf.servlet.handler.support;

import java.util.Enumeration;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionBindingEvent;
import javax.servlet.sip.SipSessionEvent;
import org.mobicents.ssf.event.DefaultEventType;
import org.mobicents.ssf.event.Event;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class SimpleSipSessionMetadata extends AbstractMappingMetadata {

    /**
     * 属性に設定されているクラスのタイプ
     */
    private Class<?>[] types = null;

    /**
     * 属性名
     */
    private String[] attributeNames = null;

    /**
     * イベントタイプ
     */
    private DefaultEventType[] eventTypes = null;

    public boolean matchByBeanName(ApplicationContext context, Event event, String beanName) {
        return false;
    }

    public boolean matchByClassName(ApplicationContext context, Event event, String className) {
        return false;
    }

    public boolean match(ApplicationContext context, Event event) {
        Object target = event.getTargetEvent();
        SipSession session = null;
        if (target instanceof SipSessionEvent) {
            SipSessionEvent e = (SipSessionEvent) target;
            session = e.getSession();
        } else if (target instanceof SipSessionBindingEvent) {
            SipSessionBindingEvent e = (SipSessionBindingEvent) target;
            session = e.getSession();
        }
        if (session == null) {
            return false;
        }
        if (this.isCheckStateName()) {
            if (!this.matchByStateName(context, event)) return false;
        }
        if (this.sessionName != null) {
            if (!this.matchBySessionName(context, event)) {
                return false;
            }
        }
        for (DefaultEventType eventType : eventTypes) {
            if (eventType == event.type) {
                return true;
            }
        }
        if (types != null && types.length > 0) {
            if (hasAttributeByType(session, types)) {
                return true;
            }
        }
        if (attributeNames != null && attributeNames.length > 0) {
            if (hasAttributeByName(session, attributeNames)) {
                return true;
            }
        }
        if (StringUtils.hasText(className)) {
            return matchByClassName(context, event, className);
        } else if (StringUtils.hasText(beanName)) {
            return matchByBeanName(context, event, beanName);
        }
        return false;
    }

    private boolean hasAttributeByType(SipSession session, Class<?>[] types) {
        Enumeration<String> e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            Object value = session.getAttribute(name);
            Class<?> clazz = value.getClass();
            for (Class<?> type : types) {
                if (clazz.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAttributeByName(SipSession session, String[] attributeNames) {
        for (String attributeName : attributeNames) {
            Object value = session.getAttribute(attributeName);
            if (value != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 属性に設定されているクラスのタイプを取得します。
     * @return 属性に設定されているクラスのタイプ
     */
    public Class<?>[] getTypes() {
        if (types == null) return new Class<?>[0];
        return types.clone();
    }

    /**
     * 属性に設定されているクラスのタイプを設定します。
     * @param types 属性に設定されているクラスのタイプ
     */
    public void setTypes(Class<?>[] types) {
        this.types = types.clone();
    }

    /**
     * 属性名を取得します。
     * @return 属性名
     */
    public String[] getAttributeNames() {
        if (attributeNames == null) return new String[0];
        return attributeNames.clone();
    }

    /**
     * 属性名を設定します。
     * @param attributeNames 属性名
     */
    public void setAttributeNames(String[] attributeNames) {
        this.attributeNames = attributeNames.clone();
    }

    /**
     * イベントタイプを取得します。
     * @return イベントタイプ
     */
    public DefaultEventType[] getEventTypes() {
        if (eventTypes == null) {
            return new DefaultEventType[0];
        }
        return eventTypes.clone();
    }

    /**
     * イベントタイプを設定します。
     * @param eventTypes イベントタイプ
     */
    public void setEventTypes(DefaultEventType[] eventTypes) {
        this.eventTypes = eventTypes.clone();
    }
}
