package de.srcml.dom.supports;

import java.util.List;
import de.srcml.dom.Method;
import de.srcml.dom.Parameters;

/**
 * This interface is shared by all SrcMLElements supporting methods and
 * provides means to query, add and remove methods.
 *
 * @author Frank Raiser
 * @version $Revision: 1.1 $
 */
public interface ISupportsMethods {

    /**
     * Adds a pre-created method to this element.
     * Note that the given Method should not be attached to any other tree
     * already.
     *
     * @param f_method the Method to add
     * @return true iff the Method was successfully added
     */
    public boolean addMethod(Method f_method);

    /**
     * Adds a new method with the given name taking no parameters.
     *
     * @param f_name name of the new method
     * @return the created Method if successfully added, null otherwise
     */
    public Method addMethod(String f_name);

    /**
     * Adds a new method with the given signature.
     *
     * @param f_name name of the method
     * @param f_params parameters the method takes
     * @return the created Method if successfully added, null otherwise
     */
    public Method addMethod(String f_name, Parameters f_params);

    /**
     * Removes all methods from this element.
     */
    public void clearMethods();

    /**
     * Returns a list of all declared methods.
     *
     * @return List of Methods declared in this element.
     */
    public List<Method> getMethods();

    /**
     * Returns a list of all methods with the given name.
     *
     * @param f_name name of the method(s)
     * @return List of Methods with the given name.
     */
    public List<Method> getMethods(String f_name);

    /**
     * Returns a method which matches the given signature.
     * In a proper document there shouldn't be multiple methods with the
     * same signature, so this method is just expected to return the
     * first matching method.
     *
     * @param f_name Name of the method
     * @param f_params Parameters the method takes (names don't matter - only types)
     * @return the requested Method or null if no matching method was found
     */
    public Method getMethod(String f_name, Parameters f_params);

    /**
     * Removes a given method from this element.
     *
     * @param f_method the Method to remove
     * @return true iff the method was successfully removed
     */
    public boolean removeMethod(Method f_method);

    /**
     * Removes all given methods from this element.
     *
     * @param f_methods the Methods to remove
     */
    public void removeMethods(List<? extends Method> f_methods);
}
