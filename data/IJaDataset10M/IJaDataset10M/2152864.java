package com.wonebiz.crm.client.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "staticsData")
public class StaticsData implements java.io.Serializable {

    private static final long serialVersionUID = 7908828581815258593L;

    private Integer id;

    private Integer yearInt;

    private Integer monthInt;

    private Integer staticsId;

    private Integer staticsValue;

    private Date createTime;

    public StaticsData() {
    }

    public String toString() {
        return "id:" + this.staticsId + ", value:" + this.staticsValue + ", on:" + this.yearInt + "-" + this.monthInt;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "yearInt")
    public Integer getYearInt() {
        return this.yearInt;
    }

    public void setYearInt(Integer yearInt) {
        this.yearInt = yearInt;
    }

    @Column(name = "monthInt")
    public Integer getMonthInt() {
        return this.monthInt;
    }

    public void setMonthInt(Integer monthInt) {
        this.monthInt = monthInt;
    }

    @Column(name = "staticsId")
    public Integer getStaticsId() {
        return this.staticsId;
    }

    public void setStaticsId(Integer staticsId) {
        this.staticsId = staticsId;
    }

    @Column(name = "staticsValue")
    public Integer getStaticsValue() {
        return this.staticsValue;
    }

    public void setStaticsValue(Integer staticsValue) {
        this.staticsValue = staticsValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
