package com.antilia.letsplay.domain;

import com.antilia.hibernate.cfg.PersistenceSet;

/**
 * The persistence set of all entities.
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class PlayPersistenceSet extends PersistenceSet {

    private static final PlayPersistenceSet instance = new PlayPersistenceSet();

    private PlayPersistenceSet() {
        addEntitiesInSamePackageAs(DWord.class);
    }

    /**
	 * @return the instance
	 */
    public static PlayPersistenceSet getInstance() {
        return instance;
    }
}
