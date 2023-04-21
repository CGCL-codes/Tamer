package uk.ac.lkl.common.util.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import uk.ac.lkl.common.util.database.constraint.ForeignKey;
import uk.ac.lkl.common.util.database.constraint.PrimaryKey;
import uk.ac.lkl.common.util.database.constraint.TableConstraint;
import uk.ac.lkl.common.util.database.path.DatabasePathClassDatum;
import uk.ac.lkl.common.util.database.path.ForeignKeyDefinitionComparator;
import uk.ac.lkl.common.util.database.path.NameBasedDefinitionComparator;
import uk.ac.lkl.common.util.database.path.PrimaryKeyDefinitionComparator;

/**
 * A sequence of (database, table, column). Can be (database) or just (database,
 * table).
 * 
 * @author $Author: darren.pearce $
 * @version $Revision: 3891 $
 * @version $Date: 2010-01-27 07:00:24 -0500 (Wed, 27 Jan 2010) $
 * 
 */
public class DatabasePath<E extends Definition> implements Comparable<DatabasePath<?>> {

    private static ArrayList<DatabasePathClassDatum<?>> DATUM_LIST;

    private static HashMap<Class<? extends Definition>, DatabasePathClassDatum<?>> DATUM_MAP;

    static {
        initialiseDatumList();
        initialiseDatumMap();
    }

    private static void initialiseDatumList() {
        DATUM_LIST = new ArrayList<DatabasePathClassDatum<?>>();
        DATUM_LIST.add(new DatabasePathClassDatum<DatabaseSetDefinition>(DatabaseSetDefinition.class, new NameBasedDefinitionComparator<DatabaseSetDefinition>(), 0));
        DATUM_LIST.add(new DatabasePathClassDatum<DatabaseDefinition>(DatabaseDefinition.class, new NameBasedDefinitionComparator<DatabaseDefinition>(), 1));
        DATUM_LIST.add(new DatabasePathClassDatum<TableDefinition>(TableDefinition.class, new NameBasedDefinitionComparator<TableDefinition>(), 2));
        DATUM_LIST.add(new DatabasePathClassDatum<ColumnDefinition>(ColumnDefinition.class, new NameBasedDefinitionComparator<ColumnDefinition>(), 3));
        DATUM_LIST.add(new DatabasePathClassDatum<PrimaryKey>(PrimaryKey.class, new PrimaryKeyDefinitionComparator(), 3));
        DATUM_LIST.add(new DatabasePathClassDatum<ForeignKey>(ForeignKey.class, new ForeignKeyDefinitionComparator(), 3));
    }

    private static void initialiseDatumMap() {
        DATUM_MAP = new HashMap<Class<? extends Definition>, DatabasePathClassDatum<?>>();
        for (DatabasePathClassDatum<?> datum : DATUM_LIST) {
            Class<? extends Definition> entityClass = datum.getDefinitionClass();
            DATUM_MAP.put(entityClass, datum);
        }
    }

    private TreeMap<Integer, Definition> identifierMap;

    private int firstHierarchyIndex;

    public DatabasePath(E headEntity, Definition... tailEntities) {
        List<Definition> entities = new ArrayList<Definition>();
        entities.add(headEntity);
        entities.addAll(Arrays.asList(tailEntities));
        initialiseEntities(entities);
    }

    public DatabasePath(E headEntity, DatabasePath<?> tailPath) {
        List<Definition> entities = new ArrayList<Definition>();
        entities.add(headEntity);
        entities.addAll(tailPath.getIdentifiers());
        initialiseEntities(entities);
    }

