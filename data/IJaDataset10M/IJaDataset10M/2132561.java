package br.com.visualmidia.business;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * @author   Lucas
 */
public class Parcel implements Serializable {

    private static final long serialVersionUID = 3544956545491481145L;

    private String date;

    private float valueBeforeParcelDateExpiration;

    private float valueAfterParcelDateExpiration;

    private float ticket;

    private float mora;

    private float payDiscount;

    private float payValue;

    private Calendar payDate;

    private boolean pay;

    @Deprecated
    private List<TypeOfPayment> paymentType = new LinkedList<TypeOfPayment>();

    private TypeOfPayment onePaymentType;

    private boolean printed;

    private Person person;

    private String idPerson;

    private boolean isRegistrationTax;

    private GDDate gdDatePayDate;

    private float _valueReceived;

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        if (person != null) {
            idPerson = person.getId();
            person = null;
        }
        if (paymentType != null && paymentType.size() >= 1) {
            onePaymentType = paymentType.get(0);
            paymentType = null;
        }
        if (payDate == null) return;
        gdDatePayDate = new GDDate(payDate);
        payDate = null;
    }

    public Parcel(String date, float valueWithoutDiscount, float value, float ticket, float mora, float payDiscount, float payValue) {
        this.date = date;
        this.valueAfterParcelDateExpiration = valueWithoutDiscount;
        this.valueBeforeParcelDateExpiration = value;
        this.ticket = ticket;
        this.mora = mora;
        this.payDiscount = payDiscount;
        this.payValue = payValue;
    }

    public Parcel(String date, float valueWithoutDiscount, float value, float ticket, float mora, float payDiscount, float payValue, boolean isRegistrationTax) {
        this.date = date;
        this.valueAfterParcelDateExpiration = valueWithoutDiscount;
        this.valueBeforeParcelDateExpiration = value;
        this.ticket = ticket;
        this.mora = mora;
        this.payDiscount = payDiscount;
        this.payValue = payValue;
        this.isRegistrationTax = isRegistrationTax;
    }

    public void setPaymenType(TypeOfPayment type) {
        onePaymentType = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(GDDate date) {
        this.date = date.getFormatedDate();
    }

    public GDDate getPayDate() {
        return gdDatePayDate;
    }

    public void setPayDate(GDDate payDate) {
        this.gdDatePayDate = payDate;
    }

    public float getDiscount() {
        return payDiscount;
    }

    public void setDiscount(float discount) {
        this.payDiscount = discount;
    }

    public float getMora() {
        return mora;
    }

    public void setMora(float mora) {
        this.mora = mora;
    }

    public float getPayValue() {
        return payValue;
    }

    public void setPayValue(float payValue) {
        this.payValue = payValue;
    }

    public float getTicket() {
        return ticket;
    }

    public void setTicket(float ticket) {
        this.ticket = ticket;
    }

    public float getValueBeforeParcelDateExpiration() {
        return valueBeforeParcelDateExpiration;
    }

    public void setValueBeforeParcelDateExpiration(float value) {
        this.valueBeforeParcelDateExpiration = value;
    }

    public boolean isPayed() {
        return pay;
    }

    public void setPayed(boolean status) {
        this.pay = status;
    }

    public float getValueAfterParcelDateExpiration() {
        return valueAfterParcelDateExpiration;
    }

    public void setValueAfterParcelDateExpiration(float valueWithoutDiscount) {
        this.valueAfterParcelDateExpiration = valueWithoutDiscount;
    }

    public String getPaymentType() {
        return (onePaymentType != null) ? onePaymentType.getType() : "";
    }

    public TypeOfPayment getPayment() {
        return onePaymentType;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPersonReceivedId(Person person) {
        this.idPerson = person.getId();
    }

    public void setPersonReceivedId(String idPerson) {
        this.idPerson = idPerson;
    }

    public String getIdPersonReceived() {
        return idPerson;
    }

    public void setValueReceived(float received) {
        _valueReceived = received;
    }

    public Money getValueReceived() {
        return new Money(_valueReceived);
    }

    public boolean isRegistrationTax() {
        return isRegistrationTax;
    }

    public void setRegistrationTax(boolean isRegistrationTax) {
        this.isRegistrationTax = isRegistrationTax;
    }
}
