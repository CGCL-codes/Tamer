package sale.swing;

import javax.swing.*;
import util.*;
import sale.events.*;
import sale.*;

/**
  * {@link ListModel} for a customer queue.
  *
  * @author Steffen Zschaler
  * @version 2.0 18/08/1999
  * @since v2.0
  */
public class CustomerQueueModel extends AbstractListModel implements HelpableListener, CustomerQueueListener, SerializableListener {

    /**
    * The SalesPoint whose customer queue is modelled.
    *
    * @serial
    */
    private SalesPoint m_spModel;

    /**
    * Create a new CustomerQueueModel.
    *
    * @param sp the SalesPoint whose customer queue is to be modelled.
    */
    public CustomerQueueModel(SalesPoint sp) {
        super();
        m_spModel = sp;
        listenerList = new ListenerHelper(this);
    }

    /**
    * Get the customer that queues at the specified position.
    *
    * @param nIdx the position at which to search the customer.
    *
    * @override Never
    */
    public Object getElementAt(int nIdx) {
        return m_spModel.getCustomers().get(nIdx);
    }

    /**
    * Get the number of customers currently queueing at the SalesPoint.
    *
    * @override Never
    */
    public int getSize() {
        return m_spModel.getCustomers().size();
    }

    /**
    * Subscribe as a {@link CustomerQueueListener} at the {@link SalesPoint}.
    *
    * @override Never
    */
    public void subscribe() {
        m_spModel.addCustomerQueueListener(this);
    }

    /**
    * Unsubscribe as a {@link CustomerQueueListener} from the {@link SalesPoint}.
    *
    * @override Never
    */
    public void unsubscribe() {
        m_spModel.removeCustomerQueueListener(this);
    }

    /**
    * Update the model. Empty method.
    *
    * @override Never.
    */
    public void updateModel() {
    }

    /**
    * Called when a new customer queued at the SalesPoint.
    *
    * @override Never
    */
    public void onCustomerQueued(CustomerQueueEvent e) {
        int nIdx = m_spModel.getCustomers().indexOf(e.getAffectedCustomer());
        fireIntervalAdded(this, nIdx, nIdx);
    }

    /**
    * Called when a customer unqueued from the SalesPoint.
    *
    * @override Never
    */
    public void onCustomerUnQueued(CustomerQueueEvent e) {
        fireContentsChanged(this, 0, getSize());
    }
}
