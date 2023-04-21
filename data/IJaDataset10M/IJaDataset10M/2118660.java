package com.student.vo;

import java.sql.Timestamp;
import java.util.List;
import org.directwebremoting.annotations.DataTransferObject;
import com.student.model.RpList;
import com.student.util.Model2VoUtil;

@DataTransferObject
public class DormRpAssociationVo {

    private Long rpId;

    private String rpName;

    private Long dormId;

    private String dormRpTime;

    public Long getRpId() {
        return rpId;
    }

    public void setRpId(Long rpId) {
        this.rpId = rpId;
    }

    public String getRpName() {
        return rpName;
    }

    public void setRpName(String rpName) {
        this.rpName = rpName;
    }

    public Long getDormId() {
        return dormId;
    }

    public void setDormId(Long dormId) {
        this.dormId = dormId;
    }

    public String getDormRpTime() {
        return dormRpTime;
    }

    public void setDormRpTime(String dormRpTime) {
        this.dormRpTime = dormRpTime;
    }
}
