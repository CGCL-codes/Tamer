package org.opencms.workplace.tools.accounts;

import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsOrganizationalUnit;
import org.opencms.security.CmsRole;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDefaultAction;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListItemDetails;
import org.opencms.workplace.list.CmsListItemDetailsFormatter;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.I_CmsListFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * User roles overview view.<p>
 * 
 * @author Raphael Schnuck  
 * 
 * @version $Revision: 1.5 $ 
 * 
 * @since 6.5.6 
 */
public abstract class A_CmsRolesList extends A_CmsListDialog {

    /** list action id constant. */
    public static final String LIST_ACTION_ICON = "ai";

    /** list column id constant. */
    public static final String LIST_COLUMN_DEPENDENCY = "cd";

    /** list column id constant. */
    public static final String LIST_COLUMN_GROUP_NAME = "cgn";

    /** list column id constant. */
    public static final String LIST_COLUMN_HIDDEN_NAME = "chn";

    /** list column id constant. */
    public static final String LIST_COLUMN_ICON = "ci";

    /** list column id constant. */
    public static final String LIST_COLUMN_NAME = "cn";

    /** list item detail id constant. */
    public static final String LIST_DETAIL_DESCRIPTION = "dd";

    /** list item detail id constant. */
    public static final String LIST_DETAIL_PATH = "dp";

    /** Path to the list buttons. */
    public static final String PATH_BUTTONS = "tools/accounts/buttons/";

    /** Stores the value of the request parameter for the organizational unit. */
    private String m_paramOufqn;

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     * @param listId the id of the list
     * @param listName the name of the list
     */
    protected A_CmsRolesList(CmsJspActionElement jsp, String listId, CmsMessageContainer listName) {
        super(jsp, listId, listName, LIST_COLUMN_HIDDEN_NAME, CmsListOrderEnum.ORDER_ASCENDING, null);
    }

    /**
     * Returns the right icon path for the given list item.<p>
     * 
     * @param item the list item to get the icon path for
     * 
     * @return the icon path for the given role
     */
    public abstract String getIconPath(CmsListItem item);

    /**
     * Returns the organizational unit parameter value.<p>
     * 
     * @return the organizational unit parameter value
     */
    public String getParamOufqn() {
        return m_paramOufqn;
    }

