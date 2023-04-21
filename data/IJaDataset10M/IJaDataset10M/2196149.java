package net.sf.brightside.bizcom.metamodel.spring;

import org.springframework.context.ApplicationContext;
import net.sf.brightside.bizcom.core.spring.ApplicationContextProvider;
import net.sf.brightside.bizcom.core.spring.ApplicationContextProviderSingleton;
import net.sf.brightside.bizcom.metamodel.User;
import net.sf.brightside.bizcom.metamodel.beans.UserBean;
import junit.framework.TestCase;

public class UserBeanTest extends TestCase {

    private ApplicationContextProvider provider;

    private ApplicationContext context;

    private User userUnderTest;

    private User adHocUser;

    protected void setUp() throws Exception {
        provider = new ApplicationContextProviderSingleton();
        context = provider.createContext();
        assertNotNull(context);
        userUnderTest = (User) context.getBean("net.sf.brightside.bizcom.metamodel.User");
        assertNotNull(userUnderTest);
        adHocUser = new UserBean("TestName");
    }

    public void testGetName() {
        assertEquals("TestName", userUnderTest.getName());
    }

    public void testSetName() {
        userUnderTest.setName("TestName");
        assertEquals("TestName", userUnderTest.getName());
    }

    public void testEquals() {
        assertEquals(true, userUnderTest.equals(adHocUser));
    }
}
