package org.coos.messaging;

/**
 * @author Knut Eilif Husa, Tellu AS
 * A helperclass for communicating with the bus
 */
public class InteractionHelper {

    Endpoint endpoint;

    public InteractionHelper(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Request an exchange from the supplied endpointUri
     *
     * @param endpointUri
     *            the endpoint uri
     * @param exchange
     *            the exchange to be processed
     * @return the processed exchange
     */
    public Exchange request(String endpointUri, Exchange exchange) {
        exchange.setPattern(new ExchangePattern(ExchangePattern.OutIn));
        Message msg = exchange.getOutBoundMessage();
        msg.setReceiverEndpointUri(endpointUri);
        return endpoint.processExchange(exchange);
    }

    /**
     * Sends a reply to the exchange
     *
     * @param exchange
     *            the exchange to be processed
     * @return the processed exchange
     */
    public Exchange reply(Exchange exchange) {
        Message inMsg = exchange.getInBoundMessage();
        Message outMsg = exchange.getOutBoundMessage();
        outMsg.setReceiverEndpointUri(inMsg.getSenderEndpointUri());
        outMsg.setSenderEndpointUri(inMsg.getReceiverEndpointUri());
        return endpoint.processExchange(exchange);
    }

    /**
     * Sends an exchange to the endpoint using the supplied endpoint uri
     * Note that the exchangePattern of the exchange is preserved through this method.
     * Hence it is possible to send exchanges of all exchange patterns using this method.
     *
     * @param endpointUri
     *            the endpoint uri
     * @param exchange
     *            the exchange to be processed
     * @return the processed exchange
     */
    public Exchange send(String endpointUri, Exchange exchange) {
        Message msg = exchange.getOutBoundMessage();
        msg.setReceiverEndpointUri(endpointUri);
        return endpoint.processExchange(exchange);
    }

    /**
     * Sends an exchange to the endpoint using the supplied endpoint uri using a send and forget
     * exchange strategy meaning that the receiver must not acknowledge the reception of the message.
     *
     * @param endpointUri
     *            the endpoint uri
     * @param exchange
     *            the exchange to be processed
     * @return the processed exchange
     */
    public Exchange sendAndForget(String endpointUri, Exchange exchange) {
        exchange.setPattern(new ExchangePattern(ExchangePattern.OutOnly));
        Message msg = exchange.getOutBoundMessage();
        msg.setReceiverEndpointUri(endpointUri);
        return endpoint.processExchange(exchange);
    }

    /**
     * Sends an exchange to the endpoint using the supplied endpoint uri using an robust
     * exchange scheme meaning that the receiver must acknowledge the reception of the message.
     *
     * @param endpointUri
     *            the endpoint uri
     * @param exchange
     *            the exchange to be processed
     * @return the processed exchange
     */
    public Exchange sendRobust(String endpointUri, Exchange exchange) {
        exchange.setPattern(new ExchangePattern(ExchangePattern.RobustOutOnly));
        Message msg = exchange.getOutBoundMessage();
        msg.setReceiverEndpointUri(endpointUri);
        return endpoint.processExchange(exchange);
    }

    /**
     * Sends an exchange to the endpoint using the supplied endpoint uri and
     * asyncCallback. Note that the exchangePattern of the exchange is preserved through this method.
     * Hence it is possible to send exchanges of all exchange patterns using this method.
     *
     * @param endpointUri
     *            the endpoint uri
     * @param exchange
     *            the exchange to be processed
     * @param callback
     *            the callback notifying when the exchange is ready
     */
    public void send(String endpointUri, Exchange exchange, AsyncCallback callback) {
        Message msg = exchange.getOutBoundMessage();
        msg.setReceiverEndpointUri(endpointUri);
        endpoint.processExchange(exchange, callback);
    }
}
