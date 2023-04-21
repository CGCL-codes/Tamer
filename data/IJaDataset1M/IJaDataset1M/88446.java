package org.jraptor.core.bean;

/**
 * Abstract base class for domain classes with a requirement to implement equals(), hashCode() and toString(). Provides
 * a generic implementation of these methods based on business key equality.
 * <p>
 * This class delegates all implementation details to the {@link org.jraptor.core.bean.BeanUtils BeanUtils} class.
 * 
 * @author <a href="mailto:goran.oberg@jraptor.org">Goran Oberg</a>
 * @version $Rev: 143 $ $Date: 2008-12-31 08:02:29 -0500 (Wed, 31 Dec 2008) $
 * @see org.jraptor.core.bean.BeanUtils
 * @see org.jraptor.core.bean.BusinessKey
 * @see org.jraptor.core.bean.MethodType
 */
public abstract class AbstractBean {

    /**
	 * Indicates whether some other object is equal to this one based on a business key.
	 * 
	 * @param obj the reference object with which to compare
	 * @return true if this object is the same as the obj argument; false otherwise
	 */
    @Override
    public boolean equals(Object obj) {
        return BeanUtils.equals(this, obj, super.equals(obj));
    }

    /**
	 * Returns a hash code value for this object based on a business key.
	 * 
	 * @return a hash code value for this object
	 */
    @Override
    public int hashCode() {
        return BeanUtils.hashCode(this, super.hashCode());
    }

    /**
	 * Returns a string representation of this object based on a business key, for example:
	 * 
	 * <pre>
	 * User[getAge=28,getId=12,getName=Joe]
	 * </pre>
	 * 
	 * <b>Note:</b> This method returns a string that textually represents this object and is therefore useful for debug
	 * and logging purposes.
	 * 
	 * @return a string representation for this object
	 */
    @Override
    public String toString() {
        return BeanUtils.toString(this, super.toString());
    }
}
