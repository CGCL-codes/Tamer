package cn.myapps.core.fsp.ws;

import java.rmi.RemoteException;
import com.teemlink.fsp.ws.fault.FspWSException;
import com.teemlink.fsp.ws.org.service.FspOrgServiceServiceLocator;
import junit.framework.TestCase;
import cn.myapps.util.sequence.Sequence;
import cn.myapps.util.sequence.SequenceException;

public class FspOrgServiceTest extends TestCase {

    FspOrgServiceServiceLocator locator;

    com.teemlink.fsp.ws.org.service.FspOrgService service;

    protected void setUp() throws Exception {
        super.setUp();
        locator = new FspOrgServiceServiceLocator();
        service = locator.getOrgService();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAll() throws SequenceException, RemoteException {
        try {
            String invokerId = "01b98ff4-8c9e-aa40-b2f3-e326d40b8440";
            String domainId = Sequence.getSequence();
            service.createDomain(domainId, "银行A", "测试用银行", 0, invokerId);
            String userId0 = Sequence.getSequence();
            service.createUser(userId0, "用户A", "a", "123", "test@163.com", domainId, null, "020", invokerId);
            service.updateUser(userId0, "用户AA", "a", "123", "test@163.com", null, "020", invokerId);
            String userId1 = Sequence.getSequence();
            service.createUser(userId1, "用户A-1", "a1", "123", "test@163.com", domainId, userId0, "020", invokerId);
            String deptId0 = Sequence.getSequence();
            service.createDepartment(deptId0, "部门A", domainId, null, invokerId);
            String deptId1 = Sequence.getSequence();
            service.createDepartment(deptId1, "部门A-1", domainId, null, invokerId);
            String roleId = Sequence.getSequence();
            service.createRole(roleId, "角色A", invokerId);
            String[] deptIds = new String[] { deptId0 };
            service.addDeptsToUser(userId0, deptIds, invokerId);
            deptIds = new String[] { deptId1 };
            service.addDeptsToUser(userId0, deptIds, invokerId);
            service.removeDeptsFromUser(userId0, deptIds, invokerId);
            String[] roleIds = new String[] { roleId };
            service.addRolesToUser(userId0, roleIds, invokerId);
            service.removeRolesFromUser(userId0, roleIds, invokerId);
            service.addUsersToRole(roleId, new String[] { userId0, userId1 }, invokerId);
            service.removeUsersFromRole(roleId, new String[] { userId0, userId1 }, invokerId);
            service.addUsersToDept(deptId0, new String[] { userId0, userId1 }, invokerId);
            service.removeUsersFromDept(deptId0, new String[] { userId0, userId1 }, invokerId);
            service.removeUser(userId1, invokerId);
            service.removeUser(userId0, invokerId);
            service.removeDepartment(deptId1, invokerId);
            service.removeDepartment(deptId0, invokerId);
            service.removeDomain(domainId, invokerId);
            service.removeRole(roleId, invokerId);
        } catch (FspWSException e) {
            e.printStackTrace();
            System.out.println("Error Code: " + e.getErrorCode());
            assertTrue(false);
        }
    }
}