    /**
     * Sets the user organizational unit value.<p>
     * 
     * @param ouFqn the organizational unit parameter value
     */
    public void setParamOufqn(String ouFqn) {
        if (ouFqn == null) {
            ouFqn = "";
        }
        m_paramOufqn = ouFqn;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {
        List roles = getList().getAllContent();
        Iterator itRoles = roles.iterator();
        while (itRoles.hasNext()) {
            CmsListItem item = (CmsListItem) itRoles.next();
            String roleName = item.get(LIST_COLUMN_GROUP_NAME).toString();
            StringBuffer html = new StringBuffer(512);
            try {
                if (detailId.equals(LIST_DETAIL_PATH)) {
                    html.append(OpenCms.getOrgUnitManager().readOrganizationalUnit(getCms(), CmsOrganizationalUnit.getParentFqn(roleName)).getDisplayName(getLocale()));
                } else if (detailId.equals(LIST_DETAIL_DESCRIPTION)) {
                    CmsRole role = CmsRole.valueOf(getCms().readGroup(roleName));
                    html.append(role.getDescription(getCms().getRequestContext().getLocale()));
                } else {
                    continue;
                }
            } catch (Exception e) {
            }
            item.set(detailId, html.toString());
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#getListItems()
     */
    protected List getListItems() throws CmsException {
        List ret = new ArrayList();
        List roles = getRoles();
        List pRoles = new ArrayList(roles);
        Iterator itRoles = roles.iterator();
        while (itRoles.hasNext()) {
            CmsRole role = (CmsRole) itRoles.next();
            CmsListItem item = getList().newItem(role.getGroupName());
            Locale locale = getCms().getRequestContext().getLocale();
            item.set(LIST_COLUMN_NAME, role.getName(locale));
            String dependency = "";
            CmsRole parent = role;
            while ((parent.getParentRole() != null) && (parent.getParentRole().getParentRole() != null)) {
                String roleName = parent.getParentRole().getName(locale);
                if (dependency.length() > 0) {
                    roleName += ", ";
                }
                dependency = roleName + dependency;
                parent = parent.getParentRole();
            }
            String hiddenName = dependency;
            if (role.forOrgUnit(null).equals(CmsRole.WORKPLACE_USER)) {
                dependency = "";
                Iterator itWuParents = pRoles.iterator();
                while (itWuParents.hasNext()) {
                    CmsRole wuParent = (CmsRole) itWuParents.next();
                    if (wuParent.forOrgUnit(null).equals(CmsRole.WORKPLACE_USER) || (wuParent.forOrgUnit(null).equals(CmsRole.ROOT_ADMIN))) {
                        continue;
                    }
                    String roleName = wuParent.getName(locale);
                    if (dependency.length() > 0) {
                        roleName += ", ";
                    }
                    dependency = roleName + dependency;
                }
            }
            item.set(LIST_COLUMN_DEPENDENCY, dependency);
            if (hiddenName.length() > 0) {
                hiddenName = CmsRole.ROOT_ADMIN.getName(locale) + ", " + hiddenName;
            } else {
                hiddenName = CmsRole.ROOT_ADMIN.getName(locale);
            }
            if (role.getParentRole() != null) {
                hiddenName += ", " + role.getName(locale);
            }
            item.set(LIST_COLUMN_HIDDEN_NAME, hiddenName);
            item.set(LIST_COLUMN_GROUP_NAME, role.getGroupName());
            ret.add(item);
        }
        return ret;
    }

    /**
     * Returns all roles to display.<p>
     * 
     * @return a list of {@link CmsRole} objects
     * 
     * @throws CmsException if something goes wrong
     */
    protected abstract List getRoles() throws CmsException;

    /**
     * Returns if the organizational unit details button should be displayed.<p>
     * 
     * @return if the organizational unit details button should be displayed
     */
    protected boolean includeOuDetails() {
        return true;
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {
        CmsListColumnDefinition iconCol = new CmsListColumnDefinition(LIST_COLUMN_ICON);
        iconCol.setName(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_COLS_ICON_0));
        iconCol.setHelpText(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_COLS_ICON_HELP_0));
        iconCol.setWidth("1%");
        iconCol.setSorteable(false);
        CmsListDirectAction dirAction = new CmsListDefaultAction(LIST_ACTION_ICON) {

            public String getIconPath() {
                return ((A_CmsRolesList) getWp()).getIconPath(getItem());
            }
        };
        dirAction.setName(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_ICON_NAME_0));
        dirAction.setHelpText(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_ICON_HELP_0));
        dirAction.setIconPath(PATH_BUTTONS + "role.png");
        dirAction.setEnabled(false);
        iconCol.addDirectAction(dirAction);
        metadata.addColumn(iconCol);
        CmsListColumnDefinition nameCol = new CmsListColumnDefinition(LIST_COLUMN_NAME);
        nameCol.setName(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_COLS_NAME_0));
        nameCol.setWidth("40%");
        metadata.addColumn(nameCol);
        CmsListColumnDefinition depCol = new CmsListColumnDefinition(LIST_COLUMN_DEPENDENCY);
        depCol.setName(Messages.get().container(Messages.GUI_ROLEEDIT_LIST_COLS_DEPENDENCY_0));
        depCol.setWidth("60%");
        depCol.setTextWrapping(true);
        metadata.addColumn(depCol);
        CmsListColumnDefinition hideNameCol = new CmsListColumnDefinition(LIST_COLUMN_HIDDEN_NAME);
        hideNameCol.setSorteable(true);
        hideNameCol.setVisible(false);
        metadata.addColumn(hideNameCol);
        hideNameCol.setPrintable(false);
        CmsListColumnDefinition groupNameCol = new CmsListColumnDefinition(LIST_COLUMN_GROUP_NAME);
        groupNameCol.setVisible(false);
        metadata.addColumn(groupNameCol);
        groupNameCol.setPrintable(false);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {
        CmsListItemDetails descriptionDetails = new CmsListItemDetails(LIST_DETAIL_DESCRIPTION);
        descriptionDetails.setAtColumn(LIST_COLUMN_NAME);
        descriptionDetails.setVisible(true);
        descriptionDetails.setShowActionName(Messages.get().container(Messages.GUI_ROLEEDIT_DETAIL_SHOW_DESCRIPTION_NAME_0));
        descriptionDetails.setShowActionHelpText(Messages.get().container(Messages.GUI_ROLEEDIT_DETAIL_SHOW_DESCRIPTION_HELP_0));
        descriptionDetails.setHideActionName(Messages.get().container(Messages.GUI_ROLEEDIT_DETAIL_HIDE_DESCRIPTION_NAME_0));
        descriptionDetails.setHideActionHelpText(Messages.get().container(Messages.GUI_ROLEEDIT_DETAIL_HIDE_DESCRIPTION_HELP_0));
        descriptionDetails.setName(Messages.get().container(Messages.GUI_ROLEEDIT_DETAIL_DESCRIPTION_NAME_0));
        descriptionDetails.setFormatter(new I_CmsListFormatter() {

            /**
             * @see org.opencms.workplace.list.I_CmsListFormatter#format(java.lang.Object, java.util.Locale)
             */
            public String format(Object data, Locale locale) {
                StringBuffer html = new StringBuffer(512);
                html.append("<table border='0' cellspacing='0' cellpadding='0'>\n");
                html.append("\t<tr>\n");
                html.append("\t\t<td style='white-space:normal;' >\n");
                html.append("\t\t\t");
                html.append(data == null ? "" : data);
                html.append("\n");
                html.append("\t\t</td>\n");
                html.append("\t</tr>\n");
                html.append("</table>\n");
                return html.toString();
            }
        });
        metadata.addItemDetails(descriptionDetails);
        if (includeOuDetails()) {
            CmsListItemDetails pathDetails = new CmsListItemDetails(LIST_DETAIL_PATH);
            pathDetails.setAtColumn(LIST_COLUMN_NAME);
            pathDetails.setVisible(false);
            pathDetails.setShowActionName(Messages.get().container(Messages.GUI_ROLES_DETAIL_SHOW_PATH_NAME_0));
            pathDetails.setShowActionHelpText(Messages.get().container(Messages.GUI_ROLES_DETAIL_SHOW_PATH_HELP_0));
            pathDetails.setHideActionName(Messages.get().container(Messages.GUI_ROLES_DETAIL_HIDE_PATH_NAME_0));
            pathDetails.setHideActionHelpText(Messages.get().container(Messages.GUI_ROLES_DETAIL_HIDE_PATH_HELP_0));
            pathDetails.setName(Messages.get().container(Messages.GUI_ROLES_DETAIL_PATH_NAME_0));
            pathDetails.setFormatter(new CmsListItemDetailsFormatter(Messages.get().container(Messages.GUI_ROLES_DETAIL_PATH_NAME_0)));
            metadata.addItemDetails(pathDetails);
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#validateParamaters()
     */
    protected void validateParamaters() throws Exception {
        OpenCms.getRoleManager().checkRole(getCms(), CmsRole.ACCOUNT_MANAGER.forOrgUnit(getParamOufqn()));
    }
}
