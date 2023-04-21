package camelinaction;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @version $Revision: 95 $
 */
public class SpringBigFileSedaTest extends CamelSpringTestSupport {

    @Test
    public void testBigFile() throws Exception {
        context.getShutdownStrategy().setTimeout(300);
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        context.stop();
        long delta = System.currentTimeMillis() - start;
        System.out.println("Took " + delta / 1000 + " seconds");
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/BigFileSedaTest.xml");
    }
}
