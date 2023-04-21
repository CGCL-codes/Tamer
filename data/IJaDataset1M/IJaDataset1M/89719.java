package no.ugland.utransprod.model;

import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Domeneklasse for SuperOffice tabell SALE
 * @author atle.brekka
 */
public class Sale extends BaseObject {

    private Integer saleId;

    private String number1;

    private BigDecimal amount;

    private Integer saledate;

    private Contact contact;

    private Integer userdefId;

    private Integer groupIdx;

    private Integer registered;

    private Integer probability;

    private Integer projectId;

    public Sale() {
        super();
    }

    public Sale(final Integer aSaleId, final String aNumber1, final BigDecimal aAmount, final Integer aSaledate, final Contact aContact, final Integer aUserdefId, final Integer aGroupIdx, final Integer isRegistered, final Integer aProbability, final Integer aProjectId) {
        super();
        this.saleId = aSaleId;
        this.number1 = aNumber1;
        this.amount = aAmount;
        this.saledate = aSaledate;
        this.contact = aContact;
        this.userdefId = aUserdefId;
        this.groupIdx = aGroupIdx;
        this.registered = isRegistered;
        this.probability = aProbability;
        this.projectId = aProjectId;
    }

    /**
     * @return bel�p
     */
    public final BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param aAmount
     */
    public final void setAmount(final BigDecimal aAmount) {
        this.amount = aAmount;
    }

    /**
     * @return kontakt
     */
    public final Contact getContact() {
        return contact;
    }

    /**
     * @param aContact
     */
    public final void setContact(final Contact aContact) {
        this.contact = aContact;
    }

    /**
     * @return nummer1
     */
    public final String getNumber1() {
        return number1;
    }

    /**
     * @param aNumber1
     */
    public final void setNumber1(final String aNumber1) {
        this.number1 = aNumber1;
    }

    /**
     * @return salgsdato
     */
    public final Integer getSaledate() {
        return saledate;
    }

    /**
     * @param asSaledate
     */
    public final void setSaledate(final Integer asSaledate) {
        this.saledate = asSaledate;
    }

    /**
     * @return id
     */
    public final Integer getSaleId() {
        return saleId;
    }

    /**
     * @param aSaleId
     */
    public final void setSaleId(final Integer aSaleId) {
        this.saleId = aSaleId;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof Sale)) {
            return false;
        }
        Sale castOther = (Sale) other;
        return new EqualsBuilder().append(saleId, castOther.saleId).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(saleId).toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("saleId", saleId).toString();
    }

    public final Integer getUserdefId() {
        return userdefId;
    }

    public final void setUserdefId(final Integer aUserdefId) {
        this.userdefId = aUserdefId;
    }

    public final Integer getGroupIdx() {
        return groupIdx;
    }

    public final void setGroupIdx(final Integer aGroupIdx) {
        this.groupIdx = aGroupIdx;
    }

    public final Integer getRegistered() {
        return registered;
    }

    public final void setRegistered(final Integer isRegistered) {
        this.registered = isRegistered;
    }

    public final Integer getProbability() {
        return probability;
    }

    public final void setProbability(final Integer aProbability) {
        this.probability = aProbability;
    }

    public final Integer getProjectId() {
        return projectId;
    }

    public final void setProjectId(final Integer aProjectId) {
        this.projectId = aProjectId;
    }
}
