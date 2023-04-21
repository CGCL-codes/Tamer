package cz.zcu.kiv.permission_report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.opencms.file.CmsGroup;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.CmsUser;
import org.opencms.file.CmsVfsResourceNotFoundException;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsAccessControlEntry;
import org.opencms.security.CmsAccessControlList;
import org.opencms.security.CmsPermissionSet;
import org.opencms.security.CmsPermissionSetCustom;
import org.opencms.security.CmsPrincipal;
import org.opencms.security.CmsRole;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWorkplaceSettings;

/**
 * Web KIV modul <strong>cz.zcu.kiv.permission_report</strong>
 *
 * Copyright (c) 2007-2009 Department of Computer Science,
 * University of West Bohemia, Pilsen, CZ
 *
 * This software and this file is available under the Creative Commons
 * Attribution-Noncommercial-Share Alike license.  You may obtain a copy
 * of the License at   http://creativecommons.org/licenses/   .
 *
 * This software is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License
 * for the specific language governing permissions and limitations.
 * 
 * @author Stanislav Skalicky
 *
 */
public class PermissionReportDialog extends CmsDialog {

    /** The dialog type. */
    public static final String DIALOG_TYPE = "permission-report";

    /** Request parameter value for the action: reload. */
    public static final String DIALOG_RELOAD = "reload";

    /** Value for the action: reload. */
    public static final int ACTION_RELOAD = 101;

    private boolean sum = false;

    private boolean own = false;

    private boolean folderin = false;

    private boolean groupin = false;

    private boolean all = false;

