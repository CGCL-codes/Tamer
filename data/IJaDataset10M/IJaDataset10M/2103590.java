package org.openuss.security;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openuss.security.acegi.ldap.ActiveDirectoryBindAuthenticatorTest;
import org.openuss.security.acegi.ldap.ActiveDirectoryLdapTemplateTest;
import org.openuss.security.acegi.ldap.ConfigurableLdapAuthenticationProviderTest;
import org.openuss.security.acegi.ldap.ExtendedLdapUserDetailsMapperTest;
import org.openuss.security.acegi.shibboleth.PlexusShibbolethIntegrationTest;
import org.openuss.security.acegi.shibboleth.ShibbolethAuthenticationProcessingFilterTest;
import org.openuss.security.acegi.shibboleth.ShibbolethAuthenticationProviderTest;
import org.openuss.security.acl.AclPermissionIntegrationTest;
import org.openuss.security.acl.ObjectIdentityDaoTest;
import org.openuss.security.acl.PermissionDaoTest;
import org.openuss.security.ldap.*;

public class SecurityTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.openuss.security");
        suite.addTestSuite(GroupDaoTest.class);
        suite.addTestSuite(GroupImplTest.class);
        suite.addTestSuite(SecurityServiceIntegrationTest.class);
        suite.addTestSuite(UserDaoTest.class);
        suite.addTestSuite(UserDetailsServiceAdapterTest.class);
        suite.addTestSuite(UserImplTest.class);
        suite.addTestSuite(AclPermissionIntegrationTest.class);
        suite.addTestSuite(ObjectIdentityDaoTest.class);
        suite.addTestSuite(PermissionDaoTest.class);
        suite.addTestSuite(AttributeMappingDaoTest.class);
        suite.addTestSuite(AuthenticationDomainDaoTest.class);
        suite.addTestSuite(LdapConfigurationNotifyServiceIntegrationTest.class);
        suite.addTestSuite(LdapConfigurationServiceIntegrationTest.class);
        suite.addTestSuite(LdapServerDaoTest.class);
        suite.addTestSuite(RoleAttributeKeyDaoTest.class);
        suite.addTestSuite(UserDnPatternDaoTest.class);
        suite.addTestSuite(ActiveDirectoryBindAuthenticatorTest.class);
        suite.addTestSuite(ActiveDirectoryLdapTemplateTest.class);
        suite.addTestSuite(ConfigurableLdapAuthenticationProviderTest.class);
        suite.addTestSuite(ExtendedLdapUserDetailsMapperTest.class);
        suite.addTestSuite(PlexusShibbolethIntegrationTest.class);
        suite.addTestSuite(ShibbolethAuthenticationProcessingFilterTest.class);
        suite.addTestSuite(ShibbolethAuthenticationProviderTest.class);
        return suite;
    }
}
