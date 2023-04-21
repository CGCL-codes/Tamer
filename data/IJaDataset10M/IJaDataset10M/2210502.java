package com.vlee.ejb.accounting;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;

public class RetainedEarningsObject extends java.lang.Object implements Serializable {

    public Integer pkId;

    public Integer pcCenterId;

    public Integer batchId;

    public Integer fiscalYearId;

    public BigDecimal retainedEarnings;

    public String status;

    public RetainedEarningsObject() {
    }
}
