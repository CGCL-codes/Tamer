package es.atareao.queensboro.db;

/**
 *
 * @author Protactino
 */
public class Order {

    private String _column;

    private boolean _ascendent;

    /**
     * Crea una nueva instancia de OrderExpression
     */
    public Order(String column) {
        this.setColumn(column);
        this.setAscendent(true);
    }

    public Order(String column, boolean ascendent) {
        this.setColumn(column);
        this.setAscendent(ascendent);
    }

    public Order(String column, boolean ascendent, boolean nullsLast) {
        this.setColumn(column);
        this.setAscendent(ascendent);
    }

    public boolean isAscendent() {
        return _ascendent;
    }

    public void setAscendent(boolean ascendent) {
        this._ascendent = ascendent;
    }

    public String getColumn() {
        return _column;
    }

    public void setColumn(String column) {
        this._column = Nomenclator.stdName(column);
    }

    public String toSql() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumn());
        if (this.isAscendent()) {
            sb.append(" ASC");
        } else {
            sb.append(" DESC");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order) {
            Order oe = (Order) obj;
            if (this.toSql().equals(oe.toSql())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this._column != null ? this._column.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.getColumn();
    }
}
