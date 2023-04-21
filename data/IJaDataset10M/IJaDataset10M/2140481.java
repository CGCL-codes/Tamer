package com.globant.google.mendoza.malbec;

import com.globant.google.mendoza.malbec.transport.NotificationDispatcher;
import com.globant.google.mendoza.malbec.transport.Transport;

/** The main entry point to the GBuy API.
 *
 * You create one instance of this class and use it to send messages to GBuy
 * and register the order callback. It must be passed a transport
 * implementation that will be used to send and receive the GBuy messages.
 *
 * To send commands to GBuy you do:
 *
 * <pre>
 *
 * StandAlone transport = new StandAlone(clientFactory, serverFactory,
 * merchantId, merchantKey, gbuyURL);
 *
 * GBuy gbuy = new GBuy(transport);
 *
 * gbuy.getOrder(orderNumber).archive();
 *
 * </pre>
 *
 * To receive notifications, you must create an implementation of the
 * OrderListener interface and call registerOrderListener. Then call start() to
 * start listening for connections.
 *
 * <pre>
 *
 * StandAlone transport = new StandAlone(clientFactory, serverFactory,
 * merchantId, merchantKey, gbuyURL);
 *
 * GBuy gbuy = new GBuy(transport);
 *
 * gbuy.registerOrderListener(orderListener);
 *
 * guy.start();
 *
 * </pre>
 *
 * You can call registerOrderListener and registerCalculationListener multiple
 * times, but it will have no effect until you call start. Once the GBuy is
 * started, you can change the listeners, but always remember to call start so
 * the changes are applied.
 */
public final class GBuy {

    /** The transport implementation.
   */
    private Transport transport = null;

    /** The order listener.
   */
    private OrderListener orderListener = null;

    /** The callback calculation listener.
   */
    private CalculationListener calculationListener = null;

    /** True if the transport has been started.
   */
    private boolean started = false;

    /** Creates an instance of the GBuy api.
   *
   * @param theTransport The trasport implementation. It cannot be null.
   */
    public GBuy(final Transport theTransport) {
        if (theTransport == null) {
            throw new IllegalArgumentException("the transport cannot be null");
        }
        transport = theTransport;
    }

    /** Registers the listener for order notifications.
   *
   * Calling this function multiple times overwrites the old listener, if any.
   *
   * @param theListener The order listener. It can be null.
   */
    public void registerOrderListener(final OrderListener theListener) {
        orderListener = theListener;
    }

    /** Registers the listener for the merchant calculation callback.
   *
   * Calling this function multiple times overwrites the old listener, if any.
   *
   * @param theListener The callback listener. It can be null.
   */
    public void registerCalculationListener(final CalculationListener theListener) {
        calculationListener = theListener;
    }

    /** Creates an order to send commands to GBuy.
   *
   * @param orderNumber The order number to wrap.
   *
   * @return Returns the order ready to send commands to GBuy.
   */
    public Order getOrder(final String orderNumber) {
        return new Order(transport, orderNumber);
    }

    /** Starts listening to connections from GBuy.
   *
   * You must call registerOrderListener or registerCalculationListener before
   * calling start. The listener will start receiving notifications after this
   * function is called.
   *
   * The second and subsequent calls to start only re-registers the listeners
   * in the transport.
   */
    public void start() {
        if (orderListener == null && calculationListener == null) {
            throw new IllegalStateException("Must set the order listener or the" + " calculation listener first");
        }
        NotificationDispatcher dispatcher;
        dispatcher = new NotificationDispatcher(transport, orderListener, calculationListener);
        transport.registerReceiver(dispatcher);
        if (!started) {
            started = true;
            transport.start();
        }
    }

    /** Stops listening to connections from GBuy.
   *
   * The listenr will not receive any more notificatinos once stop is called.
   */
    public void stop() {
        if (started) {
            started = false;
            transport.stop();
        }
    }
}
