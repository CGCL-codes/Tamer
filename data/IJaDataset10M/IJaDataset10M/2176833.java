package java.rmi.server;

import java.io.InvalidObjectException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.UnexpectedException;
import org.apache.harmony.rmi.common.RMIHash;
import org.apache.harmony.rmi.internal.nls.Messages;

/**
 * @com.intel.drl.spec_ref
 *
 * @author  Mikhail A. Markov
 */
public class RemoteObjectInvocationHandler extends RemoteObject implements InvocationHandler {

    private static final long serialVersionUID = 2L;

    /**
     * @com.intel.drl.spec_ref
     */
    public RemoteObjectInvocationHandler(RemoteRef ref) {
        super(ref);
        if (ref == null) {
            throw new NullPointerException(Messages.getString("rmi.20"));
        }
    }

    /**
     * @com.intel.drl.spec_ref
     */
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Class mClass = m.getDeclaringClass();
        if (m.getDeclaringClass() == Object.class) {
            return invokeObjectMethod(proxy, m, args);
        } else if (!(proxy instanceof Remote)) {
            throw new IllegalArgumentException(Messages.getString("rmi.21"));
        } else {
            return invokeRemoteMethod(proxy, m, args);
        }
    }

    /**
     * @com.intel.drl.spec_ref
     */
    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException(Messages.getString("rmi.22", this.getClass().getName()));
    }

    private Object invokeObjectMethod(Object proxy, Method m, Object[] args) {
        String mName = m.getName();
        if (mName.equals("hashCode")) {
            return new Integer(hashCode());
        } else if (mName.equals("equals")) {
            Object obj = args[0];
            return new Boolean((proxy == obj) || (obj != null && Proxy.isProxyClass(obj.getClass()) && equals(Proxy.getInvocationHandler(obj))));
        } else if (mName.equals("toString")) {
            Class[] interf = proxy.getClass().getInterfaces();
            if (interf.length == 0) {
                return "Proxy[" + toString() + "]";
            }
            String str = "Proxy[interf:[";
            for (int i = 0; i < interf.length - 1; ++i) {
                str += interf[i].getName() + ", ";
            }
            return str + interf[interf.length - 1].getName() + "], " + toString() + "]";
        } else {
            throw new IllegalArgumentException(Messages.getString("rmi.23", m));
        }
    }

    private Object invokeRemoteMethod(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            return ref.invoke((Remote) proxy, m, args, RMIHash.getMethodHash(m));
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception ex) {
            Method m1 = proxy.getClass().getMethod(m.getName(), m.getParameterTypes());
            Class[] declaredEx = m1.getExceptionTypes();
            for (int i = 0; i < declaredEx.length; ++i) {
                if (declaredEx[i].isAssignableFrom(ex.getClass())) {
                    throw ex;
                }
            }
            throw new UnexpectedException(Messages.getString("rmi.24"), ex);
        }
    }
}
