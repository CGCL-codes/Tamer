package org.tridentproject.repository.vocab;

import org.apache.commons.configuration.Configuration;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.net.URLEncoder;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.jaxen.SimpleNamespaceContext;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * VocabEngine is the class for updating vocabulary lists for items from a repository
 *
 **/
public class VocabEngine {

    private static Logger log = Logger.getLogger(VocabEngine.class);

    private String strRepoLoc = null;

    private String strVocabListLoc = null;

    private DocumentFactory df = DocumentFactory.getInstance();

    private HashMap<String, String> URIMap = new HashMap<String, String>();

    /**
     * The configure method reads indexing configuration from a Configuration
     * and initializes the IndexEngine
     *
     * @param config - configuration containing information about how to index an item
     *
     **/
    public void configure(Configuration config) {
        strVocabListLoc = config.getString("location");
        strRepoLoc = config.getString("repository.url");
    }

    /**
     * Updates a single item and returns a collection of item identifiers for items that require further updating.
     * The updating process will post updates to the appropriate vocab lists
     *
     * @param itemid - the item to be updated
     *
     * @return a Collection of items needing updating
     *
     **/
    public Collection<String> vocab(String itemid) {
        Collection<String> needsUpdating = new HashSet<String>();
        String item = getItemFromRepository(itemid);
        if (item == null) {
        } else {
            log.debug("retrieved item from repository, will now attempt to retrieve metadataform");
            Document metadataform = getResource(item, "/metadataform");
            log.debug("got metadata form");
            List<Update> updates = getAllUpdates(metadataform);
            for (Update update : updates) {
                putUpdate(update);
            }
        }
        return needsUpdating;
    }

    private String getItemFromRepository(String strItemId) {
        log.debug("getItemFromRepository(" + strItemId + ")");
        String item = null;
        HttpClient client = new HttpClient();
        String strUrl = strRepoLoc + "/items/" + strItemId;
        log.debug("about to get url: " + strUrl);
        GetMethod method = new GetMethod(strUrl);
        try {
            int statusCode = client.executeMethod(method);
            log.debug("Response: " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                item = strItemId;
            }
        } catch (Exception e) {
            log.warn("Error retrieving item from repository: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }
        return item;
    }

    private List<Update> getAllUpdates(Document doc) {
        List<Update> updates = new ArrayList<Update>();
        List matchingNodes = getXPath("//element[@update='true']").selectNodes(doc);
        for (Iterator iter = matchingNodes.iterator(); iter.hasNext(); ) {
            Element elem = (Element) iter.next();
            String vocablist = elem.attributeValue("values");
            String term = elem.getText();
            updates.add(new Update(vocablist, term));
            log.debug("adding (" + vocablist + ":" + term + ") to be updated");
        }
        return updates;
    }

    private Document getResource(String item, String resourceURL) {
        Document doc = null;
        String strURL = strRepoLoc + "/items/" + item + resourceURL;
        log.debug("Getting resource from repository: " + strURL);
        try {
            doc = new SAXReader().read(strURL);
            if (doc != null) log.debug(doc.asXML());
        } catch (Exception e) {
            log.warn("Unable to retrieve resource: " + strURL + ": " + e.getMessage());
        }
        return doc;
    }

    private void putUpdate(Update update) {
        if (update.vocablist != null && update.term != null && update.vocablist.length() > 0 && update.term.length() > 0) {
            PutMethod method = null;
            try {
                update.term = update.term.replaceAll(" ", "%20");
                String strUrl = strVocabListLoc + "/lists/" + update.vocablist + "/terms/" + update.term + "/";
                HttpClient client = new HttpClient();
                method = new PutMethod(strUrl);
                log.debug("sending put to vocab list: " + strUrl);
                int statusCode = client.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                    log.error("Error updating vocab list, response = " + statusCode);
                    log.error("Error response from vocab list: " + new String(method.getResponseBody()));
                }
            } catch (Exception e) {
                log.error("Error updating vocab list: " + e.getMessage());
            } finally {
                if (method != null) method.releaseConnection();
            }
        }
    }

    private XPath getXPath(String s) {
        XPath xpathSelector = df.createXPath(s);
        xpathSelector.setNamespaceContext(new SimpleNamespaceContext(URIMap));
        return xpathSelector;
    }

    public class Update {

        public String vocablist = null;

        public String term = null;

        public Update(String vocablist, String term) {
            this.vocablist = vocablist;
            this.term = term;
        }
    }
}
