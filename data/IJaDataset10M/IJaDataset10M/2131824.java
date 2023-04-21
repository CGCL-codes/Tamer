package kiff.service.impl;

import kiff.dao.CoreDAO;
import kiff.entity.Topic;
import kiff.service.TopicService;
import com.google.inject.Inject;

/**
 * An implementation of the TopicService interface.
 * @author Adam
 * @version $Id: TopicServiceImpl.java 56 2008-10-20 06:39:26Z a.ruggles $
 */
public class TopicServiceImpl extends AbstractService<Topic> implements TopicService {

    /**
	 * {@inheritDoc}
	 * Note: This method can be removed after upgrading to Guice 2.0.
	 * @see kiff.service.impl.AbstractService#setDao(kiff.dao.CoreDAO)
	 */
    @Inject(optional = true)
    public void setDao(final CoreDAO<Topic> dao) {
        this.dao = dao;
    }
}
