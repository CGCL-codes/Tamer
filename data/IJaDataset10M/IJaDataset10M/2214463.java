package photorganizer.common.logging;

import java.lang.reflect.Method;
import photorganizer.common.bean.BeanHome;
import photorganizer.common.bean.Interceptor;
import photorganizer.common.bean.Request;

public class LoggingInterceptor implements Interceptor {

    public Object invoke(Request request) throws Throwable {
        Object result = null;
        Method method = request.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Tracer tracer = BeanHome.get(Tracer.class);
        tracer.entering(className, methodName, request.getParameters());
        try {
            result = request.invoke();
        } catch (Throwable throwable) {
            tracer.throwing(className, methodName, throwable);
            throw throwable;
        }
        if (void.class.equals(method.getReturnType())) {
            tracer.exiting(className, methodName);
        } else {
            tracer.exiting(className, methodName, result);
        }
        return result;
    }
}
