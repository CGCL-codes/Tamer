package de.ios.kontor.sv.main.impl;

import java.rmi.*;
import java.util.*;
import java.net.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.framework.remote.sv.impl.*;
import de.ios.framework.gui.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.address.impl.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.order.impl.*;
import de.ios.kontor.sv.basic.co.*;
import de.ios.kontor.sv.basic.impl.*;
import de.ios.kontor.sv.util.co.*;
import de.ios.kontor.sv.util.impl.*;
import de.ios.kontor.sv.fa.co.*;
import de.ios.kontor.sv.fa.impl.*;
import de.ios.kontor.sv.stock.co.*;
import de.ios.kontor.sv.stock.impl.*;

/**
 *
 * @author fw (Frank Wiesen)
 * @version $Id: KontorSessionImpl.java,v 1.1.1.1 2004/03/24 23:02:54 nanneb Exp $
 */
public class KontorSessionImpl extends RemoteSessionImpl implements KontorSession {

    /** The Constructor. */
    public KontorSessionImpl() throws RemoteException {
    }

    /**
   * Return the Kontor server.
   * @return the Kontor server
   */
    public Kontor getServer() throws KontorException {
        return (Kontor) getRemoteServer();
    }

    /** Create the Kontor-Controllers. */
    protected void initRemoteObjects(de.ios.framework.remote.sv.impl.SessionCarrier _sc) throws de.ios.framework.basic.ServerException {
        if (_sc == null) {
            _sc = new de.ios.kontor.utils.SessionCarrier("Prototype", 0, sessionId);
            createSessionCarriers(_sc);
        }
        super.initRemoteObjects(_sc);
        try {
            de.ios.kontor.utils.SessionCarrier sc = (de.ios.kontor.utils.SessionCarrier) _sc;
            FAAccountController = new FAAccountControllerImpl(db, new FAAccountFactoryImpl(db, getKontorSessionCarrier(RG_FINANCIAL)));
            EntryBatchController = new EntryBatchControllerImpl(db, new EntryBatchFactoryImpl(db, getKontorSessionCarrier(RG_FINANCIAL)));
            EntryBatchLineController = new EntryBatchLineControllerImpl(db, new EntryBatchLineFactoryImpl(db, getKontorSessionCarrier(RG_FINANCIAL)));
            companyController = new CompanyControllerImpl(db, new CompanyFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            stateController = new StateControllerImpl(db, new StateFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            countryController = new CountryControllerImpl(db, new CountryFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            addressController = new AddressControllerImpl(db, new AddressFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            kindOfAddressController = new KindOfAddressControllerImpl(db, new KindOfAddressFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            kindOfBusinessPController = new KindOfBusinessPControllerImpl(db, new KindOfBusinessPFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            personController = new PersonControllerImpl(db, new PersonFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            contactPersonController = new ContactPersonControllerImpl(db, new ContactPersonFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            contactController = new ContactControllerImpl(db, new ContactFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            contactTypeController = new ContactTypeControllerImpl(db, new ContactTypeFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            customerController = new CustomerControllerImpl(db, new CustomerFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            bankAccountController = new BankAccountControllerImpl(db, new BankAccountFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)));
            businessPartnerController = new BusinessPartnerControllerImpl(db, new BusinessPartnerFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS), new CompanyFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS)), new PersonFactoryImpl(db, getKontorSessionCarrier(RG_CUSTOMERS))));
            createOrderConfiguration();
            orderController = new OrderControllerImpl(db, new OrderFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderLineEntryController = new OrderLineEntryControllerImpl(db, new de.ios.kontor.sv.basic.impl.DummyFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderFreeLineEntryController = new OrderFreeLineEntryControllerImpl(db, new OrderFreeLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderServiceLineEntryController = new OrderServiceLineEntryControllerImpl(db, new OrderServiceLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            orderArticleLineEntryController = new OrderArticleLineEntryControllerImpl(db, new OrderArticleLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            invoiceController = new InvoiceControllerImpl(db, new InvoiceFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceLineEntryController = new InvoiceLineEntryControllerImpl(db, new de.ios.kontor.sv.basic.impl.DummyFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceFreeLineEntryController = new InvoiceFreeLineEntryControllerImpl(db, new InvoiceFreeLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceServiceLineEntryController = new InvoiceServiceLineEntryControllerImpl(db, new InvoiceServiceLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            invoiceArticleLineEntryController = new InvoiceArticleLineEntryControllerImpl(db, new InvoiceArticleLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            bankOrderController = new BankOrderControllerImpl(db, new BankOrderFactoryImpl(db, getKontorSessionCarrier(RG_INVOICES)));
            serviceController = new ServiceControllerImpl(db, new ServiceFactoryImpl(db, getKontorSessionCarrier(RG_SERVICES)));
            deliveryNoteController = new DeliveryNoteControllerImpl(db, new DeliveryNoteFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            deliveryNoteLineEntryController = new DeliveryNoteLineEntryControllerImpl(db, new DeliveryNoteLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            articleTypeController = new ArticleTypeControllerImpl(db, new ArticleTypeFactoryImpl(db, getKontorSessionCarrier(RG_ARTICLES)));
            priceCategoryController = new PriceCategoryControllerImpl(db, new PriceCategoryFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            specialPriceController = new SpecialPriceControllerImpl(db, new SpecialPriceFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            paymentTermsController = new PaymentTermsControllerImpl(db, new PaymentTermsFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            lineEntryKindController = new LineEntryKindControllerImpl(db, new de.ios.kontor.sv.basic.impl.DummyFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)));
            partsListController = new PartsListControllerImpl(db, new PartsListFactoryImpl(db, getKontorSessionCarrier(RG_ORDERS)), articleTypeController);
            vatController = new VATControllerImpl(db, new VATFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            deliveryNotePackageController = new DeliveryNotePackageControllerImpl(db, new DeliveryNotePackageFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            deliveryNotePackageLineEntryController = new DeliveryNotePackageLineEntryControllerImpl(db, new DeliveryNotePackageLineEntryFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            packageKindController = new PackageKindControllerImpl(db, new PackageKindFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            shipmentKindController = new ShipmentKindControllerImpl(db, new ShipmentKindFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            allOrderLineEntryController.addElement(orderFreeLineEntryController);
            allOrderLineEntryController.addElement(orderServiceLineEntryController);
            allOrderLineEntryController.addElement(orderArticleLineEntryController);
            allInvoiceLineEntryController.addElement(invoiceFreeLineEntryController);
            allInvoiceLineEntryController.addElement(invoiceServiceLineEntryController);
            allInvoiceLineEntryController.addElement(invoiceArticleLineEntryController);
            stockController = new StockControllerImpl(db, new StockFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            stockEntryController = new StockEntryControllerImpl(db, new StockEntryFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            kindOfStockTransactionController = new KindOfStockTransactionControllerImpl(db, new KindOfStockTransactionFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            stockTransactionController = new StockTransactionControllerImpl(db, new StockTransactionFactoryImpl(db, getKontorSessionCarrier(RG_DELNOTES)));
            menuFolderController = new MenuFolderControllerImpl(db, new MenuFolderFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
            menuProgramController = new MenuProgramControllerImpl(db, new MenuProgramFactoryImpl(db, getKontorSessionCarrier(RG_CONFIGURATION)));
        } catch (Throwable t) {
            throw new de.ios.framework.basic.ServerException("The Initialization of the Controllers failed!", t);
        }
    }

    /**
   * Create the Order-Configuration
   */
    protected void createOrderConfiguration() throws RemoteException {
        orderConfiguration = new OrderConfigurationImpl(db, new de.ios.kontor.utils.SessionCarrier("Alle", 65535, sessionId), null);
    }

    /**
   * Get a KontorSessionCarrier.
   */
    protected de.ios.kontor.utils.SessionCarrier getKontorSessionCarrier(String group) {
        return (de.ios.kontor.utils.SessionCarrier) getSessionCarrier(group);
    }

    /**
   * Get the fixed Rights for a group (can't be removed from any user; for rights allways required by the System).
   */
    protected long getFixedRights(String group) {
        if (group.equals(RG_CUSTOMERS)) return Basic.LOAD_RIGHT;
        return super.getFixedRights(group);
    }

    /**
   * Get the FAAccount-Controller.
   */
    public FAAccountController getFAAccountController() {
        return FAAccountController;
    }

    /**
   * Get the FAAccount-ControllerImpl.
   */
    public FAAccountControllerImpl getFAAccountControllerImpl() {
        return FAAccountController;
    }

    /**
   * Get the EntryBatch-Controller.
   */
    public EntryBatchController getEntryBatchController() {
        return EntryBatchController;
    }

    /**
   * Get the EntryBatch-ControllerImpl.
   */
    public EntryBatchControllerImpl getEntryBatchControllerImpl() {
        return EntryBatchController;
    }

    /**
   * Get the EntryBatchLine-Controller.
   */
    public EntryBatchLineController getEntryBatchLineController() {
        return EntryBatchLineController;
    }

    /**
   * Get the EntryBatchLine-ControllerImpl.
   */
    public EntryBatchLineControllerImpl getEntryBatchLineControllerImpl() {
        return EntryBatchLineController;
    }

    /**
   * Get the Company-Controller.
   */
    public CompanyController getCompanyController() {
        return companyController;
    }

    /**
   * Get the Company-ControllerImpl.
   */
    public CompanyControllerImpl getCompanyControllerImpl() {
        return companyController;
    }

    /**
   * Get the Customer-Controller.
   */
    public CustomerController getCustomerController() {
        return customerController;
    }

    /**
   * Get the Customer-ControllerImpl.
   */
    public CustomerControllerImpl getCustomerControllerImpl() {
        return customerController;
    }

    /**
   * Get the BankAccount-Controller
   */
    public BankAccountController getBankAccountController() {
        return bankAccountController;
    }

    /**
   * Get the BankAccount-ControllerImpl.
   */
    public BankAccountControllerImpl getBankAccountControllerImpl() {
        return bankAccountController;
    }

    /**
   * Get the BusinessPartner-Controller.
   */
    public BusinessPartnerController getBusinessPartnerController() {
        return businessPartnerController;
    }

    /**
   * Get the BusinessPartner-ControllerImpl.
   */
    public BusinessPartnerControllerImpl getBusinessPartnerControllerImpl() {
        return businessPartnerController;
    }

    /**
   * Get the AddressController.
   */
    public AddressController getAddressController() {
        return addressController;
    }

    /**
   * Get the AddressControllerImpl.
   */
    public AddressControllerImpl getAddressControllerImpl() {
        return addressController;
    }

    /**
   * Get the KindOfAddressController.
   */
    public KindOfAddressController getKindOfAddressController() {
        return kindOfAddressController;
    }

    /**
   * Get the KindOfAddressControllerImpl.
   */
    public KindOfAddressControllerImpl getKindOfAddressControllerImpl() {
        return kindOfAddressController;
    }

    /**
   * Get the KindOfBusinessPController.
   */
    public KindOfBusinessPController getKindOfBusinessPController() {
        return kindOfBusinessPController;
    }

    /**
   * Get the KindOfBusinessPControllerImpl.
   */
    public KindOfBusinessPControllerImpl getKindOfBusinessPControllerImpl() {
        return kindOfBusinessPController;
    }

    /**
   * Get the StateController.
   */
    public StateController getStateController() {
        return stateController;
    }

    /**
   * Get the StateControllerImpl.
   */
    public StateControllerImpl getStateControllerImpl() {
        return stateController;
    }

    /**
   * Get the CountryController.
   */
    public CountryController getCountryController() {
        return countryController;
    }

    /**
   * Get the CountryControllerImpl.
   */
    public CountryControllerImpl getCountryControllerImpl() {
        return countryController;
    }

    /**
   * Get the PersonController.
   */
    public PersonController getPersonController() {
        return personController;
    }

    /**
   * Get the PersonControllerImpl.
   */
    public PersonControllerImpl getPersonControllerImpl() {
        return personController;
    }

    /**
   * Get the ContactPersonController.
   */
    public ContactPersonController getContactPersonController() {
        return contactPersonController;
    }

    /**
   * Get the ContactPersonControllerImpl.
   */
    public ContactPersonControllerImpl getContactPersonControllerImpl() {
        return contactPersonController;
    }

    /**
   * Get the ContactController.
   */
    public ContactController getContactController() {
        return contactController;
    }

    /**
   * Get the ContactControllerImpl.
   */
    public ContactControllerImpl getContactControllerImpl() {
        return contactController;
    }

    /**
   * Get the ContactTypeController.
   */
    public ContactTypeController getContactTypeController() {
        return contactTypeController;
    }

    /**
   * Get the ContactTypeControllerImpl.
   */
    public ContactTypeControllerImpl getContactTypeControllerImpl() {
        return contactTypeController;
    }

    /**
   * Get the OrderConfiguration.
   */
    public OrderConfiguration getOrderConfiguration() {
        return orderConfiguration;
    }

    /**
   * Get the OrderConfigurationImpl.
   */
    public OrderConfigurationImpl getOrderConfigurationImpl() {
        return orderConfiguration;
    }

    /**
   * Get the OrderController.
   */
    public OrderController getOrderController() {
        return orderController;
    }

    /**
   * Get the OrderControllerImpl.
   */
    public OrderControllerImpl getOrderControllerImpl() {
        return orderController;
    }

    /**
   * Get the OrderFreeLineEntryController.
   */
    public OrderFreeLineEntryController getOrderFreeLineEntryController() {
        return orderFreeLineEntryController;
    }

    /**
   * Get the OrderFreeLineEntryControllerImpl.
   */
    public OrderFreeLineEntryControllerImpl getOrderFreeLineEntryControllerImpl() {
        return orderFreeLineEntryController;
    }

    /**
   * Get the OrderLineEntryController.
   */
    public OrderLineEntryController getOrderLineEntryController() {
        return orderLineEntryController;
    }

    /**
   * Get the OrderLineEntryControllerImpl.
   */
    public OrderLineEntryControllerImpl getOrderLineEntryControllerImpl() {
        return orderLineEntryController;
    }

    /**
   * Get the OrderServiceLineEntryController.
   */
    public OrderServiceLineEntryController getOrderServiceLineEntryController() {
        return orderServiceLineEntryController;
    }

    /**
   * Get the OrderServiceLineEntryControllerImpl.
   */
    public OrderServiceLineEntryControllerImpl getOrderServiceLineEntryControllerImpl() {
        return orderServiceLineEntryController;
    }

    /**
   * Get the OrderArticleLineEntryController. 
   */
    public OrderArticleLineEntryController getOrderArticleLineEntryController() {
        return orderArticleLineEntryController;
    }

    /**
   * Get the OrderArticleLineEntryControllerImpl. 
   */
    public OrderArticleLineEntryControllerImpl getOrderArticleLineEntryControllerImpl() {
        return orderArticleLineEntryController;
    }

    /**
   * Get the InvoiceController.
   */
    public InvoiceController getInvoiceController() {
        return invoiceController;
    }

    /**
   * Get the InvoiceControllerImpl.
   */
    public InvoiceControllerImpl getInvoiceControllerImpl() {
        return invoiceController;
    }

    /**
   * Get the InvoiceLineEntryController.
   */
    public InvoiceLineEntryController getInvoiceLineEntryController() {
        return invoiceLineEntryController;
    }

    /**
   * Get the InvoiceLineEntryControllerImpl.
   */
    public InvoiceLineEntryControllerImpl getInvoiceLineEntryControllerImpl() {
        return invoiceLineEntryController;
    }

    /**
   * Get the InvoiceFreeLineEntryController.
   */
    public InvoiceFreeLineEntryController getInvoiceFreeLineEntryController() {
        return invoiceFreeLineEntryController;
    }

    /**
   * Get the InvoiceFreeLineEntryControllerImpl.
   */
    public InvoiceFreeLineEntryControllerImpl getInvoiceFreeLineEntryControllerImpl() {
        return invoiceFreeLineEntryController;
    }

    /**
   * Get the InvoiceServiceLineEntryController.
   */
    public InvoiceServiceLineEntryController getInvoiceServiceLineEntryController() {
        return invoiceServiceLineEntryController;
    }

    /**
   * Get the InvoiceServiceLineEntryControllerImpl.
   */
    public InvoiceServiceLineEntryControllerImpl getInvoiceServiceLineEntryControllerImpl() {
        return invoiceServiceLineEntryController;
    }

    /**
   * Get the InvoiceArticleLineEntryController.
   */
    public InvoiceArticleLineEntryController getInvoiceArticleLineEntryController() {
        return invoiceArticleLineEntryController;
    }

    /**
   * Get the InvoiceArticleLineEntryControllerImpl.
   */
    public InvoiceArticleLineEntryControllerImpl getInvoiceArticleLineEntryControllerImpl() {
        return invoiceArticleLineEntryController;
    }

    /**
   * Get the BankOrderController.
   */
    public BankOrderController getBankOrderController() {
        return bankOrderController;
    }

    /**
   * Get the BankOrderControllerImpl.
   */
    public BankOrderControllerImpl getBankOrderControllerImpl() {
        return bankOrderController;
    }

    /**
   * Get the LineEntryKindController.
   */
    public LineEntryKindController getLineEntryKindController() {
        return lineEntryKindController;
    }

    /**
   * Get the LineEntryKindControllerImpl.
   */
    public LineEntryKindControllerImpl getLineEntryKindControllerImpl() {
        return lineEntryKindController;
    }

    /**
   * Get the ServiceController.
   */
    public ServiceController getServiceController() {
        return serviceController;
    }

    /**
   * Get the ServiceControllerImpl.
   */
    public ServiceControllerImpl getServiceControllerImpl() {
        return serviceController;
    }

    /**
   * Get the DeliveryNoteController.
   */
    public DeliveryNoteController getDeliveryNoteController() {
        return deliveryNoteController;
    }

    /**
   * Get the DeliveryNoteControllerImpl.
   */
    public DeliveryNoteControllerImpl getDeliveryNoteControllerImpl() {
        return deliveryNoteController;
    }

    /**
   * Get the DeliveryNoteLineEntry-Controller.
   */
    public DeliveryNoteLineEntryController getDeliveryNoteLineEntryController() {
        return deliveryNoteLineEntryController;
    }

    /**
   * Get the DeliveryNoteLineEntry-ControllerImpl.
   */
    public DeliveryNoteLineEntryControllerImpl getDeliveryNoteLineEntryControllerImpl() {
        return deliveryNoteLineEntryController;
    }

    /**
   * Get the ArticleTypeController.
   */
    public ArticleTypeController getArticleTypeController() {
        return articleTypeController;
    }

    /**
   * Get the ArticleTypeControllerImpl.
   */
    public ArticleTypeControllerImpl getArticleTypeControllerImpl() {
        return articleTypeController;
    }

    /**
   * Get the PriceCategory-Controller.
   */
    public PriceCategoryController getPriceCategoryController() {
        return priceCategoryController;
    }

    /**
   * Get the PriceCategory-ControllerImpl.
   */
    public PriceCategoryControllerImpl getPriceCategoryControllerImpl() {
        return priceCategoryController;
    }

    /**
   * Get the SpecialPrice-Controller.
   */
    public SpecialPriceController getSpecialPriceController() {
        return specialPriceController;
    }

    /**
   * Get the SpecialPrice-ControllerImpl.
   */
    public SpecialPriceControllerImpl getSpecialPriceControllerImpl() {
        return specialPriceController;
    }

    /**
   * Get the PaymentTerms-Controller.
   */
    public PaymentTermsController getPaymentTermsController() {
        return paymentTermsController;
    }

    /**
   * Get the PaymentTerms-ControllerImpl.
   */
    public PaymentTermsControllerImpl getPaymentTermsControllerImpl() {
        return paymentTermsController;
    }

    /**
   * Get the PartsListController.
   */
    public PartsListController getPartsListController() {
        return partsListController;
    }

    /**
   * Get the PartsListControllerImpl.
   */
    public PartsListControllerImpl getPartsListControllerImpl() {
        return partsListController;
    }

    /**
   * Get all OrderRateLineEntryControllerImpls.
   */
    public Iterator getAllOrderRateLineEntryController() {
        return new VectorIterator(allOrderRateLineEntryController);
    }

    /**
   * Get all InvoiceRateLineEntryControllerImpls.
   */
    public Iterator getAllInvoiceRateLineEntryController() {
        return new VectorIterator(allInvoiceRateLineEntryController);
    }

    /**
   * Get all OrderLineEntryControllerImpls.
   */
    public Iterator getAllOrderLineEntryController() {
        return new VectorIterator(allOrderLineEntryController);
    }

    /**
   * Get all InvoiceLineEntryControllerImpls.
   */
    public Iterator getAllInvoiceLineEntryController() {
        return new VectorIterator(allInvoiceLineEntryController);
    }

    /**
   * Get the OrderLineController of the specified kind.
   */
    public OrderLineEntryController getOrderLineEntryController(String kind) throws KontorException {
        return getOrderLineEntryControllerImpl(kind);
    }

    /**
   * Get the OrderLineControllerImpl of the specified kind.
   */
    public OrderLineEntryControllerImpl getOrderLineEntryControllerImpl(String kind) throws KontorException {
        try {
            Iterator it = getAllOrderLineEntryController();
            while (it.next()) if (((OrderLineEntryControllerImpl) it.getObject()).getKind().compareTo(kind) == 0) return (OrderLineEntryControllerImpl) it.getObject();
            throw new KontorException("OrderLineEntryControllerImpl of type '" + kind + "' not found.");
        } catch (IteratorException ite) {
            throw new KontorException(ite.getMessage(), ite);
        }
    }

    /**
   * Get the InvoiceLineController of the specified kind.
   */
    public InvoiceLineEntryController getInvoiceLineEntryController(String kind) throws KontorException {
        return getInvoiceLineEntryControllerImpl(kind);
    }

    /**
   * Get the InvoiceLineControllerImpl of the specified kind.
   */
    public InvoiceLineEntryControllerImpl getInvoiceLineEntryControllerImpl(String kind) throws KontorException {
        try {
            Iterator it = getAllInvoiceLineEntryController();
            while (it.next()) if (((InvoiceLineEntryControllerImpl) it.getObject()).getKind().compareTo(kind) == 0) return (InvoiceLineEntryControllerImpl) it.getObject();
            throw new KontorException("InvoiceLineEntryController of type '" + kind + "' not found.");
        } catch (IteratorException ite) {
            throw new KontorException(ite.getMessage(), ite);
        }
    }

    /**
   * Get the Stock-Controller.
   */
    public StockController getStockController() {
        return stockController;
    }

    /**
   * Get the Stock-ControllerImpl.
   */
    public StockControllerImpl getStockControllerImpl() {
        return stockController;
    }

    /**
   * Get the StockEntry-Controller.
   */
    public StockEntryController getStockEntryController() {
        return stockEntryController;
    }

    /**
   * Get the StockEntry-ControllerImpl.
   */
    public StockEntryControllerImpl getStockEntryControllerImpl() {
        return stockEntryController;
    }

    /**
   * Get the KindOfStockTransaction-Controller.
   */
    public KindOfStockTransactionController getKindOfStockTransactionController() {
        return kindOfStockTransactionController;
    }

    /**
   * Get the KindOfStockTransaction-ControllerImpl.
   */
    public KindOfStockTransactionControllerImpl getKindOfStockTransactionControllerImpl() {
        return kindOfStockTransactionController;
    }

    /**
   * Get the StockTransaction-Controller.
   */
    public StockTransactionController getStockTransactionController() {
        return stockTransactionController;
    }

    /**
   * Get the StockTransaction-ControllerImpl.
   */
    public StockTransactionControllerImpl getStockTransactionControllerImpl() {
        return stockTransactionController;
    }

    /**
   * Get the VAT-Controller.
   */
    public VATController getVATController() {
        return vatController;
    }

    /**
   * Get the VAT-ControllerImpl.
   */
    public VATControllerImpl getVATControllerImpl() {
        return vatController;
    }

    /**
   * Get the MenuFolder-Controller.
   */
    public MenuFolderController getMenuFolderController() {
        return menuFolderController;
    }

    /**
   * Get the MenuFolder-ControllerImpl.
   */
    public MenuFolderControllerImpl getMenuFolderControllerImpl() {
        return menuFolderController;
    }

    /**
   * Get the MenuProgram-Controller.
   */
    public MenuProgramController getMenuProgramController() {
        return menuProgramController;
    }

    /**
   * Get the MenuProgram-ControllerImpl.
   */
    public MenuProgramControllerImpl getMenuProgramControllerImpl() {
        return menuProgramController;
    }

    /**
   * Get the DeliveryNotePackage-Controller.
   */
    public DeliveryNotePackageController getDeliveryNotePackageController() {
        return deliveryNotePackageController;
    }

    /**
   * Get the DeliveryNotePackage-ControllerImpl.
   */
    public DeliveryNotePackageControllerImpl getDeliveryNotePackageControllerImpl() {
        return deliveryNotePackageController;
    }

    /**
   * Get the DeliveryNotePackageLineEntry-Controller.
   */
    public DeliveryNotePackageLineEntryController getDeliveryNotePackageLineEntryController() {
        return deliveryNotePackageLineEntryController;
    }

    /**
   * Get the DeliveryNotePackageLineEntry-ControllerImpl.
   */
    public DeliveryNotePackageLineEntryControllerImpl getDeliveryNotePackageLineEntryControllerImpl() {
        return deliveryNotePackageLineEntryController;
    }

    /**
   * Get the PackageKind-Controller.
   */
    public PackageKindController getPackageKindController() {
        return packageKindController;
    }

    /**
   * Get the PackageKind-ControllerImpl.
   */
    public PackageKindControllerImpl getPackageKindControllerImpl() {
        return packageKindController;
    }

    /**
   * Get the ShipmentKind-Controller.
   */
    public ShipmentKindController getShipmentKindController() {
        return shipmentKindController;
    }

    /**
   * Get the ShipmentKind-ControllerImpl.
   */
    public ShipmentKindControllerImpl getShipmentKindControllerImpl() {
        return shipmentKindController;
    }

    /** The Controller for FAAccounts */
    protected FAAccountControllerImpl FAAccountController = null;

    /** The Controller for EntryBatchs */
    protected EntryBatchControllerImpl EntryBatchController = null;

    /** The Controller for EntryBatchLines */
    protected EntryBatchLineControllerImpl EntryBatchLineController = null;

    /**
   * The Controller for companys
   */
    protected CompanyControllerImpl companyController = null;

    /**
   * The Controller for persons
   */
    protected PersonControllerImpl personController = null;

    /**
   * The Controller for contact persons
   */
    protected ContactPersonControllerImpl contactPersonController = null;

    /**
   * The Controller for contacts
   */
    protected ContactControllerImpl contactController = null;

    /**
   * The Controller for contact types
   */
    protected ContactTypeControllerImpl contactTypeController = null;

    /**
   * The Controller for customers
   */
    protected CustomerControllerImpl customerController = null;

    /**
   * The Controller for Bank accounts
   */
    protected BankAccountControllerImpl bankAccountController = null;

    /**
   * The Controller for addresses
   */
    protected AddressControllerImpl addressController = null;

    /**
   * The Controller for address kinds
   */
    protected KindOfAddressControllerImpl kindOfAddressController = null;

    /**
   * The Controller for business partner kinds
   */
    protected KindOfBusinessPControllerImpl kindOfBusinessPController = null;

    /**
   * The Controller for states
   */
    protected StateControllerImpl stateController = null;

    /**
   * The Controller for countries
   */
    protected CountryControllerImpl countryController = null;

    /**
   * The Configuration for the Order-Package.
   */
    protected OrderConfigurationImpl orderConfiguration = null;

    /**
   * The Controller for Orders.
   */
    protected OrderControllerImpl orderController = null;

    /**
   * The Controller for all OrderLineEntries.
   */
    protected OrderLineEntryControllerImpl orderLineEntryController = null;

    /**
   * The Controller for OrderFreeLineEntries.
   */
    protected OrderFreeLineEntryControllerImpl orderFreeLineEntryController = null;

    /**
   * The Controller for OrderServiceLineEntries.
   */
    protected OrderServiceLineEntryControllerImpl orderServiceLineEntryController = null;

    /**
   * The Controller for OrderArticleLineEntries.
   */
    protected OrderArticleLineEntryControllerImpl orderArticleLineEntryController = null;

    /**
   * The Controller for Invoices.
   */
    protected InvoiceControllerImpl invoiceController = null;

    /**
   * The Controller for InvoiceServiceLineEntries.
   */
    protected InvoiceServiceLineEntryControllerImpl invoiceServiceLineEntryController = null;

    /**
   * The Controller for all InvoiceLineEntries.
   */
    protected InvoiceLineEntryControllerImpl invoiceLineEntryController = null;

    /**
   * The Controller for InvoiceFreeLineEntries.
   */
    protected InvoiceFreeLineEntryControllerImpl invoiceFreeLineEntryController = null;

    /**
   * The Controller for InvoiceArticleLineEntries.
   */
    protected InvoiceArticleLineEntryControllerImpl invoiceArticleLineEntryController = null;

    /**
   * The Controller for BankOrders.
   */
    protected BankOrderControllerImpl bankOrderController = null;

    /**
   * The Controller for LineEntryKinds.
   */
    protected LineEntryKindControllerImpl lineEntryKindController = null;

    /**
   * The Controller for Services.
   */
    protected ServiceControllerImpl serviceController = null;

    /**
   * The Controller for DeliveryNotes.
   */
    protected DeliveryNoteControllerImpl deliveryNoteController = null;

    /**
   * The Controller for DeliveryNoteLineEntrys.
   */
    protected DeliveryNoteLineEntryControllerImpl deliveryNoteLineEntryController = null;

    /**
   * The Controller for ArticleTypes.
   */
    protected ArticleTypeControllerImpl articleTypeController = null;

    /**
   * The Controller for PriceCategories
   */
    protected PriceCategoryControllerImpl priceCategoryController = null;

    /**
   * The Controller for SpecialPrices
   */
    protected SpecialPriceControllerImpl specialPriceController = null;

    /** 
   * The Controller for PaymentTerms 
   */
    protected PaymentTermsControllerImpl paymentTermsController = null;

    /**
   * The Controller for PartLists.
   */
    protected PartsListControllerImpl partsListController = null;

    /**
   * The list of all OrderRateLineEntryControllers.
   */
    protected Vector allOrderRateLineEntryController = new Vector();

    /**
   * The list of all InvoiceRateLineEntryControllers.
   */
    protected Vector allInvoiceRateLineEntryController = new Vector();

    /**
   * The list of all OrderLineEntryControllers.
   */
    protected Vector allOrderLineEntryController = new Vector();

    /**
   * The list of all InvoiceLineEntryControllers.
   */
    protected Vector allInvoiceLineEntryController = new Vector();

    /**
   * The BuisinessPartnerController for queries on Companies and Persons.
   */
    protected BusinessPartnerControllerImpl businessPartnerController = null;

    /** The Controller for Stocks */
    protected StockControllerImpl stockController = null;

    /** The Controller for StockEntrys */
    protected StockEntryControllerImpl stockEntryController = null;

    /** The Controller for KindOfStockTransactions */
    protected KindOfStockTransactionControllerImpl kindOfStockTransactionController = null;

    /** The Controller for StockTransactions */
    protected StockTransactionControllerImpl stockTransactionController = null;

    /** The Controller for VATs */
    protected VATControllerImpl vatController = null;

    /** The Controller for MenuFolders */
    protected MenuFolderControllerImpl menuFolderController = null;

    /** The Controller for MenuPrograms */
    protected MenuProgramControllerImpl menuProgramController = null;

    /** The Controller for DeliveryNotePackages */
    protected DeliveryNotePackageControllerImpl deliveryNotePackageController = null;

    /** The Controller for DeliveryNotePackageLineEntrys */
    protected DeliveryNotePackageLineEntryControllerImpl deliveryNotePackageLineEntryController = null;

    /** The Controller for PackageKinds */
    protected PackageKindControllerImpl packageKindController = null;

    /** The Controller for ShipmentKinds */
    protected ShipmentKindControllerImpl shipmentKindController = null;

    /** Right-Groups. */
    protected static final String RG_FINANCIAL = "FINANCIAL";

    protected static final String RG_CUSTOMERS = "CUSTOMERS";

    protected static final String RG_ORDERS = "ORDERS";

    protected static final String RG_SERVICES = "SERVICES";

    protected static final String RG_ARTICLES = "ARTICLES";

    protected static final String RG_INVOICES = "INVOICES";

    protected static final String RG_DELNOTES = "DELNOTES";
}
