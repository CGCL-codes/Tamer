package ${package}.dom.employee;

import java.util.List;

import org.nakedobjects.applib.annotation.Named;


@Named("Employees")
public interface EmployeeRepository {

    public List<Employee> allEmployees();

    public List<Employee> findEmployees(
    		@Named("Name") 
    		String name);
}
