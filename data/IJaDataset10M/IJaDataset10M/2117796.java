package unidadGestion.dao;

import java.util.*;
import java.sql.*;
import unidadGestion.*;

public class BDUnidadGestionUtil {

    public static ArrayList<UnidadGestionVO> getListaUnidadesGestionFromRS(ResultSet rs) {
        ArrayList<UnidadGestionVO> listaUnidadsGestion = new ArrayList<UnidadGestionVO>();
        try {
            rs.beforeFirst();
            while (rs.next()) {
                UnidadGestionVO objUnidadGestion = new UnidadGestionVO();
                objUnidadGestion.setId(rs.getInt("id"));
                objUnidadGestion.setIdCentroGestion(rs.getInt("idCentroGestion"));
                objUnidadGestion.setNombreCentroGestion(rs.getString("nombreCentroGestion"));
                objUnidadGestion.setNombre(rs.getString("nombre"));
                objUnidadGestion.setNotas(rs.getString("notas"));
                objUnidadGestion.setEstado(rs.getString("estado"));
                objUnidadGestion.setPosicion(rs.getInt("posicion"));
                objUnidadGestion.setIdUsuarioCreacion(rs.getInt("idUsuarioCreacion"));
                objUnidadGestion.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                objUnidadGestion.setIdUsuarioModificacion(rs.getInt("idUsuarioModificacion"));
                objUnidadGestion.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                listaUnidadsGestion.add(objUnidadGestion);
            }
        } catch (Exception ex) {
            System.out.println("EXCEPCION EN getObjUnidadGestionFromRS : UnidadGestionUtil " + ex.toString());
        }
        return listaUnidadsGestion;
    }

    public static String getCondicionesSQL(UnidadGestionParametros parametros) {
        String whereBusquedaRapida = "";
        String whereIdUnidadGestion = "";
        String whereIdCentroGestion = "";
        String whereNombre = "";
        String whereEstado = "";
        if (parametros.busquedaRapida != null) {
            whereBusquedaRapida = "AND ( unidadesgestion.nombre LIKE '%" + parametros.busquedaRapida + "%' ";
            whereBusquedaRapida += "      OR centrosgestion.nombre LIKE '%" + parametros.busquedaRapida + "%') ";
        }
        if (parametros.idUnidadGestion != null) {
            whereIdUnidadGestion = "AND unidadesgestion.id = '" + parametros.idUnidadGestion + "' ";
        }
        if (parametros.idCentroGestion != null) {
            whereIdCentroGestion = "AND unidadesgestion.idCentroGestion = '" + parametros.idCentroGestion + "' ";
        }
        if (parametros.nombre != null) {
            whereNombre = "AND unidadesgestion.nombre like '%" + parametros.nombre + "%' ";
        }
        if (parametros.estado != null) {
            whereEstado = "AND unidadesgestion.estado = '" + parametros.estado + "' ";
        }
        String sql = "";
        sql += whereBusquedaRapida;
        sql += whereIdUnidadGestion;
        sql += whereIdCentroGestion;
        sql += whereNombre;
        sql += whereEstado;
        return sql;
    }

    public static String getSqlWhere(UnidadGestionParametros parametros) {
        String sql = getCondicionesSQL(parametros);
        String sqlWhere = " ";
        if (!sql.isEmpty()) {
            sqlWhere = sql.replaceFirst("AND", "WHERE ");
        }
        return sqlWhere;
    }

    public static String getSqlOrder(UnidadGestionParametros parametros) {
        String orden = "unidadesgestion.nombre ";
        if (parametros.ordenSQL != null) {
            orden = parametros.ordenSQL;
        }
        return "ORDER BY " + orden;
    }
}
