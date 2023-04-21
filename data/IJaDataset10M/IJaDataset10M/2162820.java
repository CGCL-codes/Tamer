package com.windsor.node.plugin.eis12;

import com.windsor.node.common.domain.NodeTransaction;
import com.windsor.node.common.domain.ProcessContentResult;

public class EISGetPointEmissions extends BaseEisXmlPlugin {

    public static final String SERVICE_NAME = "EISGetPointEmissions";

    /** Velocity template name. */
    public static final String TEMPLATE_NAME = "Point.vm";

    private static final String OUTFILEBASE_NAME = "EISPointEmissions";

    public EISGetPointEmissions() {
        super();
        setServiceName(SERVICE_NAME);
        setDataCategory(DataCategory.Point);
        debug("EISGetPointEmissions instantiated.");
    }

    public ProcessContentResult process(NodeTransaction transaction) {
        return generateXmlFile(transaction, TEMPLATE_NAME, OUTFILEBASE_NAME);
    }
}
