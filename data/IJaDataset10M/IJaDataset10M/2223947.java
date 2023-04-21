package ua.org.hatu.daos.engine.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Class that represents user entity: admin, student, tutor 
 * 
 * @author zeus (alex.pogrebnyuk@gmail.com)
 *
 */
@Entity
@Table(name = "USERS")
public class User implements DomainObject, Comparable<User> {

    private static final long serialVersionUID = 6715801778012248710L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRST_NAME", unique = false, nullable = true, length = 255)
    private String firstName;

    @Column(name = "SECOND_NAME", unique = false, nullable = true, length = 255)
    private String secondName;

    @Column(name = "LAST_NAME", unique = false, nullable = true, length = 255)
    private String lastName;

    @Column(name = "USERNAME", unique = true, updatable = false, nullable = false, length = 30)
    private String username;

    @Column(name = "PASSWORD", unique = false, nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "RANKING", unique = false, nullable = false)
    private int ranking = 0;

    @Column(name = "CREATED", unique = false, nullable = false)
    private Date created = new Date();

    @OneToOne(cascade = CascadeType.ALL)
    private UserSession session;

    @ManyToMany(targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles = new HashSet<Role>();

    public User() {
    }

    public User(String userName, String password, String email) {
        this.username = userName;
        this.password = password;
        this.email = email;
    }

    public final Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
	 * @return the session
	 */
    public UserSession getSession() {
        return session;
    }

    /**
	 * @param session the session to set
	 */
    public void setSession(UserSession session) {
        this.session = session;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    public int hashCode() {
        return getUsername().hashCode();
    }

    public int compareTo(User user) {
        return Long.valueOf(this.getCreated().getTime()).compareTo(Long.valueOf(user.getCreated().getTime()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[User: {");
        sb.append("firstName = " + this.firstName + "; ");
        sb.append("secondName = " + this.secondName + "; ");
        sb.append("username = " + this.username + "; ");
        sb.append("email = " + this.email);
        sb.append("}]");
        return sb.toString();
    }
}
