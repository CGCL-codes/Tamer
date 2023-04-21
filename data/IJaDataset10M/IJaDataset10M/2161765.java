package xdoclet.modules.mx4j;

import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.modules.jmx.XDocletModulesJmxMessages;
import xdoclet.util.Translator;

/**
 * Generates MBeanDescriptionAdaptor subclass for Mx4j.
 *
 * @author        Dmitri Colebatch (dim@bigpond.net.au)
 * @created       January 31, 2002
 * @ant.element   display-name="Mx4j Description" name="mx4jdescription" parent="xdoclet.modules.jmx.JMXDocletTask"
 * @version       $Revision: 1.7 $
 */
public class Mx4jDescriptionAdapterSubTask extends TemplateSubTask {

    protected static String mbeanDescriptionClassPattern;

    private static String DEFAULT_TEMPLATE_FILE = "resources/mx4j-mbean-description.xdt";

    private static String GENERATED_FILE_NAME = "{0}MBeanDescription.java";

    /**
     * Describe what the Mx4jDescriptionAdapterSubTask constructor does
     */
    public Mx4jDescriptionAdapterSubTask() {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile(GENERATED_FILE_NAME);
        setHavingClassTag("jmx:mbean");
        mbeanDescriptionClassPattern = "{0}MBeanDescription";
    }

    /**
     * Gets the MbeanDescriptionClassPattern attribute of the Mx4jDescriptionAdapterSubTask class
     *
     * @return   The MbeanDescriptionClassPattern value
     */
    public static String getMbeanDescriptionClassPattern() {
        return mbeanDescriptionClassPattern;
    }

    /**
     * Sets the Pattern attribute of the Mx4jDescriptionAdapterSubTask object
     *
     * @param new_pattern  The new Pattern value
     */
    public void setPattern(String new_pattern) {
        mbeanDescriptionClassPattern = new_pattern;
    }

    /**
     * Called to validate configuration parameters.
     *
     * @exception XDocletException  Description of Exception
     */
    public void validateOptions() throws XDocletException {
        super.validateOptions();
        if (getMbeanDescriptionClassPattern() == null || getMbeanDescriptionClassPattern().trim().equals("")) {
            throw new XDocletException(Translator.getString(XDocletMessages.class, XDocletMessages.PARAMETER_MISSING_OR_EMPTY, new String[] { "pattern" }));
        }
        if (getMbeanDescriptionClassPattern().indexOf("{0}") == -1) {
            throw new XDocletException(Translator.getString(XDocletModulesJmxMessages.class, XDocletModulesJmxMessages.PATTERN_HAS_NO_PLACEHOLDER));
        }
    }
}
