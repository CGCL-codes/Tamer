package csiebug.domain.hibernateImpl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import csiebug.domain.UserEmail;

/**
 * 
 * @author George_Tsai
 * @version 2010/9/29
 *
 */
public class UserEmailImpl extends BasicObjectImpl implements UserEmail {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String emailAccount;

    private String emailDomain;

    private Boolean majorFlag;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setMajorFlag(Boolean majorFlag) {
        this.majorFlag = majorFlag;
    }

    public Boolean getMajorFlag() {
        return majorFlag;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserEmailImpl)) {
            return false;
        }
        UserEmailImpl userEmail = (UserEmailImpl) obj;
        return new EqualsBuilder().append(this.userId, userEmail.getUserId()).append(this.emailAccount, userEmail.getEmailAccount()).append(this.emailDomain, userEmail.getEmailDomain()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(this.userId).append(this.emailAccount).append(this.emailDomain).toHashCode();
    }

    public String toString() {
        return getEmailAccount() + "@" + getEmailDomain();
    }
}
