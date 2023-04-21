package servlet.circulation;

/**
 *
 * @author  vasu praveen
 */
public final class CheckoutToBinderHandler {

    private static CheckoutToBinderHandler instance = null;

    /** Creates a new instance of DBConnector *
     */
    private CheckoutToBinderHandler() {
    }

    /** Getter for property instance.
     * @return Value of property instance.
     */
    public static servlet.circulation.CheckoutToBinderHandler getInstance() {
        instance = new CheckoutToBinderHandler();
        return instance;
    }

    /** Setter for property instance.
     * @param instance New value of property instance.
     */
    public void setInstance(servlet.circulation.CheckoutToBinderHandler instance) {
        this.instance = instance;
    }
}
