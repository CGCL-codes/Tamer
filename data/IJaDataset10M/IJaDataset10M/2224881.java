package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Our first unit test using the Mock component
 *
 * @version $Revision: 38 $
 */
public class FirstMockTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.addComponent("jms", context.getComponent("seda"));
        return context;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("jms:topic:quote").to("mock:quote");
            }
        };
    }

    @Test
    public void testQuote() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testSameMessageArrived() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedBodiesReceived("Camel rocks");
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessages() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedBodiesReceivedInAnyOrder("Camel rocks", "Hello Camel");
        template.sendBody("jms:topic:quote", "Hello Camel");
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessagesOrdered() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedBodiesReceived("Hello Camel", "Camel rocks");
        template.sendBody("jms:topic:quote", "Hello Camel");
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testContains() throws Exception {
        MockEndpoint quote = getMockEndpoint("mock:quote");
        quote.expectedMessageCount(2);
        quote.allMessages().body().contains("Camel");
        template.sendBody("jms:topic:quote", "Hello Camel");
        template.sendBody("jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }
}
