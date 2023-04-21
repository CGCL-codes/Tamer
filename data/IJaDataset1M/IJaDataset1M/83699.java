package com.shuetech.usermanagement.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.EJB;
import com.shuetech.general.data.EAOJPAHibernateBean;
import com.shuetech.general.entity.criteria.CriteriaImpl;
import com.shuetech.general.util.data.Node;
import com.shuetech.general.util.data.Tree;
import com.shuetech.usermanagement.Definitions.UserAccountDefinition;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserManagementBean implements UserManagement {

    @EJB
    private EAOJPAHibernateBean eao;

    public void populateRoles(Tree<String> roleTree) {
        Node<String> root = roleTree.getRootElement();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void populateRoleUsers(HashMap<String, List<UserAccountDefinition>> roleUsers) {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllRoles() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllUserAccounts() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteUserAccount(String username) {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean createUserAccount(String username, String password, String firstname, String lastname, String email) {
        return false;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean grantRoleToUser(String user, String role) {
        return false;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean revokeRoleFromUser(String user, String role) {
        return false;
    }
}
