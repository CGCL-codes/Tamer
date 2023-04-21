package org.codehaus.groovy.grails.commons;

import groovy.lang.Closure;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.beans.PropertyDescriptor;

/**
 * @author Graeme Rocher
 * @since 14-Jan-2006
 *
 * Default implementation of a tag lib class
 *
 */
public class DefaultGrailsTagLibClass extends AbstractInjectableGrailsClass implements GrailsTagLibClass {

    protected static final String TAG_LIB = "TagLib";

    private List supportedControllers;

    private Set tags = new HashSet();

    private String namespace = GrailsTagLibClass.DEFAULT_NAMESPACE;

    /**
     * <p>Default contructor
     *
     * @param clazz        the tag library class
     */
    public DefaultGrailsTagLibClass(Class clazz) {
        super(clazz, TAG_LIB);
        Class supportedControllerClass = (Class) getPropertyOrStaticPropertyOrFieldValue(SUPPORTS_CONTROLLER, Class.class);
        if (supportedControllerClass != null) {
            supportedControllers = new ArrayList();
            supportedControllers.add(supportedControllerClass);
        } else {
            List tmp = (List) getPropertyOrStaticPropertyOrFieldValue(SUPPORTS_CONTROLLER, List.class);
            if (tmp != null) {
                supportedControllers = tmp;
            }
        }
        PropertyDescriptor[] props = getReference().getPropertyDescriptors();
        for (int i = 0; i < props.length; i++) {
            PropertyDescriptor prop = props[i];
            Closure tag = (Closure) getPropertyOrStaticPropertyOrFieldValue(prop.getName(), Closure.class);
            if (tag != null) {
                tags.add(prop.getName());
            }
        }
        String ns = (String) getPropertyOrStaticPropertyOrFieldValue(NAMESPACE_FIELD_NAME, String.class);
        if (ns != null && !"".equals(ns.trim())) {
            namespace = ns.trim();
        }
    }

    public boolean supportsController(GrailsControllerClass controllerClass) {
        if (controllerClass == null) return false; else if (supportedControllers != null) {
            if (supportedControllers.contains(controllerClass.getClazz())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean hasTag(String tagName) {
        return tags.contains(tagName);
    }

    public Set getTagNames() {
        return tags;
    }

    public String getNamespace() {
        return namespace;
    }
}
