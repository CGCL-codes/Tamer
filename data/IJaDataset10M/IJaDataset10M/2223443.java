package net.sourceforge.jdefprog.reflection;

import java.lang.reflect.Array;
import net.sourceforge.jdefprog.cc.Nn;

/**
 * Implementation of TypeConverter that implements
 * the get methods receiving arrays using the get methods
 * receiving single values. The methods receiving single values
 * have to be implemented in subclasses.
 * 
 * @author Federico Tomassetti (f.tomassetti@gmail.com)
 */
public abstract class AbstractTypeConverter<T> implements TypeConverter<T> {

    private Class<T> t;

    protected AbstractTypeConverter(Class<?> t) {
        this.t = (Class<T>) t;
    }

    public final ClassDesc[] get(@Nn T[] paramTypes) {
        ClassDesc[] res = new ClassDesc[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            res[i] = get(paramTypes[i]);
        }
        return res;
    }

    public final T[] get(@Nn ClassDesc[] paramTypes) {
        T[] res = (T[]) Array.newInstance(this.t, paramTypes.length);
        for (int i = 0; i < paramTypes.length; i++) {
            res[i] = get(paramTypes[i]);
        }
        return res;
    }
}
