package org.wijiscommons.ssaf.search.service.client;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.w3c.dom.Element;
import org.wijiscommons.ssaf.search.FatalRecordRetrievalFault;
import org.wijiscommons.ssaf.search.FatalSearchFault;
import org.wijiscommons.ssaf.search.SearchService;
import org.wijiscommons.ssaf.search.SearchService_Service;
import org.wijiscommons.ssaf.search.wrapper.RecordRetrieval;
import org.wijiscommons.ssaf.search.wrapper.RecordRetrievalResponse;
import org.wijiscommons.ssaf.search.wrapper.Search;
import org.wijiscommons.ssaf.search.wrapper.SearchResponse;

/**
 * 
 * This class is a spring configured javaClient for Search and RecordRetrieval web service.
 * It has a bean called "webServiceClient" in applicationContext.xml.
 */
public class SearchAndRecordRetrievalClient {

    private static final Log log = LogFactory.getLog(SearchAndRecordRetrievalClient.class);

    private static final QName SERVICE_NAME = new QName("http://wijiscommons.org/ssaf/search/", "SearchService");

    /**
     *  Spring injected properties
     */
    private String trustStoreFilePath;

    private String trustStorePassword;

    private String keyStoreFilePath;

    private String keyStorePassword;

    private String endpointURL;

    public static SearchService port;

    public SearchAndRecordRetrievalClient() {
        super();
    }

    public SearchAndRecordRetrievalClient(String wsdlLocation) throws Exception {
        this();
        URL wsdlURL = null;
        try {
            wsdlURL = new URL("file", "", wsdlLocation);
        } catch (Exception e) {
            log.error("Caught Exception while creating URL object : " + e.getMessage());
        }
        SearchService_Service searchService = new SearchService_Service(wsdlURL, SERVICE_NAME);
        port = searchService.getSearch();
    }

    /**
     * JavaClient for Search web service
     * @param searchRequestDocumentElement
     * @return SearchResponse
     */
    public SearchResponse search(Element searchRequestDocumentElement) throws FatalSearchFault {
        log.info("Invoking search web service...");
        SearchResponse searchReponse = null;
        SearchService port = sendCertAlong();
        Search search = new Search();
        search.setAny(searchRequestDocumentElement);
        if (port != null) {
            searchReponse = port.search(search);
        } else {
            log.error("Search service port is null");
        }
        if (searchReponse == null) {
            System.out.println("SearchReponse returned for SSAFSolr is null");
        }
        return searchReponse;
    }

    /**
     * JavaClient for RecordRetrieval web service
     * @param recordRetrievalRequestDocElement
     * @return RecordRetrievalResponse
     */
    public RecordRetrievalResponse retrieve(Element recordRetrievalRequestDocElement) throws FatalRecordRetrievalFault {
        log.info("Invoking recordRetrieval web service...");
        RecordRetrievalResponse recordRetrievalResponse = null;
        RecordRetrieval recordRetrievalRequest = new RecordRetrieval();
        recordRetrievalRequest.setAny(recordRetrievalRequestDocElement);
        SearchService port = sendCertAlong();
        if (port != null) {
            recordRetrievalResponse = port.recordRetrieval(recordRetrievalRequest);
        } else {
            log.error("Retrieve service port is null");
        }
        return recordRetrievalResponse;
    }

    /**
	 * Method to configure client certs for web service invocation.
	 * @return
	 */
    private SearchService sendCertAlong() {
        try {
            HTTPConduit conduit = null;
            if (port != null) {
                Client client = ClientProxy.getClient(port);
                conduit = (HTTPConduit) client.getConduit();
            }
            TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setSecureSocketProtocol("SSLv3");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            String trustpass = trustStorePassword;
            File truststore = new File(trustStoreFilePath);
            keyStore.load(new FileInputStream(truststore), trustpass.toCharArray());
            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(keyStore);
            TrustManager[] tm = trustFactory.getTrustManagers();
            tlsParams.setTrustManagers(tm);
            File keystore = new File(keyStoreFilePath);
            keyStore.load(new FileInputStream(keystore), keyStorePassword.toCharArray());
            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(keyStore, keyStorePassword.toCharArray());
            KeyManager[] km = keyFactory.getKeyManagers();
            tlsParams.setKeyManagers(km);
            conduit.setTlsClientParameters(tlsParams);
        } catch (Exception e) {
            log.error("Caught Exception : " + e.getMessage());
        }
        return port;
    }

    public String getTrustStoreFilePath() {
        return trustStoreFilePath;
    }

    public void setTrustStoreFilePath(String trustStoreFilePath) {
        this.trustStoreFilePath = trustStoreFilePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public void setKeyStoreFilePath(String keyStoreFilePath) {
        this.keyStoreFilePath = keyStoreFilePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getEndpointURL() {
        return endpointURL;
    }

    public void setEndpointURL(String endpointURL) {
        BindingProvider bindingProvider = (BindingProvider) port;
        this.endpointURL = endpointURL;
        if (bindingProvider != null) {
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        }
    }

    public void setWsdlLocation(String wsdlLocation) {
        URL wsdlURL = null;
        try {
            wsdlURL = new URL("file", "", wsdlLocation);
        } catch (Exception e) {
            log.error("Caught Exception while creating URL object : " + e.getMessage());
        }
        SearchService_Service searchService = new SearchService_Service(wsdlURL, SERVICE_NAME);
        port = searchService.getSearch();
    }
}
