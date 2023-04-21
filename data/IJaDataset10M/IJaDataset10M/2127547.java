package com.liferay.wol.service.persistence;

/**
 * <a href="WallEntryFinderUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WallEntryFinderUtil {

    public static int countByG1_G2_U1_U2(long groupId1, long groupId2, long userId1, long userId2) throws com.liferay.portal.SystemException {
        return getFinder().countByG1_G2_U1_U2(groupId1, groupId2, userId1, userId2);
    }

    public static java.util.List<com.liferay.wol.model.WallEntry> findByG1_G2_U1_U2(long groupId1, long groupId2, long userId1, long userId2, int start, int end) throws com.liferay.portal.SystemException {
        return getFinder().findByG1_G2_U1_U2(groupId1, groupId2, userId1, userId2, start, end);
    }

    public static WallEntryFinder getFinder() {
        return _finder;
    }

    public void setFinder(WallEntryFinder finder) {
        _finder = finder;
    }

    private static WallEntryFinder _finder;
}
