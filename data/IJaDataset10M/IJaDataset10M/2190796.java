package org.opencms.workplace.demos.list;

import org.opencms.file.CmsGroup;
import org.opencms.file.CmsUser;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsRuntimeException;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsUUID;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDateMacroFormatter;
import org.opencms.workplace.list.CmsListDefaultAction;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListIndependentAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListItemActionIconComparator;
import org.opencms.workplace.list.CmsListItemDetails;
import org.opencms.workplace.list.CmsListItemDetailsFormatter;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListMultiAction;
import org.opencms.workplace.list.CmsListSearchAction;
import org.opencms.workplace.tools.accounts.Messages;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * User accounts list demo.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision$ 
 * 
 * @since 6.0.0 
 */
public class CmsListDemo15b extends A_CmsListDialog {

    /** list action id constant. */
    public static final String LIST_ACTION_ACTIVATE = "aa";

    /** list action id constant. */
    public static final String LIST_ACTION_DEACTIVATE = "ac";

    /** list action id constant. */
    public static final String LIST_ACTION_DELETE = "ad";

    /** list column id constant. */
    public static final String LIST_COLUMN_ACTIVATE = "ca";

    /** list column id constant. */
    public static final String LIST_COLUMN_DELETE = "cd";

    /** list column id constant. */
    public static final String LIST_COLUMN_EMAIL = "cm";

    /** list column id constant. */
    public static final String LIST_COLUMN_LASTLOGIN = "cl";

    /** list column id constant. */
    public static final String LIST_COLUMN_LOGIN = "ci";

    /** list column id constant. */
    public static final String LIST_COLUMN_NAME = "cn";

    /** list action id constant. */
    public static final String LIST_DEFACTION_EDIT = "de";

    /** list item detail id constant. */
    public static final String LIST_DETAIL_GROUPS = "dg";

    /** list independent action id constant. */
    public static final String LIST_IACTION_REFRESH = "ir";

    /** list id constant. */
    public static final String LIST_ID = "lsu2";

    /** list action id constant. */
    public static final String LIST_MACTION_ACTIVATE = "ma";

