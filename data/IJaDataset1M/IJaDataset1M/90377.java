package net.sf.snipper.web.view.util;

import net.sf.snipper.web.util.SnipDisplayHelper;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple view helper for generating Spring ModelAndView objects that are: <ol> <li>Standardised across the application (meaning
 * that the same named object is passed to the same named view under 'standard' conditions)</li> <li>Simple in that they take a
 * single object to the view (which would usually be a {@link SnipDisplayHelper}</li> </ol>
 * 
 * @author Dan Shannon
 * @version $Id: SimpleViewHelper.java,v 1.1 2006/11/14 08:28:33 dshannon Exp $
 */
public class SimpleViewHelper {

    /**
     * Base helper for simply displaying a snip
     */
    public static final SimpleViewHelper DISPLAY_SNIP = new SimpleViewHelper("displaySnip", "displayHelper");

    /**
     * Helper for display when a snip is unknown
     */
    public static final SimpleViewHelper UNKNOWN_SNIP = new SimpleViewHelper("unknownSnipError", "displayHelper");

    /**
     * Helper for display when an application is unknown
     */
    public static final SimpleViewHelper UNKNOWN_APPLICATION = new SimpleViewHelper("unknownApplicationError", "displayHelper");

    /**
     * Helper for display when administering an application
     */
    public static final SimpleViewHelper EDIT_APPLICATION = new SimpleViewHelper("adminApplication", "displayHelper");

    /**
     * Helper for display when editing a snip
     */
    public static final SimpleViewHelper EDIT_SNIP = new SimpleViewHelper("editSnip", "displayHelper");

    /**
     * Helper for displaying snip history
     */
    public static final SimpleViewHelper DISPLAY_SNIP_HISTORY = new SimpleViewHelper("displaySnipHistory", "displayHelper");

    /**
     * Generate the Spring ModelAndView object
     * 
     * @param viewHelper The viewHelper to use
     * @param object The object to be passed to the view
     * @return The ModelAndView for the Spring view resolver to use
     */
    public static ModelAndView modelAndView(SimpleViewHelper viewHelper, Object object) {
        return new ModelAndView(viewHelper.viewName, viewHelper.displayHelperObjectName, object);
    }

    /**
     * The view name to be passed to the Spring view resolver
     */
    private String viewName;

    /**
     * The display object name to be passed to the view (so that the JSTL tag ${[name]} will work).
     */
    private String displayHelperObjectName;

    /**
     * Trivial constructor used in construction of the static instances
     * 
     * @param viewName The view name
     * @param displayHelperObjectName The default display helper object name that will be passed to the view resolver
     */
    private SimpleViewHelper(String viewName, String displayHelperObjectName) {
        this.displayHelperObjectName = displayHelperObjectName;
        this.viewName = viewName;
    }
}
