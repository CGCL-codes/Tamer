package com.liferay.portal.service.spring;

/**
 * <a href="ReleaseLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ReleaseLocalServiceUtil {

    public static com.liferay.portal.model.Release getRelease() throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            ReleaseLocalService releaseLocalService = ReleaseLocalServiceFactory.getService();
            return releaseLocalService.getRelease();
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portal.model.Release updateRelease() throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            ReleaseLocalService releaseLocalService = ReleaseLocalServiceFactory.getService();
            return releaseLocalService.updateRelease();
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
