package de.fmannan.addbook.domainmodel.contactdata;

/**
 * @author fmannan
 * 
 * Contains specific, personal-related data such as birthdate and name.
 * 
 */
public class PrivateContactData extends AbstractContactData {

    private String birthdate;

    private String remark;

    public PrivateContactData() {
        super();
        this.birthdate = "";
        this.remark = "";
    }

    /**
	 * @return a Date representing the birthdate of the person
	 */
    public String getBirthdate() {
        return birthdate;
    }

    /**
	 * 
	 * @param birthdate
	 *            set the birthdate of the person
	 */
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /**
	 * 
	 * @return a remark
	 */
    public String getRemark() {
        return remark;
    }

    /**
	 * 
	 * @param remark set a new remark
	 */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
