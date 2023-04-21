package net.taylor.portal.entity.resourcebundle;

import javax.persistence.EntityManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.testing.SeamCrudTest;

/**
 * Unit tests for the Resource Seam components.
 * 
 * @author jgilbert
 * @generated
 */
public class ResourceSeamTest extends SeamCrudTest<Resource> {

    /** @generated */
    public ResourceSeamTest(String name) {
        super(name, "java:/taylor-portal", 20);
    }

    /** @generated */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ResourceSeamTest("testCrud"));
        return new Bootstrap(suite);
    }

    /** @generated */
    public void initData(EntityManager em) {
        addValue("key", "key", false);
        addValue("value", "value", false);
        addList("resourceBundleComboBox.filter('')");
        addChart("resourceBundlePieChart");
    }
}
