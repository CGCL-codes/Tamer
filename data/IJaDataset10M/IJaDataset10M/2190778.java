package org.jboss.tutorial.partial_deployment_descriptor.bean;

import javax.interceptor.InvocationContext;
import org.jboss.logging.Logger;

/**
 * FirstInterceptor
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
public class FirstInterceptor {

    private static Logger logger = Logger.getLogger(FirstInterceptor.class);

    public Object interceptorMethod(InvocationContext context) throws Exception {
        logger.info(this.getClass().getSimpleName() + " intercepted " + context.getTarget() + "'s " + context.getMethod());
        return context.proceed();
    }
}
