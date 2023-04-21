package com.evaserver.rof.script;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Max Antoni
 * @version $Revision: 85 $
 */
public class ClientReferenceImpl implements ClientReference {

    private TWindow window;

    private TType type;

    /**
     * standard constructor.
     *
     * @param inWindow the window.
     * @param inType the type.
     */
    ClientReferenceImpl(TWindow inWindow, TType inType) {
        if (inWindow == null) {
            throw new NullPointerException("Window is null.");
        }
        if (inType == null) {
            throw new NullPointerException("Type is null.");
        }
        window = inWindow;
        type = inType;
    }

    public boolean hasProperty(String inName) {
        try {
            return window.exists(access(inName));
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public Object call(String inKey) throws RemoteException {
        return call(inKey, Expression.EMPTY_ARRAY);
    }

    public Object call(String inKey, Expression[] inArguments) throws RemoteException {
        if (inKey == null) {
            throw new NullPointerException();
        }
        if (inKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        try {
            return window.eval(new Call(access(inKey), inArguments)).toObject(window);
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public void delete(String inKey) throws RemoteException {
        if (inKey == null) {
            throw new NullPointerException();
        }
        if (inKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        try {
            window.eval(new Delete(access(inKey)));
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public ClientReference get(String inKey) throws RemoteException {
        return new ClientReferenceImpl(window, getType(inKey));
    }

    public Object toObject(String inKey) throws RemoteException {
        try {
            return getType(inKey).toObject(window);
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    private TType getType(String inKey) throws RemoteException {
        if (inKey == null) {
            throw new NullPointerException();
        }
        if (inKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        try {
            return window.getType(access(inKey));
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public void put(String inKey, Expression inValue) throws RemoteException {
        if (inKey == null || inValue == null) {
            throw new NullPointerException();
        }
        if (inKey.length() == 0) {
            throw new IllegalArgumentException();
        }
        try {
            window.eval(new Assign(access(inKey), inValue));
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public Object toObject() throws RemoteException {
        try {
            return type.toObject(window);
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public String toString() {
        return type.toString();
    }

    /**
	 * creates an access for the given string. If the key is a number, a
	 * brackets access is returned, otherwise a member access.
	 *
	 * @param inKey the key.
	 * @return the access.
	 * @throws ScriptException
	 */
    Access access(String inKey) throws ScriptException {
        return ExpressionFactory.member(getAccess(), inKey);
    }

    public void setRemote(Object inRemote) {
        if (type instanceof TObject) {
            ((TObject) type).setRemote(window, inRemote, true);
        } else {
            throw new UnsupportedOperationException("Cannot set remote object for " + type.typeof());
        }
    }

    public ClientReference newInstance(String inClassName) throws RemoteException {
        return newInstance(inClassName, Expression.EMPTY_ARRAY);
    }

    public ClientReference newInstance(String inClassName, Expression[] inArguments) throws RemoteException {
        return window.newInstance(inClassName, inArguments);
    }

    public ClientReference newInstanceWithRemote(String inClassName, Object inObject) throws RemoteException {
        return newInstanceWithRemote(inClassName, Expression.EMPTY_ARRAY, inObject);
    }

    public List newInstancesWithRemoteList(String inClassName, Expression[] inArguments, Collection inRemote) throws RemoteException {
        return window.newInstancesWithRemoteList(inClassName, inArguments, inRemote);
    }

    public ClientReference newInstanceWithRemote(String inClassName, Expression[] inArguments, Object inObject) throws RemoteException {
        return window.newInstanceWithRemote(inClassName, inArguments, inObject);
    }

    public Access getAccess() throws RemoteException {
        try {
            return window.getAccess(type);
        } catch (ScriptException e) {
            throw new RemoteException(e.toString());
        }
    }

    public Object call(String inKey, Expression inArgument) throws RemoteException {
        return call(inKey, new Expression[] { inArgument });
    }

    public Object call(String inKey, Expression inArgument1, Expression inArgument2) throws RemoteException {
        return call(inKey, new Expression[] { inArgument1, inArgument2 });
    }

    public Iterator getEnumerablePropertyNames() {
        if (type instanceof TObject) {
            return ((TObject) type).getEnumerablePropertyNames();
        }
        throw new UnsupportedOperationException("Cannot get enumerable property names for " + type.typeof());
    }

    public Window getWindow() {
        return window;
    }

    public ClientReference newMappedWithRemote(ClientReference inConstructor, String inMapping, Object inRemote) throws RemoteException {
        return window.newMappedWithRemote(inConstructor, inMapping, inRemote);
    }

    public List newInstancesWithRemoteList(String inClassName, Collection inRemote) throws RemoteException {
        return newInstancesWithRemoteList(inClassName, Expression.EMPTY_ARRAY, inRemote);
    }

    public List newMappedWithRemoteList(ClientReference inConstructor, String inMapping, Collection inRemote) throws RemoteException {
        return window.newMappedWithRemoteList(inConstructor, inMapping, inRemote);
    }
}