    private void initialiseEntities(List<Definition> entities) {
        Integer currentHierarchyIndex = null;
        identifierMap = new TreeMap<Integer, Definition>();
        for (Definition entity : entities) {
            if (entity == null) return;
            DatabasePathClassDatum<?> datum = getDatum(entity);
            int hierarchyIndex = datum.getHierarchyIndex();
            if (currentHierarchyIndex == null) {
                firstHierarchyIndex = hierarchyIndex;
                currentHierarchyIndex = hierarchyIndex;
            } else {
                currentHierarchyIndex++;
                if (hierarchyIndex != currentHierarchyIndex) throw new IllegalArgumentException("Path not valid");
            }
            identifierMap.put(currentHierarchyIndex, entity);
        }
    }

    private String getIdentifierHandle(DatabasePathClassDatum<?> datum) {
        int hierarchyIndex = datum.getHierarchyIndex();
        return getIdentifierHandle(hierarchyIndex);
    }

    private Definition getIdentifier(int hierarchyIndex) {
        return identifierMap.get(hierarchyIndex);
    }

    public Collection<Definition> getIdentifiers() {
        return Collections.unmodifiableCollection(identifierMap.values());
    }

    private String getIdentifierHandle(int key) {
        Definition identifier = getIdentifier(key);
        if (identifier == null) return null;
        return identifier.getHandle();
    }

    private String getIdentifierHandle(Class<? extends Definition> entityClass) {
        DatabasePathClassDatum<?> datum = DATUM_MAP.get(entityClass);
        return getIdentifierHandle(datum);
    }

    public String getDatabaseHandle() {
        return getIdentifierHandle(DatabaseDefinition.class);
    }

    public String getTableHandle() {
        return getIdentifierHandle(TableDefinition.class);
    }

    public String getColumnHandle() {
        return getIdentifierHandle(ColumnDefinition.class);
    }

    public String getConstraintHandle() {
        return getIdentifierHandle(TableConstraint.class);
    }

    public int getLength() {
        return identifierMap.size();
    }

    private static DatabasePathClassDatum<?> getDatum(Definition entity) {
        for (Class<? extends Definition> entityClass : DATUM_MAP.keySet()) {
            boolean isInstance = entityClass.isInstance(entity);
            if (isInstance) {
                DatabasePathClassDatum<?> datum = DATUM_MAP.get(entityClass);
                return datum;
            }
        }
        Class<? extends Definition> entityClass = entity.getClass();
        throw new IllegalArgumentException("Can't map entity class: " + entityClass);
    }

    private int getChildIndex(Definition entity) {
        return getDatum(entity).getChildIndex();
    }

    public int getFirstHierarchyIndex() {
        return firstHierarchyIndex;
    }

    public Definition getFirstIdentifier() {
        return getIdentifier(getFirstHierarchyIndex());
    }

    public int compareTo(DatabasePath<?> other) {
        Integer thisLength = this.getLength();
        Integer otherLength = other.getLength();
        Integer thisFirstHierarchyIndex = this.getFirstHierarchyIndex();
        Integer otherFirstHierarchyIndex = other.getFirstHierarchyIndex();
        int firstKeyComparison = thisFirstHierarchyIndex.compareTo(otherFirstHierarchyIndex);
        if (firstKeyComparison != 0) return firstKeyComparison;
        int commonElementCount = Math.min(thisLength, otherLength);
        for (int i = thisFirstHierarchyIndex; i < thisFirstHierarchyIndex + commonElementCount; i++) {
            Definition thisIdentifier = this.getIdentifier(i);
            Definition otherIdentifier = other.getIdentifier(i);
            Integer thisKey = getChildIndex(thisIdentifier);
            Integer otherKey = getChildIndex(otherIdentifier);
            int keyComparison = thisKey.compareTo(otherKey);
            if (keyComparison != 0) return keyComparison;
            DatabasePathClassDatum<?> datum = getDatum(thisIdentifier);
            int identifierComparison = datum.compare(thisIdentifier, otherIdentifier);
            if (identifierComparison != 0) return identifierComparison;
        }
        return thisLength.compareTo(otherLength);
    }

    public String toString() {
        String result = "";
        for (Definition entity : identifierMap.values()) {
            result += ".";
            result += entity.getHandle();
        }
        return result;
    }
}
