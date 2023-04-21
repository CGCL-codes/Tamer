package org.skunk.dav.client.gui;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * a property can be registered with the state monitor so that when the 
 * a given application property changes state, so will the registered property,
 * according to a mapping of property values.  If the mapping is null,
 * the application property value is used directly to set the target property value.
 */
public class StateMonitor {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private static List monitors;

    private Object target;

    private String targetProperty;

    private String applicationProperty;

    private Mapper valueMapper;

    private DomainMatcher domainMatcher;

    private Method setter;

    static {
        monitors = Collections.synchronizedList(new ArrayList());
    }

    /**
     *  set a target object's property value to vary according 
     *  to changes in the value of the application property, which
     *  is maintained statically by the StateMonitor class.  The
     *  target property's value is set to whatever the valueMapper
     *  returns, or, if the valueMapper is null, to the value of
     *  the application property.
     *  @param target the Object whose property will be set
     *  @param targetProperty the property of the Object that will be set
     *  @param applicationProperty the application property according to which the target property will vary
     *  @param valueMapper the Mapper that maps application values to target values, or null 
     *  @param domainMatcher the DomainMatcher that tests domainKeys, or null
     */
    public static void registerProperty(Object target, String targetProperty, String applicationProperty, Mapper valueMapper, DomainMatcher domainMatcher) {
        unregisterProperty(target, targetProperty, applicationProperty);
        monitors.add(new StateMonitor(target, targetProperty, applicationProperty, valueMapper, domainMatcher));
    }

    public static void unregisterProperty(Object target, String targetProperty, String applicationProperty) {
        for (Iterator it = monitors.iterator(); it.hasNext(); ) {
            StateMonitor sm = (StateMonitor) it.next();
            if (sm.target.equals(target) && sm.targetProperty.equals(targetProperty) && sm.applicationProperty.equals(applicationProperty)) {
                monitors.remove(sm);
                break;
            }
        }
    }

    public static final void setProperty(String applicationProperty, Object value, Object domainKey) {
        for (Iterator it = monitors.iterator(); it.hasNext(); ) {
            StateMonitor nextMonitor = (StateMonitor) it.next();
            if (nextMonitor.applicationProperty.equals(applicationProperty)) {
                DomainMatcher dm = nextMonitor.domainMatcher;
                if (dm == null || dm.domainMatches(domainKey)) nextMonitor.configureTarget(value);
            }
        }
    }

    public interface DomainMatcher {

        boolean domainMatches(Object domainKey);
    }

    /**
     *  maps values of the application property to values of the target property 
     */
    public interface Mapper {

        /**
	 *  @param applicationValue the application value of the registered application property
	 *  @return target value that the StateMonitor should set registered target properties 
	 */
        Object getTargetValue(Object applicationValue);

        /**
	 * @return whether or not the Mapper can map this value
	 */
        boolean canMapValue(Object applicationValue);
    }

    /**
     * @return a Mapper object that simply wraps a Map object
     */
    public static Mapper getMapper(final Map m) {
        return new Mapper() {

            public Object getTargetValue(Object applicationValue) {
                return (m.containsKey(applicationValue)) ? m.get(applicationValue) : null;
            }

            public boolean canMapValue(Object applicationValue) {
                return m.containsKey(applicationValue);
            }
        };
    }

    private StateMonitor(Object target, String targetProperty, String applicationProperty, Mapper valueMapper, DomainMatcher domainMatcher) {
        this.target = target;
        this.targetProperty = targetProperty;
        this.applicationProperty = applicationProperty;
        this.valueMapper = valueMapper;
        this.domainMatcher = domainMatcher;
    }

    private void configureTarget(Object applicationValue) {
        if (valueMapper == null || valueMapper.canMapValue(applicationValue)) {
            try {
                if (setter == null) {
                    BeanInfo info = Introspector.getBeanInfo(target.getClass());
                    PropertyDescriptor[] propDescriptors = info.getPropertyDescriptors();
                    for (int i = 0; i < propDescriptors.length; i++) {
                        PropertyDescriptor pd = propDescriptors[i];
                        String propName = pd.getName();
                        if (propName.equals(targetProperty)) {
                            setter = pd.getWriteMethod();
                            break;
                        }
                    }
                }
                if (setter != null) {
                    Object targetValue = (valueMapper != null) ? valueMapper.getTargetValue(applicationValue) : applicationValue;
                    setter.invoke(target, new Object[] { targetValue });
                } else {
                    log.trace("*** Error: setter not found for " + targetProperty);
                }
            } catch (IntrospectionException intro) {
                log.error("Exception", intro);
            } catch (IllegalAccessException illAcc) {
                log.error("Exception", illAcc);
            } catch (IllegalArgumentException illArg) {
                log.error("Exception", illArg);
            } catch (InvocationTargetException invoTarg) {
                log.error("Exception", invoTarg);
            }
        }
    }
}
