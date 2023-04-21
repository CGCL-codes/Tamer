package org.javacommerce.common.data;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import org.apache.ojb.broker.util.collections.ManageableArrayList;
import org.javacommerce.common.Amount;
import org.javacommerce.common.OrderStatusType;

/**
 * @author Michael Blanton
 * @version $Revision: 1.8 $
 * @hibernate.joined-subclass 
 * @hibernate.joined-subclass-key column="product_option_id"
 * @ojb.class table="tbl_order_item"
 * @ojb.field primarykey="true"
 *            column="order_id"
 *            name="orderId"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="ship_to_person_id"
 *            name="shipToPersonId"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="shipping_address_id"
 *            name="shippingAddressId"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="shipping_phone_number_id"
 *            name="shippingPhoneNumberId"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="product_option_id"
 *            name="productOptionId"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="created_by_id"
 *            name="createdById"
 *            jdbc-type="VARCHAR"
 *            length="36"
 * @ojb.field column="modified_by_id"
 *            name="modifiedById"
 *            jdbc-type="VARCHAR"
 *            length="36"
 */
public class OrderItem {

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.Order"
	 *                foreignkey="orderId"
	 */
    private Order order;

    /**
	 * @ojb.field primarykey="true"
	 *            column="line_item"
	 */
    private int lineItem;

    /**
	 * @ojb.field column="quantity"
	 */
    private int quantity;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.Contact"
	 *               foreignkey="shipToPersonId"
	 */
    private Contact shipToPerson;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.Address"
	 *               foreignkey="shippingAddressId"
	 */
    private Address shippingAddress;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.PhoneNumber"
	 *               foreignkey="shippingPhoneNumberId"
	 */
    private PhoneNumber shippingPhoneNumber;

    /**
	 * @ojb.field column="order_item_status"
	 */
    private String orderItemStatus;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.ProductOption"
	 *                foreignkey="productOptionId"
	 */
    private ProductOption productOption;

    /**
	 * @ojb.field column="sku"
	 *            length="32"
	 */
    private String sku;

    /**
	 * @ojb.field column="cost"
	 */
    private BigDecimal cost;

    /**
	 * @ojb.field column="cost_currency"
	 *            length="3"
	 */
    private String costCurrency;

    /**
	 * @ojb.field column="msrp"
	 */
    private BigDecimal msrp;

    /**
	 * @ojb.field column="msrp_currency"
	 *            length="32"
	 */
    private String msrpCurrency;

    /**
	 * @ojb.field column="list"
	 */
    private BigDecimal list;

    /**
	 * @ojb.field column="list_currency"
	 *            length="32"
	 */
    private String listCurrency;

    /**
	 * @ojb.field column="sale"
	 */
    private BigDecimal sale;

    /**
	 * @ojb.field column="sale_currency"
	 *            length="32"
	 */
    private String saleCurrency;

    /**
	 * @ojb.field column="upc"
	 *            length="32"
	 */
    private String upc;

    /**
	 * @ojb.field column="name"
	 *            length="64"
	 */
    private String name;

    /**
	 * @ojb.field column="description"
	 *            jdbc-type="BLOB"
	 */
    private String description;

    /**
	 * @ojb.field column="created_date"
	 */
    private Calendar dateCreated;

    /**
	 * @ojb.field column="last_modified"
	 */
    private Calendar lastModified;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.Account"
	 *                foreignkey="createdById"
	 */
    private Account createdBy;

    /**
	 * @ojb.reference class-ref="org.javacommerce.common.data.Account"
	 *                foreignkey="modifiedById"
	 */
    private Account modifiedBy;

    /**
	 * @ojb.collection element-class-ref="org.javacommerce.common.data.OrderItemHistory"
	 *                 foreignkey="orderItemId"
	 */
    private ManageableArrayList historyRecords;

    public OrderItem() {
        setHistoryRecords(new ManageableArrayList());
    }

