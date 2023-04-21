package beans;

import daos.DepartmentDaoMysql;
import daos.EmployeeDaoMysql;
import dtos.DepartmentDTO;
import dtos.EmployeeDTO;

public class DepartmentBean {

    private DepartmentDTO department = new DepartmentDTO();

    private DepartmentDaoMysql departmentDao = new DepartmentDaoMysql();

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartmentDao(DepartmentDaoMysql departmentDao) {
        this.departmentDao = departmentDao;
    }

    public DepartmentDaoMysql getDepartmentDao() {
        return departmentDao;
    }
}
