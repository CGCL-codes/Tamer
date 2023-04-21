package aml.ramava.data.entities;

import java.util.Date;
import java.util.Vector;
import aml.ramava.common.Utilities;
import aml.ramava.data.CodificatorEntityControler;

public class BillRevised {

    private String id;

    private ExibitionParticipant participant;

    private Date creationDate;

    private Date paymentDate;

    private Date dueDate;

    private Currency currency;

    private Vector<BillProduct> products;

    public BillRevised() {
        this(null, null, null, null, null, null, null);
    }

    public BillRevised(String id, ExibitionParticipant participant, Date creationDate, Date paymentDate, Date dueDate, Currency currency, Vector<BillProduct> products) {
        super();
        this.id = id;
        this.participant = participant;
        this.creationDate = creationDate;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
        this.currency = currency;
        this.products = products;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ExibitionParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(ExibitionParticipant participant) {
        this.participant = participant;
    }

    public Vector<BillProduct> getProducts() {
        return products;
    }

    public void setProducts(Vector<BillProduct> products) {
        this.products = products;
    }

    public double getBillTotalWithoutVAT() {
        double result = 0.0;
        for (int i = 0; i < products.size(); i++) {
            BillProduct product = products.get(i);
            result += product.getAmount() * product.getPrice();
        }
        return result;
    }

    public double getProductTotalDiscount() {
        double result = 0.0;
        for (int i = 0; i < products.size(); i++) {
            BillProduct product = products.get(i);
            result += product.getTotalDiscount();
        }
        return result;
    }

    public double getTotalVAT(VATRate rate) {
        double result = 0.0;
        for (BillProduct product : products) {
            if (rate.getRate() == product.getVATRate()) {
                result += product.getDiscountedTotal() * product.getVATRate();
            }
        }
        return Utilities.round(result, 2);
    }

    public double getProductWithVATWithDiscounts() {
        double result = getBillTotalWithoutVAT();
        result -= getProductTotalDiscount();
        for (VATRate rate : CodificatorEntityControler.getVatRates()) {
            result += getTotalVAT(rate);
        }
        return result;
    }

    public String toString() {
        return id != null ? id : "bill<null id>";
    }
}
