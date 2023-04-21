package com.liferay.portlet.workflow.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.kernel.util.MethodWrapper;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.http.TunnelUtil;
import com.liferay.portlet.workflow.service.WorkflowInstanceServiceUtil;

/**
 * <a href="WorkflowInstanceServiceHttp.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a HTTP utility for the <code>com.liferay.portlet.workflow.service.WorkflowInstanceServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it requires an
 * additional <code>com.liferay.portal.security.auth.HttpPrincipal</code> parameter.
 * </p>
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for tunneling
 * without the cost of serializing to text. The drawback is that it only works with
 * Java.
 * </p>
 *
 * <p>
 * Set the property <code>tunnel.servlet.hosts.allowed</code> in portal.properties
 * to configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.security.auth.HttpPrincipal
 * @see com.liferay.portlet.workflow.service.WorkflowInstanceServiceUtil
 * @see com.liferay.portlet.workflow.service.http.WorkflowInstanceServiceSoap
 *
 */
public class WorkflowInstanceServiceHttp {

    public static com.liferay.portlet.workflow.model.WorkflowInstance addInstance(HttpPrincipal httpPrincipal, long definitionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        try {
            Object paramObj0 = new LongWrapper(definitionId);
            MethodWrapper methodWrapper = new MethodWrapper(WorkflowInstanceServiceUtil.class.getName(), "addInstance", new Object[] { paramObj0 });
            Object returnObj = null;
            try {
                returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
            } catch (Exception e) {
                if (e instanceof com.liferay.portal.SystemException) {
                    throw (com.liferay.portal.SystemException) e;
                }
                if (e instanceof com.liferay.portal.PortalException) {
                    throw (com.liferay.portal.PortalException) e;
                }
                throw new com.liferay.portal.SystemException(e);
            }
            return (com.liferay.portlet.workflow.model.WorkflowInstance) returnObj;
        } catch (com.liferay.portal.SystemException se) {
            _log.error(se, se);
            throw se;
        }
    }

    public static void signalInstance(HttpPrincipal httpPrincipal, long instanceId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        try {
            Object paramObj0 = new LongWrapper(instanceId);
            MethodWrapper methodWrapper = new MethodWrapper(WorkflowInstanceServiceUtil.class.getName(), "signalInstance", new Object[] { paramObj0 });
            try {
                TunnelUtil.invoke(httpPrincipal, methodWrapper);
            } catch (Exception e) {
                if (e instanceof com.liferay.portal.SystemException) {
                    throw (com.liferay.portal.SystemException) e;
                }
                if (e instanceof com.liferay.portal.PortalException) {
                    throw (com.liferay.portal.PortalException) e;
                }
                throw new com.liferay.portal.SystemException(e);
            }
        } catch (com.liferay.portal.SystemException se) {
            _log.error(se, se);
            throw se;
        }
    }

    public static void signalToken(HttpPrincipal httpPrincipal, long instanceId, long tokenId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException {
        try {
            Object paramObj0 = new LongWrapper(instanceId);
            Object paramObj1 = new LongWrapper(tokenId);
            MethodWrapper methodWrapper = new MethodWrapper(WorkflowInstanceServiceUtil.class.getName(), "signalToken", new Object[] { paramObj0, paramObj1 });
            try {
                TunnelUtil.invoke(httpPrincipal, methodWrapper);
            } catch (Exception e) {
                if (e instanceof com.liferay.portal.SystemException) {
                    throw (com.liferay.portal.SystemException) e;
                }
                if (e instanceof com.liferay.portal.PortalException) {
                    throw (com.liferay.portal.PortalException) e;
                }
                throw new com.liferay.portal.SystemException(e);
            }
        } catch (com.liferay.portal.SystemException se) {
            _log.error(se, se);
            throw se;
        }
    }

    private static Log _log = LogFactoryUtil.getLog(WorkflowInstanceServiceHttp.class);
}
