package camelinaction;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * A set of routes that watches a directory for new orders, reads them, converts the order 
 * file into a JMS Message and then sends it to the JMS incomingOrders queue hosted 
 * on an embedded ActiveMQ broker instance.
 * 
 * From there a content-based router is used to send the order to either the
 * xmlOrders or csvOrders queue. If an order file does not end with the
 * csv, csl, or xml extension the order is sent to the badOrders queue. 
 *
 * @author janstey
 *
 */
public class OrderRouterOtherwise {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() {
                from("file:src/data?noop=true").to("jms:incomingOrders");
                from("jms:incomingOrders").choice().when(header("CamelFileName").endsWith(".xml")).to("jms:xmlOrders").when(header("CamelFileName").regex("^.*(csv|csl)$")).to("jms:csvOrders").otherwise().to("jms:badOrders");
                from("jms:xmlOrders").process(new Processor() {

                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received XML order: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                });
                from("jms:csvOrders").process(new Processor() {

                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received CSV order: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                });
                from("jms:badOrders").process(new Processor() {

                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Received bad order: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                });
            }
        });
        context.start();
        Thread.sleep(10000);
        context.stop();
    }
}
