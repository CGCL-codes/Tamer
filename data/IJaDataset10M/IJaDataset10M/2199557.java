package org.seasar.webhelpers.examples.web.validator2;

import java.util.Date;
import org.seasar.webhelpers.examples.dto.EmpDto;

public abstract class AbstractListPage {

    protected EmpDto[] empItems;

    protected int empIndex;

    protected Integer empNo;

    protected String empName;

    protected Date hiredate;

    public AbstractListPage() {
    }

    public EmpDto[] getEmpItems() {
        return empItems;
    }

    public void setEmpItems(EmpDto[] empItems) {
        this.empItems = empItems;
    }

    public int getEmpIndex() {
        return empIndex;
    }

    public void setEmpIndex(int empIndex) {
        this.empIndex = empIndex;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public String initialize() {
        this.empItems = new EmpDto[10];
        for (int i = 0; i < this.empItems.length; i++) {
            EmpDto emp = new EmpDto();
            emp.setId(new Integer(i));
            emp.setEmpNo(new Integer(i));
            emp.setEmpName("name" + i);
            emp.setHiredate(new Date());
            this.empItems[i] = emp;
        }
        return null;
    }

    public String prerender() {
        return null;
    }

    public String doExecute() {
        return null;
    }
}
