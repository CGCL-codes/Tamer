package camelinaction.component;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The HelloWorld producer.
 */
public class MyProducer extends DefaultProducer {

    private static final transient Log LOG = LogFactory.getLog(MyProducer.class);

    private MyEndpoint endpoint;

    public MyProducer(MyEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());
    }
}
