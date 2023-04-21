package com.em.validation.rebind.reflector;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnnotationInvocationHandler implements InvocationHandler {

    private Map<String, Object> override = new HashMap<String, Object>();

    private Annotation annotation = null;

    private String id = "";

    public <T extends Annotation> AnnotationInvocationHandler(T annotation, Map<String, Object> override) {
        this.annotation = annotation;
        this.override = override;
        this.id = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        StringBuilder signature = new StringBuilder();
        signature.append("@");
        signature.append(annotation.annotationType().getName());
        signature.append("(proxyid=");
        signature.append(this.id);
        if (this.override.size() > 0) {
            signature.append(", overrides=[");
            int index = 0;
            for (String key : this.override.keySet()) {
                if (index++ > 0) {
                    signature.append(", ");
                }
                signature.append(key);
            }
            signature.append("]");
        }
        signature.append(")");
        this.override.put("toString", signature.toString());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = this.override.get(method.getName());
        if (result == null) {
            result = method.invoke(this.annotation, args);
        }
        return result;
    }
}
