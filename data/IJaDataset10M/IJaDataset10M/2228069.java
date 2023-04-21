package xdoclet.modules.apache.tapestry;

import org.apache.commons.logging.Log;
import xjavadoc.XClass;
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;

/**
 * Generates component-specifications for Tapestry applications.
 *
 * @author        Michael Newcomb (mnewcomb@sourceforge.net)
 * @created       February 4, 2005
 * @version       $Revision: 1.1 $
 * @ant.element   name="component-specification" display-name="Component-Specification SubTask"
 *      parent="xdoclet.modules.apache.tapestry.TapestryDocletTask"
 */
public class ComponentSpecificationSubTask extends TemplateSubTask {

    protected static final String DEFAULT_TEMPLATE_FILE = "resources/component-specification.xdt";

    /**
     */
    public ComponentSpecificationSubTask() {
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setDestinationFile("{0}.jwc");
        setPrefixWithPackageStructure(false);
    }

    /**
     * Describe what the method does
     *
     * @param clazz                 Describe what the parameter does
     * @return                      Describe the return value
     * @exception XDocletException
     */
    protected boolean matchesGenerationRules(XClass clazz) throws XDocletException {
        return clazz.getDoc().getTag("tapestry.component-specification") != null;
    }
}
