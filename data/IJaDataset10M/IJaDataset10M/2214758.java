package net.sourceforge.greenvine.generator.helper.dbextractor.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.greenvine.database.Column;
import net.sourceforge.greenvine.database.ColumnConstraint;
import net.sourceforge.greenvine.database.ColumnRef;
import net.sourceforge.greenvine.database.Database;
import net.sourceforge.greenvine.database.ForeignKey;
import net.sourceforge.greenvine.database.PrimaryKey;
import net.sourceforge.greenvine.database.Table;
import net.sourceforge.greenvine.database.UniqueKey;
import net.sourceforge.greenvine.database.propertytypes.types.ColumnDatatypeType;
import net.sourceforge.greenvine.generator.helper.model.ModelHelper;
import net.sourceforge.greenvine.model.BaseAssociationType;
import net.sourceforge.greenvine.model.ChildTableRef;
import net.sourceforge.greenvine.model.ComplexIdentity;
import net.sourceforge.greenvine.model.ConstrainedIdentity;
import net.sourceforge.greenvine.model.Entity;
import net.sourceforge.greenvine.model.Identity;
import net.sourceforge.greenvine.model.IdentityProperty;
import net.sourceforge.greenvine.model.IdentityPropertyType;
import net.sourceforge.greenvine.model.JoinTableRef;
import net.sourceforge.greenvine.model.Key;
import net.sourceforge.greenvine.model.KeyType;
import net.sourceforge.greenvine.model.ManyToMany;
import net.sourceforge.greenvine.model.ManyToOne;
import net.sourceforge.greenvine.model.ManyToOneRef;
import net.sourceforge.greenvine.model.Model;
import net.sourceforge.greenvine.model.OneToMany;
import net.sourceforge.greenvine.model.OneToOne;
import net.sourceforge.greenvine.model.OneToOneRef;
import net.sourceforge.greenvine.model.ParentTableRef;
import net.sourceforge.greenvine.model.RelatedKey;
import net.sourceforge.greenvine.model.SimpleIdentity;
import net.sourceforge.greenvine.model.SimpleProperty;
import net.sourceforge.greenvine.model.SimplePropertyRef;
import net.sourceforge.greenvine.model.SimplePropertyType;
import net.sourceforge.greenvine.model.ThisKey;
import net.sourceforge.greenvine.model.UniqueConstraint;
import net.sourceforge.greenvine.model.UniqueConstraintType;
import net.sourceforge.greenvine.model.propertytypes.types.PropertyDatatypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class ModelDatabaseExtractor {

    private final Model model;

    private final ModelHelper modelHelper;

    public ModelDatabaseExtractor(Model model) {
        this.model = model;
        this.modelHelper = new ModelHelper(model);
    }

    public Database extractDatabase() {
        inferDatabaseMetadata();
        Database database = new Database();
        database.setName(model.getData().getDatasource().getName());
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            extractTableFromEntity(entity, database);
        }
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            extractJoinTablesFromEntity(entity, database);
        }
        return database;
    }

    public void inferDatabaseMetadata() {
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            if (entity.getTableName() == null || entity.getTableName().length() == 0) {
                entity.setTableName(getTableNameFromEntityName(entity.getName()));
            }
        }
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            inferPrimaryKeyColumns(entity);
            inferSimplePropertyColumns(entity);
        }
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            inferTableReferences(entity);
        }
    }

    private void inferTableReferences(Entity entity) {
        for (ManyToMany manyToMany : entity.getManyToMany()) {
            if (manyToMany.getJoinTableRef() == null) {
                Entity related = getEntityByName(manyToMany.getEntityRef().getEntityName());
                ManyToMany otherSide = getOtherSide(entity, related, manyToMany);
                JoinTableRef jtr = createJoinTableRef(entity, manyToMany, related, otherSide);
                manyToMany.setJoinTableRef(jtr);
            }
        }
        for (ManyToOne manyToOne : entity.getManyToOne()) {
            if (manyToOne.getJoinTableRef() == null && manyToOne.getParentTableRef() == null) {
                Entity related = getEntityByName(manyToOne.getEntityRef().getEntityName());
                OneToMany otherSide = getOtherSide(entity, related, manyToOne);
                if (!manyToOne.getNotNull()) {
                    JoinTableRef jtr = createJoinTableRef(entity, manyToOne, related, otherSide);
                    manyToOne.setJoinTableRef(jtr);
                } else {
                    ParentTableRef rtr = createParentTableRef(manyToOne, entity, related);
                    manyToOne.setParentTableRef(rtr);
                }
            }
        }
        for (OneToMany oneToMany : entity.getOneToMany()) {
            Entity related = getEntityByName(oneToMany.getEntityRef().getEntityName());
            ManyToOne otherSide = getOtherSide(entity, related, oneToMany);
            if (oneToMany.getJoinTableRef() == null && oneToMany.getChildTableRef() == null) {
                if (!otherSide.getNotNull()) {
                    JoinTableRef jtr = createJoinTableRef(entity, oneToMany, related, otherSide);
                    oneToMany.setJoinTableRef(jtr);
                } else {
                    ChildTableRef rtr = createChildTableRef(oneToMany, entity, related);
                    oneToMany.setChildTableRef(rtr);
                }
            }
        }
        for (OneToOne oneToOne : entity.getOneToOne()) {
            Entity related = getEntityByName(oneToOne.getEntityRef().getEntityName());
            OneToOne otherSide = getOtherSide(entity, related, oneToOne);
            if (oneToOne.getJoinTableRef() == null && oneToOne.getParentTableRef() == null && oneToOne.getChildTableRef() == null) {
                if (!oneToOne.getNotNull() && !otherSide.getNotNull()) {
                    JoinTableRef jtr = createJoinTableRef(entity, oneToOne, related, otherSide);
                    oneToOne.setJoinTableRef(jtr);
                } else {
                    if (oneToOne.getNotNull()) {
                        ParentTableRef rtr = createParentTableRef(oneToOne, entity, related);
                        oneToOne.setParentTableRef(rtr);
                    } else {
                        ChildTableRef rtr = createChildTableRef(oneToOne, entity, related);
                        oneToOne.setChildTableRef(rtr);
                    }
                }
            }
        }
    }

    private OneToOne getOtherSide(Entity entity, Entity related, OneToOne otherSide) {
        for (OneToOne oneToOne : related.getOneToOne()) {
            if (oneToOne.getEntityRef().getEntityName().equals(entity.getName())) {
                if (oneToOne.getName().equals(otherSide.getEntityRef().getInverseRef().getPropertyName())) {
                    return oneToOne;
                }
            }
        }
        throw new RuntimeException("Other side of one-to-one not found for entity: " + entity.getName() + " related to entity: " + related.getName());
    }

    private ManyToOne getOtherSide(Entity entity, Entity related, OneToMany otherSide) {
        for (ManyToOne manyToOne : related.getManyToOne()) {
            if (manyToOne.getEntityRef().getEntityName().equals(entity.getName())) {
                if (manyToOne.getName().equals(otherSide.getEntityRef().getInverseRef().getPropertyName())) {
                    return manyToOne;
                }
            }
        }
        throw new RuntimeException("Other side of many-to-one not found for entity: " + entity.getName() + " related to entity: " + related.getName());
    }

    private OneToMany getOtherSide(Entity entity, Entity related, ManyToOne otherSide) {
        for (OneToMany oneToMany : related.getOneToMany()) {
            if (oneToMany.getEntityRef().getEntityName().equals(entity.getName())) {
                if (oneToMany.getName().equals(otherSide.getEntityRef().getInverseRef().getPropertyName())) {
                    return oneToMany;
                }
            }
        }
        throw new RuntimeException("Other side of many-to-one not found for entity: " + entity.getName() + " related to entity: " + related.getName());
    }

    private ManyToMany getOtherSide(Entity entity, Entity related, ManyToMany otherSide) {
        for (ManyToMany manyToMany : related.getManyToMany()) {
            if (manyToMany.getEntityRef().getEntityName().equals(entity.getName())) {
                if (manyToMany.getName().equals(otherSide.getEntityRef().getInverseRef().getPropertyName())) {
                    return manyToMany;
                }
            }
        }
        throw new RuntimeException("Other side of many-to-one not found for entity: " + entity.getName() + " related to entity: " + related.getName());
    }

    private ParentTableRef createParentTableRef(ManyToOne manyToOne, Entity entity, Entity related) {
        ParentTableRef rtr = new ParentTableRef();
        inferForeignKeyColumns(manyToOne, related.getIdentity(), rtr);
        Key thisKey = createKey(rtr.getColumn(), entity, related);
        rtr.setKey(thisKey);
        return rtr;
    }

    private ParentTableRef createParentTableRef(OneToOne oneToOne, Entity entity, Entity related) {
        ParentTableRef rtr = new ParentTableRef();
        Key thisKey = null;
        if (oneToOne.getConstrained()) {
            inferForeignKeyColumnsConstrained(oneToOne, related.getIdentity(), rtr);
            thisKey = createKeyConstrained(entity, related);
        } else {
            inferForeignKeyColumns(oneToOne, related.getIdentity(), rtr);
            thisKey = createKey(entity, related);
        }
        rtr.setKey(thisKey);
        return rtr;
    }

    private ChildTableRef createChildTableRef(OneToOne oneToOne, Entity entity, Entity related) {
        OneToOne otherSide = getOtherSide(entity, related, oneToOne);
        ChildTableRef rtr = new ChildTableRef();
        Key thisKey = null;
        if (otherSide.getConstrained()) {
            thisKey = createKeyConstrained(related, entity);
        } else {
            thisKey = createKey(related, entity);
        }
        rtr.setKey(thisKey);
        return rtr;
    }

    private ChildTableRef createChildTableRef(OneToMany oneToMany, Entity entity, Entity related) {
        ChildTableRef rtr = new ChildTableRef();
        Key thisKey = createKey(related, entity);
        rtr.setKey(thisKey);
        return rtr;
    }

    private Key createKey(net.sourceforge.greenvine.model.Column[] fkColumns, Entity entity, Entity related) {
        Key key = new Key();
        key.setReferencedTable(related.getTableName());
        key.setReferencingTable(entity.getTableName());
        createColumnConstraints(key, related.getIdentity(), fkColumns);
        return key;
    }

    private Key createKeyConstrained(Entity entity, Entity related) {
        Key key = new Key();
        key.setReferencedTable(related.getTableName());
        key.setReferencingTable(entity.getTableName());
        createColumnConstraints(key, related.getIdentity(), "");
        return key;
    }

    private Key createKey(Entity entity, Entity related) {
        Key key = new Key();
        key.setReferencedTable(related.getTableName());
        key.setReferencingTable(entity.getTableName());
        createColumnConstraints(key, related.getIdentity(), "FK_");
        return key;
    }

    private JoinTableRef createJoinTableRef(Entity entity, BaseAssociationType entityAssociation, Entity related, BaseAssociationType relatedAssociation) {
        JoinTableRef jtr = new JoinTableRef();
        ThisKey thisKey = createThisKey(entity, entityAssociation, related, relatedAssociation);
        jtr.setThisKey(thisKey);
        RelatedKey relatedKey = createRelatedKey(entity, entityAssociation, related, relatedAssociation);
        jtr.setRelatedKey(relatedKey);
        return jtr;
    }

    private ThisKey createThisKey(Entity entity, BaseAssociationType entityAssociation, Entity related, BaseAssociationType relatedAssociation) {
        ThisKey key = new ThisKey();
        key.setReferencedTable(entity.getTableName());
        key.setReferencingTable(getJoinTableName(entity, entityAssociation, related, relatedAssociation));
        if (entity.getName().equals(related.getName())) {
            createColumnConstraints(key, entity.getIdentity(), relatedAssociation.getName(), "FK_");
        } else {
            createColumnConstraints(key, entity.getIdentity(), "FK_");
        }
        return key;
    }

    private RelatedKey createRelatedKey(Entity entity, BaseAssociationType entityAssociation, Entity related, BaseAssociationType relatedAssociation) {
        RelatedKey key = new RelatedKey();
        key.setReferencedTable(related.getTableName());
        key.setReferencingTable(getJoinTableName(entity, entityAssociation, related, relatedAssociation));
        if (entity.getName().equals(related.getName())) {
            createColumnConstraints(key, related.getIdentity(), entityAssociation.getName(), "FK_");
        } else {
            createColumnConstraints(key, related.getIdentity(), "FK_");
        }
        return key;
    }

    private String getJoinTableName(Entity entity, BaseAssociationType entityAssociation, Entity related, BaseAssociationType relatedAssociation) {
        Set<String> names = new TreeSet<String>();
        names.add(entity.getName());
        names.add(related.getName());
        if (entity.getName().equals(related.getName())) {
            names.add(entityAssociation.getName());
            names.add(relatedAssociation.getName());
        }
        String tableName = "DBO.TBL";
        for (String name : names) {
            tableName = tableName + "_" + name.toUpperCase();
        }
        return tableName;
    }

    private void createColumnConstraints(KeyType key, Identity identity, String columnPrefix) {
        if (identity.getSimpleIdentity() != null) {
            net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
            colCon.setReferencedColumn(identity.getSimpleIdentity().getColumn().getName());
            colCon.setReferencingColumn(columnPrefix + identity.getSimpleIdentity().getColumn().getName());
            key.addColumnConstraint(colCon);
        } else {
            for (IdentityProperty identityProperty : identity.getComplexIdentity().getIdentityProperty()) {
                net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
                colCon.setReferencedColumn(identityProperty.getColumn().getName());
                colCon.setReferencingColumn(columnPrefix + identityProperty.getColumn().getName());
                key.addColumnConstraint(colCon);
            }
        }
    }

    private void createColumnConstraints(KeyType key, Identity identity, String name, String columnPrefix) {
        if (identity.getSimpleIdentity() != null) {
            net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
            colCon.setReferencedColumn(identity.getSimpleIdentity().getColumn().getName());
            colCon.setReferencingColumn(columnPrefix + getColumnNameFromPropertyName(name) + "_ID");
            key.addColumnConstraint(colCon);
        } else {
            for (IdentityProperty identityProperty : identity.getComplexIdentity().getIdentityProperty()) {
                net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
                colCon.setReferencedColumn(identityProperty.getColumn().getName());
                colCon.setReferencingColumn(columnPrefix + getColumnNameFromPropertyName(identityProperty.getName()) + "_" + getColumnNameFromPropertyName(name) + "_ID");
                key.addColumnConstraint(colCon);
            }
        }
    }

    private void createColumnConstraints(KeyType key, Identity identity, net.sourceforge.greenvine.model.Column[] fkColumns) {
        if (identity.getSimpleIdentity() != null) {
            net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
            colCon.setReferencedColumn(identity.getSimpleIdentity().getColumn().getName());
            colCon.setReferencingColumn(fkColumns[0].getName());
            key.addColumnConstraint(colCon);
        } else {
            int i = 0;
            for (IdentityProperty identityProperty : identity.getComplexIdentity().getIdentityProperty()) {
                net.sourceforge.greenvine.model.ColumnConstraint colCon = new net.sourceforge.greenvine.model.ColumnConstraint();
                colCon.setReferencedColumn(identityProperty.getColumn().getName());
                colCon.setReferencingColumn(fkColumns[i].getName());
                key.addColumnConstraint(colCon);
                i++;
            }
        }
    }

    private void inferPrimaryKeyColumns(Entity entity) {
        Identity identity = entity.getIdentity();
        if (identity != null) {
            if (identity.getSimpleIdentity() != null) {
                inferSimpleIdentityColumns(identity.getSimpleIdentity());
            } else if (identity.getConstrainedIdentity() != null) {
                inferConstrainedIdentityColumns(identity.getConstrainedIdentity());
            } else {
                inferComplexIdentityColumns(identity.getComplexIdentity());
            }
        }
    }

    private void inferConstrainedIdentityColumns(ConstrainedIdentity constrainedIdentity) {
    }

    private void inferComplexIdentityColumns(ComplexIdentity complexIdentity) {
        for (IdentityProperty identityProperty : complexIdentity.getIdentityProperty()) {
            if (identityProperty.getColumn() == null) {
                net.sourceforge.greenvine.model.Column column = getColumnFromIdentityProperty(identityProperty);
                identityProperty.setColumn(column);
            }
        }
    }

    private void inferSimpleIdentityColumns(SimpleIdentity simpleIdentity) {
        if (simpleIdentity.getColumn() == null) {
            net.sourceforge.greenvine.model.Column column = getColumnFromSimpleIdentity(simpleIdentity);
            simpleIdentity.setColumn(column);
        }
    }

    private net.sourceforge.greenvine.model.Column getColumnFromSimpleIdentity(SimpleIdentity simpleIdentity) {
        net.sourceforge.greenvine.model.Column column = new net.sourceforge.greenvine.model.Column();
        column.setName(getColumnNameFromPropertyName(simpleIdentity.getName()));
        column.setType(getColumnTypeFromProperyType(simpleIdentity.getType()));
        column.setNotNull(true);
        return column;
    }

    private net.sourceforge.greenvine.model.Column getColumnFromIdentityProperty(IdentityPropertyType simpleIdentity) {
        net.sourceforge.greenvine.model.Column column = new net.sourceforge.greenvine.model.Column();
        column.setName(getColumnNameFromPropertyName(simpleIdentity.getName()));
        column.setType(getColumnTypeFromProperyType(simpleIdentity.getType()));
        column.setNotNull(true);
        return column;
    }

    private net.sourceforge.greenvine.model.Column getColumnFromProperty(SimplePropertyType simpleProperty) {
        net.sourceforge.greenvine.model.Column column = new net.sourceforge.greenvine.model.Column();
        column.setName(getColumnNameFromPropertyName(simpleProperty.getName()));
        column.setType(getColumnTypeFromProperyType(simpleProperty.getType()));
        column.setNotNull(simpleProperty.getNotNull());
        return column;
    }

    private static String getColumnNameFromPropertyName(String propName) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(propName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            int start = matcher.start();
            matcher.appendReplacement(sb, "_" + propName.charAt(start));
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    private static ColumnDatatypeType getColumnTypeFromProperyType(PropertyDatatypeType type) {
        ColumnDatatypeType dbType = null;
        switch(type) {
            case BIG_DECIMAL:
                dbType = ColumnDatatypeType.DECIMAL;
                break;
            case BINARY:
                dbType = ColumnDatatypeType.BINARY;
                break;
            case BLOB:
                dbType = ColumnDatatypeType.BLOB;
                break;
            case BOOLEAN:
                dbType = ColumnDatatypeType.BOOLEAN;
                break;
            case BYTE:
                dbType = ColumnDatatypeType.TINYINT;
                break;
            case CALENDAR_DATE:
                dbType = ColumnDatatypeType.DATE;
                break;
            case CALENDAR:
                dbType = ColumnDatatypeType.TIMESTAMP;
                break;
            case CHARACTER:
                dbType = ColumnDatatypeType.CHARACTER;
                break;
            case CLASS:
                dbType = ColumnDatatypeType.VARCHAR;
                break;
            case CLOB:
                dbType = ColumnDatatypeType.CLOB;
                break;
            case CURRENCY:
                dbType = ColumnDatatypeType.VARCHAR;
                break;
            case DATE:
                dbType = ColumnDatatypeType.DATE;
                break;
            case DOUBLE:
                dbType = ColumnDatatypeType.DOUBLE;
                break;
            case FLOAT:
                dbType = ColumnDatatypeType.FLOAT;
                break;
            case INTEGER:
                dbType = ColumnDatatypeType.INTEGER;
                break;
            case LOCALE:
                dbType = ColumnDatatypeType.VARCHAR;
                break;
            case LONG:
                dbType = ColumnDatatypeType.BIGINT;
                break;
            case SERIALIZABLE:
                dbType = ColumnDatatypeType.BLOB;
                break;
            case SHORT:
                dbType = ColumnDatatypeType.SMALLINT;
                break;
            case STRING:
                dbType = ColumnDatatypeType.VARCHAR;
                break;
            case TEXT:
                dbType = ColumnDatatypeType.LONGVARCHAR;
                break;
            case TIME:
                dbType = ColumnDatatypeType.TIME;
                break;
            case TIMESTAMP:
                dbType = ColumnDatatypeType.TIMESTAMP;
                break;
            case TIMEZONE:
                dbType = ColumnDatatypeType.VARCHAR;
                break;
        }
        return dbType;
    }

    private void inferForeignKeyColumns(BaseAssociationType association, Identity identity, ParentTableRef parentTableRef) {
        inferForeignKeyColumns(association, identity, parentTableRef, "FK_", "_ID");
    }

    private void inferForeignKeyColumnsConstrained(BaseAssociationType association, Identity identity, ParentTableRef parentTableRef) {
        inferForeignKeyColumns(association, identity, parentTableRef, "", "_ID");
    }

    private void inferForeignKeyColumns(BaseAssociationType association, Identity identity, ParentTableRef parentTableRef, String prefix, String suffix) {
        SimpleIdentity simpleIdentity = identity.getSimpleIdentity();
        if (simpleIdentity != null) {
            net.sourceforge.greenvine.model.Column column = getColumnFromSimpleIdentity(simpleIdentity);
            String name = prefix + getColumnNameFromPropertyName(association.getName()) + suffix;
            column.setName(name);
            parentTableRef.addColumn(column);
        } else {
            for (IdentityProperty identityProperty : identity.getComplexIdentity().getIdentityProperty()) {
                net.sourceforge.greenvine.model.Column column = getColumnFromIdentityProperty(identityProperty);
                String name = prefix + getColumnNameFromPropertyName(association.getName()) + "_" + column.getName();
                column.setName(name);
                parentTableRef.addColumn(column);
            }
        }
    }

    private void inferSimplePropertyColumns(Entity entity) {
        for (SimpleProperty simpleProperty : entity.getSimpleProperty()) {
            if (simpleProperty.getColumn() == null) {
                net.sourceforge.greenvine.model.Column column = getColumnFromProperty(simpleProperty);
                simpleProperty.setColumn(column);
            }
        }
    }

    private static String getTableNameFromEntityName(String entityName) {
        return "DBO.TBL_" + entityName.toUpperCase();
    }

    private void extractJoinTablesFromEntity(Entity entity, Database database) {
        for (ManyToMany manyToMany : entity.getManyToMany()) {
            Table table = extractJoinTable(manyToMany.getJoinTableRef(), database);
            addTableToDatabase(table, database);
        }
        for (OneToMany oneToMany : entity.getOneToMany()) {
            JoinTableRef joinTableRef = oneToMany.getJoinTableRef();
            if (joinTableRef != null) {
                Table table = extractJoinTable(joinTableRef, database);
                KeyType relatedKey = joinTableRef.getRelatedKey();
                UniqueKey unique = createUniqueKey(table, relatedKey);
                table.addUniqueKey(unique);
                addTableToDatabase(table, database);
            }
        }
        for (ManyToOne manyToOne : entity.getManyToOne()) {
            JoinTableRef joinTableRef = manyToOne.getJoinTableRef();
            if (joinTableRef != null) {
                Table table = extractJoinTable(manyToOne.getJoinTableRef(), database);
                KeyType thisKey = joinTableRef.getThisKey();
                UniqueKey unique = createUniqueKey(table, thisKey);
                table.addUniqueKey(unique);
                addTableToDatabase(table, database);
            }
        }
        for (OneToOne oneToOne : entity.getOneToOne()) {
            JoinTableRef joinTableRef = oneToOne.getJoinTableRef();
            if (joinTableRef != null) {
                Table table = extractJoinTable(joinTableRef, database);
                KeyType relatedKey = joinTableRef.getThisKey();
                UniqueKey unique1 = createUniqueKey(table, relatedKey);
                table.addUniqueKey(unique1);
                KeyType thisKey = joinTableRef.getRelatedKey();
                UniqueKey unique2 = createUniqueKey(table, thisKey);
                table.addUniqueKey(unique2);
                addTableToDatabase(table, database);
            }
        }
    }

    private UniqueKey createUniqueKey(Table table, KeyType key) {
        UniqueKey unique = new UniqueKey();
        String uniqueKeyName = "UNIQUE_" + getUnqualifiedTablName(table.getName());
        for (int i = 0; i < key.getColumnConstraintCount(); i++) {
            ColumnRef colRef = new ColumnRef();
            colRef.setName(key.getColumnConstraint(i).getReferencingColumn());
            uniqueKeyName = uniqueKeyName + "_" + colRef.getName().toUpperCase();
            unique.addColumnRef(colRef);
        }
        unique.setName(uniqueKeyName);
        return unique;
    }

    private String getUnqualifiedTablName(String fqName) {
        if (fqName.lastIndexOf(".") > 0) {
            return fqName.substring(fqName.lastIndexOf(".") + 1);
        }
        return fqName;
    }

    private void addTableToDatabase(Table table, Database database) {
        Table tab = getTableByName(table.getName(), database);
        if (tab == null) {
            database.addTable(table);
        }
    }

    private Table getTableByName(String name, Database database) {
        for (Table tab : database.getTable()) {
            if (tab.getName().equals(name)) {
                return tab;
            }
        }
        return null;
    }

    private Table extractJoinTable(JoinTableRef joinTableRef, Database database) {
        Table joinTable = new Table();
        joinTable.setName(joinTableRef.getThisKey().getReferencingTable());
        joinTable.setPrimaryKey(new PrimaryKey());
        KeyType thisKey = joinTableRef.getThisKey();
        KeyType thatKey = joinTableRef.getRelatedKey();
        Table thisKeyReferenced = getTableByName(thisKey.getReferencedTable(), database);
        Table thatKeyReferenced = getTableByName(thatKey.getReferencedTable(), database);
        for (int i = 0; i < thisKey.getColumnConstraintCount(); i++) {
            net.sourceforge.greenvine.model.ColumnConstraint constraint = thisKey.getColumnConstraint(i);
            extractJoinTableColumn(joinTable, thisKeyReferenced, constraint);
        }
        for (int i = 0; i < thatKey.getColumnConstraintCount(); i++) {
            net.sourceforge.greenvine.model.ColumnConstraint constraint = thatKey.getColumnConstraint(i);
            extractJoinTableColumn(joinTable, thatKeyReferenced, constraint);
        }
        extractForeignKeysFromJoinTableRef(joinTableRef, joinTable);
        return joinTable;
    }

    private void extractJoinTableColumn(Table joinTable, Table referencedTable, net.sourceforge.greenvine.model.ColumnConstraint constraint) {
        Column fk = new Column();
        fk.setName(constraint.getReferencingColumn());
        Column referencedColumn = getReferencedColumn(constraint, referencedTable);
        fk.setType(referencedColumn.getType());
        fk.setScale(referencedColumn.getScale());
        fk.setPrecision(referencedColumn.getPrecision());
        fk.setNotNull(referencedColumn.getNotNull());
        joinTable.addColumn(fk);
        ColumnRef colRef = new ColumnRef();
        colRef.setName(constraint.getReferencingColumn());
        joinTable.getPrimaryKey().addColumnRef(colRef);
    }

    private Column getReferencedColumn(net.sourceforge.greenvine.model.ColumnConstraint constraint, Table referencedTable) {
        for (Column col : referencedTable.getColumn()) {
            if (col.getName().equals(constraint.getReferencedColumn())) {
                return col;
            }
        }
        return null;
    }

    private void extractTableFromEntity(Entity entity, Database database) {
        Table table = new Table();
        table.setName(entity.getTableName());
        addIdentityColumns(entity.getIdentity(), table);
        addManyToOneColumns(entity.getManyToOne(), table);
        addOneToOneColumns(entity.getOneToOne(), table);
        addSimplePropertyColumns(entity.getSimpleProperty(), table);
        addPrimaryKey(entity, table);
        addUniqueKeys(entity, table);
        addForeignKeys(entity, table);
        database.addTable(table);
    }

    private void addForeignKeys(Entity entity, Table table) {
        for (ManyToOne manyToOne : entity.getManyToOne()) {
            extractForeignKeyFromRelatedTable(manyToOne.getParentTableRef(), table);
        }
        for (OneToOne oneToOne : entity.getOneToOne()) {
            if (oneToOne.getNotNull()) {
                extractForeignKeyFromRelatedTable(oneToOne.getParentTableRef(), table);
            }
        }
    }

    private void extractForeignKeyFromRelatedTable(ParentTableRef parentTableRef, Table table) {
        if (parentTableRef != null) {
            Key key = parentTableRef.getKey();
            getForeignKeyFromKeyType(table, key);
        }
    }

    private void extractForeignKeysFromJoinTableRef(JoinTableRef joinTableRef, Table table) {
        if (joinTableRef != null) {
            KeyType thisKey = joinTableRef.getThisKey();
            getForeignKeyFromKeyType(table, thisKey);
            KeyType relatedKey = joinTableRef.getRelatedKey();
            getForeignKeyFromKeyType(table, relatedKey);
        }
    }

    private void getForeignKeyFromKeyType(Table table, KeyType key) {
        String referencingName = getUnqualifiedTablName(key.getReferencingTable());
        String referencedName = getUnqualifiedTablName(key.getReferencedTable());
        ForeignKey fk = new ForeignKey();
        fk.setReferencedTable(key.getReferencedTable());
        fk.setReferencingTable(key.getReferencingTable());
        for (int i = 0; i < key.getColumnConstraintCount(); i++) {
            ColumnConstraint cc = new ColumnConstraint();
            cc.setReferencedColumn(key.getColumnConstraint(i).getReferencedColumn());
            cc.setReferencingColumn(key.getColumnConstraint(i).getReferencingColumn());
            referencedName = referencedName + "_" + key.getColumnConstraint(i).getReferencedColumn();
            referencingName = referencingName + "_" + key.getColumnConstraint(i).getReferencingColumn();
            fk.addColumnConstraint(cc);
        }
        fk.setName("FK_" + referencingName + "_" + referencedName);
        table.addForeignKey(fk);
    }

    private void addUniqueKeys(Entity entity, Table table) {
        if (entity.getNaturalId() != null) {
            UniqueKey uniqueKey = createUniqueKey(entity, table, entity.getNaturalId());
            table.addUniqueKey(uniqueKey);
        }
        for (UniqueConstraint entityUnique : entity.getUniqueConstraint()) {
            UniqueKey uniqueKey = createUniqueKey(entity, table, entityUnique);
            table.addUniqueKey(uniqueKey);
        }
    }

    private UniqueKey createUniqueKey(Entity entity, Table table, UniqueConstraintType entityUnique) {
        UniqueKey tableUnique = new UniqueKey();
        StringBuilder uniqueName = new StringBuilder();
        uniqueName.append("UNIQUE_");
        uniqueName.append(getUnqualifiedTablName(table.getName()));
        for (SimplePropertyRef simplePropertyRef : entityUnique.getSimplePropertyRef()) {
            String fieldName = simplePropertyRef.getName();
            SimpleProperty simpleProperty = modelHelper.getSimplePropertyFromEntity(entity, fieldName);
            ColumnRef tableCol = new ColumnRef();
            if (simpleProperty.getColumn() != null) {
                tableCol.setName(simpleProperty.getColumn().getName());
            } else {
                tableCol.setName(getColumnNameFromPropertyName(fieldName));
            }
            tableUnique.addColumnRef(tableCol);
            uniqueName.append("_");
            uniqueName.append(tableCol.getName());
        }
        for (ManyToOneRef manyToOneRef : entityUnique.getManyToOneRef()) {
            String fieldName = manyToOneRef.getName();
            ManyToOne manyToOne = modelHelper.getManyToOneFromEntity(entity, fieldName);
            if (manyToOne.getParentTableRef() != null) {
                for (net.sourceforge.greenvine.model.Column fkCol : manyToOne.getParentTableRef().getColumn()) {
                    ColumnRef tableCol = new ColumnRef();
                    tableCol.setName(fkCol.getName());
                    tableUnique.addColumnRef(tableCol);
                    uniqueName.append("_");
                    uniqueName.append(tableCol.getName());
                }
            }
        }
        for (OneToOneRef manyToOneRef : entityUnique.getOneToOneRef()) {
            String fieldName = manyToOneRef.getName();
            OneToOne oneToOne = modelHelper.getOneToOneFromEntity(entity, fieldName);
            if (oneToOne.getParentTableRef() != null) {
                for (net.sourceforge.greenvine.model.Column fkCol : oneToOne.getParentTableRef().getColumn()) {
                    ColumnRef tableCol = new ColumnRef();
                    tableCol.setName(fkCol.getName());
                    tableUnique.addColumnRef(tableCol);
                    uniqueName.append("_");
                    uniqueName.append(tableCol.getName());
                }
            }
        }
        tableUnique.setName(uniqueName.toString());
        return tableUnique;
    }

    private void addPrimaryKey(Entity entity, Table table) {
        Identity identity = entity.getIdentity();
        PrimaryKey primaryKey = new PrimaryKey();
        table.setPrimaryKey(primaryKey);
        if (identity.getSimpleIdentity() != null) {
            ColumnRef columnRef = new ColumnRef();
            primaryKey.addColumnRef(columnRef);
            if (identity.getSimpleIdentity().getColumn() == null) {
                columnRef.setName(getColumnNameFromPropertyName(identity.getSimpleIdentity().getName()));
            } else {
                columnRef.setName(identity.getSimpleIdentity().getColumn().getName());
            }
        } else if (identity.getConstrainedIdentity() != null) {
            OneToOne oneToOne = getOneToOneByName(entity, identity.getConstrainedIdentity().getOneToOneRef().getName());
            for (net.sourceforge.greenvine.model.Column col : oneToOne.getParentTableRef().getColumn()) {
                ColumnRef columnRef = new ColumnRef();
                columnRef.setName(col.getName());
                primaryKey.addColumnRef(columnRef);
            }
        } else {
            for (IdentityProperty identityProperty : identity.getComplexIdentity().getIdentityProperty()) {
                ColumnRef columnRef = new ColumnRef();
                primaryKey.addColumnRef(columnRef);
                if (identityProperty.getColumn() == null) {
                    columnRef.setName(getColumnNameFromPropertyName(identityProperty.getName()));
                } else {
                    columnRef.setName(identityProperty.getColumn().getName());
                }
            }
        }
    }

    private OneToOne getOneToOneByName(Entity entity, String name) {
        for (OneToOne oneToOne : entity.getOneToOne()) {
            if (oneToOne.getName().equals(name)) {
                return oneToOne;
            }
        }
        return null;
    }

    private void addSimplePropertyColumns(SimpleProperty[] simpleProperties, Table table) {
        for (SimpleProperty prop : simpleProperties) {
            net.sourceforge.greenvine.model.Column propCol = prop.getColumn();
            net.sourceforge.greenvine.database.Column dbCol = new net.sourceforge.greenvine.database.Column();
            dbCol.setName(propCol.getName());
            if (propCol.getType() != null) {
                dbCol.setType(propCol.getType());
            } else {
                dbCol.setType(getColumnTypeFromProperyType(prop.getType()));
            }
            dbCol.setScale(propCol.getScale());
            dbCol.setPrecision(propCol.getPrecision());
            dbCol.setNotNull(propCol.getNotNull());
            if (prop.getNotNull() != dbCol.getNotNull()) dbCol.setNotNull(prop.getNotNull());
            table.addColumn(dbCol);
        }
    }

    private void addOneToOneColumns(OneToOne[] oneToOnes, Table table) {
        for (OneToOne oneToOne : oneToOnes) {
            if (!oneToOne.getConstrained()) {
                if (oneToOne.getParentTableRef() != null) {
                    addForeignKeyColumns(oneToOne.getParentTableRef().getColumn(), oneToOne.getNotNull(), table);
                }
            }
        }
    }

    private void addManyToOneColumns(ManyToOne[] manyToOnes, Table table) {
        for (ManyToOne manyToOne : manyToOnes) {
            if (manyToOne.getParentTableRef() != null) {
                addForeignKeyColumns(manyToOne.getParentTableRef().getColumn(), manyToOne.getNotNull(), table);
            }
        }
    }

    private void addForeignKeyColumns(net.sourceforge.greenvine.model.Column[] fkColumns, boolean notNull, Table table) {
        for (net.sourceforge.greenvine.model.Column fkColumn : fkColumns) {
            Column column = getColumnFromFkColumn(fkColumn, notNull);
            if (!doesColumnExist(table, column)) {
                table.addColumn(column);
            }
        }
    }

    private boolean doesColumnExist(Table table, Column column) {
        for (Column col : table.getColumn()) {
            if (col.getName().equals(column.getName())) {
                return true;
            }
        }
        return false;
    }

    private Column getColumnFromFkColumn(net.sourceforge.greenvine.database.ColumnType fkColumn, boolean notNull) {
        Column col = new Column();
        col.setName(fkColumn.getName());
        col.setType(fkColumn.getType());
        col.setScale(fkColumn.getScale());
        col.setPrecision(fkColumn.getPrecision());
        col.setNotNull(notNull);
        return col;
    }

    private void addIdentityColumns(Identity identity, Table table) {
        if (identity != null) {
            if (identity.getSimpleIdentity() != null) {
                addSimpleIdentityColumns(identity.getSimpleIdentity(), table);
            } else if (identity.getConstrainedIdentity() != null) {
                addConstrainedIdentityColumns(identity.getConstrainedIdentity(), table);
            } else {
                addComplexIdentityColumns(identity.getComplexIdentity(), table);
            }
        }
    }

    private void addConstrainedIdentityColumns(ConstrainedIdentity constrainedIdentity, Table table) {
    }

    private void addSimpleIdentityColumns(SimpleIdentity simpleIdentity, Table table) {
        Column column = getColumnFromFkColumn(simpleIdentity.getColumn(), true);
        table.addColumn(column);
    }

    private void addComplexIdentityColumns(ComplexIdentity complexIdentity, Table table) {
        for (IdentityProperty identityProperty : complexIdentity.getIdentityProperty()) {
            Column column = getColumnFromFkColumn(identityProperty.getColumn(), true);
            table.addColumn(column);
        }
    }

    public Entity getEntityByName(String name) {
        for (Entity entity : model.getData().getDatasource().getEntities().getEntity()) {
            String entityName = entity.getName();
            if (entityName.equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public void marshalModel(File file) throws IOException, MarshalException, ValidationException {
        Writer out = new FileWriter(file);
        model.marshal(out);
        out.flush();
        out.close();
    }
}
