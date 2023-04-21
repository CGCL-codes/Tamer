package sfeir.gwt.ergosoom.server.guice;

import java.util.logging.Logger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Deprecated
public class LoggingInterceptor implements MethodInterceptor {

    private Logger log = Logger.getLogger(LoggingInterceptor.class.getName());

    @Override
    public Object invoke(MethodInvocation method) throws Throwable {
        log.info("calling " + method.getMethod().getName());
        Object ret = method.proceed();
        log.info(ret + " returned to client.");
        return ret;
    }
}
