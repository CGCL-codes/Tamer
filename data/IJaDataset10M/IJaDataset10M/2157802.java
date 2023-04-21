package net.sf.brightside.bizcom.metamodel.beans.serverSide;

import net.sf.brightside.bizcom.metamodel.beans.common.UserBean;
import junit.framework.TestCase;

public class StorageManagerBeanTest extends TestCase {

    private StorageManagerBean manager;

    private UserBean user, userActual;

    protected void setUp() {
        manager = StorageManagerBean.getInstance();
        user = new UserBean("someUser*127.0.0.1*0*some@email.com");
    }

    public void testManager() {
        assertEquals(StorageManagerBean.getInstance(), manager);
    }

    public void testGetUser() {
        manager.addUser("someUser*127.0.0.1*0*some@email.com");
        userActual = (UserBean) manager.getUser("0");
        assertEquals(user, userActual);
    }
}
