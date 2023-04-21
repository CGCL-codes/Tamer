package modules.base.res;

import org.mga.common.ColumFactory;
import org.mga.common.PropertyData;
import org.mga.common.SQLObject;
import org.mga.common.SQLObjectData;
import org.mga.common.fields.Char;
import org.mga.common.fields.Float;
import org.mga.common.fields.One2Many;

@SuppressWarnings("serial")
public class Currency extends SQLObject {

    private Char code = (Char) ColumFactory.createColumn("Char");

    private Char name = (Char) ColumFactory.createColumn("Char");

    private Float currentRate = (Float) ColumFactory.createColumn("Float");

    private One2Many rateList = (One2Many) ColumFactory.createColumn("One2Many");

    /**
	 *
	 */
    public Currency() {
    }

    /**
	 * @return the code
	 */
    public Char getCode() {
        return code;
    }

    /**
	 * @param code the code to set
	 */
    public void setCode(Char code) {
        this.code = code;
    }

    /**
	 * @return the currentRate
	 */
    public Float getCurrentRate() {
        return currentRate;
    }

    /**
	 * @param currentRate the currentRate to set
	 */
    public void setCurrentRate(Float currentRate) {
        this.currentRate = currentRate;
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
	 * @return the rateList
	 */
    public One2Many getRateList() {
        return rateList;
    }

    /**
	 * @param rateList the rateList to set
	 */
    public void setRateList(One2Many rateList) {
        this.rateList = rateList;
    }

    public int afterCreate(SQLObjectData sqlData) {
        return 0;
    }

    public boolean afterDelete(SQLObjectData sqlData) {
        return false;
    }

    public boolean afterUpdate(SQLObjectData sqlData) {
        return false;
    }

    public boolean beforeCreate(SQLObjectData sqlData) {
        return false;
    }

    public boolean beforeDelete(SQLObjectData sqlData) {
        return false;
    }

    public boolean beforeUpdate(SQLObjectData sqlData) {
        return false;
    }

    public void propertyChanged(PropertyData data) {
    }

    public boolean afterSearch(SQLObjectData sqlData) {
        return false;
    }

    public boolean beforeSearch(SQLObjectData sqlData) {
        return false;
    }
}
