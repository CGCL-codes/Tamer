package edu.umn.cs5115.scheduler.entities;

import edu.umn.cs5115.scheduler.SchedulerDocument;
import edu.umn.cs5115.scheduler.framework.KeyValueCodingSet;
import edu.umn.cs5115.scheduler.framework.ManagedData;
import edu.umn.cs5115.scheduler.framework.gui.AlphaColor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Grant, Dan
 *                                      -----------------------------------
 *                                      |        Course                   |
 *                                      |            extends ManagedData  |
 *                                      |            implements CoursePart|
 *            WE ARE HERE --->          |                                 |
 *                                      | - Document = document           |
 *                                      | - getParent() = null            |
 *                                      -----------------------------------
 *                                                        |
 *                                                        |
 *                   --------------------------------------
 *                   |
 *                   |
 *   -----------------------------------
 *   |        Section 1                |
 *   |            extends ManagedData  |
 *   |            implements CoursePart|
 *   |                                 |
 *   | - Document = Course.document    |------------
 *   | - parentSection = null          |           |
 *   | - getParent() = Course          |           |
 *   -----------------------------------           |
 *                                                 |
 *                                                 |
 *                                ----------------------------------------
 *                                |        Sub Section 1                 |
 *                                |            extends ManagedData       |
 *                                |            implements CoursePart     |
 *                                |                                      |
 *                                | - Document = Section1.Course.document|
 *                                | - parentSection = Section1           |
 *                                | - getParent() = Section1             |
 *                                ----------------------------------------
 */
public class Course extends CoursePart {

    private KeyValueCodingSet sections;

    private String department;

    private String courseNumber;

    private String name;

    private String description;

    private String preReqs = "None";

    private String libEds = "None";

    private AlphaColor color;

    public static final String SECTIONS_KEY = "sections";

    public static final String DEPARTMENT_KEY = "department";

    public static final String COURSE_NUMBER_KEY = "courseNumber";

    public static final String NAME_KEY = "name";

    public static final String DESCRIPTION_KEY = "description";

    public static final String PRE_REQS_KEY = "preReqs";

    public static final String LIB_EDS_KEY = "libEds";

    public static final String RED_KEY = "red";

    public static final String GREEN_KEY = "green";

    public static final String BLUE_KEY = "blue";

    public static final String COLOR_KEY = "color";

    static {
        ArrayList colorKeys = new ArrayList(3);
        colorKeys.add(RED_KEY);
        colorKeys.add(GREEN_KEY);
        colorKeys.add(BLUE_KEY);
        setKeysTriggerNotificationForDependentKey(Course.class, colorKeys, COLOR_KEY);
        ArrayList sectionsKeyList = new ArrayList(1);
        sectionsKeyList.add(SECTIONS_KEY);
        setKeysTriggerNotificationForDependentKey(Course.class, sectionsKeyList, CHILDREN_KEY);
    }

    public Course(SchedulerDocument document) {
        super(document);
        sections = new KeyValueCodingSet();
        didChangeValueForKey(SECTIONS_KEY);
        document.getUndoManager().disableUndoRegistration();
        setColor(CourseColors.getUnusedColor(document.getSchedule()));
        document.getUndoManager().enableUndoRegistration();
    }

    /**
     * Returns the set of sections.
     * @return The list of top-level sections for this course.
     */
    public Set getSections() {
        return sections;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
        didChangeValueForKey(DEPARTMENT_KEY);
    }

    /**
     * Adds a section to this course.
     * 
     * This method should only be used by the Section class.  A Section's constructor includes the parent
     * Course, so adding it explicitly does not make sense.
     *  
     * @param section
     */
    protected void addSection(Section section) {
        sections.add(section);
    }

    /**
     * @return the courseNumber
     */
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * @return the department (a 4-letter code)
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Get a short string identifying the course.  This is the concatenation of
     * the department code and the course's number.
     */
    public String getShortName() {
        return department + " " + courseNumber;
    }

