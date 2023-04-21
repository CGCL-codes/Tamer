package org.igsl.functor.memoize;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

class Handler implements InvocationHandler {

    Object obj;

    HashMap<String, HashMap<Object, Object>> maps;

    Handler(Object obj, Class theInterface) {
        this.obj = obj;
        this.maps = new HashMap<String, HashMap<Object, Object>>();
        for (Method intMethod : theInterface.getMethods()) {
            for (Method objMethod : obj.getClass().getMethods()) {
                if (objMethod.getAnnotation(Memoize.class) == null) {
                    continue;
                }
                if (intMethod.getName().equals(objMethod.getName())) {
                    Class<?> intReturnClass = intMethod.getReturnType();
                    Class<?> objReturnClass = objMethod.getReturnType();
                    if (intReturnClass != null && objReturnClass != null && intReturnClass.isAssignableFrom(objReturnClass)) {
                        Class<?>[] intParameterClasses = intMethod.getParameterTypes();
                        Class<?>[] objParameterClasses = objMethod.getParameterTypes();
                        if (intParameterClasses.length == objParameterClasses.length) {
                            boolean mismatched = false;
                            for (int i = 0; i < intParameterClasses.length; ++i) {
                                if (!intParameterClasses[i].isAssignableFrom(objParameterClasses[i])) {
                                    mismatched = true;
                                    break;
                                }
                            }
                            if (!mismatched) {
                                maps.put(objMethod.getName(), new HashMap<Object, Object>());
                            }
                        }
                    }
                }
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        HashMap<Object, Object> map = (HashMap<Object, Object>) maps.get(method.getName());
        if (map != null) {
            ArrayList<Object> arg = new ArrayList<Object>(args.length);
            for (Object obj : args) {
                arg.add(obj);
            }
            result = map.get(arg);
            if (result == null) {
                result = method.invoke(obj, args);
                map.put(arg, result);
            }
        } else {
            result = method.invoke(obj, args);
        }
        return result;
    }
}
