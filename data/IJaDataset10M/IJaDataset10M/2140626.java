package com.acv.service.security.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import com.acv.dao.security.SecurityGroupDao;
import com.acv.dao.security.model.Group;
import com.acv.dao.security.model.Role;
import com.acv.service.common.exception.InvalidObjectException;
import com.acv.service.common.exception.ObjectAlreadyExistsException;
import com.acv.service.common.exception.ObjectNotFoundException;
import com.acv.service.security.SecurityGroupManager;

/**
 * The Class SecurityGroupManagerImpl.
 */
public class SecurityGroupManagerImpl implements SecurityGroupManager {

    private static final Logger log = Logger.getLogger(SecurityGroupManagerImpl.class);

    private SecurityGroupDao securityGroupDao;

    /**
	 * Sets the security group dao.
	 *
	 * @param securityGroupDao
	 *            the new security group dao
	 */
    public void setSecurityGroupDao(SecurityGroupDao securityGroupDao) {
        this.securityGroupDao = securityGroupDao;
    }

    public Group getGroupById(String id) throws ObjectNotFoundException {
        try {
            return securityGroupDao.getGroupById(Long.valueOf(id));
        } catch (DataRetrievalFailureException e) {
            log.warn("Group not found", e);
            throw new ObjectNotFoundException(e.getMessage());
        }
    }

    public List<Group> getGroups() {
        return securityGroupDao.getGroups();
    }

    public void removeGroup(String id) throws ObjectNotFoundException {
        try {
            securityGroupDao.removeGroup(Long.valueOf(id));
        } catch (DataRetrievalFailureException e) {
            log.warn("Group not found", e);
            throw new ObjectNotFoundException(e.getMessage());
        }
    }

    public void saveGroup(Group Group) throws ObjectAlreadyExistsException, InvalidObjectException {
        try {
            securityGroupDao.saveGroup(Group);
        } catch (DataAccessException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                log.warn("Group save failed", e);
                throw new ObjectAlreadyExistsException(e.getMessage());
            }
            if (e.getCause() instanceof PropertyValueException) {
                log.warn("Group save failed", e);
                throw new InvalidObjectException(e.getMessage());
            }
            throw e;
        }
    }

    public Group getGroupByName(String groupName) throws ObjectNotFoundException {
        try {
            return securityGroupDao.getGroupByName(groupName);
        } catch (DataRetrievalFailureException e) {
            log.warn("Group not found", e);
            throw new ObjectNotFoundException(e.getMessage());
        }
    }

    public List<Group> search(Map<String, Object> parameters, String sortBy) {
        return securityGroupDao.search(parameters, sortBy);
    }

    public List<Role> getRoles() {
        return securityGroupDao.getRoles();
    }

    public Role getRoleById(String id) throws ObjectNotFoundException {
        try {
            return securityGroupDao.getRoleById(Long.valueOf(id));
        } catch (DataRetrievalFailureException e) {
            log.warn("Group not found", e);
            throw new ObjectNotFoundException(e.getMessage());
        }
    }
}
