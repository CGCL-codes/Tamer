package org.broadleafcommerce.core.offer.domain;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.broadleafcommerce.core.offer.service.type.OfferDiscountType;
import org.broadleafcommerce.money.Money;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "BLC_OFFER_AUDIT")
@Inheritance(strategy = InheritanceType.JOINED)
public class OfferAuditImpl implements OfferAudit {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "OfferAuditId")
    @GenericGenerator(name = "OfferAuditId", strategy = "org.broadleafcommerce.persistence.IdOverrideTableGenerator", parameters = { @Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"), @Parameter(name = "segment_column_name", value = "ID_NAME"), @Parameter(name = "value_column_name", value = "ID_VAL"), @Parameter(name = "segment_value", value = "OfferAuditImpl"), @Parameter(name = "increment_size", value = "50"), @Parameter(name = "entity_name", value = "org.broadleafcommerce.core.offer.domain.OfferAuditImpl") })
    @Column(name = "OFFER_AUDIT_ID")
    protected Long id;

    @ManyToOne(targetEntity = OfferImpl.class)
    @JoinColumn(name = "OFFER_ID")
    @Index(name = "OFFERAUDIT_OFFER_INDEX", columnNames = { "OFFER_ID" })
    protected Offer offer;

    @Column(name = "OFFER_CODE_ID")
    @Index(name = "OFFERAUDIT_OFFERCODE_INDEX", columnNames = { "OFFER_CODE_ID" })
    protected Long offerCodeId;

    @Column(name = "CUSTOMER_ID")
    @Index(name = "OFFERAUDIT_CUSTOMER_INDEX", columnNames = { "CUSTOMER_ID" })
    protected Long customerId;

    @Column(name = "OFFER_TYPE")
    @Index(name = "OFFERAUDIT_TYPE_INDEX", columnNames = { "OFFER_TYPE" })
    protected String offerType;

    @Column(name = "RELATED_ID")
    @Index(name = "OFFERAUDIT_RELATED_INDEX", columnNames = { "RELATED_ID" })
    protected Long relatedId;

    @Column(name = "RELATED_RETAIL_PRICE", precision = 19, scale = 5)
    protected BigDecimal relatedRetailPrice;

    @Column(name = "RELATED_SALE_PRICE", precision = 19, scale = 5)
    protected BigDecimal relatedSalePrice;

    @Column(name = "RELATED_PRICE", precision = 19, scale = 5)
    protected BigDecimal relatedPrice;

    @Column(name = "REDEEMED_DATE")
    protected Date redeemedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Long getOfferCodeId() {
        return offerCodeId;
    }

    public void setOfferCodeId(Long offerCodeId) {
        this.offerCodeId = offerCodeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OfferDiscountType getOfferType() {
        return OfferDiscountType.getInstance(offerType);
    }

    public void setOfferType(OfferDiscountType offerType) {
        this.offerType = offerType.getType();
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Money getRelatedRetailPrice() {
        return relatedRetailPrice == null ? null : new Money(relatedRetailPrice);
    }

    public void setRelatedRetailPrice(Money relatedRetailPrice) {
        this.relatedRetailPrice = Money.toAmount(relatedRetailPrice);
    }

    public Money getRelatedSalePrice() {
        return relatedSalePrice == null ? null : new Money(relatedSalePrice);
    }

    public void setRelatedSalePrice(Money relatedSalePrice) {
        this.relatedSalePrice = Money.toAmount(relatedSalePrice);
    }

    public Money getRelatedPrice() {
        return relatedPrice == null ? null : new Money(relatedPrice);
    }

    public void setRelatedPrice(Money relatedPrice) {
        this.relatedPrice = Money.toAmount(relatedPrice);
    }

    public Date getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(Date redeemedDate) {
        this.redeemedDate = redeemedDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((offerCodeId == null) ? 0 : offerCodeId.hashCode());
        result = prime * result + ((offerType == null) ? 0 : offerType.hashCode());
        result = prime * result + ((redeemedDate == null) ? 0 : redeemedDate.hashCode());
        result = prime * result + ((relatedId == null) ? 0 : relatedId.hashCode());
        result = prime * result + ((relatedPrice == null) ? 0 : relatedPrice.hashCode());
        result = prime * result + ((relatedRetailPrice == null) ? 0 : relatedRetailPrice.hashCode());
        result = prime * result + ((relatedSalePrice == null) ? 0 : relatedSalePrice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        OfferAuditImpl other = (OfferAuditImpl) obj;
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        if (customerId == null) {
            if (other.customerId != null) return false;
        } else if (!customerId.equals(other.customerId)) return false;
        if (offer == null) {
            if (other.offer != null) return false;
        } else if (!offer.equals(other.offer)) return false;
        if (offerCodeId == null) {
            if (other.offerCodeId != null) return false;
        } else if (!offerCodeId.equals(other.offerCodeId)) return false;
        if (offerType == null) {
            if (other.offerType != null) return false;
        } else if (!offerType.equals(other.offerType)) return false;
        if (redeemedDate == null) {
            if (other.redeemedDate != null) return false;
        } else if (!redeemedDate.equals(other.redeemedDate)) return false;
        if (relatedId == null) {
            if (other.relatedId != null) return false;
        } else if (!relatedId.equals(other.relatedId)) return false;
        if (relatedPrice == null) {
            if (other.relatedPrice != null) return false;
        } else if (!relatedPrice.equals(other.relatedPrice)) return false;
        if (relatedRetailPrice == null) {
            if (other.relatedRetailPrice != null) return false;
        } else if (!relatedRetailPrice.equals(other.relatedRetailPrice)) return false;
        if (relatedSalePrice == null) {
            if (other.relatedSalePrice != null) return false;
        } else if (!relatedSalePrice.equals(other.relatedSalePrice)) return false;
        return true;
    }
}
