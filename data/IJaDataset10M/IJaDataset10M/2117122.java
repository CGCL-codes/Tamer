package pl.pyrkon.cm.client.cashier.common;

import java.io.Serializable;

public class PaymentStatistics implements Serializable {

    private static final long serialVersionUID = 5923571468090997514L;

    private Integer paymentCount;

    private Double paymentQuota;

    public Integer getPaymentCount() {
        return paymentCount;
    }

    public void setPaymentCount(Integer paymentCount) {
        this.paymentCount = paymentCount;
    }

    public Double getPaymentQuota() {
        return paymentQuota;
    }

    public void setPaymentQuota(Double paymentQuota) {
        this.paymentQuota = paymentQuota;
    }
}
