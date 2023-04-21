package pl.pollub.superkino.payments;

import java.util.Date;
import pl.pollub.superkino.tickets.Ticket;

public class Payment {

    private Integer idPayment;

    private Ticket ticket;

    private PaymentMethod paymentMethod;

    private Date date;

    public Payment() {
    }

    public Integer getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(Integer idPayment) {
        this.idPayment = idPayment;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
