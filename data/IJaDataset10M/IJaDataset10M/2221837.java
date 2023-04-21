package jwt.data.models;

import java.io.Serializable;

/** @hibernate.class table="JWT_USER"
 * @hibernate.cache usage="transactional"*/
public class UserModel implements Serializable {

    private Long id;

    private String name;

    private String surname;

    private String login;

    private String password;

    /**
	 *  @return Returns the id.
	 *  @hibernate.id generator-class="native" unsaved-value="null"
	 *  
	 */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** @hibernate.property length="20" not-null="true" */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /** @hibernate.property length="20" not-null="true" */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** @hibernate.property length="20" not-null="true" */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** @hibernate.property length="20" not-null="true" */
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
