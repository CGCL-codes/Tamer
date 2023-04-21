package com.liferay.portlet.announcements.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.BooleanWrapper;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.kernel.util.MethodWrapper;
import com.liferay.portal.kernel.util.NullWrapper;
import com.liferay.portal.security.auth.HttpPrincipal;
import com.liferay.portal.service.http.TunnelUtil;
import com.liferay.portlet.announcements.service.AnnouncementsDeliveryServiceUtil;

/**
 * <a href="AnnouncementsDeliveryServiceHttp.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a HTTP utility for the
 * <code>com.liferay.portlet.announcements.service.AnnouncementsDeliveryServiceUtil</code> service
 * utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it requires an
 * additional <code>com.liferay.portal.security.auth.HttpPrincipal</code>
 * parameter.
 * </p>
 *
 * <p>
 * The benefits of using the HTTP utility is that it is fast and allows for
 * tunneling without the cost of serializing to text. The drawback is that it
 * only works with Java.
 * </p>
 *
 * <p>
 * Set the property <code>tunnel.servlet.hosts.allowed</code> in
 * portal.properties to configure security.
 * </p>
 *
 * <p>
 * The HTTP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.security.auth.HttpPrincipal
 * @see com.liferay.portlet.announcements.service.AnnouncementsDeliveryServiceUtil
 * @see com.liferay.portlet.announcements.service.http.AnnouncementsDeliveryServiceSoap
 *
 */
public class AnnouncementsDeliveryServiceHttp {

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery updateDelivery(HttpPrincipal httpPrincipal, long userId, java.lang.String type, boolean email, boolean sms, boolean website) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            Object paramObj0 = new LongWrapper(userId);
            Object paramObj1 = type;
            if (type == null) {
                paramObj1 = new NullWrapper("java.lang.String");
            }
            Object paramObj2 = new BooleanWrapper(email);
            Object paramObj3 = new BooleanWrapper(sms);
            Object paramObj4 = new BooleanWrapper(website);
            MethodWrapper methodWrapper = new MethodWrapper(AnnouncementsDeliveryServiceUtil.class.getName(), "updateDelivery", new Object[] { paramObj0, paramObj1, paramObj2, paramObj3, paramObj4 });
            Object returnObj = null;
            try {
                returnObj = TunnelUtil.invoke(httpPrincipal, methodWrapper);
            } catch (Exception e) {
                if (e instanceof com.liferay.portal.PortalException) {
                    throw (com.liferay.portal.PortalException) e;
                }
                if (e instanceof com.liferay.portal.SystemException) {
                    throw (com.liferay.portal.SystemException) e;
                }
                throw new com.liferay.portal.SystemException(e);
            }
            return (com.liferay.portlet.announcements.model.AnnouncementsDelivery) returnObj;
        } catch (com.liferay.portal.SystemException se) {
            _log.error(se, se);
            throw se;
        }
    }

    private static Log _log = LogFactoryUtil.getLog(AnnouncementsDeliveryServiceHttp.class);
}
