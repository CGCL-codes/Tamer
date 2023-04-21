package cn.vlabs.duckling.vwb.ui.command;

import java.security.Permission;
import cn.vlabs.duckling.dct.services.dpage.DPage;
import cn.vlabs.duckling.vwb.services.auth.permissions.PagePermission;
import cn.vlabs.duckling.vwb.services.auth.permissions.PermissionFactory;

/**
 * Introduction Here.
 * 
 * @date Feb 25, 2010
 * @author xiejj@cnic.cn
 */
public class DPageCommand extends AbstractCommand {

    public static final DPageCommand VIEW = new DPageCommand("view", null, PagePermission.VIEW, "/PageContent.jsp", "%u/page/%n");

    public static final DPageCommand DIFF = new DPageCommand("diff", null, PagePermission.VIEW, "/InfoContent.jsp", "%u/page/%n?a=diff");

    public static final DPageCommand INFO = new DPageCommand("info", null, PagePermission.VIEW, "/InfoContent.jsp", "%u/page/%n?a=info");

    public static final DPageCommand EDIT_INFO = new DPageCommand("info", null, PagePermission.EDIT, "/InfoContent.jsp", "%u/page/%n?a=info");

    public static final DPageCommand SHARE = new DPageCommand("share", null, PagePermission.EDIT, "/InfoContent.jsp", "%u/page/%n?a=share");

    public static final DPageCommand PREVIEW = new DPageCommand("preview", null, PagePermission.EDIT, "/PortletContent.jsp", "%u/page/%n?a=preview");

    public static final DPageCommand CONFLICT = new DPageCommand("conflict", null, PagePermission.EDIT, "/ConflictCotnent.jsp", "%u/page/%n?a=conflict");

    public static final DPageCommand EDIT = new DPageCommand("edit", null, PagePermission.EDIT, "EditContent.jsp", "%u/page/%n?a=edit");

    public static final DPageCommand DELETE = new DPageCommand("delete", null, PagePermission.DELETE, "InfoContent.jsp", "%u/page/%n?a=del");

    public DPageCommand(String action, DPage target, Permission permission, String contentJsp, String urlPattern) {
        super(action, target, permission, contentJsp, urlPattern);
    }

    public Command targetCommand(Object target) {
        Permission permission;
        if (getRequiredPermission() != null) {
            DPage page = (DPage) target;
            permission = PermissionFactory.getPagePermission(page, getRequiredPermission().getActions());
        } else {
            permission = null;
        }
        return new DPageCommand(getAction(), (DPage) target, permission, getContentJSP(), getURLPattern());
    }
}
