package com.sun.j2ee.blueprints.opc.powebservice;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import org.springframework.ejb.support.AbstractStatelessSessionBean;
import com.sun.j2ee.blueprints.opc.JNDINames;
import com.sun.j2ee.blueprints.opc.purchaseorder.PurchaseOrder;
import com.sun.j2ee.blueprints.opc.serviceexceptions.InvalidPOException;
import com.sun.j2ee.blueprints.opc.serviceexceptions.ProcessingException;
import com.sun.j2ee.blueprints.opc.utils.JMSUtils;
import com.sun.j2ee.blueprints.servicelocator.ejb.NullBeanFactoryLocator;

/**
 *  This class is the entry point for purchase orders submitted 
 *  by adventure builder web site application when a user 
 *  submits an order.
 */
public class PoEndpointBean extends AbstractStatelessSessionBean {

    public void setSessionContext(SessionContext sessionContext) {
        super.setSessionContext(sessionContext);
        setBeanFactoryLocator(new NullBeanFactoryLocator());
    }

    /**
     * Accept a purchase order, place the order in a JMS queue and return the 
     * order id so that the caller can have a correlation id for the order
     */
    public String submitPurchaseOrder(PurchaseOrder po) throws InvalidPOException, ProcessingException, RemoteException {
        if (po == null) {
            throw new InvalidPOException("The Purchase Order received was empty!!!!");
        } else if (po.getUserId() == null || po.getEmailId() == null || po.getLocale() == null || po.getOrderDate() == null || po.getShippingInfo() == null || po.getBillingInfo() == null || po.getTotalPrice() == 0 || po.getCreditCard() == null || po.getHeadCount() == 0 || po.getStartDate() == null || po.getEndDate() == null || po.getDepartureCity() == null) {
            throw new InvalidPOException("No field in the purchase order can be null!");
        }
        if (JMSUtils.sendMessage(JNDINames.WORKFLOW_MGR_MDB_QUEUE, JNDINames.DOC_TYPE, JNDINames.PO_DOCUMENT, (Object) po) == false) throw new ProcessingException("Irrecoverable error while submitting the order for processing");
        return po.getPoId();
    }

    protected void onEjbCreate() throws CreateException {
    }
}
