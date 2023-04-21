package com.pk.platform.business.charge.vo;

import java.util.Date;
import com.pk.platform.business.common.vo.CommonVO;

public class IncomeExpenseVO extends CommonVO {

    private String indexName;

    private String year;

    private String month;

    private String type;

    private Double amountStart;

    private Double amountEnd;

    private Date chargeTimeStart;

    private Date chargeTimeEnd;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmountStart() {
        return amountStart;
    }

    public void setAmountStart(Double amountStart) {
        this.amountStart = amountStart;
    }

    public Double getAmountEnd() {
        return amountEnd;
    }

    public void setAmountEnd(Double amountEnd) {
        this.amountEnd = amountEnd;
    }

    public Date getChargeTimeStart() {
        return chargeTimeStart;
    }

    public void setChargeTimeStart(Date chargeTimeStart) {
        this.chargeTimeStart = chargeTimeStart;
    }

    public Date getChargeTimeEnd() {
        return chargeTimeEnd;
    }

    public void setChargeTimeEnd(Date chargeTimeEnd) {
        this.chargeTimeEnd = chargeTimeEnd;
    }
}
