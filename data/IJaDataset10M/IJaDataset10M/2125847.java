package org.posterita.beans;

import java.math.BigDecimal;

public class WebInvoiceLineBean extends WebOrderLineBean {

    private Integer invoiceId;

    private Integer qtyInvoiced;

    private BigDecimal lineTotalAmount;

    public BigDecimal getLineTotalAmt() {
        return lineTotalAmount;
    }

    public void setLineTotalAmt(BigDecimal lineTotalAmt) {
        this.lineTotalAmount = lineTotalAmt;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getQtyInvoiced() {
        return qtyInvoiced;
    }

    public void setQtyInvoiced(Integer qtyInvoiced) {
        this.qtyInvoiced = qtyInvoiced;
    }
}
