package net.sourceforge.javautil.inject.point;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import net.sourceforge.javautil.inject.IInjectionRequirements;

/**
 * The base for parameter based injection
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class InjectionPointParameter<T> extends InjectionPointAbstract<T> {

    protected final int parameterIndex;

    public InjectionPointParameter(Class declaringClass, int parameterIndex, IInjectionRequirements<T> requirements) {
        super(declaringClass, requirements);
        this.parameterIndex = parameterIndex;
    }

    /**
	 * @return The index of the parameter at which this injection point takes place
	 */
    public int getParameterIndex() {
        return parameterIndex;
    }

    public void inject(Object instance, T bean) throws Exception {
        ((Object[]) instance)[this.parameterIndex] = bean;
    }
}
