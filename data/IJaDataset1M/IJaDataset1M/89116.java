package nickyb.sqleonardo.querybuilder.beans;

/**
 *
 * @author gtoffoli
 */
public class Entity {

    private String schema = "";

    private String entityName = "";

    /** Creates a new instance of Entity */
    public Entity(String schema, String entityName) {
        this.setSchema(schema);
        this.setEntityName(entityName);
    }

    public String toString() {
        return getEntityName();
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
