package org.geonetwork.gaap.services;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.geonetwork.gaap.dao.PermissionDao;
import org.geonetwork.gaap.dao.GroupDao;
import org.geonetwork.gaap.dao.OperationDao;
import org.geonetwork.gaap.dao.UserDao;
import org.geonetwork.gaap.domain.operation.Permission;
import org.geonetwork.gaap.domain.operation.Operation;
import org.geonetwork.gaap.domain.user.User;
import org.geonetwork.gaap.domain.group.Group;
import java.util.List;

public abstract class AbstractServiceTest extends AbstractTransactionalSpringContextTests {

    public PermissionDao getPermissionDao() {
        return permissionDao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public OperationDao getOperationDao() {
        return operationDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    PermissionDao permissionDao;

    GroupDao groupDao;

    OperationDao operationDao;

    UserDao userDao;

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext-gn-services-gaap-test.xml", "applicationContext-gn-services-gaap.xml", "applicationContext-gn-persistence-gaap.xml" };
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setOperationDao(OperationDao operationDao) {
        this.operationDao = operationDao;
    }

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();
        List<Permission> permissions = permissionDao.loadAllPermissions();
        for (Permission p : permissions) {
            permissionDao.deletePermission(p);
        }
        List<User> users = userDao.loadAllUsers();
        for (User usr : users) {
            userDao.deleteUser(usr);
        }
        List<Group> groups = groupDao.loadAllGroups();
        for (Group gr : groups) {
            groupDao.deleteGroup(gr);
        }
        List<Operation> operations = operationDao.loadAllOperations();
        for (Operation op : operations) {
            operationDao.deleteOperation(op);
        }
    }
}
