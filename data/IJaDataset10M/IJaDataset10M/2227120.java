package com.google.gsa.valve.saml.authz;

import com.google.gsa.AuthorizationProcessImpl;
import com.google.gsa.sessions.nonValidSessionException;
import com.google.gsa.valve.configuration.ValveConfiguration;
import com.google.gsa.valve.configuration.ValveConfigurationException;
import com.google.gsa.valve.configuration.ValveConfigurationInstance;
import com.google.gsa.valve.saml.XmlProcessingException;
import com.google.gsa.valve.utils.ValveUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.log4j.Logger;

/**
 * It implements the server process that treats the SAML authorization 
 * requests coming from the appliance. Each request includes the username and 
 * the URL, so that this servlet uses the user information internally stored 
 * in a session in order to authorize the access to that content.
 * <p>
 * It invokes the root authorization process to get the authorization class 
 * that is going to process it. It gets that result and sends it back to the 
 * caller (appliance) in SAML format. If it's a 20X response (OK), it returns a 
 * "Permit" message and if not, it'll return a "Deny".
 * It doesn't care about the content sent from the content source as it only 
 * checks security.
 * <p>
 * In the case the root authorization would not have any URL pattern that 
 * matches with the content URL, it sends a -1 error code that is treated 
 * here as an "Indeterminate" response.
 * 
 * 
 */
public class SAMLAuthZ extends HttpServlet {

    static final String SOAP_ENV_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    static final String SAML_NS = "urn:oasis:names:tc:SAML:2.0:assertion";

    static final String SAMLP_NS = "urn:oasis:names:tc:SAML:2.0:protocol";

    static final String SAML_STATUS_CODE_SUCCESS = "urn:oasis:names:tc:SAML:2.0:status:Success";

    static final String SAML_STATUS_CODE_REQUESTER = "urn:oasis:names:tc:SAML:2.0:status:Requester";

    static final String SAML_STATUS_CODE_RESPONDER = "urn:oasis:names:tc:SAML:2.0:status:Responder";

    static final String SAML_DECISION_PERMIT = "Permit";

    static final String SAML_DECISION_DENY = "Deny";

    static final String SAML_DECISION_INDETERMINATE = "Indeterminate";

    private static Logger logger = Logger.getLogger(SAMLAuthZ.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss'Z'");

    private String authorizationProcessClsName = null;

    private AuthorizationProcessImpl authorizationProcessCls = null;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> 
     * methods. It gets the SAML authorization request from the appliance and 
     * processes it accordingly.
     * 
     * @param request HTTP request
     * @param response HTTP response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("AuthzDecisionQuery received");
        response.setContentType("text/xml;charset=utf-8");
        try {
            setAuthorizationClass();
        } catch (ValveConfigurationException e) {
            logger.error("Configuration error when setting Authorization instance: " + e);
        }
        String buildStatus = SAML_STATUS_CODE_SUCCESS;
        AuthzDecisionQuery query = null;
        try {
            query = extractQueryFromRequest(request);
        } catch (XmlProcessingException ex) {
            logger.error("Bad input XML string - will respond " + SAML_STATUS_CODE_REQUESTER, ex);
            buildStatus = SAML_STATUS_CODE_REQUESTER;
        } catch (Exception ex) {
            logger.error("Bad input XML string - unable to respond", ex);
            throw new ServletException(ex);
        }
        String decisionResult = SAML_DECISION_INDETERMINATE;
        if (query != null) {
            try {
                decisionResult = getDecision(request, response, query);
            } catch (Exception ex) {
                logger.error("Problems getting decision - will respond " + SAML_STATUS_CODE_RESPONDER, ex);
                buildStatus = SAML_STATUS_CODE_RESPONDER;
            }
        } else {
            query = new AuthzDecisionQuery();
        }
        PrintWriter out = response.getWriter();
        logger.debug("AuthzDecisionQuery received with ID=\"" + query.getId() + "\"" + " for accessing resource=\"" + query.getResource() + "\"" + " by subject=\"" + query.getSubject() + "\"" + " with action=\"" + query.getAction() + "\"" + " (namespace=\"" + query.getActionNamespace() + "\")" + ", responding decision=\"" + decisionResult + "\"");
        try {
            buildResponse(buildStatus, out, query, decisionResult);
        } catch (Exception ex) {
            logger.error("Problems generating SOAP response - unable to respond", ex);
            throw new ServletException(ex);
        }
        out.close();
    }

