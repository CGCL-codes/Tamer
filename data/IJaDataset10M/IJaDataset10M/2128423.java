package com.yict.csms.resourceplan.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.yict.csms.system.entity.DataDict;
import com.yict.csms.system.entity.User;

/**
 * 雪箱订更记录行信息表Entity
 * @author ryan.wang
 *
 */
@Entity
@Table(name = "CSMS_BOX_CONTRACTOR_LINE")
public class BoxContractorLine {

    @Id
    @Column(name = "LINEID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csms_box_contractor_line_seq")
    @SequenceGenerator(name = "csms_box_contractor_line_seq", sequenceName = "SEQ_CSMS_BOX_CONTRACTOR_LINE", allocationSize = 1, initialValue = 1)
    private Long lineId;

    @Column(name = "BCID")
    private Long bcId;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "BOXNUM")
    private String boxNum;

    @Column(name = "IMPORTID")
    private String importId;

    @Column(name = "YARDPOSTION")
    private String yardPostion;

    @Column(name = "GETORDERNUM")
    private String getOrderNum;

    @Column(name = "RESGETDATE")
    private Date resGetDate;

    @Column(name = "BOXSHAPE")
    private String boxShape;

    @Column(name = "CWI")
    private String cwi;

    @Column(name = "PTI")
    private Integer pti;

    @Column(name = "PCC")
    private Integer pcc;

    @Column(name = "PCS")
    private Float pcs;

    @Column(name = "PCT")
    private Float pct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TCORTX")
    private DataDict tcOrtx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CCORCX")
    private DataDict ccOrcx;

    @Column(name = "PTITURNON")
    private Date ptiTurnOn;

    @Column(name = "PTICUTOFF")
    private Date ptiCutOff;

    @Column(name = "PCTURNON")
    private Date pcTurnOn;

    @Column(name = "PCCUTOFF")
    private Date pcCutOff;

    @Column(name = "ISDELETE")
    private String isDelete;

    @Column(name = "SENDREMARKS")
    private String sendRemarks;

    @Column(name = "CREATEDATE")
    private Date createDate;

    @Column(name = "UPDATEDATE")
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATEPERSON")
    private User createPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATEPERSON")
    private User updatePerson;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getBcId() {
        return bcId;
    }

    public void setBcId(Long bcId) {
        this.bcId = bcId;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public String getYardPostion() {
        return yardPostion;
    }

    public void setYardPostion(String yardPostion) {
        this.yardPostion = yardPostion;
    }

    public String getGetOrderNum() {
        return getOrderNum;
    }

    public void setGetOrderNum(String getOrderNum) {
        this.getOrderNum = getOrderNum;
    }

    public Date getResGetDate() {
        return resGetDate;
    }

    public void setResGetDate(Date resGetDate) {
        this.resGetDate = resGetDate;
    }

    public String getBoxShape() {
        return boxShape;
    }

    public void setBoxShape(String boxShape) {
        this.boxShape = boxShape;
    }

    public String getCwi() {
        return cwi;
    }

    public void setCwi(String cwi) {
        this.cwi = cwi;
    }

    public Integer getPti() {
        return pti;
    }

    public void setPti(Integer pti) {
        this.pti = pti;
    }

    public Integer getPcc() {
        return pcc;
    }

    public void setPcc(Integer pcc) {
        this.pcc = pcc;
    }

    public Float getPcs() {
        return pcs;
    }

    public void setPcs(Float pcs) {
        this.pcs = pcs;
    }

    public Float getPct() {
        return pct;
    }

    public void setPct(Float pct) {
        this.pct = pct;
    }

    public DataDict getTcOrtx() {
        return tcOrtx;
    }

    public void setTcOrtx(DataDict tcOrtx) {
        this.tcOrtx = tcOrtx;
    }

    public DataDict getCcOrcx() {
        return ccOrcx;
    }

    public void setCcOrcx(DataDict ccOrcx) {
        this.ccOrcx = ccOrcx;
    }

    public Date getPtiTurnOn() {
        return ptiTurnOn;
    }

    public void setPtiTurnOn(Date ptiTurnOn) {
        this.ptiTurnOn = ptiTurnOn;
    }

    public Date getPtiCutOff() {
        return ptiCutOff;
    }

    public void setPtiCutOff(Date ptiCutOff) {
        this.ptiCutOff = ptiCutOff;
    }

    public Date getPcTurnOn() {
        return pcTurnOn;
    }

    public void setPcTurnOn(Date pcTurnOn) {
        this.pcTurnOn = pcTurnOn;
    }

    public Date getPcCutOff() {
        return pcCutOff;
    }

    public void setPcCutOff(Date pcCutOff) {
        this.pcCutOff = pcCutOff;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getSendRemarks() {
        return sendRemarks;
    }

    public void setSendRemarks(String sendRemarks) {
        this.sendRemarks = sendRemarks;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public User getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(User createPerson) {
        this.createPerson = createPerson;
    }

    public User getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(User updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
    }
}
