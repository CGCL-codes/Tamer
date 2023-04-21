package com.liferay.portlet.messageboards.service;

/**
 * <a href="MBMessageFlagLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.messageboards.service.MBMessageFlagLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.messageboards.service.MBMessageFlagLocalService
 *
 */
public class MBMessageFlagLocalServiceUtil {

    private static MBMessageFlagLocalService _service;

    public static com.liferay.portlet.messageboards.model.MBMessageFlag addMBMessageFlag(com.liferay.portlet.messageboards.model.MBMessageFlag mbMessageFlag) throws com.liferay.portal.SystemException {
        return _service.addMBMessageFlag(mbMessageFlag);
    }

    public static void deleteMBMessageFlag(long messageFlagId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        _service.deleteMBMessageFlag(messageFlagId);
    }

    public static void deleteMBMessageFlag(com.liferay.portlet.messageboards.model.MBMessageFlag mbMessageFlag) throws com.liferay.portal.SystemException {
        _service.deleteMBMessageFlag(mbMessageFlag);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return _service.dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return _service.dynamicQuery(dynamicQuery, start, end);
    }

    public static com.liferay.portlet.messageboards.model.MBMessageFlag getMBMessageFlag(long messageFlagId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return _service.getMBMessageFlag(messageFlagId);
    }

    public static java.util.List<com.liferay.portlet.messageboards.model.MBMessageFlag> getMBMessageFlags(int start, int end) throws com.liferay.portal.SystemException {
        return _service.getMBMessageFlags(start, end);
    }

    public static int getMBMessageFlagsCount() throws com.liferay.portal.SystemException {
        return _service.getMBMessageFlagsCount();
    }

    public static com.liferay.portlet.messageboards.model.MBMessageFlag updateMBMessageFlag(com.liferay.portlet.messageboards.model.MBMessageFlag mbMessageFlag) throws com.liferay.portal.SystemException {
        return _service.updateMBMessageFlag(mbMessageFlag);
    }

    public static void addReadFlags(long userId, java.util.List<com.liferay.portlet.messageboards.model.MBMessage> messages) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        _service.addReadFlags(userId, messages);
    }

    public static void deleteFlags(long userId) throws com.liferay.portal.SystemException {
        _service.deleteFlags(userId);
    }

    public static boolean hasReadFlag(long userId, long messageId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return _service.hasReadFlag(userId, messageId);
    }

    public static MBMessageFlagLocalService getService() {
        return _service;
    }

    public void setService(MBMessageFlagLocalService service) {
        _service = service;
    }
}
