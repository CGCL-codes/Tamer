package net.sourceforge.gda.examples.data.personel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hasani
 * Date: Apr 19, 2007
 * Time: 2:55:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IManager extends IEmployee {

    /**
     *
     * Returns a list of employees.
     *
     * @return List<IEmployee>
     */
    public List<IEmployee> getEmployeeList();

    /**
     *
     * Removes an employee
     *
     * @param employee the employee property to set.
     */
    public void removeEmployee(IEmployee employee);

    /**
     *
     * Adds a employee under this manager.
     *
     * @param employee the employee property to set.
     */
    public void addEmployee(IEmployee employee);

    /**
     *
     * Adds a supervisor.
     *
     * @param supervisor the supervisor property to set.
     */
    public void addSupervisor(ISupervisor supervisor);

    /**
     *
     * Remove supervisor.
     *
     * @param supervisor the supervisor property to set.
     */
    public void removeSupervisor(ISupervisor supervisor);

    /**
     *
     * Returns all supervisors.
     *
     * @return List<ISupervisor>
     */
    public List<ISupervisor> getSupervisorList();
}
