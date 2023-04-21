package ar.com.puntosoft.shared.server.spring;

import java.util.logging.Logger;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import ar.com.puntosoft.shared.domain.ApplicationException;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

public class AppEngineExceptionsAdvice implements MethodInterceptor {

    private static final Logger log = Logger.getLogger(AppEngineExceptionsAdvice.class.getName());

    @Override
    public Object invoke(MethodInvocation arg0) throws Throwable {
        try {
            log.fine("-> AppEngineExceptionsAdvice: " + arg0.toString());
            return arg0.proceed();
        } catch (DatastoreTimeoutException e) {
            e.printStackTrace();
            return arg0.proceed();
        } catch (DatastoreNeedIndexException e) {
            e.printStackTrace();
            ApplicationException ex = new ApplicationException(e);
            throw ex;
        }
    }
}
