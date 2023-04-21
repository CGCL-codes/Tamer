package org.insight.common.report;

import java.util.ResourceBundle;

/**
 * Utility methods for handling resource strings dedicated to a particular
 * report.
 *
 * <pre>
 * Version History:
 *
 * $Log: ReportResources.java,v $
 * Revision 1.2  2006/02/03 09:05:20  cjn
 * Fixed checkstyle violations.
 *
 * Revision 1.1  2003/03/19 09:59:25  cjn
 * Refactored packages
 *
 * Revision 1.1  2002/09/05 16:05:53  cjn
 * Initial creation
 * </pre>
 *
 * @author Chris Nappin
 * @version $Revision: 1.2 $
 */
public class ReportResources {

    /** The resource bundle being wrapped. */
    private ResourceBundle bundle;

    /**
     * Creates the wrapper object, and loads the specified resource bundle.
     * @param name The name of the resource bundle
     */
    public ReportResources(String name) {
        bundle = ResourceBundle.getBundle(name);
    }

    /**
     * Get a specific string.
     * @param name The name of the resource string
     * @return The value of the resource string
     */
    public String getString(String name) {
        return bundle.getString(name);
    }
}
