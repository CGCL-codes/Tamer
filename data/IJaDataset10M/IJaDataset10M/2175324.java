package com.shuetech.usermanagement.session;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import com.shuetech.general.util.data.Tree;
import com.shuetech.usermanagement.Definitions.UserAccountDefinition;

@Remote
public interface UserManagement {

    public void populateRoles(Tree<String> roleTree);

    public void populateRoleUsers(HashMap<String, List<UserAccountDefinition>> roleUsers);

    public void deleteAllRoles();

    public void deleteAllUserAccounts();

    public void deleteUserAccount(String username);

    public boolean createUserAccount(String username, String password, String firstname, String lastname, String email);

    public boolean grantRoleToUser(String user, String role);

    public boolean revokeRoleFromUser(String user, String role);
}
