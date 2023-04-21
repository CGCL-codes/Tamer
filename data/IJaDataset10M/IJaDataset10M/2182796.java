package org.ddsteps.fixture.ldap.support;

import junit.framework.TestCase;
import org.ddsteps.dataset.bean.DataRowBean;
import org.ddsteps.dataset.bean.DataValueBean;
import org.easymock.MockControl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ldap.BadLdapGrammarException;
import org.springframework.ldap.EntryNotFoundException;
import org.springframework.ldap.LdapOperations;
import org.springframework.ldap.support.DistinguishedName;

public class TeardownLdapFixtureOperationTest extends TestCase {

    private MockControl ldapOperationsControl;

    private LdapOperations ldapOperationsMock;

    private TeardownLdapFixtureOperation tested;

    protected void setUp() throws Exception {
        super.setUp();
        ldapOperationsControl = MockControl.createControl(LdapOperations.class);
        ldapOperationsMock = (LdapOperations) ldapOperationsControl.getMock();
        tested = new TeardownLdapFixtureOperation();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        ldapOperationsControl = null;
        ldapOperationsMock = null;
        tested = null;
    }

    protected void replay() {
        ldapOperationsControl.replay();
    }

    protected void verify() {
        ldapOperationsControl.verify();
    }

    public void testHandleRow() {
        DataRowBean dataRow = new DataRowBean();
        dataRow.addValue(new DataValueBean("dn", "cn=Some Person, ou=company1, c=Sweden"));
        dataRow.addValue(new DataValueBean("objectclass", new String[] { "top", "person" }));
        dataRow.addValue(new DataValueBean("cn", "Some Person"));
        dataRow.addValue(new DataValueBean("sn", "Person"));
        dataRow.addValue(new DataValueBean("description", "Some description"));
        ldapOperationsMock.unbind(new DistinguishedName("cn=Some Person, ou=company1, c=Sweden"));
        replay();
        tested.handleRow(ldapOperationsMock, dataRow);
        verify();
    }

    public void testHandleRow_NoDn() {
        DataRowBean dataRow = new DataRowBean();
        dataRow.addValue(new DataValueBean("objectclass", new String[] { "top", "person" }));
        dataRow.addValue(new DataValueBean("cn", "Some Person"));
        dataRow.addValue(new DataValueBean("sn", "Person"));
        dataRow.addValue(new DataValueBean("description", "Some description"));
        replay();
        try {
            tested.handleRow(ldapOperationsMock, dataRow);
            fail("DataIntegrityViolationException expected");
        } catch (DataIntegrityViolationException expected) {
            assertTrue(true);
        }
        verify();
    }

    public void testHandleRow_InvalidDnDataType() {
        DataRowBean dataRow = new DataRowBean();
        dataRow.addValue(new DataValueBean("dn", Boolean.TRUE));
        replay();
        try {
            tested.handleRow(ldapOperationsMock, dataRow);
            fail("BadLdapGrammarException expected");
        } catch (BadLdapGrammarException expected) {
            assertTrue(true);
        }
        verify();
    }

    public void testHandleRow_NotFound() {
        DataRowBean dataRow = new DataRowBean();
        dataRow.addValue(new DataValueBean("dn", "cn=Some Person, ou=company1, c=Sweden"));
        ldapOperationsMock.unbind(new DistinguishedName("cn=Some Person, ou=company1, c=Sweden"));
        ldapOperationsControl.setThrowable(new EntryNotFoundException("dummy"));
        replay();
        tested.handleRow(ldapOperationsMock, dataRow);
        verify();
    }
}
