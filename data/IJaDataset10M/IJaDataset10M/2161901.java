package test.hibernate;

public interface Updateable {

    /**
	 * @hibernate.property
	 *  column="LAST_UPDATED_BY"
	 */
    public String getUpdateComment();
}
