package org.codehaus.groovy.grails.support;

import java.beans.PropertyEditorSupport;
import org.springframework.util.ClassUtils;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Aug 8, 2005
 */
public class ClassEditor extends PropertyEditorSupport {

    private ClassLoader classLoader = null;

    public ClassEditor() {
        super();
    }

    public ClassEditor(Object arg0) {
        super(arg0);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getAsText() {
        return ((Class) getValue()).getName();
    }

    public void setAsText(String className) throws IllegalArgumentException {
        try {
            Class clazz = ClassUtils.resolvePrimitiveClassName(className);
            if (clazz != null) {
                setValue(clazz);
            } else {
                setValue(this.classLoader.loadClass(className));
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not load class [" + className + "]!");
        }
    }
}
