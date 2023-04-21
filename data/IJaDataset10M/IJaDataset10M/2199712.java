package org.eaasyst.eaa.syst.data;

import java.io.Serializable;

/**
 * <p>This interface specifies the required methods for all for all
 * entity beans in the Eaasy Street framework.</p>
 *
 * @version 2.2
 * @author Jeff Chilton
 */
public interface DataBean extends Serializable {

    /**
	 * <p>Returns a String representation of this entity bean.</p>
	 * 
	 * @return a String representation of this entity bean
	 * @since Eaasy Street 2.2
	 */
    public String toString();

    /**
	 * <p>Returns true if this entity bean is eaual to the bean
	 * passed.</p>
	 * 
	 * @param bean the to which this bean should be compared
	 * @return true if this entity bean is eaual to the bean passed
	 * @since Eaasy Street 2.2
	 */
    public boolean equals(DataBean bean);

    /**
	 * <p>Returns a HashCode representation of this entity bean.</p>
	 * 
	 * @return a HashCode representation of this entity bean
	 * @since Eaasy Street 2.2
	 */
    public int hashCode();

    /**
	 * <p>Returns the result of a comparison between this bean and
	 * the bean passed. Both beans must be of the same class.</p>
	 * 
	 * @param bean the to which this bean should be compared
	 * @return the result of a comparison between this bean and
	 * the bean passed
	 * @since Eaasy Street 2.2
	 */
    public int compareTo(DataBean bean);

    /**
	 * <p>Returns a String containing the comma-separated values of
	 * this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated values of
	 * this entity bean
	 * @since Eaasy Street 2.2
	 */
    public String toCsv();

    /**
	 * <p>Returns a String containing the comma-separated labels 
	 * corresponding to the values of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated labels 
	 * corresponding to the values of this entity bean
	 * @since Eaasy Street 2.2
	 */
    public String getCsvHeadings();
}
