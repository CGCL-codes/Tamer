package camelinaction;

import java.io.File;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision: 27 $
 */
public class SpringOrderToCsvBeanTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("camelinaction/SpringOrderToCsvBeanTest.xml");
    }

    @Test
    public void testOrderToCsvBean() throws Exception {
        String inhouse = "0000005555000001144120091209  2319@1108";
        template.sendBodyAndHeader("direct:start", inhouse, "Date", "20091209");
        File file = new File("target/orders/received/report-20091209.csv");
        assertTrue("File should exist", file.exists());
        String body = context.getTypeConverter().convertTo(String.class, file);
        assertEquals("000000555,20091209,000001144,2319,1108", body);
    }
}
