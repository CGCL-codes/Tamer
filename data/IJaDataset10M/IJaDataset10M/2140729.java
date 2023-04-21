package hendrey.shades.internal;

import hendrey.orm.ORMDictionary;
import hendrey.orm.ORMapping;
import hendrey.orm.Query;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author ghendrey
 */
public class DictionaryImpl implements ORMDictionary {

    private Map<String, ORMapping> orms = new LinkedHashMap<String, ORMapping>();

    private Map<String, Entry> relationships = new LinkedHashMap<String, Entry>();

    private Map<String, String> removers = new LinkedHashMap<String, String>();

    private Map<String, String> setters = new LinkedHashMap<String, String>();

    private Map<String, Query> queries = new LinkedHashMap<String, Query>();

    private String name;

    /** Creates a new instance of Dictionary */
    public DictionaryImpl(String name) {
        this.name = name;
    }

    public void defineRelationship(String relationshipName, String joinExpression, String... tableAliases) {
        relationships.put(relationshipName, new Entry(joinExpression, tableAliases));
    }

    public void defineRelationshipRemover(String relationshipName, String removalExpression) {
        removers.put(relationshipName, removalExpression);
    }

    public boolean hasDefinedRelationship(String name) {
        return relationships.containsKey(name);
    }

    public Relationship getRelationship(String name, ShadesRecord e1, ShadesRecord e2) {
        if (!relationships.containsKey(name)) {
            throw new RuntimeException("relationship named \"" + name + "\" not defined in dictionary named " + this.name);
        }
        return new Relationship(name, e1, e2, relationships.get(name));
    }

    public Relationship getRelationshipSetter(String name, ShadesRecord e1, ShadesRecord e2) {
        if (!setters.containsKey(name)) {
            throw new RuntimeException("relationship setter named \"" + name + "\" not defined in dictionary named " + this.name);
        }
        return new Relationship(name, e1, e2, setters.get(name));
    }

    public Relationship getRelationshipRemover(String name, ShadesRecord e1, ShadesRecord e2) {
        if (!removers.containsKey(name)) {
            throw new RuntimeException("relationship remover  named \"" + name + "\" not defined in dictionary named " + this.name);
        }
        return new Relationship(name, e1, e2, removers.get(name));
    }

    public static class Entry implements java.io.Serializable {

        public String joinExpression;

        public String[] tableAliases;

        public Entry(String joinExpression, String... tableAliases) {
            this.joinExpression = joinExpression;
            this.tableAliases = tableAliases;
        }
    }

    public String getName() {
        return name;
    }

    public void defineORMapping(String ormName, ORMapping orm) {
        orms.put(ormName, orm);
    }

    public ORMapping getORM(String ormName) {
        return orms.get(ormName);
    }

    public String toString() {
        return "ORMDictionary: " + name + "\n" + orms + "\n" + this.relationships + "\n" + this.removers;
    }

    public void defineRelationshipSetter(String relationshipName, String expression) {
        setters.put(relationshipName, expression);
    }

    public String[] getORMNames() {
        return orms.keySet().toArray(new String[] {});
    }

    public void defineQuery(String queryName, Query query) {
        queries.put(queryName, query);
    }

    public String[] getQueryNames() {
        return queries.keySet().toArray(new String[] {});
    }

    public String[] getRelationshipNames() {
        return this.relationships.keySet().toArray(new String[] {});
    }

    public Query getQuery(String queryName) {
        if (!queries.containsKey(queryName)) {
            throw new RuntimeException("Query  named \"" + queryName + "\" not defined in dictionary named " + this.name);
        }
        return queries.get(queryName);
    }
}
