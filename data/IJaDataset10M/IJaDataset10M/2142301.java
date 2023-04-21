package uk.co.lakesidetech.springxmldb.spring;

import java.util.Map;
import org.w3c.dom.Node;
import org.xmldb.api.base.Resource;
import uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao;
import uk.co.lakesidetech.springxmldb.dao.IXPathXMLDBDao;
import uk.co.lakesidetech.springxmldb.dao.IXQueryXMLDBDao;
import uk.co.lakesidetech.springxmldb.exception.XMLDBDataAccessException;
import uk.co.lakesidetech.springxmldb.exception.XMLParsingException;

/**
 * <p>
 * Implementation example of facade that uses results caching with source attribute wangy
 * cache and oscache. This is an example of how caching of results can be used in the facade
 * retrieval of content
 * </p>
 * 
 * @author Stuart Eccles
 */
public class CachingXMLDBFacade extends SimpleXMLDBFacade implements IXMLDBFacade {

    private IResourceManageXMLDBDao manageDao;

    private IXPathXMLDBDao xpathDao;

    private IXQueryXMLDBDao xqueryDao;

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

    /**
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#insertUpdateXMLDocument(java.lang.String, java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.flush.oscache.OscacheCacheFlushAttribute(groups="temporary,test")
	 **/
    public String insertUpdateXMLDocument(String xmlDocument, String docID, String collectionPath) {
        return manageDao.insertUpdateXMLDocument(xmlDocument, docID, collectionPath);
    }

    /**
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#insertUpdateXMLDocument(java.lang.String, java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.flush.oscache.OscacheCacheFlushAttribute(groups="temporary,test")
	 **/
    public boolean removeDocument(String docID, String collectionPath) {
        return manageDao.removeDocument(docID, collectionPath);
    }

    /** (non-Javadoc)
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#retrieveDocumentRaw(java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public String retrieveDocumentAsString(String docID, String collectionPath) {
        return manageDao.retrieveDocumentAsString(docID, collectionPath);
    }

    /** (non-Javadoc)
	 * @see uk.co.lakesidetech.springxmldb.spring.IXMLDBFacade#retrieveDocumentAsResource(java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public Resource retrieveDocumentAsResource(String docID, String collectionPath) {
        return manageDao.retrieveDocumentAsResource(docID, collectionPath);
    }

    /** (non-Javadoc)
	 * @throws XMLParsingException
	 * @throws XMLDBDataAccessException
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#queryWithXPathCollectionRaw(java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public Node queryWithXPathCollectionAsString(String xPathQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return super.queryWithXPathCollectionAsString(xPathQuery, collectionPath);
    }

    /** (non-Javadoc)
	 * @throws XMLParsingException
	 * @throws XMLDBDataAccessException
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#queryResourceWithXPathCollectionRaw(java.lang.String, java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public Node queryResourceWithXPathCollectionAsString(String resourceId, String xPathQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return super.queryResourceWithXPathCollectionAsString(resourceId, xPathQuery, collectionPath);
    }

    /** (non-Javadoc)
	 * @throws XMLParsingException
	 * @throws XMLDBDataAccessException
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#queryWithXQueryCollectionRaw(java.lang.String, java.lang.String)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath) throws XMLDBDataAccessException, XMLParsingException {
        return super.queryWithXQueryCollectionAsString(xQuery, collectionPath);
    }

    /** (non-Javadoc)
	 * @throws XMLParsingException
	 * @throws XMLDBDataAccessException
	 * @see uk.co.lakesidetech.springxmldb.spring.ISimpleXMLDBFacade#queryWithXQueryCollectionRaw(java.lang.String, java.lang.String, java.util.Map)
	 * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
	 */
    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath, Map variables) throws XMLDBDataAccessException, XMLParsingException {
        return super.queryWithXQueryCollectionAsString(xQuery, collectionPath, variables);
    }

    /**
     * @see uk.co.lakesidetech.springxmldb.spring.IXMLDBFacade#queryWithXPathCollectionAsString(java.lang.String, java.lang.String, int)
     * @param xPathQuery
     * @param collectionPath
     * @param noResults
     * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
     * @return
     * @throws XMLParsingException
     */
    public Node queryWithXPathCollectionAsString(String xPathQuery, String collectionPath, int noResults) throws XMLParsingException {
        return super.queryWithXPathCollectionAsString(xPathQuery, collectionPath, noResults);
    }

    /**
     * @see uk.co.lakesidetech.springxmldb.spring.IXMLDBFacade#queryWithXQueryCollectionAsString(java.lang.String, java.lang.String, int)
     * @param xQuery
     * @param collectionPath
     * @param noResults
     * @@org.wanghy.cache.interceptor.caching.oscache.OscacheCachingAttribute(refreshPeriod=60, groups="temporary,test")
     * @return
     * @throws XMLParsingException
     */
    public Node queryWithXQueryCollectionAsString(String xQuery, String collectionPath, int noResults) throws XMLParsingException {
        return super.queryWithXQueryCollectionAsString(xQuery, collectionPath, noResults);
    }
}
