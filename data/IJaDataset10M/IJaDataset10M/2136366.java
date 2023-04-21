package dukestutoring.web;

import dukestutoring.entity.Student;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author ian
 */
@Named
@RequestScoped
public class StudentManager {

    protected Student newStudent;

    public StudentManager(Student newStudent) {
        this.newStudent = newStudent;
    }

    public StudentManager() {
        this.newStudent = new Student();
    }

    /**
     * Get the value of newStudent
     *
     * @return the value of newStudent
     */
    public Student getNewStudent() {
        return newStudent;
    }

    /**
     * Set the value of newStudent
     *
     * @param newStudent new value of newStudent
     */
    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }
}
