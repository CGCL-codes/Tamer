package camelinaction;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * A custom producer.
 *
 * @version $Revision: 60 $
 */
public class ERPProducer extends DefaultProducer {

    public ERPProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public ERPEndpoint getEndpoint() {
        return (ERPEndpoint) super.getEndpoint();
    }

    public void process(Exchange exchange) throws Exception {
        String input = exchange.getIn().getBody(String.class);
        if (getEndpoint().isVerbose()) {
            System.out.println("Calling ERP with: " + input);
        }
        exchange.getOut().setBody("Simulated response from ERP");
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }
}
