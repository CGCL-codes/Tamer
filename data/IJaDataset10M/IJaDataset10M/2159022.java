package gov.fnal.mcas.servlets.base;

import gov.fnal.mcas.util.XsltTransform;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XsltServlet extends HttpServlet {

    public static final String DataSourceURL = "DataSourceURL";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        try {
            String filePath = getServletContext().getRealPath(getXsltScript(request));
            Map<String, String> xsltParams = getXsltParams(request);
            String sourceUrl = xsltParams.get(XsltTransform.ThisDataSourceURL);
            if (sourceUrl == null) sourceUrl = getDataSourceURL(request);
            XsltTransform.doTransform(filePath, sourceUrl, xsltParams, writer);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    public String getXsltScript(HttpServletRequest request) throws ServletException {
        String xsltName = request.getParameter("xslt");
        if (xsltName == null) {
            String msg = "Must specify name of xsl transform for command: xslt";
            throw new ServletException(msg);
        }
        return xsltName;
    }

    public Map<String, String> getXsltParams(HttpServletRequest request) {
        LinkedHashMap<String, String> xsltParams = new LinkedHashMap<String, String>(request.getParameterMap());
        return xsltParams;
    }

    public String getDataSourceURL(HttpServletRequest request) throws ServletException {
        String sourceUrl = getParameterValue(request, DataSourceURL, null);
        if (sourceUrl == null) {
            String msg = "Must specify name of data source URL for command: xslt";
            throw new ServletException(msg);
        }
        return sourceUrl;
    }

    public String getParameterValue(HttpServletRequest request, String parameter_name, String default_value) {
        String parameter_value = request.getParameter(parameter_name);
        if (parameter_value == null) parameter_value = default_value;
        return parameter_value;
    }
}
