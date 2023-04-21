package com.lalfa.framework.entity.mversion;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.lalfa.framework.entity.IdEntity;

@Entity
@Table(name = "testresult")
public class Testresult extends IdEntity {

    private Mversion mversion;

    private Boolean result;

    private String remark;

    /** default constructor */
    public Testresult() {
    }

    /** minimal constructor */
    public Testresult(Mversion mversion, Boolean result) {
        this.mversion = mversion;
        this.result = result;
    }

    /** full constructor */
    public Testresult(Mversion mversion, Boolean result, String remark) {
        this.mversion = mversion;
        this.result = result;
        this.remark = remark;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mversion_id")
    public Mversion getMversion() {
        return this.mversion;
    }

    public void setMversion(Mversion mversion) {
        this.mversion = mversion;
    }

    @Column(name = "result", nullable = false)
    public Boolean getResult() {
        return this.result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    @Column(name = "remark", length = 9999)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
