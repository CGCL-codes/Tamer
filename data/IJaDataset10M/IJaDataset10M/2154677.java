package com.projectmanagement.model.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = 3064234008462630246L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "company_id", nullable = true)
    private Company company;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "homepage", nullable = true)
    private String homepage;

    @Column(name = "displey_name", nullable = true)
    private String displayName;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "use_avatar", nullable = true)
    private boolean useAvatar;

    @Column(name = "office_number", nullable = true)
    private String officeNumber;

    @Column(name = "fax_number", nullable = true)
    private String faxNumber;

    @Column(name = "home_number", nullable = true)
    private String homeNumber;

    @Column(name = "created_on", nullable = false)
    private Timestamp createdOn = new Timestamp(0);

    @Column(name = "created_by_id", nullable = true)
    private User createdBy;

    @Column(name = "updated_on", nullable = false)
    private Timestamp updatedOn = new Timestamp(0);

    @Column(name = "last_login", nullable = false)
    private Timestamp lastLogin = new Timestamp(0);

    @Column(name = "last_visit", nullable = false)
    private Timestamp lastVisit = new Timestamp(0);

    @Column(name = "last_activity", nullable = false)
    private Timestamp lastActivity = new Timestamp(0);

    @Column(name = "userType", nullable = false)
    private String userType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_PERMISSIONS", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") })
    private List<Permission> permissions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private List<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_GROUPS", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "group_id", referencedColumnName = "id") })
    private List<Group> groups;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ReceivedMessage> receivedMessage;

    @OneToMany(fetch = FetchType.LAZY)
    private List<SentMessage> sentMessage;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUseAvatar() {
        return useAvatar;
    }

    public void setUseAvatar(boolean useAvatar) {
        this.useAvatar = useAvatar;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Timestamp getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Timestamp lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Timestamp getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Timestamp lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<ReceivedMessage> getReceivedMessages() {
        return receivedMessage;
    }

    public void setReceivedMessages(List<ReceivedMessage> receivedMessage) {
        this.receivedMessage = receivedMessage;
    }

    public List<SentMessage> getSentMessages() {
        return sentMessage;
    }

    public void setSentMessages(List<SentMessage> sentMessage) {
        this.sentMessage = sentMessage;
    }
}
