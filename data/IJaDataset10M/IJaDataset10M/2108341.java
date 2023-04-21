package com.miranteinfo.seam.framework.service;

import org.jboss.seam.annotations.intercept.AroundInvoke;
import org.jboss.seam.annotations.intercept.Interceptor;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.intercept.InvocationContext;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.TransactionInterceptor;
import com.miranteinfo.seam.framework.service.exception.BusinessException;

/**
 * Intereptor responsavel por efetuar todo o controle transacional da camada de negocio (Service).
 * 
 * @author lucas lins
 * 
 */
@Interceptor(within = TransactionInterceptor.class)
public class TransactionSupportInterceptorImpl {

    public static final String TRANSACTION_SUPPORT_STARTED = "MIRSEAM_TRANSACTION_SUPPORT_STARTED";

    public static final String BUSINESS_EXCEPTION_ROLLBACK = "BUSINESS_EXCEPTION_ROLLBACK";

    @AroundInvoke
    public Object intercept(InvocationContext invocation) throws Exception {
        if (Contexts.getEventContext().isSet(TRANSACTION_SUPPORT_STARTED) || !Transaction.instance().isActive()) return invocation.proceed();
        Contexts.getEventContext().set(TRANSACTION_SUPPORT_STARTED, true);
        ServiceSupport service = (ServiceSupport) invocation.getTarget();
        Object returnedValue;
        try {
            returnedValue = invocation.proceed();
            service.flush();
            service.raiseEvents();
        } catch (BusinessException be) {
            Contexts.getEventContext().set(BUSINESS_EXCEPTION_ROLLBACK, true);
            throw be;
        } finally {
            Contexts.getEventContext().remove(TRANSACTION_SUPPORT_STARTED);
        }
        return returnedValue;
    }
}
