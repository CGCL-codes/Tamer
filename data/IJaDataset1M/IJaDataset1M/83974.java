package com.medsol.user.action;

import com.opensymphony.xwork2.ActionSupport;
import com.medsol.user.service.RoleService;
import com.medsol.user.service.UserService;
import com.medsol.user.model.LoggedInUserDetails;
import com.medsol.user.model.Role;
import com.medsol.user.model.User;
import com.medsol.common.model.AddressInfo;
import com.medsol.owner.model.Owner;
import com.medsol.owner.action.OwnerAction;
import com.medsol.owner.service.OwnerService;
import java.util.List;
import java.util.ArrayList;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: vinay
 * Date: 16 Aug, 2008
 * Time: 3:41:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserAction extends ActionSupport {

    private RoleService roleService;

    private OwnerService ownerService;

    private UserService userService;

    private List<User> allUsers;

    private List<String> selectedRoles;

    private List<User> srchResults;

    private Owner owner;

    private User user;

    private String oldPw;

    private String newPw1;

    private String newPw2;

    @SkipValidation
    public String listUsers() throws Exception {
        allUsers = userService.findAll();
        return SUCCESS;
    }

    public String createOrUpdateUser() throws Exception {
        String operationDescription;
        populateUserRoles();
        if (user.getUserId() == null) {
            operationDescription = "created";
            if (user.getAddressInfo() == null) {
                user.setAddressInfo(new AddressInfo());
            }
            userService.save(user);
        } else {
            operationDescription = "updated";
            userService.update(user);
        }
        addActionMessage("Successfully " + operationDescription + " User - " + user.getUserId() + ".");
        return SUCCESS;
    }

    private void populateUserRoles() {
        if (user != null && selectedRoles != null && !selectedRoles.isEmpty()) {
            List<Role> roles = new ArrayList<Role>(selectedRoles.size());
            for (String selectedRole : selectedRoles) {
                Role role = new Role();
                role.setId(new Long(selectedRole));
                roles.add(role);
            }
            user.setRoles(roles);
        }
    }

    @SkipValidation
    public String addUser() throws Exception {
        return SUCCESS;
    }

    @SkipValidation
    public String searchUser() throws Exception {
        srchResults = userService.getUserByName(user.getName());
        return SUCCESS;
    }

    @SkipValidation
    public String editUser() throws Exception {
        if (this.user == null || this.user.getUserId() == null) {
            addActionError("User Id is not set in the request");
            return INPUT;
        }
        user = userService.findById(user.getUserId());
        populateSelectedRoles();
        return SUCCESS;
    }

    private void populateSelectedRoles() {
        if (user != null && !user.getRoles().isEmpty()) {
            List<String> userRoles = new ArrayList<String>(user.getRoles().size());
            for (Role role : user.getRoles()) {
                userRoles.add(String.valueOf(role.getId()));
            }
            this.selectedRoles = userRoles;
        }
    }

    public String deactivateUser() {
        if (this.user == null || this.user.getUserId() == null) {
            addActionError("User Id is not set in the request");
            return INPUT;
        }
        userService.deactvateUser(user.getUserId());
        addActionMessage("User deactivated successfully");
        return SUCCESS;
    }

    public String changePassword() throws Exception {
        LoggedInUserDetails loggedInUserDetails = (LoggedInUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User loggedInUser = loggedInUserDetails.getUser();
        if (oldPw.equals(loggedInUser.getPassword())) {
            if (StringUtils.hasText(newPw1)) {
                if (newPw1.equals(newPw2)) {
                    loggedInUser.setPassword(newPw1);
                    userService.update(loggedInUser);
                    addActionMessage("User Password updated successfully");
                    return SUCCESS;
                } else {
                    addActionError("New Passwords does not match");
                    return INPUT;
                }
            } else {
                addActionError("Please provide some text for the new password");
                return INPUT;
            }
        } else {
            addActionError("Old Password given does not match");
            return INPUT;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public List<User> getSrchResults() {
        return srchResults;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public List<Role> getRoles() {
        return roleService.findAll();
    }

    public List<Owner> getOwnerCompanies() {
        return ownerService.findAll();
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setOwnerService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public String getOldPw() {
        return oldPw;
    }

    public void setOldPw(String oldPw) {
        this.oldPw = oldPw;
    }

    public String getNewPw1() {
        return newPw1;
    }

    public void setNewPw1(String newPw1) {
        this.newPw1 = newPw1;
    }

    public String getNewPw2() {
        return newPw2;
    }

    public void setNewPw2(String newPw2) {
        this.newPw2 = newPw2;
    }
}
