package org.openuss.foundation;

/**
 * Named DomainObjects extends the DomainObject Interface by a name property.
 * 
 * @author Ingo Dueppe
 * 
 */
public interface NamedDomainObject extends DomainObject {

    /**
	 * Name of the domain object.
	 * 
	 * @return String
	 */
    public String getName();
}
