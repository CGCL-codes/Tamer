package net.sourceforge.freejava.potato.adapter.bean;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import net.sourceforge.freejava.potato.AbstractPotatoType;
import net.sourceforge.freejava.potato.IPotatoEvent;
import net.sourceforge.freejava.potato.IPotatoMethod;
import net.sourceforge.freejava.potato.IPotatoProperty;
import net.sourceforge.freejava.reflect.MethodSignature;

/**
 * @test {@link BeanPotatoTypeTest}
 */
public class BeanPotatoType<T> extends AbstractPotatoType<T> {

    private final BeanInfo beanInfo;

    private final BeanDescriptor beanDescriptor;

    private Map<String, BeanPotatoProperty> propertyMap;

    private Map<MethodSignature, BeanPotatoMethod> methodMap;

    private Map<String, BeanPotatoEvent> eventMap;

    public BeanPotatoType(Class<T> beanClass) throws IntrospectionException {
        this(beanClass, Introspector.getBeanInfo(beanClass));
    }

    public BeanPotatoType(Class<T> beanClass, BeanInfo beanInfo) {
        super(beanClass);
        this.beanInfo = beanInfo;
        this.beanDescriptor = beanInfo.getBeanDescriptor();
        propertyMap = new TreeMap<String, BeanPotatoProperty>();
        methodMap = new HashMap<MethodSignature, BeanPotatoMethod>();
        eventMap = new TreeMap<String, BeanPotatoEvent>();
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            BeanPotatoProperty potatoProperty = new BeanPotatoProperty(this, propertyDescriptor);
            propertyMap.put(propertyDescriptor.getName(), potatoProperty);
        }
        for (MethodDescriptor methodDescriptor : beanInfo.getMethodDescriptors()) {
            BeanPotatoMethod potatoMethod = new BeanPotatoMethod(this, methodDescriptor);
            MethodSignature sign = new MethodSignature(methodDescriptor.getMethod());
            methodMap.put(sign, potatoMethod);
        }
        for (EventSetDescriptor eventSetDescriptor : beanInfo.getEventSetDescriptors()) {
            BeanPotatoEvent potatoEvent = new BeanPotatoEvent(this, eventSetDescriptor);
            eventMap.put(eventSetDescriptor.getName(), potatoEvent);
        }
    }

    @Override
    public String getName() {
        return beanDescriptor.getName();
    }

    @Override
    public String getDisplayName(Locale locale) {
        return beanDescriptor.getDisplayName();
    }

    @Override
    public String getDescription(Locale locale) {
        return beanDescriptor.getShortDescription();
    }

    @Override
    public int getPreferenceLevel() {
        return FeatureDescriptorUtil.getFeaturePreferenceLevel(beanDescriptor);
    }

    @Override
    public Collection<? extends IPotatoProperty> getProperties() {
        return propertyMap.values();
    }

    @Override
    public Collection<? extends IPotatoMethod> getMethods() {
        return methodMap.values();
    }

    @Override
    public Collection<? extends IPotatoEvent> getEvents() {
        return eventMap.values();
    }

    @Override
    public BeanPotatoProperty getProperty(String propertyName) {
        return propertyMap.get(propertyName);
    }

    @Override
    public IPotatoMethod getMethod(String methodName, Class<?>... parameterTypes) {
        MethodSignature sign = new MethodSignature(methodName, parameterTypes);
        return methodMap.get(sign);
    }

    @Override
    public IPotatoEvent getEvent(String eventName) {
        return eventMap.get(eventName);
    }
}
