package com.avaje.ebean.server.deploy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.persistence.PersistenceException;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.LazyLoadEbeanServer;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.internal.SpiQuery;
import com.avaje.ebean.server.core.PersistRequest;
import com.avaje.ebean.server.deploy.id.ImportedId;
import com.avaje.ebean.server.deploy.meta.DeployBeanPropertyAssocMany;
import com.avaje.ebean.server.lib.util.StringHelper;

/**
 * Property mapped to a List Set or Map.
 */
public class BeanPropertyAssocMany<T> extends BeanPropertyAssoc<T> {

    /**
	 * Join for manyToMany intersection table.
	 */
    final TableJoin intersectionJoin;

    /**
	 * For ManyToMany this is the Inverse join used to build reference queries.
	 */
    final TableJoin inverseJoin;

    /**
	 * Derived list of exported property and matching foreignKey
	 */
    ExportedProperty[] exportedProperties;

    /**
	 * Property on the 'child' bean that links back to the 'master'.
	 */
    BeanProperty childMasterProperty;

    boolean embeddedExportedProperties;

    /**
	 * Flag to indicate that this is a unidirectional relationship.
	 */
    final boolean unidirectional;

    /**
	 * Flag to indicate manyToMany relationship.
	 */
    final boolean manyToMany;

    final String fetchOrderBy;

    BeanProperty mapKeyProperty;

    final String mapKey;

    /**
	 * The type of the many, set, list or map.
	 */
    final Query.Type manyType;

    final String serverName;

    BeanCollectionHelp<T> help;

    ImportedId importedId;

    /**
	 * Create this property.
	 */
    public BeanPropertyAssocMany(BeanDescriptorMap owner, BeanDescriptor<?> descriptor, DeployBeanPropertyAssocMany<T> deploy) {
        super(owner, descriptor, deploy);
        this.unidirectional = deploy.isUnidirectional();
        this.manyToMany = deploy.isManyToMany();
        this.serverName = descriptor.getServerName();
        this.manyType = deploy.getManyType();
        this.mapKey = deploy.getMapKey();
        this.fetchOrderBy = deploy.getFetchOrderBy();
        this.intersectionJoin = deploy.createIntersectionTableJoin();
        this.inverseJoin = deploy.createInverseTableJoin();
    }

    public void initialise() {
        super.initialise();
        if (!isTransient) {
            help = BeanCollectionHelpFactory.create(this);
            if (manyToMany) {
                importedId = createImportedId(this, targetDescriptor, tableJoin);
            } else {
                childMasterProperty = initChildMasterProperty();
            }
            if (mapKey != null) {
                mapKeyProperty = initMapKeyProperty();
            }
            exportedProperties = createExported();
            if (exportedProperties.length > 0) {
                embeddedExportedProperties = exportedProperties[0].isEmbedded();
            }
        }
    }

    /**
	 * Set the lazy load server to help create reference collections (that lazy
	 * load on demand).
	 */
    public void setEbeanServer(LazyLoadEbeanServer ebeanServer) {
        if (help != null) {
            help.setEbeanServer(ebeanServer);
        }
    }

    /**
	 * Ignore changes for Many properties.
	 */
    public boolean hasChanged(Object bean, Object oldValues) {
        return false;
    }

    @Override
    public void appendSelect(DbSqlContext ctx) {
    }

    @Override
    public Object readSet(DbReadContext ctx, Object bean, Class<?> type) throws SQLException {
        return null;
    }

    @Override
    public Object read(DbReadContext ctx) throws SQLException {
        return null;
    }

    @Override
    public boolean isValueLoaded(Object value) {
        if (value instanceof BeanCollection<?>) {
            return ((BeanCollection<?>) value).isPopulated();
        }
        return true;
    }

    public void add(BeanCollection<?> collection, Object bean) {
        help.add(collection, bean);
    }

    @Override
    public InvalidValue validateCascade(Object manyValue) {
        ArrayList<InvalidValue> errs = help.validate(manyValue);
        if (errs == null) {
            return null;
        } else {
            return new InvalidValue("recurse.many", targetDescriptor.getFullName(), manyValue, InvalidValue.toArray(errs));
        }
    }

