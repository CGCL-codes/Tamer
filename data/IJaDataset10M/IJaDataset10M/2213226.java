package assets.plan.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import assets.company.model.Company;
import com.systop.core.model.BaseModel;

@Entity
@Table(name = "inv_plans")
public class InvPlan extends BaseModel {

    private Integer id;

    /**
   * 计划单位
   */
    private Company company;

    /**
   * 用途
   */
    private String usedFor;

    /**
   * 计划所属年度
   */
    private String planYear;

    /**
   * 计划开始时间
   */
    private Date startDate;

    /**
   * 计划结束时间
   */
    private Date endDate;

    /**
   * 计划投资额
   */
    private Double planInv;

    /**
   * 实际投资额
   */
    private Double realInv;

    /**
   * 剩余说明
   */
    private String surplusRemark;

    /**
   * 计划名称
   */
    private String name;

    /**
   * 剩余投资用途
   */
    private String surplusUsedFor;

    @Id
    @GeneratedValue(generator = "hibseq")
    @GenericGenerator(name = "hibseq", strategy = "hilo")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Column(name = "used_for", columnDefinition = "varchar(500)")
    public String getUsedFor() {
        return usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }

    @Column(name = "plan_year")
    public String getPlanYear() {
        return planYear;
    }

    public void setPlanYear(String planYear) {
        this.planYear = planYear;
    }

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "plan_inv")
    public Double getPlanInv() {
        return planInv;
    }

    public void setPlanInv(Double planInv) {
        this.planInv = planInv;
    }

    @Column(name = "real_inv")
    public Double getRealInv() {
        return realInv;
    }

    public void setRealInv(Double realInv) {
        this.realInv = realInv;
    }

    @Column(name = "surplus_remark", columnDefinition = "varchar(500)")
    public String getSurplusRemark() {
        return surplusRemark;
    }

    public void setSurplusRemark(String surplusRemark) {
        this.surplusRemark = surplusRemark;
    }

    @Column(name = "surplus_used_for", columnDefinition = "varchar(500)")
    public String getSurplusUsedFor() {
        return surplusUsedFor;
    }

    public void setSurplusUsedFor(String surplusUsedFor) {
        this.surplusUsedFor = surplusUsedFor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
