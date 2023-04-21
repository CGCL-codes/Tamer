package org.telscenter.sail.webapp.domain.impl;

import java.io.Serializable;
import org.telscenter.sail.webapp.domain.Run;
import net.sf.sail.webapp.domain.User;

/**
 * @author patrick lawler
 *
 */
public class ChangePeriodParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    private User student;

    private Run run;

    private String projectcode;

    private String projectcodeTo;

    /**
	 * @return the student
	 */
    public User getStudent() {
        return student;
    }

    /**
	 * @param student the student to set
	 */
    public void setStudent(User student) {
        this.student = student;
    }

    /**
	 * @return the run
	 */
    public Run getRun() {
        return run;
    }

    /**
	 * @param run the run to set
	 */
    public void setRun(Run run) {
        this.run = run;
    }

    /**
	 * @return the projectcode
	 */
    public String getProjectcode() {
        return projectcode;
    }

    /**
	 * @param projectcode the projectcode to set
	 */
    public void setProjectcode(String projectcode) {
        this.projectcode = projectcode;
    }

    /**
	 * @return the projectcodeTo
	 */
    public String getProjectcodeTo() {
        return projectcodeTo;
    }

    /**
	 * @param projectcodeTo the projectcodeTo to set
	 */
    public void setProjectcodeTo(String projectcodeTo) {
        this.projectcodeTo = projectcodeTo;
    }
}
