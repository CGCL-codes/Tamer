package com.sri.emo.wizard;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The interface for generic wizards.  Often the actual operation of the
 * wizard will be performed by another controller.  The wizard provides
 * more of a domain model for traversing a series of steps.
 *
 * @author Michael Rimov
 */
public interface Wizard extends Serializable {

    /**
     * Retrieve the first wizard page and initialize any special data for the
     * Wizard.
     *
     * @return WizardPage instance.
     * @throws WizardException upon error.
     */
    WizardPage begin() throws WizardException;

    /**
     * Retrieves a pointer to the current page in the wizard.
     *
     * @return Wizard Page instance
     * @throws IllegalStateException if this is called before begin() is
     *                               ever invoked.
     */
    WizardPage getCurrentPage();

    /**
     * Retrieve a page by a given id.  The id is different dependong
     * on the implementation.
     *
     * @param s Serializable the page key.
     * @return WizardPage as defined.
     * @throws IllegalArgumentException if s is null or the page given doesn't
     *                                  exist.
     */
    WizardPage getPageById(Serializable s);

    /**
     * Allows programmatic rewinding to a previous page.
     *
     * @param pageId Serializable the page key.
     * @return WizardPage the resulting page.
     */
    WizardPage backupToPage(Serializable pageId);

    /**
     * Execute the next page in the wizard.
     *
     * @param newData the data to post to the Wizard.
     * @param src     the source page of the next event.
     * @return WizardPage instance.
     * @throws WizardException upon error.
     */
    WizardPage next(WizardPage src, Serializable newData) throws WizardException;

    /**
     * Retrieve the first wizard page and initialize any special data for the
     * Wizard.
     *
     * @return WizardPage instance.
     * @throws WizardException upon error.
     */
    WizardPage previous() throws WizardException;

    /**
     * Processes the final finish and returns some sort of data that can
     * be whatever is desired.  If using a web environment, the WizardController
     * will put the object on the request context with the name
     * &quot;WizardResult&quot;
     *
     * @param src             WizardPage the source of the event.
     * @param data            Serializable the data given during wht wizard post.
     * @param additonalParams anything that the underlying wizard needs.
     *                        The values are set by the Application Controller.
     * @return Object: whatever object the wizard returns after
     *         processing.
     * @throws WizardException upon error.
     */
    Object processFinish(WizardPage src, Serializable data, Map additonalParams) throws WizardException;

    /**
     * De-initialize all data for the wizard.
     *
     * @throws WizardException upon destruction error.
     */
    void destroy() throws WizardException;

    /**
     * Retrieve all the data for the wizard keyed by page id.
     *
     * @return Map keyed by id.
     * @throws WizardException upon error
     */
    Map getAllData() throws WizardException;

    /**
     * Retrieve all Pages in the order that the person has stepped through
     * them.
     *
     * @return List of WizardPage objects.
     * @throws WizardException if unable to query history.
     */
    List getStepHistory() throws WizardException;

    /**
     * Retrieve the title of the wizard.
     *
     * @return java.lang.String.
     */
    String getTitle();

    /**
     * Retrieve the text summary of the wizard.  May return null since summary
     * attribute is optional.
     *
     * @return String or null if none is defined.
     */
    String getSummary();

    /**
     * Retrieve the id of the Wizard.  Implementations may return
     * null if they do not differentiate wizards by any id.
     *
     * @return Object any particular object.
     */
    Object getId();
}