    /** 
     * Servlet's doGet
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Servlet's doPost
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "SAML authorization servlet";
    }

    /**
     * Extracts the SAML authorization query from the request in order to 
     * process it. It sends the authorization object to be processed through 
     * the authorization.
     * 
     * @param request HTTP request
     * 
     * @return authorization decision query
     * 
     * @throws IOException
     * @throws XMLStreamException
     * @throws XmlProcessingException
     */
    private AuthzDecisionQuery extractQueryFromRequest(HttpServletRequest request) throws IOException, XMLStreamException, XmlProcessingException {
        InputStream is = request.getInputStream();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader reader = xif.createXMLStreamReader(is);
        StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader);
        QName qName = null;
        try {
            qName = new QName(SAMLP_NS, "AuthzDecisionQuery");
            OMElement authzDecisionQuery = builder.getSOAPEnvelope().getBody().getFirstChildWithName(qName);
            checkNotNull(authzDecisionQuery);
            qName = new QName(null, "ID");
            OMAttribute id = authzDecisionQuery.getAttribute(qName);
            checkNotNull(id);
            qName = new QName(null, "Resource");
            OMAttribute resource = authzDecisionQuery.getAttribute(qName);
            checkNotNull(resource);
            qName = new QName(SAML_NS, "Subject");
            OMElement subject = authzDecisionQuery.getFirstChildWithName(qName);
            checkNotNull(subject);
            qName = new QName(SAML_NS, "NameID");
            OMElement nameId = subject.getFirstChildWithName(qName);
            checkNotNull(nameId);
            qName = new QName(SAML_NS, "Action");
            OMElement action = authzDecisionQuery.getFirstChildWithName(qName);
            checkNotNull(action);
            qName = new QName(null, "Namespace");
            OMAttribute actionNamespace = action.getAttribute(qName);
            checkNotNull(actionNamespace);
            AuthzDecisionQuery query = new AuthzDecisionQuery(id.getAttributeValue().trim(), resource.getAttributeValue().trim(), nameId.getText().trim(), action.getText().trim(), actionNamespace.getAttributeValue().trim());
            return query;
        } catch (NullPointerException ex) {
            throw new XmlProcessingException(qName + " not found while processing SAML AuthzDecisionQuery request");
        } catch (Exception ex) {
            throw new XmlProcessingException(qName + " not found while processing SAML AuthzDecisionQuery request", ex);
        }
    }

    /**
     * Builds the SAML authorization response to be sent to the caller
     * 
     * @param buildStatus status
     * @param writer servlet writer
     * @param query authorization query
     * @param decisionResult decision result
     * 
     * @throws XMLStreamException
     */
    private void buildResponse(String buildStatus, Writer writer, AuthzDecisionQuery query, String decisionResult) throws XMLStreamException {
        SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();
        SOAPEnvelope soapEnvelope = factory.createSOAPEnvelope();
        soapEnvelope.addAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema", null);
        soapEnvelope.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance", null);
        SOAPBody soapBody = factory.createSOAPBody(soapEnvelope);
        String strCurrentDate = "unknown";
        String issuerName = "unknown";
        try {
            strCurrentDate = getCurrentDate();
            InetAddress addr = InetAddress.getLocalHost();
            issuerName = addr.getHostName();
        } catch (Exception ex) {
            buildStatus = SAML_STATUS_CODE_RESPONDER;
            logger.error("Problems building response - will respond " + buildStatus, ex);
        }
        OMNamespace samlp = factory.createOMNamespace("urn:oasis:names:tc:SAML:2.0:protocol", "samlp");
        OMElement response = factory.createOMElement("Response", samlp, soapBody);
        OMNamespace saml = response.declareNamespace("urn:oasis:names:tc:SAML:2.0:assertion", "saml");
        response.addAttribute("ID", "foo1", null);
        response.addAttribute("Version", "2.0", null);
        response.addAttribute("IssueInstant", strCurrentDate, null);
        OMElement status = factory.createOMElement("Status", samlp, response);
        OMElement statusCode = factory.createOMElement("StatusCode", samlp, status);
        statusCode.addAttribute("Value", buildStatus, null);
        if (buildStatus == SAML_STATUS_CODE_SUCCESS) {
            OMElement assertion = factory.createOMElement("Assertion", saml, response);
            assertion.addAttribute("ID", "foo2", null);
            assertion.addAttribute("Version", "2.0", null);
            assertion.addAttribute("IssueInstant", strCurrentDate, null);
            OMElement issuer = factory.createOMElement("Issuer", saml, assertion);
            issuer.setText(issuerName);
            OMElement subject = factory.createOMElement("Subject", saml, assertion);
            OMElement nameID = factory.createOMElement("NameID", saml, subject);
            nameID.setText(query.getSubject());
            OMElement authzDecisionStatement = factory.createOMElement("AuthzDecisionStatement", saml, assertion);
            authzDecisionStatement.addAttribute("Decision", decisionResult, null);
            authzDecisionStatement.addAttribute("Resource", query.getResource(), null);
            OMElement action = factory.createOMElement("Action", saml, authzDecisionStatement);
            action.addAttribute("Namespace", query.getActionNamespace(), null);
            action.setText(query.getAction());
        }
        soapEnvelope.serializeAndConsume(writer);
    }

    /**
     * Checks the Object is not null
     * 
     * @param object the object instance
     */
    public static final void checkNotNull(final Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Gets current date as a String
     * 
     * @return the current date
     */
    private String getCurrentDate() {
        String strCurrentDate = null;
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            strCurrentDate = dateFormat.format(currentDate);
        } catch (Exception e) {
            logger.error("Date error: " + e);
        }
        return strCurrentDate;
    }

    /**
     * Sets the root authorization class that drives the authorization 
     * process
     * 
     * @throws ValveConfigurationException
     */
    public void setAuthorizationClass() throws ValveConfigurationException {
        if (authorizationProcessCls == null) {
            setAuthorizationProcessImpl(getValveConfig().getAuthorizationProcessImpl());
        }
    }

    /**
     * Sets the authorization process instance needed to process the request
     * 
     * @param authorizationProcessClsName the name of the authorization class
     *  
     */
    public void setAuthorizationProcessImpl(String authorizationProcessClsName) throws ValveConfigurationException {
        logger.debug("Setting authorizationProcessClsName: " + authorizationProcessClsName);
        this.authorizationProcessClsName = authorizationProcessClsName;
        if ((this.authorizationProcessClsName == null) || (this.authorizationProcessClsName.equals(""))) {
            throw new ValveConfigurationException("Configuration parameter [authorizationProcessImpl] has not been set correctly");
        }
        try {
            authorizationProcessCls = (AuthorizationProcessImpl) Class.forName(authorizationProcessClsName).newInstance();
            authorizationProcessCls.setValveConfiguration(getValveConfig());
        } catch (InstantiationException ie) {
            throw new ValveConfigurationException("Configuration parameter [authorizationProcessImpl] has not been set correctly - InstantiationException");
        } catch (IllegalAccessException iae) {
            throw new ValveConfigurationException("Configuration parameter [authorizationProcessImpl] has not been set correctly - IllegalAccessException");
        } catch (ClassNotFoundException cnfe) {
            throw new ValveConfigurationException("Configuration parameter [authorizationProcessImpl] has not been set correctly - ClassNotFoundException");
        }
    }

    /**
     * It calls the sendRequest method and sends the SAML 
     * response error depending on that
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param query authorization query
     * 
     * @return
     */
    protected String getDecision(HttpServletRequest request, HttpServletResponse response, AuthzDecisionQuery query) {
        logger.debug("SAMLAuthZ: getDecision");
        String decision = SAML_DECISION_INDETERMINATE;
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        try {
            statusCode = sendRequest(request, response, query.getResource(), query.getSubject());
            if (statusCode == -1) {
                decision = SAML_DECISION_INDETERMINATE;
            } else {
                if ((statusCode >= 200) && (statusCode < 300)) {
                    decision = SAML_DECISION_PERMIT;
                } else {
                    if ((statusCode >= 400) && (statusCode < 600)) {
                        decision = SAML_DECISION_DENY;
                    } else {
                        decision = SAML_DECISION_INDETERMINATE;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error getting the AuthZ decision: " + ex);
        }
        return decision;
    }

    /**
     * Calls the authorization class that matches with the URL and sends the 
     * HTTP error code back to the caller
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param url content url
     * @param userId user id
     * 
     * @return HTTP error code
     */
    protected int sendRequest(HttpServletRequest request, HttpServletResponse response, String url, String userId) {
        logger.debug("SAMLAuthZ: sendRequest");
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        String authCookieDomain = null;
        String authCookiePath = null;
        try {
            authCookieDomain = getValveConfig().getAuthCookieDomain();
            authCookiePath = getValveConfig().getAuthCookiePath();
            Cookie[] cookies = constructAuthNCookie(userId, authCookieDomain, authCookiePath);
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    logger.trace("BEFORE AuthZ: Request Cookie[" + i + "]: " + cookies[i].getName() + " - " + cookies[i].getDomain() + " - " + cookies[i].getValue());
                }
            } else {
                logger.debug("There is no cookie");
            }
            authorizationProcessCls.setValveConfiguration(getValveConfig());
            statusCode = authorizationProcessCls.authorize(request, response, cookies, url, null);
            logger.debug("Response status code is: " + statusCode);
        } catch (nonValidSessionException nvE) {
            logger.debug("Non valid session. Proceeding to logout");
            statusCode = ValveUtils.logout(request, response, null, getValveConfig());
            logger.debug("Setting the error code to: " + statusCode);
        } catch (Exception e) {
            logger.error("Authorization process raised exception: " + e.getMessage(), e);
            if (statusCode == 0) {
                statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            }
            logger.debug("Setting the error code to: " + statusCode);
        }
        return statusCode;
    }

    /**
     * Constructs the authentication cookie that simulates the main 
     * Security Framework's cookie
     * 
     * @param userId user id
     * @param authCookieDomain cookie domain
     * @param authCookiePath cookie path
     * 
     * @return authentication cookie
     */
    public Cookie[] constructAuthNCookie(String userId, String authCookieDomain, String authCookiePath) {
        logger.debug("SAMLAuthZ:constructAuthNCookie");
        Cookie[] cookies = null;
        try {
            Cookie authCookie = null;
            String authCookieName = getValveConfig().getAuthCookieName();
            int authMaxAge = new Integer(getValveConfig().getAuthMaxAge()).intValue();
            authCookie = new Cookie(authCookieName, userId);
            authCookie.setDomain(authCookieDomain);
            authCookie.setPath(authCookiePath);
            authCookie.setMaxAge(authMaxAge);
            logger.debug("AuthN cookie created: " + authCookieName + ":" + userId);
            cookies = new Cookie[1];
            cookies[0] = authCookie;
        } catch (Exception e) {
            logger.error("Error creating the authentication cookie" + e);
        }
        return cookies;
    }

    /**
     * Gets the Valve configuration instance
     * 
     * @return valve configuration
     */
    public ValveConfiguration getValveConfig() {
        ValveConfiguration valveConfig = null;
        try {
            valveConfig = ValveConfigurationInstance.getValveConfig();
        } catch (ValveConfigurationException e) {
            logger.debug("Config file instance is not readable: " + e.getMessage());
            valveConfig = readValveConfig();
        }
        return valveConfig;
    }

    /**
     * Reads the Valve config file in case it doesn't exist. This is used when 
     * the Valve Config instance is not readable
     * 
     * @return valve configuration
     */
    private ValveConfiguration readValveConfig() {
        ValveConfiguration valveConfig = null;
        try {
            String gsaValveConfigPath = ValveUtils.readValveConfigParameter();
            logger.debug("Reading config file... Config file located at: " + gsaValveConfigPath);
            valveConfig = ValveConfigurationInstance.getValveConfig(gsaValveConfigPath);
        } catch (ValveConfigurationException e) {
            logger.error("Configuration Exception when reading config file: " + e.getMessage(), e);
        }
        return valveConfig;
    }

    /**
     * Class that holds the authorization decision query to take a security 
     * decision to let the user access the resource
     * 
     */
    private static final class AuthzDecisionQuery {

        /**
         * Holds value of property id.
         */
        private String id;

        /**
         * Holds value of property resource.
         */
        private String resource;

        /**
         * Holds value of property subject.
         */
        private String subject;

        /**
         * Holds value of property action.
         */
        private String action;

        /**
         * Holds value of property actionNamespace.
         */
        private String actionNamespace;

        /**
         * Class constructor - default
         * 
         */
        public AuthzDecisionQuery() {
        }

        /**
         * Class contructor
         * 
         * @param id request id
         * @param resource url
         * @param subject user's subject
         * @param action action 
         * @param actionNamespace action namespace
         */
        public AuthzDecisionQuery(String id, String resource, String subject, String action, String actionNamespace) {
            this.id = id;
            this.resource = resource;
            this.subject = subject;
            this.action = action;
            this.actionNamespace = actionNamespace;
        }

        /**
         * Gets the id.
         * @return id.
         */
        public String getId() {
            return this.id;
        }

        /**
         * Sets the id.
         * @param id request id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets the user subject.
         *
         * @return user subject.
         */
        public String getSubject() {
            return this.subject;
        }

        /**
         * Sets the user subject.
         *
         * @param subject user subject
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         * Gets the resource.
         * @return resource.
         */
        public String getResource() {
            return this.resource;
        }

        /**
         * Sets the resource.
         * @param resource url.
         */
        public void setResource(String resource) {
            this.resource = resource;
        }

        /**
         * Gets the action.
         * @return action.
         */
        public String getAction() {
            return this.action;
        }

        /**
         * Sets the action.
         * @param action action.
         */
        public void setAction(String action) {
            this.action = action;
        }

        /**
         * Gets the actionNamespace.
         * @return action namespace.
         */
        public String getActionNamespace() {
            return this.actionNamespace;
        }

        /**
         * Sets the actionNamespace.
         * @param actionNamespace action namespace.
         */
        public void setActionNamespace(String actionNamespace) {
            this.actionNamespace = actionNamespace;
        }
    }
}
