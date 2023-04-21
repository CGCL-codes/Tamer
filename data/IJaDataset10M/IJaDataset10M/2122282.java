package com.converter;

/**
 * TabLoginId entity. @author MyEclipse Persistence Tools
 */
public class TabLoginId implements java.io.Serializable {

    private String account;

    private String domain;

    /** default constructor */
    public TabLoginId() {
    }

    /** full constructor */
    public TabLoginId(String account, String domain) {
        this.account = account;
        this.domain = domain;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof TabLoginId)) return false;
        TabLoginId castOther = (TabLoginId) other;
        return ((this.getAccount() == castOther.getAccount()) || (this.getAccount() != null && castOther.getAccount() != null && this.getAccount().equals(castOther.getAccount()))) && ((this.getDomain() == castOther.getDomain()) || (this.getDomain() != null && castOther.getDomain() != null && this.getDomain().equals(castOther.getDomain())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getAccount() == null ? 0 : this.getAccount().hashCode());
        result = 37 * result + (getDomain() == null ? 0 : this.getDomain().hashCode());
        return result;
    }
}
