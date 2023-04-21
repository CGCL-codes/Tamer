package uk.gov.dti.og.fox.logging;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import uk.gov.dti.og.fox.bean.PropertyBeanValueFilter;
import uk.gov.dti.og.fox.util.Filter;
import uk.gov.dti.og.fox.util.Lexer;
import uk.gov.dti.og.fox.util.Token;

/**
 * Initialises the 
 *
 * @author Gary Watson
 */
public class LoggingConfigurationServlet extends HttpServlet {

    /** Creates new ApplicationLifeCycleListener */
    public LoggingConfigurationServlet() {
    }

    /**
    * Called by the Servlet container just after the servlet is initialised with
    * its configuration, <code>init(ServletConfig)</code>.
    *
    * @see init(ServletConfig)
    */
    public void init() throws javax.servlet.ServletException {
        ServletConfig config = getServletConfig();
        Log.getInstance().removeAllLoggers();
        Log.getInstance().addLogger(new BufferedLogger(new ConsoleLogger()));
        String logExpression = config.getInitParameter("logExpression");
        if (logExpression != null) {
            try {
                Log.getInstance().setFilter(getPropertyExpressionFilter(logExpression));
                System.out.println("Logging expression parsed and set to \"" + logExpression + "\"...");
            } catch (Exception ex) {
                throw new ServletException("Unable to parse logging expression \"" + logExpression + "\" - please fix in web.xml deployment descriptior!");
            }
        }
    }

    /**
    * Called by the Servlet container just before the servlet is to be destroyed.
    */
    public void destroy() {
        Log.methodStart(this.getClass(), "destroy");
        Log.methodEnd(this.getClass(), "destroy");
    }

    /**
    * Called when a request to this servlet is made.
    *
    * @param request the HTTP request being made.
    * @param response the HTTP response generated ny this servlet.
    */
    public void service(final javax.servlet.ServletRequest request, final javax.servlet.ServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        Log.methodStart(this.getClass(), "service");
        Log.methodEnd(this.getClass(), "service");
    }

    /**
    * Returns a useful description of what this servlet does.
    *
    * @return information about this servlet.
    */
    public java.lang.String getServletInfo() {
        return "Application Logging Configuration Servlet";
    }

    public static Filter getPropertyExpressionFilter(String expression) {
        List tokenList = parseExpression(expression);
        List postfixTokenList = convertToPostfixNotation(tokenList);
        return createFilterFromPostfixExpression(postfixTokenList);
    }

    private static List parseExpression(String expression) {
        Lexer lexer = new Lexer(expression);
        LinkedList tokens = new LinkedList();
        Token currTok = lexer.getNextToken();
        while (currTok.getSymbol() != Token.EOF) {
            if (currTok.isWhiteSpace()) {
            } else if (currTok.getSymbol() == Token.WORD) {
                StringBuffer sBuf = new StringBuffer();
                sBuf.append(currTok.getString());
                while (lexer.PeekNextToken().getSymbol() == Token.PERIOD) {
                    lexer.getNextToken();
                    if (lexer.PeekNextToken().getSymbol() == Token.ALPHA) {
                        sBuf.append(".").append(lexer.getNextToken().getString());
                    }
                }
                tokens.add(sBuf.toString());
            } else {
                tokens.add(currTok.getString());
            }
            currTok = lexer.getNextToken();
        }
        return tokens;
    }

    private static List convertToPostfixNotation(List tokenList) {
        return tokenList;
    }

    private static Filter createFilterFromPostfixExpression(List postfixExpressionList) {
        return new PropertyBeanValueFilter((String) postfixExpressionList.get(0), (String) postfixExpressionList.get(1), (String) postfixExpressionList.get(2));
    }

    public static void main(String args[]) {
        LoggingConfigurationServlet serv = new LoggingConfigurationServlet();
        serv.getPropertyExpressionFilter("severity.code >= 3");
    }
}
