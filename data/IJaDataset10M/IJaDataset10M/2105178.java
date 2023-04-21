package org.briareo.common.jdbc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.log4j.Logger;
import org.briareo.common.jdbc.exception.DbException;

public abstract class QueryBuilder {

    /**
   * Loggger de clase
   */
    private static Logger logger = Logger.getLogger(QueryBuilder.class);

    /**
   * Obtiene un array con los parametros en el orden deseado.
   * 
   * @param hm
   * @return
   */
    protected abstract Object[] getArrayParams(HashMap hm);

    /**
   * El array es el conjunto de valores a remplazar en la query.<br>
   * NOTA: Los parametros son posicionales y el orden se define en
   * getArrayParams( HashMap)
   * 
   * @param aoParams
   * @return
   */
    protected abstract String getSQLQuery(Object[] aoParams);

    /**
   * Recoge el elemento 'index' del vector.
   * 
   * @param aoParams
   * @param index
   * @return
   */
    protected Object getArrayParam(Object[] aoParams, int index) {
        Object obj = null;
        try {
            obj = aoParams[index];
        } catch (NullPointerException ex) {
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
        return obj;
    }

    /**
   * Metodo para eliminar los parametros que sean nulos.
   * 
   * @param ao
   * @return
   */
    protected Object[] deleteNullElements(Object[] ao) {
        Object[] aoRet = new Object[0];
        List elementosNoNulos = new ArrayList();
        for (int i = 0; i < ao.length; i++) {
            if (ao[i] != null) {
                elementosNoNulos.add(ao[i]);
            }
        }
        aoRet = elementosNoNulos.toArray();
        return aoRet;
    }

    /**
   * Obtiene la un List con el resultado de la ejecucion de la query deducida de
   * los parametros.
   * 
   * @param qr
   * @param hmParams
   * @return
   * @throws DbException
   */
    private List execQuery(QueryRunner qr, HashMap hmParams) throws DbException {
        List result = new ArrayList();
        try {
            Object[] aoParams = this.getArrayParams(hmParams);
            String sql = this.getSQLQuery(aoParams);
            logger.debug("[getPRUs]:: sql: " + sql);
            aoParams = this.deleteNullElements(aoParams);
            result = (List) qr.query(sql, aoParams, new ArrayListHandler());
        } catch (Exception ex) {
            throw new DbException("Error en la ejecucion de la SQL.", ex);
        }
        return result;
    }

    /**
   * Obtiene la un List con el resultado de la ejecucion de la query deducida de
   * los parametros.
   * 
   * @param ds
   * @param hmParams
   * @return
   * @throws DbException
   */
    public List execQuery(DataSource ds, HashMap hm) throws DbException {
        List result = new ArrayList();
        QueryRunner qr = new QueryRunner(ds);
        result = this.execQuery(qr, hm);
        return result;
    }
}
