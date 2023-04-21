package dk.pervasive.jcaf;

import java.util.Enumeration;

/**
 * Defines a set of methods that a entity uses to communicate with its
 * ContextService container, for example, to get a RMI handle to other
 * processes, get Context Translators, dispatch requests, or write to a log
 * file.
 * 
 * <p>
 * There is one environment per ContextService per Java Virtual Machine.
 * 
 * <p>
 * In the case of a distributed, Peer-to-Peer setup of Context Services, there
 * will be one environment instance for each virtual machine. In this situation,
 * the environment cannot be used as a location to share global information
 * (because the information won't be truly global). Use an external resource
 * like a database instead.
 * 
 * @author <a href="mailto:bardram@daimi.au.dk">Jakob Bardram </a>
 *  
 */
public interface EntityEnvironment {

    /**
     * Removes the attribute with the given name from the environment.
     * 
     * @param name
     */
    public void removeAttribute(String name);

    /**
     * Sets the attribute with the given name from the environment.
     * 
     * @param name
     * @param object
     */
    public void setAttribute(String name, Object object);

    /**
     * Returns the container attribute with the given name, or null if there is
     * no attribute by that name.
     * 
     * @param name
     * @return Object
     */
    public Object getAttribute(String name);

    /**
     * Returns an Enumeration containing the attribute names available within
     * this environment.
     * 
     * @return Enumeration
     */
    public Enumeration getAttributeNames();

    /**
     * Returns a String containing the value of the named context-wide
     * initialization parameter, or null if the parameter does not exist.
     * 
     * @param name
     * @return String
     */
    public String getInitParameter(String name);

    /**
     * Returns the names of the environment's initialization parameters as an
     * Enumeration of String objects, or an empty Enumeration if the context has
     * no initialization parameters.
     * 
     * @return Enumeration
     */
    public Enumeration getInitParameterNames();

    /**
     * Returns a handle to the Translator Repository, containing all available
     * Context Translators
     * 
     * @see dk.pervasive.jcaf.ContextTranslator
     * 
     * @return TranslatorRepository
     */
    public TransformerRepository getTransformerRepository();

    /**
     * Returns a handle to the Context Service for this Entity Environment.
     * 
     * @return ContextService
     */
    public ContextService getContextService();

    /**
     * Returns the name and version of the container on which the entity is
     * running.
     * 
     * @return String
     */
    public String getServerInfo();

    /**
     * Writes the specified message to a log file, usually an event log.
     * 
     * @param msg
     */
    public void log(String msg);

    /**
     * Writes an explanatory message and a stack trace for a given Throwable
     * exception to the log file.
     * 
     * @param message
     * @param throwable
     */
    public void log(String message, Throwable throwable);
}
