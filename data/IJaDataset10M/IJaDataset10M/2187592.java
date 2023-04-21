package org.nextframework.core.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nextframework.core.config.Config;
import org.nextframework.core.standard.Next;
import org.nextframework.core.web.init.ContextLoaderListener;
import org.nextframework.exception.NotInNextContextException;

/**
 * @author rogelgarcia
 * @since 21/01/2006
 * @version 1.1
 */
public class NextWeb extends Next {

    protected static final Log log = LogFactory.getLog(NextWeb.class);

    public static void createRequestContext(HttpServletRequest request, HttpServletResponse response) {
        if (request == null || response == null) {
            throw new NullPointerException();
        }
        ServletContext servletContext = request.getSession().getServletContext();
        getWebApplicationContext(servletContext);
        getRequestContext(request, response);
    }

    public static WebRequestContext getRequestContext() throws NotInNextContextException {
        WebRequestContext context = (WebRequestContext) requestContext.get();
        if (context == null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            String msg = "O c�digo n�o est� sendo executado em um contexto NEXT!";
            if (stackTrace.length >= 4) {
                msg += "\nClasse: " + stackTrace[3].getClassName() + " " + "\nM�todo: " + stackTrace[3].getMethodName() + " " + "\nLinha: " + stackTrace[3].getLineNumber();
            }
            msg += "\nSe voc� estiver executanto em um ambiente J2EE, verifique se voc� configurou no web.xml o filtro da classe ???." + "\nA configura��o desse filtro � importante porque � ele quem cria o contexto NEXT.";
            throw new NotInNextContextException(msg);
        }
        return context;
    }

    public static void createApplicationContext(ServletContext servletContext, Config config) {
        WebApplicationContext applicationContext;
        applicationContext = new DefaultWebApplicationContext(config, servletContext);
        servletContext.setAttribute(WebApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        Next.applicationContext.set(applicationContext);
    }

    public static WebRequestContext getRequestContext(HttpServletRequest request, HttpServletResponse response) {
        getWebApplicationContext(request.getSession().getServletContext());
        WebRequestContext requestContext = (WebRequestContext) request.getAttribute(WebRequestContext.REQUEST_CONTEXT_ATTRIBUTE);
        if (requestContext == null) {
            log.trace("Criando contexto de requisi��o Next... ");
            requestContext = new DefaultWebRequestContext(request, response, getWebApplicationContext(request.getSession().getServletContext()));
            request.setAttribute(WebRequestContext.REQUEST_CONTEXT_ATTRIBUTE, requestContext);
            Next.requestContext.set(requestContext);
        } else {
            requestContext = new DefaultWebRequestContext(request, response, getWebApplicationContext(request.getSession().getServletContext()));
            request.setAttribute(WebRequestContext.REQUEST_CONTEXT_ATTRIBUTE, requestContext);
            Next.requestContext.set(requestContext);
        }
        return requestContext;
    }

    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        WebApplicationContext applicationContext = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE);
        if (applicationContext == null) {
            throw new NotInNextContextException("O contexto de aplica��o NEXT ainda n�o foi criado. Verifique se o listener " + ContextLoaderListener.class.getName() + " est� configurado no web.xml");
        }
        Next.applicationContext.set(applicationContext);
        return applicationContext;
    }
}
