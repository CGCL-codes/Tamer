package se.sics.tasim.tacscm.sim;

import java.util.logging.Logger;
import se.sics.isl.util.ArrayUtils;
import se.sics.tasim.props.ActiveOrders;
import se.sics.tasim.props.DeliveryNotice;

public abstract class Supplier extends Builtin {

    private static final String CONF = "supplier.";

    protected Logger log = Logger.getLogger(Supplier.class.getName());

    private int[] components;

    public Supplier() {
        super(CONF);
    }

    protected final void setup() {
        String componentList = getProperty("products");
        int[] components = getSimulation().parseProducts(componentList);
        this.components = components == null ? new int[0] : components;
        if (this.components.length > 0) {
            StringBuffer sb = new StringBuffer().append(getName()).append(" produces components: ");
            for (int i = 0, n = components.length; i < n; i++) {
                if (i > 0) sb.append(", ");
                sb.append(components[i]);
            }
            log.finest(sb.toString());
        } else {
            log.severe("no components for supplier " + getName());
        }
        supplierSetup();
    }

    protected final void stopped() {
        supplierStopped();
    }

    protected final void shutdown() {
        supplierShutdown();
    }

    /**
   * Request that this supplier adds the active orders for the
   * specified customer.
   *
   * @param customer the customer for which orders are searched
   * @param activeOrders the active order table to fill
   */
    protected abstract void addActiveOrders(String customer, ActiveOrders activeOrders);

    public int getComponentCount() {
        return components.length;
    }

    public int getComponentID(int index) {
        return components[index];
    }

    int[] getComponents() {
        return components;
    }

    protected boolean isProducing(int productID) {
        return ArrayUtils.indexOf(components, productID) >= 0;
    }

    protected void addSupplyOrdered(int productID, int quantityOrdered, int averageUnitPrice) {
        getSimulation().addSupplyOrdered(productID, quantityOrdered, averageUnitPrice);
    }

    protected void addSupplyDelivered(int productID, int quantityDelivered) {
        getSimulation().addSupplyDelivered(productID, quantityDelivered);
    }

    protected void sendSupplierCapacity(int productID, int capacity) {
        getSimulation().sendSupplierCapacity(getAddress(), productID, capacity);
    }

    protected void sendSupplierReputation(String agent, double reputation) {
        getSimulation().sendSupplierReputation(getAddress(), agent, reputation);
    }

    protected void requestPayment(String receiver, int orderID, long totalPrice) {
        getSimulation().transaction(getAddress(), receiver, orderID, totalPrice);
    }

    protected void deliverToFactory(String receiver, int date, DeliveryNotice notice) {
        if (getSimulation().deliverToFactory(receiver, date, notice)) {
            sendMessage(receiver, notice);
        } else {
            log.warning("could not deliver to " + receiver + ": " + notice);
        }
    }

    /**
   * Called when the simulation is being setup, before the simulation starts
   */
    protected abstract void supplierSetup();

    /**
   * Called when the simulation is about to end, but before it has ended
   */
    protected abstract void supplierStopped();

    /**
   * Called when the simulation has been ended
   */
    protected abstract void supplierShutdown();

    protected void finalize() throws Throwable {
        log.info("SUPPLIER " + getName() + " IS BEING GARBAGED");
        super.finalize();
    }
}
