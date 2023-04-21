package com.privilege.entity.tournament;

import java.util.Set;
import org.directwebremoting.util.Logger;
import com.privilege.entity.Entity;
import com.privilege.entity.vote.Item;
import com.privilege.entity.vote.Vote;

public class Description implements Entity {

    private long id;

    private String name;

    private Set<PoolDescription> poolDescriptions = null;

    private static final Logger logger = Logger.getLogger(Description.class);

    public long getId() {
        logger.debug("Method: getId(" + ")");
        return this.id;
    }

    private void setId(long id) {
        logger.debug("Method: setId(" + id + ")");
        this.id = id;
    }

    public String getName() {
        logger.debug("Method: getName(" + ")");
        return this.name;
    }

    public void setName(String name) {
        logger.debug("Method: setName(" + name + ")");
        this.name = name;
    }

    public Set<PoolDescription> getPoolDescriptions() {
        logger.debug("Method: getPoolDescription(" + ")");
        return this.poolDescriptions;
    }

    public void setPoolDescriptions(Set<PoolDescription> poolDescriptions) {
        logger.debug("Method: setPoolDescription(" + poolDescriptions + ")");
        this.poolDescriptions = poolDescriptions;
    }

    public void associate(Entity entity) {
        logger.debug("Method: associate(" + entity + ")");
        if (entity instanceof PoolDescription) {
            this.poolDescriptions.add((PoolDescription) entity);
        }
    }

    public void dissociate(Entity entity) {
        logger.debug("Method: dissociate(" + entity + ")");
        if (entity instanceof PoolDescription) {
            this.poolDescriptions.remove((PoolDescription) entity);
        }
    }
}
