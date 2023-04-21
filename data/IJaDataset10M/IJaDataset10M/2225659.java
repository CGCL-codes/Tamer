package wsl.mdn.dataview;

import wsl.fw.datasource.DataObject;
import wsl.fw.datasource.DefaultKeyGeneratorData;
import wsl.fw.datasource.Entity;
import wsl.fw.datasource.EntityImpl;
import wsl.fw.datasource.Field;
import wsl.fw.datasource.FieldImpl;

public class JdbcDriver extends DataObject {

    public static final String ENT_JDBCDRIVER = "TBL_JDBCDRIVER", FLD_ID = "FLD_ID", FLD_DRIVER = "FLD_DRIVER", FLD_NAME = "FLD_NAME", FLD_URL_FORMAT = "FLD_URL_FORMAT", FLD_DESCRIPTION = "FLD_DESCRIPTION", FLD_FILE_NAME = "FLD_FILE_NAME", FLD_DEL_STATUS = "FLD_DEL_STATUS";

    /**
	 * Blank ctor
	 */
    public JdbcDriver() {
    }

    /**
	 *  Constructor useful for seeding d/b
	 */
    public JdbcDriver(String name, String driver, String desc, String fileName) throws IllegalArgumentException {
        setName(name);
        setDriver(driver);
        setDescription(desc);
        setFileName(fileName);
    }

    /**
	 * Static factory method to create the entity to be used by this dataobject
	 * and any subclasses. This is called by the DataManager's factory when
	 * creating a JDBCDRIVER entity.
	 * @return the created entity.
	 */
    public static Entity createEntity() {
        Entity ent = new EntityImpl(ENT_JDBCDRIVER, JdbcDriver.class);
        ent.addKeyGeneratorData(new DefaultKeyGeneratorData(ENT_JDBCDRIVER, FLD_ID));
        ent.addField(new FieldImpl(FLD_ID, Field.FT_INTEGER, Field.FF_SYSTEM_KEY));
        ent.addField(new FieldImpl(FLD_NAME, Field.FT_STRING, Field.FF_UNIQUE_KEY | Field.FF_NAMING));
        ent.addField(new FieldImpl(FLD_DRIVER, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_URL_FORMAT, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_DESCRIPTION, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_FILE_NAME, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_DEL_STATUS, Field.FT_INTEGER));
        return ent;
    }

    /**
	 * @return String the name of the entity that defines this DataObject
	 */
    public String getEntityName() {
        return ENT_JDBCDRIVER;
    }

    /**
	 * @return int the id
	 */
    public int getId() {
        return getIntValue(FLD_ID);
    }

    /**
	 * Sets the id
	 * @param id the id to set
	 */
    public void setId(int id) {
        setValue(FLD_ID, id);
    }

    /**
	 * Returns the name
	 * @return String the name
	 */
    public String getName() {
        return getStringValue(FLD_NAME);
    }

    /**
	 * Sets the name
	 * @param val the value to set
	 */
    public void setName(String val) {
        setValue(FLD_NAME, val);
    }

    /**
	 * Returns the driver name
	 * @return String the name
	 */
    public String getDriver() {
        return getStringValue(FLD_DRIVER);
    }

    /**
	 * Sets the driver name
	 * @param val the value to set
	 */
    public void setDriver(String val) {
        setValue(FLD_DRIVER, val);
    }

    /**
	 * Returns the description of the datatransfer
	 * @return String
	 */
    public String getDescription() {
        return getStringValue(FLD_DESCRIPTION);
    }

    /**
	 * Sets the entity description into the datatransfer
	 * @param name
	 * @return void
	 */
    public void setDescription(String desc) {
        setValue(FLD_DESCRIPTION, desc);
    }

    public String getUrlFormat() {
        return getStringValue(FLD_URL_FORMAT);
    }

    public void setUrlFormat(String urlFormat) {
        setValue(FLD_URL_FORMAT, urlFormat);
    }

    public String getFileName() {
        return getStringValue(FLD_FILE_NAME);
    }

    public void setFileName(String fileName) {
        setValue(FLD_FILE_NAME, fileName);
    }

    public int getDelStatus() {
        return getIntValue(FLD_DEL_STATUS);
    }

    public void setDelStatus(int delStatus) {
        setValue(FLD_DEL_STATUS, delStatus);
    }
}
