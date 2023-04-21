package davidlauzon.activerecord.nodes;

import davidlauzon.activerecord.nodes.field.Field;
import davidlauzon.activerecord.visitor.SqlSerializer;

public class InsertStatement extends SqlStatement {

    /*********************************************************************************************
     * VARIABLES
     *********************************************************************************************/
    private Table _into;

    private Field[] _columns;

    private Object[] _values;

    /*********************************************************************************************
     * SIMPLE GETTERS & SETTERS
     *********************************************************************************************/
    public Table into() {
        return _into;
    }

    public Field[] columns() {
        return _columns;
    }

    public Object[] values() {
        return _values;
    }

    /*********************************************************************************************
     * PUBLIC METHODS
     *********************************************************************************************/
    public InsertStatement() {
        super();
    }

    public void setInto(Table into) {
        _into = into;
    }

    public void setColumns(Field[] columns) {
        _columns = columns;
    }

    public void setValues(Object[] values) {
        _values = values;
    }

    @Override
    public String accept(SqlSerializer visitor) {
        return visitor.visit(this);
    }
}
