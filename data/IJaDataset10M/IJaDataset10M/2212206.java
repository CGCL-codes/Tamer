package org.t2framework.ioc.lucy.perf.prototype_measure2;

import org.t2framework.commons.annotation.composite.PrototypeScope;

@PrototypeScope
public class Department {

    long deptId;

    String deptName;

    public Department() {
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
