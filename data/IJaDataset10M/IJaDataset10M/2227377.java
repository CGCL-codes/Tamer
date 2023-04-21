package com.angel.mocks.daos;

import com.angel.architecture.persistence.ids.ObjectId;
import com.angel.dao.generic.interfaces.GenericDAO;
import com.angel.mocks.providers.Address;

/**
 *
 * @author William
 */
public interface NotMappedDAO extends GenericDAO<Address, ObjectId> {
}
