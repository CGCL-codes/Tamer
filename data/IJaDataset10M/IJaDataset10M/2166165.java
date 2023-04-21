package cw.customermanagementmodul.customer.persistence;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.Type;
import cw.boardingschoolmanagement.persistence.CWPersistence;

/**
 * Implementation of persistence 'Customer'
 * 
 * @author Manuel Geier
 **/
@Entity(name = Customer.ENTITY_NAME)
@Table(name = Customer.TABLE_NAME)
public class Customer extends CWPersistence {

    public static final String ENTITY_NAME = "customer";

    public static final String TABLE_NAME = "customer";

    public static final String PROPERTYNAME_ID = "id";

    public static final String PROPERTYNAME_TITLE = "title";

    public static final String PROPERTYNAME_FORENAME = "forename";

    public static final String PROPERTYNAME_SURNAME = "surname";

    public static final String PROPERTYNAME_GENDER = "gender";

    public static final String PROPERTYNAME_STREET = "street";

    public static final String PROPERTYNAME_CITY = "city";

    public static final String PROPERTYNAME_POSTOFFICENUMBER = "postOfficeNumber";

    public static final String PROPERTYNAME_PROVINCE = "province";

    public static final String PROPERTYNAME_COUNTRY = "country";

    public static final String PROPERTYNAME_BIRTHDAY = "birthday";

    public static final String PROPERTYNAME_LANDLINEPHONE = "landlinephone";

    public static final String PROPERTYNAME_MOBILEPHONE = "mobilephone";

    public static final String PROPERTYNAME_EMAIL = "email";

    public static final String PROPERTYNAME_FAX = "fax";

    public static final String PROPERTYNAME_CREATIONDATE = "creationDate";

    public static final String PROPERTYNAME_INACTIVEDATE = "inactiveDate";

    public static final String PROPERTYNAME_COMMENT = "comment";

    public static final String PROPERTYNAME_ACTIVE = "active";

    private Long id;

    private boolean active = true;

    private String title = "";

    private String forename = "";

    private String surname = "";

    private boolean gender = true;

    private String street = "";

    private String postOfficeNumber = "";

    private String city = "";

    private String province = "";

    private String country = "";

    private Date birthday;

    private String landlinephone = "";

    private String mobilephone = "";

    private String fax = "";

    private String email = "";

    private Date creationDate;

    private Date inactiveDate;

    private String comment = "";

    private Customer() {
        super(null);
    }

    Customer(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.id == null || ((Customer) obj).id == null) {
            return false;
        }
        if (this.id != ((Customer) obj).id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\nId: ");
        buf.append(id);
        buf.append("\nTitle: ");
        buf.append(title);
        buf.append("\nForename: ");
        buf.append(forename);
        buf.append("\nSurname: ");
        buf.append(surname);
        buf.append(id);
        buf.append(", ");
        buf.append(title);
        buf.append(", ");
        buf.append(forename);
        buf.append(", ");
        buf.append(surname);
        buf.append(", ");
        buf.append(street);
        buf.append(", ");
        buf.append(city);
        buf.append(", ");
        buf.append(province);
        buf.append(", ");
        buf.append(country);
        buf.append(", ");
        buf.append(mobilephone);
        buf.append(", ");
        buf.append(landlinephone);
        buf.append(", ");
        buf.append(gender);
        buf.append(", ");
        buf.append(email);
        buf.append(", ");
        buf.append(fax);
        buf.append(", ");
        buf.append(comment);
        buf.append(", ");
        buf.append(birthday);
        buf.append(", ");
        buf.append(creationDate);
        buf.append(", ");
        buf.append(inactiveDate);
        return buf.toString();
    }

    @Column(name = PROPERTYNAME_TITLE)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String old = this.title;
        this.title = title;
        firePropertyChange(Customer.PROPERTYNAME_TITLE, old, title);
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        String old = this.forename;
        this.forename = forename;
        firePropertyChange(Customer.PROPERTYNAME_FORENAME, old, forename);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        String old = this.surname;
        this.surname = surname;
        firePropertyChange(Customer.PROPERTYNAME_FORENAME, old, forename);
    }

    /**
     * Returns the gender
     * @return true -> man, false -> woman
     */
    public boolean getGender() {
        return gender;
    }

    /**
     * Sets the gender
     * @param gender true -> man, false -> woman
     */
    public void setGender(boolean gender) {
        boolean old = this.gender;
        this.gender = gender;
        firePropertyChange(Customer.PROPERTYNAME_GENDER, old, gender);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        String old = this.street;
        this.street = street;
        firePropertyChange(Customer.PROPERTYNAME_STREET, old, street);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        String old = this.city;
        this.city = city;
        firePropertyChange(Customer.PROPERTYNAME_CITY, old, city);
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        Date old = this.birthday;
        this.birthday = birthday;
        firePropertyChange(Customer.PROPERTYNAME_BIRTHDAY, old, birthday);
    }

    public String getLandlinephone() {
        return landlinephone;
    }

    public void setLandlinephone(String landlinephone) {
        String old = this.landlinephone;
        this.landlinephone = landlinephone;
        firePropertyChange(Customer.PROPERTYNAME_LANDLINEPHONE, old, landlinephone);
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        String old = this.mobilephone;
        this.mobilephone = mobilephone;
        firePropertyChange(Customer.PROPERTYNAME_MOBILEPHONE, old, mobilephone);
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        String old = this.fax;
        this.fax = fax;
        firePropertyChange(Customer.PROPERTYNAME_FAX, old, fax);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String old = this.email;
        this.email = email;
        firePropertyChange(Customer.PROPERTYNAME_EMAIL, old, email);
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        Date old = this.creationDate;
        this.creationDate = creationDate;
        firePropertyChange(Customer.PROPERTYNAME_CREATIONDATE, old, creationDate);
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(Date inactiveDate) {
        Date old = this.inactiveDate;
        this.inactiveDate = inactiveDate;
        firePropertyChange(Customer.PROPERTYNAME_INACTIVEDATE, old, inactiveDate);
    }

    @Type(type = "text")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        String old = this.comment;
        this.comment = comment;
        firePropertyChange(Customer.PROPERTYNAME_COMMENT, old, comment);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = this.active;
        this.active = active;
        firePropertyChange(Customer.PROPERTYNAME_ACTIVE, old, active);
        if (active) {
            setInactiveDate(null);
        } else {
            setInactiveDate(new Date());
        }
    }

    public String getPostOfficeNumber() {
        return postOfficeNumber;
    }

    public void setPostOfficeNumber(String postOfficeNumber) {
        String old = this.postOfficeNumber;
        this.postOfficeNumber = postOfficeNumber;
        firePropertyChange(Customer.PROPERTYNAME_POSTOFFICENUMBER, old, postOfficeNumber);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        String old = this.province;
        this.province = province;
        firePropertyChange(Customer.PROPERTYNAME_PROVINCE, old, province);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        String old = this.country;
        this.country = country;
        firePropertyChange(Customer.PROPERTYNAME_COUNTRY, old, country);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = PROPERTYNAME_ID)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long old = this.id;
        this.id = id;
        firePropertyChange(Customer.PROPERTYNAME_ID, old, id);
    }
}
