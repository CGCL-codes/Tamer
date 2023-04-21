package org.openid4java.samples.consumerservlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;

/**
 * @author Adapted from code by Sutra Zhou
 * 
 */
public class ConsumerServlet extends javax.servlet.http.HttpServlet {

    private static final long serialVersionUID = -5998885243419513055L;

    private final Log log = LogFactory.getLog(this.getClass());

    private ServletContext context;

    private ConsumerManager manager;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        log.debug("context: " + context);
        try {
            this.manager = new ConsumerManager();
            manager.setAssociations(new InMemoryConsumerAssociationStore());
            manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        } catch (ConsumerException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("true".equals(req.getParameter("is_return"))) {
            processReturn(req, resp);
        } else {
            String identifier = req.getParameter("openid_identifier");
            if (identifier != null) {
                this.authRequest(identifier, req, resp);
            } else {
                this.getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
            }
        }
    }

    private void processReturn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Identifier identifier = null;
        boolean loggedIn = "true".equals(req.getSession().getAttribute("loggedIn"));
        if (!loggedIn) identifier = this.verifyResponse(req);
        log.debug("identifier: " + identifier);
        if (identifier == null && !loggedIn) {
            System.out.println("A &&&&& identifiyer = " + identifier);
            this.getServletContext().getRequestDispatcher("/error.jsp?err=" + req.getParameter("openid.error")).forward(req, resp);
        } else {
            if (identifier != null && identifier.getIdentifier() != null) req.setAttribute("identifier", identifier.getIdentifier());
            req.getSession().setAttribute("loggedIn", "true");
            this.getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("return")).forward(req, resp);
        }
    }

    public String authRequest(String userSuppliedString, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException, ServletException {
        try {
            String fullURL = httpReq.getRequestURL().toString();
            String returnToUrl = fullURL + "?is_return=true";
            List discoveries = manager.discover(userSuppliedString);
            DiscoveryInformation discovered = manager.associate(discoveries);
            httpReq.getSession().setAttribute("openid-disc", discovered);
            String realm = null;
            int k = fullURL.indexOf("://");
            int l = fullURL.indexOf("/", k + 3);
            if (l > -1) realm = fullURL.substring(0, l + 1); else realm = fullURL + "/";
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl, realm);
            FetchRequest fetch = FetchRequest.createFetchRequest();
            SRegRequest sregReq = SRegRequest.createFetchRequest();
            if ("1".equals(httpReq.getParameter("nickname"))) {
                sregReq.addAttribute("nickname", false);
            }
            if ("1".equals(httpReq.getParameter("email"))) {
                fetch.addAttribute("email", "http://schema.openid.net/contact/email", false);
                sregReq.addAttribute("email", false);
            }
            if ("1".equals(httpReq.getParameter("fullname"))) {
                fetch.addAttribute("fullname", "http://schema.openid.net/contact/fullname", false);
                sregReq.addAttribute("fullname", false);
            }
            if ("1".equals(httpReq.getParameter("dob"))) {
                fetch.addAttribute("dob", "http://schema.openid.net/contact/dob", true);
                sregReq.addAttribute("dob", false);
            }
            if ("1".equals(httpReq.getParameter("gender"))) {
                fetch.addAttribute("gender", "http://schema.openid.net/contact/gender", false);
                sregReq.addAttribute("gender", false);
            }
            if ("1".equals(httpReq.getParameter("postcode"))) {
                fetch.addAttribute("postcode", "http://schema.openid.net/contact/postcode", false);
                sregReq.addAttribute("postcode", false);
            }
            if ("1".equals(httpReq.getParameter("country"))) {
                fetch.addAttribute("country", "http://schema.openid.net/contact/country", false);
                sregReq.addAttribute("country", false);
            }
            if ("1".equals(httpReq.getParameter("language"))) {
                fetch.addAttribute("language", "http://schema.openid.net/contact/language", false);
                sregReq.addAttribute("language", false);
            }
            if ("1".equals(httpReq.getParameter("timezone"))) {
                fetch.addAttribute("timezone", "http://schema.openid.net/contact/timezone", false);
                sregReq.addAttribute("timezone", false);
            }
            if (!sregReq.getAttributes().isEmpty()) {
                authReq.addExtension(sregReq);
            }
            String key = getServletConfig().getInitParameter("key");
            authReq.set("openid.oauth.consumer", key);
            authReq.set("openid.ns.oauth", "http://specs.openid.net/extensions/oauth/1.0");
            if (!discovered.isVersion2()) {
                httpResp.sendRedirect(authReq.getDestinationUrl(true));
                return null;
            } else {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/hybrid/formredirection.jsp");
                httpReq.setAttribute("prameterMap", httpReq.getParameterMap());
                httpReq.setAttribute("message", authReq);
                dispatcher.forward(httpReq, httpResp);
            }
        } catch (OpenIDException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Identifier verifyResponse(HttpServletRequest httpReq) {
        try {
            ParameterList response = new ParameterList(httpReq.getParameterMap());
            DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession().getAttribute("openid-disc");
            StringBuffer receivingURL = httpReq.getRequestURL();
            String queryString = httpReq.getQueryString();
            if (queryString != null && queryString.length() > 0) receivingURL.append("?").append(httpReq.getQueryString());
            VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);
            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
                if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
                    MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
                    if (ext instanceof SRegResponse) {
                        SRegResponse sregResp = (SRegResponse) ext;
                        for (Iterator iter = sregResp.getAttributeNames().iterator(); iter.hasNext(); ) {
                            String name = (String) iter.next();
                            String value = sregResp.getParameterValue(name);
                            httpReq.setAttribute(name, value);
                        }
                    }
                }
                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                    FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
                    List aliases = fetchResp.getAttributeAliases();
                    for (Iterator iter = aliases.iterator(); iter.hasNext(); ) {
                        String alias = (String) iter.next();
                        List values = fetchResp.getAttributeValues(alias);
                        if (values.size() > 0) {
                            log.debug(alias + " : " + values.get(0));
                            httpReq.setAttribute(alias, values.get(0));
                        }
                    }
                }
                return verified;
            }
        } catch (OpenIDException e) {
            e.printStackTrace();
        }
        return null;
    }
}
