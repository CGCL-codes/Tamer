package com.tll.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * Order trans entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Order Id, Date Created and Username", properties = { "order.id", "dateCreated", "username" }))
public class OrderTrans extends TimeStampEntity implements IChildEntity<Order>, IAccountRelatedEntity {

    private static final long serialVersionUID = 8026809773722347843L;

    public static final int MAXLEN_USERNAME = 32;

    public static final int MAXLEN_SHIP_MODE_NAME = 64;

    public static final int MAXLEN_SHIP_ROUTING_NUM = 64;

    private Order order;

    private String username;

    private OrderTransOp orderTransOp;

    private OrderTransOpResult orderTransResult;

    private String shipModeName;

    private String shipRoutingNum;

    private float itemTotal = 0f;

    private float salesTax = 0f;

    private float shipCost = 0f;

    private float total = 0f;

    private Address billToAddress;

    private Address shipToAddress;

    private PaymentInfo pymntInfo;

    private PaymentTrans pymntTrans;

    private Set<OrderItemTrans> itemTransactions = new LinkedHashSet<OrderItemTrans>();

    @Override
    public Class<? extends IEntity> entityClass() {
        return OrderTrans.class;
    }

    public Address getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(final Address billToAddress) {
        this.billToAddress = billToAddress;
    }

    public float getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(final float itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Set<OrderItemTrans> getItemTransactions() {
        return itemTransactions;
    }

    public void setItemTransactions(final Set<OrderItemTrans> itemTransactions) {
        this.itemTransactions = itemTransactions;
    }

    @NotNull
    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @NotNull
    public OrderTransOp getOrderTransOp() {
        return orderTransOp;
    }

    public void setOrderTransOp(final OrderTransOp orderTransOp) {
        this.orderTransOp = orderTransOp;
    }

    @NotNull
    public OrderTransOpResult getOrderTransResult() {
        return orderTransResult;
    }

    public void setOrderTransResult(final OrderTransOpResult orderTransResult) {
        this.orderTransResult = orderTransResult;
    }

    public PaymentInfo getPymntInfo() {
        return pymntInfo;
    }

    public void setPymntInfo(final PaymentInfo pymntInfo) {
        this.pymntInfo = pymntInfo;
    }

    public PaymentTrans getPymntTrans() {
        return pymntTrans;
    }

    public void setPymntTrans(final PaymentTrans pymntTrans) {
        this.pymntTrans = pymntTrans;
    }

    public float getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(final float salesTax) {
        this.salesTax = salesTax;
    }

    public float getShipCost() {
        return shipCost;
    }

    public void setShipCost(final float shipCost) {
        this.shipCost = shipCost;
    }

    @Length(max = MAXLEN_SHIP_MODE_NAME)
    public String getShipModeName() {
        return shipModeName;
    }

    public void setShipModeName(final String shipModeName) {
        this.shipModeName = shipModeName;
    }

    @Length(max = MAXLEN_SHIP_ROUTING_NUM)
    public String getShipRoutingNum() {
        return shipRoutingNum;
    }

    public void setShipRoutingNum(final String shipRoutingNum) {
        this.shipRoutingNum = shipRoutingNum;
    }

    public Address getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(final Address shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(final float total) {
        this.total = total;
    }

    @NotEmpty
    @Length(max = MAXLEN_USERNAME)
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public OrderItemTrans getOrderItemTrans(final Object pk) {
        return findEntityInCollection(itemTransactions, pk);
    }

    public void addOrderItemTrans(final OrderItemTrans e) {
        addEntityToCollection(itemTransactions, e);
    }

    public void removeOrderItemTrans(final OrderItemTrans e) {
        removeEntityFromCollection(itemTransactions, e);
    }

    public void clearOrderItemTransactions() {
        clearEntityCollection(itemTransactions);
    }

    public int getNumItemTransactions() {
        return getCollectionSize(itemTransactions);
    }

    @Override
    public Order getParent() {
        return getOrder();
    }

    @Override
    public void setParent(final Order e) {
        setOrder(e);
    }

    @Override
    public Long accountKey() {
        try {
            return getOrder().getAccount().getId();
        } catch (final NullPointerException npe) {
            LOG.warn("Unable to provide related account id due to a NULL nested entity");
            return null;
        }
    }

    public Long orderId() {
        try {
            return getOrder().getId();
        } catch (final NullPointerException npe) {
            return null;
        }
    }
}
