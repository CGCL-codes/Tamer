package com.ravi.agent;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ravi.taskman.TTTask;

public class TTTaskEndpoint extends DefaultEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(TTTaskEndpoint.class);

    /**
	 * creates a snmp endpoint
	 * 
	 * @param uri
	 *            the endpoint uri
	 * @param component
	 *            the component
	 */
    public TTTaskEndpoint(String uri, TTTaskComponent component) {
        super(uri, component);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new TTTaskConsumer(this, processor);
    }

    public Producer createProducer() throws Exception {
        return new TTTaskProducer(this);
    }

    public Exchange createExchange(TTTask task) {
        return createExchange(getExchangePattern(), task);
    }

    private Exchange createExchange(ExchangePattern pattern, TTTask task) {
        Exchange exchange = new DefaultExchange(this, pattern);
        DefaultMessage msg = new DefaultMessage();
        msg.setBody(task);
        exchange.setIn(msg);
        return exchange;
    }

    public boolean isSingleton() {
        return true;
    }
}
