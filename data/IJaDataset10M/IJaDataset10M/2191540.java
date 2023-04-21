package org.ouobpo.study.bpstudy200803.domainmodel.domain;

import java.util.List;
import org.ouobpo.study.bpstudy200803.domainmodel.dao.EmployeeDao;
import org.seasar.domainmodel.annotation.LifeCycleObject;

/**
 * 従業員リポジトリ
 * 
 * ドメイン層からデータアクセスの実装（S2Dao）を隠蔽する。
 */
@LifeCycleObject
public class EmployeeRepository {

    private EmployeeDao fEmployeeDao;

    public int insert(Employee employee) {
        return fEmployeeDao.insert(employee);
    }

    public int update(Employee employee) {
        return fEmployeeDao.update(employee);
    }

    public int delete(Employee employee) {
        return fEmployeeDao.delete(employee);
    }

    public List<Employee> selectAll() {
        return fEmployeeDao.selectAll();
    }

    public Employee selectById(Integer employeeId) {
        return fEmployeeDao.selectById(employeeId);
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        fEmployeeDao = employeeDao;
    }
}
