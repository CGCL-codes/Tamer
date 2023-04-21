package org.iptc.ines.component.persistence.rdbms.dao;

import java.io.InputStream;
import java.util.List;
import org.iptc.ines.component.persistence.model.NewsMLDocument;
import org.iptc.ines.component.persistence.model.StoreAnyItem;
import org.iptc.ines.exception.StoreException;
import org.iptc.nar.core.model.AnyItemType;
import org.springframework.dao.DataAccessException;

/**
 * DAO for NewsItem persistence.
 * 
 * @author Bertrand Goupil
 */
public interface StoreItemDao {

    /**
	 * DAO for save or update Item
	 * 
	 * @param storeItem
	 * @throws DataAccessException
	 */
    public boolean saveOrUpdate(StoreAnyItem storeItem, NewsMLDocument newsMLDocument);

    /**
	 * DAO method to get the latest version of NewsMLDocument Object. Use to get
	 * the XML data of the Item.
	 * 
	 * @param guid
	 * @return
	 */
    public List<NewsMLDocument> getNewsMLDocumentFromGuid(String guid);

    /**
	 * DAO method to get the latest version of DOM Object. Use to get the XML
	 * data of the Item.
	 * 
	 * @param guid
	 * @return
	 */
    public InputStream getNewsMLDOMFromGuid(String guid) throws DataAccessException;

    /**
	 * DAO method to get the anyItem object from guid
	 * 
	 * @param guid
	 * @return corresponding AnyItem object
	 */
    public StoreAnyItem getItemFromGuid(String guid);

    /**
	 * Delete Item from the storeItem object
	 * 
	 * @param guid
	 */
    public void delete(StoreAnyItem storeItem);

    /**
	 * Delete Item with the specified guid
	 * 
	 * @param guid
	 */
    public void delete(String guid) throws StoreException;

    /**
	 * get all anyItem from database
	 * 
	 * @throws StoreException
	 */
    public List<String> exportAll() throws StoreException;

    /**
	 * get all anyItem from database that match search string
	 * 
	 * @throws StoreException
	 */
    public List<AnyItemType> searchItemFromMetadata(String searchString) throws StoreException;
}
