package org.middleheaven.domain.store.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.query.Query;
import org.middleheaven.domain.store.AbstractEntityInstanceStorage;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.InstanceStorageException;
import org.middleheaven.domain.store.ReadStrategy;
import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataService;
import org.middleheaven.persistance.DataSet;
import org.middleheaven.persistance.DataSetNotFoundException;
import org.middleheaven.persistance.DataStoreSchema;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.RelatedDataSet;
import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.building.ColumnNameValueLocator;
import org.middleheaven.persistance.criteria.building.ColumnOrderConstraint;
import org.middleheaven.persistance.criteria.building.ColumnValueConstraint;
import org.middleheaven.persistance.criteria.building.ExplicitValueLocator;
import org.middleheaven.persistance.criteria.building.OrderConstraint;
import org.middleheaven.persistance.criteria.building.RelationOperator;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.storage.types.EntityFieldTypeMapper;
import org.middleheaven.storage.types.EntityInstanceTypeMapper;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.classification.LogicOperator;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.FieldValueCriterion;
import org.middleheaven.util.criteria.LogicCriterion;
import org.middleheaven.util.criteria.OrderingCriterion;

/**
 * Realizes a EntityInstanceStorage on top of a dataset oriented persistence mechanism.
 */
public class DataSetEntityInstanceStorage extends AbstractEntityInstanceStorage {

    private DataService dataPersistanceService;

    private DomainModel domaiModel;

    private DomainModelDataSetMapper mapper;

    public DataSetEntityInstanceStorage(DataService dataPersistanceService, DomainModel domainModel, DomainModelDataSetMapper mapper) {
        this.dataPersistanceService = dataPersistanceService;
        this.domaiModel = domainModel;
        this.mapper = mapper;
    }

