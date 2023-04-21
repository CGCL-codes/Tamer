package org.teaframework.container;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.teaframework.util.resource.ResourceHelper;

/**
 * MBeanServerInterceptor intercept manageable service request and regist
 * service instance to a MBeanServer and ServiceContainer.
 * 
 * @author <a href="mailto:founder_chen@yahoo.com.cn">Peter Cheng </a>
 * @version $Revision: 1.15 $ $Date: 2005/05/22 06:46:49 $
 * @version Revision: 1.0
 */
public class MBeanServerInterceptor implements ServiceInterceptor {

    private final Log logger = LogFactory.getLog(getClass());

    private ManageableServiceContainer manageableServiceContainer = ManageableServiceContainer.getInstance();

    /**
	 * Interceptor invoke.
	 * 
	 * @param serviceEntry
	 * @throws Throwable
	 */
    public void invoke(ServiceEntry serviceEntry) throws Throwable {
        if (BooleanUtils.toBoolean(serviceEntry.getManageable())) {
            logger.info("Register Managable Service : " + serviceEntry.getImplementation());
            Object object = ResourceHelper.instantiate(StringUtils.trim(serviceEntry.getImplementation()));
            synchronized (manageableServiceContainer) {
                manageableServiceContainer.registManagebleService(object, serviceEntry);
            }
        }
    }
}
