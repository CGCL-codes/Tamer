package com.jvantage.ce.web;

import com.jvantage.ce.acl.AccessDeniedException;
import com.jvantage.ce.common.TagConstants;
import com.jvantage.ce.common.URLCreator;
import com.jvantage.ce.facilities.system.GeneralLicenseException;
import com.jvantage.ce.presentation.PageContext;
import com.jvantage.ce.presentation.PageData;
import com.jvantage.ce.presentation.PresentationException;
import com.jvantage.ce.presentation.ejb.PageBuilderLocal;
import com.jvantage.ce.session.SessionID;
import com.jvantage.ce.session.UserRequestMessage;
import com.jvantage.ce.session.UserResponseMessage;
import org.apache.commons.lang.StringUtils;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author Brent Clay
 */
public class MainXml extends HttpServlet {

    @EJB
    private PageBuilderLocal pageBuilderBean;

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        httpResponse.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = httpResponse.getWriter();
        try {
            UserRequestMessage jVantageRequest = new UserRequestMessage(httpRequest);
            UserResponseMessage jVantageResponse = new UserResponseMessage(httpResponse);
            Long trxNumberLong = (Long) httpRequest.getAttribute(URLCreator.fURLParameter_TransactionNumber);
            if ((trxNumberLong != null) && (trxNumberLong.longValue() > 0L)) {
                jVantageRequest.setParameterNameAndValue(URLCreator.fURLParameter_TransactionNumber, String.valueOf(trxNumberLong));
            }
            jVantageRequest.setParameterNameAndValue(URLCreator.fURLParameter_UiDialect, URLCreator.fURLParameter_UiDialectXml);
            HttpSession httpSession = httpRequest.getSession(false);
            if (httpSession != null) {
                SessionID sessionID = (SessionID) httpSession.getAttribute(MainHtml.jVantageSessionKey);
                PageContext pageContext = new PageContext();
                pageContext.setSessionID(sessionID);
                pageContext.setUserRequestMessage(jVantageRequest);
                pageContext.setUserResponseMessage(jVantageResponse);
                try {
                    pageContext = pageBuilderBean.render(pageContext);
                    PageData pageData = pageContext.getPageData();
                    if (pageData != null) {
                        out.print(pageData.getTagHashtable().get(TagConstants.sfDefaultRecordBeg));
                    }
                } catch (PresentationException ex) {
                    Logger.getLogger(MainXml.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GeneralLicenseException ex) {
                    Logger.getLogger(MainXml.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AccessDeniedException ex) {
                    Logger.getLogger(MainXml.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            out.close();
        }
    }

    private void setUrmParameterFromHttpRequest(HttpServletRequest httpRequest, UserRequestMessage jVantageRequest, String parameterName) {
        String value = httpRequest.getParameter(parameterName);
        if (StringUtils.isNotBlank(value)) {
            jVantageRequest.setParameterNameAndValue(parameterName, value);
        }
    }
}
