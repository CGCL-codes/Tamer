package org.sempere.commons.naming;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Hashtable;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests class for DefaultNamingManager class.
 * 
 * @author bsempere
 */
public class DefaultNamingManagerTest {

    private DefaultNamingManager manager;

    private Context context;

    @Before
    public void before() throws Exception {
        this.context = mock(Context.class);
        this.manager = new DefaultNamingManager(this.context);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void lookupWhenJndiNameDoesNotExistShouldThrowException() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalObject";
        when(this.context.lookup(jndiName)).thenThrow(new NamingException());
        this.manager.lookup(jndiName);
    }

    @Test
    public void lookupWhenJndiNameExistsShouldReturnObjectReference() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalObject";
        Object expectedObject = "MyObject";
        when(this.context.lookup(jndiName)).thenReturn(expectedObject);
        Object actualObject = this.manager.lookup(jndiName);
        assertSame(expectedObject, actualObject);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getLocalObjectWhenJndiNameDoesNotExistShouldThrowException() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalObject";
        when(this.context.lookup(jndiName)).thenThrow(new NamingException());
        this.manager.getLocalObject(jndiName);
    }

    @Test
    public void getLocalObjectWhenJndiNameExistsShouldReturnObjectReference() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalObject";
        Object expectedObject = "MyObject";
        when(this.context.lookup(jndiName)).thenReturn(expectedObject);
        Object actualObject = this.manager.getLocalObject(jndiName);
        assertSame(expectedObject, actualObject);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getLocalEJBHomeWhenJndiNameDoesNotExistShouldThrowException() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalEJBHome";
        when(this.context.lookup(jndiName)).thenThrow(new NamingException());
        this.manager.getLocalEJBHome(jndiName);
    }

    @Test
    public void getLocalEJBHomeWhenJndiNameExistsShouldReturnObjectReference() throws Exception {
        String jndiName = "org.sempere.jee.ejb.session.MyLocalEJBHome";
        EJBLocalHome expectedEJBLocalHome = mock(EJBLocalHome.class);
        when(this.context.lookup(jndiName)).thenReturn(expectedEJBLocalHome);
        EJBLocalHome actualEJBLocalHome = this.manager.getLocalEJBHome(jndiName);
        assertSame(expectedEJBLocalHome, actualEJBLocalHome);
    }

    @Test
    public void getEnvironementWhenItIsNullShouldReturnAnEmptyHashtable() throws Exception {
        assertTrue("Environment should be empty.", this.manager.getEnvironment().isEmpty());
    }

    @Test
    public void getEnvironementWhenItIsNotNullShouldReturnIt() throws Exception {
        Hashtable<String, String> expectedEnvironment = new Hashtable<String, String>();
        expectedEnvironment.put(Context.PROVIDER_URL, "t3://localhost:9001");
    }
}
