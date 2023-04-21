package com.google.code.facebookapi;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A FacebookRestClient that uses the XML result format. This means results from calls to the Facebook API are returned as XML and transformed into instances of
 * {@link org.w3c.dom.Document}.
 */
public abstract class FacebookXmlRestClientBase extends SpecificReturnTypeAdapter<Document> {

    protected static Log log = LogFactory.getLog(FacebookXmlRestClientBase.class);

    protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public boolean isNamespaceAware() {
        return factory.isNamespaceAware();
    }

    public void setNamespaceAware(boolean v) {
        factory.setNamespaceAware(v);
    }

    public FacebookXmlRestClientBase(ExtensibleClient client) {
        super("xml", client);
        factory.setNamespaceAware(true);
    }

    public FacebookXmlRestClientBase(String apiKey, String secret) {
        this(new ExtensibleClient("xml", apiKey, secret));
    }

    public FacebookXmlRestClientBase(String apiKey, String secret, String sessionKey) {
        this(new ExtensibleClient("xml", apiKey, secret, sessionKey));
    }

    public FacebookXmlRestClientBase(String apiKey, String secret, String sessionKey, boolean sessionSecret) {
        this(new ExtensibleClient("xml", apiKey, secret, sessionKey, sessionSecret));
    }

    protected Document parseCallResult(Object rawResponse) throws FacebookException {
        log.debug("Facebook response:  " + rawResponse);
        return XmlHelper.parseCallResult(rawResponse, factory);
    }

    /**
	 * Prints out the DOM tree.
	 */
    public void printDom(Node n, String prefix) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("\n");
            XmlHelper.printDom(n, prefix, sb);
            log.debug(sb.toString());
        }
    }

    /**
	 * Executes a batch of queries. You define the queries to execute by calling 'beginBatch' and then invoking the desired API methods that you want to execute as part
	 * of your batch as normal. Invoking this method will then execute the API calls you made in the interim as a single batch query.
	 * 
	 * @param serial
	 *            set to true, and your batch queries will always execute serially, in the same order in which your specified them. If set to false, the Facebook API
	 *            server may execute your queries in parallel and/or out of order in order to improve performance.
	 * 
	 * @return a list containing the results of the batch execution. The list will be ordered such that the first element corresponds to the result of the first query in
	 *         the batch, and the second element corresponds to the result of the second query, and so on. The types of the objects in the list will match the type
	 *         normally returned by the API call being invoked (so calling users_getLoggedInUser as part of a batch will place a Long in the list, and calling friends_get
	 *         will place a Document in the list, etc.).
	 * 
	 *         The list may be empty, it will never be null.
	 * 
	 * @throws FacebookException
	 * @throws IOException
	 */
    public List<Document> executeBatch(boolean serial) throws FacebookException {
        List<String> clientResults = client.executeBatch(serial);
        List<Document> result = new ArrayList<Document>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (String clientResult : clientResults) {
                Document doc = builder.parse(new InputSource(new StringReader(clientResult)));
                NodeList responses = doc.getElementsByTagName("batch_run_response_elt");
                for (int count = 0; count < responses.getLength(); count++) {
                    Node responseNode = responses.item(count);
                    Document respDoc = builder.newDocument();
                    responseNode = respDoc.importNode(responseNode, true);
                    respDoc.appendChild(responseNode);
                    try {
                        respDoc = XmlHelper.parseCallResult(respDoc);
                        result.add(respDoc);
                    } catch (FacebookException ignored) {
                        result.add(null);
                    }
                }
            }
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException("Error parsing batch response", ex);
        } catch (SAXException ex) {
            throw new RuntimeException("Error parsing batch response", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Error parsing batch response", ex);
        }
        return result;
    }
}
