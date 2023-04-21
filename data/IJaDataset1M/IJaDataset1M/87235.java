package org.zeroexchange.model.resource.participant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.zeroexchange.model.resource.money.PriceType;
import org.zeroexchange.model.user.User;

/**
 * @author black
 *
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { ResourceTender.FIELD_USER + "_id", ResourceTender.FIELD_RESOURCE + "_id" }))
public class Supply extends ResourceTender {

    public static final String FIELD_TOTAL_PRICE = "totalPrice";

    private static final long serialVersionUID = 1L;

    private BigDecimal hours;

    private BigDecimal totalPrice;

    private String currency;

    private PriceType priceType;

    private Date supplyTime;

    /** Users who viewed the contract. */
    private Set<User> viewers;

    /**
     * @return the hours
     */
    public BigDecimal getHours() {
        return hours == null ? BigDecimal.ZERO : hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice == null ? BigDecimal.ZERO : totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PriceType getPriceType() {
        return priceType == null ? PriceType.TOTAL : priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSupplyTime() {
        return supplyTime;
    }

    public void setSupplyTime(Date supplyTime) {
        this.supplyTime = supplyTime;
    }

    @ManyToMany
    @JoinTable(name = "Supply_Viewer")
    public Set<User> getViewers() {
        if (viewers == null) {
            viewers = new HashSet<User>();
        }
        return viewers;
    }

    public void setViewers(Set<User> viewers) {
        this.viewers = viewers;
    }
}
