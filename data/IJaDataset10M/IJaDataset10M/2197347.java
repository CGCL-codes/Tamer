package org.xactor.ws.server;

import java.lang.reflect.Method;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.xml.ws.WebServiceContext;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.invocation.AbstractInvocationHandler;
import org.jboss.wsf.spi.invocation.Invocation;
import org.jboss.wsf.spi.invocation.InvocationContext;
import org.jboss.wsf.spi.invocation.WebServiceContextInjector;
import org.xactor.tm.TxManager;
import org.xactor.ws.atomictx.TxServerHandler;

/**
 * Handles invocations on JSE endpoints.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 25-Apr-2007
 */
public class InvocationHandlerJSE extends AbstractInvocationHandler {

    private TransactionManager tm;

    public void create(Endpoint ep) {
        super.create(ep);
        tm = TxManager.getInstance();
    }

    protected Object getBeanInstance(Endpoint ep) throws InstantiationException, IllegalAccessException {
        Class epImpl = ep.getTargetBeanClass();
        Object targetBean = epImpl.newInstance();
        return targetBean;
    }

    public void invoke(Endpoint ep, Invocation epInv) throws Exception {
        invoke(ep, null, epInv);
    }

    public void invoke(Endpoint ep, Object beanInstance, Invocation epInv) throws Exception {
        try {
            if (beanInstance == null) beanInstance = getBeanInstance(ep);
            InvocationContext invContext = epInv.getInvocationContext();
            WebServiceContext wsContext = invContext.getAttachment(WebServiceContext.class);
            if (wsContext != null) {
                new WebServiceContextInjector().injectContext(beanInstance, (WebServiceContext) wsContext);
            }
            Method method = getImplMethod(beanInstance.getClass(), epInv.getJavaMethod());
            Transaction tx = TxServerHandler.getCurrentTransaction();
            Object retObj = null;
            try {
                if (tx != null) tm.resume(tx);
                retObj = method.invoke(beanInstance, epInv.getArgs());
            } finally {
                if (tx != null) tm.suspend();
            }
            epInv.setReturnValue(retObj);
        } catch (Exception e) {
            handleInvocationException(e);
        }
    }
}