    /** parameter name for the user id. */
    private static final String PARAM_USERID = "userid";

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsListDemo15b(CmsJspActionElement jsp) {
        super(jsp, LIST_ID, Messages.get().container(Messages.GUI_USERS_LIST_NAME_0), null, null, null);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsListDemo15b(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#defaultActionHtmlStart()
     */
    public String defaultActionHtmlStart() {
        StringBuffer result = new StringBuffer(2048);
        result.append(dialogContentStart(getParamTitle()));
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListIndepActions()
     */
    public void executeListIndepActions() {
        if (getParamListAction().equals(LIST_IACTION_REFRESH)) {
            refreshList();
        }
        super.executeListIndepActions();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() throws CmsRuntimeException {
        if (getParamListAction().equals(LIST_MACTION_ACTIVATE)) {
            try {
                Iterator itItems = getSelectedItems().iterator();
                while (itItems.hasNext()) {
                    CmsListItem listItem = (CmsListItem) itItems.next();
                    String usrName = listItem.get(LIST_COLUMN_LOGIN).toString();
                    CmsUser user = getCms().readUser(usrName);
                    if (!user.isEnabled()) {
                        user.setEnabled(true);
                    }
                }
            } catch (CmsException e) {
                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_ACTIVATE_SELECTED_USERS_0), e);
            }
        } else {
            throwListUnsupportedActionException();
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() {
        CmsListItem item = getSelectedItem();
        CmsUUID userId = new CmsUUID(item.getId());
        String userName = item.get(LIST_COLUMN_LOGIN).toString();
        Map params = new HashMap();
        params.put(PARAM_USERID, userId);
        params.put(CmsDialog.PARAM_ACTION, CmsDialog.DIALOG_INITIAL);
        if (getParamListAction().equals(LIST_DEFACTION_EDIT)) {
        } else if (LIST_ACTION_DELETE.equals(getParamListAction())) {
        } else if (getParamListAction().equals(LIST_ACTION_ACTIVATE)) {
            try {
                CmsUser user = getCms().readUser(userId);
                user.setEnabled(true);
            } catch (CmsException e) {
                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_ACTIVATE_USER_1, userName), e);
            }
        } else if (getParamListAction().equals(LIST_ACTION_DEACTIVATE)) {
            try {
                CmsUser user = getCms().readUser(userId);
                user.setEnabled(false);
            } catch (CmsException e) {
                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_DEACTIVATE_USER_1, userName), e);
            }
        } else {
            throwListUnsupportedActionException();
        }
        listSave();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {
        List users = getList().getAllContent();
        Iterator itUsers = users.iterator();
        while (itUsers.hasNext()) {
            CmsListItem item = (CmsListItem) itUsers.next();
            String userName = item.get(LIST_COLUMN_LOGIN).toString();
            StringBuffer html = new StringBuffer(512);
            try {
                if (detailId.equals(LIST_DETAIL_GROUPS)) {
                    Iterator itGroups = getCms().getGroupsOfUser(userName, false).iterator();
                    while (itGroups.hasNext()) {
                        html.append(((CmsGroup) itGroups.next()).getName());
                        if (itGroups.hasNext()) {
                            html.append("<br>");
                        }
                        html.append("\n");
                    }
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
        List users = OpenCms.getOrgUnitManager().getUsers(getCms(), "/", true);
        Iterator itUsers = users.iterator();
        while (itUsers.hasNext()) {
            CmsUser user = (CmsUser) itUsers.next();
            CmsListItem item = getList().newItem(user.getId().toString());
            item.set(LIST_COLUMN_LOGIN, user.getName());
            item.set(LIST_COLUMN_NAME, user.getFullName());
            item.set(LIST_COLUMN_EMAIL, user.getEmail());
            item.set(LIST_COLUMN_LASTLOGIN, new Date(user.getLastlogin()));
            ret.add(item);
        }
        return ret;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {
        addMessages(Messages.get().getBundleName());
        addMessages(org.opencms.workplace.demos.list.Messages.get().getBundleName());
        addMessages(org.opencms.workplace.demos.Messages.get().getBundleName());
        super.initMessages();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {
        CmsListColumnDefinition actCol = new CmsListColumnDefinition(LIST_COLUMN_ACTIVATE);
        actCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_ACTIVATE_0));
        actCol.setWidth("30");
        actCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        actCol.setListItemComparator(new CmsListItemActionIconComparator());
        CmsListDirectAction actAction = new CmsListDirectAction(LIST_ACTION_ACTIVATE) {

            public boolean isVisible() {
                String usrId = getItem().getId();
                try {
                    return !getCms().readUser(new CmsUUID(usrId)).isEnabled();
                } catch (CmsException e) {
                    return super.isVisible();
                }
            }
        };
        actAction.setName(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_ACTIVATE_NAME_0));
        actAction.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_ACTIVATE_HELP_0));
        actAction.setConfirmationMessage(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_ACTIVATE_CONF_0));
        actAction.setIconPath(ICON_INACTIVE);
        actCol.addDirectAction(actAction);
        CmsListDirectAction deactAction = new CmsListDirectAction(LIST_ACTION_DEACTIVATE) {

            public boolean isVisible() {
                String usrId = getItem().getId();
                try {
                    return getCms().readUser(new CmsUUID(usrId)).isEnabled();
                } catch (CmsException e) {
                    return super.isVisible();
                }
            }
        };
        deactAction.setName(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_DEACTIVATE_NAME_0));
        deactAction.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_DEACTIVATE_HELP_0));
        deactAction.setConfirmationMessage(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_DEACTIVATE_CONF_0));
        deactAction.setIconPath(ICON_ACTIVE);
        actCol.addDirectAction(deactAction);
        metadata.addColumn(actCol);
        CmsListColumnDefinition deleteCol = new CmsListColumnDefinition(LIST_COLUMN_DELETE);
        deleteCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_DELETE_0));
        deleteCol.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_COLS_DELETE_HELP_0));
        deleteCol.setWidth("20");
        deleteCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        deleteCol.setSorteable(false);
        CmsListDirectAction deleteAction = new CmsListDirectAction(LIST_ACTION_DELETE);
        deleteAction.setName(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_DELETE_NAME_0));
        deleteAction.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_ACTION_DELETE_HELP_0));
        deleteAction.setConfirmationMessage(new CmsMessageContainer(null, "Do you want to delete the selected user?"));
        deleteAction.setIconPath(ICON_DELETE);
        deleteCol.addDirectAction(deleteAction);
        metadata.addColumn(deleteCol);
        CmsListColumnDefinition loginCol = new CmsListColumnDefinition(LIST_COLUMN_LOGIN);
        loginCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_LOGIN_0));
        loginCol.setWidth("20%");
        loginCol.setSorteable(true);
        CmsListDefaultAction defEditAction = new CmsListDefaultAction(LIST_DEFACTION_EDIT);
        defEditAction.setName(Messages.get().container(Messages.GUI_USERS_LIST_DEFACTION_EDIT_NAME_0));
        defEditAction.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_DEFACTION_EDIT_HELP_0));
        loginCol.addDefaultAction(defEditAction);
        metadata.addColumn(loginCol);
        CmsListColumnDefinition nameCol = new CmsListColumnDefinition(LIST_COLUMN_NAME);
        nameCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_USERNAME_0));
        nameCol.setWidth("30%");
        nameCol.setSorteable(true);
        metadata.addColumn(nameCol);
        CmsListColumnDefinition emailCol = new CmsListColumnDefinition(LIST_COLUMN_EMAIL);
        emailCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_EMAIL_0));
        emailCol.setWidth("30%");
        emailCol.setSorteable(true);
        metadata.addColumn(emailCol);
        CmsListColumnDefinition lastLoginCol = new CmsListColumnDefinition(LIST_COLUMN_LASTLOGIN);
        lastLoginCol.setName(Messages.get().container(Messages.GUI_USERS_LIST_COLS_LASTLOGIN_0));
        lastLoginCol.setWidth("20%");
        lastLoginCol.setSorteable(true);
        lastLoginCol.setFormatter(CmsListDateMacroFormatter.getDefaultDateFormatter());
        metadata.addColumn(lastLoginCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {
        CmsListItemDetails userGroupsDetails = new CmsListItemDetails(LIST_DETAIL_GROUPS);
        userGroupsDetails.setAtColumn(LIST_COLUMN_LOGIN);
        userGroupsDetails.setVisible(false);
        userGroupsDetails.setShowActionName(Messages.get().container(Messages.GUI_USERS_DETAIL_SHOW_GROUPS_NAME_0));
        userGroupsDetails.setShowActionHelpText(Messages.get().container(Messages.GUI_USERS_DETAIL_SHOW_GROUPS_HELP_0));
        userGroupsDetails.setHideActionName(Messages.get().container(Messages.GUI_USERS_DETAIL_HIDE_GROUPS_NAME_0));
        userGroupsDetails.setHideActionHelpText(Messages.get().container(Messages.GUI_USERS_DETAIL_HIDE_GROUPS_HELP_0));
        userGroupsDetails.setName(Messages.get().container(Messages.GUI_USERS_DETAIL_GROUPS_NAME_0));
        userGroupsDetails.setFormatter(new CmsListItemDetailsFormatter(Messages.get().container(Messages.GUI_USERS_DETAIL_GROUPS_NAME_0)));
        metadata.addItemDetails(userGroupsDetails);
        CmsListIndependentAction refreshAction = new CmsListIndependentAction(LIST_IACTION_REFRESH);
        refreshAction.setName(new CmsMessageContainer(null, "Refresh"));
        refreshAction.setHelpText(new CmsMessageContainer(null, "Click here to refresh the list"));
        refreshAction.setIconPath(ICON_MULTI_ADD);
        metadata.addIndependentAction(refreshAction);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {
        CmsListMultiAction activateUser = new CmsListMultiAction(LIST_MACTION_ACTIVATE);
        activateUser.setName(Messages.get().container(Messages.GUI_USERS_LIST_MACTION_ACTIVATE_NAME_0));
        activateUser.setHelpText(Messages.get().container(Messages.GUI_USERS_LIST_MACTION_ACTIVATE_HELP_0));
        activateUser.setConfirmationMessage(Messages.get().container(Messages.GUI_USERS_LIST_MACTION_ACTIVATE_CONF_0));
        activateUser.setIconPath(ICON_MULTI_ACTIVATE);
        metadata.addMultiAction(activateUser);
        CmsListSearchAction searchAction = new CmsListSearchAction(metadata.getColumnDefinition(LIST_COLUMN_LOGIN));
        searchAction.addColumn(metadata.getColumnDefinition(LIST_COLUMN_EMAIL));
        metadata.setSearchAction(searchAction);
    }
}
