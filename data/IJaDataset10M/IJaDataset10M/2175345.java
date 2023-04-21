package org.ox.framework.web.renderers;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.RowSetDynaClass;
import org.apache.log4j.Logger;
import org.ox.framework.exception.oxException;
import org.ox.framework.utils.Utils;
import org.ox.framework.web.responses.defaultResponse;
import org.ox.framework.web.servlet.businessServlet;

/**
 *
 * @author admin
 */
public class SingleData implements oxRenderer {

    private static Logger logger = Logger.getLogger(SingleData.class);

    private String contentType;

    private Properties headers = null;

    private boolean resetHeaders;

    public void doRender(defaultResponse dresponse, HttpServletRequest request, HttpServletResponse response) {
        String str = "<oxmessage>";
        str = str + "<data>" + dresponse.getData() + "</data>";
        str = str + "<message>" + dresponse.getMessage() + "</message>";
        str = str + "<validations>";
        for (Object key : dresponse.getValidationMsg().keySet()) {
            String value = dresponse.getValidationMsg().getProperty("" + key);
            str = str + "<validation>";
            str = str + "<fid>" + key + "</fid>";
            str = str + "<message>" + value + "</message>";
            str = str + "</validation>";
        }
        str = str + "</validations>";
        str = str + "</oxmessage>";
        try {
            Writer w = (Writer) request.getAttribute(businessServlet.WRITER);
            w.append(str);
        } catch (IOException ex) {
            logger.error("ERROR: Cannot write response", ex);
            throw new oxException("ERROR: Cannot write response");
        }
        if (isResetHeaders()) {
            response.reset();
        }
        if (getHeaders() != null) {
            for (Enumeration E = getHeaders().propertyNames(); E.hasMoreElements(); ) {
                String p = (String) E.nextElement();
                response.addHeader(p, getHeaders().getProperty(p));
            }
        }
        response.setContentType(getContentType());
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the headers
     */
    public Properties getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Properties headers) {
        this.headers = headers;
    }

    /**
     * @return the resetHeaders
     */
    public boolean isResetHeaders() {
        return resetHeaders;
    }

    /**
     * @param resetHeaders the resetHeaders to set
     */
    public void setResetHeaders(boolean resetHeaders) {
        this.resetHeaders = resetHeaders;
    }
}