    @Override
    public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
        try {
            EntityModel entityModel = this.getStoreManager().getDomainModel().getModelFor(criteria.getTargetClass().getName());
            final EntityModelDataSetMapping mapping = mapper.getMappingFor(entityModel);
            DataStoreSchemaName schemaName = mapping.getSchemaName();
            DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(schemaName);
            DataQuery dq = schema.query(interpret(criteria));
            return new DataQueryAdapter<T>(mapping, dq);
        } catch (Exception e) {
            throw this.handleException(e);
        }
    }

    @Override
    public <T> void remove(EntityCriteria<T> criteria) {
        try {
            EntityModel entityModel = this.getStoreManager().getDomainModel().getModelFor(criteria.getTargetClass().getName());
            final EntityModelDataSetMapping mapping = mapper.getMappingFor(entityModel);
            DataStoreSchemaName schemaName = mapping.getSchemaName();
            DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(schemaName);
            if (mapping.isSingleDataSetInheritance()) {
                schema.getDataSet(mapping.getDataSetName()).delete(interpret(criteria));
            } else {
                throw new UnsupportedOperationException("Not implememented yet");
            }
        } catch (Exception e) {
            throw this.handleException(e);
        }
    }

    /**
	 * @param e
	 * @return
	 */
    private InstanceStorageException handleException(Exception e) {
        return new InstanceStorageException(e);
    }

    private <T> DataSetCriteria interpret(EntityCriteria<T> criteria) {
        EntityModelDataSetMapping mapping = this.mapper.getMappingFor(this.domaiModel.getModelFor(criteria.getTargetClass().getName()));
        DataStoreSchema schema = this.dataPersistanceService.getDataStoreSchema(mapping.getSchemaName());
        DataSetCriteria dsCriteria = new DataSetCriteria();
        dsCriteria.setDistinct(criteria.isDistinct());
        dsCriteria.setMaxCount(criteria.getCount());
        dsCriteria.setOffset(criteria.getStart());
        interpretLogic(dsCriteria, criteria);
        interpretGroup(dsCriteria, criteria);
        interpretOrder(dsCriteria, criteria);
        EntityInstanceTypeMapper et = mapping.getTypeMapper();
        Set<String> tablesAdded = new HashSet<String>();
        for (DataColumnModel cm : et.getColumns()) {
            if (tablesAdded.add(cm.getDataSetModel().getName())) {
                RelatedDataSet rd = new RelatedDataSet(new LogicConstraint(LogicOperator.and()), RelationOperator.INNER_JOIN);
                rd.setSourceDataSetModel(cm.getDataSetModel());
                dsCriteria.addRelatedDataSet(rd);
            }
        }
        return dsCriteria;
    }

    private <T> void interpretOrder(DataSetCriteria dsCriteria, EntityCriteria<T> criteria) {
        for (OrderingCriterion ordering : criteria.ordering()) {
            DataColumnModel[] columns = mapper.getEntityFieldTypeMapper(ordering.getFieldName()).getColumns();
            OrderConstraint orderConstraint = new ColumnOrderConstraint(columns[0].getName(), !ordering.isDescendant());
            dsCriteria.addOrderingConstraint(orderConstraint);
        }
    }

    private <T> void interpretGroup(DataSetCriteria dsCriteria, EntityCriteria<T> criteria) {
    }

    private <T> void interpretLogic(DataSetCriteria dsCriteria, EntityCriteria<T> criteria) {
        interpretLogicConstraint(dsCriteria, criteria.constraints());
    }

    private <T> void interpretLogicConstraint(DataSetCriteria dsCriteria, LogicCriterion criterion) {
        final LogicConstraint constraint = new LogicConstraint(criterion.getOperator());
        dsCriteria.addLogicConstraint(constraint);
        for (Criterion c : criterion) {
            if (c instanceof FieldValueCriterion) {
                final FieldValueCriterion f = (FieldValueCriterion) c;
                final EntityFieldTypeMapper entityFieldTypeMapper = mapper.getEntityFieldTypeMapper(f.getFieldName());
                DataRow row = new DataRow() {

                    @Override
                    public Iterator<DataColumn> iterator() {
                        throw new UnsupportedOperationException("Not implememented yet");
                    }

                    @Override
                    public DataColumn getColumn(final QualifiedName name) {
                        return new DataColumn() {

                            @Override
                            public DataColumnModel getModel() {
                                throw new UnsupportedOperationException("Not implememented yet");
                            }

                            @Override
                            public Object getValue() {
                                throw new UnsupportedOperationException("Not implememented yet");
                            }

                            @Override
                            public void setValue(Object value) {
                                ColumnValueConstraint v = new ColumnValueConstraint(new ColumnNameValueLocator(name), f.getOperator(), new ExplicitValueLocator(value));
                                constraint.addConstraint(v);
                            }
                        };
                    }
                };
                entityFieldTypeMapper.write(f.valueHolder().getValue(), row, entityFieldTypeMapper.getColumns());
            } else if (c instanceof LogicCriterion) {
                interpretLogicConstraint(dsCriteria, (LogicCriterion) c);
            }
        }
    }

    private Map<DataSet, Collection<DataRow>> classify(Collection<EntityInstance> objs) throws DataSetNotFoundException {
        Map<DataSet, Collection<DataRow>> groups = new HashMap<DataSet, Collection<DataRow>>();
        for (EntityInstance i : objs) {
            EntityModel entityModel = i.getEntityModel();
            final EntityModelDataSetMapping mapping = mapper.getMappingFor(entityModel);
            DataStoreSchema schema = dataPersistanceService.getDataStoreSchema(mapping.getSchemaName());
            DataSet dataSet = schema.getDataSet(mapping.getDataSetName());
            for (DataRow row : mapping.write(i)) {
                Collection<DataRow> rows = groups.get(dataSet);
                if (rows == null) {
                    rows = new LinkedList<DataRow>();
                    groups.put(dataSet, rows);
                }
                rows.add(row);
            }
        }
        return groups;
    }

    @Override
    public void update(Collection<EntityInstance> objs) {
        try {
            for (Map.Entry<DataSet, Collection<DataRow>> entry : classify(objs).entrySet()) {
                entry.getKey().update(entry.getValue());
            }
        } catch (Exception e) {
            throw this.handleException(e);
        }
    }

    @Override
    public void insert(Collection<EntityInstance> objs) {
        try {
            for (Map.Entry<DataSet, Collection<DataRow>> entry : classify(objs).entrySet()) {
                entry.getKey().insert(entry.getValue());
            }
        } catch (Exception e) {
            throw this.handleException(e);
        }
    }

    @Override
    public void remove(Collection<EntityInstance> objs) {
        try {
            for (Map.Entry<DataSet, Collection<DataRow>> entry : classify(objs).entrySet()) {
                entry.getKey().insert(entry.getValue());
            }
        } catch (Exception e) {
            throw this.handleException(e);
        }
    }

    protected Sequence<Long> getSeedSequence(EntityInstance instance) {
        final EntityModelDataSetMapping mapping = mapper.getMappingFor(instance.getEntityModel());
        return dataPersistanceService.getDataStoreSchema(mapping.getSchemaName()).getSequence(mapping.getDataSetName());
    }
}
