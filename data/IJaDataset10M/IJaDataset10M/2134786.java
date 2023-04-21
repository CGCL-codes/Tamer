package org.guestshome.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.guestshome.commons.database.LogFields;
import org.guestshome.commons.database.LogicalDelete;
import org.sqlutils.ValueObject;

@Entity
@Table(name = "GUEST_CONTRIBUTIONS")
public class GuestContribution implements Serializable, ValueObject, LogFields, LogicalDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CONTRIBUTION_DATE")
    private Date contributionDate;

    private BigDecimal amount;

    private String reason;

    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "ROW_VERSION")
    @Version
    private long rowVersion;

    private String deleted;

    @Column(name = "CONTRIBUTOR_ID")
    private int contributorId;

    @Column(name = "PERSON_ID")
    private int personId;

    @ManyToOne
    @JoinColumn(name = "CONTRIBUTOR_ID", insertable = false, updatable = false)
    private Contributor contributor;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID", insertable = false, updatable = false)
    private Person person;

    private static final long serialVersionUID = 1L;

    public GuestContribution() {
        super();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getContributionDate() {
        return this.contributionDate;
    }

    public void setContributionDate(Date contributionDate) {
        this.contributionDate = contributionDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public long getRowVersion() {
        return this.rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public String getDeleted() {
        return this.deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Contributor getContributor() {
        return this.contributor;
    }

    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getContributorId() {
        return contributorId;
    }

    public void setContributorId(int contributorId) {
        this.contributorId = contributorId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof GuestContribution && ((GuestContribution) obj).getId() == getId();
    }
}
