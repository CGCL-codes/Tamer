package com.yict.csms.adminManage.entity;

import java.math.BigDecimal;
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
import com.yict.csms.baseInfo.entity.ContractType;
import com.yict.csms.company.entity.Company;
import com.yict.csms.system.entity.Profession;
import com.yict.csms.system.entity.User;

/**
 * 承包商排名表Entity
 * @author ryan.wang
 *
 */
@Entity
@Table(name = "CSMS_CONTRACTOR_RANKING")
public class ContractorRanking {

    @Id
    @Column(name = "RANKINGID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csms_contractor_ranking_seq")
    @SequenceGenerator(name = "csms_contractor_ranking_seq", sequenceName = "SEQ_CSMS_CONTRACTOR_RANKING", allocationSize = 1, initialValue = 1)
    private Long rankingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFEID")
    private Profession typeId;

    @Column(name = "RANKING")
    private Long ranking;

    @ManyToOne
    @JoinColumn(name = "COMPANYID")
    private Company companyId;

    @Column(name = "FIXEDPERCENT")
    private BigDecimal fixedPercent;

    @Column(name = "REWARDPERCENT")
    private BigDecimal rewardPercent;

    @Column(name = "STARTDATE")
    private Date startDate;

    @Column(name = "ENDDATE")
    private Date endDate;

    @Column(name = "REMARKS")
    private String remarks;

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

    public Long getRankingId() {
        return rankingId;
    }

    public void setRankingId(Long rankingId) {
        this.rankingId = rankingId;
    }

    public Profession getTypeId() {
        return typeId;
    }

    public void setTypeId(Profession typeId) {
        this.typeId = typeId;
    }

    public Long getRanking() {
        return ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public BigDecimal getFixedPercent() {
        return fixedPercent;
    }

    public void setFixedPercent(BigDecimal fixedPercent) {
        this.fixedPercent = fixedPercent;
    }

    public BigDecimal getRewardPercent() {
        return rewardPercent;
    }

    public void setRewardPercent(BigDecimal rewardPercent) {
        this.rewardPercent = rewardPercent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
}
