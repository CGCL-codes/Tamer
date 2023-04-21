package org.hibernate.type;

import java.lang.reflect.Method;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.tuple.component.ComponentTuplizer;
import org.hibernate.tuple.component.ComponentMetamodel;

/**
 * @author Gavin King
 */
public class EmbeddedComponentType extends ComponentType {

    public boolean isEmbedded() {
        return true;
    }

    public EmbeddedComponentType(ComponentMetamodel metamodel) {
        super(metamodel);
    }

    public boolean isMethodOf(Method method) {
        return ((ComponentTuplizer) tuplizerMapping.getTuplizer(EntityMode.POJO)).isMethodOf(method);
    }

    public Object instantiate(Object parent, SessionImplementor session) throws HibernateException {
        final boolean useParent = parent != null && super.getReturnedClass().isInstance(parent);
        return useParent ? parent : super.instantiate(parent, session);
    }
}
