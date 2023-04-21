package org.xsocket.stream;

import java.io.IOException;
import org.jruby.exceptions.RaiseException;
import org.xsocket.stream.IConnectHandler;
import org.xsocket.stream.IDataHandler;
import org.xsocket.stream.INonBlockingConnection;

/**
* Workaround support class for usage within JRuby
*
* Implementation base for handler implemented in JRuby. The adapter restores
* the native exception of a JRuby org.jruby.exceptions.RaiseException.
* @author grro@xsocket.org
*/
public final class JRubyHandlerAdapter implements IConnectHandler, IDisconnectHandler, IDataHandler, ITimeoutHandler, IConnectionScoped, org.xsocket.ILifeCycle {

    private IHandler delegee = null;

    private boolean isConnectionScoped = false;

    private boolean isConnectHandler = false;

    private boolean isDisconnectHandler = false;

    private boolean isDataHandler = false;

    private boolean isTimeoutHandler = false;

    private boolean isLifeCycleHandler = false;

    public JRubyHandlerAdapter(IHandler handler) {
        this.delegee = handler;
        isConnectionScoped = (handler instanceof IConnectionScoped);
        isConnectHandler = (handler instanceof IConnectHandler);
        isDisconnectHandler = (handler instanceof IDisconnectHandler);
        isDataHandler = (handler instanceof IDataHandler);
        isTimeoutHandler = (handler instanceof ITimeoutHandler);
        isLifeCycleHandler = (handler instanceof org.xsocket.ILifeCycle);
    }

    public boolean onConnect(INonBlockingConnection connection) throws IOException {
        try {
            if (isConnectHandler) {
                return ((IConnectHandler) delegee).onConnect(connection);
            } else {
                return false;
            }
        } catch (RaiseException jrubyEx) {
            mapException(jrubyEx);
            return false;
        }
    }

    public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
        try {
            if (isDisconnectHandler) {
                return ((IDisconnectHandler) delegee).onDisconnect(connection);
            } else {
                return false;
            }
        } catch (RaiseException jrubyEx) {
            mapException(jrubyEx);
            return false;
        }
    }

    public boolean onData(INonBlockingConnection connection) throws IOException {
        try {
            if (isDataHandler) {
                return ((IDataHandler) delegee).onData(connection);
            } else {
                return false;
            }
        } catch (RaiseException jrubyEx) {
            mapException(jrubyEx);
            return false;
        }
    }

    public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
        try {
            if (isTimeoutHandler) {
                return ((ITimeoutHandler) delegee).onConnectionTimeout(connection);
            } else {
                return false;
            }
        } catch (RaiseException jrubyEx) {
            mapException(jrubyEx);
            return false;
        }
    }

    public boolean onIdleTimeout(INonBlockingConnection connection) throws IOException {
        try {
            if (isTimeoutHandler) {
                return ((ITimeoutHandler) delegee).onIdleTimeout(connection);
            } else {
                return false;
            }
        } catch (RaiseException jrubyEx) {
            mapException(jrubyEx);
            return false;
        }
    }

    public void onInit() {
        if (isLifeCycleHandler) {
            ((org.xsocket.ILifeCycle) delegee).onInit();
        }
    }

    public void onDestroy() {
        if (isLifeCycleHandler) {
            ((org.xsocket.ILifeCycle) delegee).onDestroy();
        }
    }

    private void mapException(RaiseException jrubyEx) throws IOException {
        if (jrubyEx.getCause() == null) {
            throw new IOException(jrubyEx.getMessage());
        }
        Class nativeExceptionclass = jrubyEx.getCause().getClass();
        if (RuntimeException.class.isAssignableFrom(nativeExceptionclass)) {
            throw (RuntimeException) jrubyEx.getCause();
        }
        if (IOException.class.isAssignableFrom(nativeExceptionclass)) {
            throw (IOException) jrubyEx.getCause();
        }
        throw new RuntimeException(jrubyEx.getMessage());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object clone() throws CloneNotSupportedException {
        if (isConnectionScoped) {
            JRubyHandlerAdapter copy = (JRubyHandlerAdapter) super.clone();
            copy.delegee = (IHandler) ((IConnectionScoped) this.delegee).clone();
            return copy;
        } else {
            return this;
        }
    }
}
