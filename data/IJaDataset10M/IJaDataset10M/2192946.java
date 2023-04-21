package com.liferay.portlet.announcements.service;

/**
 * <a href="AnnouncementsDeliveryLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.announcements.service.AnnouncementsDeliveryLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.announcements.service.AnnouncementsDeliveryLocalService
 *
 */
public class AnnouncementsDeliveryLocalServiceUtil {

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery addAnnouncementsDelivery(com.liferay.portlet.announcements.model.AnnouncementsDelivery announcementsDelivery) throws com.liferay.portal.SystemException {
        return getService().addAnnouncementsDelivery(announcementsDelivery);
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery createAnnouncementsDelivery(long deliveryId) {
        return getService().createAnnouncementsDelivery(deliveryId);
    }

    public static void deleteAnnouncementsDelivery(long deliveryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteAnnouncementsDelivery(deliveryId);
    }

    public static void deleteAnnouncementsDelivery(com.liferay.portlet.announcements.model.AnnouncementsDelivery announcementsDelivery) throws com.liferay.portal.SystemException {
        getService().deleteAnnouncementsDelivery(announcementsDelivery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery getAnnouncementsDelivery(long deliveryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getAnnouncementsDelivery(deliveryId);
    }

    public static java.util.List<com.liferay.portlet.announcements.model.AnnouncementsDelivery> getAnnouncementsDeliveries(int start, int end) throws com.liferay.portal.SystemException {
        return getService().getAnnouncementsDeliveries(start, end);
    }

    public static int getAnnouncementsDeliveriesCount() throws com.liferay.portal.SystemException {
        return getService().getAnnouncementsDeliveriesCount();
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery updateAnnouncementsDelivery(com.liferay.portlet.announcements.model.AnnouncementsDelivery announcementsDelivery) throws com.liferay.portal.SystemException {
        return getService().updateAnnouncementsDelivery(announcementsDelivery);
    }

    public static void deleteDeliveries(long userId) throws com.liferay.portal.SystemException {
        getService().deleteDeliveries(userId);
    }

    public static void deleteDelivery(long deliveryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteDelivery(deliveryId);
    }

    public static void deleteDelivery(long userId, java.lang.String type) throws com.liferay.portal.SystemException {
        getService().deleteDelivery(userId, type);
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery getDelivery(long deliveryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getDelivery(deliveryId);
    }

    public static java.util.List<com.liferay.portlet.announcements.model.AnnouncementsDelivery> getUserDeliveries(long userId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getUserDeliveries(userId);
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery getUserDelivery(long userId, java.lang.String type) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getUserDelivery(userId, type);
    }

    public static com.liferay.portlet.announcements.model.AnnouncementsDelivery updateDelivery(long userId, java.lang.String type, boolean email, boolean sms, boolean website) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().updateDelivery(userId, type, email, sms, website);
    }

    public static AnnouncementsDeliveryLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("AnnouncementsDeliveryLocalService is not set");
        }
        return _service;
    }

    public void setService(AnnouncementsDeliveryLocalService service) {
        _service = service;
    }

    private static AnnouncementsDeliveryLocalService _service;
}
