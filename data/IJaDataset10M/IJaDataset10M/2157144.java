package velosurf.model;

import java.util.List;
import velosurf.util.Logger;
import velosurf.util.StringLists;

/**
 * An imported key (aka foreign key) attribute.
 *
 */
public class ImportedKey extends Attribute {

    /**
     * Foreign key columns.
     */
    private List<String> fkCols = null;

    /**
     * Imported key constructor.
     *
     * @param name name of this exported key
     * @param entity parent entity
     * @param pkEntity primary key entity
     * @param fkCols foreign key columns
     */
    public ImportedKey(String name, Entity entity, String pkEntity, List<String> fkCols) {
        super(name, entity);
        setResultEntity(pkEntity);
        this.fkCols = fkCols;
        setResultType(Attribute.ROW);
    }

    /**
     * Query getter.
     * @return SQL query
     */
    protected synchronized String getQuery() {
        if (query == null) {
            Entity pkEntity = db.getEntity(resultEntity);
            for (String param : getFKCols()) {
                addParamName(param);
            }
            query = "SELECT * FROM " + pkEntity.getTableName() + " WHERE " + StringLists.join(pkEntity.getPKCols(), " = ? AND ") + " = ?";
        }
        return query;
    }

    /**
     * Foreign key columns getter.
     * @return foreign key columns list
     */
    public List<String> getFKCols() {
        if (fkCols == null) {
            fkCols = entity.getDB().getEntity(getResultEntity()).getPKCols();
        }
        return fkCols;
    }

    /**
     * Foreign key columns setter.
     *
     * @param fkCols foreign key columns list
     */
    public void setFKCols(List<String> fkCols) {
        this.fkCols = fkCols;
    }

    /**
     * Debug method.
     *
     * @return the definition string of this attribute
     */
    public String toString() {
        return "imported-key" + (fkCols == null ? "" : " on " + StringLists.join(fkCols, ","));
    }
}
