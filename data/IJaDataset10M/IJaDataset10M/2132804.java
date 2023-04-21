package ru.sgnhp.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*****
 *
 * @author Alexey Khudyakov
 * @company "Salavatgazoneftehimproekt" Ltd
 *
 *****
 */
@Entity
@Table(name = "users", catalog = "workflowdb", schema = "")
@NamedQueries({ @NamedQuery(name = "WorkflowUserBean.findAll", query = "SELECT w FROM WorkflowUserBean w order by w.lastName"), @NamedQuery(name = "WorkflowUserBean.findByUid", query = "SELECT w FROM WorkflowUserBean w WHERE w.uid = :uid"), @NamedQuery(name = "WorkflowUserBean.findByLogin", query = "SELECT w FROM " + "WorkflowUserBean w WHERE w.login = :login"), @NamedQuery(name = "WorkflowUserBean.findByLastName", query = "SELECT w FROM" + " WorkflowUserBean w WHERE w.lastName = :lastName"), @NamedQuery(name = "WorkflowUserBean.findByFirstName", query = "SELECT w FROM " + "WorkflowUserBean w WHERE w.firstName = :firstName"), @NamedQuery(name = "WorkflowUserBean.findByMiddleName", query = "SELECT w FROM " + "WorkflowUserBean w WHERE w.middleName = :middleName"), @NamedQuery(name = "WorkflowUserBean.findByEmail", query = "SELECT w FROM " + "WorkflowUserBean w WHERE w.email = :email"), @NamedQuery(name = "WorkflowUserBean.findBySessionUid", query = "SELECT w FROM " + "WorkflowUserBean w WHERE w.sessionUid = :sessionUid") })
public class WorkflowUserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Uid", nullable = false)
    private Long uid;

    @Basic(optional = false)
    @Column(name = "Login", nullable = false, length = 20)
    private String login;

    @Column(name = "LastName", length = 50)
    private String lastName;

    @Column(name = "FirstName", length = 50)
    private String firstName;

    @Column(name = "MiddleName", length = 50)
    private String middleName;

    @Column(name = "Email", length = 50)
    private String email;

    @Column(name = "SessionUid", length = 50)
    private String sessionUid;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    @OrderBy("uid desc")
    private Set<WorkflowBean> assignedWorkflows = new HashSet<WorkflowBean>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    @OrderBy("uid desc")
    private Set<WorkflowBean> receivedWorkflows = new HashSet<WorkflowBean>();

    @ForeignKey(name = "fk_groups_users")
    @JoinColumn(name = "GroupUid", referencedColumnName = "Uid", nullable = false)
    @ManyToOne(optional = false)
    private UserGroupBean userGroupBean;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "workflowUserBean", fetch = FetchType.LAZY)
    @OrderBy("uid desc")
    private Set<OutgoingMailBean> outgoingMailBeans = new HashSet<OutgoingMailBean>();

    public WorkflowUserBean() {
    }

    public WorkflowUserBean(Long uid) {
        this.uid = uid;
    }

    public WorkflowUserBean(Long uid, String login) {
        this.uid = uid;
        this.login = login;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionUid() {
        return sessionUid;
    }

    public void setSessionUid(String sessionUid) {
        this.sessionUid = sessionUid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uid != null ? uid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WorkflowUserBean)) {
            return false;
        }
        WorkflowUserBean other = (WorkflowUserBean) object;
        if ((this.uid == null && other.uid != null) || (this.uid != null && !this.uid.equals(other.uid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.sgnhp.domain.WorkflowUserBean[uid=" + uid + "]";
    }

    public Set<WorkflowBean> getAssignedWorkflows() {
        return assignedWorkflows;
    }

    public void setAssignedWorkflows(Set<WorkflowBean> assignedWorkflows) {
        this.assignedWorkflows = assignedWorkflows;
    }

    public Set<WorkflowBean> getReceivedWorkflows() {
        return receivedWorkflows;
    }

    public void setReceivedWorkflows(Set<WorkflowBean> receivedWorkflows) {
        this.receivedWorkflows = receivedWorkflows;
    }

    public UserGroupBean getUserGroupBean() {
        return userGroupBean;
    }

    public void setUserGroupBean(UserGroupBean userGroupBean) {
        this.userGroupBean = userGroupBean;
    }

    public Set<OutgoingMailBean> getOutgoingMailBeans() {
        return outgoingMailBeans;
    }

    public void setOutgoingMailBeans(Set<OutgoingMailBean> outgoingMailBeans) {
        this.outgoingMailBeans = outgoingMailBeans;
    }
}
