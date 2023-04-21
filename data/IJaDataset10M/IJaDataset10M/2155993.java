package org.adempiere.webui;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.adempiere.webui.session.ServerContext;
import net.sf.cglib.proxy.InvocationHandler;

/**
 * Intercaptor for Server context properties that delegate to the threadlocal instance
 * @author Low Heng Sin
 *
 */
public class ServerContextCallback implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServerContext context = ServerContext.getCurrentInstance();
        if (method.getName().equals("getProperty")) {
            Class<?>[] types = method.getParameterTypes();
            if (types != null && types.length == 1 && types[0] == String.class && args != null && args.length == 1 && args[0] instanceof String) {
                return context.getContext((String) args[0]);
            } else if (types != null && types.length == 2 && types[0] == String.class && types[1] == String.class && args != null && args[0] instanceof String && args[1] instanceof String) return context.getContext((String) args[0]);
        }
        Method m = context.getClass().getMethod(method.getName(), method.getParameterTypes());
        return m.invoke(context, args);
    }
}
