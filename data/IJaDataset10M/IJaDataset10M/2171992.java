// $Id: NotSupportedMethodException.java,v 1.1.1.1 2005/10/22 06:55:50 vcrfix Exp $
package bestbooks3.controller;

importcontroller
 * NotSupportedMethodException provides a handler to be thrown when a
 * method in the framework is not implemented but the method signature
 * must remain for compiler compatibility reasons.
 * 
 * @author P. Ingle 
 * @version 3.0
 */
public class NotSupportedMethodException extends Exception
{
    public static final String rcsid = "$Id: NotSupportedMethodException.java,v 1.1.1.1 2005/10/22 06:55:50 vcrfix Exp $";

	/**
	 * Constructor for objects of class NotSupportedMethodException
	 */
	public NotSupportedMethodException()
	{
	}
	
	public String toString() 
	{
	    return "THIS METHOD IS NO LONGER SUPPORTED!";
	}
}
