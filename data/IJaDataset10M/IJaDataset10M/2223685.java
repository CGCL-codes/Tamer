package com.pk.platform.business.charge.vo;

import com.pk.platform.business.common.vo.CommonVO;
import com.pk.platform.domain.charge.ChargeTemp;

public class ChargeTempVO extends CommonVO {

    private ChargeTemp ct = new ChargeTemp();

    private String classId;

    public ChargeTemp getCt() {
        return ct;
    }

    public void setCt(ChargeTemp ct) {
        this.ct = ct;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
