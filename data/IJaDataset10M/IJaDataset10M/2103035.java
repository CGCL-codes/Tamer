package it.xargon.lrpc;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import it.xargon.util.*;
import it.xargon.lrpc.LXmpInvocationAnswer.AnswerType;
import it.xargon.lrpc.LXmpObjectDescription.Flavor;

class LocalObjectWrapper {

    private Identifier objectId = null;

    private Class<?>[] interfaces = null;

    private Object wrappedObj = null;

    private LRpcEndpointImpl iendpoint = null;

    public LocalObjectWrapper(LRpcEndpointImpl endpoint, Identifier objId, Object obj) {
        objectId = objId;
        wrappedObj = obj;
        iendpoint = endpoint;
        interfaces = extractAllInterfaces(wrappedObj);
        if (interfaces == null) {
            String etx = "Object has no publishable interfaces!\n";
            etx += "toString: " + obj.toString() + "\n";
            etx += "getClass: " + obj.getClass().getName();
            throw new IllegalStateException(etx);
        }
    }

    private static Class<?>[] extractAllInterfaces(Object ref) {
        if (ref == null) throw new IllegalArgumentException();
        HashSet<Class<?>> ifaces = new HashSet<Class<?>>();
        Class<?> scanning = ref.getClass();
        while (scanning != null) {
            Class<?>[] scanned = scanning.getInterfaces();
            for (Class<?> iface : scanned) {
                if (Modifier.isPublic(iface.getModifiers())) ifaces.add(iface);
            }
            scanning = scanning.getSuperclass();
        }
        if (ifaces.size() == 0) return null;
        return ifaces.toArray(new Class<?>[ifaces.size()]);
    }

    public Object getWrappedObject() {
        return wrappedObj;
    }

    public Identifier getObjectId() {
        return objectId;
    }

    public void dispose() {
        objectId = null;
        interfaces = null;
        wrappedObj = null;
    }

    public Class<?>[] getInterfaces() {
        return Arrays.copyOf(interfaces, interfaces.length);
    }

    public String[] getInterfaceNames() {
        String[] result = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) result[i] = interfaces[i].getName();
        return result;
    }

    public LXmpInvocationAnswer lxmpInvoke(LXmpInvocation invocation) {
        String targetClassName = invocation.targetClass;
        String targetMethodName = invocation.methodName;
        String[] targetMethodSignature = invocation.signature;
        LXmpInvocationAnswer answer = new LXmpInvocationAnswer();
        LXmpObjectDescription desc = new LXmpObjectDescription();
        Class<?> targetClass = null;
        try {
            targetClass = Tools.getTypeForName(targetClassName);
        } catch (ClassNotFoundException ex) {
            answer.answtype = AnswerType.NOCLASS;
            answer.content = new LXmpObjectDescription();
            answer.content.flavor = Flavor.SERIALIZED;
            answer.content.sercontents = Bitwise.serializeObject(targetClassName);
            return answer;
        }
        Class<?>[] signature = new Class<?>[targetMethodSignature.length];
        for (int i = 0; i < signature.length; i++) {
            try {
                signature[i] = Tools.getTypeForName(targetMethodSignature[i]);
            } catch (ClassNotFoundException ex) {
                answer.answtype = AnswerType.NOCLASS;
                answer.content = new LXmpObjectDescription();
                answer.content.flavor = Flavor.SERIALIZED;
                answer.content.sercontents = Bitwise.serializeObject(targetMethodSignature[i]);
                return answer;
            }
        }
        Method method = null;
        try {
            method = targetClass.getMethod(targetMethodName, signature);
        } catch (NoSuchMethodException ex) {
            answer.answtype = AnswerType.NOMETHOD;
            answer.content = new LXmpObjectDescription();
            answer.content.flavor = Flavor.SERIALIZED;
            answer.content.sercontents = Bitwise.serializeObject(targetMethodName);
            return answer;
        }
        Class<?> returnType = method.getReturnType();
        Annotation[] methodAnnots = method.getAnnotations();
        Annotation methodAnnot = null;
        for (Annotation iann : methodAnnots) {
            if (iann.annotationType().equals(ByRef.class) || iann.annotationType().equals(BySer.class)) methodAnnot = iann;
        }
        LXmpObjectDescription[] xmpargs = invocation.arguments;
        Object[] args = new Object[xmpargs.length];
        try {
            for (int iarg = 0; iarg < args.length; iarg++) {
                args[iarg] = iendpoint.unmarshalObject(xmpargs[iarg]);
            }
        } catch (ClassNotFoundException ex) {
            answer.answtype = AnswerType.FAILURE;
            desc.flavor = Flavor.SERIALIZED;
            desc.sercontents = Bitwise.serializeObject(ex);
            answer.content = desc;
        }
        Object result = null;
        Throwable externalException = null;
        Throwable internalException = null;
        LRpcFactoryInternal.enterLocalInvocation(iendpoint);
        try {
            result = method.invoke(wrappedObj, args);
        } catch (IllegalArgumentException e) {
            externalException = e;
        } catch (IllegalAccessException e) {
            externalException = e;
        } catch (InvocationTargetException e) {
            internalException = e.getCause();
        } catch (Throwable t) {
            externalException = t;
        }
        LRpcFactoryInternal.exitLocalInvocation();
        if (method.isAnnotationPresent(LRpcImmediate.class)) return null;
        if (externalException != null) {
            answer.answtype = AnswerType.FAILURE;
            desc.flavor = Flavor.SERIALIZED;
            desc.sercontents = Bitwise.serializeObject(externalException);
        } else if (internalException != null) {
            answer.answtype = AnswerType.EXCEPTION;
            desc.flavor = Flavor.SERIALIZED;
            LRpcRemoteException wrapperException = new LRpcRemoteException("Exception happened on remote side of " + iendpoint.getXmpConnection().toString(), internalException);
            desc.sercontents = Bitwise.serializeObject(wrapperException);
        } else {
            try {
                desc = iendpoint.marshalObject(result, returnType, methodAnnot);
                answer.answtype = AnswerType.SUCCESS;
            } catch (IllegalArgumentException ex) {
                answer.answtype = AnswerType.FAILURE;
                desc.flavor = Flavor.SERIALIZED;
                desc.sercontents = Bitwise.serializeObject(new LRpcException("Return value not marshallable", ex));
            }
        }
        answer.content = desc;
        return answer;
    }
}
