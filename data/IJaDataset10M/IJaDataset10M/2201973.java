package org.apache.harmony.jndi.tests.javax.naming.spi;

import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import org.apache.harmony.jndi.tests.javax.naming.spi.mock.InvokeRecord;
import org.apache.harmony.jndi.tests.javax.naming.spi.mock.MockInitialContextFactoryBuilder;
import junit.framework.TestCase;

public class NamingManagerExploreTest extends TestCase {

    public void testFactoryBuilder() throws IllegalStateException, SecurityException, NamingException {
        if (!NamingManager.hasInitialContextFactoryBuilder()) {
            InitialContextFactoryBuilder contextFactoryBuilder = MockInitialContextFactoryBuilder.getInstance();
            NamingManager.setInitialContextFactoryBuilder(contextFactoryBuilder);
        }
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(Context.URL_PKG_PREFIXES, "org.apache.harmony.jndi.tests.javax.naming.spi.mock");
        MyInitialContext context = new MyInitialContext(env);
        Context urlContext = NamingManager.getURLContext("http", env);
        assertEquals("http", urlContext.getEnvironment().get("url.schema"));
        String name = "http://www.apache.org";
        String obj = "String object";
        context.bind(name, obj);
        assertNull(InvokeRecord.getLatestUrlSchema());
    }

    public void testFactoryBuilder_name() throws IllegalStateException, SecurityException, NamingException {
        if (!NamingManager.hasInitialContextFactoryBuilder()) {
            InitialContextFactoryBuilder contextFactoryBuilder = MockInitialContextFactoryBuilder.getInstance();
            NamingManager.setInitialContextFactoryBuilder(contextFactoryBuilder);
        }
        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(Context.URL_PKG_PREFIXES, "org.apache.harmony.jndi.tests.javax.naming.spi.mock");
        MyInitialContext context = new MyInitialContext(env);
        Context urlContext = NamingManager.getURLContext("http", env);
        assertEquals("http", urlContext.getEnvironment().get("url.schema"));
        Name name = new CompositeName("http://www.apache.org");
        String obj = "Name object";
        context.bind(name, obj);
        assertNull(InvokeRecord.getLatestUrlSchema());
    }

    class MyInitialContext extends InitialContext {

        public MyInitialContext(Hashtable<?, ?> environment) throws NamingException {
            super(environment);
        }

        public Context getDefaultContext() {
            return this.defaultInitCtx;
        }
    }
}
