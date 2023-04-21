package org.openjf.ldap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.openjf.usergroup.Groupx;
import org.openjf.usergroup.User;
import junit.framework.TestCase;

public class LdapTest extends TestCase {

    private Ldap ldap;

    private LdapSettings ldapSettings;

    private Object mocks[];

    protected void setUp() throws Exception {
        super.setUp();
        ldapSettings = createMock(LdapSettings.class);
        ldap = new Ldap(ldapSettings);
        mocks = new Object[] { ldapSettings };
    }

    private void initializeServerSettings() {
        expect(ldapSettings.getLdapUrl()).andReturn("ldap://SBCDF003");
        expect(ldapSettings.getLdapPrincipals()).andReturn("BCDF001\\v_frm");
        expect(ldapSettings.getLdapCredentials()).andReturn("4k1eJ7gJUH3Y");
        expect(ldapSettings.getUsersBaseDn()).andReturn("ou=Usuarios,DC=bc").anyTimes();
        expect(ldapSettings.getFindUserQuery()).andReturn("(sAMAccountName={0})").anyTimes();
        expect(ldapSettings.getGroupsBaseDn()).andReturn("ou=Security Groups,DC=bc").anyTimes();
        expect(ldapSettings.getFindGroupQuery()).andReturn("(sAMAccountName={0})").anyTimes();
        expect(ldapSettings.getUserLoginLdapAttr()).andReturn("sAMAccountName").anyTimes();
    }

    public void testGetUserDN() {
        initializeServerSettings();
        replay(mocks);
        ldap.start();
        assertEquals("ou=Usuarios,DC=bc,CN=Cristiano Kliemann,OU=DF,OU=DEINF", ldap.getUserDN("deinf.crk"));
        verify(mocks);
    }

    public void testGetGroupDN() {
        fail("Not yet implemented");
    }

    public void testFindGroup() {
        fail("Not yet implemented");
    }

    public void testFindUser() {
        fail("Not yet implemented");
    }

    public void testListUserGroups() {
        initializeServerSettings();
        replay(mocks);
        ldap.start();
        User user = new User();
        user.setId(1);
        user.setLogin("deinf.crk");
        System.out.println(ldap.listUserGroups(user));
        verify(mocks);
        fail("Not yet implemented");
    }

    public void testListGroupUsers() {
        initializeServerSettings();
        replay(mocks);
        ldap.start();
        Groupx group = new Groupx();
        group.setId(1);
        group.setName("@DEINFDIMADSUARQ");
        System.out.println(ldap.listGroupUsers(group));
        verify(mocks);
        fail("Not yet implemented");
    }
}
