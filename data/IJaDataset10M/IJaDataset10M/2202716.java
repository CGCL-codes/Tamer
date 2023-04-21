package org.nakedobjects.plugins.sql.objectstore.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.CollectionMapper;
import org.nakedobjects.plugins.sql.objectstore.DatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.VersionMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMapping;
import org.nakedobjects.runtime.persistence.PersistorUtil;

/** used where there is a one to many association, and the elements are only known to parent */
public class ReversedAutoAssociationMapper extends AbstractAutoMapper implements CollectionMapper {

    private static final Logger LOG = Logger.getLogger(ReversedAutoAssociationMapper.class);

    private NakedObjectAssociation field;

    private final ObjectReferenceMapping idMapping;

    private final VersionMapping versionMapping;

    public ReversedAutoAssociationMapper(final String elemenType, final NakedObjectAssociation field, final String parameterBase, final FieldMappingLookup lookup) {
        super(elemenType, parameterBase, lookup);
        this.field = field;
        idMapping = lookup.createMapping(field.getSpecification());
        versionMapping = lookup.createVersionMapping();
    }

    public void createTables(final DatabaseConnector connection) {
        if (!connection.hasTable(table)) {
            StringBuffer sql = new StringBuffer();
            sql.append("create table ");
            sql.append(table);
            sql.append(" (");
            idMapping.appendColumnDefinitions(sql);
            sql.append(", ");
            for (FieldMapping mapping : fieldMappings) {
                mapping.appendColumnDefinitions(sql);
                sql.append(",");
            }
            sql.append(versionMapping.appendColumnDefinitions());
            sql.append(")");
            connection.update(sql.toString());
        }
        for (int i = 0; collectionMappers != null && i < collectionMappers.length; i++) {
            if (collectionMappers[i].needsTables(connection)) {
                collectionMappers[i].createTables(connection);
            }
        }
    }

    public void loadInternalCollection(final DatabaseConnector connector, final NakedObject parent) {
        NakedObject collection = (NakedObject) field.get(parent);
        if (collection.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            LOG.debug("loading internal collection " + field);
            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            idMapping.appendColumnNames(sql);
            sql.append(", ");
            sql.append(columnList());
            sql.append(" from ");
            sql.append(table);
            sql.append(" where ");
            idMapping.appendUpdateValues(sql, parent);
            Results rs = connector.select(sql.toString());
            List<NakedObject> list = new ArrayList<NakedObject>();
            while (rs.next()) {
                Oid oid = idMapping.recreateOid(rs, specification);
                NakedObject element = getAdapter(specification, oid);
                loadFields(element, rs);
                LOG.debug("  element  " + element.getOid());
                list.add(element);
            }
            CollectionFacet collectionFacet = collection.getSpecification().getFacet(CollectionFacet.class);
            collectionFacet.init(collection, list.toArray(new NakedObject[list.size()]));
            rs.close();
            PersistorUtil.end(collection);
        }
    }

    protected void loadFields(final NakedObject object, final Results rs) {
        PersistorUtil.start(object, ResolveState.RESOLVING);
        for (FieldMapping mapping : fieldMappings) {
            mapping.initializeField(object, rs);
        }
        object.setOptimisticLock(versionMapping.getLock(rs));
        PersistorUtil.end(object);
    }

    public void saveInternalCollection(final DatabaseConnector connector, final NakedObject parent) {
        NakedObject collection = field.get(parent);
        LOG.debug("Saving internal collection " + collection);
        deleteAllElments(connector, parent);
        reinsertElements(connector, parent, collection);
    }

    private void reinsertElements(final DatabaseConnector connector, final NakedObject parent, NakedObject collection) {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + table + " (");
        idMapping.appendColumnNames(sql);
        sql.append(", ");
        sql.append(columnList());
        sql.append(", ");
        sql.append(versionMapping.insertColumns());
        sql.append(") values (");
        idMapping.appendInsertValues(sql, parent);
        sql.append(", ");
        CollectionFacet collectionFacet = field.getFacet(CollectionFacet.class);
        for (NakedObject element : collectionFacet.iterable(collection)) {
            StringBuffer insert = new StringBuffer(sql);
            insert.append(values(element));
            SerialNumberVersion version = new SerialNumberVersion(0, "", new Date());
            insert.append(versionMapping.insertValues(version));
            insert.append(") ");
            connector.insert(insert.toString());
            element.setOptimisticLock(version);
        }
    }

    private void deleteAllElments(final DatabaseConnector connector, final NakedObject parent) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(table);
        sql.append(" where ");
        idMapping.appendUpdateValues(sql, parent);
        connector.update(sql.toString());
    }
}
