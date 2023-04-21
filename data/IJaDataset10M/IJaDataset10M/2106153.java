package examples.employee.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.seasar.dao.annotation.tiger.Bean;
import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;

@Bean(table = "EMP")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(value = IdType.SEQUENCE, sequenceName = "EMP_SEQ")
    protected Integer empno;

    protected String ename;

    protected String job;

    protected Integer mgr;

    protected Date hiredate;

    protected BigDecimal sal;

    protected BigDecimal comm;

    protected Integer deptno;

    protected int versionNo;

    protected String dname;

    protected int status;

    public Employee() {
    }

    public Integer getEmpno() {
        return this.empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return this.ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(java.lang.String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return this.mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return this.hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public BigDecimal getSal() {
        return this.sal;
    }

    public void setSal(BigDecimal sal) {
        this.sal = sal;
    }

    public BigDecimal getComm() {
        return this.comm;
    }

    public void setComm(BigDecimal comm) {
        this.comm = comm;
    }

    public Integer getDeptno() {
        return this.deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Employee)) {
            return false;
        }
        Employee castOther = (Employee) other;
        return getEmpno() == castOther.getEmpno();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("[");
        setupToString(buf);
        buf.append("]");
        return buf.toString();
    }

    protected void setupToString(StringBuffer buf) {
        buf.append(empno).append(", ");
        buf.append(ename).append(", ");
        buf.append(job).append(", ");
        buf.append(mgr).append(", ");
        buf.append(hiredate).append(", ");
        buf.append(sal).append(", ");
        buf.append(comm).append(", ");
        buf.append(deptno).append(", ");
        buf.append(versionNo);
    }

    public int hashCode() {
        if (empno != null) {
            return empno.intValue();
        }
        return 0;
    }
}
