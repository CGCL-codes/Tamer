package com.ademille.hvthelper.model;

import java.util.Collections;
import java.util.List;
import com.ademille.hvthelper.gui.util.SortedLinkedList;

public class TeachingAssignmentInfo {

    private boolean hometeaching = false;

    private List<Teacher> teachers;

    private List<Quorum> quorums;

    private List<Family> unassigned;

    public TeachingAssignmentInfo() {
        teachers = new SortedLinkedList<Teacher>();
        quorums = new SortedLinkedList<Quorum>();
        unassigned = new SortedLinkedList<Family>();
    }

    /**
	 * Create a method that helps in de-serialization. Xstream may de-serialize
	 * older file versions and create a LinkedList instead of a SortedLinkedList
	 * since it doesn't use the constructor. So, force it to be a SortedLinkedList
	 * here.
	  * 
	 * @return This object.
	 */
    private Object readResolve() {
        teachers = new SortedLinkedList<Teacher>(teachers);
        quorums = new SortedLinkedList<Quorum>(quorums);
        unassigned = new SortedLinkedList<Family>(unassigned);
        return this;
    }

    /**
	 * @return the hometeaching
	 */
    public boolean isHometeaching() {
        return hometeaching;
    }

    /**
	 * @param hometeaching
	 *          the hometeaching to set
	 */
    public void setHometeaching(boolean hometeaching) {
        this.hometeaching = hometeaching;
    }

    /**
	 * @return the quorums
	 */
    public List<Quorum> getQuorums() {
        return quorums;
    }

    /**
	 * @param quorums
	 *          the quorums to set
	 */
    public void setQuorums(List<Quorum> quorums) {
        this.quorums = new SortedLinkedList<Quorum>(quorums);
    }

    /**
	 * @return the teachers
	 */
    public List<Teacher> getTeachers() {
        return teachers;
    }

    /**
	 * @param teachers
	 *          the teachers to set
	 */
    public void setTeachers(List<Teacher> teachers) {
        this.teachers = new SortedLinkedList<Teacher>(teachers);
    }

    public void addTeacher(Teacher t) {
        if (null != teachers) {
            teachers.add(t);
            Collections.sort(teachers);
        }
    }

    public void removeTeacher(Teacher t) {
        if (null != teachers) {
            teachers.remove(t);
        }
    }

    /**
	 * @return the unassigned
	 */
    public List<Family> getUnassigned() {
        return unassigned;
    }

    /**
	 * @param unassigned
	 *          the unassigned to set
	 */
    public void setUnassigned(List<Family> unassigned) {
        this.unassigned = new SortedLinkedList<Family>(unassigned);
    }

    public void addUnassigned(Family f) {
        if (null != unassigned) {
            unassigned.add(f);
        }
    }

    public void removeUnassigned(Family f) {
        if (null != unassigned) {
            unassigned.remove(f);
        }
    }
}
