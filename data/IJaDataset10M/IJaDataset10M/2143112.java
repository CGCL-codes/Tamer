package org.ministone.mlets.user.domain;

/**
 * 
 *@author Sun Wenju
 *@since 0.1
 */
public abstract class UserProfileData implements java.io.Serializable {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 4772581135225499430L;

    private char gender;

    /**
     * 
     */
    public char getGender() {
        return this.gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    private java.lang.String remark;

    /**
     * 
     */
    public java.lang.String getRemark() {
        return this.remark;
    }

    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    private java.lang.String imageHead;

    /**
     * 
     */
    public java.lang.String getImageHead() {
        return this.imageHead;
    }

    public void setImageHead(java.lang.String imageHead) {
        this.imageHead = imageHead;
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    private org.ministone.mlets.user.domain.AccountData accountData;

    /**
     * 
     */
    public org.ministone.mlets.user.domain.AccountData getAccountData() {
        return this.accountData;
    }

    public void setAccountData(org.ministone.mlets.user.domain.AccountData accountData) {
        this.accountData = accountData;
    }

    /**
     * Returns <code>true</code> if the argument is an UserProfileData instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserProfileData)) {
            return false;
        }
        final UserProfileData that = (UserProfileData) object;
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

    /**
     * Constructs new instances of {@link org.ministone.mlets.user.domain.UserProfileData}.
     */
    public static final class Factory {

        /**
         * Constructs a new instance of {@link org.ministone.mlets.user.domain.UserProfileData}.
         */
        public static org.ministone.mlets.user.domain.UserProfileData newInstance() {
            return new org.ministone.mlets.user.domain.UserProfileDataImpl();
        }

        /**
         * Constructs a new instance of {@link org.ministone.mlets.user.domain.UserProfileData}, taking all required and/or
         * read-only properties as arguments.
         */
        public static org.ministone.mlets.user.domain.UserProfileData newInstance(org.ministone.mlets.user.domain.AccountData accountData) {
            final org.ministone.mlets.user.domain.UserProfileData entity = new org.ministone.mlets.user.domain.UserProfileDataImpl();
            entity.setAccountData(accountData);
            return entity;
        }

        /**
         * Constructs a new instance of {@link org.ministone.mlets.user.domain.UserProfileData}, taking all possible properties
         * (except the identifier(s))as arguments.
         */
        public static org.ministone.mlets.user.domain.UserProfileData newInstance(char gender, java.lang.String remark, java.lang.String imageHead, org.ministone.mlets.user.domain.AccountData accountData) {
            final org.ministone.mlets.user.domain.UserProfileData entity = new org.ministone.mlets.user.domain.UserProfileDataImpl();
            entity.setGender(gender);
            entity.setRemark(remark);
            entity.setImageHead(imageHead);
            entity.setAccountData(accountData);
            return entity;
        }
    }
}
