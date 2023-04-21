package common.model;

import com.sun.xml.registry.uddi.bindings_v2_2.Email;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;

/**
 *
 * @author shousuke
 */
@Entity
@Table(name = "WEB_USER")
@NamedQueries({ @NamedQuery(name = "WebUser.findByUsername", query = "SELECT w FROM WebUser w WHERE w.username = :username") })
public class WebUser extends GenericEntityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createdDate;

    private Date modifiedDate;

    private String oprtnlFlag;

    private String username;

    private String password;

    private String displayName;

    private String firstName;

    private String lastName;

    private String middleName;

    private String gender;

    private Date birthDate;

    private String headline;

    private String mood;

    private Date lastLogin;

    private int onlineStatus;

    @Id
    @GeneratedValue(generator = "WebUserSeq")
    @TableGenerator(name = "WebUserSeq", table = "ID_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "LAST_SEQUENCE", pkColumnValue = "WEB_USER", allocationSize = 1)
    @Column(name = "SID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CREATED_DATE", nullable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "MODIFIED_DATE", nullable = false)
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Column(name = "OPRTNL_FLAG", nullable = false, length = 1)
    public String getOprtnlFlag() {
        return oprtnlFlag;
    }

    public void setOprtnlFlag(String oprtnlFlag) {
        this.oprtnlFlag = oprtnlFlag;
    }

    @Column(name = "PASSWORD", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "USERNAME", nullable = false, length = 250)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "BIRTH_DATE", nullable = true)
    @Temporal(value = javax.persistence.TemporalType.DATE)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "DISPLAY_NAME", nullable = false, length = 30)
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column(name = "FIRST_NAME", nullable = true, length = 30)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "GENDER", nullable = false, length = 15)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "HEADLINE", nullable = true, length = 100)
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Column(name = "LAST_NAME", nullable = true, length = 30)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "MIDDLE_NAME", nullable = true, length = 30)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(name = "MOOD", nullable = true, length = 50)
    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    @Column(name = "ONLINE_STATUS", nullable = true, length = 1)
    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Column(name = "LAST_LOGIN", nullable = true)
    @Temporal(value = javax.persistence.TemporalType.DATE)
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
