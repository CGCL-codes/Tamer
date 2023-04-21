package org.jmetis.service.core.assertion;

/**
 * {@code IAssertHandler}
 * 
 * @author aerlach
 */
public interface IAssertHandler {

    /**
	 * Asserts that the given {@code value} is not {@code null}. If it is
	 * {@code null} an {@link NullPointerException} is thrown with the given
	 * {@code name}.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
    <T> T mustNotBeNull(String name, T value) throws NullPointerException;

    /**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
    String mustNotBeNullOrEmpty(String name, String value) throws NullPointerException, IllegalArgumentException;
}