    public PermissionReportDialog(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
	 * Public constructor with JSP variables.
	 * <p>
	 * 
	 * @param context the JSP page context
	 * @param req the JSP request
	 * @param res the JSP response
	 */
    public PermissionReportDialog(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        super(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {
        fillParamValues(request);
        setParamDialogtype(DIALOG_TYPE);
        if (DIALOG_CONFIRMED.equals(getParamAction())) {
            setAction(ACTION_CONFIRMED);
        } else if (DIALOG_RELOAD.equals(getParamAction())) {
            setAction(ACTION_RELOAD);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            setAction(ACTION_CANCEL);
        } else {
            setAction(ACTION_DEFAULT);
            setParamTitle(key(Messages.DIALOG_TITLE));
        }
    }

    /**
     * Generates HTML for report dialog
     * @param resourceName report root folder resource name
     * @param principalName user/group for who the report will be generated
     * @param own access entry defined directly on the chosen resource with the chosen principal flag
     * @param folderin folder inherited access entries flag
     * @param groupin group inherited access entries flag
     * @param all permissions for all other principals flag
     * @return HTML string of the report
     */
    @SuppressWarnings("unchecked")
    public String getReportHtml(String resourceName, String principalName, boolean sum, boolean own, boolean folderin, boolean groupin, boolean all) {
        StringBuffer result = new StringBuffer("");
        String storedSite = this.getCms().getRequestContext().getSiteRoot();
        List<CmsResource> folders;
        List<CmsResource> files;
        try {
            setReportParams(sum, own, folderin, groupin, all);
            this.getCms().getRequestContext().setSiteRoot("");
            folders = this.getJsp().getCmsObject().getSubFolders(resourceName, CmsResourceFilter.ALL);
            files = this.getJsp().getCmsObject().getFilesInFolder(resourceName, CmsResourceFilter.ALL);
            result.append("<ul id=\"permission-report\" class=\"filetree\">");
            for (CmsResource folder : folders) {
                result.append("<li><div class=\"folder\">" + folder.getName());
                result.append(getPermissionString(folder, findPrincipal(principalName)));
                result.append("</div>");
                result.append(getResourceSubTreeHtml(folder.getRootPath(), findPrincipal(principalName)));
                result.append("</li>");
            }
            for (CmsResource file : files) {
                result.append("<li><div class=\"file\">" + file.getName());
                result.append(getPermissionString(file, findPrincipal(principalName)));
                result.append("</div></li>");
            }
            result.append("</ul>");
        } catch (CmsVfsResourceNotFoundException e) {
            result = new StringBuffer(key(Messages.ERR_RESOURCE_NOTFOUND));
        } catch (NullPointerException e) {
            result = new StringBuffer(e.getMessage());
        } catch (CmsException e) {
            e.printStackTrace();
        } finally {
            this.getCms().getRequestContext().setSiteRoot(storedSite);
        }
        return result.toString();
    }

    /**
     * Generates HTML of the given resourceName subtree with it's permissions.
     * This method browses the VFS tree recursively.
     * @param resourceName files and folders of this resource will be added to the report
     * @param principal user/group for who the report will be generated
     * @return HMTL string of the subtree report
     */
    @SuppressWarnings("unchecked")
    private String getResourceSubTreeHtml(String resourceName, CmsPrincipal principal) {
        StringBuffer result = new StringBuffer("");
        List<CmsResource> folders;
        List<CmsResource> files;
        try {
            folders = this.getJsp().getCmsObject().getSubFolders(resourceName, CmsResourceFilter.ALL);
            files = this.getJsp().getCmsObject().getFilesInFolder(resourceName, CmsResourceFilter.ALL);
            result.append("\n");
            result.append("<ul>");
            result.append("\n");
            for (CmsResource folder : folders) {
                result.append("<li><div class=\"folder\">" + folder.getName());
                result.append(getPermissionString(folder, principal));
                result.append("</div>");
                result.append(getResourceSubTreeHtml(folder.getRootPath(), principal));
                result.append("</li>");
            }
            for (CmsResource file : files) {
                result.append("<li><div class=\"file\">" + file.getName());
                result.append(getPermissionString(file, principal));
                result.append("</div></li>");
            }
            result.append("\n");
            result.append("</ul>");
            result.append("\n");
        } catch (CmsException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * generates string of the given principal permissions on the given resource
     * @param resource 
     * @param principal
     * @return Permissions HTML string
     */
    @SuppressWarnings("unchecked")
    private String getPermissionString(CmsResource resource, CmsPrincipal principal) {
        StringBuffer result = new StringBuffer("");
        result.append("<span class=\"permissions\">");
        CmsAccessControlList accessControlList;
        CmsAccessControlList accessControlInheritedList;
        try {
            List<CmsAccessControlEntry> entryList = new ArrayList<CmsAccessControlEntry>();
            CmsPermissionSet inheritedPermissionSet;
            CmsPermissionSet allOthersSet;
            CmsPermissionSetCustom customPermissionSet;
            List<CmsGroup> userGroups = new ArrayList<CmsGroup>();
            List<CmsRole> userRoles = new ArrayList<CmsRole>();
            CmsUser user = null;
            accessControlList = this.getJsp().getCmsObject().getAccessControlList(resource.getRootPath(), false);
            if (principal.isUser()) {
                user = (CmsUser) principal;
                userGroups = this.getCms().getGroupsOfUser(principal.getName(), false, true);
                userRoles = OpenCms.getRoleManager().getRolesOfUser(this.getCms(), principal.getName(), principal.getOuFqn(), true, false, true);
                if (this.sum) {
                    if ((customPermissionSet = accessControlList.getPermissions(user, userGroups, userRoles)) != null) {
                        result.append(" | <strong>SUM</strong> -> " + customPermissionSet.getPermissionString());
                    }
                }
            } else if (principal.isGroup()) {
                CmsGroup parentGroup = this.getCms().getParent(principal.getName());
                if (parentGroup != null) {
                    userGroups.add(parentGroup);
                }
                if (this.sum) {
                    if ((customPermissionSet = accessControlList.getPermissions(principal.getId())) != null) {
                        result.append(" | <strong>SUM</strong> -> " + customPermissionSet.getPermissionString());
                    }
                }
            }
            if (this.own) {
                entryList = this.getCms().getAccessControlEntries(resource.getRootPath(), false);
                for (CmsAccessControlEntry entry : entryList) {
                    if (!entry.isInherited() && entry.getPrincipal().equals(principal.getId())) {
                        result.append("| <strong>OWN</strong> > " + entry.getPermissions().getPermissionString());
                    }
                }
            }
            accessControlInheritedList = this.getJsp().getCmsObject().getAccessControlList(resource.getRootPath(), true);
            if (folderin) {
                inheritedPermissionSet = accessControlInheritedList.getPermissions(principal.getId());
                if (inheritedPermissionSet != null) {
                    result.append(" | <strong>FOL</strong> > " + inheritedPermissionSet.getPermissionString());
                }
            }
            if (groupin) {
                Iterator i = userGroups.iterator();
                while (i.hasNext()) {
                    CmsGroup group = (CmsGroup) i.next();
                    if ((customPermissionSet = accessControlList.getPermissions(group.getId())) != null) {
                        result.append(" | <strong>" + group.getName() + "</strong> -> " + customPermissionSet.getPermissionString());
                    }
                }
            }
            allOthersSet = accessControlList.getPermissions(CmsAccessControlEntry.PRINCIPAL_ALL_OTHERS_ID);
            if (all) {
                if (allOthersSet != null) {
                    int flags = CmsAccessControlEntry.ACCESS_FLAGS_ALLOTHERS;
                    CmsAccessControlEntry allOthersEntry = new CmsAccessControlEntry(resource.getResourceId(), CmsAccessControlEntry.PRINCIPAL_ALL_OTHERS_ID, allOthersSet, flags);
                    result.append(" | <strong>ALL</strong> > " + allOthersEntry.getPermissions().getPermissionString());
                }
            }
        } catch (CmsException e) {
            e.printStackTrace();
        }
        result.append("</span>");
        return result.toString();
    }

    /**
     * finds CmsPrincipal in OpenCms
     * @param principalName simple principal name or principal name with prefix
     * @return found principal or throws NullPointerException
     */
    private CmsPrincipal findPrincipal(String principalName) {
        CmsPrincipal result = null;
        if (principalName.startsWith(CmsPrincipal.PRINCIPAL_GROUP)) {
            principalName = principalName.substring(CmsPrincipal.PRINCIPAL_GROUP.length() + 1, principalName.length());
        } else if (principalName.startsWith(CmsPrincipal.PRINCIPAL_USER)) {
            principalName = principalName.substring(CmsPrincipal.PRINCIPAL_USER.length() + 1, principalName.length());
        }
        result = (CmsPrincipal) this.getCms().lookupPrincipal(principalName);
        if (result == null) {
            throw new NullPointerException(key(Messages.ERR_PRINCIPAL_NOTFOUND));
        }
        return result;
    }

    /**
     * sets parameter flags for report dialog
     * @param own access entry defined directly on the chosen resource with the chosen principal flag
     * @param folderin folder inherited access entries flag
     * @param groupin group inherited access entries flag
     * @param all permissions for all other principals flag
     */
    private void setReportParams(boolean sum, boolean own, boolean folderin, boolean groupin, boolean all) {
        if (!sum && !own && !folderin && !groupin && !all) {
            throw new NullPointerException(key(Messages.ERR_PERMISSION_NONE));
        }
        this.sum = sum;
        this.own = own;
        this.folderin = folderin;
        this.groupin = groupin;
        this.all = all;
    }
}
