package research.model;

import java.util.ArrayList;
import java.util.List;
import research.domain.TestParameterValue;
import research.entity.Entity;
import research.entity.EntityType;

public class TestParameterHierarchy implements IHierarchyProvider {

    @Override
    public List<Entity> getChildren(Entity entity) {
        return new ArrayList<Entity>();
    }

    @Override
    public Entity getParent(Entity entity) {
        return ((TestParameterValue) entity).getTestParameter();
    }

    @Override
    public EntityType getRootType() {
        return null;
    }

    @Override
    public boolean hasChildren(Entity entity) {
        return false;
    }

    @Override
    public boolean supportsType(EntityType type) {
        return false;
    }
}
