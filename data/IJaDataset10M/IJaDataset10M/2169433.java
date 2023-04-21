package net.sourceforge.mygp.model;

import java.io.IOException;
import java.util.ArrayList;
import net.sourceforge.mygp.Assessment;
import net.sourceforge.mygp.Grade;
import net.sourceforge.mygp.Student;

/**
 * An API for generic data storage.
 * 
 * @author matt
 */
public interface DataStorage {

    /**
	 * Writes a particular grade.
	 * 
	 * @param g the grade to write
	 * @throws Exception if anything happens
	 */
    public void writeGrade(Grade g) throws Exception;

    /**
	 * Gets the grade for the given student and assessment.
	 * 
	 * @param s the student
	 * @param a the assessment
	 * @return the grade
	 * @throws Exception if anything happens
	 */
    public Grade getGrade(Student s, Assessment a) throws Exception;

    /**
	 * Gets all grades available.
	 * 
	 * @return a list of all grades
	 * @throws Exception just in case
	 */
    public ArrayList<Grade> getAllGrades() throws Exception;

    /**
	 * Removes the given student.
	 * 
	 * @param s the student
	 * @throws Exception if it fails
	 */
    public void remove(Student s) throws Exception;

    /**
	 * @param a the assessment
	 * @throws Exception if it fails
	 */
    public void remove(Assessment a) throws Exception;

    /**
	 * Removes all data from this storage.
	 * 
	 * @throws IOException if something fails
	 */
    public void wipe() throws IOException;

    /**
	 * Creates a backup of this storage.
	 * 
	 * @throws Exception just in case
	 */
    public void backup() throws Exception;

    /**
	 * Adds the given student manually (as opposed to adding a grade for the given student).
	 * 
	 * @param s the student
	 * @throws Exception if there's a problem
	 */
    public void addStudent(Student s) throws Exception;

    /**
	 * Adds the given assessment manually (as opposed to adding a grade for the given assessment).
	 * 
	 * @param a the assessment
	 * @throws Exception if there's a problem
	 */
    public void addAssessment(Assessment a) throws Exception;

    /**
	 * @return all assessments
	 * @throws Exception in case of FAIL
	 */
    public ArrayList<Assessment> getAllAssessments() throws Exception;

    /**
	 * @return all students
	 * @throws Exception in case of FAIL
	 */
    public ArrayList<Student> getAllStudents() throws Exception;
}
