package obra.dao;

import java.util.*;
import java.sql.*;
import obra.*;

/**
 *
 * @author Sergio
 */
public class BDObraUtil {

    public static ArrayList<ObraVO> getListaObrasFromRS(ResultSet rs) {
        ArrayList<ObraVO> listaObras = new ArrayList<ObraVO>();
        try {
            rs.beforeFirst();
            while (rs.next()) {
                ObraVO objObra = new ObraVO();
                objObra.setId(rs.getInt("id"));
                objObra.setIdCliente(rs.getInt("idCliente"));
                objObra.setNombreCliente(rs.getString("nombreCliente"));
                objObra.setCodigo(rs.getInt("codigo"));
                objObra.setNombre(rs.getString("nombre"));
                objObra.setIdDireccion(rs.getInt("idDireccion"));
                objObra.setDireccionCorta(rs.getString("direccionCorta"));
                objObra.setNotas(rs.getString("notas"));
                objObra.setEstado(rs.getString("estado"));
                objObra.setPosicion(rs.getInt("posicion"));
                objObra.setIdUsuarioCreacion(rs.getInt("idUsuarioCreacion"));
                objObra.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                objObra.setIdUsuarioModificacion(rs.getInt("idUsuarioModificacion"));
                objObra.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                listaObras.add(objObra);
            }
        } catch (Exception ex) {
            System.out.println("EXCEPCION EN getObjObraFromRS : ObraUtil " + ex.toString());
        }
        return listaObras;
    }

    public static String getCondicionesSQL(ObraParametros parametros) {
        String whereIdObra = "";
        String whereIdCliente = "";
        String whereIdDireccion = "";
        String whereCodigo = "";
        String whereNombre = "";
        String whereEstado = "";
        if (parametros.idObra != null) {
            whereIdObra = "AND obras.id = '" + parametros.idObra + "' ";
        }
        if (parametros.idCliente != null) {
            whereIdCliente = "AND obras.idCliente = '" + parametros.idCliente + "' ";
        }
        if (parametros.idDireccion != null) {
            whereIdDireccion = "AND obras.idDireccion = '" + parametros.idDireccion + "' ";
        }
        if (parametros.codigo != null) {
            whereCodigo = "AND obras.codigo = '" + parametros.codigo + "' ";
        }
        if (parametros.nombre != null) {
            whereNombre = "AND obras.nombre = '" + parametros.nombre + "' ";
        }
        if (parametros.estado != null) {
            whereEstado = "AND obras.estado = '" + parametros.estado + "' ";
        }
        String sql = "";
        sql += whereIdObra;
        sql += whereIdCliente;
        sql += whereIdDireccion;
        sql += whereCodigo;
        sql += whereNombre;
        sql += whereEstado;
        return sql;
    }

    public static String getSqlWhere(ObraParametros parametros) {
        String sql = getCondicionesSQL(parametros);
        String sqlWhere = " ";
        if (!sql.isEmpty()) {
            sqlWhere = sql.replaceFirst("AND", "WHERE ");
        }
        return sqlWhere;
    }

    public static String getSqlOrder(ObraParametros parametros) {
        String orden = "obras.nombre ";
        if (parametros.ordenSQL != null) {
            orden = parametros.ordenSQL;
        }
        return "ORDER BY " + orden;
    }
}
