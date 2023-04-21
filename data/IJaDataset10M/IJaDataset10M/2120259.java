package pl.voidsystems.yajf.dao.table;

/**
 * @author  rlitman
 */
public interface IDBTableFieldColumn extends IDBTableColumn {

    /**
     * @uml.property  name="field"
     */
    String getField();

    /**
     * @uml.property  name="sortField"
     */
    String getSortField();

    boolean isSortable();
}
