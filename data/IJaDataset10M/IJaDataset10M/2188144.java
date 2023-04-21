package org.dctmutils.daaf.method.lifecycle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dctmutils.daaf.DaafHelper;
import org.dctmutils.daaf.exception.DaafException;
import org.dctmutils.daaf.method.DaafMethodBase;
import org.dctmutils.daaf.object.DaafMethodArguments;

/**
 * Base class for methods involving Lifecycle activity. <br>
 * If a stateName is not passed to this method, the <codePromoteLifecycleMethod</code>
 * or <codeDemoteLifecycleMethod</code> will move the lifecycle to its next
 * state in the appropriate direction.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public abstract class LifecycleMethod extends DaafMethodBase {

    /**
     * 
     */
    private static Log log = LogFactory.getLog(LifecycleMethod.class);

    /**
     * The name of the package in which to find the document that we want to
     * promote.
     */
    public static final String PACKAGE_NAME = "packageName";

    /**
     * The optional 'targetStateName' argument.
     */
    public static final String TARGET_STATE_NAME_ARG = "targetStateName";

    /**
     * The object type of the document that we want to promote.
     */
    public static final String OBJECT_TYPE = "objectType";

    /**
     * Specify 'wcm-supporting' as the packageName to promote a supporting
     * document in a wcm workflow.
     */
    public static final String WCM_SUPPORTING_PACKAGE_TYPE = "wcm-supporting";

    /**
     * 'Package:0' is the default package name.
     */
    public static final String DEFAULT_PACKAGE_NAME = "Package:0";

    /**
     * The stateName member variable (not required).
     */
    protected String targetStateName = null;

    /**
     * The name of the package to retrieve the to document from.
     */
    protected String packageName = null;

    /**
     * The r_object_type fo the target document.
     */
    protected String objectType = null;

    /**
     * Creates a new <code>DemoteLifecycleMethod</code> instance.
     * 
     * @param helper
     * @param args
     * @throws DaafException
     */
    public LifecycleMethod(DaafHelper helper, DaafMethodArguments args) throws DaafException {
        super(helper);
        log.debug("LifecycleMethod called with args = " + args.toString());
        packageName = getArgumentValue(args, PACKAGE_NAME);
        targetStateName = getArgumentValue(args, TARGET_STATE_NAME_ARG);
        objectType = getArgumentValue(args, OBJECT_TYPE);
        if (StringUtils.isBlank(packageName)) {
            packageName = DEFAULT_PACKAGE_NAME;
        }
    }
}
