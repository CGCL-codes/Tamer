package org.nexopenframework.util;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Utility class to convert aliased invokers to its full class names</p>
 * <p>Added as a response to NXFWK-55 request </p>
 * 
 * @author Marc Baiges Camprub�
 * @version 1.0
 * @since 1.0
 */
public class InvokersUtils {

    public static String POJO_INVOKER = "pojo";

    public static String EJB_LOCAL_INVOKER = "EJBLocal";

    public static String EJB_REMOTE_INVOKER = "EJBRemote";

    /**
	 * Method to decode an alias into full class name
	 * @param invokerClassString
	 * @return
	 */
    public static String decodeInvokerString(String invokerClassString) {
        if (invokerClassString.equals(POJO_INVOKER)) {
            return "org.nexopenframework.core.runtime.invokers.PojoInvoker";
        } else if (invokerClassString.equals(EJB_LOCAL_INVOKER)) {
            return "org.nexopenframework.core.runtime.invokers.EJBLocalInvoker";
        } else if (invokerClassString.equals(EJB_REMOTE_INVOKER)) {
            return "org.nexopenframework.core.runtime.invokers.EJBRemoteInvoker";
        }
        return invokerClassString;
    }
}
