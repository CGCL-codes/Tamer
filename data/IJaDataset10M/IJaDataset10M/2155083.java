package org.acegisecurity.vote;

import java.util.List;
import java.util.Vector;
import junit.framework.TestCase;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.providers.TestingAuthenticationToken;

/**
 * Tests {@link UnanimousBased}.
 *
 * @author Ben Alex
 * @version $Id: UnanimousBasedTests.java,v 1.6 2005/11/30 01:23:34 benalex Exp $
 */
public class UnanimousBasedTests extends TestCase {

    public UnanimousBasedTests() {
        super();
    }

    public UnanimousBasedTests(String arg0) {
        super(arg0);
    }

    public final void setUp() throws Exception {
        super.setUp();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UnanimousBasedTests.class);
    }

    public void testOneAffirmativeVoteOneDenyVoteOneAbstainVoteDeniesAccess() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("ROLE_1"));
        config.addConfigAttribute(new SecurityConfig("DENY_FOR_SURE"));
        try {
            mgr.decide(auth, new Object(), config);
            fail("Should have thrown AccessDeniedException");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testOneAffirmativeVoteTwoAbstainVotesGrantsAccess() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("ROLE_2"));
        mgr.decide(auth, new Object(), config);
        assertTrue(true);
    }

    public void testOneDenyVoteTwoAbstainVotesDeniesAccess() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("ROLE_WE_DO_NOT_HAVE"));
        try {
            mgr.decide(auth, new Object(), config);
            fail("Should have thrown AccessDeniedException");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testRoleVoterPrefixObserved() throws Exception {
        TestingAuthenticationToken auth = makeTestTokenWithFooBarPrefix();
        UnanimousBased mgr = makeDecisionManagerWithFooBarPrefix();
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("FOOBAR_1"));
        config.addConfigAttribute(new SecurityConfig("FOOBAR_2"));
        mgr.decide(auth, new Object(), config);
        assertTrue(true);
    }

    public void testThreeAbstainVotesDeniesAccessWithDefault() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        assertTrue(!mgr.isAllowIfAllAbstainDecisions());
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("IGNORED_BY_ALL"));
        try {
            mgr.decide(auth, new Object(), config);
            fail("Should have thrown AccessDeniedException");
        } catch (AccessDeniedException expected) {
            assertTrue(true);
        }
    }

    public void testThreeAbstainVotesGrantsAccessWithoutDefault() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        mgr.setAllowIfAllAbstainDecisions(true);
        assertTrue(mgr.isAllowIfAllAbstainDecisions());
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("IGNORED_BY_ALL"));
        mgr.decide(auth, new Object(), config);
        assertTrue(true);
    }

    public void testTwoAffirmativeVotesTwoAbstainVotesGrantsAccess() throws Exception {
        TestingAuthenticationToken auth = makeTestToken();
        UnanimousBased mgr = makeDecisionManager();
        ConfigAttributeDefinition config = new ConfigAttributeDefinition();
        config.addConfigAttribute(new SecurityConfig("ROLE_1"));
        config.addConfigAttribute(new SecurityConfig("ROLE_2"));
        mgr.decide(auth, new Object(), config);
        assertTrue(true);
    }

    private UnanimousBased makeDecisionManager() {
        UnanimousBased decisionManager = new UnanimousBased();
        RoleVoter roleVoter = new RoleVoter();
        DenyVoter denyForSureVoter = new DenyVoter();
        DenyAgainVoter denyAgainForSureVoter = new DenyAgainVoter();
        List voters = new Vector();
        voters.add(roleVoter);
        voters.add(denyForSureVoter);
        voters.add(denyAgainForSureVoter);
        decisionManager.setDecisionVoters(voters);
        return decisionManager;
    }

    private UnanimousBased makeDecisionManagerWithFooBarPrefix() {
        UnanimousBased decisionManager = new UnanimousBased();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("FOOBAR_");
        DenyVoter denyForSureVoter = new DenyVoter();
        DenyAgainVoter denyAgainForSureVoter = new DenyAgainVoter();
        List voters = new Vector();
        voters.add(roleVoter);
        voters.add(denyForSureVoter);
        voters.add(denyAgainForSureVoter);
        decisionManager.setDecisionVoters(voters);
        return decisionManager;
    }

    private TestingAuthenticationToken makeTestToken() {
        return new TestingAuthenticationToken("somebody", "password", new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_1"), new GrantedAuthorityImpl("ROLE_2") });
    }

    private TestingAuthenticationToken makeTestTokenWithFooBarPrefix() {
        return new TestingAuthenticationToken("somebody", "password", new GrantedAuthority[] { new GrantedAuthorityImpl("FOOBAR_1"), new GrantedAuthorityImpl("FOOBAR_2") });
    }
}
