package org.plazmaforge.framework.erm.query;

import org.plazmaforge.framework.erm.Configuration;
import org.plazmaforge.framework.erm.FetchMode;
import org.plazmaforge.framework.erm.LoadMode;
import org.plazmaforge.framework.erm.criteria.Criteria;
import org.plazmaforge.framework.erm.mapping.Attribute;
import org.plazmaforge.framework.erm.mapping.Entity;
import org.plazmaforge.framework.erm.mapping.Key;

public class QueryBuilder {

    protected QueryInput createQueryInput(Criteria criteria, FetchMode fetchMode, LoadMode loadMode) {
        if (criteria == null) {
            criteria = new Criteria();
        }
        if (fetchMode == null) {
            fetchMode = getConfigFetchMode();
        }
        if (loadMode == null) {
            loadMode = getConfigLoadMode();
        }
        QueryInput input = new QueryInput(criteria, fetchMode, loadMode);
        return input;
    }

    protected QueryInput createQueryInput() {
        return createQueryInput(null, null, null);
    }

    public SelectTemplate createLoadTemplate(Entity entity) {
        return createLoadTemplate(entity, createQueryInput());
    }

    public SelectTemplate createLoadTemplate(Entity entity, QueryInput queryInput) {
        checkEntity(entity);
        checkQueryInput(queryInput);
        SelectTemplate template = new SelectTemplate();
        template.setType(QueryType.LOAD);
        template.setQueryInput(queryInput);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(false);
        template.populateTemplate(entity, entityConfig);
        EntityContext entityContext = template.getEntityContext();
        Attribute[] keys = entity.getGlobalDetailKeys();
        if (isEmpty(keys)) {
            throw new RuntimeException("Can't load entity without key");
        }
        for (Attribute key : keys) {
            template.addFilter(key, entityContext);
        }
        template.prepare();
        return template;
    }

    public SelectTemplate createSelectTemplate(Entity entity) {
        return createSelectTemplate(entity, createQueryInput());
    }

    public SelectTemplate createSelectTemplate(Entity entity, QueryInput queryInput) {
        return createSelectTemplate(entity, queryInput, QueryType.SELECT);
    }

    public SelectTemplate createSelectTemplate(Entity entity, QueryInput queryInput, QueryType queryType) {
        checkEntity(entity);
        SelectTemplate template = new SelectTemplate();
        template.setType(queryType);
        template.setQueryInput(queryInput);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(true);
        template.populateTemplate(entity, entityConfig);
        template.prepare();
        return template;
    }

    /**
     * Return Query Template to insert one entity
     * @param entity
     * @return
     */
    public InsertTemplate createInsertTemplate(Entity entity) {
        checkEntity(entity);
        Attribute[] attributes = QueryTemplate.getAttributes(entity, QueryType.INSERT);
        QueryInput queryInput = createQueryInput();
        queryInput.setAttributes(attributes);
        return createInsertTemplate(entity, queryInput);
    }

    public InsertTemplate createInsertTemplate(Entity entity, QueryInput queryInput) {
        checkEntity(entity);
        checkQueryInput(queryInput);
        InsertTemplate template = new InsertTemplate();
        template.setQueryInput(queryInput);
        TableDef table = template.addTable(entity);
        EntityContext context = new EntityContext(entity, table);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(false);
        context.setEntityConfig(entityConfig);
        template.setEntityContext(context);
        Attribute[] attributes = queryInput.getAttributes();
        template.populateTemplate(context, attributes);
        return template;
    }

    /**
     * Return Query Template to update one entity
     * @param entity
     * @return
     */
    public UpdateTemplate createUpdateTemplate(Entity entity) {
        checkEntity(entity);
        Key key = entity.getGlobalKey();
        Attribute[] attributes = QueryTemplate.getAttributes(entity, QueryType.UPDATE);
        QueryInput queryInput = createQueryInput();
        queryInput.setAttributes(attributes);
        queryInput.setKey(key);
        return createUpdateTemplate(entity, queryInput);
    }

