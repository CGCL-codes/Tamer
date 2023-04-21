package com.liferay.portlet.shopping.service.http;

import com.liferay.portlet.shopping.service.ShoppingCouponServiceUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <a href="ShoppingCouponServiceJSON.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a JSON utility for the <code>com.liferay.portlet.shopping.service.ShoppingCouponServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for JSON to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to a <code>org.json.JSONArray</code>. If the method in the service
 * utility returns a <code>com.liferay.portlet.shopping.model.ShoppingCoupon</code>,
 * that is translated to a <code>org.json.JSONObject</code>. Methods that JSON cannot
 * safely use are skipped. The logic for the translation is encapsulated in <code>com.liferay.portlet.shopping.service.http.ShoppingCouponJSONSerializer</code>.
 * </p>
 *
 * <p>
 * This allows you to call the the backend services directly from JavaScript. See
 * <code>portal-web/docroot/html/portlet/tags_admin/unpacked.js</code> for a reference
 * of how that portlet uses the generated JavaScript in <code>portal-web/docroot/html/js/service.js</code>
 * to call the backend services directly from JavaScript.
 * </p>
 *
 * <p>
 * The JSON utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.shopping.service.ShoppingCouponServiceUtil
 * @see com.liferay.portlet.shopping.service.http.ShoppingCouponJSONSerializer
 *
 */
public class ShoppingCouponServiceJSON {

    public static JSONObject addCoupon(long plid, java.lang.String code, boolean autoCode, java.lang.String name, java.lang.String description, int startDateMonth, int startDateDay, int startDateYear, int startDateHour, int startDateMinute, int endDateMonth, int endDateDay, int endDateYear, int endDateHour, int endDateMinute, boolean neverExpire, boolean active, java.lang.String limitCategories, java.lang.String limitSkus, double minOrder, double discount, java.lang.String discountType) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException {
        com.liferay.portlet.shopping.model.ShoppingCoupon returnValue = ShoppingCouponServiceUtil.addCoupon(plid, code, autoCode, name, description, startDateMonth, startDateDay, startDateYear, startDateHour, startDateMinute, endDateMonth, endDateDay, endDateYear, endDateHour, endDateMinute, neverExpire, active, limitCategories, limitSkus, minOrder, discount, discountType);
        return ShoppingCouponJSONSerializer.toJSONObject(returnValue);
    }

    public static void deleteCoupon(long plid, long couponId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException {
        ShoppingCouponServiceUtil.deleteCoupon(plid, couponId);
    }

    public static JSONObject getCoupon(long plid, long couponId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException {
        com.liferay.portlet.shopping.model.ShoppingCoupon returnValue = ShoppingCouponServiceUtil.getCoupon(plid, couponId);
        return ShoppingCouponJSONSerializer.toJSONObject(returnValue);
    }

    public static JSONArray search(long plid, long companyId, java.lang.String code, boolean active, java.lang.String discountType, boolean andOperator, int begin, int end) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException {
        java.util.List returnValue = ShoppingCouponServiceUtil.search(plid, companyId, code, active, discountType, andOperator, begin, end);
        return ShoppingCouponJSONSerializer.toJSONArray(returnValue);
    }

    public static JSONObject updateCoupon(long plid, long couponId, java.lang.String name, java.lang.String description, int startDateMonth, int startDateDay, int startDateYear, int startDateHour, int startDateMinute, int endDateMonth, int endDateDay, int endDateYear, int endDateHour, int endDateMinute, boolean neverExpire, boolean active, java.lang.String limitCategories, java.lang.String limitSkus, double minOrder, double discount, java.lang.String discountType) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException {
        com.liferay.portlet.shopping.model.ShoppingCoupon returnValue = ShoppingCouponServiceUtil.updateCoupon(plid, couponId, name, description, startDateMonth, startDateDay, startDateYear, startDateHour, startDateMinute, endDateMonth, endDateDay, endDateYear, endDateHour, endDateMinute, neverExpire, active, limitCategories, limitSkus, minOrder, discount, discountType);
        return ShoppingCouponJSONSerializer.toJSONObject(returnValue);
    }
}
