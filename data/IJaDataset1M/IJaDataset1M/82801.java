package org.petsoar.order;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @hibernate.class table="ORDERS"
 */
public class Order {

    public static final String ORDER_STATUS_PENDING = "Pending";

    public static final String ORDER_STATUS_SHIPPED = "Shipped";

    private long id;

    private ShipmentInfo shipmentInfo = new ShipmentInfo();

    private BillingInfo billingInfo = new BillingInfo();

    private CreditCardInfo creditCardInfo = new CreditCardInfo();

    private Set pets;

    private BigDecimal totalPrice = new BigDecimal(0d);

    private String status = ORDER_STATUS_PENDING;

    /**
     * @hibernate.id column="ORDERID" generator-class="increment" unsaved-value="0"
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShipmentInfo getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(ShipmentInfo shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public BillingInfo getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
    }

    /**
     * @hibernate.component
     */
    public CreditCardInfo getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(CreditCardInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }

    /**
     * The total price.
     * @hibernate.property column="PRICE"
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @hibernate.set table="ODER_PETS" lazy="true"
     * @hibernate.collection-many-to-many class="org.petsoar.pets.Pet" column="PET_ID"
     * @hibernate.collection-key column="ORDER_ID"
     */
    public Set getPets() {
        return pets;
    }

    public void setPets(Set pets) {
        this.pets = pets;
    }

    /**
     * The status of the Order.
     * @hibernate.property column="STATUS"
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(ORDER_STATUS_PENDING) || status.equals(ORDER_STATUS_SHIPPED)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid orderStatus");
        }
    }
}
