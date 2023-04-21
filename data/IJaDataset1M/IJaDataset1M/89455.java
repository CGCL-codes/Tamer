package com.brekeke.hiway.iccard.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费员领退Entity
 * @author LEPING.LI
 * @version 1.0.0
 */
public class TollerGetUseICcard extends BasePojo {

    private static final long serialVersionUID = -1369072338617569441L;

    private String id;

    private String tscode;

    private String optype;

    private Date tdate;

    private String turn;

    private String toller;

    private BigDecimal ebacknormalcard;

    private BigDecimal ebackbadcard;

    private BigDecimal xbacknormalcard;

    private BigDecimal xbackbadcard;

    private BigDecimal losecard;

    private BigDecimal driverlosecard;

    private BigDecimal otherlosecard;

    private String accountor;

    private String remark;

    private Date lastmodify;

    private BigDecimal flag01;

    private BigDecimal flag02;

    private BigDecimal flag03;

    private String flag04;

    private String flag05;

    private String flag06;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTscode() {
        return tscode;
    }

    public void setTscode(String tscode) {
        this.tscode = tscode;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public Date getTdate() {
        return tdate;
    }

    public void setTdate(Date tdate) {
        this.tdate = tdate;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getToller() {
        return toller;
    }

    public void setToller(String toller) {
        this.toller = toller;
    }

    public BigDecimal getEbacknormalcard() {
        return ebacknormalcard;
    }

    public void setEbacknormalcard(BigDecimal ebacknormalcard) {
        this.ebacknormalcard = ebacknormalcard;
    }

    public BigDecimal getEbackbadcard() {
        return ebackbadcard;
    }

    public void setEbackbadcard(BigDecimal ebackbadcard) {
        this.ebackbadcard = ebackbadcard;
    }

    public BigDecimal getXbacknormalcard() {
        return xbacknormalcard;
    }

    public void setXbacknormalcard(BigDecimal xbacknormalcard) {
        this.xbacknormalcard = xbacknormalcard;
    }

    public BigDecimal getXbackbadcard() {
        return xbackbadcard;
    }

    public void setXbackbadcard(BigDecimal xbackbadcard) {
        this.xbackbadcard = xbackbadcard;
    }

    public BigDecimal getLosecard() {
        return losecard;
    }

    public void setLosecard(BigDecimal losecard) {
        this.losecard = losecard;
    }

    public BigDecimal getDriverlosecard() {
        return driverlosecard;
    }

    public void setDriverlosecard(BigDecimal driverlosecard) {
        this.driverlosecard = driverlosecard;
    }

    public BigDecimal getOtherlosecard() {
        return otherlosecard;
    }

    public void setOtherlosecard(BigDecimal otherlosecard) {
        this.otherlosecard = otherlosecard;
    }

    public String getAccountor() {
        return accountor;
    }

    public void setAccountor(String accountor) {
        this.accountor = accountor;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(Date lastmodify) {
        this.lastmodify = lastmodify;
    }

    public BigDecimal getFlag01() {
        return flag01;
    }

    public void setFlag01(BigDecimal flag01) {
        this.flag01 = flag01;
    }

    public BigDecimal getFlag02() {
        return flag02;
    }

    public void setFlag02(BigDecimal flag02) {
        this.flag02 = flag02;
    }

    public BigDecimal getFlag03() {
        return flag03;
    }

    public void setFlag03(BigDecimal flag03) {
        this.flag03 = flag03;
    }

    public String getFlag04() {
        return flag04;
    }

    public void setFlag04(String flag04) {
        this.flag04 = flag04;
    }

    public String getFlag05() {
        return flag05;
    }

    public void setFlag05(String flag05) {
        this.flag05 = flag05;
    }

    public String getFlag06() {
        return flag06;
    }

    public void setFlag06(String flag06) {
        this.flag06 = flag06;
    }
}
