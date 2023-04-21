package com.gnizr.web.action.user;

import java.util.List;
import org.apache.log4j.Logger;
import com.gnizr.core.exceptions.NoSuchUserException;
import com.gnizr.core.folder.FolderManager;
import com.gnizr.core.user.UserManager;
import com.gnizr.core.util.GnizrDaoUtil;
import com.gnizr.db.dao.Folder;
import com.gnizr.db.dao.User;
import com.gnizr.db.vocab.AccountStatus;
import com.gnizr.web.action.AbstractAction;

public class EditUser extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5568453303140180078L;

    private static final Logger logger = Logger.getLogger(EditUser.class);

    private List<User> gnizrUsers;

    private String username;

    private User editUser;

    private UserManager userManager;

    private FolderManager folderManager;

    public FolderManager getFolderManager() {
        return folderManager;
    }

    public void setFolderManager(FolderManager folderManager) {
        this.folderManager = folderManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<User> getGnizrUsers() {
        return gnizrUsers;
    }

    @Override
    protected String go() throws Exception {
        return SUCCESS;
    }

    public String fetchEditData() {
        try {
            gnizrUsers = userManager.listUsers();
            return SUCCESS;
        } catch (Exception e) {
            logger.error("error fetching user account info", e);
        }
        return ERROR;
    }

    public String doChangePassword() {
        String op = INPUT;
        try {
            if (editUser.getPassword() != null) {
                User u = userManager.getUser(editUser.getUsername());
                u.setPassword(editUser.getPassword());
                if (userManager.changePassword(u) == true) {
                    addActionMessage("Successfully changed user password!");
                    op = SUCCESS;
                } else {
                    addActionError("unable to change password for user: " + editUser.getUsername());
                    op = ERROR;
                }
            }
            editUser = userManager.getUser(editUser.getUsername());
        } catch (Exception e) {
            addActionMessage("No such user: " + editUser.getUsername());
            op = INPUT;
        }
        return op;
    }

    public String doChangeStatus() {
        String op = INPUT;
        boolean isOkay = false;
        try {
            if (editUser.getAccountStatus() == AccountStatus.ACTIVE) {
                isOkay = userManager.activateUserAccount(editUser);
            } else if (editUser.getAccountStatus() == AccountStatus.INACTIVE) {
                isOkay = userManager.inactivateUserAccount(editUser);
            } else if (editUser.getAccountStatus() == AccountStatus.DISABLED) {
                isOkay = userManager.disableUserAccount(editUser);
            }
            editUser = userManager.getUser(editUser.getUsername());
        } catch (Exception e) {
            addActionMessage("No such user: " + editUser.getUsername());
            op = INPUT;
        }
        if (isOkay == true) {
            addActionMessage("Successfully changed user account status!");
            op = SUCCESS;
        }
        return op;
    }

    public String doUpdate() {
        String op = INPUT;
        try {
            if (editUser == null) {
                editUser = userManager.getUser(username);
                op = SUCCESS;
            } else {
                boolean isOkay = userManager.changeProfile(editUser);
                if (isOkay == true) {
                    addActionMessage("Successfully changed user profile");
                    op = SUCCESS;
                } else {
                    op = ERROR;
                }
            }
            editUser = userManager.getUser(editUser.getUsername());
        } catch (NoSuchUserException e) {
            logger.debug(e);
            addActionMessage("No such user: " + username);
            op = INPUT;
        }
        return op;
    }

    public String doDelete() {
        String op = INPUT;
        if (username != null) {
            try {
                boolean isOkay = userManager.deleteUser(new User(username));
                if (isOkay == false) {
                    addActionError("unable to delete user account: " + username);
                    op = ERROR;
                } else {
                    addActionMessage("Successfully deleted user account: " + username);
                    op = SUCCESS;
                }
            } catch (Exception e) {
                logger.debug(e);
                addActionMessage("No such user: " + username);
                op = INPUT;
            }
        }
        fetchEditData();
        return op;
    }

    public User getEditUser() {
        return editUser;
    }

    public void setEditUser(User editUser) {
        this.editUser = editUser;
    }

    public String doAddNewUser() {
        String op = INPUT;
        if (editUser != null) {
            try {
                editUser.setCreatedOn(GnizrDaoUtil.getNow());
                boolean isOkay = userManager.createUser(editUser);
                if (isOkay == true) {
                    Folder myf = folderManager.createFolder(editUser, FolderManager.MY_BOOKMARKS_LABEL, "");
                    if (myf == null) {
                        logger.error("Unable to create My Bookmarks folder");
                        return ERROR;
                    }
                    op = SUCCESS;
                    addActionMessage("Successfully added a new user account!");
                } else {
                    addActionError("Unable to create a new user account");
                    logger.error("unable to create a new user account: user=" + editUser);
                    op = ERROR;
                }
            } catch (Exception e) {
                addActionMessage("An user account of the same username already exists.");
                op = INPUT;
            }
        }
        return op;
    }
}
