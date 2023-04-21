package uchicago.src.reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringArgInvoker extends Invoker {

    public StringArgInvoker(Object o, Method m, String param) {
        super(o, m, param);
    }

    protected void invoke() throws InvocationTargetException, IllegalAccessException {
        method.invoke(object, new Object[] { param });
    }
}
