package net.mjrz.fm.entity.beans;

import java.util.Date;
import java.math.BigDecimal;

public class CurrencyMonitor {

    long id;

    long ownerid;

    String country;

    String code;

    String symbol;

    Date lastUpdateTs;

    BigDecimal lastUpdateValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Date lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public BigDecimal getLastUpdateValue() {
        return lastUpdateValue;
    }

    public void setLastUpdateValue(BigDecimal lastUpdateValue) {
        this.lastUpdateValue = lastUpdateValue;
    }
}