    /**
	 * Refresh the appropriate list set or map.
	 */
    public void refresh(EbeanServer server, Query<?> query, Transaction t, Object parentBean) {
        help.refresh(server, query, t, parentBean);
    }

    /**
	 * Returns true.
	 */
    @Override
    public boolean containsMany() {
        return true;
    }

    /**
	 * Return the many type.
	 */
    public Query.Type getManyType() {
        return manyType;
    }

    /**
	 * Return true if this is many to many.
	 */
    public boolean isManyToMany() {
        return manyToMany;
    }

    /**
	 * ManyToMany only, join from local table to intersection table.
	 */
    public TableJoin getIntersectionTableJoin() {
        return intersectionJoin;
    }

    /**
	 * Set the join properties from the parent bean to the child bean.
	 * This is only valid for OneToMany and NOT valid for ManyToMany.
	 */
    public void setJoinValuesToChild(PersistRequest request, Object parent, Object child, Object mapKeyValue) {
        if (mapKeyProperty != null) {
            mapKeyProperty.setValue(child, mapKeyValue);
        }
        if (!manyToMany) {
            if (childMasterProperty != null) {
                childMasterProperty.setValue(child, parent);
            } else {
            }
        }
    }

    /**
	 * Return the order by clause used to order the fetching of the data for
	 * this list, set or map.
	 */
    public String getFetchOrderBy() {
        return fetchOrderBy;
    }

    /**
	 * Return the default mapKey when returning a Map.
	 */
    public String getMapKey() {
        return mapKey;
    }

    public void createReference(Object parentBean, ObjectGraphNode profilePoint, boolean readOnly, boolean sharedInstance) {
        BeanCollection<?> ref = help.createReference(parentBean, serverName, name, profilePoint);
        if (sharedInstance) {
            ref.setSharedInstance();
        } else if (readOnly) {
            ref.setReadOnly(true);
        }
        setValue(parentBean, ref);
    }

    public BeanCollection<?> createEmpty() {
        return help.createEmpty();
    }

    public void setPredicates(Query<?> query, Object parentBean) {
        if (manyToMany) {
            SpiQuery<?> iq = (SpiQuery<?>) query;
            iq.setIncludeTableJoin(inverseJoin);
        }
        ExportedProperty[] expProps = getExported();
        if (embeddedExportedProperties) {
            BeanProperty[] uids = descriptor.propertiesId();
            parentBean = uids[0].getValue(parentBean);
        }
        for (int i = 0; i < expProps.length; i++) {
            Object val = expProps[i].getValue(parentBean);
            String fkColumn = expProps[i].getForeignDbColumn();
            if (!manyToMany) {
                fkColumn = targetDescriptor.getBaseTableAlias() + "." + fkColumn;
            } else {
                fkColumn = "int_." + fkColumn;
            }
            query.where().eq(fkColumn, val);
        }
        if (extraWhere != null) {
            String ta = targetDescriptor.getBaseTableAlias();
            String where = StringHelper.replaceString(extraWhere, "${ta}", ta);
            query.where().raw(where);
        }
        if (fetchOrderBy != null) {
            query.orderBy(fetchOrderBy);
        }
    }

    private ExportedProperty[] getExported() {
        if (exportedProperties == null) {
            exportedProperties = createExported();
            embeddedExportedProperties = exportedProperties[0].isEmbedded();
        }
        return exportedProperties;
    }

    /**
	 * Create the array of ExportedProperty used to build reference objects.
	 */
    private ExportedProperty[] createExported() {
        BeanProperty[] uids = descriptor.propertiesId();
        ArrayList<ExportedProperty> list = new ArrayList<ExportedProperty>();
        if (uids.length == 1 && uids[0].isEmbedded()) {
            BeanPropertyAssocOne<?> one = (BeanPropertyAssocOne<?>) uids[0];
            BeanDescriptor<?> targetDesc = one.getTargetDescriptor();
            BeanProperty[] emIds = targetDesc.propertiesBaseScalar();
            for (int i = 0; i < emIds.length; i++) {
                ExportedProperty expProp = findMatch(true, emIds[i]);
                list.add(expProp);
            }
        } else {
            for (int i = 0; i < uids.length; i++) {
                ExportedProperty expProp = findMatch(false, uids[i]);
                list.add(expProp);
            }
        }
        return (ExportedProperty[]) list.toArray(new ExportedProperty[list.size()]);
    }

