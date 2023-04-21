package org.openuss.lecture;

/**
 * 
 */
public class CourseMemberInfo implements java.io.Serializable {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = 284728550300455951L;

    public CourseMemberInfo() {
        this.courseId = null;
        this.userId = null;
        this.username = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.memberType = null;
        this.title = null;
    }

    public CourseMemberInfo(Long courseId, Long userId, String username, String firstName, String lastName, String email, org.openuss.lecture.CourseMemberType memberType, String title) {
        this.courseId = courseId;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.memberType = memberType;
        this.title = title;
    }

    /**
	 * Copies constructor from other CourseMemberInfo
	 * 
	 * @param otherBean
	 *            , cannot be <code>null</code>
	 * @throws NullPointerException
	 *             if the argument is <code>null</code>
	 */
    public CourseMemberInfo(CourseMemberInfo otherBean) {
        this(otherBean.getCourseId(), otherBean.getUserId(), otherBean.getUsername(), otherBean.getFirstName(), otherBean.getLastName(), otherBean.getEmail(), otherBean.getMemberType(), otherBean.getTitle());
    }

    /**
	 * Copies all properties from the argument value object into this value
	 * object.
	 */
    public void copy(CourseMemberInfo otherBean) {
        this.setCourseId(otherBean.getCourseId());
        this.setUserId(otherBean.getUserId());
        this.setUsername(otherBean.getUsername());
        this.setFirstName(otherBean.getFirstName());
        this.setLastName(otherBean.getLastName());
        this.setEmail(otherBean.getEmail());
        this.setMemberType(otherBean.getMemberType());
        this.setTitle(otherBean.getTitle());
    }

    private Long courseId;

    /**
     * 
     */
    public Long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    private Long userId;

    /**
     * 
     */
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String username;

    /**
     * 
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String firstName;

    /**
     * 
     */
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName;

    /**
     * 
     */
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String email;

    /**
     * 
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private org.openuss.lecture.CourseMemberType memberType;

    /**
     * 
     */
    public org.openuss.lecture.CourseMemberType getMemberType() {
        return this.memberType;
    }

    public void setMemberType(org.openuss.lecture.CourseMemberType memberType) {
        this.memberType = memberType;
    }

    private String title;

    /**
     * 
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
