package com.agilejava.bignumbers.plexus;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.composition.AbstractComponentComposer;
import org.codehaus.plexus.component.composition.CompositionException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.ReflectionUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A <code>FieldComponentComposer</code> subtype that will notify the injected 
 * object of its usage, allowing it to adapt itself based on the annotations of 
 * the receiving field.
 *
 * @author Wilfred Springer
 */
public class AnnotatedFieldComponentComposer extends AbstractComponentComposer {

    public List assembleComponent(Object component, ComponentDescriptor componentDescriptor, PlexusContainer container) throws CompositionException {
        List retValue = new LinkedList();
        List requirements = componentDescriptor.getRequirements();
        for (Iterator i = requirements.iterator(); i.hasNext(); ) {
            ComponentRequirement requirement = (ComponentRequirement) i.next();
            Field field = findMatchingField(component, componentDescriptor, requirement, container);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            List descriptors = assignRequirementToField(component, field, container, requirement);
            retValue.addAll(descriptors);
        }
        return retValue;
    }

    private List assignRequirementToField(Object component, Field field, PlexusContainer container, ComponentRequirement requirement) throws CompositionException {
        try {
            List retValue;
            String role = requirement.getRole();
            if (field.getType().isArray()) {
                List dependencies = container.lookupList(role);
                Object[] array = (Object[]) Array.newInstance(field.getType(), dependencies.size());
                retValue = container.getComponentDescriptorList(role);
                field.set(component, dependencies.toArray(array));
            } else if (Map.class.isAssignableFrom(field.getType())) {
                Map dependencies = container.lookupMap(role);
                retValue = container.getComponentDescriptorList(role);
                field.set(component, dependencies);
            } else if (List.class.isAssignableFrom(field.getType())) {
                List dependencies = container.lookupList(role);
                retValue = container.getComponentDescriptorList(role);
                field.set(component, dependencies);
            } else if (Set.class.isAssignableFrom(field.getType())) {
                Map dependencies = container.lookupMap(role);
                retValue = container.getComponentDescriptorList(role);
                field.set(component, dependencies.entrySet());
            } else {
                String key = requirement.getRequirementKey();
                Object dependency = container.lookup(key);
                ComponentDescriptor componentDescriptor = container.getComponentDescriptor(key);
                retValue = new ArrayList(1);
                retValue.add(componentDescriptor);
                field.set(component, dependency);
                if (dependency instanceof Injectable) {
                    ((Injectable) dependency).injected(field);
                }
            }
            return retValue;
        } catch (IllegalArgumentException e) {
            throw new CompositionException("Composition failed for the field " + field.getName() + " " + "in object of type " + component.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new CompositionException("Composition failed for the field " + field.getName() + " " + "in object of type " + component.getClass().getName(), e);
        } catch (ComponentLookupException e) {
            throw new CompositionException("Composition failed of field " + field.getName() + " " + "in object of type " + component.getClass().getName() + " because the requirement " + requirement + " was missing", e);
        }
    }

    protected Field findMatchingField(Object component, ComponentDescriptor componentDescriptor, ComponentRequirement requirement, PlexusContainer container) throws CompositionException {
        String fieldName = requirement.getFieldName();
        Field field = null;
        if (fieldName != null) {
            field = getFieldByName(component, fieldName, componentDescriptor);
        } else {
            Class fieldClass = null;
            try {
                if (container != null) {
                    fieldClass = container.getContainerRealm().loadClass(requirement.getRole());
                } else {
                    fieldClass = Thread.currentThread().getContextClassLoader().loadClass(requirement.getRole());
                }
            } catch (ClassNotFoundException e) {
                StringBuffer msg = new StringBuffer("Component Composition failed for component: ");
                msg.append(componentDescriptor.getHumanReadableKey());
                msg.append(": Requirement class: '");
                msg.append(requirement.getRole());
                msg.append("' not found.");
                throw new CompositionException(msg.toString(), e);
            }
            field = getFieldByType(component, fieldClass, componentDescriptor);
        }
        return field;
    }

    protected Field getFieldByName(Object component, String fieldName, ComponentDescriptor componentDescriptor) throws CompositionException {
        Field field = ReflectionUtils.getFieldByNameIncludingSuperclasses(fieldName, component.getClass());
        if (field == null) {
            StringBuffer msg = new StringBuffer("Component Composition failed. No field of name: '");
            msg.append(fieldName);
            msg.append("' exists in component: ");
            msg.append(componentDescriptor.getHumanReadableKey());
            throw new CompositionException(msg.toString());
        }
        return field;
    }

    protected Field getFieldByTypeIncludingSuperclasses(Class componentClass, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
        List fields = getFieldsByTypeIncludingSuperclasses(componentClass, type, componentDescriptor);
        if (fields.size() == 0) {
            return null;
        }
        if (fields.size() == 1) {
            return (Field) fields.get(0);
        }
        throw new CompositionException("There are several fields of type '" + type + "', " + "use 'field-name' to select the correct field.");
    }

    protected List getFieldsByTypeIncludingSuperclasses(Class componentClass, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
        Class arrayType = Array.newInstance(type, 0).getClass();
        Field[] fields = componentClass.getDeclaredFields();
        List foundFields = new ArrayList();
        for (int i = 0; i < fields.length; i++) {
            Class fieldType = fields[i].getType();
            if (fieldType.isAssignableFrom(type) || fieldType.isAssignableFrom(arrayType)) {
                foundFields.add(fields[i]);
            }
        }
        if (componentClass.getSuperclass() != Object.class) {
            List superFields = getFieldsByTypeIncludingSuperclasses(componentClass.getSuperclass(), type, componentDescriptor);
            foundFields.addAll(superFields);
        }
        return foundFields;
    }

    protected Field getFieldByType(Object component, Class type, ComponentDescriptor componentDescriptor) throws CompositionException {
        Field field = getFieldByTypeIncludingSuperclasses(component.getClass(), type, componentDescriptor);
        if (field == null) {
            StringBuffer msg = new StringBuffer("Component composition failed. No field of type: '");
            msg.append(type);
            msg.append("' exists in class '");
            msg.append(component.getClass().getName());
            msg.append("'.");
            if (componentDescriptor != null) {
                msg.append(" Component: ");
                msg.append(componentDescriptor.getHumanReadableKey());
            }
            throw new CompositionException(msg.toString());
        }
        return field;
    }
}
