package com.liferay.portlet.messageboards.service;

/**
 * <a href="MBDiscussionLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be
 * overwritten the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the
 * <code>com.liferay.portlet.messageboards.service.MBDiscussionLocalService</code>
 * bean. The static methods of this class calls the same methods of the bean
 * instance. It's convenient to be able to just write one line to call a method
 * on a bean instead of writing a lookup call and a method call.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portlet.messageboards.service.MBDiscussionLocalService
 *
 */
public class MBDiscussionLocalServiceUtil {

    public static com.liferay.portlet.messageboards.model.MBDiscussion addMBDiscussion(com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion) throws com.liferay.portal.SystemException {
        return getService().addMBDiscussion(mbDiscussion);
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion createMBDiscussion(long discussionId) {
        return getService().createMBDiscussion(discussionId);
    }

    public static void deleteMBDiscussion(long discussionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        getService().deleteMBDiscussion(discussionId);
    }

    public static void deleteMBDiscussion(com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion) throws com.liferay.portal.SystemException {
        getService().deleteMBDiscussion(mbDiscussion);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery);
    }

    public static java.util.List<Object> dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.SystemException {
        return getService().dynamicQuery(dynamicQuery, start, end);
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion getMBDiscussion(long discussionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getMBDiscussion(discussionId);
    }

    public static java.util.List<com.liferay.portlet.messageboards.model.MBDiscussion> getMBDiscussions(int start, int end) throws com.liferay.portal.SystemException {
        return getService().getMBDiscussions(start, end);
    }

    public static int getMBDiscussionsCount() throws com.liferay.portal.SystemException {
        return getService().getMBDiscussionsCount();
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion updateMBDiscussion(com.liferay.portlet.messageboards.model.MBDiscussion mbDiscussion) throws com.liferay.portal.SystemException {
        return getService().updateMBDiscussion(mbDiscussion);
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion getDiscussion(long discussionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getDiscussion(discussionId);
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion getDiscussion(java.lang.String className, long classPK) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getDiscussion(className, classPK);
    }

    public static com.liferay.portlet.messageboards.model.MBDiscussion getThreadDiscussion(long threadId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return getService().getThreadDiscussion(threadId);
    }

    public static MBDiscussionLocalService getService() {
        if (_service == null) {
            throw new RuntimeException("MBDiscussionLocalService is not set");
        }
        return _service;
    }

    public void setService(MBDiscussionLocalService service) {
        _service = service;
    }

    private static MBDiscussionLocalService _service;
}
