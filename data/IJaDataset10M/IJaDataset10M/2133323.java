package org.fxplayer.dao;

import org.fxplayer.domain.TrackType;

/**
 * The Class TrackTypeDao.
 */
public class TrackTypeDao extends AbstractDao<TrackType, Integer> {

    /** The Constant instance. */
    private static final TrackTypeDao instance = new TrackTypeDao();

    /**
	 * Gets the single instance of TrackTypeDao.
	 * @return single instance of TrackTypeDao
	 */
    public static final TrackTypeDao getInstance() {
        return instance;
    }

    /**
	 * Instantiates a new track type dao.
	 */
    private TrackTypeDao() {
        super();
    }
}
