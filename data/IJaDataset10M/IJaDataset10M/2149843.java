package org.jmlnitrate.transformation.outbound;

import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmlnitrate.handler.response.HttpServletResponseHandler;
import org.jmlnitrate.handler.response.ResponseHandler;

/**
 * This class represents an {@link OutboundTransformation} the transforms an XML
 * Document using XSLT.  This class is specifically for HTTP and is bound to 
 * {@link HttpServletResponseHandler}.
 *
 * @author Arthur Copeland
 * @version $Revision: 3 $
 *
 */
public class HttpServletXSLTOutboundTransformation extends BaseOutboundTransformation {

    /**
     * Holds the logger
     */
    private static final Log logger = LogFactory.getLog(HttpServletXSLTOutboundTransformation.class);

    /**
     * Delegate XSLTransformationEngine
     */
    private XSLTOutboundTransformation xsltTransform;

    /**
     * Default Ctor.
     */
    public HttpServletXSLTOutboundTransformation() {
        super();
        xsltTransform = new XSLTOutboundTransformation();
    }

    /**
     * Transforms a {@link ResponseHandler} to a valid output format.
     *
     * @param response The Response to transform
     * @throws Exception if an error happens
     */
    public void transform(ResponseHandler response) throws Exception {
        Object xml = ((HttpServletResponseHandler) response).getValue(HttpServletResponseHandler.KEY_RESULT);
        String xsl = (String) (((HttpServletResponseHandler) response).getValue(HttpServletResponseHandler.KEY_VIEW));
        if (xsl != null && !xsl.equals("") && xsl.endsWith(".xsl")) {
            StreamResult result = xsltTransform.transform(xml, xsl);
            setTransformationResult(result.getWriter().toString());
        }
    }
}
