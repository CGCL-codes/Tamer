package com.rif.server.service.skeleton.impl;

import java.lang.reflect.InvocationTargetException;
import com.rif.common.IServiceEndpoint;
import com.rif.common.request.IServiceRequest;
import com.rif.common.response.IServiceResponse;
import com.rif.common.response.impl.ServiceResponseImpl;
import com.rif.server.service.execute.ServiceExecuteManager;
import com.rif.server.service.finder.IServiceFinder;
import com.rif.server.service.finder.ServiceFinderManager;
import com.rif.server.service.skeleton.IServiceSkeleton;

/**
 * 
 * @author bruce.liu (mailto:jxta.liu@gmail.com)
 * 2011-7-14 下午11:35:46
 */
public class DefaultServiceSkeletonImpl implements IServiceSkeleton {

    private IServiceEndpoint serviceEndpoint;

    private IServiceFinder serviceFinder;

    private Object targetObject;

    @SuppressWarnings("rawtypes")
    private Class targetClazz;

    public DefaultServiceSkeletonImpl() {
    }

    public DefaultServiceSkeletonImpl(IServiceEndpoint serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        this.serviceFinder = ServiceFinderManager.INSTANCE.getFind(serviceEndpoint);
        this.targetObject = serviceFinder.find(serviceEndpoint);
        this.targetClazz = serviceFinder.getType(serviceEndpoint);
    }

    @Override
    public void execute(IServiceRequest request, IServiceResponse response) {
        String methodName = request.getOperationName();
        String[] signatures = request.getSignatures();
        Object[] args = request.getArgs();
        try {
            Object result = ServiceExecuteManager.INSTANCE.getServiceExecute().execute(targetClazz, targetObject, methodName, signatures, args);
            ((ServiceResponseImpl) response).setResult(result);
            ((ServiceResponseImpl) response).setSucess(true);
        } catch (InvocationTargetException ite) {
            ((ServiceResponseImpl) response).setException(ite.getTargetException());
            ((ServiceResponseImpl) response).setFail(true);
            ite.printStackTrace();
        } catch (Throwable t) {
            ((ServiceResponseImpl) response).setException(t);
            ((ServiceResponseImpl) response).setFail(true);
            t.printStackTrace();
        }
    }

    public IServiceEndpoint getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(IServiceEndpoint serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public IServiceFinder getServiceFinder() {
        return serviceFinder;
    }

    public void setServiceFinder(IServiceFinder serviceFinder) {
        this.serviceFinder = serviceFinder;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    @SuppressWarnings("rawtypes")
    public Class getTargetClazz() {
        return targetClazz;
    }

    @SuppressWarnings("rawtypes")
    public void setTargetClazz(Class targetClazz) {
        this.targetClazz = targetClazz;
    }
}
