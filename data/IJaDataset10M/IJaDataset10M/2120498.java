package org.o14x.utilz.adapter;

import java.lang.reflect.Method;
import org.o14x.utilz.converters.Converter;
import org.o14x.utilz.invocation.MethodMapper;
import org.o14x.utilz.invocation.MethodMapping;

/**
 * An abstract basic implementation of ObjectWrapper.<br/>
 * Method processInvoke remains abstract.
 * 
 * @see org.o14x.utilz.adapter.ObjectWrapper
 * 
 * @author Olivier Dangr�aux
 */
public abstract class AbstractObjectWrapper implements ObjectWrapper {

    private Object wrappedObject;

    private MethodMapper methodMapper;

    /**
	 * Instantiates a new AbstractObjectWrapper.
	 */
    public AbstractObjectWrapper() {
        methodMapper = new MethodMapper();
    }

    /**
	 * Instantiates a new AbstractObjectWrapper wrapping the given object.
	 * 
	 * @param wrappedObject The object to be wrapped.
	 */
    public AbstractObjectWrapper(Object wrappedObject) {
        this();
        setWrappedObject(wrappedObject);
    }

    public final void setWrappedObject(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public final Object getWrappedObject() {
        return wrappedObject;
    }

    /**
	 * Adds a MethodMapping to this ObjectWrapper.<br/>
	 * A MethodMapping permits to transmit automatically a call to this ObjectWrapper to a method of the wrapped object.
	 * 
	 * @param methodMapping A MethodMapping.
	 * 
	 * @see MethodMapping
	 */
    public void addMethodMapping(MethodMapping methodMapping) {
        methodMapper.addMethodMapping(methodMapping);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object returnValue = null;
        MethodMapping methodMapping = methodMapper.getMethodMapping(method);
        if (method.equals(ObjectAdapter.class.getMethod("getUnderlyingObject", new Class[0]))) {
            returnValue = getWrappedObject();
        } else if (methodMapping != null) {
            Object[] arguments;
            int[] parameterIndexTab = methodMapping.getParameterIndexTab();
            Converter[] converters = methodMapping.getConverterTab();
            if (parameterIndexTab != null) {
                arguments = new Object[parameterIndexTab.length];
                for (int i = 0; i < parameterIndexTab.length; i++) {
                    arguments[i] = args[parameterIndexTab[i]];
                    if (converters != null && converters.length > i) {
                        Converter converter = converters[i];
                        if (converter != null) {
                            arguments[i] = converter.convert(args[parameterIndexTab[i]]);
                        }
                    }
                }
            } else {
                arguments = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    arguments[i] = args[i];
                    if (converters != null && converters.length > i) {
                        Converter converter = converters[i];
                        if (converter != null) {
                            arguments[i] = converter.convert(args[i]);
                        }
                    }
                }
            }
            returnValue = methodMapping.getBMethod().invoke(getWrappedObject(), arguments);
        } else {
            returnValue = processInvoke(method, args);
        }
        return returnValue;
    }

    public abstract Object processInvoke(Method method, Object[] args) throws Throwable;
}
