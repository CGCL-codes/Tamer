package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import junit.framework.TestCase;
import org.easymock.MockControl;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 08.07.2003
 */
public class JndiTemplateTests extends TestCase {

    public void testLookupSucceeds() throws Exception {
        Object o = new Object();
        String name = "foo";
        MockControl mc = MockControl.createControl(Context.class);
        final Context mock = (Context) mc.getMock();
        mock.lookup(name);
        mc.setReturnValue(o);
        mock.close();
        mc.setVoidCallable(1);
        mc.replay();
        JndiTemplate jt = new JndiTemplate() {

            protected Context createInitialContext() throws NamingException {
                return mock;
            }
        };
        Object o2 = jt.lookup(name);
        assertEquals(o, o2);
        mc.verify();
    }

    public void testLookupFails() throws Exception {
        NamingException ne = new NamingException();
        String name = "foo";
        MockControl mc = MockControl.createControl(Context.class);
        final Context mock = (Context) mc.getMock();
        mock.lookup(name);
        mc.setThrowable(ne);
        mock.close();
        mc.setVoidCallable(1);
        mc.replay();
        JndiTemplate jt = new JndiTemplate() {

            protected Context createInitialContext() throws NamingException {
                return mock;
            }
        };
        try {
            jt.lookup(name);
            fail("Should have thrown NamingException");
        } catch (NamingException ex) {
        }
        mc.verify();
    }

    public void testBind() throws Exception {
        Object o = new Object();
        String name = "foo";
        MockControl mc = MockControl.createControl(Context.class);
        final Context mock = (Context) mc.getMock();
        mock.bind(name, o);
        mc.setVoidCallable(1);
        mock.close();
        mc.setVoidCallable(1);
        mc.replay();
        JndiTemplate jt = new JndiTemplate() {

            protected Context createInitialContext() throws NamingException {
                return mock;
            }
        };
        jt.bind(name, o);
        mc.verify();
    }

    public void testRebind() throws Exception {
        Object o = new Object();
        String name = "foo";
        MockControl mc = MockControl.createControl(Context.class);
        final Context mock = (Context) mc.getMock();
        mock.rebind(name, o);
        mc.setVoidCallable(1);
        mock.close();
        mc.setVoidCallable(1);
        mc.replay();
        JndiTemplate jt = new JndiTemplate() {

            protected Context createInitialContext() throws NamingException {
                return mock;
            }
        };
        jt.rebind(name, o);
        mc.verify();
    }

    public void testUnbind() throws Exception {
        String name = "something";
        MockControl mc = MockControl.createControl(Context.class);
        final Context mock = (Context) mc.getMock();
        mock.unbind(name);
        mc.setVoidCallable(1);
        mock.close();
        mc.setVoidCallable(1);
        mc.replay();
        JndiTemplate jt = new JndiTemplate() {

            protected Context createInitialContext() throws NamingException {
                return mock;
            }
        };
        jt.unbind(name);
        mc.verify();
    }
}
