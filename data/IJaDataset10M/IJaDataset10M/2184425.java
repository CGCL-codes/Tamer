package com.gotloop.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.gotloop.jdo.Tag;

/**
 * Tag Data Acces Object Implementation.
 * @author jibhaine
 *
 */
@Repository
public class TagDAOImpl extends AbstractDAOImpl<Tag, String> implements TagDAO {

    /**
	 * Logger for TagDAOImpl.
	 */
    private static final Logger LOG = LoggerFactory.getLogger(TagDAOImpl.class);

    @Override
    public int getNbTagOccur(String tagLabel) {
        LOG.debug("count occurences for tag {}", tagLabel);
        return 0;
    }
}
