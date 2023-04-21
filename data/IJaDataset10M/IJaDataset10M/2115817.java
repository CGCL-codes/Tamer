package com.performance.dao;

import com.jxva.dao.BaseDao;
import com.performance.model.Actor;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:24 by Automatic Generate Toolkit
 */
public class ActorDao extends BaseDao {

    public Actor getActor(int actorId) {
        return dao.get(Actor.class, actorId);
    }

    public int save(Actor actor) {
        return dao.save(actor);
    }

    public int update(Actor actor) {
        return dao.update(actor);
    }

    public int delete(Actor actor) {
        return dao.delete(actor);
    }

    public int delete(int actorId) {
        return dao.delete(Actor.class, actorId);
    }

    public int saveOrUpdate(Actor actor) {
        return dao.saveOrUpdate(actor);
    }
}
