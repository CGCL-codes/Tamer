package tests.ObjectManagerTests;

import org.junit.Assert;
import org.junit.Test;
import org.sprutframework.DaoManager;
import org.sprutframework.exceptions.SprutFrameworkException;
import org.sprutframework.tests.ObjectManagerTest;

public class ObjectManagerTests {

    @Test
    public void test() throws SprutFrameworkException {
        DaoManager objManager = new DaoManager();
        objManager.initDaos();
        ObjectManagerTest obj = (ObjectManagerTest) objManager.getDao("testDao");
        Assert.assertTrue(obj.testMethod());
    }
}
