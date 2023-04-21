package kiff.service.impl;

import java.util.Date;
import java.util.List;
import kiff.dao.CoreDAO;
import kiff.dao.ForumAclDAO;
import kiff.dao.ForumDAO;
import kiff.entity.Forum;
import kiff.entity.ForumAcl;
import kiff.entity.ForumAclEntry;
import kiff.entity.User;
import kiff.enumerator.ForumStatus;
import kiff.exception.DataLayerException;
import kiff.exception.ServiceLayerException;
import kiff.security.AclEntry;
import kiff.service.ForumService;
import com.google.inject.Inject;

/**
 * An implementation of the ForumService interface.
 * @author Adam
 * @version $Id: ForumServiceImpl.java 73 2008-12-02 06:28:25Z a.ruggles $
 */
public class ForumServiceImpl extends AbstractService<Forum> implements ForumService {

    /**
	 * Forum Access Control List Data Access Object.
	 */
    private ForumAclDAO forumAclDAO;

    /**
	 * {@inheritDoc}
	 * @see kiff.service.ForumService#findByStatus(kiff.enumerator.ForumStatus)
	 */
    public List<Forum> findByStatus(final ForumStatus... statuses) throws ServiceLayerException {
        try {
            return ((ForumDAO) dao).findByStatus(statuses);
        } catch (DataLayerException dataLayerEx) {
            throw new ServiceLayerException(dataLayerEx);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.service.impl.AbstractService#insert(kiff.entity.CoreEntity, kiff.entity.User)
	 */
    @Override
    public void insert(final Forum forum, final User user) throws ServiceLayerException {
        if (user == null || !user.isAdministrator()) {
            throw new SecurityException("User " + user + " does not have permission to create a forum.");
        }
        try {
            ForumAcl acl = (ForumAcl) forum.getPermissions();
            if (acl == null || acl.getId() == null) {
                acl = (ForumAcl) forumAclDAO.findByName(ForumAcl.DEFAULT_NAME);
            } else {
                acl = (ForumAcl) forumAclDAO.loadById(acl.getId());
            }
            ForumAcl forumAcl = new ForumAcl(forum);
            for (AclEntry entry : acl.getEntries()) {
                forumAcl.addEntry(new ForumAclEntry(entry.getPrincipal(), entry.getPermission()));
            }
            if (forum.getParent() != null) {
                if (forum.getParent().getId() == null) {
                    forum.setParent(null);
                } else {
                    forum.setParent(dao.loadById(forum.getParent().getId()));
                }
            }
            Date timestamp = new Date();
            forum.setOrder(((ForumDAO) dao).findCountByParent(forum.getParent()));
            forum.setPostCount(0);
            forum.setTopicCount(0);
            forum.setCreator(user);
            forum.setCreated(timestamp);
            forum.setModified(timestamp);
            forum.setPermissions(forumAcl);
            dao.insert(forum);
        } catch (DataLayerException dataLayerException) {
            throw new ServiceLayerException(dataLayerException);
        }
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.service.impl.AbstractService#remove(kiff.entity.CoreEntity, kiff.entity.User)
	 */
    @Override
    public void remove(final Forum forum, final User user) throws ServiceLayerException {
        if (user == null || !user.isAdministrator()) {
            throw new SecurityException("User " + user + " does not have permission to update a forum.");
        }
        super.remove(forum, user);
    }

    /**
	 * {@inheritDoc}
	 * Note: This method can be removed after upgrading to Guice 2.0.
	 * @see kiff.service.impl.AbstractService#setDao(kiff.dao.CoreDAO)
	 */
    @Inject(optional = true)
    public void setDao(final CoreDAO<Forum> dao) {
        this.dao = dao;
    }

    /**
	 * Sets forumAclDAO.
	 * @param forumAclDAO the forumAclDAO to set.
	 */
    @Inject
    public void setForumAclDAO(final ForumAclDAO forumAclDAO) {
        this.forumAclDAO = forumAclDAO;
    }

    /**
	 * {@inheritDoc}
	 * @see kiff.service.impl.AbstractService#update(kiff.entity.CoreEntity, kiff.entity.User)
	 */
    @Override
    public Forum update(final Forum forum, final User user) throws ServiceLayerException {
        if (user == null || !user.isAdministrator()) {
            throw new SecurityException("User " + user + " does not have permission to update a forum.");
        }
        try {
            forum.setModified(new Date());
            Forum persistentForum = dao.loadById(forum.getId());
            if (forum.getParent() != null) {
                if (forum.getParent().getId() == null) {
                    forum.setParent(null);
                } else {
                    forum.setParent(dao.loadById(forum.getParent().getId()));
                }
            }
            persistentForum.update(forum);
            if (forum.getParent() == null) {
                persistentForum.setParent(null);
            }
            return dao.update(persistentForum);
        } catch (DataLayerException dataLayerException) {
            throw new ServiceLayerException(dataLayerException);
        }
    }
}
