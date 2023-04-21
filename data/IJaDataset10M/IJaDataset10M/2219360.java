package com.corratech.opensuite.services;

import static org.junit.Assert.fail;
import java.util.Date;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.corratech.opensuite.api.businesscomponent.BusinessComponent;
import com.corratech.opensuite.api.businesscomponent.BusinessComponentManagement;
import com.corratech.opensuite.api.businesscomponent.UserCredentials;
import com.corratech.opensuite.api.security.Group;
import com.corratech.opensuite.api.security.Role;
import com.corratech.opensuite.api.security.User;
import com.corratech.opensuite.api.security.UserManagement;

public class OpensuiteServiceFactoryTest {

    private static OpensuiteServiceFacade facade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        facade = OpensuiteServiceFacade.getInstance();
    }

    @SuppressWarnings("unused")
    @Test
    public void testGetUserManagement() {
        UserManagement userManagement = facade.getUserManagementService();
        List<User> allUsers = userManagement.getUsers();
        List<User> testUsers = userManagement.getUsers("r%");
        User user1 = userManagement.getUser("user1");
        User user2 = userManagement.getUser("user2");
        User rootUser = userManagement.getUser("root");
        rootUser.setPassword("pass2");
        userManagement.disableUser(rootUser);
        userManagement.enableUser(rootUser);
        userManagement.disableUser("root");
        userManagement.enableUser("root");
        User user4 = userManagement.createUser("user4", "pwd", new Date(), "user4_first_name", "", "user4_last_name", "");
        userManagement.removeUser("user4");
        List<Group> groups = userManagement.getGroups();
        List<Group> groupsByName = userManagement.getGroups("opensui");
        List<Role> roles = userManagement.getRoles();
    }

    public void testGetSingletoneInstance() {
        fail("Not yet implemented");
    }

    @SuppressWarnings("unused")
    public void testGetBusinessComponentManagment() {
        UserManagement userManagement = facade.getUserManagementService();
        BusinessComponentManagement bcManagment = facade.getBCManagementService();
        User rootUser = userManagement.getUser("root");
        BusinessComponent testBc1 = bcManagment.createBusinessComponent("TestBc1", "http://localhost/", -1, -1);
        bcManagment.removeBusinessComponent(testBc1.getBusinessComponentId());
        BusinessComponent testBc2 = bcManagment.createBusinessComponent("TestBc2", "http://localhost:8080/", -1, -1);
        bcManagment.removeBusinessComponent("TestBc2");
        BusinessComponent centric = bcManagment.getBusinessComponent("CentricCRM");
        UserCredentials uc = bcManagment.createUserCredentials(rootUser.getUserId(), "root_centric", "root_centric_pwd", centric.getBusinessComponentId());
        uc = bcManagment.getUserCredentialsById(uc.getCredentialsId());
        bcManagment.removeUserCredentialsById(uc.getCredentialsId());
        List<UserCredentials> ucList = bcManagment.getUserCredentialsList(centric.getBusinessComponentId());
        ucList = bcManagment.getUserCredentialsList(centric);
    }

    @SuppressWarnings("unused")
    public void testGetAclManagement() {
        fail("Not yet implemented");
    }
}
