package org.opencms.workplace.tools.workplace.broadcast;

import org.opencms.file.CmsUser;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsSessionInfo;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsUUID;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.list.A_CmsListDialog;
import org.opencms.workplace.list.CmsListColumnAlignEnum;
import org.opencms.workplace.list.CmsListColumnDefinition;
import org.opencms.workplace.list.CmsListDateMacroFormatter;
import org.opencms.workplace.list.CmsListDefaultAction;
import org.opencms.workplace.list.CmsListDirectAction;
import org.opencms.workplace.list.CmsListItem;
import org.opencms.workplace.list.CmsListItemActionIconComparator;
import org.opencms.workplace.list.CmsListItemDetails;
import org.opencms.workplace.list.CmsListItemDetailsFormatter;
import org.opencms.workplace.list.CmsListMetadata;
import org.opencms.workplace.list.CmsListMultiAction;
import org.opencms.workplace.list.CmsListOrderEnum;
import org.opencms.workplace.list.CmsListTimeIntervalFormatter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * Session list for broadcasting messages.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision: 1.17 $ 
 * 
 * @since 6.0.0 
 */
public class CmsSessionsList extends A_CmsListDialog {

    /** list action id constant. */
    public static final String LIST_ACTION_MESSAGE = "am";

    /** list action id constant. */
    public static final String LIST_ACTION_PENDING_DISABLED = "apd";

    /** list action id constant. */
    public static final String LIST_ACTION_PENDING_ENABLED = "ape";

    /** list column id constant. */
    public static final String LIST_COLUMN_CREATION = "cc";

    /** list column id constant. */
    public static final String LIST_COLUMN_INACTIVE = "ci";

    /** list column id constant. */
    public static final String LIST_COLUMN_MESSAGE = "cm";

    /** list column id constant. */
    public static final String LIST_COLUMN_ORGUNIT = "cou";

    /** list column id constant. */
    public static final String LIST_COLUMN_PENDING = "cp";

    /** list column id constant. */
    public static final String LIST_COLUMN_PROJECT = "cj";

    /** list column id constant. */
    public static final String LIST_COLUMN_SITE = "cs";

    /** list column id constant. */
    public static final String LIST_COLUMN_USER = "cu";

    /** list action id constant. */
    public static final String LIST_DEFACTION_MESSAGE = "dm";

    /** list column id constant. */
    public static final String LIST_DETAIL_EMAIL = "de";

    /** list id constant. */
    public static final String LIST_ID = "ls";

    /** list action id constant. */
    public static final String LIST_MACTION_EMAIL = "me";

    /** list action id constant. */
    public static final String LIST_MACTION_MESSAGE = "mm";

