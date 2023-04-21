package fr.helmet.entity.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.Length;
import org.hibernate.validator.Pattern;
import fr.helmet.utils.Encrypt;
import fr.helmet.utils.RandomUtils;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;

/**
 * Abstract class used by all account types.
 * @author "Vincent LAUGIER"
 *
 */
@Entity
public abstract class AbstractPerson extends AbstractMoralEntity {

    private Logger logger = Logger.getLogger(AbstractPerson.class);

    private String lastname;

    private String firstname;

    private String login;

    private String password;

    private Date birthDate;

    private String clearPassword;

    private Gender gender;

    private String fullname;

    private List<AbstractGroup> groups;

    protected AbstractPerson() {
    }

    @Override
    @Transient
    public String getName() {
        return this.lastname + " " + this.firstname;
    }

    @Length(max = 50)
    @NotNull
    @Pattern(regex = "([\\p{L}\\s-'.]{2,50})?")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Length(max = 50)
    @NotNull
    @Pattern(regex = "([\\p{L}\\s-'.]{2,50})?")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(unique = true)
    @Length(max = 60)
    @NotNull
    @Pattern(regex = "([a-zA-Z0-9_\\.-@]{5,30})?")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Length(max = 32)
    @Pattern(regex = "([a-zA-Z0-9_\\.-]{5,32})?")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(TemporalType.DATE)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @PreUpdate
    @PrePersist
    public void checkBeforeUpdatePersistAbstractPerson() {
        if (this.firstname == null && this.lastname != null) {
            String tmp = this.lastname;
            if (this.lastname.contains(" ")) {
                this.lastname = StringUtils.substringAfter(tmp, " ");
                this.firstname = StringUtils.substringBefore(tmp, " ");
            }
        }
        if (this.login == null) {
            this.login = this.getEmail();
        }
        if (this.password != null && this.password.length() != 32) {
            logger.warn("encrypt !!");
            this.password = Encrypt.encode(this.password);
        } else {
            logger.warn("do not encrypt !!");
        }
        if (this.gender == null) {
            this.gender = Gender.MALE;
        }
    }

    public void generatePassword() {
        String newPassword = RandomUtils.generatePassword();
        this.password = newPassword;
        this.clearPassword = newPassword;
    }

    @Transient
    public String getFullname() {
        String fullname = "";
        if (this.firstname != null) {
            fullname = firstname;
        }
        if (this.lastname != null) {
            fullname += " " + this.lastname.toUpperCase();
        }
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Column(name = "gender", columnDefinition = "integer", nullable = true)
    @Type(type = "fr.helmet.entity.enumeration.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "fr.helmet.entity.user.Gender"), @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "fromCode") })
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Column(name = "clear_password")
    @Length(max = 32)
    @Pattern(regex = "([a-zA-Z0-9_\\.-]{5,32})?")
    public String getClearPassword() {
        return clearPassword;
    }

    public void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }

    @ManyToMany
    @JoinTable(name = "group_person_asso", joinColumns = { @JoinColumn(name = "person_id") }, inverseJoinColumns = { @JoinColumn(name = "group_id") })
    public List<AbstractGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<AbstractGroup> groups) {
        this.groups = groups;
    }
}
