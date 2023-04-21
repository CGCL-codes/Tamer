package net.sf.gaboto.query;

import net.sf.gaboto.Gaboto;
import net.sf.gaboto.GabotoSnapshot;
import net.sf.gaboto.node.pool.EntityPool;
import net.sf.gaboto.node.pool.EntityPoolConfiguration;
import net.sf.gaboto.time.TimeInstant;

/**
 * Simple query that grabs all entities of a specific type.
 * 
 * 
 * @author Arno Mittelbach
 * @version 0.1
 */
public class ListOfTypedEntities extends GabotoQueryImpl {

    private String type;

    private TimeInstant timeInstant;

    private boolean forceCreation;

    public ListOfTypedEntities(String type, TimeInstant ti) {
        super();
        this.type = type;
        this.timeInstant = ti;
        this.forceCreation = true;
    }

    public ListOfTypedEntities(String type, TimeInstant ti, boolean forceCreation) {
        super();
        this.type = type;
        this.timeInstant = ti;
        this.forceCreation = forceCreation;
    }

    public ListOfTypedEntities(Gaboto gaboto, String type, TimeInstant ti) {
        super(gaboto);
        this.type = type;
        this.timeInstant = ti;
        this.forceCreation = true;
    }

    public ListOfTypedEntities(Gaboto gaboto, String type, TimeInstant ti, boolean forceCreation) {
        super(gaboto);
        this.type = type;
        this.timeInstant = ti;
        this.forceCreation = forceCreation;
    }

    @Override
    public int getResultType() {
        return GabotoQueryImpl.RESULT_TYPE_ENTITY_POOL;
    }

    @Override
    public Object execute() {
        GabotoSnapshot snapshot = getGaboto().getSnapshot(timeInstant);
        EntityPoolConfiguration config = new EntityPoolConfiguration(snapshot);
        config.addAcceptedType(type);
        return EntityPool.createFrom(config);
    }

    @Override
    protected void doPrepare() {
        if (forceCreation) forceCreation = true;
    }
}
