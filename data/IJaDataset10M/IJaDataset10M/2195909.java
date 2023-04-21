package com.walle.pojo;

/**
 * @author Administrator
 *
 */
public class Staff {

    String staffID;

    String staffName;

    String email;

    /**
	 * @return the staffID
	 */
    public String getStaffID() {
        return staffID;
    }

    /**
	 * @param staffID the staffID to set
	 */
    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    /**
	 * @return the staffName
	 */
    public String getStaffName() {
        return staffName;
    }

    /**
	 * @param staffName the staffName to set
	 */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email the email to set
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((staffID == null) ? 0 : staffID.hashCode());
        result = prime * result + ((staffName == null) ? 0 : staffName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Staff other = (Staff) obj;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        if (staffID == null) {
            if (other.staffID != null) return false;
        } else if (!staffID.equals(other.staffID)) return false;
        if (staffName == null) {
            if (other.staffName != null) return false;
        } else if (!staffName.equals(other.staffName)) return false;
        return true;
    }
}
