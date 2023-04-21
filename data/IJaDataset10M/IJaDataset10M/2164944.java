package net.sf.ldaptemplate.support;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Integration tests for ContextSourceImpl.
 * 
 * @author Mattias Arthursson
 */
public class LdapContextSourcelITest extends AbstractDependencyInjectionSpringContextTests {

    private LdapContextSource tested;

    protected String[] getConfigLocations() {
        return new String[] { "/conf/ldapTemplateTestContext.xml" };
    }

    public void testGetReadOnlyContext() throws NamingException {
        DirContext ctx = null;
        try {
            ctx = tested.getReadOnlyContext();
            assertNotNull(ctx);
            Hashtable environment = ctx.getEnvironment();
            assertTrue(environment.containsKey(LdapContextSource.SUN_LDAP_POOLING_FLAG));
            assertFalse(environment.containsKey(Context.SECURITY_PRINCIPAL));
            assertFalse(environment.containsKey(Context.SECURITY_CREDENTIALS));
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void testGetReadWriteContext() throws NamingException {
        DirContext ctx = null;
        try {
            ctx = tested.getReadWriteContext();
            assertNotNull(ctx);
            Hashtable environment = ctx.getEnvironment();
            assertTrue(environment.containsKey(Context.SECURITY_PRINCIPAL));
            assertTrue(environment.containsKey(Context.SECURITY_CREDENTIALS));
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void setTested(LdapContextSource tested) {
        this.tested = tested;
    }
}
