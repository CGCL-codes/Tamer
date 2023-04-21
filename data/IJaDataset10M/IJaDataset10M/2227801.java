package org.examcity.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.util.Assert;

/**
 * Entity that represents an account for normal users (administrators, exam creators, reviewers).
 */
@Entity
@Table(name = "EC_USER")
public class User {

    @Id
    @Column(name = "USER_ID")
    @SequenceGenerator(name = "SEQ_EC_USER")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(name = "FULL_NAME", nullable = false, length = 80)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private boolean superuser;

    @Column(name = "STATUS_ID", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    @Column(length = 250)
    private String comments;

    public User() {
        status = UserStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
	 * Marks this user as <i>active</i>.
	 */
    public void activate() {
        Assert.isTrue(status == UserStatus.INACTIVE, "Cannot activate user with status " + status);
        status = UserStatus.ACTIVE;
    }

    /**
	 * Marks this user as <i>inactive</i>.
	 */
    public void deactivate() {
        Assert.isTrue(status == UserStatus.ACTIVE, "Cannot inactivate user with status " + status);
        status = UserStatus.INACTIVE;
    }

    /**
	 * Marks this user as <i>deleted</i>.
	 * <p>
	 * This method changes the username to <code>"username${id}"</code> so that it is possible to
	 * create a new user with the same username in the future. Additionally, this method does not
	 * delete the instance from the database.
	 */
    public void delete() {
        Assert.notNull(id, "Cannot delete a non-saved user");
        Assert.isTrue(status != UserStatus.DELETED, "Cannot delete user with status " + status);
        status = UserStatus.DELETED;
        username = username + "$" + id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(username).toHashCode();
    }

    @Override
    public boolean equals(Object _o) {
        if (_o instanceof User) {
            User o = (User) _o;
            return new EqualsBuilder().append(username, o.getUsername()).isEquals();
        }
        return false;
    }
}
