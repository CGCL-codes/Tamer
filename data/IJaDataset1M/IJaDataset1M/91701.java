package de.ios.kontor.sv.postgres.impl;

import java.rmi.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;

/**
 *
 */
public class KontorSessionImpl extends de.ios.kontor.sv.main.impl.KontorSessionImpl implements de.ios.kontor.sv.main.co.KontorSession {

    /** The Constructor. */
    public KontorSessionImpl() throws RemoteException {
    }

    /**
   * Create the Kontor-Controllers.
   */
    protected void initRemoteObjects(de.ios.framework.remote.sv.impl.SessionCarrier _sc) throws de.ios.framework.basic.ServerException {
        if (_sc == null) {
            _sc = new de.ios.kontor.utils.SessionCarrier("Prototype", 0, sessionId);
            createSessionCarriers(_sc);
        }
        super.initRemoteObjects(_sc);
        try {
            de.ios.kontor.utils.SessionCarrier sc = (de.ios.kontor.utils.SessionCarrier) _sc;
            orderController = new OrderControllerImpl(db, new de.ios.kontor.sv.order.impl.OrderFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderLineEntryController = new OrderLineEntryControllerImpl(db, new de.ios.kontor.sv.basic.impl.DummyFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderFreeLineEntryController = new OrderFreeLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.OrderFreeLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderServiceLineEntryController = new OrderServiceLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.OrderServiceLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderArticleLineEntryController = new OrderArticleLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.OrderArticleLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            invoiceController = new InvoiceControllerImpl(db, new de.ios.kontor.sv.order.impl.InvoiceFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceLineEntryController = new InvoiceLineEntryControllerImpl(db, new de.ios.kontor.sv.basic.impl.DummyFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceFreeLineEntryController = new InvoiceFreeLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.InvoiceFreeLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceServiceLineEntryController = new InvoiceServiceLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.InvoiceServiceLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceArticleLineEntryController = new InvoiceArticleLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.InvoiceArticleLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            bankOrderController = new BankOrderControllerImpl(db, new de.ios.kontor.sv.order.impl.BankOrderFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            contactPersonController = new ContactPersonControllerImpl(db, new de.ios.kontor.sv.address.impl.ContactPersonFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            customerController = new CustomerControllerImpl(db, new de.ios.kontor.sv.address.impl.CustomerFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            deliveryNoteLineEntryController = new DeliveryNoteLineEntryControllerImpl(db, new de.ios.kontor.sv.order.impl.DeliveryNoteLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            deliveryNoteController = new DeliveryNoteControllerImpl(db, new de.ios.kontor.sv.order.impl.DeliveryNoteFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            stockEntryController = new StockEntryControllerImpl(db, new de.ios.kontor.sv.stock.impl.StockEntryFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            stockTransactionController = new StockTransactionControllerImpl(db, new de.ios.kontor.sv.stock.impl.StockTransactionFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            allOrderLineEntryController.removeAllElements();
            allOrderLineEntryController.addElement(orderFreeLineEntryController);
            allOrderLineEntryController.addElement(orderServiceLineEntryController);
            allOrderLineEntryController.addElement(orderArticleLineEntryController);
            allInvoiceLineEntryController.removeAllElements();
            allInvoiceLineEntryController.addElement(invoiceFreeLineEntryController);
            allInvoiceLineEntryController.addElement(invoiceServiceLineEntryController);
            allInvoiceLineEntryController.addElement(invoiceArticleLineEntryController);
        } catch (Throwable t) {
            throw new de.ios.framework.basic.ServerException("The Initialization of the Controllers failed!", t);
        }
    }
}
