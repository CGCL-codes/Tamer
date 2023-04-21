package com.cosmos.acacia.crm.data.contacts;

import com.cosmos.acacia.annotation.Component;
import com.cosmos.acacia.annotation.Form;
import com.cosmos.acacia.annotation.FormComponentPair;
import java.io.Serializable;
import java.util.UUID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.cosmos.acacia.annotation.Property;
import com.cosmos.acacia.annotation.PropertyName;
import com.cosmos.acacia.annotation.PropertyValidator;
import com.cosmos.acacia.annotation.SelectableList;
import com.cosmos.acacia.annotation.ValidationType;
import com.cosmos.acacia.crm.bl.contacts.ContactsServiceRemote;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DataObjectBean;
import com.cosmos.acacia.crm.data.DbResource;
import org.hibernate.annotations.Type;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "passports")
@NamedQueries({ @NamedQuery(name = Passport.NQ_FIND_ALL, query = "select t from Passport t" + " where" + "  t.person = :person" + " order by t.passportType, t.passportNumber") })
@Form(formContainers = {  }, serviceClass = ContactsServiceRemote.class)
public class Passport extends DataObjectBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String CLASS_NAME = "Passport";

    public static final String NQ_FIND_ALL = CLASS_NAME + ".findAll";

    @Id
    @Column(name = "passport_id", nullable = false)
    @Type(type = "uuid")
    private UUID passportId;

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    @Property(title = "Person", customDisplay = "${person.displayName}", useEntityAttributes = false)
    private Person person;

    @JoinColumn(name = "passport_type_id", referencedColumnName = "resource_id")
    @ManyToOne
    @Property(title = "Passport Type", selectableList = @SelectableList(className = "com.cosmos.acacia.crm.enums.PassportType"), formComponentPair = @FormComponentPair(firstComponent = @Component(text = "Passport Type:")))
    private DbResource passportType;

    @Column(name = "passport_number", nullable = false)
    @Property(title = "Passport Number", propertyValidator = @PropertyValidator(validationType = ValidationType.LENGTH, maxLength = 16))
    private String passportNumber;

    @Column(name = "issue_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Property(title = "Issue Date")
    private Date issueDate;

    @Column(name = "expiration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Property(title = "Expiration Date")
    private Date expirationDate;

    @JoinColumn(name = "issuer_id", referencedColumnName = "organization_id", nullable = false)
    @ManyToOne
    @Property(title = "Issuer", selectableList = @SelectableList(constructorParameters = { @PropertyName(getter = "'PassportOffice'", setter = "classifier") }))
    private Organization issuer;

    @JoinColumn(name = "issuer_branch_id", referencedColumnName = "address_id", nullable = false)
    @ManyToOne
    @Property(title = "Issuer Branch", selectableList = @SelectableList(constructorParameters = { @PropertyName(getter = "issuer", setter = "businessPartner") }))
    private Address issuerBranch;

    @Column(name = "additional_info")
    @Property(title = "Additional Info", propertyValidator = @PropertyValidator(validationType = ValidationType.LENGTH, maxLength = 255), formComponentPair = @FormComponentPair(secondComponent = @Component(componentConstraints = "span")))
    private String additionalInfo;

    @JoinColumn(name = "passport_id", referencedColumnName = "data_object_id", insertable = false, updatable = false)
    @OneToOne
    private DataObject dataObject;

    public Passport() {
    }

    public Passport(UUID passportId) {
        this.passportId = passportId;
    }

    public UUID getPassportId() {
        return passportId;
    }

    public void setPassportId(UUID passportId) {
        this.passportId = passportId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        if (person != null) {
            setParentId(person.getBusinessPartnerId());
        } else {
            setParentId(null);
        }
    }

    @Override
    public UUID getParentId() {
        if (person != null) {
            return person.getParentId();
        }
        return null;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        firePropertyChange("issueDate", this.issueDate, issueDate);
        this.issueDate = issueDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        firePropertyChange("expirationDate", this.expirationDate, expirationDate);
        this.expirationDate = expirationDate;
    }

    public Organization getIssuer() {
        return issuer;
    }

    public void setIssuer(Organization issuer) {
        this.issuer = issuer;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Address getIssuerBranch() {
        return issuerBranch;
    }

    public void setIssuerBranch(Address issuerBranch) {
        this.issuerBranch = issuerBranch;
    }

    @Override
    public DataObject getDataObject() {
        return dataObject;
    }

    @Override
    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }

    public DbResource getPassportType() {
        return passportType;
    }

    public void setPassportType(DbResource passportType) {
        this.passportType = passportType;
    }

    @Override
    public UUID getId() {
        return getPassportId();
    }

    @Override
    public void setId(UUID id) {
        setPassportId(id);
    }

    @Override
    public String getInfo() {
        return getPassportNumber();
    }

    @Override
    public String toShortText() {
        return toText();
    }

    @Override
    public String toText() {
        StringBuilder sb = new StringBuilder();
        sb.append("No ").append(passportNumber);
        sb.append("/").append(passportType);
        return sb.toString();
    }
}
