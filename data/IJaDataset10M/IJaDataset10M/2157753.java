package org.kablink.teaming.portal.liferay;

import java.lang.reflect.Method;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

public class LiferayProxyClient {

    private static final String LIFERAY_PROXY_SERVER_CLASS_NAME = "org.kablink.teaming.liferay.proxy.LiferayProxyServer";

    private static final String LIFERAY_PROXY_SERVER_INVOKE_METHOD_NAME = "invoke";

    private static Method proxyServerInvokeMethod;

    private static Object proxyServer;

    static {
        try {
            Class classObj = Class.forName(LIFERAY_PROXY_SERVER_CLASS_NAME, true, PortalClassLoaderUtil.getClassLoader());
            proxyServerInvokeMethod = classObj.getMethod(LIFERAY_PROXY_SERVER_INVOKE_METHOD_NAME, new Class[] { String.class, String.class, String.class, String.class, Class[].class, Object[].class });
            proxyServer = classObj.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Invoke a Liferay service.
	 * <p>
	 * This mechanism allows an invocation to be <i>initiated</i> from ICEcore
	 * side, yet <i>executed</i> within the environment of Liferay context. 
	 * 
	 * @param contextCompanyWebId Web ID of the company whose context the 
	 * service is to be invoked in
	 * @param contextUserName screen name of the portal user in whose context
	 * the service is to be invoked
	 * @param className class name of the Liferay service
	 * @param methodName method name of the Liferay service
	 * @param methodArgTypes method argument types
	 * @param methodArgs method arguments to the invocation
	 * @throws Exception
	 */
    public static Object invoke(String contextCompanyWebId, String contextUserName, String className, String methodName, Class[] methodArgTypes, Object[] methodArgs) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader portalClassLoader = PortalClassLoaderUtil.getClassLoader();
        Thread.currentThread().setContextClassLoader(portalClassLoader);
        try {
            return proxyServerInvokeMethod.invoke(proxyServer, new Object[] { contextCompanyWebId, contextUserName, className, methodName, methodArgTypes, methodArgs });
        } finally {
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
}
