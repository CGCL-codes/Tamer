package de.itar.logic;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import de.itar.logic.HjDesc;
import de.itar.logic.HjDescManager;

public class UT_HjDescManager {

    private static HjDescManager manager;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("UT_HjDescManager.beforeClass()");
        manager = new HjDescManager();
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("UT_HjDescManager.afterClass()");
        String[] hjDescNames = manager.getHjDescNames();
        for (String name : hjDescNames) {
            HjDesc hjDesc = manager.getHjDesc(name);
            assertNotNull(hjDesc);
            try {
                hjDesc.delete();
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    @Test
    public void test() {
    }
}
