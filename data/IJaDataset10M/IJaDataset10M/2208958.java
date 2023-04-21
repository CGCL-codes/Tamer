package org.bluepitch.taskflow.service.account;

import org.bluepitch.taskflow.domain.entity.account.Authority;
import org.bluepitch.taskflow.domain.entity.account.Role;
import org.bluepitch.taskflow.domain.entity.account.User;
import org.bluepitch.taskflow.domain.manager.AccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bluepitch
 * Date: 2010-9-2
 * Time: 17:52:57
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountManager accountManager;

    @Override
    public void createAndStoreUser(User user) {
        accountManager.createAndStoreUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return accountManager.getUser(id);
    }

    @Override
    public void deleteUser(Long id) {
        accountManager.deleteUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLoginNameUnique(String newLoginName, String oldLoginName) {
        return accountManager.isLoginNameUnique(newLoginName, oldLoginName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRole() {
        return accountManager.getAllRole();
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRole(Long id) {
        return accountManager.getRole(id);
    }

    @Override
    public void saveRole(Role role) {
        accountManager.saveRole(role);
    }

    @Override
    public void deleteRole(Long id) {
        accountManager.deleteRole(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Authority> getAllAuthority() {
        return accountManager.getAllAuthority();
    }
}
