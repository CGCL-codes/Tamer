package com.tcs.bancs.ui.filters.security.saml;

import javax.servlet.http.HttpServletRequest;
import com.tcs.bancs.channels.integration.UIError;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class DefaultErrorHandler extends ErrorHandler {

    @Override
    public String getHtml(HttpServletRequest httpRequest, Errors error, String stackTrace) {
        StringBuffer html = new StringBuffer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        String uiError = error.name();
        html.append("<%@page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\"%>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>ErrorToCS</title>");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
        html.append("</head>");
        html.append("<body>");
        html.append("<div id='errdisplay' style='display: none;'>");
        html.append("	<H2>Error: ").append(uiError).append("</H2>");
        html.append("</div>");
        if (stackTrace != null) {
            html.append(stackTrace);
        }
        html.append("</body>");
        html.append("</html>");
        String returnString = html.toString();
        return returnString;
    }

    @Override
    public void init(String invalidSessionHandlerInitParam) throws ConfigXMLParsingException {
    }
}
