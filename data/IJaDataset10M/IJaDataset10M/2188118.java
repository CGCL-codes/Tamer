package com.divosa.security.repository;

import com.divosa.security.domain.Application;
import com.divosa.security.exception.ObjectNotFoundException;

/**
 * @author b.ottenkamp
 */
public interface ApplicationDAO extends BaseDAO {

    public Application getApplication(final String applicationName) throws ObjectNotFoundException;
}
