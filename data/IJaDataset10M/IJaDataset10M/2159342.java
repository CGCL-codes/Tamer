package uk.ac.lkl.common.util.restlet.server;

import java.sql.SQLException;
import java.util.List;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.restlet.EntityFilter;
import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.migen.system.ai.analysis.Detection;

public class TopLevelCollectionEntityHandler<C> extends AbstractCollectionEntityHandler<Void, C> {

    public TopLevelCollectionEntityHandler(GenericClass<C> childClass, EntityTableManipulator<C> manipulator) throws RestletException {
        super(GenericClass.getSimple(Void.class), childClass, manipulator);
    }

    @Override
    public EntityId<C> addEntity(List<EntityId<?>> idList, EntityId<Void> parentId, C childEntity, EntityMapper mapper) throws SQLException, RestletException {
        return manipulator.insertObject(childEntity, mapper);
    }

    @Override
    public void removeEntity(List<EntityId<?>> idList, EntityId<Detection> entityId) throws RestletException {
        throw new RestletException("Cannot remove Detection");
    }

    @Override
    public List<C> getChildEntityList(List<EntityId<?>> idList, EntityId<Void> parentId, EntityFilter<C> filter, EntityMapper mapper) throws SQLException, RestletException {
        return manipulator.selectAll(mapper);
    }
}
