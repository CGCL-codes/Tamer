package pl.voidsystems.yajf.dao.table;

import pl.voidsystems.yajf.components.IComponent;
import pl.voidsystems.yajf.components.Label;
import pl.voidsystems.yajf.dao.DBIntegerInput;

public class DBIntegerInputColumn extends DBInputColumn {

    /**
     * @param title
     *            Column title
     * @param field
     *            Entity field name
     * @param cls
     *            Entity class
     */
    public DBIntegerInputColumn(String title, String field, Class cls) {
        super(title, field, cls);
    }

    /**
     * @see pl.voidsystems.yajf.dao.table.IDBTableColumn#makeReadOnlyComponent(java.lang.Object)
     */
    @Override
    public IComponent makeReadOnlyComponent(Object entity) {
        Integer value = (Integer) this.accessor.get(entity);
        return new Label(value.toString());
    }

    /**
     * @see pl.voidsystems.yajf.dao.table.IDBTableColumn#makeWritableComponent(java.lang.Object)
     */
    @Override
    public IComponent makeWritableComponent(Object entity) {
        DBIntegerInput input = new DBIntegerInput(entity, this.field);
        return input;
    }
}
