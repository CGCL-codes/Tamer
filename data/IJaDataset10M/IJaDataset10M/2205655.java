package com.caucho.hessian.client;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.HessianRemote;
import com.caucho.services.server.AbstractSkeleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.WeakHashMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proxy implementation for Hessian clients.  Applications will generally
 * use HessianProxyFactory to create proxy clients.
 */
public class HessianProxy implements InvocationHandler, Serializable {

    private static final Logger log = Logger.getLogger(HessianProxy.class.getName());

    protected HessianProxyFactory _factory;

    private WeakHashMap<Method, String> _mangleMap = new WeakHashMap<Method, String>();

    private Class<?> _type;

    private URL _url;

    /**
     * Protected constructor for subclassing
     */
    protected HessianProxy(URL url, HessianProxyFactory factory) {
        this(url, factory, null);
    }

    /**
     * Protected constructor for subclassing
     */
    protected HessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
        _factory = factory;
        _url = url;
        _type = type;
    }

    /**
     * Returns the proxy's URL.
     */
    public URL getURL() {
        return _url;
    }

    /**
     * Handles the object invocation.
     *
     * @param proxy the proxy object to invoke
     * @param method the method to call
     * @param args the arguments to the proxy object
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mangleName;
        synchronized (_mangleMap) {
            mangleName = _mangleMap.get(method);
        }
        if (mangleName == null) {
            String methodName = method.getName();
            Class<?>[] params = method.getParameterTypes();
            if (methodName.equals("equals") && params.length == 1 && params[0].equals(Object.class)) {
                Object value = args[0];
                if (value == null || !Proxy.isProxyClass(value.getClass())) {
                    return Boolean.FALSE;
                }
                Object proxyHandler = Proxy.getInvocationHandler(value);
                if (!(proxyHandler instanceof HessianProxy)) {
                    return Boolean.FALSE;
                }
                HessianProxy handler = (HessianProxy) proxyHandler;
                return new Boolean(_url.equals(handler.getURL()));
            } else if (methodName.equals("hashCode") && params.length == 0) {
                return new Integer(_url.hashCode());
            } else if (methodName.equals("getHessianType")) {
                return proxy.getClass().getInterfaces()[0].getName();
            } else if (methodName.equals("getHessianURL")) {
                return _url.toString();
            } else if (methodName.equals("toString") && params.length == 0) {
                return "HessianProxy[" + _url + "]";
            }
            if (!_factory.isOverloadEnabled()) {
                mangleName = method.getName();
            } else {
                mangleName = mangleName(method);
            }
            synchronized (_mangleMap) {
                _mangleMap.put(method, mangleName);
            }
        }
        InputStream is = null;
        HessianConnection conn = null;
        try {
            if (log.isLoggable(Level.FINER)) {
                log.finer("Hessian[" + _url + "] calling " + mangleName);
            }
            conn = sendRequest(mangleName, args);
            is = conn.getInputStream();
            AbstractHessianInput in;
            int code = is.read();
            if (code == 'H') {
                int major = is.read();
                int minor = is.read();
                in = _factory.getHessian2Input(is);
                Object value = in.readReply(method.getReturnType());
                return value;
            } else if (code == 'r') {
                int major = is.read();
                int minor = is.read();
                in = _factory.getHessianInput(is);
                in.startReplyBody();
                Object value = in.readObject(method.getReturnType());
                if (value instanceof InputStream) {
                    value = new ResultInputStream(conn, is, in, (InputStream) value);
                    is = null;
                    conn = null;
                } else {
                    in.completeReply();
                }
                return value;
            } else {
                throw new HessianProtocolException("'" + (char) code + "' is an unknown code");
            }
        } catch (HessianProtocolException e) {
            throw new HessianRuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            try {
                if (conn != null) {
                    conn.destroy();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
        }
    }

    protected String mangleName(Method method) {
        Class<?>[] param = method.getParameterTypes();
        if (param == null || param.length == 0) {
            return method.getName();
        } else {
            return AbstractSkeleton.mangleName(method, false);
        }
    }

    /**
     * Sends the HTTP request to the Hessian connection.
     */
    protected HessianConnection sendRequest(String methodName, Object[] args) throws IOException {
        HessianConnection conn = null;
        conn = _factory.getConnectionFactory().open(_url);
        boolean isValid = false;
        try {
            addRequestHeaders(conn);
            OutputStream os = null;
            try {
                os = conn.getOutputStream();
            } catch (Exception e) {
                throw new HessianRuntimeException(e);
            }
            AbstractHessianOutput out = _factory.getHessianOutput(os);
            out.call(methodName, args);
            out.flush();
            conn.sendRequest();
            isValid = true;
            return conn;
        } finally {
            if (!isValid && conn != null) {
                conn.destroy();
            }
        }
    }

    /**
     * Method that allows subclasses to add request headers such as cookies.
     * Default implementation is empty.
     */
    protected void addRequestHeaders(HessianConnection conn) {
        conn.addHeader("Content-Type", "x-application/hessian");
        String basicAuth = _factory.getBasicAuth();
        if (basicAuth != null) {
            conn.addHeader("Authorization", basicAuth);
        }
    }

    /**
     * Method that allows subclasses to parse response headers such as cookies.
     * Default implementation is empty.
     * @param conn
     */
    protected void parseResponseHeaders(URLConnection conn) {
    }

    public Object writeReplace() {
        return new HessianRemote(_type.getName(), _url.toString());
    }

    static class ResultInputStream extends InputStream {

        private HessianConnection _conn;

        private InputStream _connIs;

        private AbstractHessianInput _in;

        private InputStream _hessianIs;

        ResultInputStream(HessianConnection conn, InputStream is, AbstractHessianInput in, InputStream hessianIs) {
            _conn = conn;
            _connIs = is;
            _in = in;
            _hessianIs = hessianIs;
        }

        @Override
        public int read() throws IOException {
            if (_hessianIs != null) {
                int value = _hessianIs.read();
                if (value < 0) {
                    close();
                }
                return value;
            } else {
                return -1;
            }
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            if (_hessianIs != null) {
                int value = _hessianIs.read(buffer, offset, length);
                if (value < 0) {
                    close();
                }
                return value;
            } else {
                return -1;
            }
        }

        @Override
        public void close() throws IOException {
            HessianConnection conn = _conn;
            _conn = null;
            InputStream connIs = _connIs;
            _connIs = null;
            AbstractHessianInput in = _in;
            _in = null;
            InputStream hessianIs = _hessianIs;
            _hessianIs = null;
            try {
                if (hessianIs != null) {
                    hessianIs.close();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            try {
                if (in != null) {
                    in.completeReply();
                    in.close();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            try {
                if (connIs != null) {
                    connIs.close();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                log.log(Level.FINE, e.toString(), e);
            }
        }
    }
}
