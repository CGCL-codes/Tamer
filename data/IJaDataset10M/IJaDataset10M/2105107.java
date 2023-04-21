package modules.base.res;

import java.sql.Connection;
import org.mga.common.ColumFactory;
import org.mga.common.PropertyData;
import org.mga.common.SQLObject;
import org.mga.common.SQLObjectData;
import org.mga.common.fields.Boolean;
import org.mga.common.fields.Char;
import org.mga.common.fields.Many2Many;
import org.mga.common.fields.One2Many;
import org.mga.core.ConnectionManager;

@SuppressWarnings("serial")
public class Partner extends SQLObject {

    private Char name = (Char) ColumFactory.createColumn("Char");

    private Char email = (Char) ColumFactory.createColumn("Char");

    private Char age = (Char) ColumFactory.createColumn("Char");

    private Char vat = (Char) ColumFactory.createColumn("Char");

    private Char url = (Char) ColumFactory.createColumn("Char");

    private Boolean active = (Boolean) ColumFactory.createColumn("Boolean");

    private One2Many address = (One2Many) ColumFactory.createColumn("One2Many");

    private Many2Many categoryIds = (Many2Many) ColumFactory.createColumn("Many2Many");

    public Partner() {
        super();
        name.setLabel("Name");
        name.setSize(64);
        email.setLabel("Email");
        email.setSize(256);
        age.setLabel("Age");
        vat.setLabel("Vat");
        vat.setSize(256);
        url.setLabel("Website");
        url.setSize(265);
        address.setRelation("modules.base.res.Address");
        address.setLabel("Address");
        categoryIds.setRelation("modules.base.res.Category");
        categoryIds.setCol1("partner_id");
        categoryIds.setCol2("category_id");
        categoryIds.setTable("res_partner_category");
        categoryIds.setLabel("Categories");
        active.setLabel("Active");
        active.setName("active");
        active.setDefaultValue(true);
    }

    /**
	 * @return the active
	 */
    public Boolean getActive() {
        return active;
    }

    /**
	 * @param active the active to set
	 */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
	 * @return the url
	 */
    public Char getUrl() {
        return url;
    }

    /**
	 * @param url the url to set
	 */
    public void setUrl(Char url) {
        this.url = url;
    }

    /**
	 * @return the vat
	 */
    public Char getVat() {
        return vat;
    }

    /**
	 * @param vat the vat to set
	 */
    public void setVat(Char vat) {
        this.vat = vat;
    }

    /**
	 * @return the email
	 */
    public Char getEmail() {
        return email;
    }

    /**
	 * @param email the email to set
	 */
    public void setEmail(Char email) {
        this.email = email;
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
	 * @return the age
	 */
    public Char getAge() {
        return age;
    }

    /**
	 * @param age the age to set
	 */
    public void setAge(Char age) {
        this.age = age;
    }

    /**
	 * @return the address
	 */
    public One2Many getAddress() {
        return address;
    }

    /**
	 * @param address the address to set
	 */
    public void setAddress(One2Many address) {
        this.address = address;
    }

    /**
	 * @return the category_ids
	 */
    public Many2Many getCategoryIds() {
        return categoryIds;
    }

    /**
	 * @param category_ids the category_ids to set
	 */
    public void setCategoryIds(Many2Many category_ids) {
        this.categoryIds = category_ids;
    }

    public boolean afterDelete(SQLObjectData sqlData) {
        return true;
    }

    public boolean afterUpdate(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeDelete(SQLObjectData sqlData) {
        return true;
    }

    public boolean beforeUpdate(SQLObjectData sqlData) {
        return true;
    }

    public int afterCreate(SQLObjectData sqlData) {
        Connection cn = ConnectionManager.getConnection();
        return 0;
    }

    public boolean beforeCreate(SQLObjectData sqlData) {
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
