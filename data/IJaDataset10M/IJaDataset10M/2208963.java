package uk.co.lakesidetech.springxmldb.spring;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.w3c.dom.Node;
import org.xmldb.api.base.Resource;
import uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao;
import uk.co.lakesidetech.springxmldb.dao.IXPathXMLDBDao;
import uk.co.lakesidetech.springxmldb.dao.IXQueryXMLDBDao;
import uk.co.lakesidetech.springxmldb.dao.IXUpdateXMLDBDao;
import uk.co.lakesidetech.springxmldb.exception.XMLDBDataAccessException;
import uk.co.lakesidetech.springxmldb.exception.XMLParsingException;

/**
 * <p>
 * A simple implementation of <code>IXMLDBFacade</code> which just delegates to the appropiate
 * dao.
 * </p>
 * 
 * @author Stuart Eccles
 */
public class SimpleXMLDBFacade implements IXMLDBFacade {

    private static final String ATTRIBUTE_NAME_RESOURCEID = "resourceId";

    private static final String ELEMENT_NAME_RESULT = "result";

    private static final String ELEMENT_NAME_ROOT_RESULTS = "results";

    /** logging class */
    private static Log log = LogFactory.getLog(SimpleXMLDBFacade.class);

    private IResourceManageXMLDBDao manageDao;

    private IXPathXMLDBDao xpathDao;

    private IXQueryXMLDBDao xqueryDao;

    private IXUpdateXMLDBDao xupdateDao;

    /**
     * @param xupdateDao The xupdateDao to set.
     */
    public void setXupdateDao(IXUpdateXMLDBDao xupdateDao) {
        this.xupdateDao = xupdateDao;
    }

    /**
	 * @param manageDao The manageDao to set.
	 */
    public void setManageDao(IResourceManageXMLDBDao manageDao) {
        this.manageDao = manageDao;
    }

    /**
	 * @param xpathDao The xpathDao to set.
	 */
    public void setXpathDao(IXPathXMLDBDao xpathDao) {
        this.xpathDao = xpathDao;
    }

    /**
	 * @param xqueryDao The xqueryDao to set.
	 */
    public void setXqueryDao(IXQueryXMLDBDao xqueryDao) {
        this.xqueryDao = xqueryDao;
    }

    public String insertUpdateXMLDocument(String xmlDocument, String docID, String collectionPath) {
        return manageDao.insertUpdateXMLDocument(xmlDocument, docID, collectionPath);
    }

    public boolean removeDocument(String docID, String collectionPath) {
        return manageDao.removeDocument(docID, collectionPath);
    }

    public String retrieveDocumentAsString(String docID, String collectionPath) {
        return manageDao.retrieveDocumentAsString(docID, collectionPath);
    }

    public Node queryWithXPathCollectionAsString(String xPathQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return convertResultMapToXML(xpathDao.queryWithXPathCollectionAsString(xPathQuery, collectionPath));
    }

    public Node queryResourceWithXPathCollectionAsString(String resourceId, String xPathQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return convertResultMapToXML(xpathDao.queryResourceWithXPathCollectionAsString(resourceId, xPathQuery, collectionPath));
    }

    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return convertResultMapToXML(xqueryDao.queryWithXQueryCollectionAsString(xQuery, collectionPath));
    }

    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath, Map variables) throws XMLDBDataAccessException, XMLParsingException {
        return convertResultMapToXML(xqueryDao.queryWithXQueryCollectionAsString(xQuery, collectionPath, variables));
    }

    public Resource retrieveDocumentAsResource(String docID, String collectionPath) {
        return manageDao.retrieveDocumentAsResource(docID, collectionPath);
    }

    public Node queryWithXPathCollectionAsString(String xPathQuery, String collectionPath, int noResults) throws XMLParsingException {
        Map results = xpathDao.queryWithXPathCollectionAsString(xPathQuery, collectionPath);
        if (noResults > 0) {
            results = produceLimitedMap(results, noResults);
        }
        return convertResultMapToXML(results);
    }

    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath, int noResults) throws XMLParsingException {
        Map results = xqueryDao.queryWithXQueryCollectionAsString(xQuery, collectionPath);
        if (noResults > 0) {
            results = produceLimitedMap(results, noResults);
        }
        return convertResultMapToXML(results);
    }

    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath, Map variables, int noResults) throws XMLParsingException {
        Map results = xqueryDao.queryWithXQueryCollectionAsString(xQuery, collectionPath, variables);
        if (noResults > 0) {
            results = produceLimitedMap(results, noResults);
        }
        return convertResultMapToXML(results);
    }

    protected Map produceLimitedMap(Map results, int limit) {
        Map newResults = new LinkedHashMap();
        Iterator it = results.keySet().iterator();
        for (int i = 0; i < limit && it.hasNext(); i++) {
            Object key = it.next();
            newResults.put(key, results.get(key));
        }
        return newResults;
    }

    protected Node convertResultMapToXML(Map results) throws XMLParsingException {
        org.jdom.Document doc = new org.jdom.Document();
        Element root = new Element(ELEMENT_NAME_ROOT_RESULTS);
        doc.setRootElement(root);
        try {
            addXMLDocsToNode(root, results);
            return new DOMOutputter().output(doc);
        } catch (JDOMException e) {
            log.error(e);
            throw new XMLParsingException(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e);
            throw new XMLParsingException(e.getMessage(), e);
        }
    }

    /**
	 * add a collection of xml documents to a root node
	 * @param node the JDOM node to add to
	 * @param xmlDocs a Map of String XML documents
	 * @throws JDOMException parsing the documents
	 * @throws IOException input issue
	 */
    protected void addXMLDocsToNode(Element node, Map xmlDocs) throws JDOMException, IOException {
        for (Iterator it = xmlDocs.keySet().iterator(); it.hasNext(); ) {
            String resourceId = (String) it.next();
            String xml = (String) xmlDocs.get(resourceId);
            SAXBuilder builder = new SAXBuilder(false);
            org.jdom.Document doc = builder.build(new StringReader(xml));
            Element xmlRootEl = doc.detachRootElement();
            Element result = new Element(ELEMENT_NAME_RESULT);
            Attribute id = new Attribute(ATTRIBUTE_NAME_RESOURCEID, resourceId);
            result.setAttribute(id);
            result.addContent(xmlRootEl);
            node.addContent(result);
        }
    }

    /**
     * @see uk.co.lakesidetech.springxmldb.spring.IXMLDBFacade#xupdateXMLDocument(java.lang.String, java.lang.String, java.lang.String)
     * @param xupdate
     * @param docID
     * @param collectionPath
     */
    public void xupdateXMLDocument(String xupdate, String docID, String collectionPath) {
        xupdateDao.updateResourceWithXUpdate(xupdate, docID, collectionPath);
    }

    /**
     * @see uk.co.lakesidetech.springxmldb.spring.IXMLDBFacade#xupdateCollection(java.lang.String, java.lang.String)
     * @param xupdate
     * @param collectionPath
     */
    public void xupdateCollection(String xupdate, String collectionPath) {
        xupdateDao.updateWithXUpdate(xupdate, collectionPath);
    }
}
