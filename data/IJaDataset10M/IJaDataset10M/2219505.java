package org.jmad.adapters;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jmad.RegistrationInfo;
import org.jmad.Role;
import org.jmad.TestConfiguration;
import org.jmad.Thumbnail;
import org.jmad.User;
import org.jmad.UserGroup;
import org.jmad.dao.hibernate.HibernateRoleDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserXMLAdapterTest {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserXMLAdapterTest.class);

    private User simpleuser;

    private List<Role> roles;

    private List<User> testUsers;

    public UserXMLAdapterTest() {
        TestConfiguration.getInstance();
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        ThumbnailImageAdapter adapter = new ThumbnailImageAdapter();
        String filename = TestConfiguration.getTestDataPath() + "/landDrilling.jpg";
        Thumbnail th = adapter.read(filename);
        simpleuser = new User(null, "liberrocal");
        simpleuser.setPassword("kilopoi");
        simpleuser.setFirstName("Linnetti");
        simpleuser.setLastName("Berrocal");
        simpleuser.setPasswordChangeDate(new Date());
        RegistrationInfo ri = RegistrationInfo.getInstance(new Long(1));
        simpleuser.setRegistrationInfo(ri);
        simpleuser.setForceChangePassword(false);
        simpleuser.setIcon(th);
        simpleuser.addRole(roles.get(0));
        testUsers = TestConfiguration.buildTransientTestUsers();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWriteUserWithGroup() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String path = TestConfiguration.getOutputPath();
        UserXMLAdapter adapter = new UserXMLAdapter();
        UserGroup group1 = new UserGroup(new Long(1), "Grupo 1");
        group1.setRegistrationInfo(RegistrationInfo.getInstance(new Long(1)));
        UserGroup group2 = new UserGroup(new Long(3), "Grupo 3");
        group2.setRegistrationInfo(RegistrationInfo.getInstance(new Long(1)));
        group1.addMember(this.testUsers.get(0));
        group1.addMember(this.testUsers.get(1));
        group2.addMember(this.testUsers.get(2));
        group2.addMember(this.testUsers.get(3));
        try {
            for (User u : this.testUsers) {
                String filename = String.format("%s\\User_%s_%s.%s", path, u.getUsername(), sdf.format(new Date()), "xml");
                adapter.write(filename, u);
            }
        } catch (AdapterException e) {
            String msg = "%s: %s";
            msg = String.format(msg, e.getClass().getName(), e.getMessage());
            logger.error(msg);
            fail(msg);
        }
    }

    @Test
    public void testWriteRead() {
        ThumbnailImageAdapter thadapter = new ThumbnailImageAdapter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String path = TestConfiguration.getOutputPath();
        String filename = String.format("%s\\User_%s.%s", path, sdf.format(new Date()), "xml");
        String ifilename = String.format("%s\\UserImage_%s.%s", path, sdf.format(new Date()), "jpg");
        UserXMLAdapter adapter = new UserXMLAdapter();
        try {
            adapter.write(filename, this.simpleuser);
        } catch (AdapterException e) {
            String msg = "%s: %s";
            msg = String.format(msg, e.getClass().getName(), e.getMessage());
            logger.error(msg);
            fail(msg);
        }
        try {
            User u = adapter.read(filename);
            assertEquals(this.simpleuser.getFirstName(), u.getFirstName());
            assertEquals(this.simpleuser.getIcon().getImageBytes().length, u.getIcon().getImageBytes().length);
            thadapter.write(ifilename, u.getIcon());
        } catch (AdapterException e) {
            String msg = "%s: %s";
            msg = String.format(msg, e.getClass().getName(), e.getMessage());
            logger.error(msg);
            fail(msg);
        }
    }
}
