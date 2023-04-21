package javax.print;

import javax.print.attribute.standard.JobStateReason;
import junit.framework.TestCase;

public class IsAttributeCategorySupportedTest extends TestCase {

    public void testIsAttributeCategorySupported() {
        System.out.println("============= START testIsAttributeCategorySupported ================");
        PrintService[] services;
        Class[] claz;
        boolean supported;
        services = PrintServiceLookup.lookupPrintServices(null, null);
        TestUtil.checkServices(services);
        for (int i = 0, ii = services.length; i < ii; i++) {
            System.out.println("------------------" + services[i].getName() + "-------------------");
            claz = services[i].getSupportedAttributeCategories();
            for (int j = 0, jj = claz.length; j < jj; j++) {
                supported = services[i].isAttributeCategorySupported(claz[j]);
                if (!supported) {
                    fail("Category " + claz[j] + " must be supported.");
                }
                System.out.println(claz[j] + ": " + supported);
            }
            supported = services[i].isAttributeCategorySupported(JobStateReason.ABORTED_BY_SYSTEM.getCategory());
            if (supported) {
                fail("Category " + JobStateReason.ABORTED_BY_SYSTEM.getCategory() + " must not be supported.");
            }
            System.out.println(JobStateReason.ABORTED_BY_SYSTEM.getCategory() + ": " + supported);
        }
        System.out.println("============= END testIsAttributeCategorySupported ================");
    }
}
