package org.javanuke.core.model.dao;

import java.util.Collection;
import java.util.Iterator;
import org.javanuke.core.model.dto.DataTransferObjectSupport;

/**
 * @author Franklin Samir (franklin@portaljava.com)
 * Created on 21/04/2003
 */
public interface DBObjectDAOInterface {

    /**
		 * Retrieve a list of data from data base, with no criteria. All rows will
		 * be returned.
		 * 
		 * @return Collection containing DTOs.@see org.jnuke.model.dao.CompleteDAOInterface#findAll()
		 */
    public abstract Collection findAll();

    /**
		 * 
		 * @return Iterator
		 * @see  org.jnuke.model.dao.CompleteDAOInterface#findAll()
		 */
    public Iterator findAllAsIterator();

    /**
		 * Retrieve a list of data from data base. Criterias will be used to query
		 * filter the query.
		 */
    public abstract Collection findByCriteria(DataTransferObjectSupport dto);

    /**
		* Retrieve a list of data from data base. Criterias will be used to query
		* filter the query.
		* @return Iterator
		* @see org.jnuke.model.dao.CompleteDAOInterface#findByCriteria(org.jnuke.
		* model.dto. DataTransferObject)
		*/
    public Iterator findByCriteriaAsIterator(DataTransferObjectSupport dto);

    /**
		* Remove a row from data base.
		* @param a DTO with parameters to construct the query criterias.
		* @return transaction succesfull?
		*/
    public abstract boolean exclude(DataTransferObjectSupport dto);

    /**
		* Store data in data base.
		* 
		* @return transaction sucessfull?
		*/
    public abstract boolean store(DataTransferObjectSupport dto);

    /**
		* Retrieve one specific row from data base.
		* 
		* @param a DTO with parameters to construct the query criterias.
		* @return a DTO with data from data base
		*/
    public abstract DataTransferObjectSupport findOne(DataTransferObjectSupport dto);
}
