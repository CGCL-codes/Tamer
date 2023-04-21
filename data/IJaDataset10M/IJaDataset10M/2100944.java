package edu.mit.lcs.haystack.server.extensions.weboperation;

import java.util.Map;
import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.ozone.web.WebViewPart;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.INode;

/**
 * @author Ryan Manuel
 */
public class WebOpManager {

    public static final String NAMESPACE = "http://haystack.lcs.mit.edu/schemata/WebOperation#";

    public static final String OPERATION_NAMESPACE = "http://haystack.lcs.mit.edu/schemata/operation#";

    public static final String METADATA_NAMESPACE = "http://haystack.lcs.mit.edu/ui/metadataEditor#";

    public static final Resource OPERATION = new Resource(OPERATION_NAMESPACE, "Operation");

    public static final Resource OPERATION_PARAMETER = new Resource(OPERATION_NAMESPACE, "Parameter");

    public static final Resource OPERATION_REQUIRED = new Resource(OPERATION_NAMESPACE, "required");

    public static final Resource OPERATION_POSSIBLE_VALUES = new Resource(OPERATION_NAMESPACE, "possibleValues");

    public static final Resource OPERATION_PERFORM_NO_REQUIRED = new Resource(OPERATION_NAMESPACE, "dispIfParamsAllNotRequired");

    public static final Resource METADATA_STRING = new Resource(METADATA_NAMESPACE, "String");

    public static final Resource WEB_OPERATION = new Resource(NAMESPACE, "WebOperation");

    public static final Resource WEB_OPERATION_TYPE = new Resource(NAMESPACE, "operType");

    public static final Resource WEB_OPERATION_ACTION_URL = new Resource(NAMESPACE, "actionURL");

    public static final Resource WEB_OPERATION_INIT_PAGE = new Resource(NAMESPACE, "initPage");

    public static final Resource WEB_OPERATION_HEADERS = new Resource(NAMESPACE, "headers");

    public static final Resource WEB_OPERATION_PARAMETER_PROP = new Resource(NAMESPACE, "operParameter");

    public static final Resource WEB_OPERATION_PATTERN = new Resource(NAMESPACE, "pattern");

    public static final Resource VISIBLE_WEB_OPERATION = new Resource(NAMESPACE, "VisibleWebOperation");

    public static final Resource VISIBLE_WEB_OPERATION_URL = new Resource(NAMESPACE, "visibleURL");

    public static final Resource VISIBLE_WEB_OPERATION_HEADERS = new Resource(NAMESPACE, "visibleHeaders");

    public static final Resource VISIBLE_WEB_OPERATION_POSTDATA = new Resource(NAMESPACE, "visiblePostData");

    public static final Resource PARAMETER = new Resource(NAMESPACE, "WebOpParameter");

    public static final Resource PARAMETER_ORIGINAL_NAME = new Resource(NAMESPACE, "origName");

    public static final Resource PARAMETER_ORIGINAL_VALUE = new Resource(NAMESPACE, "origValue");

    public static final Resource PARAMETER_TYPE = new Resource(NAMESPACE, "paramType");

    public static final Resource PARAMETER_RANGE_PROP = new Resource(NAMESPACE, "paramRange");

    public static final Resource PARAMETER_DISPLAY_NAME = new Resource(NAMESPACE, "dispName");

    public static final Resource PARAMETER_RIGHT_TEXT = new Resource(NAMESPACE, "rightText");

    public static final Resource PARAMETER_LEFT_TEXT = new Resource(NAMESPACE, "leftText");

    public static final Resource PARAMETER_RANGE = new Resource(NAMESPACE, "paramRangeValue");

    public static final Resource PARAMETER_RANGE_DISPLAY = new Resource(NAMESPACE, "paramRangeDisplay");

    public static final Resource PARAMETER_RANGE_SUBMIT = new Resource(NAMESPACE, "paramRangeSubmit");

    /**
     *  creates a web operation from an example
     */
    public static Resource createWebOperation(IRDFContainer rdfc, WebViewPart webView, String operName, Resource patternRes) throws RDFException {
        String url = webView.getCurrentOpURL();
        String headers = webView.getCurrentOpHeaders();
        String postData = webView.getCurrentOpPostData();
        INode[] containingForms = webView.getInitDoc();
        String initURL = webView.getInitiatingURL();
        Resource initPage = new Resource(initURL);
        System.out.println("Creating Web Operation: " + operName);
        WebOperation webOp = new WebOperation(url, headers, postData, containingForms, patternRes, initPage);
        if (operName != null) {
            webOp.setOperationName(operName);
        } else {
            webOp.setOperationName("Web Operation");
        }
        Resource returnRes = webOp.makeResource(rdfc);
        System.out.println("Web Operation Created\n" + webOp);
        return returnRes;
    }

    /**
	 *  invokes a web operation with the values specified in namedValues
	 */
    public static void invokeWebOperation(IRDFContainer rdfc, Map namedValues, Resource webOpRes, Context context) throws RDFException {
        WebOperation webOp = WebOperation.fromResource(webOpRes, rdfc);
        webOp.invokeOperation(namedValues, rdfc, context);
    }
}
