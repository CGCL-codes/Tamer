package mx.com.nyak.orden.dao.impl;

import java.sql.SQLException;
import mx.com.nyak.base.dao.BaseJDBCDAO;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.exception.DataAccesException;
import mx.com.nyak.orden.dto.Atributo;
import org.apache.log4j.Logger;

public class AtributoDAOJdbcImpl extends BaseJDBCDAO {

    private Logger logger = Logger.getLogger(AtributoDAOJdbcImpl.class);

    public AtributoDAOJdbcImpl() throws DataAccesException {
        super();
    }

    public void update(Atributo atributo) throws DataAccesException {
        StringBuffer query = new StringBuffer();
        logger.debug("DATOS DEL atributo EN DAO JDBC " + atributo);
        query.append(" UPDATE atributo ");
        query.append(" SET fecha_atributo=" + atributo.getFechaAtributo() + ", ");
        query.append("  etiqueta='" + atributo.getEtiqueta() + "' , ");
        query.append("  valor='" + atributo.getValor() + "' , ");
        query.append(" 	WHERE id_atributo=" + atributo.getIdAtributo());
        try {
            executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
                if (getRs() != null) {
                    getRs().close();
                }
            } catch (Exception ex) {
                logger.error(StackTraceUtil.getStackTrace(ex));
            }
        }
    }

    public void delete(Atributo atributo) throws DataAccesException {
        StringBuffer query = new StringBuffer();
        logger.debug("DATOS DEL atributo EN DAO JDBC " + atributo);
        query.append(" DELETE FROM atributo ");
        query.append(" 	WHERE id_atributo=" + atributo.getIdAtributo());
        try {
            executeUpdate(query.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
                if (getRs() != null) {
                    getRs().close();
                }
            } catch (Exception ex) {
                logger.error(StackTraceUtil.getStackTrace(ex));
            }
        }
    }
}
