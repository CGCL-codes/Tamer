package net.techwatch.jpa.dao;

import java.util.List;
import net.techwatch.jpa.entity.Employee;
import net.techwatch.jpa.entity.Job;

public interface IEmployeeDao extends Dao<Long, Employee> {

    List<Job> findAll();
}
