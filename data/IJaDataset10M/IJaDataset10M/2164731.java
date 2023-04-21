package org.mushroomdb.query.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mushroomdb.catalog.Column;
import org.mushroomdb.catalog.Register;
import org.mushroomdb.catalog.Table;
import org.mushroomdb.engine.EvaluationEngine;
import org.mushroomdb.engine.ResultSet;
import org.mushroomdb.exception.EvaluationEngineException;
import org.mushroomdb.exception.RDBMSRuntimeException;
import org.mushroomdb.interceptor.CompositeInterceptor;
import org.mushroomdb.interceptor.InterceptorService;
import org.mushroomdb.query.Query;

/**
 * @author Matias
 *
 */
public class UpdateQuery extends Query {

    private SelectionQuery selectionQuery;

    private Map values;

    /**
	 * Construye una query de update usando una query de selecci�n
	 * para identificar qu� registros eliminar.
	 * @param selectionQuery
	 */
    public UpdateQuery(SelectionQuery selectionQuery) {
        this.selectionQuery = selectionQuery;
        this.values = new HashMap();
    }

    /**
	 * Agrega una modificaci�n de una columna.
	 * @param column
	 * @param value
	 */
    public void setUpdate(Column column, Object value) {
        this.values.put(column, value);
    }

    /**
	 * Devuelve la query que identifica los registros a modificar.
	 * @return
	 */
    public SelectionQuery getSelectionQuery() {
        return this.selectionQuery;
    }

    /**
	 * Devuelve las columnas que se van a modificar.
	 * @return
	 */
    public Iterator getUpdatedColumns() {
        return this.values.keySet().iterator();
    }

    /**
	 * Devuelve el valor de actualizaci�n para una columna.
	 * @param column
	 * @return
	 */
    public Object getUpdateValue(Column column) {
        return this.values.get(column);
    }

    /**
	 * @see org.mushroomdb.query.Query#execute(org.mushroomdb.engine.EvaluationEngine)
	 */
    public Object execute(EvaluationEngine evaluationEngine) {
        ResultSet resultSet = evaluationEngine.executeQuery(this.getSelectionQuery());
        int updateCount = 0;
        try {
            while (resultSet.hasNext()) {
                Register register = (Register) resultSet.next();
                Iterator columns = this.getUpdatedColumns();
                while (columns.hasNext()) {
                    Column column = (Column) columns.next();
                    Table table = column.getTable();
                    int index = register.getColumnIndex(column);
                    if (index > -1) {
                        InterceptorService interceptorService = InterceptorService.getInstance();
                        CompositeInterceptor compositeInterceptor = (CompositeInterceptor) interceptorService.getInterceptor();
                        compositeInterceptor.beforeUpdate(register, index, this.getUpdateValue(column), table);
                        register.setValue(index, this.getUpdateValue(column));
                        compositeInterceptor.afterUpdate(register, index, this.getUpdateValue(column), table);
                    }
                }
                updateCount++;
            }
        } catch (EvaluationEngineException e) {
            throw new RDBMSRuntimeException(e);
        }
        return new Integer(updateCount);
    }

    private void beforeUpdate(Register register, int index, Object value, Table table) {
    }
}
