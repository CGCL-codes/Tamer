package edu.harvard.iq.safe.saasystem.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Akio Sone
 */
@Entity
@Table(name = "access_account_table", catalog = "safe_archive_system_db", schema = "")
@NamedQueries({ @NamedQuery(name = "AccessAccountTable.findAll", query = "SELECT a FROM AccessAccountTable a"), @NamedQuery(name = "AccessAccountTable.findByAaId", query = "SELECT a FROM AccessAccountTable a WHERE a.aaId = :aaId"), @NamedQuery(name = "AccessAccountTable.findByIpAddress", query = "SELECT a FROM AccessAccountTable a WHERE a.ipAddress = :ipAddress"), @NamedQuery(name = "AccessAccountTable.findByPortNumber", query = "SELECT a FROM AccessAccountTable a WHERE a.portNumber = :portNumber"), @NamedQuery(name = "AccessAccountTable.findByHostName", query = "SELECT a FROM AccessAccountTable a WHERE a.hostName = :hostName"), @NamedQuery(name = "AccessAccountTable.findByAdminEmail", query = "SELECT a FROM AccessAccountTable a WHERE a.adminEmail = :adminEmail"), @NamedQuery(name = "AccessAccountTable.findByUserName", query = "SELECT a FROM AccessAccountTable a WHERE a.userName = :userName"), @NamedQuery(name = "AccessAccountTable.findByUserPwd", query = "SELECT a FROM AccessAccountTable a WHERE a.userPwd = :userPwd"), @NamedQuery(name = "AccessAccountTable.findByUserAdmin", query = "SELECT a FROM AccessAccountTable a WHERE a.userAdmin = :userAdmin"), @NamedQuery(name = "AccessAccountTable.findByAccessAdmin", query = "SELECT a FROM AccessAccountTable a WHERE a.accessAdmin = :accessAdmin"), @NamedQuery(name = "AccessAccountTable.findByCollectionAdmin", query = "SELECT a FROM AccessAccountTable a WHERE a.collectionAdmin = :collectionAdmin"), @NamedQuery(name = "AccessAccountTable.findByViewDebugInfo", query = "SELECT a FROM AccessAccountTable a WHERE a.viewDebugInfo = :viewDebugInfo"), @NamedQuery(name = "AccessAccountTable.findByUserDesc", query = "SELECT a FROM AccessAccountTable a WHERE a.userDesc = :userDesc"), @NamedQuery(name = "AccessAccountTable.findByRegisteredDate", query = "SELECT a FROM AccessAccountTable a WHERE a.registeredDate = :registeredDate") })
public class AccessAccountTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aa_id", nullable = false)
    private Long aaId;

    @Basic(optional = false)
    @Column(name = "ip_address", nullable = false, length = 15)
    private String ipAddress;

    @Basic(optional = false)
    @Column(name = "port_number", nullable = false, length = 5)
    private String portNumber;

    @Basic(optional = false)
    @Column(name = "host_name", nullable = false, length = 255)
    private String hostName;

    @Column(name = "admin_email", length = 255)
    private String adminEmail;

    @Basic(optional = false)
    @Column(name = "user_name", nullable = false, length = 128)
    private String userName;

    @Basic(optional = false)
    @Column(name = "user_pwd", nullable = false, length = 128)
    private String userPwd;

    @Basic(optional = false)
    @Column(name = "user_admin", nullable = false)
    private boolean userAdmin;

    @Basic(optional = false)
    @Column(name = "access_admin", nullable = false)
    private boolean accessAdmin;

    @Basic(optional = false)
    @Column(name = "collection_admin", nullable = false)
    private boolean collectionAdmin;

    @Basic(optional = false)
    @Column(name = "view_debug_info", nullable = false)
    private boolean viewDebugInfo;

    @Column(name = "user_desc", length = 255)
    private String userDesc;

    @Column(name = "registered_date")
    private Long registeredDate;

    public AccessAccountTable() {
    }

    public AccessAccountTable(Long aaId) {
        this.aaId = aaId;
    }

    public AccessAccountTable(Long aaId, String ipAddress, String portNumber, String hostName, String userName, String userPwd, boolean userAdmin, boolean accessAdmin, boolean collectionAdmin, boolean viewDebugInfo) {
        this.aaId = aaId;
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userAdmin = userAdmin;
        this.accessAdmin = accessAdmin;
        this.collectionAdmin = collectionAdmin;
        this.viewDebugInfo = viewDebugInfo;
    }

    public Long getAaId() {
        return aaId;
    }

    public void setAaId(Long aaId) {
        this.aaId = aaId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public boolean getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        this.userAdmin = userAdmin;
    }

    public boolean getAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public boolean getCollectionAdmin() {
        return collectionAdmin;
    }

    public void setCollectionAdmin(boolean collectionAdmin) {
        this.collectionAdmin = collectionAdmin;
    }

    public boolean getViewDebugInfo() {
        return viewDebugInfo;
    }

    public void setViewDebugInfo(boolean viewDebugInfo) {
        this.viewDebugInfo = viewDebugInfo;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public Long getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Long registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aaId != null ? aaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccessAccountTable)) {
            return false;
        }
        AccessAccountTable other = (AccessAccountTable) object;
        if ((this.aaId == null && other.aaId != null) || (this.aaId != null && !this.aaId.equals(other.aaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.harvard.iq.safe.saasystem.entities.AccessAccountTable[aaId=" + aaId + "]";
    }
}
