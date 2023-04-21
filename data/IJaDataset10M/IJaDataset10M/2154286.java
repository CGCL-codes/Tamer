package edu.ba.library.management.user;

import java.util.Date;

public class CustomerCard {

    protected int cardNumber;

    protected Customer customer;

    protected Date validFrom;

    protected Date validUntil;

    protected CustomerCardStatus status = CustomerCardStatus.Normal;

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public CustomerCardStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerCardStatus status) {
        this.status = status;
    }
}
