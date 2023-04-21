package org.tru42.signal.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.tru42.signal.lang.ConnectException;
import org.tru42.signal.lang.ISignal;
import org.tru42.signal.lang.ISink;

public class Sink implements ISink {

    private final Object owner;

    private final Method method;

    protected ISignal signal;

    public Sink(Object owner, Method sink) {
        this.owner = owner;
        this.method = sink;
    }

    public void signalValueChanged(long timestamp, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int len = method.getParameterTypes().length;
        Object[] values = new Object[len];
        if (len > 0) {
            values[0] = timestamp;
            for (int i = 1; i < args.length + 1 && i < len; i++) values[i] = args[i - 1];
        }
        method.invoke(owner, values);
    }

    public ISignal getSignal() {
        return signal;
    }

    public String getName() {
        return method.getName();
    }

    public Object getOwner() {
        return owner;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?>[] getTypes() {
        return method.getParameterTypes();
    }

    public boolean compatible(Object obj) {
        if (obj instanceof ISignal) {
            Class<?>[] localTypes = method.getParameterTypes();
            Class<?>[] signalTypes = ((ISignal) obj).getTypes();
            if (signalTypes.length < localTypes.length) {
                return false;
            }
            for (int i = 0; i < localTypes.length; i++) if (!(localTypes[i].isAssignableFrom(signalTypes[i]))) {
                return false;
            }
            return true;
        } else return false;
    }

    public void connect(Object obj) throws ConnectException {
        try {
            connectSignal((ISignal) obj);
            signal.connectSink(this);
        } catch (Exception e) {
            disconnectSignal();
            throw new ConnectException("Could not connect!\n" + e.getMessage());
        }
    }

    public void disconnect() {
        if (owner instanceof ISignalProcessor && ((ISignalProcessor) owner).getModel() != null) ((ISignalProcessor) owner).getModel().disconnect(this); else {
            signal.disconnectSink(this);
            disconnectSignal();
        }
    }

    @Override
    public boolean isConnected() {
        return signal != null;
    }

    public void connectSignal(ISignal signal) throws ConnectException {
        if (!compatible(signal)) throw new ConnectException("Incompatible signal");
        this.signal = signal;
    }

    protected void disconnectSignal() {
        this.signal = null;
    }
}
