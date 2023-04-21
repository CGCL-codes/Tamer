package org.freshvanilla.rmi;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.freshvanilla.net.DataSocket;
import org.freshvanilla.net.DataSockets;
import org.freshvanilla.net.WireFormat;
import org.freshvanilla.utils.Callback;
import org.freshvanilla.utils.Classes;
import org.freshvanilla.utils.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmiInvocationHandler implements InvocationHandler, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(RmiInvocationHandler.class);

    private static final Object[] NO_OBJECTS = {};

    private final Factory<String, DataSocket> _factory;

    private final boolean _closeFactory;

    private final ConcurrentMap<Method, RmiMethod> _rmiMethodMap;

    public RmiInvocationHandler(Factory<String, DataSocket> factory, boolean closeFactory) {
        _factory = factory;
        _closeFactory = closeFactory;
        _rmiMethodMap = new ConcurrentHashMap<Method, RmiMethod>(31);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RmiMethod rmiMethod = getRmiMethod(method);
        boolean async = rmiMethod._async;
        if (args == null) {
            args = NO_OBJECTS;
        }
        int argsLength = args.length - (async ? 1 : 0);
        DataSocket ds = _factory.acquire(async ? "async-org.freshvanilla.rmi" : "sync-org.freshvanilla.rmi");
        try {
            final long sequenceNumber = (async ? ds.microTimestamp() : 0);
            if (async) {
                Callback<?> callback = (Callback<?>) args[argsLength];
                ds.addCallback(sequenceNumber, callback);
                ds.setReader(new RmiCallback(ds));
            }
            WireFormat wf = ds.wireFormat();
            ByteBuffer wb = ds.writeBuffer();
            wf.writeNum(wb, sequenceNumber);
            wf.writeTag(wb, rmiMethod._methodName);
            wf.writeArray(wb, argsLength, args);
            wf.flush(ds, wb);
            if (async) {
                return null;
            }
            ByteBuffer rb = ds.read();
            long sequenceNumber2 = wf.readNum(rb);
            assert sequenceNumber2 == 0;
            boolean success = wf.readBoolean(rb);
            Object reply = wf.readObject(rb);
            if (success) {
                return Classes.parseAs(reply, rmiMethod._returnType);
            }
            if (reply instanceof Throwable) {
                Throwable t = (Throwable) reply;
                DataSockets.appendStackTrace(ds, t);
                throw t;
            }
            throw new AssertionError(reply);
        } finally {
            _factory.recycle(ds);
        }
    }

    public void close() {
        if (_closeFactory) {
            _factory.close();
        }
    }

    private RmiMethod getRmiMethod(Method method) {
        RmiMethod ret = _rmiMethodMap.get(method);
        if (ret == null) {
            ret = new RmiMethod(method.getName(), method.getReturnType(), method.getParameterTypes());
            RmiMethod prev = _rmiMethodMap.putIfAbsent(method, ret);
            if (prev != null) {
                ret = prev;
            }
        }
        return ret;
    }

    static class RmiCallback implements Callback<DataSocket> {

        private final DataSocket ds;

        RmiCallback(DataSocket ds) {
            this.ds = ds;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void onCallback(DataSocket dataSocket) throws Exception {
            Callback callback = null;
            try {
                ByteBuffer rb = ds.read();
                final WireFormat wf = ds.wireFormat();
                long sequenceNumber = wf.readNum(rb);
                callback = ds.removeCallback(sequenceNumber);
                assert sequenceNumber != 0;
                boolean success = wf.readBoolean(rb);
                Object reply = wf.readObject(rb);
                if (callback == null) {
                    LOG.error("Response to unknown callback reply=" + reply);
                } else if (success) {
                    callback.onCallback(reply);
                } else {
                    callback.onException((Throwable) reply);
                }
            } catch (Exception e) {
                if (ds.isClosed()) {
                    return;
                }
                if (!(e instanceof AsynchronousCloseException)) {
                    LOG.error("Exception thrown processing callback", e);
                }
                try {
                    if (callback != null) {
                        callback.onException(e);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        public void onException(Throwable t) {
            LOG.warn("Unhandled exception", t);
        }
    }

    static class RmiMethod {

        public final String _methodName;

        public final Class<?> _returnType;

        public final Class<?>[] _parameterTypes;

        public final boolean _async;

        RmiMethod(String methodName, Class<?> returnType, Class<?>[] parameterTypes) {
            _methodName = methodName;
            _returnType = returnType;
            _parameterTypes = parameterTypes;
            _async = (parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1] == Callback.class);
        }
    }
}
