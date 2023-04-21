package net.sourceforge.javautil.common.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * This allows proxies to declare that they support {@link ProxyAdapter}'s.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: IAdaptableProxyHandler.java 2297 2010-06-16 00:13:14Z ponderator $
 */
public interface IAdaptableProxyHandler extends InvocationHandler {

    /**
	 * @param adapter The adapter to add to the proxy
	 */
    void adapt(ProxyAdapter adapter);
}
