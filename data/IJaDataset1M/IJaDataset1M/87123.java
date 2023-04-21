package org.commsuite.web.beans.roles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javolution.util.FastTable;
import org.apache.log4j.Logger;
import org.commsuite.model.FakedRole;
import org.commsuite.model.ws.WSAction;
import org.commsuite.model.ws.WSRole;
import org.commsuite.model.ws.WSUser;
import org.commsuite.util.SpringAdminPanelContext;
import org.commsuite.web.beans.BeansUtils;
import org.commsuite.web.beans.LanguageSelectionBean;
import org.commsuite.web.beans.actions.ActionExtended;
import org.commsuite.web.beans.users.UserExtended;
import org.commsuite.ws.CommunicateUtils;
import org.commsuite.ws.ICommunicateWS;
import org.commsuite.ws.WebServiceException;

/**
 * @since 1.0
 * @author Szymon Kuzniak
 */
public class SearchRoleBean {

    private static final Logger logger = Logger.getLogger(SearchRoleBean.class);

    private static final String SEARCH_PAGE_ADDRESS = "/pages/roles/CSRoleSearch.jsf";

    private String lastUsedId;

    private String name;

    private String description;

    private String roleName;

    private String selectedRole;

    private boolean[] selectedActions;

    private boolean[] selectedUsers;

    private List<UserExtended> temporaryUsersForRole;

    private List<ActionExtended> temporaryActionsForRole;

    private List<UserExtended> temporaryUsersToDelete;

    private List<String> selectedActionsIds;

    private List<String> selectedUserIds;

    public List<UserExtended> getTemporaryUsersToDelete() {
        return temporaryUsersToDelete;
    }

    public void setTemporaryUsersToDelete(List<UserExtended> temporaryUsersToDelete) {
        this.temporaryUsersToDelete = temporaryUsersToDelete;
    }

    public boolean[] getSelectedActions() {
        return selectedActions;
    }

    public void setSelectedActions(boolean[] selectedActions) {
        this.selectedActions = selectedActions;
    }

