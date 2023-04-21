package org.javanuke.core.model.dao;

import java.util.Collection;
import net.sf.hibernate.Criteria;
import org.javanuke.core.model.dto.DataTransferObjectSupport;

/**
 * @aut[hor Franklin Samir (franklin@portaljava.com)
 * Created on 15/02/2003
 */
public abstract class DataAccesObject implements CompleteDAOInterface {

    protected Class myDTO;

    protected CommonDAO commonDAO;

    public abstract boolean exclude(DataTransferObjectSupport dbObject);

    public abstract Collection findByCriteria(Criteria criteria);

    public abstract Collection findAll();

    public abstract boolean store(DataTransferObjectSupport dbObject);

    /**
	 * Returns the myDTO.
	 * @return Class
	 */
    public Class getMyDTO() {
        return myDTO;
    }

    /**
	 * Sets the myDTO.
	 * @param myDTO The myDTO to set
	 */
    public void setMyDTO(Class myDTO) {
        this.myDTO = myDTO;
    }
}
