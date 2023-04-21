package org.openxava.test.ejb.xejb;

import java.util.*;
import java.math.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import org.openxava.ejbx.*;
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.validators.ValidationException;
import org.openxava.test.ejb.*;

/**
 * @ejb:bean name="Invoice" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/Invoice" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IInvoice"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Invoice" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o.customer_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o.customer_number = ?1 " 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Invoice o WHERE 1 = 1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Invoice o WHERE 1 = 1" 	
 * @ejb:finder signature="Collection findPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid = 'S'" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid = 'S'" 	
 * @ejb:finder signature="Collection findNotPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid <> 'S'" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findNotPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid <> 'S'" 
 * 
 * @jboss:table-name "XAVATEST_INVOICE"
 *
 * @author Javier Paniza
 */
public abstract class InvoiceBean extends EJBReplicableBase implements org.openxava.test.ejb.IInvoice, EntityBean {

    private boolean creating = false;

    private boolean modified = false;

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.InvoiceKey ejbCreate(Map values) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        executeSets(values);
        return null;
    }

    public void ejbPostCreate(Map values) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.InvoiceKey ejbCreate(org.openxava.test.ejb.InvoiceData data) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setData(data);
        setYear(data.getYear());
        setNumber(data.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.InvoiceData data) throws CreateException, ValidationException {
    }

    /**
	 * @ejb:create-method
	 */
    public org.openxava.test.ejb.InvoiceKey ejbCreate(org.openxava.test.ejb.InvoiceValue value) throws CreateException, ValidationException {
        initMembers();
        creating = true;
        modified = false;
        setInvoiceValue(value);
        setYear(value.getYear());
        setNumber(value.getNumber());
        return null;
    }

    public void ejbPostCreate(org.openxava.test.ejb.InvoiceValue value) throws CreateException, ValidationException {
    }

    public void ejbLoad() {
        creating = false;
        modified = false;
    }

    public void ejbStore() {
        if (creating) {
            creating = false;
            return;
        }
        if (!modified) return;
        modified = false;
    }

    private org.openxava.converters.TrimStringConverter commentConverter;

    private org.openxava.converters.TrimStringConverter getCommentConverter() {
        if (commentConverter == null) {
            try {
                commentConverter = (org.openxava.converters.TrimStringConverter) getMetaModel().getMapping().getConverter("comment");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "comment"));
            }
        }
        return commentConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "COMMENT"
	 */
    public abstract java.lang.String get_Comment();

    public abstract void set_Comment(java.lang.String newComment);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getComment() {
        try {
            return (String) getCommentConverter().toJava(get_Comment());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Comment", "Invoice", "String"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setComment(String newComment) {
        try {
            this.modified = true;
            set_Comment((java.lang.String) getCommentConverter().toDB(newComment));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Comment", "Invoice", "String"));
        }
    }

    private org.openxava.converters.DateUtilSQLConverter dateConverter;

    private org.openxava.converters.DateUtilSQLConverter getDateConverter() {
        if (dateConverter == null) {
            try {
                dateConverter = (org.openxava.converters.DateUtilSQLConverter) getMetaModel().getMapping().getConverter("date");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "date"));
            }
        }
        return dateConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DATE"
	 */
    public abstract java.sql.Date get_Date();

    public abstract void set_Date(java.sql.Date newDate);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.util.Date getDate() {
        try {
            return (java.util.Date) getDateConverter().toJava(get_Date());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Date", "Invoice", "java.util.Date"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setDate(java.util.Date newDate) {
        try {
            this.modified = true;
            set_Date((java.sql.Date) getDateConverter().toDB(newDate));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Date", "Invoice", "java.util.Date"));
        }
    }

    private org.openxava.converters.BooleanSNConverter paidConverter;

    private org.openxava.converters.BooleanSNConverter getPaidConverter() {
        if (paidConverter == null) {
            try {
                paidConverter = (org.openxava.converters.BooleanSNConverter) getMetaModel().getMapping().getConverter("paid");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "paid"));
            }
        }
        return paidConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "PAID"
	 */
    public abstract java.lang.String get_Paid();

    public abstract void set_Paid(java.lang.String newPaid);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public boolean isPaid() {
        try {
            return ((Boolean) getPaidConverter().toJava(get_Paid())).booleanValue();
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Paid", "Invoice", "boolean"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setPaid(boolean newPaid) {
        try {
            this.modified = true;
            set_Paid((java.lang.String) getPaidConverter().toDB(new Boolean(newPaid)));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "Paid", "Invoice", "boolean"));
        }
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getYearDiscount() {
        try {
            org.openxava.test.calculators.YearInvoiceDiscountCalculator yearDiscountCalculator = (org.openxava.test.calculators.YearInvoiceDiscountCalculator) getMetaModel().getMetaProperty("yearDiscount").getMetaCalculator().getCalculator();
            yearDiscountCalculator.setYear(getYear());
            return (java.math.BigDecimal) yearDiscountCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "YearDiscount", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setYearDiscount(java.math.BigDecimal newYearDiscount) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public int getDetailsCount() {
        try {
            org.openxava.test.calculators.DetailsCountCalculator detailsCountCalculator = (org.openxava.test.calculators.DetailsCountCalculator) getMetaModel().getMetaProperty("detailsCount").getMetaCalculator().getCalculator();
            detailsCountCalculator.setYear(getYear());
            detailsCountCalculator.setNumber(getNumber());
            detailsCountCalculator.setConnectionProvider(getPortableContext());
            return ((Integer) detailsCountCalculator.calculate()).intValue();
        } catch (NullPointerException ex) {
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "DetailsCount", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setDetailsCount(int newDetailsCount) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public boolean isConsiderable() {
        try {
            org.openxava.test.calculators.ConsiderableCalculator considerableCalculator = (org.openxava.test.calculators.ConsiderableCalculator) getMetaModel().getMetaProperty("considerable").getMetaCalculator().getCalculator();
            considerableCalculator.setAmount(getAmountsSum());
            return ((Boolean) considerableCalculator.calculate()).booleanValue();
        } catch (NullPointerException ex) {
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Considerable", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setConsiderable(boolean newConsiderable) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getSellerDiscount() {
        try {
            org.openxava.test.calculators.SellerInvoiceDiscountCalculator sellerDiscountCalculator = (org.openxava.test.calculators.SellerInvoiceDiscountCalculator) getMetaModel().getMetaProperty("sellerDiscount").getMetaCalculator().getCalculator();
            sellerDiscountCalculator.setSellerNumber(getCustomer().getSeller_number());
            return (java.math.BigDecimal) sellerDiscountCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "SellerDiscount", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setSellerDiscount(java.math.BigDecimal newSellerDiscount) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getAmountsSum() {
        try {
            org.openxava.test.calculators.AmountsSumCalculator amountsSumCalculator = (org.openxava.test.calculators.AmountsSumCalculator) getMetaModel().getMetaProperty("amountsSum").getMetaCalculator().getCalculator();
            amountsSumCalculator.setEntity(this);
            return (java.math.BigDecimal) amountsSumCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "AmountsSum", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setAmountsSum(java.math.BigDecimal newAmountsSum) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public String getImportance() {
        try {
            org.openxava.test.calculators.ImportanceCalculator importanceCalculator = (org.openxava.test.calculators.ImportanceCalculator) getMetaModel().getMetaProperty("importance").getMetaCalculator().getCalculator();
            importanceCalculator.setAmount(getAmountsSum());
            return (String) importanceCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Importance", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setImportance(String newImportance) {
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "YEAR"
	 */
    public abstract int getYear();

    /**
	  * 
	  */
    public abstract void setYear(int newYear);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getCustomerDiscount() {
        try {
            org.openxava.test.calculators.CustomerInvoiceDiscountCalculator customerDiscountCalculator = (org.openxava.test.calculators.CustomerInvoiceDiscountCalculator) getMetaModel().getMetaProperty("customerDiscount").getMetaCalculator().getCalculator();
            customerDiscountCalculator.setNumber(getCustomer_number());
            customerDiscountCalculator.setPaid(isPaid());
            return (java.math.BigDecimal) customerDiscountCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "CustomerDiscount", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setCustomerDiscount(java.math.BigDecimal newCustomerDiscount) {
    }

    private org.openxava.converters.BigDecimalNumberConverter vatPercentageConverter;

    private org.openxava.converters.BigDecimalNumberConverter getVatPercentageConverter() {
        if (vatPercentageConverter == null) {
            try {
                vatPercentageConverter = (org.openxava.converters.BigDecimalNumberConverter) getMetaModel().getMapping().getConverter("vatPercentage");
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("generator.create_converter_error", "vatPercentage"));
            }
        }
        return vatPercentageConverter;
    }

    /**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "VATPERCENTAGE"
	 */
    public abstract java.math.BigDecimal get_VatPercentage();

    public abstract void set_VatPercentage(java.math.BigDecimal newVatPercentage);

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getVatPercentage() {
        try {
            return (java.math.BigDecimal) getVatPercentageConverter().toJava(get_VatPercentage());
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "VatPercentage", "Invoice", "java.math.BigDecimal"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setVatPercentage(java.math.BigDecimal newVatPercentage) {
        try {
            this.modified = true;
            set_VatPercentage((java.math.BigDecimal) getVatPercentageConverter().toDB(newVatPercentage));
        } catch (org.openxava.converters.ConversionException ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.conversion_error", "VatPercentage", "Invoice", "java.math.BigDecimal"));
        }
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getCustomerTypeDiscount() {
        try {
            org.openxava.test.calculators.CustomerTypeInvoiceDiscountCalculator customerTypeDiscountCalculator = (org.openxava.test.calculators.CustomerTypeInvoiceDiscountCalculator) getMetaModel().getMetaProperty("customerTypeDiscount").getMetaCalculator().getCalculator();
            customerTypeDiscountCalculator.setType(getCustomer().getType());
            return (java.math.BigDecimal) customerTypeDiscountCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "CustomerTypeDiscount", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setCustomerTypeDiscount(java.math.BigDecimal newCustomerTypeDiscount) {
    }

    /**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
    public java.math.BigDecimal getVat() {
        try {
            org.openxava.test.calculators.PercentageCalculator vatCalculator = (org.openxava.test.calculators.PercentageCalculator) getMetaModel().getMetaProperty("vat").getMetaCalculator().getCalculator();
            vatCalculator.setPercentage(getVatPercentage());
            vatCalculator.setValue(getAmountsSum());
            return (java.math.BigDecimal) vatCalculator.calculate();
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Vat", "Invoice", ex.getLocalizedMessage()));
        }
    }

    public void setVat(java.math.BigDecimal newVat) {
    }

    /**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "NUMBER"
	 */
    public abstract int getNumber();

    /**
	  * 
	  */
    public abstract void setNumber(int newNumber);

    private org.openxava.test.ejb.InvoiceDetailHome detailsHome;

    /**
	 * @ejb:interface-method
	 */
    public java.util.Collection getDetails() {
        try {
            return getDetailsHome().findByInvoice(getYear(), getNumber());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_collection_elements_error", "Details", "Invoice"));
        }
    }

    private org.openxava.test.ejb.InvoiceDetailHome getDetailsHome() throws Exception {
        if (detailsHome == null) {
            detailsHome = (org.openxava.test.ejb.InvoiceDetailHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/InvoiceDetail"), org.openxava.test.ejb.InvoiceDetailHome.class);
        }
        return detailsHome;
    }

    private org.openxava.test.ejb.DeliveryHome deliveriesHome;

    /**
	 * @ejb:interface-method
	 */
    public void addToDeliveries(org.openxava.test.ejb.Delivery newElement) {
        if (newElement != null) {
            try {
                newElement.setInvoiceKey((org.openxava.test.ejb.InvoiceKey) getEntityContext().getPrimaryKey());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("add_collection_element_error", "Delivery", "Invoice"));
            }
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void removeFromDeliveries(org.openxava.test.ejb.Delivery toRemove) {
        if (toRemove != null) {
            try {
                toRemove.setInvoiceKey(null);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(XavaResources.getString("remove_collection_element_error", "Delivery", "Invoice"));
            }
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public java.util.Collection getDeliveries() {
        try {
            return getDeliveriesHome().findByInvoice(getYear(), getNumber());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_collection_elements_error", "Deliveries", "Invoice"));
        }
    }

    private org.openxava.test.ejb.DeliveryHome getDeliveriesHome() throws Exception {
        if (deliveriesHome == null) {
            deliveriesHome = (org.openxava.test.ejb.DeliveryHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Delivery"), org.openxava.test.ejb.DeliveryHome.class);
        }
        return deliveriesHome;
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.Customer getCustomer() {
        try {
            return getCustomerHome().findByPrimaryKey(getCustomerKey());
        } catch (ObjectNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("get_reference_error", "Customer", "Invoice"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public void setCustomer(org.openxava.test.ejb.Customer newCustomer) {
        this.modified = true;
        try {
            if (newCustomer == null) setCustomerKey(null); else setCustomerKey((org.openxava.test.ejb.CustomerKey) newCustomer.getPrimaryKey());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException(XavaResources.getString("set_reference_error", "Customer", "Invoice"));
        }
    }

    /**
	 * @ejb:interface-method
	 */
    public org.openxava.test.ejb.CustomerKey getCustomerKey() {
        org.openxava.test.ejb.CustomerKey key = new org.openxava.test.ejb.CustomerKey();
        key.number = getCustomer_number();
        return key;
    }

    /**
	 * @ejb:interface-method
	 */
    public void setCustomerKey(org.openxava.test.ejb.CustomerKey key) {
        this.modified = true;
        if (key == null) {
            key = new org.openxava.test.ejb.CustomerKey();
        }
        setCustomer_number(key.number);
    }

    /**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "CUSTOMER_NUMBER"
	 */
    public abstract int getCustomer_number();

    public abstract void setCustomer_number(int newCustomer_number);

    private org.openxava.test.ejb.CustomerHome customerHome;

    private org.openxava.test.ejb.CustomerHome getCustomerHome() throws Exception {
        if (customerHome == null) {
            customerHome = (org.openxava.test.ejb.CustomerHome) PortableRemoteObject.narrow(BeansContext.get().lookup("ejb/org.openxava.test.ejb/Customer"), org.openxava.test.ejb.CustomerHome.class);
        }
        return customerHome;
    }

    private MetaModel metaModel;

    private MetaModel getMetaModel() throws XavaException {
        if (metaModel == null) {
            metaModel = MetaComponent.get("Invoice").getMetaEntity();
        }
        return metaModel;
    }

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.InvoiceData getData();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setData(org.openxava.test.ejb.InvoiceData data);

    /**
	 * @ejb:interface-method
	 */
    public abstract org.openxava.test.ejb.InvoiceValue getInvoiceValue();

    /**
	 * @ejb:interface-method
	 */
    public abstract void setInvoiceValue(org.openxava.test.ejb.InvoiceValue value);

    public void setEntityContext(javax.ejb.EntityContext ctx) {
        super.setEntityContext(ctx);
    }

    public void unsetEntityContext() {
        super.unsetEntityContext();
    }

    private void initMembers() {
        setYear(0);
        setNumber(0);
        setDate(null);
        setVatPercentage(null);
        setComment(null);
        setPaid(false);
    }
}
