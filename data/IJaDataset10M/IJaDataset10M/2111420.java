package org.ikayzo.iset.yautt.model;

/**
 * 
 * @author hanning ni
 * @reviewer (please review me)
 */
public interface ValidatorPolicy {

    public void setType(String type);

    public String getType();

    /**
	 * 
	 * @param aline a record in event record file
	 * @return
	 */
    public void validate(ValidationContext context, String aline) throws ValidationException;
}
