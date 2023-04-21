package jws.services.remoting;

import jws.core.TypedValue;
import jws.util.SysUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * Copyright JaNet systems LLC
 * Author: Anton Serzhantov
 * Date: 30.10.2009
 * E-mail: aserzhantov@janetsys.com
 */
class RemoteObjectWrapper implements IRemoteObject {

    private IRemoteObject _delegate;

    public RemoteObjectWrapper(IRemoteObject delegate) {
        SysUtils.checkNotNull("delegate", delegate);
        _delegate = delegate;
    }

    public Object invoke(String name, TypedValue... args) throws Throwable {
        try {
            return _delegate.invoke(name, args);
        } catch (Throwable th) {
            throw ((th instanceof InvocationTargetException) && (th.getCause() != null)) ? th.getCause() : th;
        }
    }
}
