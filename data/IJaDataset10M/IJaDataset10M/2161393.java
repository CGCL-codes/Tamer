package org.openuss.lecture;

import java.util.Date;

/**
 * @author Ingo Dueppe 
 */
public class ApplicationInfo implements java.io.Serializable, org.openuss.foundation.DomainObject {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = -2683932134988526291L;

    public ApplicationInfo() {
        this.id = null;
        this.applicationDate = null;
        this.confirmed = false;
        this.departmentInfo = null;
        this.instituteInfo = null;
        this.applyingUserInfo = null;
    }

    public ApplicationInfo(Long id, Date applicationDate, boolean confirmed, org.openuss.lecture.DepartmentInfo departmentInfo, org.openuss.lecture.InstituteInfo instituteInfo, org.openuss.security.UserInfo applyingUserInfo) {
        this.id = id;
        this.applicationDate = applicationDate;
        this.confirmed = confirmed;
        this.departmentInfo = departmentInfo;
        this.instituteInfo = instituteInfo;
        this.applyingUserInfo = applyingUserInfo;
    }

    public ApplicationInfo(Long id, Date applicationDate, Date confirmationDate, String description, boolean confirmed, org.openuss.lecture.DepartmentInfo departmentInfo, org.openuss.lecture.InstituteInfo instituteInfo, org.openuss.security.UserInfo applyingUserInfo, org.openuss.security.UserInfo confirmingUserInfo) {
        this.id = id;
        this.applicationDate = applicationDate;
        this.confirmationDate = confirmationDate;
        this.description = description;
        this.confirmed = confirmed;
        this.departmentInfo = departmentInfo;
        this.instituteInfo = instituteInfo;
        this.applyingUserInfo = applyingUserInfo;
        this.confirmingUserInfo = confirmingUserInfo;
    }

    /**
	 * Copies constructor from other ApplicationInfo
	 * 
	 * @param otherBean
	 *            , cannot be <code>null</code>
	 * @throws NullPointerException
	 *             if the argument is <code>null</code>
	 */
    public ApplicationInfo(ApplicationInfo otherBean) {
        this(otherBean.getId(), otherBean.getApplicationDate(), otherBean.getConfirmationDate(), otherBean.getDescription(), otherBean.isConfirmed(), otherBean.getDepartmentInfo(), otherBean.getInstituteInfo(), otherBean.getApplyingUserInfo(), otherBean.getConfirmingUserInfo());
    }

    /**
	 * Copies all properties from the argument value object into this value
	 * object.
	 */
    public void copy(ApplicationInfo otherBean) {
        this.setId(otherBean.getId());
        this.setApplicationDate(otherBean.getApplicationDate());
        this.setConfirmationDate(otherBean.getConfirmationDate());
        this.setDescription(otherBean.getDescription());
        this.setConfirmed(otherBean.isConfirmed());
        this.setDepartmentInfo(otherBean.getDepartmentInfo());
        this.setInstituteInfo(otherBean.getInstituteInfo());
        this.setApplyingUserInfo(otherBean.getApplyingUserInfo());
        this.setConfirmingUserInfo(otherBean.getConfirmingUserInfo());
    }

    private Long id;

    /**
     * 
     */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Date applicationDate;

    /**
     * 
     */
    public Date getApplicationDate() {
        return this.applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    private Date confirmationDate;

    /**
     * 
     */
    public Date getConfirmationDate() {
        return this.confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    private String description;

    /**
     * 
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private boolean confirmed;

    /**
     * 
     */
    public boolean isConfirmed() {
        return this.confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    private org.openuss.lecture.DepartmentInfo departmentInfo;

    /**
     * 
     */
    public org.openuss.lecture.DepartmentInfo getDepartmentInfo() {
        return this.departmentInfo;
    }

    public void setDepartmentInfo(org.openuss.lecture.DepartmentInfo departmentInfo) {
        this.departmentInfo = departmentInfo;
    }

    private org.openuss.lecture.InstituteInfo instituteInfo;

    /**
     * 
     */
    public org.openuss.lecture.InstituteInfo getInstituteInfo() {
        return this.instituteInfo;
    }

    public void setInstituteInfo(org.openuss.lecture.InstituteInfo instituteInfo) {
        this.instituteInfo = instituteInfo;
    }

    private org.openuss.security.UserInfo applyingUserInfo;

    /**
     * 
     */
    public org.openuss.security.UserInfo getApplyingUserInfo() {
        return this.applyingUserInfo;
    }

    public void setApplyingUserInfo(org.openuss.security.UserInfo applyingUserInfo) {
        this.applyingUserInfo = applyingUserInfo;
    }

    private org.openuss.security.UserInfo confirmingUserInfo;

    /**
     * 
     */
    public org.openuss.security.UserInfo getConfirmingUserInfo() {
        return this.confirmingUserInfo;
    }

    public void setConfirmingUserInfo(org.openuss.security.UserInfo confirmingUserInfo) {
        this.confirmingUserInfo = confirmingUserInfo;
    }

    /**
	 * Returns <code>true</code> if the argument is an ApplicationInfo instance
	 * and all identifiers for this object equal the identifiers of the argument
	 * object. Returns <code>false</code> otherwise.
	 */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ApplicationInfo)) {
            return false;
        }
        final ApplicationInfo that = (ApplicationInfo) object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
	 * Returns a hash code based on this entity's identifiers.
	 */
    public int hashCode() {
        int hashCode = 0;
        hashCode = 29 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }
}
