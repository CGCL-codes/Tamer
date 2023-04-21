package net.sf.hibernate.audit;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.MethodUtils;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sf.hibernate.audit.rules.AuditPropertyRule;

/**
 * @author: Amar
 * Date: Dec 24, 2008
 */
public class HistoryAuditPropertyUtilsBean extends PropertyUtilsBean {

    private static final Map<Class, PropertyDescriptor[]> beanPropertyDescriptor = new HashMap<Class, PropertyDescriptor[]>();

    private Map<Class, ClassAuditDefinition> classDefinitionMap;

    private Map<String, AuditPropertyRule> rulesMap;

    public PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
        PropertyDescriptor[] definition = beanPropertyDescriptor.get(beanClass);
        if (definition == null) {
            definition = super.getPropertyDescriptors(beanClass);
            definition = applyBeanDefinitionRules(beanClass, definition);
            System.out.println("Final Definition is " + definition);
            beanPropertyDescriptor.put(beanClass, definition);
        }
        return definition;
    }

    private PropertyDescriptor[] applyBeanDefinitionRules(Class beanClass, PropertyDescriptor[] definition) {
        if (classDefinitionMap == null) {
            return definition;
        }
        ClassAuditDefinition classAuditDefinition = classDefinitionMap.get(beanClass);
        if (classAuditDefinition != null && !classAuditDefinition.isClassVisible()) {
            return new PropertyDescriptor[0];
        }
        List<PropertyDescriptor> newDefinitions = new ArrayList<PropertyDescriptor>(Arrays.asList(definition));
        Class[] classesInOrder = getClassesInOrder(beanClass);
        Map<String, String> propertyRuleMapping = new HashMap<String, String>();
        for (Class aClass : classesInOrder) {
            ClassAuditDefinition auditDefinition = classDefinitionMap.get(aClass);
            if (auditDefinition != null) {
                Map<String, String> classRules = auditDefinition.getPropertyRuleMapping();
                propertyRuleMapping.putAll(classRules);
            }
        }
        Set<Map.Entry<String, String>> entries = propertyRuleMapping.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("entry = " + entry.getValue());
            if (entry.getValue().equals("exclude")) {
                for (int i = 0; i < newDefinitions.size(); i++) {
                    PropertyDescriptor propertyDescriptor = newDefinitions.get(i);
                    if (propertyDescriptor.getName().equals(entry.getKey())) {
                        newDefinitions.remove(propertyDescriptor);
                        break;
                    }
                }
            }
        }
        return newDefinitions.toArray(new PropertyDescriptor[0]);
    }

    private Class[] getClassesInOrder(Class beanClass) {
        List<Class> classHierarchy = new ArrayList<Class>();
        while (beanClass != null) {
            classHierarchy.add(beanClass);
            beanClass = beanClass.getSuperclass();
            if (beanClass.equals(Object.class)) {
                beanClass = null;
            }
        }
        if (classHierarchy.size() > 1) {
            Collections.reverse(classHierarchy);
        }
        return classHierarchy.toArray(new Class[0]);
    }

    public void setClassDefinitions(Map<Class, ClassAuditDefinition> classDefinitionMap) {
        this.classDefinitionMap = classDefinitionMap;
    }

    public ClassAuditDefinition getClassDefinitions(Class clazz) {
        return classDefinitionMap.get(clazz);
    }

    public boolean isClassVisibleToAudit(Class clazz) {
        ClassAuditDefinition definition = classDefinitionMap.get(clazz);
        System.out.println("Definition is " + definition + " for class " + clazz);
        if (definition == null) {
            return true;
        }
        return definition.isClassVisible();
    }

    public void setRulesMap(Map<String, AuditPropertyRule> rulesMap) {
        this.rulesMap = rulesMap;
    }

    /**
     * <p>Return an accessible property getter method for this property,
     * if there is one; otherwise return <code>null</code>.</p>
     * <p/>
     * <p><strong>FIXME</strong> - Does not work with DynaBeans.</p>
     *
     * @param clazz      The class of the read method will be invoked on
     * @param descriptor Property descriptor to return a getter for
     * @return The read method
     */
    public Method getReadMethod(Class clazz, PropertyDescriptor descriptor) {
        return (MethodUtils.getAccessibleMethod(clazz, descriptor.getReadMethod()));
    }
}
