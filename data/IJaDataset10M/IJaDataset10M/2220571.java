package com.fisoft.phucsinh.phucsinhsrv.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author me
 */
@Entity
@Table(name = "cm_customer", schema = "")
@NamedQueries({ @NamedQuery(name = "CmCustomer.findAll", query = "SELECT c FROM CmCustomer c"), @NamedQuery(name = "CmCustomer.findByCriteria", query = "SELECT e FROM CmCustomer e JOIN e.contact c WHERE " + " (:contact IS NULL OR e.contact = :contact)" + " AND c.iDNo LIKE :contactCode" + " AND c.name LIKE :contactName" + " AND (:branch IS NULL OR e.branch = :branch)" + " AND e.repName LIKE :repName" + " AND e.email LIKE :email" + " AND e.mobile LIKE :mobile" + " AND e.fax LIKE :fax" + " AND e.repHomeStreet LIKE :repHomeStreet" + " AND (:repProvince IS NULL OR e.repProvince = :repProvince)" + " AND (:repCountry IS NULL OR e.repCountry = :repCountry)" + " AND e.creditLimit >= :creditLimitMin AND e.creditLimit <= :creditLimitMax" + " AND e.activeStatus >= :activeStatusMin AND e.activeStatus <= :activeStatusMax"), @NamedQuery(name = "CmCustomer.countByCriteria", query = "SELECT COUNT(e.customerID) FROM CmCustomer e JOIN e.contact c WHERE " + " (:contact IS NULL OR e.contact = :contact)" + " AND c.iDNo LIKE :contactCode" + " AND c.name LIKE :contactName" + " AND (:branch IS NULL OR e.branch = :branch)" + " AND e.repName LIKE :repName" + " AND e.email LIKE :email" + " AND e.mobile LIKE :mobile" + " AND e.fax LIKE :fax" + " AND e.repHomeStreet LIKE :repHomeStreet" + " AND (:repProvince IS NULL OR e.repProvince = :repProvince)" + " AND (:repCountry IS NULL OR e.repCountry = :repCountry)" + " AND e.creditLimit >= :creditLimitMin AND e.creditLimit <= :creditLimitMax" + " AND e.activeStatus >= :activeStatusMin AND e.activeStatus <= :activeStatusMax") })
public class CmCustomer implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CustomerID", nullable = false)
    private Integer customerID;

    @Column(name = "RepName", length = 200)
    private String repName = "";

    @Column(name = "Title")
    private Integer title;

    @Column(name = "Gender")
    private Integer gender;

    @Column(name = "RepHomeStreet", length = 200)
    private String repHomeStreet = "";

    @Column(name = "RepDistrict", length = 100)
    private String repDistrict = "";

    @Column(name = "RepProvince")
    private Integer repProvince;

    @Column(name = "RepCountry")
    private Integer repCountry;

    @Column(name = "Email", length = 30)
    private String email = "";

    @Column(name = "Mobile", length = 30)
    private String mobile = "";

    @Column(name = "HomePhone", length = 30)
    private String homePhone = "";

    @Column(name = "OfficePhone", length = 30)
    private String officePhone = "";

    @Column(name = "Fax", length = 30)
    private String fax = "";

    @Column(name = "OtherPhone", length = 30)
    private String otherPhone = "";

    @Column(name = "Comment", length = 255)
    private String comment = "";

    @Column(name = "Note", length = 255)
    private String note = "";

    @Column(name = "CreditLimit", precision = 22)
    private Double creditLimit = 0d;

    @Column(name = "AccountCode", length = 255)
    private String accountCode;

    @Column(name = "Atribute1", length = 250)
    private String atribute1;

    @Column(name = "Atribute2", length = 250)
    private String atribute2;

    @Column(name = "Atribute3", length = 250)
    private String atribute3;

    @Column(name = "Atribute4", length = 250)
    private String atribute4;

    @Column(name = "Atribute5", length = 250)
    private String atribute5;

    @Basic(optional = false)
    @Column(name = "ActiveStatus", nullable = false)
    private Integer activeStatus = EntityStatus.ACTIVE.getValue();

    @Basic(optional = false)
    @Column(name = "Inputter", nullable = false, length = 30)
    private String inputter;

    @Basic(optional = false)
    @Column(name = "CreateDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Authoriser", length = 30)
    private String authoriser;

    @Column(name = "AuthoriseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authoriseDate;

    @Version
    @Column(name = "Version", nullable = false)
    private int version;

    @JoinColumn(name = "ContactID", referencedColumnName = "ContactID", nullable = false)
    @OneToOne(optional = false)
    private CmContact contact;

    @JoinColumn(name = "Branch", referencedColumnName = "BranchCode", nullable = false)
    @ManyToOne(optional = false)
    private AcBranch branch;

    public CmCustomer() {
    }

    public CmCustomer(CmContact contact, Integer activeStatus, String inputter, Date createDate) {
        this.contact = contact;
        this.activeStatus = activeStatus;
        this.inputter = inputter;
        this.createDate = createDate;
    }

    public Object getID() {
        return customerID;
    }

    public void setID(Object customerID) {
        this.customerID = (Integer) customerID;
    }

    public String getRepName() {
        return repName;
    }

    public void setRepName(String repName) {
        this.repName = repName;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRepHomeStreet() {
        return repHomeStreet;
    }

    public void setRepHomeStreet(String repHomeStreet) {
        this.repHomeStreet = repHomeStreet;
    }

    public String getRepDistrict() {
        return repDistrict;
    }

    public void setRepDistrict(String repDistrict) {
        this.repDistrict = repDistrict;
    }

    public Integer getRepProvince() {
        return repProvince;
    }

    public void setRepProvince(Integer repProvince) {
        this.repProvince = repProvince;
    }

    public Integer getRepCountry() {
        return repCountry;
    }

    public void setRepCountry(Integer repCountry) {
        this.repCountry = repCountry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAtribute1() {
        return atribute1;
    }

    public void setAtribute1(String atribute1) {
        this.atribute1 = atribute1;
    }

    public String getAtribute2() {
        return atribute2;
    }

    public void setAtribute2(String atribute2) {
        this.atribute2 = atribute2;
    }

    public String getAtribute3() {
        return atribute3;
    }

    public void setAtribute3(String atribute3) {
        this.atribute3 = atribute3;
    }

    public String getAtribute4() {
        return atribute4;
    }

    public void setAtribute4(String atribute4) {
        this.atribute4 = atribute4;
    }

    public String getAtribute5() {
        return atribute5;
    }

    public void setAtribute5(String atribute5) {
        this.atribute5 = atribute5;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getInputter() {
        return inputter;
    }

    public void setInputter(String inputter) {
        this.inputter = inputter;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthoriser() {
        return authoriser;
    }

    public void setAuthoriser(String authoriser) {
        this.authoriser = authoriser;
    }

    public Date getAuthoriseDate() {
        return authoriseDate;
    }

    public void setAuthoriseDate(Date authoriseDate) {
        this.authoriseDate = authoriseDate;
    }

    public int getVersion() {
        return version;
    }

    public CmContact getContact() {
        return contact;
    }

    public void setContact(CmContact contact) {
        this.contact = contact;
    }

    public AcBranch getBranch() {
        return branch;
    }

    public void setBranch(AcBranch branch) {
        this.branch = branch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerID != null ? customerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmCustomer)) {
            return false;
        }
        CmCustomer other = (CmCustomer) object;
        if ((this.customerID == null && other.customerID != null) || (this.customerID != null && !this.customerID.equals(other.customerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fisoft.phucsinh.phucsinhsrv.entity.CmCustomer[customerID=" + customerID + "]";
    }
}
