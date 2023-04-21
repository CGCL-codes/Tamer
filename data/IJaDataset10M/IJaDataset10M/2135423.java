package openvend.component;

import openvend.cgi.OvCgiSubmission;
import openvend.main.I_OvRequestContext;
import openvend.main.OvXmlModel;
import org.w3c.dom.Element;

/**
 * Dumps CGI parameters into the XML model of a request.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.5 $
 * @since 1.0
 */
public class OvCgiDumpComponent extends A_OvComponent {

    /**
     * @see openvend.component.A_OvComponent#handleRequest(I_OvRequestContext)
     */
    public void handleRequest(I_OvRequestContext requestContext) throws Exception {
        OvXmlModel xmlModel = requestContext.getXmlModel();
        Element componentElement = xmlModel.appendElement(getId());
        OvCgiSubmission submission = new OvCgiSubmission(requestContext, getCgiParams());
        xmlModel.appendCgiParams(componentElement, submission, "true".equalsIgnoreCase(getParam("validate-cgi-params", "false")));
    }
}
