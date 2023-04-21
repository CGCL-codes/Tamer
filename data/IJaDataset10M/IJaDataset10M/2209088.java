package mwt.xml.xdbforms.dblayer.query.impl;

import java.util.ArrayList;
import java.util.List;
import mwt.xml.xdbforms.dblayer.query.Criteria;
import mwt.xml.xdbforms.dblayer.query.SQLOperation;

/**
 * Implementa l'operazione di inserimento
 * Per vedere una descrizione dei metodi fare riferiemento all'interfaccia
 * SQLOperation.
 * @see SQLOperation
 * @author Gianfranco Murador, Matteo Ferri, Cristian Castiglia
 */
public class SQLInsert extends SQLOperation {

    private StringBuilder statement;

    private List<SQLParameter> parameters = null;

    private List<Object> values = null;

    private boolean isCompiled = false;

    public SQLInsert(String table) {
        statement = new StringBuilder("INSERT INTO " + table + "(");
        parameters = new ArrayList<SQLParameter>();
        values = new ArrayList<Object>();
    }

    @Override
    public void declareParameter(SQLParameter sqlp, Object value) {
        parameters.add(sqlp);
        values.add(value);
    }

    @Override
    public void compile() throws RuntimeException {
        int paramLength = parameters.size();
        int valuesLength = values.size();
        if (paramLength != valuesLength) {
            throw new RuntimeException("Error, parameters and values mismatch");
        }
        for (SQLParameter sQLParameter : parameters) {
            statement.append(sQLParameter.getName() + ",");
        }
        statement.deleteCharAt(statement.length() - 1);
        statement.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            statement.append("?,");
        }
        statement.deleteCharAt(statement.length() - 1);
        statement.append(")");
        isCompiled = true;
    }

    @Override
    public String toString() {
        if (isCompiled) {
            return statement.toString();
        } else {
            compile();
            return statement.toString();
        }
    }

    @Override
    public List<SQLParameter> getParameters() {
        return parameters;
    }

    @Override
    public Object[] getValues() {
        return values.toArray();
    }

    @Override
    public void addCriteria(Criteria c) {
        throw new UnsupportedOperationException("Not supported by an insert operation.");
    }
}
