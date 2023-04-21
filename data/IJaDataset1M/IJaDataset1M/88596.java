package es.atareao.queensboro.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @param G 
 * @author Propietario
 */
public class WrapperView<G extends WrapperView> implements Comparable {

    private Conector _conector;

    private String _schemaName;

    private String _tableName;

    private Row _row;

    private String _id;

    private Vector<String> _selected;

    private String _text;

    protected Class<? extends G> _wrapperClass;

    public WrapperView() {
        this.setRow(new Row());
        this.setSelected(new Vector<String>());
    }

    /**
     * A new instance for wrapper
     * @param wrapperClass 
     * @param conector the connector for this database
     * @param schemaName the name of the schema
     * @param tableName the name of the table
     * @param id the selected register
     * @throws java.sql.SQLException raises an exception when an error ocurs
     */
    public WrapperView(Class<? extends G> wrapperClass, Conector conector, String schemaName, String tableName, String id) throws SQLException {
        this._wrapperClass = wrapperClass;
        this.setConector(conector);
        this.setSchemaName(schemaName);
        this.setTableName(tableName);
        this.setId(id);
        this.setRow(new Row());
        this.setSelected(new Vector<String>());
    }

    /**
     * A new instance for wrapper
     * @param wrapperClass 
     * @param conector the conector for this database
     * @param schemaName the schema name
     * @param tableName the table name
     * @throws java.sql.SQLException raises an exception when an error ocurs
     */
    public WrapperView(Class<? extends G> wrapperClass, Conector conector, String schemaName, String tableName) throws SQLException {
        this(wrapperClass, conector, schemaName, tableName, "");
    }

    @Override
    public String toString() {
        return this.getValue("nombre");
    }

    /**
     * If the register is selected
     * @param element the ID of teh element we know if is selected
     * @return true if the element is selected
     */
    public boolean isSelected(String element) {
        return this.getSelected().contains(element);
    }

    /**
     * Select a register
     * @param element ID for the register that we want to select
     * @return true if the register is selected
     * @throws java.sql.SQLException raises an exception if ocurs an error
     */
    public boolean select(String element) throws SQLException {
        if (!this.isSelected(element)) {
            if (this.hasAny(new Condition("id", element))) {
                this.getSelected().add(element);
                return true;
            }
        }
        return false;
    }

    /**
     * Select all the registers in the table
     * @return true if all the registers were selected
     * @throws java.sql.SQLException raises an exception if ocurs an errer
     */
    public boolean selectAll() throws SQLException {
        this.unselectAll();
        Iterator<G> iterator = this.find().iterator();
        while (iterator.hasNext()) {
            G seleccionado = iterator.next();
            this.getSelected().add(seleccionado.getValue("id"));
        }
        return true;
    }

    /**
     * Unslect all the registers selected in the table
     * @return true if all the registers were unselected
     */
    public boolean unselectAll() {
        this.getSelected().removeAllElements();
        return true;
    }

    /**
     * unselect a register if the register was selected
     * @param element ID for the register that we want to unselect
     * @return true if the register was unselect
     */
    public boolean unSelect(String element) {
        if (this.isSelected(element)) {
            this.getSelected().remove(element);
            return true;
        }
        return false;
    }

