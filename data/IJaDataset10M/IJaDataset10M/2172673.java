package ar.com.khronos.web.flex;

import org.aopalliance.intercept.MethodInvocation;
import ar.com.khronos.core.common.BusinessException;

/**
 * Interceptor de servicios llamados desde Flex.
 * <p>
 * Se encarga de dejar rastro de cualquier error producido.
 * 
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 */
public class FlexErrorHandlerInterceptor extends FlexServiceInterceptor {

    @Override
    public Object doInvocation(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Throwable t) {
            if (t instanceof BusinessException) {
                logger.error(t.getMessage(), t.getCause());
            } else {
                logger.fatal(t.getMessage(), t);
            }
            throw t;
        }
    }
}
