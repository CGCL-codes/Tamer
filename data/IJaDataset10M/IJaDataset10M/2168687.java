package net.metasimian.web.xml.transformation;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import net.metasimian.utilities.xml.transformation.MapToXML;

/**
 * 
 * @author metasimian@sourceforge.net
 *
 */
public class MapToXMLFilter implements Filter {

    public static String rootElementName = "request";

    public static String attributeKey = "xml";

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder loader = factory.newDocumentBuilder();
            Document document = loader.newDocument();
            document.appendChild(document.createElement(rootElementName));
            Map parameterMap = request.getParameterMap();
            MapToXML.mapToXML(document, parameterMap);
            request.setAttribute(attributeKey, document);
            chain.doFilter(request, response);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
