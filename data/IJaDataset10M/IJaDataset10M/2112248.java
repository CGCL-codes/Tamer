package org.jsesoft.ri;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

/**
 * Provides services for .
 * 
 */
public class ConstructorInspector implements Inspector<Constructor<?>> {

    /**
     * 
     * Creates new ConstructorInspector.
     */
    public ConstructorInspector() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jsesoft.ri.Inspector#inspectItem(java.lang.Object)
     */
    @Override
    public void inspectItem(Constructor<?> constructor, PassBack passBack) throws Exception {
        final Object[] args = { constructor };
        passBack.getInspectorFactory().create(InspectorType.PARAMS_INSPECTOR, args, Object.class).inspectItems(passBack);
        passBack.getInspectorFactory().create(InspectorType.ANNOTATION_INSPECTOR, args, AccessibleObject.class).inspectItems(passBack);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jsesoft.ri.Inspector#inspectItems()
     */
    @Override
    public void inspectItems(PassBack passBack) throws Exception {
        final Constructor<?>[] constructors = passBack.getInspected().getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            inspectItem(constructor, passBack);
        }
    }
}
