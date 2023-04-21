package com.technoetic.dof.transport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.technoetic.dof.transport.security.AuthenticationException;
import com.technoetic.dof.transport.security.Authenticator;

public class RequestDispatcher {

    private final Logger log = Logger.getLogger(getClass());

    private Map<String, DistributedObjectAdapter> distributedObjectsByOid = Collections.synchronizedMap(new HashMap<String, DistributedObjectAdapter>());

    private Map<Object, DistributedObjectAdapter> distributedObjectsByObject = Collections.synchronizedMap(new HashMap<Object, DistributedObjectAdapter>());

    public RequestDispatcher() {
    }

    public void registerObject(String oid, Object object, Object endPoint, Authenticator authenticator) {
        DistributedObjectAdapter adapter = new DistributedObjectAdapter(oid, endPoint, object, authenticator);
        if (distributedObjectsByOid.put(oid, adapter) != null) {
            log.warn("Replacing existing distributed object adapter: oid=" + oid);
        }
        if (distributedObjectsByObject.put(object, adapter) != null) {
            log.warn("Replacing existing distributed object adapter: oid=" + oid);
        }
    }

    public boolean isDistributedObject(Object object) {
        return distributedObjectsByObject.containsKey(object);
    }

    public void unregisterObject(String oid) {
        DistributedObjectAdapter adapter = lookupObject(oid);
        if (adapter == null) {
            log.warn("Attempt to remove nonregistered distributed object: oid=" + oid);
        } else {
            distributedObjectsByOid.remove(oid);
            distributedObjectsByObject.remove(adapter.getDelegate());
        }
    }

    public DistributedObjectAdapter lookupObject(String oid) {
        return (DistributedObjectAdapter) distributedObjectsByOid.get(oid);
    }

    protected DistributedObjectAdapter lookupObject(Object object) {
        return (DistributedObjectAdapter) distributedObjectsByObject.get(object);
    }

    public Response dispatch(Request request) throws AdapterNotFoundException {
        Object[] parameters = request.getParameters();
        DistributedObjectAdapter adapter = lookupObject(request.getObjectIdentifier());
        Response response = null;
        if (adapter == null) {
            throw new AdapterNotFoundException("Invalid target for request/reply: target='" + request.getObjectIdentifier() + "'");
        } else {
            Object target = adapter.getDelegate();
            if (adapter.getAuthenticator() != null) {
                try {
                    adapter.getAuthenticator().authenticate(target, request);
                } catch (AuthenticationException ex) {
                    try {
                        Method method = MethodHelper.getMethod(target.getClass(), request.getMethodSignature());
                        return MethodHelper.isAsynchronousMethod(method) ? null : new Response(ex, true);
                    } catch (NoSuchMethodException e) {
                        log.error("error while getting method", ex);
                    }
                }
            }
            response = invokeMethod(target, request.getMethodSignature(), parameters);
        }
        return response;
    }

    private Response makeResponse(Method method, Object result) {
        if (MethodHelper.isAsynchronousMethod(method) == false) {
            return new Response(result, result instanceof Throwable);
        }
        return null;
    }

    private Response invokeMethod(Object target, String methodSignature, Object[] parameters) {
        Response response = null;
        try {
            Method method = MethodHelper.getMethod(target.getClass(), methodSignature);
            try {
                Object result = method.invoke(target, parameters);
                response = makeResponse(method, result);
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getTargetException();
                } else if (ex.getTargetException() instanceof Error) {
                    throw (Error) ex.getTargetException();
                }
                if (log.isDebugEnabled()) {
                    log.warn("Sending exception to client: ex=", ex.getTargetException());
                } else {
                    log.warn("Sending exception to client: ex=" + ex.getTargetException());
                }
                response = makeResponse(method, ex.getTargetException());
            } catch (IllegalAccessException ex) {
                log.error("Invocation error", ex);
                response = makeResponse(method, ex);
            } catch (Exception ex) {
                response = makeResponse(method, ex);
            }
        } catch (NoSuchMethodException ex) {
            log.error("Invocation error", ex);
        }
        return response;
    }
}
