package mx.com.nyak.orden.dao.impl;

import java.sql.SQLException;
import mx.com.nyak.base.dao.BaseJDBCDAO;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.exception.DataAccesException;
import mx.com.nyak.orden.dto.Producto;
import org.apache.log4j.Logger;

public class ProductoDAOJdbcImpl extends BaseJDBCDAO {

    public ProductoDAOJdbcImpl() throws DataAccesException {
        super();
    }

    private Logger logger = Logger.getLogger(ProductoDAOJdbcImpl.class);

    public void update(Producto producto) throws DataAccesException {
        StringBuffer query = new StringBuffer();
        logger.debug("DATOS DE PRODUCTO  EN DAO JDBC " + producto);
        query.append(" UPDATE producto ");
        query.append(" SET utilidad=" + producto.getUtilidad() + ", ");
        query.append(" precio_unitario=" + producto.getPrecioUnitario() + ", ");
        query.append(" producto='" + producto.getProducto() + "', ");
        query.append(" clave='" + producto.getClave() + "', ");
        query.append(" codigo='" + producto.getCodigo() + "' ");
        query.append(" WHERE id_producto=" + producto.getIdProducto());
        logger.info("QUERY: " + query.toString());
        try {
            executeUpdate(query.toString());
        } catch (SQLException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new DataAccesException(e.getMessage(), e);
        } finally {
            try {
                closeConnection();
                if (getRs() != null) {
                    getRs().close();
                }
            } catch (Exception ex) {
                logger.error(StackTraceUtil.getStackTrace(ex));
                throw new DataAccesException(ex);
            }
        }
    }
}
