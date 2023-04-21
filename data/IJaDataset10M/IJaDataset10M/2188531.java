package edu.asu.commons.mme.dao;

import edu.asu.commons.mme.entity.Game;

public class HibernateGameDao extends HibernateDao<Game> {

    public HibernateGameDao() {
        super(Game.class);
    }
}
