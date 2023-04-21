package org.nakedobjects.reflector.dotnet.resource;

import System.Type;
import org.nakedobjects.object.InstancesCriteria;

public abstract class AbstractFactoryAndRepository extends org.nakedobjects.reflector.java.resource.AbstractFactoryAndRepository {

    public AbstractFactoryAndRepository(final Type type) {
        this(type, false);
    }

    public AbstractFactoryAndRepository(final Type type, final boolean includeSubclasses) {
        super(Class.FromType(type), includeSubclasses);
    }

    protected Object[] allInstances(final Type type, final boolean includeSubclasses) {
        return super.allInstances(Class.FromType(type), includeSubclasses);
    }

    protected Object[] findByCriteria(final InstancesCriteria criteria, final Type type) {
        return super.findByCriteria(criteria, Class.FromType(type));
    }

    protected Object[] findByTitle(final String title, final Type type, final boolean includeSubclasses) {
        return super.findByTitle(title, Class.FromType(type), includeSubclasses);
    }

    protected boolean hasInstances(final Type type, final boolean includeSubclasses) {
        return super.hasInstances(Class.FromType(type), includeSubclasses);
    }
}
