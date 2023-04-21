package javax.print;

import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.QueuedJobCount;
import javax.print.attribute.standard.RequestingUserName;
import junit.framework.TestCase;

public class GetAttributeTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testGetAttribute() {
        PrintService[] services;
        Object probe;
        Class[] clazz = new Class[] { PrinterIsAcceptingJobs.ACCEPTING_JOBS.getCategory(), PrinterState.IDLE.getCategory(), QueuedJobCount.class, Destination.class, JobName.class, RequestingUserName.class };
        services = PrintServiceLookup.lookupPrintServices(null, null);
        TestUtil.checkServices(services);
        for (int i = 0, ii = services.length; i < ii; i++) {
            for (int j = 0, jj = clazz.length; j < jj; j++) {
                if (PrintServiceAttribute.class.isAssignableFrom(clazz[j])) {
                    probe = services[i].getAttribute(clazz[j]);
                    assertNotNull(probe);
                }
            }
            try {
                probe = services[i].getAttribute(null);
                fail("NullPointerException must be thrown - the category is null");
            } catch (NullPointerException e) {
            }
            try {
                Class<?> invalidClass = Copies.class;
                probe = services[i].getAttribute((Class<? extends PrintServiceAttribute>) invalidClass);
                fail("IllegalArgumentException must be thrown - category is not a Class that implements interface PrintServiceAttribute.");
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
