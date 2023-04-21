package no.ugland.utransprod.gui.model;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import no.ugland.utransprod.model.ArticleType;
import no.ugland.utransprod.model.ArticleTypeAttribute;
import no.ugland.utransprod.model.Assembly;
import no.ugland.utransprod.model.Colli;
import no.ugland.utransprod.model.ConstructionType;
import no.ugland.utransprod.model.Customer;
import no.ugland.utransprod.model.Deviation;
import no.ugland.utransprod.model.Order;
import no.ugland.utransprod.model.OrderComment;
import no.ugland.utransprod.model.OrderCost;
import no.ugland.utransprod.model.OrderLine;
import no.ugland.utransprod.model.OrderLineAttribute;
import no.ugland.utransprod.model.PostShipment;
import no.ugland.utransprod.model.ProductArea;
import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.model.Supplier;
import no.ugland.utransprod.model.Transport;
import no.ugland.utransprod.service.enums.LazyLoadOrderEnum;
import no.ugland.utransprod.util.Util;
import org.apache.commons.lang.StringUtils;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * GUI-modell for ordre
 * 
 * @author atle.brekka
 */
public class OrderModel extends AbstractOrderModel<Order, OrderModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Id
	 */
    public static final String PROPERTY_ORDER_ID = "orderId";

    /**
	 * Adresse
	 */
    public static final String PROPERTY_DELIVERY_ADDRESS = "deliveryAddress";

    /**
	 * Postnummer
	 */
    public static final String PROPERTY_POSTAL_CODE = "postalCode";

    /**
	 * Poststed
	 */
    public static final String PROPERTY_POST_OFFICE = "postOffice";

    /**
	 * Kunde
	 */
    public static final String PROPERTY_CUSTOMER = "customer";

    /**
	 * Garasjetype
	 */
    public static final String PROPERTY_CONSTRUCTION_TYPE = "constructionType";

    /**
	 * Transport
	 */
    public static final String PROPERTY_TRANSPORT = "transport";

    /**
	 * Montering
	 */
    public static final String PROPERTY_DO_ASSEMBLY = "doAssembly";

    /**
	 * Kundenr
	 */
    public static final String PROPERTY_CUSTOMER_NR = "customerNr";

    /**
	 * Kundefornavn
	 */
    public static final String PROPERTY_CUSTOMER_FIRST_NAME = "customerFirstName";

    /**
	 * Kundeetternavn
	 */
    public static final String PROPERTY_CUSTOMER_LAST_NAME = "customerLastName";

    /**
	 * Montering
	 */
    public static final String PROPERTY_ASSEMBLY = "assembly";

    /**
     * 
     */
    public static final String PROPERTY_ORDER_DATE = "orderDate";

    /**
     * 
     */
    public static final String PROPERTY_INVOICE_DATE = "invoiceDate";

    /**
     * 
     */
    public static final String PROPERTY_INVOICED_BOOL = "invoicedBool";

    /**
     * 
     */
    public static final String PROPERTY_ASSEMBLY_YEAR = "assemblyYear";

    /**
     * 
     */
    public static final String PROPERTY_ASSEMBLY_WEEK = "assemblyWeek";

    /**
     * 
     */
    public static final String PROPERTY_SUPPLIER = "supplier";

    /**
     * 
     */
    public static final String PROPERTY_AGREEMENT_DATE = "agreementDate";

    /**
     * 
     */
    public static final String PROPERTY_TELEPHONE_NR = "telephoneNr";

    /**
     * 
     */
    public static final String PROPERTY_DELIVERY_WEEK = "deliveryWeek";

    public static final String PROPERTY_PACKLIST_READY = "packlistReady";

    public static final String PROPERTY_SALESMAN = "salesman";

    public static final String PROPERTY_PAID_DATE = "paidDate";

    public static final String PROPERTY_CACHED_COMMENT = "cachedComment";

    public static final String PROPERTY_PRODUCT_AREA = "productArea";

    public static final String PROPERTY_ORDER_READY_STRING = "orderReadyString";

    public static final String PROPERTY_GAVL_DONE = "gavlDone";

    public static final String PROPERTY_TAKSTOL_DONE = "takstolDone";

    public static final String PROPERTY_FRONT_DONE = "frontDone";

    public static final String PROPERTY_VEGG_DONE = "veggDone";

    public static final String PROPERTY_ASSEMBLY_DONE_STRING = "assemblyDoneString";

    public static final String PROPERTY_TAKSTOL_PACKAGED = "takstolPackaged";

    public static final String PROPERTY_REGISTRATION_DATE_STRING = "registrationDateString";

    public static final String PROPERTY_PRODUCTION_DATE = "productionDate";

    public static final String PROPERTY_LOADING_DATE_STRING = "loadingDateString";

    public static final String PROPERTY_PROJECT_NR = "projectNr";

    public static final String PROPERTY_PROJECT_NAME = "projectName";

    public static final String PROPERTY_CUTTING_FILE_NAME = "cuttingFileName";

    public static final String PROPERTY_PROBABILITY = "probability";

    public static final String PROPERTY_TELEPHONE_NR_SITE = "telephoneNrSite";

    public static final String PROPERTY_MAX_TROSS_HEIGHT = "maxTrossHeight";

    public static final String PROPERTY_PRODUCT_AREA_GROUP = "productAreaGroup";

    /**
	 * Kundenr
	 */
    private String customerNr;

    /**
	 * Fornavn
	 */
    private String customerFirstName;

    /**
	 * Etternavn
	 */
    private String customerLastName;

    private ArrayListModel costList = null;

    private Assembly assembly;

    private boolean canChangeInfo = false;

    private boolean canChangeStatus = false;

    private boolean colliesInitiated = false;

    private boolean isSearching = false;

    private String projectNr;

    private String projectName;

    private ProductAreaGroup productAreaGroup;

    /**
	 * @param order
	 * @param search
	 * @param changeInfo
	 * @param statusChange
	 */
    public OrderModel(final Order order, final boolean search, final boolean changeInfo, final boolean statusChange, final String aProjectNr, final String aProjectName) {
        super(order);
        projectNr = aProjectNr;
        projectName = aProjectName;
        isSearching = search;
        if (order != null) {
            productAreaGroup = order.getProductAreaGroup();
            canChangeInfo = changeInfo;
            canChangeStatus = statusChange;
            Customer orderCustomer = order.getCustomer();
            if (orderCustomer != null) {
                if (orderCustomer.getCustomerNr() != null) {
                    setCustomerNr(String.valueOf(orderCustomer.getCustomerNr()));
                }
                setCustomerFirstName(orderCustomer.getFirstName());
                setCustomerLastName(orderCustomer.getLastName());
            }
            assembly = order.getAssembly();
        }
    }

    public Integer getProbability() {
        return object.getProbability();
    }

    /**
	 * Henter artikler som ligger p� ordre
	 * 
	 * @return artikler
	 */
    public List<ArticleType> getArticles() {
        return object.getArticles();
    }

    /**
	 * @return selger
	 */
    public String getSalesman() {
        return object.getSalesman();
    }

    /**
	 * @param salesman
	 */
    public void setSalesman(String salesman) {
        String oldSalesman = getSalesman();
        object.setSalesman(salesman);
        firePropertyChange(PROPERTY_SALESMAN, oldSalesman, salesman);
    }

    public String getCuttingFileName() {
        return object.getCutting() != null ? object.getCutting().getProId() : null;
    }

    public void setCuttingFileName(String aFileName) {
        String oldFileName = getCuttingFileName();
        firePropertyChange(PROPERTY_CUTTING_FILE_NAME, oldFileName, aFileName);
    }

    /**
	 * Henter garasjetype
	 * 
	 * @return garasjetype
	 */
    public ConstructionType getConstructionType() {
        return object.getConstructionType();
    }

    /**
	 * Setter garasjetype
	 * 
	 * @param constructionType
	 */
    public void setConstructionType(ConstructionType constructionType) {
        ConstructionType oldType = getConstructionType();
        object.setConstructionType(constructionType);
        firePropertyChange(PROPERTY_CONSTRUCTION_TYPE, oldType, constructionType);
    }

    /**
	 * Henter kunde
	 * 
	 * @return kunde
	 */
    public Customer getCustomer() {
        return object.getCustomer();
    }

    /**
	 * Setter kunde
	 * 
	 * @param customer
	 */
    public void setCustomer(Customer customer) {
        Customer oldCust = getCustomer();
        if (customer != null) {
            setCustomerFirstName(customer.getFirstName());
            setCustomerLastName(customer.getLastName());
            setCustomerNr(String.valueOf(customer.getCustomerNr()));
        }
        object.setCustomer(customer);
        firePropertyChange(PROPERTY_CUSTOMER, oldCust, customer);
    }

    /**
	 * Henter adresse
	 * 
	 * @return adresse
	 */
    public String getDeliveryAddress() {
        return object.getDeliveryAddress();
    }

    /**
	 * Setter adresse
	 * 
	 * @param deliveryAddress
	 */
    public void setDeliveryAddress(String deliveryAddress) {
        String oldAddress = getDeliveryAddress();
        object.setDeliveryAddress(deliveryAddress);
        firePropertyChange(PROPERTY_DELIVERY_ADDRESS, oldAddress, deliveryAddress);
    }

    /**
	 * Henter postnummer
	 * 
	 * @return postnummer
	 */
    public String getPostalCode() {
        return object.getPostalCode();
    }

    /**
	 * @param postalCode
	 */
    public void setPostalCode(String postalCode) {
        String oldCode = getPostalCode();
        object.setPostalCode(postalCode);
        firePropertyChange(PROPERTY_POSTAL_CODE, oldCode, postalCode);
    }

    /**
	 * @return poststed
	 */
    public String getPostOffice() {
        return object.getPostOffice();
    }

    /**
	 * @param postOffice
	 */
    public void setPostOffice(String postOffice) {
        String oldOffice = getPostOffice();
        object.setPostOffice(postOffice);
        firePropertyChange(PROPERTY_POST_OFFICE, oldOffice, postOffice);
    }

    /**
	 * @return transport
	 */
    public Transport getTransport() {
        return object.getTransport();
    }

    /**
	 * @param transport
	 */
    public void setTransport(Transport transport) {
        Transport oldTransport = getTransport();
        object.setTransport(transport);
        firePropertyChange(PROPERTY_TRANSPORT, oldTransport, transport);
    }

    /**
	 * @return kundenummer
	 */
    public String getCustomerNr() {
        return customerNr;
    }

    /**
	 * @param customerNr
	 */
    public void setCustomerNr(String customerNr) {
        String oldNr = getCustomerNr();
        this.customerNr = customerNr;
        firePropertyChange(PROPERTY_CUSTOMER_NR, oldNr, customerNr);
    }

    /**
	 * @return fornavn
	 */
    public String getCustomerFirstName() {
        return customerFirstName;
    }

    /**
	 * @param customerFirstName
	 */
    public void setCustomerFirstName(String customerFirstName) {
        String oldName = getCustomerFirstName();
        this.customerFirstName = customerFirstName;
        firePropertyChange(PROPERTY_CUSTOMER_FIRST_NAME, oldName, customerFirstName);
    }

    /**
	 * @return etternavn
	 */
    public String getCustomerLastName() {
        return customerLastName;
    }

    /**
	 * @param customerLastName
	 */
    public void setCustomerLastName(String customerLastName) {
        String oldName = getCustomerLastName();
        this.customerLastName = customerLastName;
        firePropertyChange(PROPERTY_CUSTOMER_LAST_NAME, oldName, customerLastName);
    }

    /**
	 * @return montering
	 */
    public boolean isDoAssembly() {
        return Util.convertNumberToBoolean(object.getDoAssembly());
    }

    /**
	 * @return true dersom montering
	 */
    public Boolean getDoAssembly() {
        return Util.convertNumberToBoolean(object.getDoAssembly());
    }

    /**
	 * @param doAssembly
	 */
    public void setDoAssembly(boolean doAssembly) {
        boolean oldAssembly = isDoAssembly();
        object.setDoAssembly(Util.convertBooleanToNumber(doAssembly));
        firePropertyChange(PROPERTY_DO_ASSEMBLY, oldAssembly, doAssembly);
    }

    /**
	 * @return montering
	 */
    public Assembly getAssembly() {
        return object.getAssembly();
    }

    /**
	 * @param assembly
	 */
    public void setAssembly(Assembly assembly) {
        Assembly oldAssembly = getAssembly();
        object.setAssembly(assembly);
        firePropertyChange(PROPERTY_ASSEMBLY, oldAssembly, assembly);
    }

    /**
	 * @return dato for montering
	 */
    public String getAssemblyDoneString() {
        if (object.getAssembly() != null && object.getAssembly().getAssembliedDate() != null) {
            return Util.SHORT_DATE_FORMAT.format(object.getAssembly().getAssembliedDate());
        }
        return null;
    }

    /**
	 * @return monterings�r
	 */
    public Integer getAssemblyYear() {
        if (assembly != null && !Util.convertNumberToBoolean(assembly.getInactive())) {
            return assembly.getAssemblyYear();
        }
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
	 * @param year
	 */
    public void setAssemblyYear(Integer year) {
        Integer oldYear = getAssemblyYear();
        if (assembly == null) {
            assembly = new Assembly();
            assembly.setAssemblyYear(Calendar.getInstance().get(Calendar.YEAR));
        }
        assembly.setAssemblyYear(year);
        assembly.setInactive(0);
        firePropertyChange(PROPERTY_ASSEMBLY_YEAR, oldYear, year);
    }

    /**
	 * @return monteringsuke
	 */
    public Integer getAssemblyWeek() {
        if (assembly != null && !Util.convertNumberToBoolean(assembly.getInactive())) {
            return assembly.getAssemblyWeek();
        }
        return null;
    }

    /**
	 * @param week
	 */
    public void setAssemblyWeek(Integer week) {
        Integer oldWeek = getAssemblyWeek();
        if (assembly == null) {
            assembly = new Assembly();
            assembly.setAssemblyYear(Calendar.getInstance().get(Calendar.YEAR));
        }
        assembly.setAssemblyWeek(week);
        assembly.setInactive(0);
        firePropertyChange(PROPERTY_ASSEMBLY_WEEK, oldWeek, week);
    }

    /**
	 * @return monteringslag
	 */
    public Supplier getSupplier() {
        if (assembly != null && !Util.convertNumberToBoolean(assembly.getInactive())) {
            return assembly.getSupplier();
        }
        return null;
    }

    /**
	 * @param assemblyTeam
	 */
    public void setSupplier(final Supplier aSupplier) {
        Supplier oldSupplier = getSupplier();
        if (assembly == null) {
            assembly = new Assembly();
            assembly.setAssemblyYear(Calendar.getInstance().get(Calendar.YEAR));
        }
        assembly.setSupplier(aSupplier);
        assembly.setInactive(0);
        firePropertyChange(PROPERTY_SUPPLIER, oldSupplier, aSupplier);
    }

    /**
	 * @return cached kommentarer
	 */
    public String getCachedComment() {
        return object.getCachedComment();
    }

    /**
	 * @param cachedComment
	 */
    public void setCachedComment(String cachedComment) {
        String oldComment = getCachedComment();
        object.setCachedComment(cachedComment);
        firePropertyChange(PROPERTY_CACHED_COMMENT, oldComment, cachedComment);
    }

    /**
	 * Sender hendelse om at all egenskaperkan ha endret seg
	 */
    public void firePropertiesChanged() {
        fireMultiplePropertiesChanged();
    }

    /**
	 * @return ordreid
	 */
    public Integer getOrderId() {
        return object.getOrderId();
    }

    /**
	 * @param orderId
	 */
    public void setOrderId(Integer orderId) {
        Integer oldId = getOrderId();
        object.setOrderId(orderId);
        firePropertyChange(PROPERTY_ORDER_ID, oldId, orderId);
    }

    /**
	 * @return ordredato
	 */
    public Date getOrderDate() {
        return object.getOrderDate();
    }

    /**
	 * @param orderDate
	 */
    public void setOrderDate(Date orderDate) {
        Date oldDate = getOrderDate();
        object.setOrderDate(Util.convertDate(orderDate, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_ORDER_DATE, oldDate, orderDate);
    }

    /**
	 * @return fakturadato
	 */
    public Date getInvoiceDate() {
        return object.getInvoiceDate();
    }

    /**
	 * @param invoiceDate
	 */
    public void setInvoiceDate(Date invoiceDate) {
        Date oldDate = getInvoiceDate();
        object.setInvoiceDate(Util.convertDate(invoiceDate, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_INVOICE_DATE, oldDate, invoiceDate);
    }

    /**
	 * @return orderdato
	 */
    public Date getAgreementDate() {
        return object.getAgreementDate();
    }

    /**
	 * @param agreementDate
	 */
    public void setAgreementDate(Date agreementDate) {
        Date oldDate = getAgreementDate();
        object.setAgreementDate(Util.convertDate(agreementDate, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_AGREEMENT_DATE, oldDate, agreementDate);
    }

    /**
	 * @return true dersom fakturert
	 */
    public Boolean getInvoicedBool() {
        if (object.getInvoiceDate() == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
	 * @param invoiced
	 */
    public void setInvoicedBool(Boolean invoiced) {
        Boolean oldBool = getInvoicedBool();
        if (invoiced) {
            setInvoiceDate(Calendar.getInstance().getTime());
        } else {
            setInvoiceDate(null);
        }
        firePropertyChange(PROPERTY_INVOICED_BOOL, oldBool, invoiced);
    }

    /**
	 * Henter ordrelinjer
	 * 
	 * @return ordrelinjer
	 */
    public ArrayListModel getOrderLineArrayListModel() {
        return new ArrayListModel(orderLineList);
    }

    public ArrayListModel getOrderLineList() {
        return new ArrayListModel(orderLineList);
    }

    /**
	 * Setter ordrelinjer
	 * 
	 * @param orderLines
	 */
    public void setOrderLineArrayListModel(ArrayListModel orderLines) {
        ArrayListModel oldOrderLines = getOrderLineArrayListModel();
        this.orderLineList.clear();
        if (orderLines != null) {
            this.orderLineList.addAll(orderLines);
        }
        firePropertyChange(PROPERTY_ORDER_LINE_ARRAY_LIST_MODEL, oldOrderLines, orderLines);
    }

    /**
	 * @return kostnader
	 */
    public ArrayListModel getCostList() {
        if (costList == null) {
            costList = new ArrayListModel();
            if (object.getOrderCosts() != null) {
                costList.addAll(object.getOrderCosts());
            }
        }
        return new ArrayListModel(costList);
    }

    /**
	 * @param costs
	 */
    public void setCostList(ArrayListModel costs) {
        ArrayListModel oldCosts = getCostList();
        if (costList == null) {
            costList = new ArrayListModel();
        }
        this.costList.clear();
        this.costList.addAll(costs);
        firePropertyChange(PROPERTY_COSTS, oldCosts, costs);
    }

    /**
	 * @return dato for gavl ferdig
	 */
    public String getGavlDone() {
        return object.getGavlDone();
    }

    /**
	 * @return dato for takstol ferdig
	 */
    public String getTakstolDone() {
        return object.getTakstolDone();
    }

    /**
	 * @return dato for front ferdig
	 */
    public String getFrontDone() {
        return object.getFrontDone();
    }

    /**
	 * @return dato for vegg ferdig
	 */
    public String getVeggDone() {
        return object.getVeggDone();
    }

    /**
	 * @return dato for n�r takstol er pakket
	 */
    public String getTakstolPackaged() {
        return object.getTakstolPackaged();
    }

    /**
	 * @see no.ugland.utransprod.gui.model.AbstractModel#viewToModel()
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void viewToModel() {
        Set<OrderComment> comments = object.getOrderComments();
        if (comments == null) {
            comments = new HashSet<OrderComment>();
        }
        comments.clear();
        comments.addAll(commentList);
        object.setOrderComments(comments);
        if (orderLineList != null) {
            Set<OrderLine> lines = object.getOrderLines();
            if (lines == null) {
                lines = new HashSet<OrderLine>();
            }
            lines.clear();
            lines.addAll(orderLineList);
            object.setOrderLines(lines);
        }
        if (costList != null) {
            Set<OrderCost> lines = object.getOrderCosts();
            if (lines == null) {
                lines = new HashSet<OrderCost>();
            }
            lines.clear();
            lines.addAll(costList);
            object.setOrderCosts(lines);
        }
        if (assembly != null && !isSearching && object.doAssembly()) {
            if (object.getAssembly() == null) {
                assembly.setFirstPlanned(assembly.toString());
            }
            assembly.setOrder(object);
            object.setAssembly(assembly);
        }
        if (colliList != null) {
            Set<Colli> collies = object.getCollies();
            if (collies == null) {
                collies = new HashSet<Colli>();
            }
            collies.clear();
            collies.addAll(colliList);
            object.setCollies(collies);
        }
    }

    /**
	 * @see no.ugland.utransprod.gui.model.AbstractModel#modelToView()
	 */
    @Override
    public void modelToView() {
        if (object.getOrderLines() != null) {
            orderLineList.clear();
            orderLineList.addAll(object.getOrderLines());
        }
        if (object.getOrderCosts() != null) {
            if (costList == null) {
                costList = new ArrayListModel();
            }
            costList.clear();
            costList.addAll(object.getOrderCosts());
        }
        if (colliList != null && object.getCollies() != null) {
            colliList.clear();
            colliList.addAll(object.getCollies());
        }
    }

    /**
	 * Kloner ordre
	 * 
	 * @param orgOrder
	 * @return klonet ordre
	 */
    public static Order cloneOrder(Order orgOrder) {
        Order clonedOrder = new Order();
        clonedOrder.setOrderId(orgOrder.getOrderId());
        clonedOrder.setConstructionType(orgOrder.getConstructionType());
        clonedOrder.setOrderLines(cloneOrderLines(orgOrder.getOrderLines()));
        return clonedOrder;
    }

    /**
	 * Kloner ordrelinjer
	 * 
	 * @param orderLines
	 * @return klonede ordrelinjer
	 */
    public static Set<OrderLine> cloneOrderLines(Set<OrderLine> orderLines) {
        HashSet<OrderLine> clonedLines = null;
        if (orderLines != null) {
            clonedLines = new HashSet<OrderLine>();
            for (OrderLine orderLine : orderLines) {
                clonedLines.add(new OrderLine(orderLine.getOrderLineId(), orderLine.getOrder(), orderLine.getConstructionTypeArticle(), orderLine.getArticleType(), cloneOrderLineAttributes(orderLine.getOrderLineAttributes()), orderLine.getOrderLineRef(), cloneOrderLines(orderLine.getOrderLines()), orderLine.getNumberOfItems(), orderLine.getDialogOrder(), orderLine.getProduced(), orderLine.getArticlePath(), orderLine.getColli(), orderLine.getHasArticle(), orderLine.getAttributeInfo(), orderLine.getIsDefault(), orderLine.getPostShipment(), orderLine.getExternalOrderLine(), orderLine.getDeviation(), orderLine.getActionStarted(), orderLine.getProductionUnit(), orderLine.getOrdNo(), orderLine.getLnNo()));
            }
        }
        return clonedLines;
    }

    /**
	 * Kloner attributter for ordlelinje
	 * 
	 * @param attributes
	 * @return klonede attributter
	 */
    public static Set<OrderLineAttribute> cloneOrderLineAttributes(Set<OrderLineAttribute> attributes) {
        HashSet<OrderLineAttribute> clonedAttributes = new HashSet<OrderLineAttribute>();
        if (attributes != null) {
            for (OrderLineAttribute attribute : attributes) {
                clonedAttributes.add(new OrderLineAttribute(attribute.getOrderLineAttributeId(), attribute.getOrderLine(), attribute.getConstructionTypeArticleAttribute(), attribute.getConstructionTypeAttribute(), attribute.getArticleTypeAttribute(), attribute.getAttributeValue(), attribute.getDialogOrder(), attribute.getAttributeName()));
            }
        }
        return clonedAttributes;
    }

    /**
	 * @param orgCosts
	 * @return klonede kostnader
	 */
    public static Collection<OrderCost> cloneCosts(Collection<OrderCost> orgCosts) {
        ArrayList<OrderCost> list = null;
        if (orgCosts != null) {
            list = new ArrayList<OrderCost>();
            for (OrderCost orderCost : orgCosts) {
                list.add(new OrderCost(orderCost.getOrderCostId(), orderCost.getOrder(), orderCost.getCostType(), orderCost.getCostUnit(), orderCost.getCostAmount(), orderCost.getInclVat(), orderCost.getSupplier(), orderCost.getInvoiceNr(), orderCost.getDeviation(), orderCost.getTransportCostBasis(), orderCost.getPostShipment(), orderCost.getComment()));
            }
        }
        return list;
    }

    /**
	 * @param orderLines
	 * @return sorterte ordrelinjer
	 */
    public static SortedSet<OrderLine> getOrderedOrderLines(Collection<OrderLine> orderLines) {
        TreeSet<OrderLine> orderedLines = null;
        if (orderLines != null) {
            orderedLines = new TreeSet<OrderLine>(new OrderLineComparator());
            for (OrderLine orderLine : orderLines) {
                orderedLines.add(orderLine);
            }
        }
        return orderedLines;
    }

    /**
	 * @param orderLineAttributes
	 * @return sorterte attributter
	 */
    public static SortedSet<OrderLineAttribute> getOrderedOrderLineAttributes(Set<OrderLineAttribute> orderLineAttributes) {
        TreeSet<OrderLineAttribute> orderedAttributes = null;
        if (orderLineAttributes != null) {
            orderedAttributes = new TreeSet<OrderLineAttribute>(new OrderLineAttributeComparator());
            for (OrderLineAttribute attribute : orderLineAttributes) {
                orderedAttributes.add(attribute);
            }
        }
        return orderedAttributes;
    }

    /**
	 * Komparator for ordrelinje
	 * 
	 * @author atle.brekka
	 */
    static class OrderLineComparator implements Comparator<OrderLine> {

        /**
		 * @param orderLine1
		 * @param orderLine2
		 * @return sortert
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
        public int compare(OrderLine orderLine1, OrderLine orderLine2) {
            Integer order1 = Util.nullToInteger1000(orderLine1.getDialogOrder());
            Integer order2 = Util.nullToInteger1000(orderLine2.getDialogOrder());
            int compareValue = -1;
            if (orderLine1.getDialogOrder() == null && orderLine2.getDialogOrder() == null) {
                compareValue = orderLine1.toString().compareTo(orderLine2.toString());
            } else {
                compareValue = order1.compareTo(order2);
            }
            if (compareValue == 0 && (orderLine1 != orderLine2)) {
                return -1;
            }
            return compareValue;
        }
    }

    /**
	 * Komparator for attributter
	 * 
	 * @author atle.brekka
	 */
    static class OrderLineAttributeComparator implements Comparator<OrderLineAttribute> {

        /**
		 * @param orderLineAttribute1
		 * @param orderLineAttribute2
		 * @return sortert
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
        public int compare(OrderLineAttribute orderLineAttribute1, OrderLineAttribute orderLineAttribute2) {
            Integer order1 = Util.nullToInteger1000(orderLineAttribute1.getDialogOrder());
            Integer order2 = Util.nullToInteger1000(orderLineAttribute2.getDialogOrder());
            int compareValue = -1;
            if (orderLineAttribute1.getDialogOrder() == null && orderLineAttribute2.getDialogOrder() == null) {
                compareValue = orderLineAttribute1.getAttributeName().compareTo(orderLineAttribute2.getAttributeName());
            } else {
                compareValue = order1.compareTo(order2);
            }
            if (compareValue == 0 && !orderLineAttribute1.equals(orderLineAttribute2)) {
                return -1;
            }
            return compareValue;
        }
    }

    /**
	 * Kloner ordrelinje
	 * 
	 * @param order
	 * @param orderLine
	 * @return ordrelinje
	 */
    public static OrderLine cloneOrderLine(Order order, OrderLine orderLine) {
        return new OrderLine(orderLine.getOrderLineId(), order, orderLine.getConstructionTypeArticle(), orderLine.getArticleType(), orderLine.getOrderLineAttributes(), orderLine.getOrderLineRef(), orderLine.getOrderLines(), orderLine.getNumberOfItems(), orderLine.getDialogOrder(), orderLine.getProduced(), orderLine.getArticlePath(), orderLine.getColli(), orderLine.getHasArticle(), orderLine.getAttributeInfo(), orderLine.getIsDefault(), orderLine.getPostShipment(), orderLine.getExternalOrderLine(), orderLine.getDeviation(), orderLine.getActionStarted(), orderLine.getProductionUnit(), orderLine.getOrdNo(), orderLine.getLnNo());
    }

    /**
	 * @return true dersom info kan endre seg
	 */
    public boolean canChangeInfo() {
        return canChangeInfo;
    }

    /**
	 * @param canChangeInfo
	 */
    public void setCanChangeInfo(boolean canChangeInfo) {
        this.canChangeInfo = canChangeInfo;
    }

    /**
	 * @return true dersom status kan endre seg
	 */
    public boolean canChangeStatus() {
        return canChangeStatus;
    }

    /**
	 * Konverterer Set til List
	 * 
	 * @param orders
	 * @return liste med ordremodeller
	 */
    public static List<OrderModel> convertOrderSetToList(List<Order> orders) {
        List<OrderModel> orderModelList = new ArrayList<OrderModel>();
        if (orders != null) {
            for (Order order : orders) {
                orderModelList.add(new OrderModel(order, false, false, false, null, null));
            }
        }
        return orderModelList;
    }

    /**
	 * @see no.ugland.utransprod.gui.model.Packable#getColliList()
	 */
    public Integer getColliesDone() {
        return object.getColliesDone();
    }

    /**
	 * @see no.ugland.utransprod.gui.model.Packable#setColliesDone(java.lang.Integer)
	 */
    public void setColliesDone(Integer colliesDone) {
        object.setColliesDone(colliesDone);
    }

    /**
	 * @return true dersom kollier er initiert
	 */
    public boolean isColliesInitiated() {
        return colliesInitiated;
    }

    /**
	 * @param colliesInitiated
	 */
    public void setColliesInitiated(boolean colliesInitiated) {
        this.colliesInitiated = colliesInitiated;
    }

    /**
	 * @param orderLine
	 */
    public void setOrderLineRemoveColli(OrderLine orderLine) {
        if (orderLine != null) {
            object.getOrderLines().add(orderLine);
        }
    }

    /**
	 * @see no.ugland.utransprod.gui.model.Packable#getOrderComplete()
	 */
    public Date getOrderComplete() {
        return object.getOrderComplete();
    }

    /**
	 * @see no.ugland.utransprod.gui.model.Packable#getOrderReady()
	 */
    public Date getOrderReady() {
        return object.getOrderReady();
    }

    /**
	 * @return leveringsuke
	 */
    public Integer getDeliveryWeek() {
        return object.getDeliveryWeek();
    }

    /**
	 * @param deliveryWeek
	 */
    public void setDeliveryWeek(Integer deliveryWeek) {
        Integer oldWeek = getDeliveryWeek();
        object.setDeliveryWeek(deliveryWeek);
        firePropertyChange(PROPERTY_DELIVERY_WEEK, oldWeek, deliveryWeek);
    }

    /**
	 * @return telefonnummer
	 */
    public String getTelephoneNr() {
        return object.getTelephoneNr();
    }

    /**
	 * @param telephoneNr
	 */
    public void setTelephoneNr(String telephoneNr) {
        String oldPhone = getTelephoneNr();
        object.setTelephoneNr(telephoneNr);
        firePropertyChange(PROPERTY_TELEPHONE_NR, oldPhone, telephoneNr);
    }

    public String getTelephoneNrSite() {
        return object.getTelephoneNrSite();
    }

    /**
	 * @param telephoneNr
	 */
    public void setTelephoneNrSite(String telephoneNr) {
        String oldPhone = getTelephoneNrSite();
        object.setTelephoneNrSite(telephoneNr);
        firePropertyChange(PROPERTY_TELEPHONE_NR_SITE, oldPhone, telephoneNr);
    }

    public String getMaxTrossHeight() {
        return object.getMaxTrossHeight() != null ? String.valueOf(object.getMaxTrossHeight()) : null;
    }

    /**
	 * @param telephoneNr
	 */
    public void setMaxTrossHeight(String maxHeight) {
        String oldMaxHeight = getMaxTrossHeight();
        object.setMaxTrossHeight(StringUtils.isNumeric(maxHeight) ? Integer.valueOf(maxHeight) : null);
        firePropertyChange(PROPERTY_MAX_TROSS_HEIGHT, oldMaxHeight, maxHeight);
    }

    /**
	 * @return dato for pakkliste
	 */
    public Date getPacklistReady() {
        return object.getPacklistReady();
    }

    /**
	 * @param packlistReady
	 */
    public void setPacklistReady(Date packlistReady) {
        Date oldReady = getPacklistReady();
        object.setPacklistReady(Util.convertDate(packlistReady, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_PACKLIST_READY, oldReady, packlistReady);
    }

    /**
	 * @return dato for forh�ndsbetaling
	 */
    public Date getPaidDate() {
        return object.getPaidDate();
    }

    /**
	 * @param paidDate
	 */
    public void setPaidDate(Date paidDate) {
        Date oldPaid = getPaidDate();
        object.setPaidDate(Util.convertDate(paidDate, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_PAID_DATE, oldPaid, paidDate);
    }

    /**
	 * @see no.ugland.utransprod.gui.model.ICostableModel#getDeviation()
	 */
    public Deviation getDeviation() {
        return null;
    }

    /**
	 * @see no.ugland.utransprod.gui.model.ICostableModel#getOrder()
	 */
    public Order getOrder() {
        return object;
    }

    /**
	 * /**
	 * 
	 * @param orderComment
	 */
    public void addComment(OrderComment orderComment) {
        if (orderComment != null) {
            orderComment.setOrder(object);
            List<OrderComment> oldList = new ArrayList<OrderComment>(commentList);
            commentList.add(orderComment);
            firePropertyChange(PROPERTY_COMMENTS, oldList, commentList);
        }
    }

    /**
	 * @return produktomr�de
	 */
    public ProductArea getProductArea() {
        return object.getProductArea();
    }

    /**
	 * @param productArea
	 */
    public void setProductArea(ProductArea productArea) {
        ProductArea oldArea = getProductArea();
        object.setProductArea(productArea);
        firePropertyChange(PROPERTY_PRODUCT_AREA, oldArea, productArea);
    }

    public ProductAreaGroup getProductAreaGroup() {
        return productAreaGroup;
    }

    /**
	 * @param productArea
	 */
    public void setProductAreaGroup(ProductAreaGroup productAreaGroup) {
        ProductAreaGroup oldArea = getProductAreaGroup();
        this.productAreaGroup = productAreaGroup;
        firePropertyChange(PROPERTY_PRODUCT_AREA_GROUP, oldArea, productAreaGroup);
    }

    /**
	 * @return dato for n�r order er klar
	 */
    public String getOrderReadyString() {
        if (object.getOrderReady() != null) {
            return Util.SHORT_DATE_FORMAT.format(object.getOrderReady());
        }
        return null;
    }

    /**
	 * @param orderReadyString
	 */
    public void setOrderReadyString(String orderReadyString) {
        String oldReady = getOrderReadyString();
        if (orderReadyString != null) {
            try {
                object.setOrderReady(Util.SHORT_DATE_FORMAT.parse(orderReadyString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            object.setOrderReady(null);
        }
        firePropertyChange(PROPERTY_ORDER_READY_STRING, oldReady, orderReadyString);
    }

    public String getRegistrationDateString() {
        if (object.getRegistrationDate() != null) {
            return Util.SHORT_DATE_FORMAT.format(object.getRegistrationDate());
        }
        return null;
    }

    public void setRegistrationDateString(String aRegistrationDateString) {
        String oldDate = getRegistrationDateString();
        if (aRegistrationDateString != null) {
            try {
                object.setRegistrationDate(Util.SHORT_DATE_FORMAT.parse(aRegistrationDateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            object.setRegistrationDate(null);
        }
        firePropertyChange(PROPERTY_REGISTRATION_DATE_STRING, oldDate, aRegistrationDateString);
    }

    public String getLoadingDateString() {
        if (object.getTransport() != null) {
            return Util.formatDate(object.getTransport().getLoadingDate(), Util.SHORT_DATE_FORMAT);
        }
        return null;
    }

    public final Date getProductionDate() {
        return object.getProductionDate();
    }

    public final void setProductionDate(final Date aDate) {
        Date oldDate = getProductionDate();
        object.setProductionDate(Util.convertDate(aDate, Util.SHORT_DATE_FORMAT));
        firePropertyChange(PROPERTY_PRODUCTION_DATE, oldDate, aDate);
    }

    public final String getProjectNr() {
        return projectNr;
    }

    public final void setProjectNr(final String aProjectNr) {
        String oldNr = getProjectNr();
        projectNr = aProjectNr;
        firePropertyChange(PROPERTY_PROJECT_NR, oldNr, aProjectNr);
    }

    public final String getProjectName() {
        return projectName;
    }

    public final void setProjectName(final String aProjectName) {
        String oldName = getProjectName();
        projectName = aProjectName;
        firePropertyChange(PROPERTY_PROJECT_NAME, oldName, aProjectName);
    }

    /**
	 * Henter bufferordre
	 * 
	 * @param presentationModel
	 * @return bufferordre
	 */
    @Override
    public OrderModel getBufferedObjectModel(PresentationModel presentationModel) {
        OrderModel orderModel = new OrderModel(new Order(), false, canChangeInfo, canChangeStatus, projectNr, projectName);
        orderModel.setOrderId((Integer) presentationModel.getBufferedValue(PROPERTY_ORDER_ID));
        orderModel.setCustomer((Customer) presentationModel.getBufferedValue(PROPERTY_CUSTOMER));
        orderModel.setOrderNr((String) presentationModel.getBufferedValue(PROPERTY_ORDER_NR));
        orderModel.setCustomerNr((String) presentationModel.getBufferedValue(PROPERTY_CUSTOMER_NR));
        orderModel.setCustomerFirstName((String) presentationModel.getBufferedValue(PROPERTY_CUSTOMER_FIRST_NAME));
        orderModel.setCustomerLastName((String) presentationModel.getBufferedValue(PROPERTY_CUSTOMER_LAST_NAME));
        orderModel.setConstructionType((ConstructionType) presentationModel.getBufferedValue(PROPERTY_CONSTRUCTION_TYPE));
        orderModel.setDeliveryAddress((String) presentationModel.getBufferedValue(PROPERTY_DELIVERY_ADDRESS));
        orderModel.setPostalCode((String) presentationModel.getBufferedValue(PROPERTY_POSTAL_CODE));
        orderModel.setPostOffice((String) presentationModel.getBufferedValue(PROPERTY_POST_OFFICE));
        orderModel.setTransport((Transport) presentationModel.getBufferedValue(PROPERTY_TRANSPORT));
        orderModel.setDoAssembly((Boolean) presentationModel.getBufferedValue(PROPERTY_DO_ASSEMBLY));
        orderModel.setOrderDate((Date) presentationModel.getBufferedValue(PROPERTY_ORDER_DATE));
        orderModel.setOrderLineArrayListModel((ArrayListModel) presentationModel.getBufferedValue(PROPERTY_ORDER_LINE_ARRAY_LIST_MODEL));
        orderModel.setCostList((ArrayListModel) presentationModel.getBufferedValue(PROPERTY_COSTS));
        orderModel.setInvoiceDate((Date) presentationModel.getBufferedValue(PROPERTY_INVOICE_DATE));
        orderModel.setAssembly((Assembly) presentationModel.getBufferedValue(PROPERTY_ASSEMBLY));
        orderModel.setSupplier((Supplier) presentationModel.getBufferedValue(PROPERTY_SUPPLIER));
        orderModel.setAssemblyWeek((Integer) presentationModel.getBufferedValue(PROPERTY_ASSEMBLY_WEEK));
        orderModel.setAssemblyYear((Integer) presentationModel.getBufferedValue(PROPERTY_ASSEMBLY_YEAR));
        orderModel.setOrderCompleteBool((Boolean) presentationModel.getBufferedValue(PROPERTY_ORDER_COMPLETE_BOOL));
        orderModel.setOrderReadyBool((Boolean) presentationModel.getBufferedValue(PROPERTY_ORDER_READY_BOOL));
        orderModel.setPackageStarted((Date) presentationModel.getBufferedValue(PROPERTY_PACKAGE_STARTED));
        orderModel.setAgreementDate((Date) presentationModel.getBufferedValue(PROPERTY_AGREEMENT_DATE));
        orderModel.setDeliveryWeek((Integer) presentationModel.getBufferedValue(PROPERTY_DELIVERY_WEEK));
        orderModel.setTelephoneNr((String) presentationModel.getBufferedValue(PROPERTY_TELEPHONE_NR));
        orderModel.setPacklistReady((Date) presentationModel.getBufferedValue(PROPERTY_PACKLIST_READY));
        orderModel.setPackedBy((String) presentationModel.getBufferedValue(PROPERTY_PACKED_BY));
        orderModel.setSalesman((String) presentationModel.getBufferedValue(PROPERTY_SALESMAN));
        orderModel.setPaidDate((Date) presentationModel.getBufferedValue(PROPERTY_PAID_DATE));
        orderModel.setCachedComment((String) presentationModel.getBufferedValue(PROPERTY_CACHED_COMMENT));
        orderModel.setProductArea((ProductArea) presentationModel.getBufferedValue(PROPERTY_PRODUCT_AREA));
        orderModel.setOrderReadyString((String) presentationModel.getBufferedValue(PROPERTY_ORDER_READY_STRING));
        orderModel.setGarageColliHeight((BigDecimal) presentationModel.getBufferedValue(PROPERTY_GARAGE_COLLI_HEIGHT));
        orderModel.setRegistrationDateString((String) presentationModel.getBufferedValue(PROPERTY_REGISTRATION_DATE_STRING));
        orderModel.setProductionDate((Date) presentationModel.getBufferedValue(PROPERTY_PRODUCTION_DATE));
        orderModel.setTelephoneNrSite((String) presentationModel.getBufferedValue(PROPERTY_TELEPHONE_NR_SITE));
        orderModel.setDefaultColliesGenerated((Integer) presentationModel.getBufferedValue(PROPERTY_DEFAULT_COLLIES_GENERATED));
        orderModel.setMaxTrossHeight((String) presentationModel.getBufferedValue(PROPERTY_MAX_TROSS_HEIGHT));
        orderModel.setProductAreaGroup((ProductAreaGroup) presentationModel.getBufferedValue(PROPERTY_PRODUCT_AREA_GROUP));
        return orderModel;
    }

    /**
	 * Legger lytter til � lytte p� bufferendringer
	 * 
	 * @param listener
	 * @param presentationModel
	 */
    @Override
    public void addBufferChangeListener(PropertyChangeListener listener, PresentationModel presentationModel) {
        presentationModel.getBufferedModel(PROPERTY_CUSTOMER).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_NR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CUSTOMER_NR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CUSTOMER_FIRST_NAME).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CUSTOMER_LAST_NAME).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CONSTRUCTION_TYPE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_DELIVERY_ADDRESS).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_POSTAL_CODE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_POST_OFFICE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_TRANSPORT).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_DO_ASSEMBLY).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_DATE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_LINE_ARRAY_LIST_MODEL).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_COSTS).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_INVOICE_DATE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_INVOICED_BOOL).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ASSEMBLY).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_SUPPLIER).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ASSEMBLY_WEEK).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ASSEMBLY_YEAR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_COMPLETE_BOOL).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_READY_BOOL).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PACKAGE_STARTED).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_AGREEMENT_DATE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_DELIVERY_WEEK).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_TELEPHONE_NR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PACKLIST_READY).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PACKED_BY).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_SALESMAN).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PAID_DATE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CACHED_COMMENT).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PRODUCT_AREA).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_READY_STRING).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_GARAGE_COLLI_HEIGHT).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_REGISTRATION_DATE_STRING).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PRODUCTION_DATE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PROJECT_NR).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PROJECT_NAME).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_CUTTING_FILE_NAME).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_TELEPHONE_NR_SITE).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_DEFAULT_COLLIES_GENERATED).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_MAX_TROSS_HEIGHT).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_PRODUCT_AREA_GROUP).addValueChangeListener(listener);
    }

    public Transportable getTransportable() {
        return object;
    }

    public List<OrderLine> getOwnOrderLines() {
        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        List<OrderLine> allOrderLines = getOrderLines();
        if (allOrderLines != null) {
            for (OrderLine orderLine : allOrderLines) {
                if (orderLine.belongTo(object)) {
                    orderLines.add(orderLine);
                }
            }
        }
        return orderLines;
    }

    public static Set<OrderLineAttribute> getOrderLinesAttributes(final Set<ArticleTypeAttribute> attributes, final OrderLine orderLine) {
        if (attributes != null) {
            Set<OrderLineAttribute> orderLineAttributes = new HashSet<OrderLineAttribute>();
            for (ArticleTypeAttribute articleTypeAttribute : attributes) {
                orderLineAttributes.add(new OrderLineAttribute(null, orderLine, null, null, articleTypeAttribute, "", null, articleTypeAttribute.getAttribute().getName()));
            }
            return orderLineAttributes;
        }
        return null;
    }

    @Override
    public String getManagerName() {
        return "orderManager";
    }

    @Override
    public PostShipment getOrderModelPostShipment() {
        return null;
    }

    public Order getOrderModelOrder() {
        return object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enum[] getEnums() {
        return new LazyLoadOrderEnum[] { LazyLoadOrderEnum.COLLIES, LazyLoadOrderEnum.ORDER_LINES, LazyLoadOrderEnum.COMMENTS };
    }

    @Override
    public Serializable getObjectId() {
        return object.getOrderId();
    }

    @Override
    public Integer getDefaultColliesGenerated() {
        return object.getDefaultColliesGenerated();
    }

    public void setDefaultColliesGenerated(Integer defaultGenerated) {
        Integer oldGenerated = getDefaultColliesGenerated();
        object.setDefaultColliesGenerated(defaultGenerated);
        firePropertyChange(PROPERTY_DEFAULT_COLLIES_GENERATED, oldGenerated, defaultGenerated);
    }

    public PostShipment getPostShipment() {
        return null;
    }

    public Set<Colli> getCollies() {
        return object.getCollies();
    }
}
