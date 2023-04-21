package org.orbeon.oxf.processor.xforms.input.action;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.jaxen.FunctionContext;
import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.xml.XPathUtils;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import java.util.Collections;
import java.util.List;

public class ActionUtils {

    /**
     * Gets a nodeset from the instance. Nodeset is a list of elements.
     */
    public static List getNodeset(PipelineContext context, FunctionContext functionContext, Document instance, String nodeset) {
        XPath nodesetXPath = XPathUtils.xpathWithFullURI(context, nodeset);
        nodesetXPath.setFunctionContext(functionContext);
        Object nodesetObject = nodesetXPath.evaluate(instance);
        if (nodesetObject instanceof List) {
            return (List) nodesetObject;
        } else if (nodesetObject instanceof Element) {
            return Collections.singletonList(nodesetObject);
        } else {
            throw new OXFException("nodeset attribute '" + nodeset + "' in insert action must return nodeset");
        }
    }

    /**
     * Evaluate the "at" XPath expression 
     */
    public static int getAtValue(FunctionContext functionContext, List nodeset, String at) {
        int atValue;
        {
            XPath atXPath = DocumentHelper.createXPath("round(" + at + ")");
            atXPath.setFunctionContext(functionContext);
            Number atObject = (Number) atXPath.evaluate(nodeset);
            if (Double.isNaN(atObject.doubleValue())) return Integer.MAX_VALUE;
            atValue = atObject.intValue() - 1;
        }
        if (atValue < 0) atValue = 0;
        if (atValue >= nodeset.size()) atValue = nodeset.size() - 1;
        return atValue;
    }
}