    public UpdateTemplate createUpdateTemplate(Entity entity, QueryInput queryInput) {
        checkEntity(entity);
        checkQueryInput(queryInput);
        Attribute[] attributes = queryInput.getAttributes();
        checkAttributes(attributes);
        Key key = queryInput.getKey();
        checkKey(key);
        Attribute[] detailKeys = key.getDetailAttributes();
        checkKeys(detailKeys);
        UpdateTemplate template = new UpdateTemplate();
        template.setQueryInput(queryInput);
        TableDef table = template.addTable(entity);
        EntityContext entityContext = new EntityContext(entity, table);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(false);
        entityContext.setEntityConfig(entityConfig);
        template.setEntityContext(entityContext);
        template.populateTemplate(entityContext, attributes);
        if (isEmpty(detailKeys)) {
            return template;
        }
        for (Attribute detailKey : detailKeys) {
            template.addFilter(detailKey, entityContext);
        }
        return template;
    }

    /**
     * 
     * @param entity
     * @return
     */
    public DeleteTemplate createDeleteTemplate(Entity entity) {
        checkEntity(entity);
        Key key = entity.getGlobalKey();
        QueryInput queryInput = createQueryInput();
        queryInput.setKey(key);
        return createDeleteTemplate(entity, queryInput);
    }

    /**
     * Return Query Template to delete one entity
     * @param entity
     * @return
     */
    public DeleteTemplate createDeleteTemplate(Entity entity, QueryInput queryInput) {
        checkEntity(entity);
        Attribute[] attributes = queryInput.getAttributes();
        DeleteTemplate template = new DeleteTemplate();
        template.setQueryInput(queryInput);
        TableDef table = template.addTable(entity);
        EntityContext entityContext = new EntityContext(entity, table);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(false);
        entityContext.setEntityConfig(entityConfig);
        template.setEntityContext(entityContext);
        template.populateTemplate(entityContext, attributes);
        Attribute[] keys = entity.getDetailKeys();
        if (isEmpty(keys)) {
            return template;
        }
        for (Attribute key : keys) {
            template.addFilter(key, entityContext);
        }
        return template;
    }

    public SelectTemplate createExistsTemplate(Entity entity, Attribute filter) {
        checkEntity(entity);
        QueryInput queryInput = createQueryInput();
        Attribute[] detailFilters = filter.getDetailAttributes();
        checkKeys(detailFilters);
        SelectTemplate template = new SelectTemplate();
        template.setQueryInput(queryInput);
        TableDef table = template.addTable(entity);
        EntityContext context = new EntityContext(entity, table);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setFetchMode(queryInput.getFetchMode());
        entityConfig.setLoadMode(queryInput.getLoadMode());
        entityConfig.setLazyReference(false);
        entityConfig.setLazyCollection(false);
        context.setEntityConfig(entityConfig);
        template.setEntityContext(context);
        if (isEmpty(detailFilters)) {
            return template;
        }
        for (Attribute detailKey : detailFilters) {
            template.addColumn(detailKey, context);
            template.addFilter(filter, context);
        }
        return template;
    }

    private void checkEntity(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must be not null");
        }
        if (entity.getName() == null) {
            throw new IllegalArgumentException("Entity Name must be not null");
        }
        if (entity.getTableName() == null && entity.hasOwnTable()) {
            throw new IllegalArgumentException("Entity Table must be not null");
        }
    }

    private void checkKey(Attribute key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
    }

    private void checkKeys(Attribute[] keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Keys must be not null");
        }
        if (keys.length == 0) {
            throw new IllegalArgumentException("keys must be not empty");
        }
    }

    private void checkAttributes(Attribute[] attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes must be not null");
        }
        if (attributes.length == 0) {
            throw new IllegalArgumentException("Attributes must be not empty");
        }
    }

    private boolean isEmpty(Attribute[] attributes) {
        return attributes == null || attributes.length == 0;
    }

    private void checkQueryInput(QueryInput queryInput) {
        if (queryInput == null) {
            throw new IllegalArgumentException("QueryInput must be not null");
        }
    }

    private FetchMode getConfigFetchMode() {
        return Configuration.DEFAULT_FETCH_MODE;
    }

    private LoadMode getConfigLoadMode() {
        return Configuration.DEFAULT_LOAD_MODE;
    }
}