    /**
	 * Find the matching foreignDbColumn for a given local property.
	 */
    private ExportedProperty findMatch(boolean embedded, BeanProperty prop) {
        String matchColumn = prop.getDbColumn();
        String searchTable;
        TableJoinColumn[] columns;
        if (manyToMany) {
            columns = intersectionJoin.columns();
            searchTable = intersectionJoin.getTable();
        } else {
            columns = tableJoin.columns();
            searchTable = tableJoin.getTable();
        }
        for (int i = 0; i < columns.length; i++) {
            String matchTo = columns[i].getLocalDbColumn();
            if (matchColumn.equalsIgnoreCase(matchTo)) {
                String foreignCol = columns[i].getForeignDbColumn();
                if (manyToMany) {
                    return new ExportedProperty(embedded, foreignCol, prop);
                } else {
                    return new ExportedProperty(embedded, foreignCol, prop);
                }
            }
        }
        String msg = "Error with the Join on [" + getFullBeanName() + "]. Could not find the matching foreign key for [" + matchColumn + "] in table[" + searchTable + "]?" + " Perhaps using a @JoinColumn with the name/referencedColumnName attributes swapped?";
        throw new PersistenceException(msg);
    }

    /**
	 * Return the child property that links back to the master bean.
	 * <p>
	 * Note that childMasterProperty will be null if a field is used instead of
	 * a ManyToOne bean association.
	 * </p>
	 */
    private BeanProperty initChildMasterProperty() {
        if (unidirectional) {
            return null;
        }
        Class<?> beanType = descriptor.getBeanType();
        BeanDescriptor<?> targetDesc = getTargetDescriptor();
        BeanPropertyAssocOne<?>[] ones = targetDesc.propertiesOne();
        for (int i = 0; i < ones.length; i++) {
            BeanPropertyAssocOne<?> prop = (BeanPropertyAssocOne<?>) ones[i];
            if (mappedBy != null) {
                if (mappedBy.equalsIgnoreCase(prop.getName())) {
                    return prop;
                }
            } else {
                if (prop.getTargetType().equals(beanType)) {
                    return prop;
                }
            }
        }
        String msg = "Can not find Master [" + beanType + "] in Child[" + targetDesc + "]";
        throw new RuntimeException(msg);
    }

    /**
	 * Search for and return the mapKey property.
	 */
    private BeanProperty initMapKeyProperty() {
        BeanDescriptor<?> targetDesc = getTargetDescriptor();
        Iterator<BeanProperty> it = targetDesc.propertiesAll();
        while (it.hasNext()) {
            BeanProperty prop = it.next();
            if (mapKey.equalsIgnoreCase(prop.getName())) {
                return prop;
            }
        }
        String from = descriptor.getFullName();
        String to = targetDesc.getFullName();
        String msg = from + ": Could not find mapKey property [" + mapKey + "] on [" + to + "]";
        throw new PersistenceException(msg);
    }

    public IntersectionRow buildManyToManyMapBean(Object parent, Object other) {
        IntersectionRow row = new IntersectionRow(intersectionJoin.getTable());
        buildExport(row, parent);
        buildImport(row, other);
        return row;
    }

    /**
	 * Set the predicates for lazy loading of the association.
	 * Handles predicates for both OneToMany and ManyToMany.
	 */
    private void buildExport(IntersectionRow row, Object parentBean) {
        BeanProperty[] uids = descriptor.propertiesId();
        ExportedProperty[] expProps = getExported();
        if (embeddedExportedProperties) {
            parentBean = uids[0].getValue(parentBean);
        }
        for (int i = 0; i < expProps.length; i++) {
            Object val = expProps[i].getValue(parentBean);
            String fkColumn = expProps[i].getForeignDbColumn();
            row.put(fkColumn, val);
        }
    }

    /**
	 * Set the predicates for lazy loading of the association.
	 * Handles predicates for both OneToMany and ManyToMany.
	 */
    private void buildImport(IntersectionRow row, Object otherBean) {
        importedId.buildImport(row, otherBean);
    }
}
