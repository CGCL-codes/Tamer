package org.proclos.etlcore.datasource.ldap;

import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.proclos.etlcore.source.ldap.LDAPResultSet;

@RunWith(JMock.class)
public class TestLDAPResultSet extends TestCase {

    private Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Before
    public void setUp() {
        final InitialContextFactoryBuilder fb = context.mock(InitialContextFactoryBuilder.class);
        final InitialContextFactory f = context.mock(InitialContextFactory.class);
        final DirContext c = context.mock(DirContext.class);
        try {
            context.checking(new Expectations() {

                {
                    one(fb).createInitialContextFactory(with(any(Hashtable.class)));
                    will(returnValue(f));
                    one(f).getInitialContext(with(any(Hashtable.class)));
                    will(returnValue(c));
                    one(c).search(with(equal("")), with(equal("(&(|(uid=peter)(uid=chris))(objectClass=person)(objectClass=posixAccount)(objectClass=inetOrgPerson))")), with(any(javax.naming.directory.SearchControls.class)));
                }
            });
            NamingManager.setInitialContextFactoryBuilder(fb);
        } catch (NamingException e) {
            System.err.println("Caught:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() {
        Properties props = new Properties();
        props.setProperty(Context.PROVIDER_URL, "ldap://localhost:389/");
        props.setProperty("query", "(|(uid=peter)(uid=chris))");
        props.setProperty("objectclasses", "person posixAccount inetOrgPerson");
        props.setProperty("name", "test");
        LDAPResultSet lrs = new LDAPResultSet(props);
        lrs.execute();
    }
}
