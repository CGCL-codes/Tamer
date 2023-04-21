package com.googlecode.mycontainer.ejb;

import java.io.Serializable;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.xml.rpc.handler.MessageContext;
import com.googlecode.mycontainer.kernel.KernelRuntimeException;

public class MySessionContext extends MyEJBContext implements SessionContext, Serializable {

    private static final long serialVersionUID = 4325436547632435L;

    public MySessionContext(Context ctx, String info) {
        super(ctx, info);
    }

    public <T> T getBusinessObject(Class<T> arg0) throws IllegalStateException {
        throw new KernelRuntimeException("not supported");
    }

    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        throw new KernelRuntimeException("not supported");
    }

    public EJBObject getEJBObject() throws IllegalStateException {
        throw new KernelRuntimeException("not supported");
    }

    public Class<?> getInvokedBusinessInterface() throws IllegalStateException {
        throw new KernelRuntimeException("not supported");
    }

    public MessageContext getMessageContext() throws IllegalStateException {
        throw new KernelRuntimeException("not supported");
    }
}
