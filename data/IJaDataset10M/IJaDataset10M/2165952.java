package org.databene.jdbacl.model;

import org.databene.commons.Named;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.collection.OrderedNameMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JDBC catalog.<br/><br/>
 * Created: 06.01.2007 08:57:57
 * @author Volker Bergmann
 */
public class DBCatalog extends AbstractCompositeDBObject<DBSchema> implements Named, Serializable {

    private static final long serialVersionUID = 3956827426638393655L;

    OrderedNameMap<DBSchema> schemas;

    public DBCatalog() {
        this(null);
    }

    public DBCatalog(String name) {
        this(name, null);
    }

    public DBCatalog(String name, Database owner) {
        super(name, "catalog", owner);
        if (owner != null) owner.addCatalog(this);
        this.schemas = OrderedNameMap.createCaseIgnorantMap();
    }

    public Database getDatabase() {
        return (Database) getOwner();
    }

    public void setDatabase(Database database) {
        this.owner = database;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public List<DBSchema> getComponents() {
        return schemas.values();
    }

    public List<DBSchema> getSchemas() {
        return getComponents();
    }

    public DBSchema getSchema(String schemaName) {
        return schemas.get(schemaName);
    }

    public void addSchema(DBSchema schema) {
        schemas.put(schema.getName(), schema);
        schema.setOwner(this);
    }

    public void removeSchema(DBSchema schema) {
        schemas.remove(schema.getName());
    }

    public List<DBTable> getTables() {
        List<DBTable> tables = new ArrayList<DBTable>();
        for (DBSchema schema : getSchemas()) for (DBTable table : schema.getTables()) tables.add(table);
        return tables;
    }

    public DBTable getTable(String name) {
        return getTable(name, true);
    }

    public DBTable getTable(String name, boolean required) {
        for (DBSchema schema : getSchemas()) for (DBTable table : schema.getTables()) if (table.getName().equals(name)) return table;
        if (required) throw new ObjectNotFoundException("Table '" + name + "'"); else return null;
    }

    public void removeTable(String tableName) {
        DBTable table = getTable(tableName);
        table.getSchema().removeTable(table);
    }

    public List<DBSequence> getSequences() {
        List<DBSequence> sequences = new ArrayList<DBSequence>();
        for (DBSchema schema : getSchemas()) for (DBSequence table : schema.getSequences(true)) sequences.add(table);
        return sequences;
    }
}
