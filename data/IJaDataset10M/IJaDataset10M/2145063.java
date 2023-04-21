package org.ofbiz.workflow.impl;

/**
 * WfActivityImplementationFact.java
 */
public class WfActivityImplementationFact {

    public static final String module = WfActivityImplementationFact.class.getName();

    /**
     * Gets the implementation class to be used.
     * @param type
     * @param wfActivity
     * @return WfActivityAbstractImplementation
     */
    public static WfActivityAbstractImplementation getConcretImplementation(String type, WfActivityImpl wfActivity) {
        if (type.equals("WAT_NO")) return new WfActivityNoImplementation(wfActivity); else if (type.equals("WAT_ROUTE")) return new WfActivityRouteImplementation(wfActivity); else if (type.equals("WAT_TOOL")) return new WfActivityToolImplementation(wfActivity); else if (type.equals("WAT_SUBFLOW")) return new WfActivitySubFlowImplementation(wfActivity); else if (type.equals("WAT_LOOP")) return new WfActivityLoopImplementation(wfActivity); else return null;
    }
}
