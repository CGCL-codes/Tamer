package org.ourgrid.peer.controller.voms;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.ourgrid.peer.PeerConfiguration;
import org.ourgrid.peer.controller.messages.VOMSMessages;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.network.signature.SignatureProperties;
import br.edu.ufcg.lsd.commune.network.signature.Util;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 *
 */
public class VomsAuthorisationStrategy {

    private static final String DN_XMLTAG = "DN";

    private static final String SSL_PROTOCOL = "SSLv3";

    private static final String VOMS_URL_PROT = "https://";

    private static final String VOMS_SERVICE_URL = "/services/VOMSAdmin?method=listMembers";

    private static final String COMMUNE_DN_SEPARATOR = ",";

    private static final String VOMS_DN_SEPARATOR = "/";

    private final ServiceManager serviceManager;

    public VomsAuthorisationStrategy(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public VomsAuthorisationData authorise(String providerDN) throws Exception {
        List<String> vomsURLList = serviceManager.getContainerContext().parseStringListProperty(PeerConfiguration.PROP_VOMS_URL);
        if (vomsURLList.isEmpty()) {
            return new VomsAuthorisationData(true, new ArrayList<String>());
        }
        List<String> allDNs = new ArrayList<String>();
        boolean authorised = false;
        for (String vomsURL : vomsURLList) {
            List<String> providerDns = getAuthorisedDNList(vomsURL);
            for (String vomsProviderDN : providerDns) {
                allDNs.add(vomsProviderDN);
                authorised |= dnEquals(providerDN, vomsProviderDN);
            }
        }
        return new VomsAuthorisationData(authorised, allDNs);
    }

    public class VomsAuthorisationData {

        private final boolean isAuthorised;

        private final List<String> usersDN;

        public VomsAuthorisationData(boolean isAuthorised, List<String> usersDN) {
            this.isAuthorised = isAuthorised;
            this.usersDN = usersDN;
        }

        /**
		 * @return the isAuthorised
		 */
        public boolean isAuthorised() {
            return isAuthorised;
        }

        /**
		 * @return the usersDN
		 */
        public List<String> getUsersDN() {
            return usersDN;
        }
    }

    private List<String> getAuthorisedDNList(String vomsURL) throws Exception, MalformedURLException, NoSuchAlgorithmException, KeyManagementException, IOException, SAXException {
        if (vomsURL == null) {
            throw new Exception(VOMSMessages.getNullVOMSUrlMessage());
        }
        URL url = new URL(VOMS_URL_PROT + vomsURL + VOMS_SERVICE_URL);
        HttpsURLConnection connection = setUpConnection(url);
        InputStream inputStream = connection.getInputStream();
        List<String> providerDns = parseXML(inputStream);
        return providerDns;
    }

    public static boolean dnEquals(String providerDN, String vomsProviderDN) {
        Set<String> providerDNSet = new HashSet<String>(Arrays.asList(providerDN.split(COMMUNE_DN_SEPARATOR)));
        Set<String> vomsProviderDNSet = new HashSet<String>(Arrays.asList(vomsProviderDN.substring(1).split(VOMS_DN_SEPARATOR)));
        return providerDNSet.equals(vomsProviderDNSet);
    }

    private List<String> parseXML(InputStream xMLStream) throws SAXException, IOException {
        InputSource source = new InputSource(xMLStream);
        DOMParser parser = new DOMParser();
        parser.parse(source);
        Document xMLDocument = parser.getDocument();
        List<String> allDns = new LinkedList<String>();
        NodeList dnNodes = xMLDocument.getElementsByTagName(DN_XMLTAG);
        for (int i = 0; i < dnNodes.getLength(); i++) {
            Node item = dnNodes.item(i);
            allDns.add(item.getFirstChild().getNodeValue());
        }
        return allDns;
    }

    private HttpsURLConnection setUpConnection(URL url) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        HttpsURLConnection openConnection = (HttpsURLConnection) url.openConnection();
        openConnection.setAllowUserInteraction(true);
        openConnection.setUseCaches(false);
        openConnection.setDoInput(true);
        openConnection.setDoOutput(true);
        SSLContext sc = SSLContext.getInstance(SSL_PROTOCOL);
        sc.init(new KeyManager[] { new MyKeyManager() }, new TrustManager[] { new BypassTrustManager() }, null);
        openConnection.setSSLSocketFactory(sc.getSocketFactory());
        return openConnection;
    }

    private class MyKeyManager implements X509KeyManager {

        private static final String CLIENT_ALIAS = "Client";

        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            return CLIENT_ALIAS;
        }

        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            return null;
        }

        public X509Certificate[] getCertificateChain(String alias) {
            return serviceManager.getMyCertPath().getCertificates().toArray(new X509Certificate[] {});
        }

        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return null;
        }

        public PrivateKey getPrivateKey(String alias) {
            try {
                return Util.decodePrivateKey(serviceManager.getContainerContext().getProperty(SignatureProperties.PROP_PRIVATE_KEY));
            } catch (InvalidKeySpecException e) {
                return null;
            }
        }

        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return null;
        }
    }

    private class BypassTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