    public String getDescriptionAsString() {
        return getDescription().toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Amount getCostAsAmount() {
        if (getCost() == null && getCostCurrency() == null) {
            return null;
        }
        Amount amtCost = new Amount();
        if (getCost() != null) {
            amtCost.setAmount(getCost());
        }
        if (getCostCurrency() != null) {
            amtCost.setCurrency(Currency.getInstance(getCostCurrency()));
        }
        return amtCost;
    }

    public void setCostAsAmount(Amount cost) {
        setCost(cost.getAmount());
        setCostCurrency(cost.getCurrency().getCurrencyCode());
    }

    public Amount getListAsAmount() {
        if (getList() == null && getListCurrency() == null) {
            return null;
        }
        Amount amtList = new Amount();
        if (getList() != null) {
            amtList.setAmount(getList());
        }
        if (getListCurrency() != null) {
            amtList.setCurrency(Currency.getInstance(getListCurrency()));
        }
        return amtList;
    }

    public void setListAsAmount(Amount list) {
        setList(list.getAmount());
        setListCurrency(list.getCurrency().getCurrencyCode());
    }

    public Amount getMsrpAsAmount() {
        if (getMsrp() == null && getMsrpCurrency() == null) {
            return null;
        }
        Amount amtMsrp = new Amount();
        if (getMsrp() != null) {
            amtMsrp.setAmount(getMsrp());
        }
        if (getMsrpCurrency() != null) {
            amtMsrp.setCurrency(Currency.getInstance(getMsrpCurrency()));
        }
        return amtMsrp;
    }

    public void setMsrpAsAmount(Amount msrp) {
        setMsrp(msrp.getAmount());
        setMsrpCurrency(msrp.getCurrency().getCurrencyCode());
    }

    public Amount getSaleAsAmount() {
        if (getSale() == null && getSaleCurrency() == null) {
            return null;
        }
        Amount amtSale = new Amount();
        if (getSale() != null) {
            amtSale.setAmount(getSale());
        }
        if (getSaleCurrency() != null) {
            amtSale.setCurrency(Currency.getInstance(getSaleCurrency()));
        }
        return amtSale;
    }

    public void setSaleAsAmount(Amount sale) {
        setSale(sale.getAmount());
        setSaleCurrency(sale.getCurrency().getCurrencyCode());
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public Account getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Account modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(String costCurrency) {
        this.costCurrency = costCurrency;
    }

    public BigDecimal getList() {
        return list;
    }

    public void setList(BigDecimal list) {
        this.list = list;
    }

    public String getListCurrency() {
        return listCurrency;
    }

    public void setListCurrency(String listCurrency) {
        this.listCurrency = listCurrency;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public String getMsrpCurrency() {
        return msrpCurrency;
    }

    public void setMsrpCurrency(String msrpCurrency) {
        this.msrpCurrency = msrpCurrency;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

    public String getSaleCurrency() {
        return saleCurrency;
    }

    public void setSaleCurrency(String saleCurrency) {
        this.saleCurrency = saleCurrency;
    }

    /**
	 * @hibernate.one-to-one
	 * @return
	 */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
	 * @hibernate.one-to-one
	 * @return
	 */
    public PhoneNumber getShippingPhoneNumber() {
        return shippingPhoneNumber;
    }

    public void setShippingPhoneNumber(PhoneNumber shippingPhoneNumber) {
        this.shippingPhoneNumber = shippingPhoneNumber;
    }

    /**
	 * @hibernate.one-to-one
	 * @return
	 */
    public Contact getShipToPerson() {
        return shipToPerson;
    }

    public void setShipToPerson(Contact shipToPerson) {
        this.shipToPerson = shipToPerson;
    }

    /**
	 * @hibernate.property column="date_created" not-null="true"
	 * @return
	 */
    public Calendar getTimestampCreated() {
        return dateCreated;
    }

    public void setTimestampCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
	 * @hibernate.property column="last_modified" not-null="true"
	 * @return
	 */
    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }

    public OrderStatusType getOrderItemStatus() {
        return null;
    }

    public void setOrderItemStatus(OrderStatusType orderItemStatus) {
        setOrderItemStatusAsString(orderItemStatus.toString());
    }

    /**
	 * @hibernate.property column="order_item_status" length="8" not-null="true"
	 * @return
	 */
    public String getOrderItemStatusAsString() {
        return orderItemStatus;
    }

    public void setOrderItemStatusAsString(String _itemStatus) {
        orderItemStatus = _itemStatus;
    }

    /**
	 * @hibernate.property column="line_item" not-null="true"
	 * @return
	 */
    public int getLineItem() {
        return lineItem;
    }

    public void setLineItem(int lineItem) {
        this.lineItem = lineItem;
    }

    /**
	 * @hibernate.many-to-one column="order_id"
	 * @return
	 */
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    /**
	 * @hibernate.property column="quantity"
	 * @return
	 */
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public synchronized int addHistoryRecord(OrderItemHistory _record) {
        getHistoryRecords().add(_record);
        return getHistoryRecords().size();
    }

    public ManageableArrayList getHistoryRecords() {
        return historyRecords;
    }

    public void setHistoryRecords(ManageableArrayList historyRecords) {
        this.historyRecords = historyRecords;
    }
}
