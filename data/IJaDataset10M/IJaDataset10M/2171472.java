package javax.print;

import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttributeSet;
import junit.framework.TestCase;

public class GetAttributesTest extends TestCase {

    public void testGetAttributes() {
        System.out.println("============= START testGetAttributes ================");
        PrintService[] services;
        PrintServiceAttributeSet as;
        Attribute[] aa;
        services = PrintServiceLookup.lookupPrintServices(null, null);
        TestUtil.checkServices(services);
        for (int i = 0, ii = services.length; i < ii; i++) {
            System.out.println("----" + services[i].getName() + "----");
            as = services[i].getAttributes();
            aa = as.toArray();
            for (int j = 0; j < aa.length; j++) {
                System.out.println(aa[j].getName() + ": " + aa[j].toString());
            }
        }
        System.out.println("============= END testGetAttributes ================");
    }
}
