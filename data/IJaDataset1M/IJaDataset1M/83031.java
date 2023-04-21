package com.cinchblog.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Transient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.Length;
import org.joda.time.DateTime;
import com.cinchblog.entity.i.Permission;
import com.cinchblog.entity.i.Validation;
import com.cinchblog.entity.util.EntityBase;
import com.cinchblog.util.EmailUtils;
import com.cinchblog.util.PasswordUtils;

/**
 * Entity of a human user of the system.
 * 
 * @author Jeff Schnitzer
 */
@NamedQueries({ @NamedQuery(name = "PersonByEmail", query = "from Person per where per.email = :email", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }), @NamedQuery(name = "PersonByUsername", query = "from Person per where per.usernameNormal = :normal", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }), @NamedQuery(name = "SearchPeople", query = "from Person per where per.email like :fragment or per.username like :fragment", hints = {  }), @NamedQuery(name = "CountAllPeople", query = "select count(*) from Person", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }), @NamedQuery(name = "CountSearchedPeople", query = "select count(*) from Person per where per.email like :fragment or per.username like :fragment", hints = {  }) })
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class Person extends EntityBase implements Comparable<Object> {

    /** */
    @Transient
    private static Log log = LogFactory.getLog(Person.class);

    static final Set<String> USER_ROLES = Collections.singleton("user");

    static final Set<String> SITE_ADMIN_ROLES;

    static {
        Set<String> roles = new HashSet<String>();
        roles.add("person");
        roles.add("siteAdmin");
        SITE_ADMIN_ROLES = Collections.unmodifiableSet(roles);
    }

    /** */
    @Id
    @GeneratedValue
    Long id;

    public Long getId() {
        return this.id;
    }

    /** The display version of the username. */
    @Column(nullable = false, length = Validation.MAX_PERSON_USERNAME)
    String username;

    public String getUsername() {
        return this.username;
    }

    /** Normalized to all uppercase */
    @Column(nullable = false, unique = true, length = Validation.MAX_PERSON_USERNAME)
    String usernameNormal;

    @Column(nullable = false, length = Validation.MAX_PERSON_NAME)
    String name;

    /** */
    @Column(name = "passwd", nullable = false, length = Validation.MAX_PERSON_PASSWORD)
    @Length(min = 3)
    String password;

    public String getPassword() {
        return this.password;
    }

    /** @param value is a plaintext password */
    public void setPassword(String value) {
        this.password = value;
    }

    /** */
    @Column(nullable = false, unique = true, length = Validation.MAX_PERSON_EMAIL)
    String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String emailAddress) {
        this.email = EmailUtils.normalizeEmail(emailAddress);
    }

    /** */
    @Type(type = "dateTime")
    @Column(nullable = false)
    DateTime joinedOn;

    public DateTime getJoinedOn() {
        return this.joinedOn;
    }

    /** */
    @Type(type = "dateTime")
    @Column(nullable = true)
    DateTime lastLogin;

    public DateTime getLastLogin() {
        return this.lastLogin;
    }

    /** */
    @Column(nullable = false)
    boolean emailConfirmed;

    public boolean getEmailConfirmed() {
        return this.emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    @Column(nullable = false)
    boolean siteAdmin;

    public boolean isSiteAdmin() {
        return this.siteAdmin;
    }

    public void setSiteAdmin(boolean value) {
        this.siteAdmin = value;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    Set<Config> config;

    public Set<Config> getConfig() {
        return config;
    }

    public void setConfig(Set<Config> config) {
        this.config = config;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @MapKey(name = "blogId")
    Map<Long, Subscription> subscriptions;

    public Map<Long, Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<Long, Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
	 */
    public Person() {
    }

    /**
	 * Constructs a new person.
	 */
    public Person(String username, String password, String email) {
        if (log.isDebugEnabled()) log.debug("Creating new person " + username);
        if (!Validation.isValidUsername(username)) throw new IllegalArgumentException("Not a valid username");
        this.username = username;
        this.usernameNormal = normalizeUsername(username);
        this.password = password;
        this.setEmail(email);
        this.joinedOn = new DateTime();
    }

    /**
	 * Constructs a new person with a random password.
	 */
    public Person(String username, String email) {
        this(username, PasswordUtils.generateRandomPassword(), email);
    }

    /**
	 * Checks to see if the password matches.
	 *
	 * @return true if the password does match.
	 */
    public boolean checkPassword(String plainText) {
        return this.password.equals(plainText);
    }

    /** */
    public void loggedIn() {
        this.lastLogin = new DateTime();
    }

    /** */
    public String toString() {
        return this.getClass().getName() + " {id=" + this.id + ", email=" + this.email + "}";
    }

    /**
	 * Natural sort order is based on name
	 */
    public int compareTo(Object arg0) {
        Person other = (Person) arg0;
        return this.username.compareTo(other.getUsername());
    }

    /**
	 * Normalizes a username to what we consider unique.
	 */
    public static String normalizeUsername(String nick) {
        if (nick.indexOf(' ') >= 0) {
            StringBuffer foo = new StringBuffer(nick.length());
            for (int i = 0; i < nick.length(); i++) {
                char maybeSpace = nick.charAt(i);
                if (maybeSpace != ' ') foo.append(Character.toUpperCase(maybeSpace));
            }
            return foo.toString();
        } else {
            return nick.toUpperCase();
        }
    }

    /**
	 * Simply resets the password to a random value.
	 */
    public void resetPassword() {
        String newPassword = PasswordUtils.generateRandomPassword();
        this.setPassword(newPassword);
    }

    /** */
    public Role getRoleIn(Blog blog) {
        Subscription sub = this.subscriptions.get(blog.getId());
        return (sub == null) ? blog.getAnonymousRole() : sub.getRole();
    }

    /** */
    public Set<Permission> getPermissionsIn(Blog blog) {
        if (this.siteAdmin) return Permission.ALL; else return this.getRoleIn(blog).getPermissions();
    }

    /** @return the j2ee security roles associated with this person */
    public Set<String> getRoles() {
        if (this.siteAdmin) return SITE_ADMIN_ROLES; else return USER_ROLES;
    }
}
