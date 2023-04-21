package org.apache.isis.extensions.wicket.model.mementos;

import java.io.Serializable;
import org.apache.isis.extensions.wicket.model.util.ClassLoaders;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtime.context.IsisContext;

/**
 * A {@link Serializable} wrapper for {@link ObjectSpecification}
 */
public class SpecMemento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Class<?> type;

    private transient ObjectSpecification specification;

    /**
	 * Factory method.
	 * @param className
	 * @return may return null if className is null
	 */
    public static SpecMemento representing(String className) {
        if (className == null) {
            return null;
        }
        return new SpecMemento(ClassLoaders.forName(className));
    }

    public static SpecMemento representing(Class<?> type) {
        if (type == null) {
            return null;
        }
        return new SpecMemento(type);
    }

    public static SpecMemento representing(ObjectSpecification specification) {
        if (specification == null) {
            return null;
        }
        return new SpecMemento(specification);
    }

    public SpecMemento(ObjectSpecification specification) {
        this(ClassLoaders.forName(specification));
        this.specification = specification;
    }

    private SpecMemento(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    /**
	 * Lazy loaded from {@link #getType()}.
	 */
    public ObjectSpecification getSpecification() {
        if (specification == null) {
            specification = IsisContext.getSpecificationLoader().loadSpecification(type);
        }
        return specification;
    }
}
