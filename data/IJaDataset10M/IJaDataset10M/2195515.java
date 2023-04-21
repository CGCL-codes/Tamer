package org.broadleafcommerce.profile.extensibility;

import org.broadleafcommerce.profile.extensibility.context.MergeClassPathXMLApplicationContext;
import org.broadleafcommerce.test.BaseTest;
import org.testng.annotations.Test;

/**
 *
 * @author jfischer
 *
 */
public class ExtensibilityTest extends BaseTest {

    @Test
    public void test() {
        try {
            MergeClassPathXMLApplicationContext test = new MergeClassPathXMLApplicationContext(new String[] { "org/broadleafcommerce/extensibility/base/applicationContext-src.xml", "org/broadleafcommerce/extensibility/base/applicationContext-src2.xml" }, new String[] { "org/broadleafcommerce/extensibility/override/applicationContext-patch1.xml", "org/broadleafcommerce/extensibility/override/applicationContext-patch2.xml" }, null);
            ExtensibilityTestBean srcBean = (ExtensibilityTestBean) test.getBean("test3");
            if (!srcBean.getTestProperty().equals("test1")) {
                assert false;
            }
            ExtensibilityTestBean bean1 = (ExtensibilityTestBean) test.getBean("test");
            if (!bean1.getTestProperty().equals("test") || !bean1.getTestProperty2().equals("test2")) {
                assert false;
            }
            ExtensibilityTestBean3 bean2 = (ExtensibilityTestBean3) test.getBean("test2");
            if (!bean2.getTestProperty().equals("new") || !bean2.getTestProperty2().equals("none2") || !bean2.getTestProperty3().equals("none3")) {
                assert false;
            }
        } catch (Exception e) {
            logger.error(e);
            assert false;
        }
    }
}
