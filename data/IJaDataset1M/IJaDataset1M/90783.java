package sts.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class UssaClass implements Serializable {

    /** identifier field */
    private String ussaCode;

    /** persistent field */
    private String ussaName;

    /** nullable persistent field */
    private String pyDpn;

    /** nullable persistent field */
    private String pyBn0;

    /** nullable persistent field */
    private String pyBn2;

    /** nullable persistent field */
    private String pyBn4;

    /** nullable persistent field */
    private String pyBn5;

    /** full constructor */
    public UssaClass(String ussaCode, String ussaName, String pyDpn, String pyBn0, String pyBn2, String pyBn4, String pyBn5) {
        this.ussaCode = ussaCode;
        this.ussaName = ussaName;
        this.pyDpn = pyDpn;
        this.pyBn0 = pyBn0;
        this.pyBn2 = pyBn2;
        this.pyBn4 = pyBn4;
        this.pyBn5 = pyBn5;
    }

    /** default constructor */
    public UssaClass() {
    }

    /** minimal constructor */
    public UssaClass(String ussaCode, String ussaName) {
        this.ussaCode = ussaCode;
        this.ussaName = ussaName;
    }

    public String getUssaCode() {
        return this.ussaCode;
    }

    public void setUssaCode(String ussaCode) {
        this.ussaCode = ussaCode;
    }

    public String getUssaName() {
        return this.ussaName;
    }

    public void setUssaName(String ussaName) {
        this.ussaName = ussaName;
    }

    public String getPyDpn() {
        return this.pyDpn;
    }

    public void setPyDpn(String pyDpn) {
        this.pyDpn = pyDpn;
    }

    public String getPyBn0() {
        return this.pyBn0;
    }

    public void setPyBn0(String pyBn0) {
        this.pyBn0 = pyBn0;
    }

    public String getPyBn2() {
        return this.pyBn2;
    }

    public void setPyBn2(String pyBn2) {
        this.pyBn2 = pyBn2;
    }

    public String getPyBn4() {
        return this.pyBn4;
    }

    public void setPyBn4(String pyBn4) {
        this.pyBn4 = pyBn4;
    }

    public String getPyBn5() {
        return this.pyBn5;
    }

    public void setPyBn5(String pyBn5) {
        this.pyBn5 = pyBn5;
    }

    public String toString() {
        return new ToStringBuilder(this).append("ussaCode", getUssaCode()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof UssaClass)) return false;
        UssaClass castOther = (UssaClass) other;
        return new EqualsBuilder().append(this.getUssaCode(), castOther.getUssaCode()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getUssaCode()).toHashCode();
    }
}
