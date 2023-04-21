package org.castafiore.shoppingmall.checkout;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import org.castafiore.wfs.types.Article;

@Entity
public class Order extends Article {

    public static final int STATE_NEW = 10;

    public static final int PROCESSING = 11;

    public static final int ON_DELIVERY = 12;

    public static final int CANCELLED_BY_MERCHANT = 13;

    public static final int CANCELLED_BY_CUSTOMER = 14;

    public static final int DELIVERED = 15;

    public static final int CONFLICT = 16;

    public static final int PAID = 17;

    public static final int CLOSED = 18;

    public static final int COURT = 19;

    private String orderedBy;

    private String orderedFrom;

    private String code;

    private BigDecimal total = new BigDecimal(0);

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getOrderedFrom() {
        return orderedFrom;
    }

    public void setOrderedFrom(String orderedFrom) {
        this.orderedFrom = orderedFrom;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderEntry> getEntries() {
        return getChildren(OrderEntry.class).toList();
    }

    public void addEntry(OrderEntry entry) {
        addChild(entry);
        total = total.add(entry.getTotal());
    }

    public void deleteEntry(String code) {
        getChild(code).remove();
    }

    /**
	 * 
	 * The Merchant reviews orders He approves order for the specified product
	 * codes other codes are considered disapproved A Notification sent to
	 * customer saying the following orders is being processing. change status
	 * to PROCESSING
	 * 
	 * @param entry
	 */
    public void merchantStartProcessing() {
        setStatus(PROCESSING);
        getSession().save(this);
    }

    /**
	 * 
	 * The Merchant reviews orders He approves order for the specified product
	 * codes A Notification sent to customer saying the following orders have
	 * been approved change status to ON_DELIVERY
	 * 
	 * @param entry
	 */
    public void merchantApproveForDelivery() {
        setStatus(ON_DELIVERY);
        getSession().save(this);
    }

    /**
	 * 
	 * The Merchant reviews orders He approves order for the specified product
	 * codes other codes are considered disapprived A Notification sent to
	 * customer saying the following orders have been cancelled with reason
	 * below change status to CANCELLED_BY_MERCHANT
	 * 
	 * @param entry
	 */
    public void merchantCancelOrder(String reason) {
        setStatus(CANCELLED_BY_MERCHANT);
        getSession().save(this);
    }

    /**
	 * 
	 * The Customer later reviews orders not yet PROCESSING A Notification sent
	 * to merchant saying the following orders have been canceled with reason
	 * below change status to CANCELLED_BY_CUSTOMER
	 * 
	 * @param entry
	 */
    public void customerCancelOrder(String reason) {
        setStatus(CANCELLED_BY_CUSTOMER);
        getSession().save(this);
    }

    /**
	 * Upon delivery of product, customer approves for payment A notification
	 * sent to merchant saying that payment has been approved by customer change
	 * status to DELIVERED
	 * 
	 * however if merchant chose automatic credit encaissage dont send
	 * notification, we move directly to merchantAcknowledgePayment
	 * 
	 */
    public void customerApprovesForPayment() {
        setStatus(DELIVERED);
        getSession().save(this);
    }

    /**
	 * Upon delivery of product, customer unapprove for payment A notification
	 * sent to merchant saying that payment has not been approved by customer
	 * change status to CONFLICT create A conflict File
	 * 
	 * 
	 */
    public void customerUnApprovedForPayment() {
        setStatus(CONFLICT);
        getSession().save(this);
    }

    /**
	 * Merchant now simply acknowledg payment A notification is sent to customer
	 * saying the amount of credits has been deduced from your account for the
	 * following order A payment is created with details, the OrderEntries for
	 * this order Decrease in quantity is performed Decrease decrease in tokens
	 * is performed Increase in tokens is performed a log is created change
	 * status to PAID
	 */
    public void merchantAcknowledgePayment() {
        setStatus(PAID);
        getSession().save(this);
    }

    /**
	 * Merchant now simply acknowledg payment A notification is sent to customer
	 * saying the amount of credits has been deduced from your account for the
	 * following order A payment is created with details, the OrderEntries for
	 * this order
	 * 
	 * a log is created change status to CONFLICT
	 */
    public void merchantUnAcknowledgePayment() {
        setStatus(CONFLICT);
        getSession().save(this);
    }

    /**
	 * CLOSED
	 */
    public void administratorCloseConflict() {
        setStatus(CLOSED);
        getSession().save(this);
    }

    /**
	 * COURT
	 */
    public void administratorSendConflictToCourt() {
        setStatus(COURT);
        getSession().save(this);
    }

    /**
	 * CLOSED
	 */
    public void courtCloseConflict() {
        setStatus(CLOSED);
        getSession().save(this);
    }
}
