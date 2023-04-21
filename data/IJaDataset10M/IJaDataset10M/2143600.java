package modelibra.wicket.model;

import java.util.List;
import org.apache.wicket.model.Model;
import org.modelibra.IEntities;
import org.modelibra.IEntity;
import org.modelibra.config.ConceptConfig;

@SuppressWarnings("serial")
public class EntitiesModel extends Model {

    public EntitiesModel(IEntities<?> entities) {
        super(entities);
    }

    public List<IEntity<?>> getEntityList() {
        return (List<IEntity<?>>) getEntities().getList();
    }

    public IEntities<?> getEntities() {
        return (IEntities<?>) getObject();
    }

    public ConceptConfig getConceptConfig() {
        return getEntities().getConceptConfig();
    }
}
