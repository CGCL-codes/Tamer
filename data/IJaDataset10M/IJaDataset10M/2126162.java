package com.pk.platform.business.charge.vo;

import com.pk.platform.business.common.vo.CommonVO;
import com.pk.platform.domain.charge.ChargeIndex;

public class ChargeIndexVO extends CommonVO {

    private ChargeIndex ci = new ChargeIndex();

    private Double amountStart;

    private Double amountEnd;

    public ChargeIndex getCi() {
        return ci;
    }

    public void setCi(ChargeIndex ci) {
        this.ci = ci;
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
}
