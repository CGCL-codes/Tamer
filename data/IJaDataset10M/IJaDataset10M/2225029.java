package camelinaction;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * An example how to use Routing Slip EIP.
 * <p/>
 * This example invokes a bean which has been annotated with @RoutingSlip.
 *
 * @version $Revision: 176 $
 */
public class SpringRoutingSlipBeanTest extends CamelSpringTestSupport {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/routingslip-header.xml");
    }

    @Test
    public void testRoutingSlip() throws Exception {
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(0);
        getMockEndpoint("mock:c").expectedMessageCount(1);
        template.sendBody("direct:start", "Hello World");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRoutingSlipCool() throws Exception {
        getMockEndpoint("mock:a").expectedMessageCount(1);
        getMockEndpoint("mock:b").expectedMessageCount(1);
        getMockEndpoint("mock:c").expectedMessageCount(1);
        template.sendBody("direct:start", "We are Cool");
        assertMockEndpointsSatisfied();
    }
}
