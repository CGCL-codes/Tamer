package net.taylor.validator;

/**
 * The interface for defining custom assertions.
 * 
 * @author jgilbert
 * 
 */
public interface IAssertion {

    /**
	 * @param value
	 * @return
	 */
    boolean isValid(Object value);
}
