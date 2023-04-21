package com.sks.bean.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sks.bean.privilege.Employee;

/**
 * EmployeeLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "employee_log", uniqueConstraints = {  })
public class EmployeeLog implements java.io.Serializable {

    private Integer logId;

    private Employee employee;

    private String operation;

    private Integer table;

    private Date operTime;

    private String loginIp;

    /** default constructor */
    public EmployeeLog() {
    }

    /** minimal constructor */
    public EmployeeLog(Integer logId, String operation, Integer table, Date operTime, String loginIp) {
        this.logId = logId;
        this.operation = operation;
        this.table = table;
        this.operTime = operTime;
        this.loginIp = loginIp;
    }

    /** full constructor */
    public EmployeeLog(Integer logId, Employee employee, String operation, Integer table, Date operTime, String loginIp) {
        this.logId = logId;
        this.employee = employee;
        this.operation = operation;
        this.table = table;
        this.operTime = operTime;
        this.loginIp = loginIp;
    }

    @Id
    @Column(name = "logId", unique = true, nullable = false, insertable = true, updatable = true)
    public Integer getLogId() {
        return this.logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee", unique = false, nullable = true, insertable = true, updatable = true)
    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Column(name = "operation", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Column(name = "table", unique = false, nullable = false, insertable = true, updatable = true)
    public Integer getTable() {
        return this.table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "operTime", unique = false, nullable = false, insertable = true, updatable = true, length = 19)
    public Date getOperTime() {
        return this.operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    @Column(name = "loginIp", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}
