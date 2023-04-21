package modules.base.res;

import org.mga.common.PropertyData;
import org.mga.common.SQLObject;
import org.mga.common.SQLObjectData;
import org.mga.common.fields.Char;
import org.mga.common.fields.Many2One;
import org.mga.common.fields.One2Many;

@SuppressWarnings("serial")
public class Address extends SQLObject {

    private Many2One partner_id = new Many2One("Partner", 0, true, "modules.base.res.Partner");

    private Char name = new Char("Name", 64, false);

    private Char street = new Char("Stree2", 64, false);

    private Char street2 = new Char("Street2", 64, false);

    private Char zip = new Char("Pin Code", 64, false);

    private Char city = new Char("City", 64, false);

    private Many2One state = new Many2One("State", 0, true, "modules.base.res.State");

    private Many2One country = new Many2One("Country", 0, true, "modules.base.res.Country");

    private Char mobile = new Char("Mobile", 64, false);

    private Char phone = new Char("Phone", 64, false);

    private Char fax = new Char("Fax", 64, false);

    /**
	 *
	 */
    public Address() {
    }

    /**
	 * @return the city
	 */
    public Char getCity() {
        return city;
    }

    /**
	 * @param city the city to set
	 */
    public void setCity(Char city) {
        this.city = city;
    }

    /**
	 * @return the country
	 */
    public Many2One getCountry() {
        return country;
    }

    /**
	 * @param country the country to set
	 */
    public void setCountry(Many2One country) {
        this.country = country;
    }

    /**
	 * @return the fax
	 */
    public Char getFax() {
        return fax;
    }

    /**
	 * @param fax the fax to set
	 */
    public void setFax(Char fax) {
        this.fax = fax;
    }

    /**
	 * @return the mobile
	 */
    public Char getMobile() {
        return mobile;
    }

    /**
	 * @param mobile the mobile to set
	 */
    public void setMobile(Char mobile) {
        this.mobile = mobile;
    }

    /**
	 * @return the name
	 */
    public Char getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(Char name) {
        this.name = name;
    }

    /**
	 * @return the partner_id
	 */
    public Many2One getPartner_id() {
        return partner_id;
    }

    /**
	 * @param partner_id the partner_id to set
	 */
    public void setPartner_id(Many2One partner_id) {
        this.partner_id = partner_id;
    }

    /**
	 * @return the phone
	 */
    public Char getPhone() {
        return phone;
    }

    /**
	 * @param phone the phone to set
	 */
    public void setPhone(Char phone) {
        this.phone = phone;
    }

    /**
	 * @return the state
	 */
    public Many2One getState() {
        return state;
    }

    /**
	 * @param state the state to set
	 */
    public void setState(Many2One state) {
        this.state = state;
    }

    /**
	 * @return the street
	 */
    public Char getStreet() {
        return street;
    }

    /**
	 * @param street the street to set
	 */
    public void setStreet(Char street) {
        this.street = street;
    }

    /**
	 * @return the street2
	 */
    public Char getStreet2() {
        return street2;
    }

    /**
	 * @param street2 the street2 to set
	 */
    public void setStreet2(Char street2) {
        this.street2 = street2;
    }

    /**
	 * @return the zip
	 */
    public Char getZip() {
        return zip;
    }

    /**
	 * @param zip the zip to set
	 */
    public void setZip(Char zip) {
        this.zip = zip;
    }

    public int afterCreate(SQLObjectData sqlData) {
        return 0;
    }

    public boolean afterDelete(SQLObjectData sqlData) {
        return true;
    }

    public boolean afterUpdate(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeCreate(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeDelete(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeUpdate(SQLObjectData sqlData) {
        return true;
    }

    public void propertyChanged(PropertyData data) {
    }

    public boolean afterSearch(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeSearch(SQLObjectData sqlData) {
        return true;
    }
}
