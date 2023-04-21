package com.eaio.nativecall;

/**
 * A Verifier implements method and module name checking for one given
 * operating system. Classes implementing Verifier must be public and have
 * a public no-argument constructor.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: Verifier.java,v 1.1 2006/01/05 20:02:44 grnull Exp $
 */
public interface Verifier {

    /**
     * If there is a default module that system functions are stored in, the
     * module's name may be returned here.
     * 
     * @return the name of a default module or <code>null</code>
     * @see NativeCall#NativeCall(String)
     */
    String getDefaultModule();

    /**
     * Returns if this Verifier supports the given operating system.
     * 
     * @return if this operating system is supported
     * @throws SecurityException because {@link java.lang.System} properties
     * may be queried
     */
    boolean supports() throws SecurityException;

    /**
     * Verifies that the given module name is correct.
     * 
     * @param module the module name, may be <code>null</code>
     * @return a module name, possibly modified, never <code>null</code>
     * @throws NullPointerException if the module name is <code>null</code>
     * and there is no default module defined
     * @throws IllegalArgumentException if the module is illegal in the
     * operating system
     * @see #getDefaultModule()
     */
    String verifyModuleName(String module) throws NullPointerException, IllegalArgumentException;

    /**
     * Verifies that the given function name is correct.
     * 
     * @param function the function name, may be <code>null</code>
     * @return a function name, possibly modified, never <code>null</code>
     * @throws NullPointerException if the function name is <code>null</code>
     * @throws IllegalArgumentException if the function is illegal in the
     * operating system
     */
    String verifyFunctionName(String function) throws NullPointerException, IllegalArgumentException;

    /**
     * Converts the given String to one of the following data types, based on the
     * module and the function name:
     * <br>
     * <ul>
     * <li>a <code>byte</code> array</li>
     * <li>a <code>char</code> array</li>
     * </ul>
     * 
     * @param val the String, never <code>null</code>
     * @param module the module name, never <code>null</code>
     * @param function the function name, never <code>null</code>
     * @return the String converted, never <code>null</code>
     */
    Object handleString(String val, String module, String function);
}
