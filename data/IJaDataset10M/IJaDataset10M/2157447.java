package net.siop;

import net.siop.receiver.DefaultMessageReceiver;
import net.siop.receiver.InvocationReceiver;

public class InstanceMethod implements InvocationReceiver {

    Object context;

    String method;

    public InstanceMethod(Object context, String method) {
        super();
        this.context = context;
        this.method = method;
    }

    public static InstanceMethod getContextMethod(Object context, String method) {
        return new InstanceMethod(context, method);
    }

    public Object getContext() {
        return context;
    }

    public String getMethod() {
        return method;
    }

    public int hashCode() {
        return Bridge.getObjectId(context).hashCode() ^ method.hashCode();
    }

    public boolean equals(Object other) {
        if (other instanceof InstanceMethod) {
            InstanceMethod objectMethod = (InstanceMethod) other;
            return context == objectMethod.context && method.equals(objectMethod.method);
        }
        return false;
    }

    public Object invoke(Object[] parameters) {
        return DefaultMessageReceiver.sendMessage(getContext(), getMethod(), parameters);
    }
}
