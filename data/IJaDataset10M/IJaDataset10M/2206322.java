package net.sourceforge.javautil.inject.requirements;

import java.lang.reflect.Type;
import net.sourceforge.javautil.inject.IInjectionRequirements;

/**
 * The base for most {@link IInjectionRequirements} implementations.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class InjectionRequirementsSimple<T> implements IInjectionRequirements<T> {

    protected final Type baseType;

    protected final Class rawType;

    public InjectionRequirementsSimple(Type baseType, Class rawType) {
        this.baseType = baseType;
        this.rawType = rawType;
    }

    public Class getRawType() {
        return this.rawType;
    }

    public Type getTargetType() {
        return baseType;
    }
}
