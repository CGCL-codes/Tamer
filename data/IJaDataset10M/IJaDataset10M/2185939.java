package com.liferay.portal.ejb;

/**
 * <a href="UserTrackerPathRemoteManagerUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.4 $
 *
 */
public class UserTrackerPathRemoteManagerUtil {

    public static java.util.List getUserTrackerPaths(java.lang.String userTrackerId, int begin, int end) throws com.liferay.portal.SystemException {
        try {
            UserTrackerPathRemoteManager userTrackerPathRemoteManager = UserTrackerPathRemoteManagerFactory.getManager();
            return userTrackerPathRemoteManager.getUserTrackerPaths(userTrackerId, begin, end);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