    /**
	 * @param courseNumber  the courseNumber to set
	 */
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
        didChangeValueForKey(COURSE_NUMBER_KEY);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        didChangeValueForKey(NAME_KEY);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
        didChangeValueForKey(DESCRIPTION_KEY);
    }

    /**
     * @return the prereqs
     */
    public String getPrereqs() {
        return preReqs;
    }

    /**
     * Set the prereqs
     */
    public void setPrereqs(String prereqs) {
        this.preReqs = prereqs;
        didChangeValueForKey(PRE_REQS_KEY);
    }

    public String getLibEds() {
        return libEds;
    }

    public void setLibEds(String libEd) {
        this.libEds = libEd;
        didChangeValueForKey(LIB_EDS_KEY);
    }

    /**
     * Get the display color for this course.  You should derive colors for
     * displaying this course using this color.
     * @return This course's display color.
     */
    public AlphaColor getColor() {
        if (color == null) color = new AlphaColor(getRed(), getGreen(), getBlue());
        return color;
    }

    /**
     * Set the display color for this course.
     * @param color The new display color.
     */
    public void setColor(AlphaColor color) {
        setRed(color.getRed());
        setGreen(color.getGreen());
        setBlue(color.getBlue());
    }

    /**
     * Clamps the value to the given range.
     * @param value The value to adjust
     * @param lowerBound The lower bound.
     * @param upperBound the upper bound.
     * @return If value is less than lowerBound, lowerBound is returned.  If 
     * value is between lowerBound and upperBound, value is returned.  If value
     * is greater than upperBound, upperBound is returned.
     */
    private int closestValueInRange(int value, int lowerBound, int upperBound) {
        if (value < lowerBound) return lowerBound;
        if (value > upperBound) return upperBound;
        return value;
    }

    /**
     * Set the value of the red channel for this course's display color.
     * @param red A value for the red channel, between 0 and 255.  Values 
     * outside this range will be set to the closest value in range.
     */
    public void setRed(int red) {
        red = closestValueInRange(red, 0, 255);
        color = null;
        setPrimitiveValueForKey(RED_KEY, red);
    }

    /**
     * Get the value of the red channel for this course's display color.
     * @return The value of the red channel, which is a value between 0 and 
     * 255.
     */
    public int getRed() {
        Number red = (Number) getPrimitiveValueForKey(RED_KEY);
        if (red != null) return red.intValue(); else return 0;
    }

    /**
     * Set the value of the green channel for this course's display color.
     * @param green A value for the green channel, between 0 and 255.  Values 
     * outside this range will be set to the closest value in range.
     */
    public void setGreen(int green) {
        green = closestValueInRange(green, 0, 255);
        color = null;
        setPrimitiveValueForKey(GREEN_KEY, green);
    }

    /**
     * Get the value of the green channel for this course's display color.
     * @return The value of the green channel, which is a value between 0 and 
     * 255.
     */
    public int getGreen() {
        Number green = (Number) getPrimitiveValueForKey(GREEN_KEY);
        if (green != null) return green.intValue(); else return 0;
    }

    /**
     * Set the value of the blue channel for this course's display color.
     * @param blue A value for the blue channel, between 0 and 255.  Values 
     * outside this range will be set to the closest value in range.
     */
    public void setBlue(int blue) {
        blue = closestValueInRange(blue, 0, 255);
        color = null;
        setPrimitiveValueForKey(BLUE_KEY, blue);
    }

    /**
     * Get the value of the blue channel for this course's display color.
     * @return The value of the blue channel, which is a value between 0 and 
     * 255.
     */
    public int getBlue() {
        Number blue = (Number) getPrimitiveValueForKey(BLUE_KEY);
        if (blue != null) return blue.intValue(); else return 0;
    }

    public CoursePart getParent() {
        return null;
    }

    public Course getCourse() {
        return this;
    }

    /**
     * Checks the descendants of this course to find if any of them are chosen.
     * @return True if some descendent is chosen, false if no descendants are
     * chosen.
     */
    public boolean isAnyDescendantChosen() {
        for (CoursePart child : getChildren()) {
            if (child.isAnyDescendantChosen()) return true;
        }
        return false;
    }

    public Collection<CoursePart> getSiblings() {
        Collection myself = new ArrayList(1);
        myself.add(this);
        return myself;
    }

    public Collection<CoursePart> getChildren() {
        return sections;
    }

    public boolean isAncestorOf(CoursePart descendant) {
        if (descendant == null) return false;
        return (descendant.getCourse() == this && descendant != this);
    }

    public boolean isSiblingOf(CoursePart sibling) {
        return false;
    }

    public boolean isChildOf(CoursePart parent) {
        return false;
    }

    public boolean equals(Object course) {
        if (!(course instanceof Course)) return false;
        Course compareCourse = (Course) course;
        if (!compareCourse.getDepartment().equals(this.getDepartment())) return false;
        if (!compareCourse.getCourseNumber().equals(this.getCourseNumber())) return false;
        return true;
    }

    public int hashCode() {
        return getDepartment().hashCode() ^ getCourseNumber().hashCode();
    }

    protected void recalculateStatus() {
        Iterator iterator = sections.iterator();
        Section chosenSection = null;
        Status newStatus = Status.NONE;
        String newStatusBrief = "";
        while (iterator.hasNext() && newStatus == Status.NONE) {
            Section section = (Section) iterator.next();
            newStatus = section.getStatus();
            switch(newStatus) {
                case CONFLICT_TIME:
                    newStatusBrief = section.getStatusBrief();
                    break;
                case CONFLICT_MULTIPLE:
                    newStatusBrief = section.getStatusBrief();
                    break;
                case CONFLICT_MISSING:
                    newStatusBrief = section.getStatusBrief();
                    break;
                case COMPLETE:
                    newStatusBrief = "Ready to register for this course";
                    break;
                case NONE:
                default:
                    break;
            }
        }
        setStatus(newStatus, newStatusBrief);
    }
}
