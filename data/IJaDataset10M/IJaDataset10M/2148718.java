package org.blueoxygen.papaje.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

@Entity
@Table(name = "jm_job_employee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class JobEmployee extends DefaultPersistence {

    private Employee employee;

    private Jobs job;

    /**
	 * @return the employee
	 */
    @ManyToOne
    public Employee getEmployee() {
        return employee;
    }

    /**
	 * @param employee the employee to set
	 */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
	 * @return the job
	 */
    @ManyToOne
    public Jobs getJob() {
        return job;
    }

    /**
	 * @param job the job to set
	 */
    public void setJob(Jobs job) {
        this.job = job;
    }
}
