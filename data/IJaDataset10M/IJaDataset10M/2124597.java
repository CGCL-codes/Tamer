package gap.jac.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

/** Uses Unsafe.allocateObject() to instantiate classes; only used for
    bootstrapping. */
class BootstrapConstructorAccessorImpl extends ConstructorAccessorImpl {

    private Constructor constructor;

    BootstrapConstructorAccessorImpl(Constructor c) {
        this.constructor = c;
    }

    public Object newInstance(Object[] args) throws IllegalArgumentException, InvocationTargetException {
        try {
            return this.constructor.newInstance(args);
        } catch (InstantiationException e) {
            throw new InvocationTargetException(e);
        } catch (IllegalAccessException e) {
            throw new InvocationTargetException(e);
        }
    }
}
