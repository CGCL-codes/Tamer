package org.criticalfailure.anp.core.domain.entity;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.criticalfailure.anp.core.domain.DomainConstants;
import org.criticalfailure.anp.core.domain.ModelObjectPersistenceException;
import org.eclipse.swt.graphics.Image;

/**
 * @author cipher@users.sourceforge.net
 * 
 */
public class Student extends Contact {

    private String color = "";

    private Set<Assignment> assignments = new HashSet<Assignment>();

    private Set<ReportCard> reportCards = new HashSet<ReportCard>();

    private Map<String, Assignment> assignmentsMonthMap = new HashMap<String, Assignment>();

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color) {
        String old = this.color;
        this.color = color;
        postPropertyChangeEvent("color", old, color);
    }

    /**
     * @return the assignments
     */
    public Set<Assignment> getAssignments() {
        return assignments;
    }

    /**
     * @param assignments
     *            the assignments to set
     */
    public void setAssignments(Set<Assignment> assignments) {
        Set<Assignment> old = this.assignments;
        this.assignments = assignments;
        for (Assignment a : this.assignments) {
            processAssignment(a);
        }
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, old.size());
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.assignments.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_REPLACE);
        postPropertyChangeEvent("assignments", old, assignments, props);
    }

    public void addAssignment(Assignment assignment) {
        int oldCount = this.assignments.size();
        this.assignments.add(assignment);
        processAssignment(assignment);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, oldCount);
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.assignments.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_ADD);
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ITEM_PROPERTY, assignment);
        postPropertyChangeEvent("assignments", assignments, assignments, props);
    }

    public void removeAssignment(Assignment assignment) {
        int oldCount = this.assignments.size();
        this.assignments.remove(assignment);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, oldCount);
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.assignments.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_REMOVE);
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ITEM_PROPERTY, assignment);
        postPropertyChangeEvent("assignments", assignments, assignments, props);
    }

    public Set<Assignment> getAssignmentsForMonth(Calendar cal) {
        Set<Assignment> assignments = new HashSet<Assignment>();
        Calendar sCal = GregorianCalendar.getInstance();
        Calendar eCal = GregorianCalendar.getInstance();
        for (Assignment a : assignments) {
        }
        return assignments;
    }

    public Set<Assignment> getAssignmentsForWeek(Calendar cal) {
        Set<Assignment> assignments = new HashSet<Assignment>();
        for (Assignment a : assignments) {
        }
        return assignments;
    }

    public Set<Assignment> getAssignmentsForDay(Calendar cal) {
        Set<Assignment> assignments = new HashSet<Assignment>();
        for (Assignment a : assignments) {
        }
        return assignments;
    }

    /**
     * @return the reportCards
     */
    public Set<ReportCard> getReportCards() {
        return reportCards;
    }

    /**
     * @param reportCards
     *            the reportCards to set
     */
    public void setReportCards(Set<ReportCard> reportCards) {
        Set<ReportCard> old = this.reportCards;
        this.reportCards = reportCards;
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, old.size());
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.reportCards.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_REPLACE);
        postPropertyChangeEvent("reportCards", old, reportCards, props);
    }

    public void addReportCard(ReportCard rc) {
        int oldCount = this.reportCards.size();
        this.reportCards.add(rc);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, oldCount);
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.reportCards.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_ADD);
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ITEM_PROPERTY, rc);
        postPropertyChangeEvent("reportCards", reportCards, reportCards, props);
    }

    public void removeReportCard(ReportCard rc) {
        int oldCount = this.reportCards.size();
        this.reportCards.remove(rc);
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(DomainConstants.MODEL_EVENT_OBJECT_OLD_COUNT_PROPERTY, oldCount);
        props.put(DomainConstants.MODEL_EVENT_OBJECT_NEW_COUNT_PROPERTY, this.reportCards.size());
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ACTION_PROPERTY, DomainConstants.MODEL_EVENT_COLLECTION_ACTION_REMOVE);
        props.put(DomainConstants.MODEL_EVENT_COLLECTION_ITEM_PROPERTY, rc);
        postPropertyChangeEvent("reportCards", reportCards, reportCards, props);
    }

    @Override
    public Image getIcon() {
        return DomainObjectImageMapper.getImage(Student.class);
    }

    @Override
    public void save() throws ModelObjectPersistenceException {
        super.save();
        for (Assignment a : getAssignments()) {
            a.save();
        }
        for (ReportCard rc : getReportCards()) {
            rc.save();
        }
    }

    private void processAssignment(Assignment a) {
        a.getStartDate();
    }

    @Override
    public String toString() {
        return "Student {id=" + id + "; first-name=" + getFirstName() + "; middle-name=" + getMiddleName() + "; last-name=" + getLastName() + "; birth-date=" + getBirthDate() + "; notes=" + getNotes() + "}";
    }
}
