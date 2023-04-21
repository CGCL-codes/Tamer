package com.brekeke.hiway.ticket.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车道库存
 * @author LEPING LI
 * @version 1.0.0
 */
public class CarlaneStore extends BasePojo {

    private static final long serialVersionUID = 4949258883505761409L;

    private String id;

    private String orgacode;

    private String organame;

    private BigDecimal carlane;

    private BigDecimal tickettype;

    private String ticketname;

    private Date storedate;

    private BigDecimal livebegin;

    private BigDecimal liveend;

    private BigDecimal livenumber;

    private String remark;

    private Date updatedate;

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

    public String getOrgacode() {
        return orgacode;
    }

    public void setOrgacode(String orgacode) {
        this.orgacode = orgacode;
    }

    public String getOrganame() {
        return organame;
    }

    public void setOrganame(String organame) {
        this.organame = organame;
    }

    public BigDecimal getCarlane() {
        return carlane;
    }

    public void setCarlane(BigDecimal carlane) {
        this.carlane = carlane;
    }

    public BigDecimal getTickettype() {
        return tickettype;
    }

    public void setTickettype(BigDecimal tickettype) {
        this.tickettype = tickettype;
    }

    public String getTicketname() {
        return ticketname;
    }

    public void setTicketname(String ticketname) {
        this.ticketname = ticketname;
    }

    public Date getStoredate() {
        return storedate;
    }

    public void setStoredate(Date storedate) {
        this.storedate = storedate;
    }

    public BigDecimal getLivebegin() {
        return livebegin;
    }

    public void setLivebegin(BigDecimal livebegin) {
        this.livebegin = livebegin;
    }

    public BigDecimal getLiveend() {
        return liveend;
    }

    public void setLiveend(BigDecimal liveend) {
        this.liveend = liveend;
    }

    public BigDecimal getLivenumber() {
        return livenumber;
    }

    public void setLivenumber(BigDecimal livenumber) {
        this.livenumber = livenumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
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
