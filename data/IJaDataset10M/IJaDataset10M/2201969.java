package telefono.dao;

import java.util.*;
import java.sql.*;
import telefono.*;

/**
 *
 * @author Egalcom
 */
public class BDTelefonoUtil {

    public static ArrayList<TelefonoVO> getListaTelefonosFromRS(ResultSet rs) {
        ArrayList<TelefonoVO> listaTelefonos = new ArrayList<TelefonoVO>();
        try {
            rs.beforeFirst();
            while (rs.next()) {
                TelefonoVO objTelefono = new TelefonoVO();
                objTelefono.setId(rs.getInt("id"));
                objTelefono.setModo(rs.getString("modo"));
                objTelefono.setIdEntidad(rs.getInt("idEntidad"));
                objTelefono.setEntidad(rs.getString("entidad"));
                objTelefono.setTipo(rs.getString("tipo"));
                objTelefono.setNumero(rs.getString("numero"));
                objTelefono.setPpl(rs.getBoolean("ppl"));
                objTelefono.setNotas(rs.getString("notas"));
                objTelefono.setEstado(rs.getString("estado"));
                objTelefono.setPosicion(rs.getInt("posicion"));
                objTelefono.setIdUsuarioCreacion(rs.getInt("idUsuarioCreacion"));
                objTelefono.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                objTelefono.setIdUsuarioModificacion(rs.getInt("idUsuarioModificacion"));
                objTelefono.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                listaTelefonos.add(objTelefono);
            }
        } catch (Exception ex) {
            System.out.println("EXCEPCION EN getObjTelefonoFromRS : TelefonoUtil " + ex.toString());
        }
        return listaTelefonos;
    }

    public static String getCondicionesSQL(TelefonoParametros parametros) {
        String whereIdTelefono = "";
        String whereEstado = "";
        String whereModo = "";
        String whereTipo = "";
        String whereIdEntidad = "";
        String whereEntidad = "";
        if (parametros.idTelefono != null) {
            whereIdTelefono = "AND telefonos.id = '" + parametros.idTelefono + "' ";
        }
        if (parametros.estado != null) {
            whereEstado = "AND telefonos.estado = '" + parametros.estado + "' ";
        }
        if (parametros.modo != null) {
            whereModo = "AND telefonos.modo = '" + parametros.modo + "' ";
        }
        if (parametros.tipo != null) {
            whereTipo = "AND telefonos.tipo = '" + parametros.tipo + "' ";
        }
        if (parametros.idEntidad != null) {
            whereIdEntidad = "AND telefonos.idEntidad = '" + parametros.idEntidad + "' ";
        }
        if (parametros.entidad != null) {
            whereEntidad = "AND telefonos.entidad LIKE '%" + parametros.entidad + "%' ";
        }
        String sql = "";
        sql += whereIdTelefono;
        sql += whereEstado;
        sql += whereModo;
        sql += whereTipo;
        sql += whereIdEntidad;
        sql += whereEntidad;
        return sql;
    }

    public static String getSqlWhere(TelefonoParametros parametros) {
        String sql = getCondicionesSQL(parametros);
        String sqlWhere = " ";
        if (!sql.isEmpty()) {
            sqlWhere = sql.replaceFirst("AND", "WHERE ");
        }
        return sqlWhere;
    }

    public static String getSqlOrder(TelefonoParametros parametros) {
        String orden = "telefonos.numero ";
        if (parametros.ordenSQL != null) {
            orden = parametros.ordenSQL;
        }
        return "ORDER BY " + orden;
    }
}
