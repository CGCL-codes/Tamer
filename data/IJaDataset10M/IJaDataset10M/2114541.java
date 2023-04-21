package xconsole;

/**
 * A module defines a virtual scope of commands that, in most situation, fulfil
 * a same goal.
 * 
 * @author keke
 * @version $Revision: 13 $
 */
public interface XConsoleModule {

    /**
     * Each module must have a context.
     * 
     * @return context of this module.
     */
    XConsoleContext getContext();

    /**
     * Return the parent module
     * 
     * @return
     */
    XConsoleModule getParent();

    /**
     * Return the full name of this module
     * 
     * @return full name
     */
    String getName();

    /**
     * Return the short name of this module
     * 
     * @return short name
     */
    String getShortName();

    /**
     * Return description of this module
     * 
     * @return description
     */
    String getDescription();

    /**
     * 
     * @param command
     *            command line
     * @return
     * @throws ConsoleException 
     */
    XConsoleExecution exec(String command) throws ConsoleException;
}
