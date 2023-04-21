package com.objecteffects.clublist.db.dao;

import com.objecteffects.clublist.domain.ClubUser;
import org.cloudme.sugar.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rusty Wright
 */
public final class ClubUserDao extends AbstractDao<ClubUser> {

    @SuppressWarnings("unused")
    private final transient Logger log = LoggerFactory.getLogger(this.getClass());

    /** */
    public ClubUserDao() {
        super(ClubUser.class);
    }

    /**
     * @param googleUserId
     * @return found ClubUser
     */
    public ClubUser findByGoogleUserId(final String googleUserId) {
        return (this.findSingle(AbstractDao.filter("googleUserId", googleUserId)));
    }

    /**
     * @param googleNickname
     * @return found ClubUser
     */
    public ClubUser findByGoogleNickname(final String googleNickname) {
        return (this.findSingle(AbstractDao.filter("googleNickname", googleNickname)));
    }
}
