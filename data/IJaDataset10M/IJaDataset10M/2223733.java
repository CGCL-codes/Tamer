package uk.co.lakesidetech.springxmldb.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.XMLResource;
import uk.co.lakesidetech.springxmldb.exception.XMLDBDataAccessException;
import uk.co.lakesidetech.springxmldb.exception.XMLParsingException;
import uk.co.lakesidetech.springxmldb.util.XMLUtils;
import uk.co.lakesidetech.springxmldb.xmldb.XMLDBActionCallback;
import uk.co.lakesidetech.springxmldb.xmldb.XMLDBRetrieveResourceActionCallback;
import uk.co.lakesidetech.springxmldb.xmldb.XMLDBStoreResourceActionCallback;
import uk.co.lakesidetech.springxmldb.xmldb.XMLDBTemplate;

/**
 * Implementation of <code>IResourceManageXMLDBDao</code> using XML:DB apis to be reusable across
 * any XML database which supports a pure XML:DB api
 * 
 * @author Stuart Eccles
 */
public class BaseResourceManageXMLDBDao extends AbstractXMLDBDao implements IResourceManageXMLDBDao {

    private boolean createNonExistingCollections;

    /** logging class */
    private static Log log = LogFactory.getLog(BaseResourceManageXMLDBDao.class);

    /**
     * @return Returns the createNonExistingCollections.
     */
    public boolean isCreateNonExistingCollections() {
        return createNonExistingCollections;
    }

