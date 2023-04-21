package com.medsol.sales.model;

import javax.persistence.*;
import com.medsol.common.model.AuditEntity;

/**
 * Created by IntelliJ IDEA.
 * User: Vinay Kaipra Puthenveettil
 * Date: May 24, 2008
 * Time: 2:56:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class SIVoucher extends AuditEntity {

    @Id
    private SIVoucherPK pk;

    private Long customerId;

    private Double amount;

    public SIVoucherPK getPk() {
        return pk;
    }

    public void setPk(SIVoucherPK pk) {
        this.pk = pk;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
