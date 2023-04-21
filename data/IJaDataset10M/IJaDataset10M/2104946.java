package net.javacrumbs.springws.test.expression;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import net.javacrumbs.springws.test.RequestProcessor;
import net.javacrumbs.springws.test.util.DefaultXmlUtil;
import net.javacrumbs.springws.test.util.XmlUtil;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;

/**
 * Iterates through expressions mapped by exceptionMapping, if the expression is evaluated as true on given request, 
 * method {@link #expressionValid(String, String)} is called.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractExpressionProcessor implements RequestProcessor {

    private static final String TRUE = Boolean.TRUE.toString();

    private int order;

    private ExpressionResolver expressionResolver;

    private Map<String, String> exceptionMapping;

    private XmlUtil xmlUtil = DefaultXmlUtil.getInstance();

    public AbstractExpressionProcessor() {
        super();
    }

    /**
	 * Iterates through expressions mapped by exceptionMapping, if the expression is evaluated as true on given request, 
     * method {@link #expressionValid(String, String)} is called.
	 */
    public WebServiceMessage processRequest(URI uri, WebServiceMessageFactory messageFactory, WebServiceMessage request) throws IOException {
        for (Map.Entry<String, String> entry : exceptionMapping.entrySet()) {
            String result = expressionResolver.resolveExpression(entry.getKey(), uri, xmlUtil.loadDocument(request));
            if (TRUE.equals(result)) {
                expressionValid(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }

    /**
	 * Method called if any XPath expression is evaluated as "true".
	 * @param expression
	 * @param errorMessage
	 */
    protected abstract void expressionValid(String expression, String errorMessage);

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver resolver) {
        this.expressionResolver = resolver;
    }

    public Map<String, String> getExceptionMapping() {
        return exceptionMapping;
    }

    public void setExceptionMapping(Map<String, String> exceptionMapping) {
        this.exceptionMapping = exceptionMapping;
    }

    public XmlUtil getXmlUtil() {
        return xmlUtil;
    }

    public void setXmlUtil(XmlUtil xmlUtil) {
        this.xmlUtil = xmlUtil;
    }
}
