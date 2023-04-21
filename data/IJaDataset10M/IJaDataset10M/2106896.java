package uk.co.lakesidetech.springxmldb.dao.exist;

import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exist.xmldb.CollectionImpl;
import org.exist.xmldb.EXistResource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import uk.co.lakesidetech.springxmldb.dao.BaseCollectionManageXMLDBDao;
import uk.co.lakesidetech.springxmldb.data.XMLDBCollectionInfo;
import uk.co.lakesidetech.springxmldb.data.XMLDBResourceInfo;
import uk.co.lakesidetech.springxmldb.exception.XMLDBDataAccessException;
import uk.co.lakesidetech.springxmldb.util.CollectionPathUtils;

/**
 * @author Stuart Eccles
 */
public class ExistCollectionManageXMLDBDao extends BaseCollectionManageXMLDBDao {

    /** logging class */
    private static Log log = LogFactory.getLog(ExistCollectionManageXMLDBDao.class);

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.ICollectionManageXMLDBDao#getCollectionResourceInfo(java.lang.String)
	 * @param collectionPath
	 * @return
	 */
    public java.util.Collection getCollectionResourceInfo(String collectionPath) throws XMLDBDataAccessException {
        Collection collection = null;
        try {
            collection = dataSource.getCollection(collectionPath);
            Vector resources = new Vector();
            if (collection != null) {
                String[] resourceNames = collection.listResources();
                for (int i = 0; i < resourceNames.length; i++) {
                    EXistResource eres = (EXistResource) collection.getResource(resourceNames[i]);
                    Resource res = collection.getResource(resourceNames[i]);
                    XMLDBResourceInfo resInfo = new XMLDBResourceInfo();
                    resInfo.setId(res.getId());
                    resInfo.setResourceType(res.getResourceType());
                    resInfo.setParentCollectionPath(collectionPath);
                    resInfo.setCreationDateTime(eres.getCreationTime());
                    resInfo.setLastmodifiedDateTime(eres.getLastModificationTime());
                    resources.add(resInfo);
                }
            }
            return resources;
        } catch (XMLDBException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e2) {
                log.error(e2);
            }
        }
    }

    /**
	 * 
	 * @see uk.co.lakesidetech.springxmldb.dao.ICollectionManageXMLDBDao#getCollectionCollectionInfo(java.lang.String)
	 * @param collectionPath
	 * @return
	 */
    public java.util.Collection getCollectionCollectionInfo(String collectionPath) throws XMLDBDataAccessException {
        Collection collection = null;
        try {
            collection = dataSource.getCollection(collectionPath);
            Vector resources = new Vector();
            if (collection != null) {
                String[] collectionNames = collection.listChildCollections();
                for (int i = 0; i < collectionNames.length; i++) {
                    Collection col = collection.getChildCollection(collectionNames[i]);
                    XMLDBCollectionInfo colInfo = new XMLDBCollectionInfo();
                    colInfo.setChildCollectionCount(col.getChildCollectionCount());
                    colInfo.setChildCollectionNames(col.listChildCollections());
                    colInfo.setCollectionName(CollectionPathUtils.findCollectionNameFromFullPath(col.getName()));
                    colInfo.setCollectionFullPath(col.getName());
                    colInfo.setResourceCount(col.getResourceCount());
                    CollectionImpl ecol = (CollectionImpl) collection.getChildCollection(collectionNames[i]);
                    colInfo.setCreationDateTime(ecol.getCreationTime());
                    resources.add(colInfo);
                }
            }
            return resources;
        } catch (XMLDBException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e2) {
                log.error(e2);
            }
        }
    }

    public XMLDBResourceInfo getResourceInfo(String resourceId, String collectionPath) throws XMLDBDataAccessException {
        Collection collection = null;
        try {
            collection = dataSource.getCollection(collectionPath);
            if (collection != null) {
                EXistResource eres = (EXistResource) collection.getResource(resourceId);
                Resource res = collection.getResource(resourceId);
                XMLDBResourceInfo resInfo = new XMLDBResourceInfo();
                resInfo.setId(res.getId());
                resInfo.setResourceType(res.getResourceType());
                resInfo.setParentCollectionPath(collectionPath);
                resInfo.setCreationDateTime(eres.getCreationTime());
                resInfo.setLastmodifiedDateTime(eres.getLastModificationTime());
                return resInfo;
            }
        } catch (XMLDBException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e2) {
                log.error(e2);
            }
        }
        return null;
    }

    public XMLDBCollectionInfo getCollectionInfo(String collectionPath) throws XMLDBDataAccessException {
        Collection collection = null;
        try {
            collection = dataSource.getCollection(collectionPath);
            if (collection != null) {
                XMLDBCollectionInfo colInfo = new XMLDBCollectionInfo();
                colInfo.setChildCollectionCount(collection.getChildCollectionCount());
                colInfo.setChildCollectionNames(collection.listChildCollections());
                colInfo.setCollectionName(CollectionPathUtils.findCollectionNameFromFullPath(collection.getName()));
                colInfo.setCollectionFullPath(collection.getName());
                colInfo.setResourceCount(collection.getResourceCount());
                CollectionImpl ecol = (CollectionImpl) collection;
                colInfo.setCreationDateTime(ecol.getCreationTime());
                return colInfo;
            }
        } catch (XMLDBException e) {
            log.error(e);
            throw new XMLDBDataAccessException(e.getMessage(), e);
        } finally {
            try {
                if (collection != null) collection.close();
            } catch (XMLDBException e2) {
                log.error(e2);
            }
        }
        return null;
    }
}
