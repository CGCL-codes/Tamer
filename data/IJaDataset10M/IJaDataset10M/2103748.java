package net.vinant.idp4java.openid4javaImpl;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;

/**
 * Sample Consumer (Relying Party) implementation.
 */
public class SimpleConsumer {

    private ConsumerManager manager;

    private String returnToUrl;

    public SimpleConsumer() throws ConsumerException {
        this("http://localhost:8081/idp4java/login");
    }

    public SimpleConsumer(String returnToUrl) throws ConsumerException {
        this.returnToUrl = returnToUrl;
        manager = new ConsumerManager();
        manager.setAssociations(new InMemoryConsumerAssociationStore());
        manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
        manager.getRealmVerifier().setEnforceRpId(false);
    }

    public String authRequest(String userSuppliedString, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        try {
            List discoveries = manager.discover(userSuppliedString);
            DiscoveryInformation discovered = manager.associate(discoveries);
            httpReq.getSession().setAttribute("openid-disc", discovered);
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);
            FetchRequest fetch = FetchRequest.createFetchRequest();
            fetch.addAttribute("email", "http://schema.openid.net/contact/email", true);
            authReq.addExtension(fetch);
            SRegRequest sregReq = SRegRequest.createFetchRequest();
            sregReq.addAttribute("email", true);
            authReq.addExtension(sregReq);
            if (!discovered.isVersion2()) {
                httpResp.sendRedirect(authReq.getDestinationUrl(true));
                return null;
            } else {
                httpResp.sendRedirect(authReq.getDestinationUrl(true));
                return null;
            }
        } catch (OpenIDException e) {
            throw new RuntimeException("wrap:" + e.getMessage(), e);
        }
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
                HttpSession session = httpReq.getSession(true);
                session.setAttribute("openid_identifier", authSuccess.getIdentity());
                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                    FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
                    session.setAttribute("emailFromFetch", fetchResp.getAttributeValues("email").get(0));
                }
                if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
                    SRegResponse sregResp = (SRegResponse) authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
                    session.setAttribute("emailFromSReg", sregResp.getAttributeValue("email"));
                }
                return verified;
            }
        } catch (OpenIDException e) {
            throw new RuntimeException("wrap:" + e.getMessage(), e);
        }
        return null;
    }
}
