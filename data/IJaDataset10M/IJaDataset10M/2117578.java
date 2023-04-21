package ua.org.nuos.sdms.middle.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 29.01.12
 * Time: 18:50
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user_registration")
@NamedQueries(value = { @NamedQuery(name = "findUserByRegistrationCode", query = "SELECT DISTINCT u FROM UserRegistration ur, User u WHERE ur.code = :code AND ur.userId = u.id") })
public class UserRegistration implements Serializable {

    @Id
    @Column(name = "user_id")
    private long userId;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    public UserRegistration() {
    }

    public UserRegistration(long userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistration that = (UserRegistration) o;
        if (userId != that.userId) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRegistration{" + "userId=" + userId + ", code='" + code + '\'' + '}';
    }
}
