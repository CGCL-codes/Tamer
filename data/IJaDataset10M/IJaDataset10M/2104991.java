package org.jboss.resteasy.examples.oauth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.Base64;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.xml.sax.InputSource;

public class EndUser {

    private static final String ConsumerWebAppURL;

    private static final String EndUserResourceURL;

    static {
        Properties props = new Properties();
        try {
            props.load(EndUser.class.getResourceAsStream("/oauth.properties"));
        } catch (Exception ex) {
            throw new RuntimeException("oauth.properties resource is not available");
        }
        ConsumerWebAppURL = props.getProperty("consumer.webapp.url");
        EndUserResourceURL = props.getProperty("enduser.resource.url");
    }

    public static void main(String[] args) throws Exception {
        EndUser user = new EndUser();
        String authorizationURI = user.requestServiceFromThirdPartyWebApp();
        String callback = user.authorizeConsumerRequestToken(authorizationURI);
        String endUserResourceData = user.setCallback(callback);
        System.out.println("Success : " + endUserResourceData);
        if (!"true".equals(System.getProperty("jetty"))) {
            accessEndUserResource("/resource1");
            accessEndUserResource("/resource2");
            accessEndUserResource("/invisible");
        }
    }

    private static void accessEndUserResource(String relativeURI) throws Exception {
        ClientRequest request = new ClientRequest(EndUserResourceURL + relativeURI);
        String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
        request.header("Authorization", "Basic " + base64Credentials);
        ClientResponse<String> response = null;
        try {
            response = request.get(String.class);
            if ("/invisible".equals(relativeURI)) {
                if (response.getStatus() != 401) {
                    throw new RuntimeException("End user can access the invisible resource");
                } else {
                    return;
                }
            }
            if (HttpResponseCodes.SC_OK != response.getStatus()) {
                throw new RuntimeException("End user can not access its own resources");
            }
            System.out.println("End user resource : " + response.getEntity());
        } finally {
            response.releaseConnection();
        }
    }

    /**
    * End user requests that a well-known 3rd party web application
    * does something useful on its behalf
    * @return authorizationURI where the end user has been redirected for
    *         the consumer temporarily request token be authorized
    * @throws Exception
    */
    public String requestServiceFromThirdPartyWebApp() throws Exception {
        String url = ConsumerWebAppURL + "?scope=" + URLEncoder.encode(EndUserResourceURL, "UTF-8");
        ClientRequest request = new ClientRequest(url);
        request.followRedirects(false);
        ClientResponse<?> response = request.get();
        response.releaseConnection();
        if (302 != response.getStatus()) {
            throw new RuntimeException("Service request has failed - redirection is expected");
        }
        String authorizationURI = response.getHeaders().getFirst("Location");
        if (authorizationURI == null) {
            throw new RuntimeException("Token authorization URI is missing");
        }
        return authorizationURI;
    }

    /**
    * End user follows the redirection by going to the authorizationURI 
    * provided by a 3rd party consumer
    * @param url the token authorization URI
    * @return the 3rd party callback URI where the authorization verifier needs to posted to
    * @throws Exception
    */
    public String authorizeConsumerRequestToken(String url) throws Exception {
        ClientRequest request = new ClientRequest(url);
        request.header("Accept", "application/xml");
        String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
        request.header("Authorization", "Basic " + base64Credentials);
        ClientResponse<String> response = null;
        try {
            response = request.get(String.class);
            if (200 != response.getStatus()) {
                throw new RuntimeException("No authorization request data is available");
            }
            String body = response.getEntity();
            String consumerId = evaluateBody(new ByteArrayInputStream(body.getBytes()), "/ns:tokenAuthorizationRequest/ns:consumerId/text()");
            String consumerName = evaluateBody(new ByteArrayInputStream(body.getBytes()), "/ns:tokenAuthorizationRequest/ns:consumerName/text()");
            String requestScope = evaluateBody(new ByteArrayInputStream(body.getBytes()), "/ns:tokenAuthorizationRequest/ns:scopes/text()");
            String requestPermission = evaluateBody(new ByteArrayInputStream(body.getBytes()), "/ns:tokenAuthorizationRequest/ns:permissions/text()");
            String message = "Authorize " + ("".equals(consumerName) ? consumerId : consumerName) + System.getProperty("line.separator") + " to access " + ("".equals(requestScope) ? "your resources" : requestScope) + (requestPermission == null ? "" : (System.getProperty("line.separator") + " and grant the following permissions : \"" + requestPermission + "\"")) + " (yes/no) ?";
            String decision = JOptionPane.showInputDialog(message);
            if (decision == null || !"yes".equalsIgnoreCase(decision)) {
                decision = "no";
            }
            String replyTo = evaluateBody(new ByteArrayInputStream(body.getBytes()), "/ns:tokenAuthorizationRequest/@replyTo");
            replyTo += "&xoauth_end_user_decision=" + decision.toLowerCase();
            return confirmAuthorization(replyTo);
        } finally {
            response.releaseConnection();
        }
    }

    private String evaluateBody(InputStream body, String expression) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceContextImpl(Collections.singletonMap("ns", "http://org.jboss.com/resteasy/oauth")));
        try {
            Object result = (Object) xpath.evaluate(expression, new InputSource(body), XPathConstants.STRING);
            return (String) result;
        } catch (XPathExpressionException ex) {
            throw new IllegalArgumentException("Illegal XPath expression '" + expression + "'", ex);
        }
    }

    public String confirmAuthorization(String url) throws Exception {
        ClientRequest request = new ClientRequest(url);
        String base64Credentials = new String(Base64.encodeBytes("admin:admin".getBytes()));
        request.header("Authorization", "Basic " + base64Credentials);
        ClientResponse<String> response = null;
        try {
            response = request.post(String.class);
            if (302 != response.getStatus()) {
                throw new RuntimeException("Initiation failed");
            }
            String callbackURI = response.getHeaders().getFirst("Location");
            if (callbackURI == null) {
                throw new RuntimeException("Callback failed");
            }
            return callbackURI;
        } finally {
            response.releaseConnection();
        }
    }

    public String setCallback(String url) throws Exception {
        ClientRequest request = new ClientRequest(url);
        ClientResponse<String> response = null;
        try {
            response = request.post(String.class);
            if (200 != response.getStatus()) {
                throw new RuntimeException("Service failed");
            }
            return response.getEntity();
        } finally {
            response.releaseConnection();
        }
    }

    private static class NamespaceContextImpl implements NamespaceContext {

        private Map<String, String> namespaces;

        public NamespaceContextImpl(Map<String, String> namespaces) {
            this.namespaces = namespaces;
        }

        public String getNamespaceURI(String prefix) {
            return namespaces.get(prefix);
        }

        public String getPrefix(String namespace) {
            for (Map.Entry<String, String> entry : namespaces.entrySet()) {
                if (entry.getValue().equals(namespace)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public Iterator getPrefixes(String namespace) {
            String prefix = namespaces.get(namespace);
            if (prefix == null) {
                return null;
            }
            return Collections.singletonList(prefix).iterator();
        }
    }
}
