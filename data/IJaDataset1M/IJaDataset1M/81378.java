package org.forzaframework.test.core.persistance.model;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 16:06:06
 */
public class Student {

    private long studentNumber;

    private String name;

    private Course preferredCourse;

    private Date birthDate;

    private Set<Enrolment> enrolments = new HashSet<Enrolment>();

    public Student() {
    }

    public Student(long studentNumber, String name, Date birthDate) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Course getPreferredCourse() {
        return preferredCourse;
    }

    public void setPreferredCourse(Course preferredCourse) {
        this.preferredCourse = preferredCourse;
    }

    public Set<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(Set<Enrolment> employments) {
        this.enrolments = employments;
    }

    public void addEnrolement(Enrolment enrolment) {
        this.enrolments.add(enrolment);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
