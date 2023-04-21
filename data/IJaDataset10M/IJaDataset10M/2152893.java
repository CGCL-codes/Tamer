package org.openuss.lecture;

/**
 * Institute
 */
public abstract class InstituteBase extends org.openuss.lecture.OrganisationImpl {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5663621085555955744L;

    private org.openuss.lecture.Department department;

    /**
     * 
     */
    public org.openuss.lecture.Department getDepartment() {
        return this.department;
    }

    public void setDepartment(org.openuss.lecture.Department department) {
        this.department = department;
    }

    private java.util.List<org.openuss.lecture.CourseType> courseTypes = new java.util.ArrayList<org.openuss.lecture.CourseType>();

    /**
     * 
     */
    public java.util.List<org.openuss.lecture.CourseType> getCourseTypes() {
        return this.courseTypes;
    }

    public void setCourseTypes(java.util.List<org.openuss.lecture.CourseType> courseTypes) {
        this.courseTypes = courseTypes;
    }

    private java.util.List<org.openuss.lecture.Application> applications = new java.util.ArrayList<org.openuss.lecture.Application>();

    /**
     * 
     */
    public java.util.List<org.openuss.lecture.Application> getApplications() {
        return this.applications;
    }

    public void setApplications(java.util.List<org.openuss.lecture.Application> applications) {
        this.applications = applications;
    }

    /**
     * 
     */
    public abstract void add(org.openuss.lecture.CourseType courseType);

    /**
     * 
     */
    public abstract void remove(org.openuss.lecture.CourseType courseType);

    /**
     * <p>
     * @Deprecated  Use UniversityService.findActiveCoursesByUniversity
     * instead
     * </p>
     */
    public abstract java.util.List getActiveCourses();

    /**
     * 
     */
    public abstract java.util.List getAllCourses();

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>org.openuss.lecture.OrganisationImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see org.openuss.lecture.Organisation#equals(Object)
     */
    public boolean equals(Object object) {
        return super.equals(object);
    }

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>org.openuss.lecture.OrganisationImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see org.openuss.lecture.Organisation#hashCode()
     */
    public int hashCode() {
        return super.hashCode();
    }
}
