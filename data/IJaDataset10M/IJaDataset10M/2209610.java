package org.noranj.formak;

import static org.junit.Assert.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.jdo.JDOObjectNotFoundException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.noranj.formak.server.Startup;
import org.noranj.formak.server.domain.biz.PurchaseOrder;
import org.noranj.formak.server.domain.sa.SystemClientParty;
import org.noranj.formak.server.domain.sa.SystemUser;
import org.noranj.formak.server.service.SystemAdminServiceImpl;
import org.noranj.formak.shared.dto.ProfileDTO;
import org.noranj.formak.shared.dto.PurchaseOrderDTO;
import org.noranj.formak.shared.dto.SystemClientPartyDTO;
import org.noranj.formak.shared.dto.SystemUserDTO;
import org.noranj.formak.shared.dto.UserProfileDTO;
import org.noranj.formak.shared.exception.NotFoundException;
import org.noranj.formak.shared.type.ActivityType;
import org.noranj.formak.shared.type.DocumentStateType;
import org.noranj.formak.shared.type.DocumentType;
import org.noranj.formak.shared.type.PartyRoleType;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * 
 * 
 * This module, both source code and documentation, is in the Public Domain, and comes with NO WARRANTY.
 * See http://www.noranj.org for further information.
 *
 * @author
 */
public class SystemAdminServiceImplTest {

    /** if set to TRUE, it means there is no data in data store for the test and test data must be created.*/
    private boolean generateTestData = true;

    private static final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        helper.setUp();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        helper.tearDown();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetSystemUsers() throws Exception {
        if (generateTestData) {
            Startup.makeTestDataUserRetailerParty();
            Startup.makeTestDataUserManufacturerParty();
            generateTestData = false;
        }
        try {
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            SystemAdminServiceImpl service = new SystemAdminServiceImpl();
            List<SystemUserDTO> sysUserList = service.getSystemUsers(null);
            if (sysUserList != null) {
                for (SystemUserDTO dto : sysUserList) {
                    System.out.printf("*** System user [%s] found. email is [%s] and parentClient ID [%s]\r\n", dto.getFirstName(), dto.getFirstName(), dto.getId());
                }
            } else {
                fail("no user is found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("AN exception happened " + ex.getMessage());
        }
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
    }

    @Test
    public void testGetSystemUser() {
        try {
            System.out.println("--------------------------------------------------------------------");
            String emailAddress = "babak@noranj.com";
            SystemAdminServiceImpl service = new SystemAdminServiceImpl();
            SystemUserDTO sysUser = service.getSystemUser(emailAddress);
            if (sysUser != null) {
                System.out.printf("System user [%s] found. parentClient ID [%s]\r\n", emailAddress, sysUser.getParentClientId());
            } else {
                fail(String.format("User not found [%s]", emailAddress));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("AN exception happened " + ex.getMessage());
        }
        System.out.println("=====================================================================");
    }

    public void testCreateUser() {
        System.out.println("--------------------------------------------------------------------");
        try {
            Startup.makeTestDataUserRetailerParty();
            Startup.makeTestDataUserManufacturerParty();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("AN exception happened " + ex.getMessage());
        }
        System.out.println("=====================================================================");
    }

    public void testDeleteSystemUserByID() {
        try {
            System.out.println("--------------------------------------------------------------------");
            SystemAdminServiceImpl service = new SystemAdminServiceImpl();
            String emailAddress = "babak@noranj.com";
            SystemUserDTO bizDocs = service.getSystemUser(emailAddress);
            if (bizDocs != null) {
                fail("There is no user to delete!!!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("AN exception happened " + ex.getMessage());
        }
        System.out.println("=====================================================================");
    }
}
