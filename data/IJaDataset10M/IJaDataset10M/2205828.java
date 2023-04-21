package isae.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author farah
 */
@Entity
@Table(name = "USER")
public class User {

    public static String ADMIN_LEVEL = "administrator";

    public static String REGULAR_LEVEL = "regularUser";

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    Long id;

    private String username;

    private String password;

    private String accessLevel;

    @Column(name = "AccessLevel")
    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @SequenceGenerator(initialValue = 0, name = "idGenerator")
    @Id
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "UserID")
    public Long getId() {
        return id;
    }

    public void setId(Long userID) {
        this.id = userID;
    }

    @Column(name = "UserName", unique = true, nullable = false, length = 100)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "Password", length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
