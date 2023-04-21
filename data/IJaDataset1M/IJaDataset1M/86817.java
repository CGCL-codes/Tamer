package org.acegisecurity.vote;

import junit.framework.TestCase;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import java.util.List;
import java.util.Vector;

/**
 * Tests {@link AbstractAccessDecisionManager}.
 *
 * @author Ben Alex
 * @version $Id: AbstractAccessDecisionManagerTests.java,v 1.3 2005/11/17 00:55:48 benalex Exp $
 */
public class AbstractAccessDecisionManagerTests extends TestCase {

    public AbstractAccessDecisionManagerTests() {
        super();
    }

    public AbstractAccessDecisionManagerTests(String arg0) {
        super(arg0);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AbstractAccessDecisionManagerTests.class);
    }

    public void testAllowIfAccessDecisionManagerDefaults() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        assertTrue(!mock.isAllowIfAllAbstainDecisions());
        mock.setAllowIfAllAbstainDecisions(true);
        assertTrue(mock.isAllowIfAllAbstainDecisions());
    }

    public void testDelegatesSupportsClassRequests() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        List list = new Vector();
        list.add(new DenyVoter());
        list.add(new MockStringOnlyVoter());
        mock.setDecisionVoters(list);
        assertTrue(mock.supports(new String().getClass()));
        assertTrue(!mock.supports(new Integer(7).getClass()));
    }

    public void testDelegatesSupportsRequests() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        List list = new Vector();
        DenyVoter voter = new DenyVoter();
        DenyAgainVoter denyVoter = new DenyAgainVoter();
        list.add(voter);
        list.add(denyVoter);
        mock.setDecisionVoters(list);
        ConfigAttribute attr = new SecurityConfig("DENY_AGAIN_FOR_SURE");
        assertTrue(mock.supports(attr));
        ConfigAttribute badAttr = new SecurityConfig("WE_DONT_SUPPORT_THIS");
        assertTrue(!mock.supports(badAttr));
    }

    public void testProperlyStoresListOfVoters() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        List list = new Vector();
        DenyVoter voter = new DenyVoter();
        DenyAgainVoter denyVoter = new DenyAgainVoter();
        list.add(voter);
        list.add(denyVoter);
        mock.setDecisionVoters(list);
        assertEquals(list.size(), mock.getDecisionVoters().size());
    }

    public void testRejectsEmptyList() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        List list = new Vector();
        try {
            mock.setDecisionVoters(list);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testRejectsListContainingInvalidObjectTypes() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        List list = new Vector();
        DenyVoter voter = new DenyVoter();
        DenyAgainVoter denyVoter = new DenyAgainVoter();
        String notAVoter = "NOT_A_VOTER";
        list.add(voter);
        list.add(notAVoter);
        list.add(denyVoter);
        try {
            mock.setDecisionVoters(list);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testRejectsNullVotersList() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        try {
            mock.setDecisionVoters(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    public void testRoleVoterAlwaysReturnsTrueToSupports() {
        RoleVoter rv = new RoleVoter();
        assertTrue(rv.supports(String.class));
    }

    public void testWillNotStartIfDecisionVotersNotSet() throws Exception {
        MockDecisionManagerImpl mock = new MockDecisionManagerImpl();
        try {
            mock.afterPropertiesSet();
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }

    private class MockDecisionManagerImpl extends AbstractAccessDecisionManager {

        public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config) throws AccessDeniedException {
            return;
        }
    }

    private class MockStringOnlyVoter implements AccessDecisionVoter {

        public boolean supports(Class clazz) {
            if (String.class.isAssignableFrom(clazz)) {
                return true;
            } else {
                return false;
            }
        }

        public boolean supports(ConfigAttribute attribute) {
            throw new UnsupportedOperationException("mock method not implemented");
        }

        public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
            throw new UnsupportedOperationException("mock method not implemented");
        }
    }
}
