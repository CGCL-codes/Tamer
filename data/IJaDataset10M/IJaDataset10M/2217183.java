package sale.events;

import sale.*;
import java.util.EventObject;

/**
  * An event indicating changes in a SalesPoint's customer queue.
  *
  * @author Steffen Zschaler
  * @version 2.0 18/08/1999
  * @since v2.0
  */
public class CustomerQueueEvent extends EventObject {

    /**
    * The customer that was either queued or unqueued.
    *
    * @serial
    */
    private Customer m_cAffected;

    /**
    * Create a new CustomerQueueEvent.
    *
    * @param spSource the SalesPoint that issued the event.
    * @param cAffected the affected customer.
    */
    public CustomerQueueEvent(SalesPoint spSource, Customer cAffected) {
        super(spSource);
        m_cAffected = cAffected;
    }

    /**
    * @return the SalesPoint that issued the event.
    *
    * @override Never
    */
    public SalesPoint getSalesPoint() {
        return (SalesPoint) getSource();
    }

    /**
    * @return the customer that is affected by the event.
    *
    * @override Never
    */
    public Customer getAffectedCustomer() {
        return m_cAffected;
    }
}
