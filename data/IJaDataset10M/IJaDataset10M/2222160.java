package referee.diary.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Masafumi Koba
 */
@XmlRootElement
@Entity
public class User extends AbstractEntity<User, String> {

    @Id
    private String id;

    @Version
    private Long version;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    public static final User NULL = new User() {

        @Override
        public String getId() {
            return null;
        }

        @Override
        public Long getVersion() {
            return null;
        }

        @Override
        public boolean isNull() {
            return true;
        }
    };

    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public Long getVersion() {
        return this.version;
    }

    public void setFirstName(final String arg) {
        this.firstName = arg;
    }

    @Override
    public void setId(final String arg) {
        this.id = arg;
    }

    public void setLastName(final String arg) {
        this.lastName = arg;
    }

    public void setPassword(final String arg) {
        this.password = arg;
    }

    @Override
    public void setVersion(final Long arg) {
        this.version = arg;
    }

    @Override
    protected User getNullObject() {
        return NULL;
    }

    @Override
    protected Class<User> getType() {
        return User.class;
    }
}
