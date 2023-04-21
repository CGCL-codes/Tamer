package org.apache.servicemix.audit.async;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.Fault;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.messaging.MessageExchange.Role;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.xml.namespace.QName;
import org.apache.servicemix.jbi.event.ExchangeListener;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.jbi.messaging.FaultImpl;
import org.apache.servicemix.jbi.messaging.NormalizedMessageImpl;

/**
 * {@link ExchangeListener} implementation that serializes the entire message exchange, except the message content.
 * 
 * This allows to serialize message exchange information without having to consume the message content stream
 */
public class NoBodyMessageExchangeSerializer implements ExchangeSerializer<MessageExchange> {

    private enum Status {

        ACTIVE, DONE, ERROR
    }

    public MessageExchange serialize(MessageExchange exchange) {
        MessageExchangeImpl result = new MessageExchangeImpl();
        result.id = exchange.getExchangeId();
        result.endpoint = exchange.getEndpoint();
        result.service = exchange.getService();
        result.role = (exchange.getRole() == Role.PROVIDER);
        if (exchange.getStatus() == ExchangeStatus.ACTIVE) result.status = Status.ACTIVE; else if (exchange.getStatus() == ExchangeStatus.DONE) result.status = Status.DONE; else result.status = Status.ERROR;
        result.interfaceName = exchange.getInterfaceName();
        result.operation = exchange.getOperation();
        result.isTransacted = exchange.isTransacted();
        result.pattern = exchange.getPattern();
        result.messages.put("in", createNormalizedMessage(exchange.getMessage("in")));
        result.messages.put("out", createNormalizedMessage(exchange.getMessage("out")));
        result.messages.put("fault", createNormalizedMessage(exchange.getMessage("fault")));
        for (Object key : exchange.getPropertyNames()) {
            result.properties.put(key, exchange.getProperty(key.toString()));
        }
        return result;
    }

    private NormalizedMessage createNormalizedMessage(NormalizedMessage message) {
        NormalizedMessageImpl result = new NormalizedMessageImpl();
        result.setContent(new StringSource("<!-- no content available -->"));
        return result;
    }

    public static final class MessageExchangeImpl implements MessageExchange, Serializable {

        private static final long serialVersionUID = 1628145105322834213L;

        private String id;

        private ServiceEndpoint endpoint;

        private boolean role;

        private Status status;

        private QName service;

        private boolean isTransacted;

        private QName interfaceName;

        private QName operation;

        private URI pattern;

        private Map<String, NormalizedMessage> messages = new HashMap<String, NormalizedMessage>();

        private Map<Object, Object> properties = new HashMap<Object, Object>();

        public Fault createFault() throws MessagingException {
            return new FaultImpl();
        }

        public NormalizedMessage createMessage() throws MessagingException {
            return new NormalizedMessageImpl();
        }

        public ServiceEndpoint getEndpoint() {
            return this.endpoint;
        }

        public Exception getError() {
            return null;
        }

        public String getExchangeId() {
            return id;
        }

        public Fault getFault() {
            return null;
        }

        public QName getInterfaceName() {
            return this.interfaceName;
        }

        public NormalizedMessage getMessage(String name) {
            return messages.get(name);
        }

        public QName getOperation() {
            return this.operation;
        }

        public URI getPattern() {
            return this.pattern;
        }

        public Object getProperty(String name) {
            return properties.get(name);
        }

        public Set getPropertyNames() {
            return properties.keySet();
        }

        public Role getRole() {
            return role ? Role.PROVIDER : Role.CONSUMER;
        }

        public QName getService() {
            return this.service;
        }

        public ExchangeStatus getStatus() {
            if (this.status == Status.ACTIVE) return ExchangeStatus.ACTIVE; else if (this.status == Status.DONE) return ExchangeStatus.DONE; else return ExchangeStatus.ERROR;
        }

        public boolean isTransacted() {
            return this.isTransacted();
        }

        public void setEndpoint(ServiceEndpoint endpoint) {
            this.endpoint = endpoint;
        }

        public void setError(Exception error) {
        }

        public void setFault(Fault fault) throws MessagingException {
            setMessage(fault, "fault");
        }

        public void setInterfaceName(QName interfaceName) {
            this.interfaceName = interfaceName;
        }

        public void setMessage(NormalizedMessage msg, String name) throws MessagingException {
            this.messages.put(name, msg);
        }

        public void setOperation(QName name) {
            this.operation = name;
        }

        public void setProperty(String name, Object obj) {
            this.properties.put(name, obj);
        }

        public void setService(QName service) {
            this.service = service;
        }

        public void setStatus(ExchangeStatus status) throws MessagingException {
            if (status == ExchangeStatus.ACTIVE) this.status = Status.ACTIVE; else if (status == ExchangeStatus.DONE) this.status = Status.DONE; else this.status = Status.ERROR;
        }
    }
}
