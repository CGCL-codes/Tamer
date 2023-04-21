package blueprint4j.db;

public interface FieldForeignKeyInterface {

    public String getName();

    public Entity getEntity();

    public Entity getNewLocal(DBConnection dbcon) throws DataException;

    public Field getField();

    public void set(String value);

    public void setParentEntity(Entity entity);
}
