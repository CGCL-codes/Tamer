package com.tencent.tendon.convert;

import java.lang.reflect.Constructor;

/**
 *
 * @author nbzhang
 */
public final class ObjectCreator<T> implements Creatable<T> {

    protected final Class<T> type;

    protected final Constructor<T> constructor;

    @SuppressWarnings("unchecked")
    public ObjectCreator(Class<? extends T> type) {
        this.type = (Class<T>) type;
        Constructor<?> c = type.getDeclaredConstructors()[0];
        for (Constructor cc : type.getConstructors()) {
            if (cc.getParameterTypes().length == 0) {
                cc.setAccessible(true);
                c = cc;
                break;
            }
        }
        this.constructor = (Constructor<T>) c;
    }

    @Override
    public T create() {
        try {
            return this.constructor.newInstance();
        } catch (Exception ex) {
            throw new ConvertException("can not new a instance for " + type, ex);
        }
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }
}
