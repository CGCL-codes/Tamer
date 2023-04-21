package com.liferay.portlet.wiki.service.permission;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;

/**
 * <a href="WikiPagePermission.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WikiPagePermission {

    public static void check(PermissionChecker permissionChecker, long nodeId, String title, String actionId) throws PortalException, SystemException {
        if (!contains(permissionChecker, nodeId, title, actionId)) {
            throw new PrincipalException();
        }
    }

    public static void check(PermissionChecker permissionChecker, WikiPage page, String actionId) throws PortalException, SystemException {
        if (!contains(permissionChecker, page, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, long nodeId, String title, String actionId) throws PortalException, SystemException {
        WikiPage page = WikiPageLocalServiceUtil.getPage(nodeId, title);
        return contains(permissionChecker, page, actionId);
    }

    public static boolean contains(PermissionChecker permissionChecker, WikiPage page, String actionId) throws PortalException, SystemException {
        WikiNode node = page.getNode();
        return permissionChecker.hasPermission(node.getGroupId(), WikiPage.class.getName(), page.getResourcePrimKey(), actionId);
    }
}
