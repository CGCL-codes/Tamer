package com.google.code.sapien.service;

import java.io.Serializable;
import java.util.Date;
import com.google.code.sapien.dao.CategoryDAO;
import com.google.code.sapien.dao.DiscussionDAO;
import com.google.code.sapien.model.Discussion;
import com.google.code.sapien.model.Response;
import com.google.code.sapien.model.User;
import com.google.code.sapien.util.PaginatedResult;
import com.google.inject.Inject;

/**
 * Default implementation of the {@link com.google.code.sapien.service.DiscussionService}.
 * @author Adam
 * @version $Id: DiscussionSapienService.java 25 2009-05-26 04:23:01Z a.ruggles $
 * 
 * Created on Feb 17, 2009 at 11:16:39 PM 
 */
public class DiscussionSapienService implements DiscussionService {

    /**
	 * The category data access object.
	 */
    private final CategoryDAO categoryDAO;

    /**
	 * Discussion Data Access Object.
	 */
    private final DiscussionDAO discussionDAO;

    /**
	 * Constructs a discussion sapien service.
	 * @param discussionDAO The discussion data access object.
	 */
    @Inject
    public DiscussionSapienService(final DiscussionDAO discussionDAO, final CategoryDAO categoryDAO) {
        this.discussionDAO = discussionDAO;
        this.categoryDAO = categoryDAO;
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.EntityService#add(java.lang.Object, com.google.code.sapien.model.User)
	 */
    public void add(final Discussion entity, final User creator) {
        final long timems = System.currentTimeMillis();
        entity.setCreated(new Date(timems));
        entity.setModified(new Date(timems));
        entity.setCreator(creator);
        entity.setModifier(creator);
        entity.setCategory(categoryDAO.load(entity.getCategory().getId()));
        entity.setResponseCount(0);
        Response response = entity.getResponse();
        if (response == null) {
            response = new Response();
        }
        response.setDiscussion(entity);
        response.setCreated(new Date(timems));
        response.setCreator(creator);
        response.setModified(new Date(timems));
        entity.setResponse(response);
        discussionDAO.add(entity);
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.EntityService#get(java.io.Serializable)
	 */
    public Discussion get(final Serializable id) {
        return discussionDAO.get(id);
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.DiscussionService#getModifiedDesc(int, int)
	 */
    public PaginatedResult<Discussion> getModifiedDesc(final int page, final int resultsPerPage) {
        final int offset = ((page - 1) * resultsPerPage);
        return new PaginatedResult<Discussion>(discussionDAO.list(offset, resultsPerPage), discussionDAO.getCount(), page, resultsPerPage);
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.DiscussionService#getModifiedDesc(java.lang.Long, int, int)
	 */
    public PaginatedResult<Discussion> getModifiedDesc(final Long categoryId, final int page, final int resultsPerPage) {
        final int offset = ((page - 1) * resultsPerPage);
        return new PaginatedResult<Discussion>(discussionDAO.list(categoryId, offset, resultsPerPage), discussionDAO.getCount(categoryId), page, resultsPerPage);
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.EntityService#remove(java.lang.Object, com.google.code.sapien.model.User)
	 */
    public void remove(final Discussion entity, final User remover) {
        final Discussion discussion = discussionDAO.load(entity.getId());
        discussionDAO.remove(discussion);
    }

    /**
	 * {@inheritDoc}
	 * @see com.google.code.sapien.service.EntityService#update(java.lang.Object, com.google.code.sapien.model.User)
	 */
    public Discussion update(final Discussion entity, final User updator) {
        final Discussion discussion = discussionDAO.load(entity.getId());
        discussion.setModifier(updator);
        discussion.setModified(new Date());
        if (entity.getCategory() != null && entity.getCategory().getId() != null) {
            entity.setCategory(categoryDAO.load(entity.getCategory().getId()));
        }
        if (entity.getSubject() != null) {
            discussion.setSubject(entity.getSubject());
        }
        if (entity.getResponseCount() != null) {
            discussion.setResponseCount(entity.getResponseCount());
        }
        if (entity.getResponse() != null && entity.getResponse().getBody() != null) {
            final Response discussionMessage = discussion.getResponse();
            discussionMessage.setBody(entity.getResponse().getBody());
            discussionMessage.setModified(discussion.getModified());
        }
        return discussion;
    }
}
