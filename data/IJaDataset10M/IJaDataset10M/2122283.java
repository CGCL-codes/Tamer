package jparse;

/**
 * Information on a Java method.
 *
 * @version $Revision: 1.1.1.1 $, $Date: 2006/10/22 16:11:42 $
 * @author Jerry James
 */
public interface Method extends HasExceptions {

    /**
     * Returns the <code>Type</code> object representing the class or
     * interface that declares the method represented by this object.
     *
     * @return the <code>Type</code> of the declaring class
     */
    public Type getDeclaringClass();

    /**
     * Return the name of this method
     *
     * @return the name of this method
     */
    public String getName();

    /**
     * Returns the Java language modifiers for the method represented by this
     * object, as an integer.  The {@link java.lang.reflect.Modifier Modifier}
     * class should be used to decode the modifiers.
     *
     * @return the modifiers for this method
     */
    public int getModifiers();

    /**
     * Returns a <code>Type</code> object that represents the formal return
     * type of this method.
     *
     * @return the return type of this method
     */
    public Type getReturnType();

    /**
     * Returns an array of <code>Type</code> objects that represent the formal
     * parameter types, in declaration order, of this method.  Returns an
     * array of length 0 if the underlying method takes no parameters.
     *
     * @return the parameter types of this method
     */
    public Type[] getParameterTypes();

    /**
     * Returns an array of <code>Type</code> objects that represent the types
     * of the exceptions declared to be thrown by this method.  Returns an
     * array of length 0 if the method declares no exceptions in its
     * <code>throws</code> clause.
     *
     * @return the exceptions declared by this method
     */
    public Type[] getExceptionTypes();

    /**
     * Determines whether the method is accessible to a given caller
     *
     * @param caller the type of the caller
     * @return <code>true</code> if the caller is able to access this method,
     * <code>false</code> otherwise
     */
    public boolean isAccessible(Type caller);

    /**
     * Determines whether this method matches the parameters given by a caller
     *
     * @param name the name of the method to match
     * @param params the types of the parameters to the method
     * @param caller the type of the caller
     * @return <code>true</code> if this method matches, <code>false</code>
     * otherwise
     */
    public boolean match(String name, Type[] params, Type caller);

    /**
     * Determines whether this method matches the parameters given by a caller
     *
     * @param params the types of the parameters to the method
     * @param caller the type of the caller
     * @return <code>true</code> if this method matches, <code>false</code>
     * otherwise
     */
    public boolean match(Type[] params, Type caller);

    /**
     * Find the best match, given two matching methods
     *
     * @param meth the other method to compare
     * @return either <var>this</var> or <var>meth</var>, depending on which
     * matches best, or <code>null</code> if neither matches best
     */
    public Method bestMatch(Method meth);

    /**
     * Determine whether two methods are exact matches: i.e., whether the
     * names are the same, they take the same number of parameters, and all
     * the parameter types are exactly equal.
     *
     * @param meth the method to compare against
     * @return <code>true</code> if the methods match exactly;
     * <code>false</code> if they differ in any particular
     */
    public boolean exactMatch(Method meth);
}