    /** Path to the list buttons. */
    public static final String PATH_BUTTONS = "tools/workplace/buttons/";

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsSessionsList(CmsJspActionElement jsp) {
        super(jsp, LIST_ID, Messages.get().container(Messages.GUI_SESSIONS_LIST_NAME_0), LIST_COLUMN_USER, CmsListOrderEnum.ORDER_ASCENDING, LIST_COLUMN_USER);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsSessionsList(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListMultiActions()
     */
    public void executeListMultiActions() throws IOException, ServletException {
        Map params = new HashMap();
        params.put(A_CmsMessageDialog.PARAM_SESSIONIDS, getParamSelItems());
        params.put(CmsDialog.PARAM_ACTION, CmsDialog.DIALOG_INITIAL);
        if (getParamListAction().equals(LIST_MACTION_MESSAGE)) {
            getToolManager().jspForwardTool(this, "/workplace/broadcast/message", params);
        } else if (getParamListAction().equals(LIST_MACTION_EMAIL)) {
            getToolManager().jspForwardTool(this, "/workplace/broadcast/email", params);
        } else {
            throwListUnsupportedActionException();
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#executeListSingleActions()
     */
    public void executeListSingleActions() throws IOException, ServletException {
        Map params = new HashMap();
        params.put(A_CmsMessageDialog.PARAM_SESSIONIDS, getSelectedItem().getId());
        params.put(CmsDialog.PARAM_ACTION, CmsDialog.DIALOG_INITIAL);
        if (getParamListAction().equals(LIST_ACTION_MESSAGE) || getParamListAction().equals(LIST_DEFACTION_MESSAGE)) {
            getToolManager().jspForwardTool(this, "/workplace/broadcast/message", params);
        } else {
            throwListUnsupportedActionException();
        }
        listSave();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#fillDetails(java.lang.String)
     */
    protected void fillDetails(String detailId) {
        List sessions = getList().getAllContent();
        Iterator i = sessions.iterator();
        while (i.hasNext()) {
            CmsListItem item = (CmsListItem) i.next();
            CmsSessionInfo session = OpenCms.getSessionManager().getSessionInfo(new CmsUUID(item.getId()));
            StringBuffer html = new StringBuffer(32);
            if (detailId.equals(LIST_DETAIL_EMAIL)) {
                try {
                    CmsUser user = getCms().readUser(session.getUserId());
                    html.append(user.getEmail());
                } catch (CmsException e) {
                }
            } else {
                continue;
            }
            item.set(detailId, html.toString());
        }
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#getListItems()
     */
    protected List getListItems() throws CmsException {
        List manageableUsers = OpenCms.getRoleManager().getManageableUsers(getCms(), "", true);
        List ret = new ArrayList();
        List sessionInfos = OpenCms.getSessionManager().getSessionInfos();
        Iterator itSessions = sessionInfos.iterator();
        while (itSessions.hasNext()) {
            CmsSessionInfo sessionInfo = (CmsSessionInfo) itSessions.next();
            CmsListItem item = getList().newItem(sessionInfo.getSessionId().toString());
            CmsUser user = getCms().readUser(sessionInfo.getUserId());
            if (!manageableUsers.contains(user)) {
                continue;
            }
            item.set(LIST_COLUMN_USER, user.getFullName());
            item.set(LIST_COLUMN_ORGUNIT, OpenCms.getOrgUnitManager().readOrganizationalUnit(getCms(), user.getOuFqn()).getDisplayName(getLocale()));
            item.set(LIST_COLUMN_CREATION, new Date(sessionInfo.getTimeCreated()));
            item.set(LIST_COLUMN_INACTIVE, new Long(System.currentTimeMillis() - sessionInfo.getTimeUpdated()));
            try {
                item.set(LIST_COLUMN_PROJECT, getCms().readProject(sessionInfo.getProject()).getName());
            } catch (Exception e) {
            }
            item.set(LIST_COLUMN_SITE, sessionInfo.getSiteRoot());
            ret.add(item);
        }
        try {
            if (OpenCms.getOrgUnitManager().getOrganizationalUnits(getCms(), "", true).isEmpty()) {
                getList().getMetadata().getColumnDefinition(LIST_COLUMN_ORGUNIT).setVisible(false);
                getList().getMetadata().getColumnDefinition(LIST_COLUMN_USER).setWidth("40%");
            } else {
                getList().getMetadata().getColumnDefinition(LIST_COLUMN_ORGUNIT).setVisible(true);
                getList().getMetadata().getColumnDefinition(LIST_COLUMN_USER).setWidth("20%");
            }
        } catch (CmsException e) {
        }
        return ret;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initMessages()
     */
    protected void initMessages() {
        addMessages(Messages.get().getBundleName());
        addMessages(org.opencms.workplace.tools.workplace.Messages.get().getBundleName());
        super.initMessages();
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setColumns(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setColumns(CmsListMetadata metadata) {
        CmsListColumnDefinition messageCol = new CmsListColumnDefinition(LIST_COLUMN_MESSAGE);
        messageCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_MESSAGE_0));
        messageCol.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_MESSAGE_HELP_0));
        messageCol.setWidth("20");
        messageCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        messageCol.setSorteable(false);
        CmsListDirectAction messageAction = new CmsListDirectAction(LIST_ACTION_MESSAGE);
        messageAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_MESSAGE_NAME_0));
        messageAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_MESSAGE_HELP_0));
        messageAction.setIconPath(PATH_BUTTONS + "send_message.png");
        messageCol.addDirectAction(messageAction);
        metadata.addColumn(messageCol);
        CmsListColumnDefinition pendingCol = new CmsListColumnDefinition(LIST_COLUMN_PENDING);
        pendingCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_PENDING_0));
        pendingCol.setWidth("20");
        pendingCol.setAlign(CmsListColumnAlignEnum.ALIGN_CENTER);
        pendingCol.setListItemComparator(new CmsListItemActionIconComparator());
        CmsListDirectAction pendingAction = new CmsListDirectAction(LIST_ACTION_PENDING_ENABLED) {

            public boolean isVisible() {
                if (getItem() != null) {
                    return !OpenCms.getSessionManager().getBroadcastQueue(getItem().getId()).isEmpty();
                }
                return super.isVisible();
            }
        };
        pendingAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_PENDING_NAME_0));
        pendingAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_PENDING_HELP_0));
        pendingAction.setIconPath(PATH_BUTTONS + "message_pending.png");
        pendingAction.setEnabled(false);
        pendingCol.addDirectAction(pendingAction);
        CmsListDirectAction notPendingAction = new CmsListDirectAction(LIST_ACTION_PENDING_DISABLED) {

            public boolean isVisible() {
                if (getItem() != null) {
                    return OpenCms.getSessionManager().getBroadcastQueue(getItem().getId()).isEmpty();
                }
                return super.isVisible();
            }
        };
        notPendingAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_NOTPENDING_NAME_0));
        notPendingAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_NOTPENDING_HELP_0));
        notPendingAction.setIconPath(PATH_BUTTONS + "message_notpending.png");
        notPendingAction.setEnabled(false);
        pendingCol.addDirectAction(notPendingAction);
        metadata.addColumn(pendingCol);
        CmsListColumnDefinition userCol = new CmsListColumnDefinition(LIST_COLUMN_USER);
        userCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_USER_0));
        CmsListDefaultAction messageEditAction = new CmsListDefaultAction(LIST_DEFACTION_MESSAGE);
        messageEditAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_MESSAGE_NAME_0));
        messageEditAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_ACTION_MESSAGE_HELP_0));
        userCol.addDefaultAction(messageEditAction);
        metadata.addColumn(userCol);
        CmsListColumnDefinition ouCol = new CmsListColumnDefinition(LIST_COLUMN_ORGUNIT);
        ouCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_ORGUNIT_0));
        ouCol.setWidth("30%");
        metadata.addColumn(ouCol);
        CmsListColumnDefinition creationCol = new CmsListColumnDefinition(LIST_COLUMN_CREATION);
        creationCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_CREATION_0));
        creationCol.setWidth("16%");
        creationCol.setFormatter(CmsListDateMacroFormatter.getDefaultDateFormatter());
        metadata.addColumn(creationCol);
        CmsListColumnDefinition inactiveCol = new CmsListColumnDefinition(LIST_COLUMN_INACTIVE);
        inactiveCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_INACTIVE_0));
        inactiveCol.setWidth("10%");
        inactiveCol.setFormatter(new CmsListTimeIntervalFormatter());
        metadata.addColumn(inactiveCol);
        CmsListColumnDefinition projectCol = new CmsListColumnDefinition(LIST_COLUMN_PROJECT);
        projectCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_PROJECT_0));
        projectCol.setWidth("12%");
        metadata.addColumn(projectCol);
        CmsListColumnDefinition siteCol = new CmsListColumnDefinition(LIST_COLUMN_SITE);
        siteCol.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_COLS_SITE_0));
        siteCol.setWidth("12%");
        metadata.addColumn(siteCol);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setIndependentActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setIndependentActions(CmsListMetadata metadata) {
        CmsListItemDetails emailDetail = new CmsListItemDetails(LIST_DETAIL_EMAIL);
        emailDetail.setAtColumn(LIST_COLUMN_USER);
        emailDetail.setVisible(false);
        emailDetail.setFormatter(new CmsListItemDetailsFormatter(Messages.get().container(Messages.GUI_SESSIONS_LABEL_EMAIL_0)));
        emailDetail.setShowActionName(Messages.get().container(Messages.GUI_SESSIONS_DETAIL_SHOW_EMAIL_NAME_0));
        emailDetail.setShowActionHelpText(Messages.get().container(Messages.GUI_SESSIONS_DETAIL_SHOW_EMAIL_HELP_0));
        emailDetail.setHideActionName(Messages.get().container(Messages.GUI_SESSIONS_DETAIL_HIDE_EMAIL_NAME_0));
        emailDetail.setHideActionHelpText(Messages.get().container(Messages.GUI_SESSIONS_DETAIL_HIDE_EMAIL_HELP_0));
        metadata.addItemDetails(emailDetail);
    }

    /**
     * @see org.opencms.workplace.list.A_CmsListDialog#setMultiActions(org.opencms.workplace.list.CmsListMetadata)
     */
    protected void setMultiActions(CmsListMetadata metadata) {
        CmsListMultiAction messageMultiAction = new CmsListMultiAction(LIST_MACTION_MESSAGE);
        messageMultiAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_MESSAGE_NAME_0));
        messageMultiAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_MESSAGE_HELP_0));
        messageMultiAction.setConfirmationMessage(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_MESSAGE_CONF_0));
        messageMultiAction.setIconPath(PATH_BUTTONS + "multi_send_message.png");
        metadata.addMultiAction(messageMultiAction);
        CmsListMultiAction emailMultiAction = new CmsListMultiAction(LIST_MACTION_EMAIL);
        emailMultiAction.setName(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_EMAIL_NAME_0));
        emailMultiAction.setHelpText(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_EMAIL_HELP_0));
        emailMultiAction.setConfirmationMessage(Messages.get().container(Messages.GUI_SESSIONS_LIST_MACTION_EMAIL_CONF_0));
        emailMultiAction.setIconPath(PATH_BUTTONS + "multi_send_email.png");
        metadata.addMultiAction(emailMultiAction);
    }
}
