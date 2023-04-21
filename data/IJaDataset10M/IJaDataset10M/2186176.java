package org.havi.ui;

/**
 * Thrown when an application attempts to control a device or feature it does
 * not have the right to do at the time of making the call. The precise
 * conditions of this are defined in more detail in the places where this
 * exception is thrown. It could be when the application does not have some
 * underlying resource reserved.
 * <p>
 * This exception is not related in any way to the Java security model or to
 * <code>java.lang.SecurityException</code>. Methods where an application
 * could be denied the right to control a device or feature by security policy
 * also must throw a <code>java.lang.SecurityException</code>.
 * <hr>
 * The parameters to the constructors are as follows, in cases where parameters
 * are not used, then the constructor should use the default values.
 * <p>
 * <h3>Default parameter values exposed in the constructors</h3>
 * <table border>
 * <tr>
 * <th>Parameter</th>
 * <th>Description</th>
 * <th>Default value</th>
 * <th>Set method</th>
 * <th>Get method</th>
 * </tr>
 * <tr>
 * <td>message</td>
 * <td>Message explaining why the exception was thrown</td>
 * <td>null</td>
 * <td>-</td>
 * <td>java.lang.Throwable#getMessage</td>
 * </tr>
 * </table>
 * <h3>Default parameter values not exposed in the constructors</h3>
 * <table border>
 * <tr>
 * <th>Description</th>
 * <th>Default value</th>
 * <th>Set method</th>
 * <th>Get method</th>
 * </tr>
 * <tr>
 * <td colspan=4>None.</td>
 * </tr>
 * </table>
 */
public class HPermissionDeniedException extends java.lang.Exception {

    /**
	 * Creates an HPermissionDeniedException object. See the class description
	 * for details of constructor parameters and default values.
	 */
    public HPermissionDeniedException() {
    }

    /**
	 * Creates an HPermissionDeniedException object with a specified reason
	 * string.
	 * @param message the reason why the exception was raised
	 */
    public HPermissionDeniedException(String message) {
    }
}