    /**
     * Count the number of the retisters in the table
     * @return number of the registers on the table
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public long count() throws SQLException {
        QueryCount queryCount = new QueryCount(this.getSchemaName(), this.getTableName());
        return this.count(queryCount);
    }

    /**
     * Count the number of register that  meet the condition suplied
     * @param condition condition that must meet 
     * @return the number of the registers that meet the suplied condition
     * @throws java.sql.SQLException raises an exception happens
     */
    public long count(Condition condition) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        QueryCount queryCount = new QueryCount(this.getSchemaName(), this.getTableName(), conditions);
        return this.count(queryCount);
    }

    /**
     * Count the number of register that meet the conditions suplied
     * @param conditions the conditions that must meet the retiststers
     * @return the number of registers that meet the suplied conditions
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public long count(Vector<Condition> conditions) throws SQLException {
        QueryCount queryCount = new QueryCount(this.getSchemaName(), this.getTableName(), conditions);
        return this.count(queryCount);
    }

    /**
     * Count the number of register that meet the conditions suplied
     * @param queryCount condition that must meet the registers
     * @return the number of registers that meet the suplied condition
     * @throws java.sql.SQLException raises an eception if an error happens
     */
    public long count(QueryCount queryCount) throws SQLException {
        return this.getConector().executeQueryCount(queryCount);
    }

    public G createWrapper() {
        G wrapper;
        try {
            wrapper = _wrapperClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        wrapper.setConector(this.getConector());
        wrapper.setSchemaName(this.getSchemaName());
        wrapper.setTableName(this.getTableName());
        return wrapper;
    }

    /**
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true for organize all the register ascending
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Condition condition, String orderColumnName, boolean ascendent) throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(condition);
        return find(conditions, orderColumnName, ascendent);
    }

    /** 
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Condition condition, String orderColumnName) throws SQLException {
        return find(condition, orderColumnName, true);
    }

    /**
     * Find all the registers than meet with the supplied condition
     * @param condition the condition that must meet the registers
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
      */
    public Vector<G> find(Condition condition) throws SQLException {
        return find(condition, "id", true);
    }

    /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true for organize all the register ascending
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
      */
    public Vector<G> find(Vector<Condition> conditions, String orderColumnName, boolean ascendent) throws SQLException {
        QuerySelect querySelect = new QuerySelect(this.getSchemaName(), this.getTableName());
        querySelect.setConditions(conditions);
        querySelect.addOrder(orderColumnName, ascendent);
        Vector<G> resultado = new Vector<G>();
        ResultSet rs = this.getConector().executeQuery(querySelect.toSql());
        while (rs.next()) {
            resultado.add(fromResultSetToWrapper(rs));
        }
        return resultado;
    }

    /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @param orderColumnName the name of the column to order the results
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Vector<Condition> conditions, String orderColumnName) throws SQLException {
        return this.find(conditions, orderColumnName, true);
    }

    /**
     * Find all the registers than meet with the supplied condition
     * @param conditions the conditions that must meet the registers
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions, "id", true);
    }

    /**
     * Find all the registers that has the current ID
     * @return the registers found
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> find() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        conditions.add(new Condition("id", this.getId()));
        return this.find(conditions, "id", true);
    }

    /**
     * Returns all the elements for the table
     * @return all the elements for the table
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public Vector<G> findAll() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.find(conditions, "id", true);
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true if the results are ordered ascendint
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition, String orderColumnName, boolean ascendent) throws SQLException {
        return this.find(condition, orderColumnName, ascendent).firstElement();
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition, String orderColumnName) throws SQLException {
        return this.find(condition, orderColumnName, true).firstElement();
    }

    /**
     * Find the register that meet with the current ID
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst() throws SQLException {
        return this.find(new Condition("id", this.getFirstId()), "id", true).firstElement();
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param condition the condition that must meet the element found
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Condition condition) throws SQLException {
        return this.find(condition, "id", true).firstElement();
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @param ascendent true if the results are ordered ascendint
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions, String orderColumnName, boolean ascendent) throws SQLException {
        return this.find(conditions, orderColumnName, ascendent).firstElement();
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @param orderColumnName the name of the column to order the results
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions, String orderColumnName) throws SQLException {
        return this.find(conditions, orderColumnName).firstElement();
    }

    /**
     * Find the first element that meet with the suplied condition
     * @param conditions the conditions that must meet the element found
     * @return the first register that meet with the supplied condition
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public G findFirst(Vector<Condition> conditions) throws SQLException {
        return this.find(conditions).firstElement();
    }

    /**
     * If there are registers that meet with the supplied conditions
     * @param conditions the conditions that must meet the registers
     * @return true if there are registers that meet with the supplied conditions
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean hasAny(Vector<Condition> conditions) throws SQLException {
        long contador = count(conditions);
        if (contador > 0) {
            return true;
        }
        return false;
    }

    /**
     * If there are registers that meet with the supplied conditions
     * @param condition the condition that must meet the registers
     * @return true if there are registers that meet with the supplied conditions
     * @throws java.sql.SQLException raises an exception if an error ocurs
     */
    public boolean hasAny(Condition condition) throws SQLException {
        long contador = count(condition);
        if (contador > 0) {
            return true;
        }
        return false;
    }

    /**
     * Get the first ID in the table
     * @return the first ID in the table
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public String getFirstId() throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT MIN(id) FROM ");
        sb.append(this.getSchemaName());
        sb.append(".");
        sb.append(this.getTableName());
        sb.append(";");
        ResultSet rs = this.getConector().executeQuery(sb.toString());
        rs.next();
        return rs.getString(1);
    }

    /**
     * List all the registers in the table
     * @return the registers in the table
     * @throws java.sql.SQLException raises an expcetion if an error happens
     */
    public Vector<G> list() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.find(conditions, "id", true);
    }

    /**
      * Read the values for register ID
      * @param id the register that we want to read the values
      * @return the values for the register
      * @throws java.sql.SQLException raises an exception if an error happens
      */
    public Row read(String id) throws SQLException {
        QuerySelect querySelect = new QuerySelect(this.getSchemaName(), this.getTableName());
        querySelect.addCondition("id", id, Condition.IGUAL, Condition.AND);
        Vector<Row> result = this.getConector().executeQuerySelect(querySelect);
        if ((result != null) && (result.size() > 0)) {
            return result.firstElement();
        }
        return null;
    }

    /**
      * Read the values for register ID
      * @return the values for the register
      * @throws java.sql.SQLException raises an exception if an error happens
      */
    public boolean read() throws SQLException {
        Row row = read(this.getId());
        if (row != null) {
            this.setRow(read(this.getId()));
            return true;
        }
        return false;
    }

    /**
     * Read a value from a register
     * @param id the register from we want to read a value
     * @param columnName the name of the column we want to know the value
     * @return the value for the column
     * @throws java.sql.SQLException raises an exception if an error happens
     */
    public String readValue(String id, String columnName) throws SQLException {
        if (id != null) {
            Row row = read(id);
            if (row.containsColumn(columnName)) {
                return row.getValue(columnName);
            }
        }
        return "";
    }

    /**
     * Get the value for a column in the current object
     * @param columnName tha name of the column we want to know the value
     * @return the value we want to know
     */
    public String getValue(String columnName) {
        return this.getRow().getValue(columnName);
    }

    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @param orders the orders to order the result
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    public Vector<G> view(String viewName, Vector<Condition> conditions, Vector<Order> orders) throws SQLException {
        Vector<G> resultado = new Vector<G>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(viewName);
        sb.append(" ");
        if ((conditions != null) && (conditions.size() > 0)) {
            sb.append(this.Condition(conditions));
        }
        if ((orders != null) && (orders.size() > 0)) {
            sb.append(this.orderToSql(orders));
        }
        sb.append(";");
        ResultSet rs = this.getConector().executeQuery(sb.toString());
        while (rs.next()) {
            resultado.add(fromResultSetToWrapper(rs));
        }
        return resultado;
    }

    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @param order the order to order the result
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    public Vector<G> view(String viewName, Vector<Condition> conditions, Order order) throws SQLException {
        Vector<Order> orders = new Vector<Order>();
        orders.add(order);
        return view(viewName, conditions, orders);
    }

    /**
     * Get a view for the table
     * @param viewName the name of the view
     * @param conditions the conditions that must meet the registers
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    public Vector<G> view(String viewName, Vector<Condition> conditions) throws SQLException {
        return view(viewName, conditions, new Vector<Order>());
    }

    /**
     * Get a view for the table, selection all registers in the table
     * @return the view
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    public Vector<G> view() throws SQLException {
        Vector<Condition> conditions = new Vector<Condition>();
        return this.find(conditions, "id", true);
    }

    public QuerySelect getQuerySelect() {
        return new QuerySelect(this.getSchemaName(), this.getTableName());
    }

    /**
     * Convert from resultset to hashtable
     * @param rs resultset to convert
     * @return the hasttable obteneid
     * @throws java.sql.SQLException raises an exception if an error ocurrs
     */
    protected Hashtable<String, String> fromResultSetToHashTable(ResultSet rs) throws SQLException {
        Hashtable<String, String> resultado = new Hashtable<String, String>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnas = rsmd.getColumnCount();
        for (int contador = 1; contador <= columnas; contador++) {
            String columnName = rsmd.getColumnName(contador);
            if (rs.getString(contador) != null) {
                resultado.put(columnName, rs.getString(contador));
            } else {
                resultado.put(columnName, "");
            }
        }
        return resultado;
    }

    protected Row fromResultSetToRow(ResultSet rs) throws SQLException {
        Row row = new Row();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnas = rsmd.getColumnCount();
        for (int contador = 1; contador <= columnas; contador++) {
            String columnName = rsmd.getColumnName(contador);
            if (rs.getString(contador) != null) {
                row.put(columnName, rs.getString(contador));
            } else {
                row.put(columnName, "");
            }
        }
        return row;
    }

    protected G fromResultSetToWrapper(ResultSet rs) throws SQLException {
        Row row = fromResultSetToRow(rs);
        G wrapper = this.createWrapper();
        wrapper.setId(row.getValue("id"));
        wrapper.setRow(row);
        return wrapper;
    }

    /**
     * Transform a vector of orders in a string
     * @param orders the vector of orders
     * @return an string that contains all the orders
     */
    protected String orderToSql(Vector<Order> orders) {
        if ((orders != null) && (orders.size() > 0)) {
            StringBuffer sb = new StringBuffer();
            sb.append(" ORDER BY ");
            Iterator iterator = orders.iterator();
            while (iterator.hasNext()) {
                Order oe = (Order) iterator.next();
                sb.append(oe.toSql());
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Transform a vector of conditions in a string
     * @param conditions the vector of conditions to transform 
     * @return the string that contains all the conditions
     */
    protected String Condition(Vector<Condition> conditions) {
        if ((conditions != null) && (conditions.size() > 0)) {
            StringBuffer sb = new StringBuffer();
            sb.append(" WHERE ");
            Iterator iterator = conditions.iterator();
            if (iterator.hasNext()) {
                sb.append(((Condition) iterator.next()).toSql());
            }
            while (iterator.hasNext()) {
                Condition wc = (Condition) iterator.next();
                sb.append(wc.conditionToSql());
                sb.append(wc.toSql());
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * Get the conector for the database
     * @return the conector for the database
     */
    public Conector getConector() {
        return _conector;
    }

    /**
     * Set the conector for the database
     * @param conector the conector for the database
     */
    public void setConector(Conector conector) {
        this._conector = conector;
    }

    /**
     * Get the current ID
     * @return the current ID
     */
    public String getId() {
        return _id;
    }

    /**
     * Set the ID for the current register
     * @param id the ID for the current register
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Get the values for the current register
     * @return the values for the current register
     */
    public Row getRow() {
        return _row;
    }

    /**
     * Get the values for the current register
     * @param row 
     */
    public void setRow(Row row) {
        this._row = row;
    }

    /**
     * Get the selected registers
     * @return the selected registers
     */
    public Vector<String> getSelected() {
        return _selected;
    }

    /**
     * Set the selected registers
     * @param selected the selected registers
     */
    protected void setSelected(Vector<String> selected) {
        this._selected = selected;
    }

    /**
     * Get the name of the schema
     * @return the name of the schema
     */
    public String getSchemaName() {
        return _schemaName;
    }

    /**
     * Set the name of the schema
     * @param schemaName the name of the schema
     */
    protected void setSchemaName(String schemaName) {
        this._schemaName = schemaName;
    }

    /**
     * Get the name of the current table
     * @return the name of the currente table
     */
    public String getTableName() {
        return _tableName;
    }

    /**
     * Set tha name of the current table
     * @param tableName the name of the current table
     */
    protected void setTableName(String tableName) {
        this._tableName = tableName;
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(((WrapperView) o).toString());
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }
}
