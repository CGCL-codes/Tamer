package net.sf.mmm.util.pojo.descriptor.base.accessor;

import net.sf.mmm.util.pojo.descriptor.api.accessor.PojoPropertyAccessor;
import net.sf.mmm.util.reflect.api.GenericType;

/**
 * This is the abstract base-implementation of the {@link PojoPropertyAccessor}
 * interface.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.1.0
 */
public abstract class AbstractPojoPropertyAccessor implements PojoPropertyAccessor {

    /**
   * The constructor.
   */
    public AbstractPojoPropertyAccessor() {
        super();
    }

    /**
   * {@inheritDoc}
   */
    public Class<?> getPropertyClass() {
        GenericType<?> propertyType = getPropertyType();
        if (getMode().isReading()) {
            return propertyType.getRetrievalClass();
        } else {
            return propertyType.getAssignmentClass();
        }
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public String toString() {
        return getMode() + "-accessor of property '" + getDeclaringClass().getSimpleName() + "." + getName() + "' with type " + getPropertyType() + "(" + getClass().getSimpleName() + ")";
    }
}
