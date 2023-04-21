package xpusp.model;

import java.util.*;

/** 
 * A Student
 * @sf $Header: /cvsroot/xpusp/xpusp/sources/xpusp/model/Student.java,v 1.7 2001/12/10 09:33:17 krico Exp $
 */
public class Student {

    /** Student id
   */
    protected int studentNr;

    /** probable graduate?
 */
    protected boolean probableGraduate;

    /** number of the course this student attends
 */
    protected int course;

    /**
     * Data for this student
     */
    protected Person data;

    /**
   * his preferences
   */
    protected StudentPreference preferences;

    public Student() {
    }

    public Student(Person p, int i, boolean b) {
        data = p;
        studentNr = i;
        probableGraduate = b;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o instanceof Student) {
            Student other = (Student) o;
            if (getStudentNr() == other.getStudentNr() && getCourse() == other.getCourse() && isProbableGraduate() == other.isProbableGraduate() && Utility.equals(getData(), other.getData()) && Utility.equals(getPreferences(), other.getPreferences())) return true;
        }
        return false;
    }

    public int getStudentNr() {
        return studentNr;
    }

    public boolean isProbableGraduate() {
        return probableGraduate;
    }

    public boolean getProbableGraduate() {
        return probableGraduate;
    }

    public int getCourse() {
        return course;
    }

    public Person getData() {
        return data;
    }

    public StudentPreference getPreferences() {
        return preferences;
    }

    public void setStudentNr(int i) {
        studentNr = i;
    }

    public void setProbableGraduate(boolean b) {
        probableGraduate = b;
    }

    public void setCourse(int i) {
        course = i;
    }

    public void setData(Person p) {
        data = p;
    }

    public void setPreferences(StudentPreference sp) {
        preferences = sp;
    }
}