    public boolean[] getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(boolean[] selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getDescription() {
        if (null == description || 0 == description.length()) {
            try {
                this.description = this.getRoleFromSelected().getDescription();
            } catch (NullPointerException npe) {
                this.handleNullPointerException();
            }
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        if (null == name || 0 == name.length()) {
            try {
                name = getRoleFromSelected().getName();
            } catch (NullPointerException npe) {
                this.handleNullPointerException();
            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSelectedActionsIds() {
        return selectedActionsIds;
    }

    public void setSelectedActionsIds(List<String> selectedActionsIds) {
        this.selectedActionsIds = selectedActionsIds;
    }

    public List<String> getSelectedUserIds() {
        return selectedUserIds;
    }

    public void setSelectedUserIds(List<String> selectedUserIds) {
        this.selectedUserIds = selectedUserIds;
    }

    public List<ActionExtended> getTemporaryActionsForRole() {
        return temporaryActionsForRole;
    }

    public void setTemporaryActionsForRole(List<ActionExtended> temporaryActionsForRole) {
        this.temporaryActionsForRole = temporaryActionsForRole;
    }

    public List<UserExtended> getTemporaryUsersForRole() {
        return temporaryUsersForRole;
    }

    public void setTemporaryUsersForRole(List<UserExtended> temporaryUsersForRole) {
        this.temporaryUsersForRole = temporaryUsersForRole;
    }

    /**
     * @return Returns the roleName.
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     *            The roleName to set.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return Returns the selectedRole.
     */
    public String getSelectedRole() {
        return selectedRole;
    }

    /**
     * @param selectedRoles
     *            The selectedRole to set.
     */
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    /**
     * this method is used to get all roles from database. when
     * roleName object is not null, and is not an empty string
     * then only roles which contains given name will be displayed
     * 
     * @return list of select items to be presented on web page
     */
    public List<SelectItem> getAvailableRoles() {
        final ICommunicateWS ws = (ICommunicateWS) SpringAdminPanelContext.getCommunicateWS();
        List<WSRole> roles = new FastTable<WSRole>();
        try {
            if (null == this.roleName || 0 == roleName.length()) {
                roles = new FastTable<WSRole>(ws.getAllRoles());
            } else {
                roles = new FastTable<WSRole>(ws.getRolesByName(this.roleName));
            }
        } catch (WebServiceException wse) {
            logger.fatal("Exception while listing available roles", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("Exception", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        }
        final List<SelectItem> choice = new FastTable<SelectItem>();
        for (final WSRole role : roles) {
            final SelectItem item = new SelectItem();
            item.setLabel(role.getName());
            item.setValue(String.valueOf(role.getId()));
            choice.add(item);
        }
        return choice;
    }

    /**
     * this method is used to delete role from database via webservice
     * 
     * @return navigation rule to list of roles screen
     */
    public String deleteRole() {
        final ICommunicateWS ws = (ICommunicateWS) SpringAdminPanelContext.getCommunicateWS();
        try {
            if (null != this.selectedRole) {
                ws.deleteRoleFromDatabase(this.selectedRole);
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ROLE_SUCCESSFULL_DELETE, BeansUtils.SEARCH_ROLE_NAVIGATION);
            } else {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ROLE_ERROR_NPE, BeansUtils.SEARCH_ROLE_NAVIGATION);
            }
        } catch (WebServiceException wse) {
            logger.fatal("problem while deleting role", wse);
            if (CommunicateUtils.ASSIGNED_ROLE_MESSAGE.equals(wse.getMessage())) {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ROLE_ERROR_ASSIGNED, BeansUtils.SEARCH_ROLE_NAVIGATION);
            } else {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ERROR_DELETE, BeansUtils.SEARCH_ROLE_NAVIGATION);
            }
        } catch (Exception e) {
            logger.fatal("problem while deleting role", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ERROR_DELETE, BeansUtils.SEARCH_ROLE_NAVIGATION);
        }
        this.clearBean();
        return BeansUtils.SEARCH_ROLE_NAVIGATION;
    }

    /**
     * returns WSRole object with given id. id is passed via web
     * page parameter.
     * @return
     */
    public WSRole getRoleFromSelected() {
        final ICommunicateWS ws = (ICommunicateWS) SpringAdminPanelContext.getCommunicateWS();
        WSRole role = new FakedRole();
        try {
            role = ws.getRoleById(this.selectedRole);
        } catch (WebServiceException wse) {
            logger.fatal("problem while getting role by id", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("problem while getting role by id", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        }
        return role;
    }

    /**
     * this method gets actions assigned to role in corrent session.
     * if tempraryActionsForRole collection is null, it fetches 
     * assigned actions to role from database.
     *  
     * @return actions assigned to role in database, and during current sesion
     */
    public List<ActionExtended> getActionsForRole() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            if (null == this.temporaryActionsForRole || !this.lastUsedId.equals(this.selectedRole)) {
                this.lastUsedId = this.selectedRole;
                this.temporaryActionsForRole = new FastTable<ActionExtended>();
                List<WSAction> a = (List<WSAction>) ws.getActionsByRole(this.selectedRole);
                for (WSAction action : a) {
                    ActionExtended actionExtended = new ActionExtended(false, action);
                    this.temporaryActionsForRole.add(actionExtended);
                }
            }
        } catch (WebServiceException wse) {
            logger.fatal("web service exception while receiveing actions for role: " + wse.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("exception while receiveing actions for role: " + e.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.SEARCH_ROLE_NAVIGATION);
        }
        return this.temporaryActionsForRole;
    }

    /**
     * this method gets users assigned to role in corrent session.
     * if tempraryUsersForRole collection is null, it fetches 
     * assigned users to role from database.
     *  
     * @return users assigned to role in database, and during current sesion
     */
    public List<UserExtended> getUsersForRole() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            if (null == this.temporaryUsersForRole || !this.lastUsedId.equals(this.selectedRole)) {
                this.lastUsedId = new String(this.selectedRole);
                this.temporaryUsersForRole = new FastTable<UserExtended>();
                List<WSUser> u = (List<WSUser>) ws.getUsersByRole(this.selectedRole);
                for (WSUser user : u) {
                    UserExtended userExtended = new UserExtended(false, user);
                    this.temporaryUsersForRole.add(userExtended);
                }
            }
        } catch (WebServiceException wse) {
            logger.fatal("web service exception while receiveing users for role: " + wse.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("exception while receiveing users for role: " + e.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        return this.temporaryUsersForRole;
    }

    /**
     * this method is used to get all actions, which are not assigned
     * to selecteg role.
     * 
     * @return list of actions no assigned yet
     */
    public List<SelectItem> getActionsToAssign() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        final List<WSAction> tmpList = new FastTable<WSAction>();
        List<WSAction> actions = new ArrayList<WSAction>();
        try {
            actions = new FastTable<WSAction>(ws.getActionsAvailable());
            for (final WSAction a : actions) {
                if (this.existsOnList(a)) {
                    tmpList.add(a);
                }
            }
            actions.removeAll(tmpList);
        } catch (WebServiceException wse) {
            logger.fatal("web service exception while listing actions", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("exception while listing actions", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        final List<SelectItem> choices = new FastTable<SelectItem>();
        for (final WSAction action : actions) {
            final SelectItem item = new SelectItem();
            item.setLabel(action.getName());
            item.setValue(String.valueOf(action.getId()));
            choices.add(item);
        }
        return choices;
    }

    /**
     * this method is used to assign selected actions to temporary
     * collection. it avoids adding actions to role every time 
     * user select new ations.
     * 
     * @return navigation rule to edit role screen
     */
    public String assignSelectedActions() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            for (Object id : this.selectedActionsIds) {
                final WSAction action = ws.getWSActionById(id.toString());
                ActionExtended actionExtended = new ActionExtended(false, action);
                this.temporaryActionsForRole.add(actionExtended);
            }
        } catch (WebServiceException wse) {
            logger.fatal("web service exception while assigning actions", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("exception while assigning actions", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ACTIONS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    /**
     * this method is used to get all users, which are not assigned
     * to selecteg role.
     * 
     * @return list of users no assigned yet
     */
    public List<SelectItem> getUsersToAssign() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        final List<WSUser> tmpList = new FastTable<WSUser>();
        List<WSUser> users = new ArrayList<WSUser>();
        try {
            users = new FastTable<WSUser>(ws.getAllUsers());
            for (final WSUser u : users) {
                if (this.existsOnList(u)) {
                    tmpList.add(u);
                }
            }
            users.removeAll(tmpList);
        } catch (WebServiceException wse) {
            logger.fatal("web service exception while listing users", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("exception while listing users", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        final List<SelectItem> choices = new FastTable<SelectItem>();
        for (WSUser user : users) {
            final SelectItem item = new SelectItem();
            item.setLabel(user.getLogin());
            item.setValue(String.valueOf(user.getId()));
            choices.add(item);
        }
        return choices;
    }

    /**
     * this method is used to assign selected users to temporary
     * collection. it avoids adding users to role every time 
     * user select new users.
     * 
     * @return navigation rule to edit role screen
     */
    public String assignSelectedUsers() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            for (Object id : this.selectedUserIds) {
                final WSUser user = ws.getUserById(id.toString());
                UserExtended userExtended = new UserExtended(false, user);
                this.temporaryUsersForRole.add(userExtended);
            }
        } catch (WebServiceException wse) {
            logger.fatal("Web service exception while getting user/role by id: " + wse.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("Exception while getting user/role by id: " + e.getMessage());
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_USERS_ERROR_FETCH, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    /**
     * called when user press the 'Cancel' button
     * 
     * @return navigation rule to edit role screen
     */
    public String cancel() {
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    /**
     * used to upadte role via web service. it saves previously
     * assigned users, and actions, and updates name and description
     * 
     * @return navigation rule to edit role screen
     */
    public String updateRole() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            WSRole role = ws.getRoleById(this.selectedRole);
            if (null == role) {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_NOTHING_SELECTED, BeansUtils.EDIT_ROLE_NAVIGATION);
                return BeansUtils.EDIT_ROLE_NAVIGATION;
            }
            role.setName(this.name);
            role.setDescription(this.description);
            role = ws.updateRole(role);
            String idsToAdd[] = new String[this.temporaryUsersForRole.size()];
            int i = 0;
            for (final UserExtended user : this.temporaryUsersForRole) {
                idsToAdd[i] = String.valueOf(user.getUser().getId());
                i++;
            }
            i = 0;
            String idsToDelete[] = new String[this.temporaryUsersToDelete.size()];
            for (final UserExtended user : this.temporaryUsersToDelete) {
                idsToDelete[i] = String.valueOf(user.getUser().getId());
                i++;
            }
            role = ws.updateUsersForRole(role, idsToAdd, idsToDelete);
            i = 0;
            String ids[] = new String[this.temporaryActionsForRole.size()];
            for (final ActionExtended action : this.temporaryActionsForRole) {
                ids[i] = String.valueOf(action.getAction().getId());
                i++;
            }
            role = ws.updateActionsForRole(role, ids);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ROLE_SUCCESSFULL_UPDATE, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (WebServiceException wse) {
            logger.fatal("WS exception while adding role(after edit)", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_ROLE_ERROR_UPDATE, BeansUtils.EDIT_ROLE_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("Exception while adding role(after edit)", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_GENERAL, BeansUtils.MESSAGE_ROLE_ERROR_UPDATE, BeansUtils.EDIT_ROLE_NAVIGATION);
        }
        this.temporaryActionsForRole = null;
        this.temporaryUsersForRole = null;
        return BeansUtils.SEARCH_ROLE_NAVIGATION;
    }

    /**
     * this method tests if given object exists in temporary collection.
     * 
     * @param object object to test. should be instance of WSAction or WSUser
     * @return true if object exist, false otherwise
     * @throws ClassCastException if object is not a WSAction or WSUser
     */
    private boolean existsOnList(Object object) {
        boolean result = false;
        if (object instanceof WSAction) {
            WSAction action = (WSAction) object;
            for (ActionExtended a : this.temporaryActionsForRole) {
                if (0 == a.getAction().getId().compareTo(action.getId())) {
                    result = true;
                    break;
                }
            }
        } else if (object instanceof WSUser) {
            WSUser user = (WSUser) object;
            for (UserExtended u : this.temporaryUsersForRole) {
                if (0 == u.getUser().getId().compareTo(user.getId())) {
                    result = true;
                    break;
                }
            }
        } else {
            throw new ClassCastException("Only WSUser or WSAction object type is supported.");
        }
        return result;
    }

    /**
     * this method is used to clear all data left after 
     * previous transation.
     *
     */
    private void clearBean() {
        this.name = "";
        this.description = "";
        this.temporaryActionsForRole = null;
        this.temporaryUsersForRole = null;
    }

    /**
     * this method is used to clear session and back to previous screen.
     * @return navigation rule to edit group screen
     */
    public String registerNewSession() {
        this.clearBean();
        if (this.selectedRole == null) {
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_NOTHING_SELECTED, BeansUtils.SEARCH_ROLE_NAVIGATION);
            return BeansUtils.SEARCH_ROLE_NAVIGATION;
        }
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    public String deleteSelectedActions() {
        List<ActionExtended> actions = new FastTable<ActionExtended>();
        for (ActionExtended a : this.temporaryActionsForRole) {
            if (a.isSelected()) {
                actions.add(a);
            }
        }
        this.temporaryActionsForRole.removeAll(actions);
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    public String deleteSelectedUsers() {
        if (null == this.temporaryUsersToDelete) {
            this.temporaryUsersToDelete = new FastTable<UserExtended>();
        }
        List<UserExtended> users = new FastTable<UserExtended>();
        for (UserExtended u : this.temporaryUsersForRole) {
            if (u.isSelected()) {
                logger.debug("HERE I AM");
                users.add(u);
                this.temporaryUsersToDelete.add(u);
            }
        }
        this.temporaryUsersForRole.removeAll(users);
        return BeansUtils.EDIT_ROLE_NAVIGATION;
    }

    /**
	 * 
	 */
    public SearchRoleBean() {
        super();
        this.temporaryActionsForRole = new FastTable<ActionExtended>();
        this.temporaryUsersToDelete = new FastTable<UserExtended>();
        this.temporaryUsersForRole = new FastTable<UserExtended>();
    }

    private void handleNullPointerException() {
        try {
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_NOTHING_SELECTED, BeansUtils.SEARCH_ROLE_NAVIGATION);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.sendRedirect(SEARCH_PAGE_ADDRESS);
        } catch (IOException ioe) {
            logger.fatal("IOException");
        }
    }
}
