package org.jpos.ee;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ResultCodeInfo implements Serializable {

    /** nullable persistent field */
    private String resultCode;

    /** nullable persistent field */
    private String resultInfo;

    /** nullable persistent field */
    private String extendedResultCode;

    /** full constructor */
    public ResultCodeInfo(String resultCode, String resultInfo, String extendedResultCode) {
        this.resultCode = resultCode;
        this.resultInfo = resultInfo;
        this.extendedResultCode = extendedResultCode;
    }

    /** default constructor */
    public ResultCodeInfo() {
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return this.resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public String getExtendedResultCode() {
        return this.extendedResultCode;
    }

    public void setExtendedResultCode(String extendedResultCode) {
        this.extendedResultCode = extendedResultCode;
    }

    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
