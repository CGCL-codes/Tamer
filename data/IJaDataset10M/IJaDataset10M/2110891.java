package nz.ac.massey.softwarec.group3.reverseAJAX;

import org.directwebremoting.ScriptSessionFilter;

/**
 * ReverseAJAXCallInterface - Interface for the ReverseAJAXCall class.
 * @version 1.0 Release
 * @since 1.0
 * @authors Natalie Eustace | Wanting Huang | Paul Smith | Craig Spence
 */
public interface ReverseAJAXCallInterface {

    /**
     * Getter for page.
     * @return String page - The page to perform the ReverseAJAXCall on.
     */
    String getPage();

    /**
     * Getter for output.
     * @return String output - The output of the ReverseAJAXCall.
     */
    String getElement();

    /**
     * Getter for element.
     * @return String element - The name of the HTML element on the page for the ReverseAJAXCall to edit.
     */
    String getOutput();

    /**
     * Getter for function.
     * @return String function - The name of the function within the pages JavaScript to call.
     */
    String getFunction();

    /**
     * Getter for params.
     * @return String params - Any parameters required for the Javascript function which is called.
     */
    String getParams();

    /**
     * Getter for done.
     * @return boolean done - Whether or not the Call has been performed by the manager.
     */
    boolean isDone();

    /**
     * Setter for done.
     * @param done - set to true when the ReverseAJAXManager completed this call
     */
    void setDone(final boolean done);

    /**
     * Getter for filter.
     * @return  ScriptSessionFilter filter - the filter which selects what pages the call should be distributed to.
     */
    ScriptSessionFilter getFilter();

    /**
     * With a given page, element and output, send a ReverseAJAXCall to the running
     * thread in ReverseAJAXManager.
     * @param String page - The page on which to run the ReverseAJAXCall.
     * @param String element - The element on the page in which to set the output.
     * @param String output - The output to set in the element.
     */
    void createNewCall(final String page, final String element, final String output);

    /**
     * With a given page, element and output, send a ReverseAJAXCall to the running
     * thread in ReverseAJAXManager, filtered by a ScriptSessionFilter.
     * @param String page - The page on which to run the ReverseAJAXCall.
     * @param String element - The element on the page in which to set the output.
     * @param String output - The output to set in the element.
     * @param String fitlerType - the type of filter that should be used.
     * @param String email - the email address which is used to make the filter (Usually the person who created a game).
     */
    void createNewFilteredCall(final String page, final String element, final String output, final String filterType, final String email);

    /**
     * With a given page, function and param, send a ReverseAJAXCall to the running
     * thread in ReverseAJAXManager, filtered by a ScriptSessionFilter.
     * @param String page - The page on which to run the ReverseAJAXCall.
     * @param String fitlerType - the type of filter that should be used.
     * @param String email - the email address which is used to make the filter (Usually the person who created a game).
     * @param String function - the name of the JavaScript function to call.
     * @param String params - the parameters (if any) for the Javascript function.
     */
    void createFilteredFunctionCall(final String page, final String filterType, final String email, final String function, final String params);

    /**
     * With a given page, function and param, send a ReverseAJAXCall to the running
     * thread in ReverseAJAXManager.
     * @param String page - The page on which to run the ReverseAJAXCall.
     * @param String function - the name of the JavaScript function to call.
     * @param String params - the parameters (if any) for the Javascript function.
     */
    void createFunctionCall(final String page, final String function, final String params);
}
