package net.sf.shineframework.server.dal.dao;

import java.io.Serializable;
import net.sf.shineframework.common.dal.dm.DalObject;
import org.springframework.dao.DataAccessException;

/**
 * Framework DAO. The purpose of this DAO is to provide generic framework data
 * access services.
 * 
 * @author amirk
 * 
 */
public interface FwDao<T extends DalObject> extends Dao<T> {

    public T getByPk(Class<? extends DalObject> clazz, Serializable pk) throws DataAccessException;
}