    /**
     * @param createNonExistingCollections The createNonExistingCollections to set.
     */
    public void setCreateNonExistingCollections(boolean createNonExistingCollections) {
        this.createNonExistingCollections = createNonExistingCollections;
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateXMLDocument(java.lang.String, java.lang.String, java.lang.String)
	 * @param xmlDocument
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateXMLDocument(String xmlDocument, String docID, String collectionPath) throws XMLDBDataAccessException {
        return insertUpdateXMLDocumentObject(xmlDocument, docID, collectionPath);
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateXMLDocument(java.io.File, java.lang.String, java.lang.String)
	 * @param xmlDocument
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateXMLDocument(File xmlDocument, String docID, String collectionPath) throws XMLDBDataAccessException {
        FileInputStream xmlFile;
        try {
            xmlFile = new FileInputStream(xmlDocument);
            Document dom = XMLUtils.toDocument(xmlFile);
            return insertUpdateXMLDocument(dom, docID, collectionPath);
        } catch (FileNotFoundException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        } catch (XMLParsingException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        }
    }

    /**
	 * Do the insert/update of the object using the supplied Object as the resource content
	 * should be byte[] or String (File works for eXist only) as a XML Resource
	 * @param xmlDocument The Object of the xml contents
	 * @param docID The document id to use. null will generate a new id
	 * @param collectionPath The collectionpath to insert/update under
	 * @return The resulting resource ID
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateXMLDocumentObject(Object xmlDocument, String resourceId, String collectionPath) throws XMLDBDataAccessException {
        return insertUpdateObject(xmlDocument, resourceId, XMLResource.RESOURCE_TYPE, collectionPath);
    }

    /**
	 * Do the insert/update of the object using the supplied Object as the resource content
	 * that supplied reosurce type will indicate which type it will be stored
	 * under (XML, binary)
	 * @param docID The document id to use. null will generate a new id
	 * @param docID The resource type
	 * @param collectionPath The collectionpath to insert/update under
	 * @return The resulting resource ID
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateObject(Object resourceContent, String resourceId, String resourceType, String collectionPath) throws XMLDBDataAccessException {
        XMLDBTemplate template = getTemplate();
        XMLDBActionCallback action = new XMLDBStoreResourceActionCallback(resourceId, resourceContent, resourceType);
        return (String) template.executeActionOnCollection(action, collectionPath);
    }

    /**
	 * Do the insert/update of the object using the supplied Object as the resource content
	 * should be byte[] or String (File works for eXist only) as a Binary Resource
	 * @param docID The document id to use. null will generate a new id
	 * @param collectionPath The collectionpath to insert/update under
	 * @return The resulting resource ID
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateBinaryObject(Object binaryResource, String resourceId, String collectionPath) throws XMLDBDataAccessException {
        return insertUpdateObject(binaryResource, resourceId, BinaryResource.RESOURCE_TYPE, collectionPath);
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#removeDocument(java.lang.String, java.lang.String)
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public boolean removeDocument(final String docID, String collectionPath) throws XMLDBDataAccessException {
        XMLDBTemplate template = getTemplate();
        XMLDBActionCallback action = new XMLDBActionCallback() {

            public Object doInXMLDBCollection(Collection col) throws XMLDBException {
                Resource resource = null;
                resource = col.getResource(docID);
                if (resource == null) {
                    return new Boolean(false);
                }
                col.removeResource(resource);
                return new Boolean(true);
            }
        };
        return ((Boolean) template.executeActionOnCollection(action, collectionPath)).booleanValue();
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#retrieveDocumentAsDOM(java.lang.String, java.lang.String)
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public Document retrieveDocumentAsDOM(final String docID, String collectionPath) throws XMLDBDataAccessException {
        XMLDBTemplate template = getTemplate();
        XMLDBActionCallback action = new XMLDBActionCallback() {

            public Object doInXMLDBCollection(Collection col) throws XMLDBException {
                try {
                    Resource resource = col.getResource(docID);
                    if (resource != null) {
                        if (resource.getResourceType().equals(XMLResource.RESOURCE_TYPE)) return XMLUtils.xmlStringToDOMDocument((String) resource.getContent());
                    }
                    return null;
                } catch (XMLParsingException e) {
                    log.error(e);
                    throw new XMLDBDataAccessException(e.getMessage(), e);
                }
            }
        };
        Document dom = (Document) template.executeActionOnCollection(action, collectionPath);
        if (dom != null) {
            return dom;
        } else {
            return null;
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#retrieveDocumentAsString(java.lang.String, java.lang.String)
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String retrieveDocumentAsString(final String docID, String collectionPath) throws XMLDBDataAccessException {
        XMLDBTemplate template = getTemplate();
        XMLDBActionCallback action = new XMLDBActionCallback() {

            public Object doInXMLDBCollection(Collection col) throws XMLDBException {
                Resource resource = col.getResource(docID);
                if (resource != null) {
                    if (resource.getResourceType().equals(XMLResource.RESOURCE_TYPE)) return (String) resource.getContent();
                }
                return null;
            }
        };
        String dom = (String) template.executeActionOnCollection(action, collectionPath);
        if (dom != null) {
            return dom;
        } else {
            return null;
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateXMLDocument(org.w3c.dom.Document, java.lang.String, java.lang.String)
	 * @param xmlDocument
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateXMLDocument(Document xmlDocument, String docID, String collectionPath) throws XMLDBDataAccessException {
        String stringDoc;
        try {
            stringDoc = XMLUtils.domDocumentToString(xmlDocument);
            return insertUpdateXMLDocument(stringDoc, docID, collectionPath);
        } catch (Exception e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateFile(java.io.File, java.lang.String, java.lang.String)
	 * @param file
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateFile(File file, String docID, String collectionPath) throws XMLDBDataAccessException {
        try {
            FileInputStream is = new FileInputStream(file);
            Document doc = XMLUtils.toDocument(is);
            return insertUpdateXMLDocument(doc, docID, collectionPath);
        } catch (XMLParsingException e1) {
            try {
                log.debug(e1);
                return insertUpdateBinaryObject(readFile(file), docID, collectionPath);
            } catch (IOException e) {
                log.error(e);
                throw new XMLDBDataAccessException("file can not be read", e);
            }
        } catch (FileNotFoundException e2) {
            log.error(e2);
            throw new XMLDBDataAccessException("file not found", e2);
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateBinaryFile(byte[], java.lang.String, java.lang.String)
	 * @param docBytes
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateBinaryFile(byte[] docBytes, String docID, String collectionPath) throws XMLDBDataAccessException {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(docBytes);
            Document doc = XMLUtils.toDocument(is);
            return insertUpdateXMLDocument(doc, docID, collectionPath);
        } catch (XMLParsingException e1) {
            log.debug(e1);
            return insertUpdateBinaryObject(docBytes, docID, collectionPath);
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#insertUpdateInputStream(java.io.InputStream, java.lang.String, java.lang.String)
	 * @param stream
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public String insertUpdateInputStream(InputStream stream, String docID, String collectionPath) throws XMLDBDataAccessException {
        try {
            Document doc = XMLUtils.toDocument(stream);
            return insertUpdateXMLDocument(doc, docID, collectionPath);
        } catch (XMLParsingException e1) {
            try {
                log.debug(e1);
                return insertUpdateBinaryObject(readStream(stream), docID, collectionPath);
            } catch (IOException e) {
                log.error(e);
                throw new XMLDBDataAccessException("file can not be read", e);
            }
        }
    }

    /**
	 * read a file into a byte array
	 * @param file the File to read
	 * @return a byte array
	 * @throws IOException
	 */
    private byte[] readFile(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        return readStream(is);
    }

    /**
	 * read an input stream into a byte array
	 * @param tream the input stream to read
	 * @return a byte array
	 * @throws IOException
	 */
    private byte[] readStream(InputStream tream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
        byte[] temp = new byte[1024];
        int count = 0;
        while ((count = tream.read(temp)) > -1) {
            bos.write(temp, 0, count);
        }
        byte[] rawData = bos.toByteArray();
        return rawData;
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.IResourceManageXMLDBDao#retrieveDocumentAsResource(java.lang.String, java.lang.String)
	 * @param docID
	 * @param collectionPath
	 * @return
	 * @throws XMLDBDataAccessException
	 */
    public Resource retrieveDocumentAsResource(String docID, String collectionPath) throws XMLDBDataAccessException {
        XMLDBTemplate template = getTemplate();
        XMLDBActionCallback action = new XMLDBRetrieveResourceActionCallback(docID);
        Resource dom = (Resource) template.executeActionOnCollection(action, collectionPath);
        if (dom != null) {
            return dom;
        } else {
            return null;
        }
    }

    protected XMLDBTemplate getTemplate() {
        XMLDBTemplate template = new XMLDBTemplate(dataSource);
        template.setCreateNonExistingCollections(createNonExistingCollections);
        return template;
    }
}
