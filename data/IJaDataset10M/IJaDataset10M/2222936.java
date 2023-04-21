package de.fmui.cmis.test.client.soap;

import java.net.URL;
import java.text.SimpleDateFormat;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.MTOMFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.WSBindingProvider;
import de.fmui.cmis.fileshare.jaxb.NavigationService;
import de.fmui.cmis.fileshare.jaxb.NavigationServicePort;
import de.fmui.cmis.fileshare.jaxb.ObjectService;
import de.fmui.cmis.fileshare.jaxb.ObjectServicePort;
import de.fmui.cmis.fileshare.jaxb.RepositoryService;
import de.fmui.cmis.fileshare.jaxb.RepositoryServicePort;
import de.fmui.cmis.test.client.ICMISClient;

public class CMISSOAPClient implements ICMISClient {

    public static final String CMIS_NAMESPACE = "http://docs.oasis-open.org/ns/cmis/ws/200908/";

    public static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    public static final String WSU_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    public static final String REPOSITORY_SERVICE = "RepositoryService";

    public static final String OBJECT_SERVICE = "ObjectService";

    public static final String DISCOVERY_SERVICE = "DiscoveryService";

    public static final String NAVIGATION_SERVICE = "NavigationService";

    public static final String MULTIFILING_SERVICE = "MulifilingService";

    public static final String VERSIONING_SERVICE = "VersioningService";

    public static final String RELATIONSHIP_SERVICE = "RelationshipService";

    private RepositoryServicePort fRepositoryService;

    private NavigationServicePort fNavigationService;

    private ObjectServicePort fObjectService;

    public CMISSOAPClient(String urlPrefix, String user, String password) throws Exception {
        RepositoryService repService = new RepositoryService(new URL(urlPrefix + REPOSITORY_SERVICE + "?wsdl"), new QName(CMIS_NAMESPACE, REPOSITORY_SERVICE));
        fRepositoryService = repService.getRepositoryServicePort(new MTOMFeature());
        if (user != null) {
            addWSSecHeader(((WSBindingProvider) fRepositoryService), user, password);
        }
        NavigationService navService = new NavigationService(new URL(urlPrefix + NAVIGATION_SERVICE + "?wsdl"), new QName(CMIS_NAMESPACE, NAVIGATION_SERVICE));
        fNavigationService = navService.getNavigationServicePort(new MTOMFeature());
        if (user != null) {
            addWSSecHeader(((WSBindingProvider) fNavigationService), user, password);
        }
        ObjectService objService = new ObjectService(new URL(urlPrefix + OBJECT_SERVICE + "?wsdl"), new QName(CMIS_NAMESPACE, OBJECT_SERVICE));
        fObjectService = objService.getObjectServicePort(new MTOMFeature());
        if (user != null) {
            addWSSecHeader(((WSBindingProvider) fObjectService), user, password);
        }
    }

    public RepositoryServicePort getRepositoryService() {
        return fRepositoryService;
    }

    public NavigationServicePort getNavigationService() {
        return fNavigationService;
    }

    public ObjectServicePort getObjectService() {
        return fObjectService;
    }

    /**
	 * Sets an <code>UsernameToken</code> WS-Security header.
	 */
    private static void addWSSecHeader(WSBindingProvider provider, String user, String password) throws ParserConfigurationException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long time = System.currentTimeMillis();
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element wsseSecurityElement = document.createElementNS(WSSE_NAMESPACE, "Security");
        Element wsuTimestampElement = document.createElementNS(WSU_NAMESPACE, "Timestamp");
        wsseSecurityElement.appendChild(wsuTimestampElement);
        Element tsCreatedElement = document.createElementNS(WSU_NAMESPACE, "Created");
        tsCreatedElement.setTextContent(sdf.format(time));
        wsuTimestampElement.appendChild(tsCreatedElement);
        Element tsExpiresElement = document.createElementNS(WSU_NAMESPACE, "Expires");
        tsExpiresElement.setTextContent(sdf.format(time + 24 * 60 * 60 * 1000));
        wsuTimestampElement.appendChild(tsExpiresElement);
        Element usernameTokenElement = document.createElementNS(WSSE_NAMESPACE, "UsernameToken");
        wsseSecurityElement.appendChild(usernameTokenElement);
        Element usernameElement = document.createElementNS(WSSE_NAMESPACE, "Username");
        usernameElement.setTextContent(user);
        usernameTokenElement.appendChild(usernameElement);
        Element passwordElement = document.createElementNS(WSSE_NAMESPACE, "Password");
        passwordElement.setTextContent(password);
        passwordElement.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        usernameTokenElement.appendChild(passwordElement);
        Element createdElement = document.createElementNS(WSU_NAMESPACE, "Created");
        createdElement.setTextContent(sdf.format(time));
        usernameTokenElement.appendChild(createdElement);
        Header header = Headers.create(wsseSecurityElement);
        provider.setOutboundHeaders(header);
    }
}
