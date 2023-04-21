package operador.dao;

import inicio.InicioApp;
import filtro.FiltroUtil;
import filtro.FiltroVO;
import java.util.*;
import java.sql.*;
import operador.*;
import utilidad.Util;

public class BDOperadorUtil {

    public static ArrayList<OperadorVO> getListaOperadoresFromRS(ResultSet rs) {
        ArrayList<OperadorVO> listaOperadores = new ArrayList<OperadorVO>();
        try {
            rs.beforeFirst();
            while (rs.next()) {
                OperadorVO objOperador = new OperadorVO();
                objOperador.setId(rs.getInt("id"));
                objOperador.setPerfil(rs.getString("perfil"));
                objOperador.setNombre(rs.getString("nombre"));
                objOperador.setApellidos(rs.getString("apellidos"));
                objOperador.setDni(rs.getString("dni"));
                objOperador.setIdUsuario(rs.getInt("idUsuario"));
                objOperador.setIdEducador(rs.getInt("idEducador"));
                objOperador.setUsuario(rs.getString("usuario"));
                objOperador.setContrasena(rs.getString("contrasena"));
                objOperador.setModelo(rs.getBoolean("modelo"));
                objOperador.setNotas(rs.getString("notas"));
                objOperador.setEstado(rs.getString("estado"));
                objOperador.setPosicion(rs.getInt("posicion"));
                objOperador.setIdUsuarioCreacion(rs.getInt("idUsuarioCreacion"));
                objOperador.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                objOperador.setIdUsuarioModificacion(rs.getInt("idUsuarioModificacion"));
                objOperador.setFechaModificacion(rs.getTimestamp("fechaModificacion"));
                listaOperadores.add(objOperador);
            }
        } catch (Exception ex) {
            System.out.println("EXCEPCION EN getObjOperadorFromRS : OperadorUtil " + ex.toString());
        }
        return listaOperadores;
    }

    public static String getCondicionesSQL(OperadorParametros parametros) {
        String whereIdOperador = "";
        String whereNombre = "";
        String whereApellidos = "";
        String whereDni = "";
        String whereIdUsuario = "";
        String whereIdEducador = "";
        String whereUsuario = "";
        String whereContrasena = "";
        String whereModelo = "";
        String wherePerfil = "";
        String whereIdUsuarioCreacion = "";
        String whereEstado = "";
        if (parametros.idOperador != null) {
            whereIdOperador = "AND operadores.id = '" + parametros.idOperador + "' ";
        }
        if (parametros.nombre != null) {
            whereNombre = "AND operadores.nombre = '" + parametros.nombre + "' ";
        }
        if (parametros.apellidos != null) {
            whereApellidos = "AND operadores.apellidos = '" + parametros.apellidos + "' ";
        }
        if (parametros.dni != null) {
            whereDni = "AND operadores.dni = '" + parametros.dni + "' ";
        }
        if (parametros.idUsuario != null) {
            whereIdUsuario = "AND operadores.idUsuario = '" + parametros.idUsuario + "' ";
        }
        if (parametros.idEducador != null) {
            whereIdEducador = "AND operadores.idEducador = '" + parametros.idEducador + "' ";
        }
        if (parametros.usuario != null) {
            whereUsuario = "AND operadores.usuario = '" + parametros.usuario + "' ";
        }
        if (parametros.contrasena != null) {
            whereContrasena = "AND operadores.contrasena = '" + parametros.contrasena + "' ";
        }
        if (parametros.modelo != null) {
            whereModelo = "AND operadores.modelo = '" + Util.boolToInt(parametros.modelo) + "' ";
        }
        if (parametros.perfil != null) {
            wherePerfil = "AND operadores.perfil = '" + parametros.perfil + "' ";
        }
        if (parametros.idUsuarioCreacion != null) {
            whereIdUsuarioCreacion = "AND operadores.idUsuarioCreacion = '" + parametros.idUsuarioCreacion + "' ";
        }
        if (parametros.estado != null) {
            whereEstado = "AND operadores.estado = '" + parametros.estado + "' ";
        }
        String sql = "";
        sql += whereIdOperador;
        sql += whereNombre;
        sql += whereApellidos;
        sql += whereDni;
        sql += whereIdUsuario;
        sql += whereIdEducador;
        sql += whereUsuario;
        sql += whereContrasena;
        sql += whereModelo;
        sql += wherePerfil;
        sql += whereIdUsuarioCreacion;
        sql += whereEstado;
        return sql;
    }

    public static String getSqlModificacionPermisosOperador() {
        String sqlAdmin = "";
        if (OperadorUtil.PERFIL_ADMIN.equals(InicioApp.operador.getPerfil())) {
            String sqlPerfil = "";
            sqlPerfil += "AND (operadores.perfil = '" + OperadorUtil.PERFIL_ADMIN + "' ";
            sqlPerfil += "     OR operadores.perfil = '" + OperadorUtil.PERFIL_USUARIO + "') ";
            String sqlCG = "";
            ArrayList<FiltroVO> listaFiltrosAcceso = InicioApp.operador.getListaFiltrosAcceso();
            for (FiltroVO f : listaFiltrosAcceso) {
                if (FiltroUtil.ENTIDAD_ACCESO_CENTRO_GESTION.equals(f.getEntidadAcceso())) {
                    sqlCG += "OR centrosgestion.id = " + f.getIdEntidadAcceso();
                }
            }
            if (sqlCG.length() > 0) {
                sqlCG = sqlCG.replaceFirst("OR", "");
                sqlCG = "AND (" + sqlCG + ") ";
            }
            sqlAdmin = sqlPerfil + sqlCG;
        }
        String sqlUsuario = "";
        if (OperadorUtil.PERFIL_USUARIO.equals(InicioApp.operador.getPerfil())) {
            sqlUsuario += "AND operadores.id = '" + InicioApp.operador.getId() + "' ";
        }
        return sqlAdmin + sqlUsuario;
    }

    public static String getSqlWhere(OperadorParametros parametros) {
        String sql = "";
        sql += getCondicionesSQL(parametros);
        sql += getSqlModificacionPermisosOperador();
        String sqlWhere = " ";
        if (!sql.isEmpty()) {
            sqlWhere = sql.replaceFirst("AND", "WHERE ");
        }
        return sqlWhere;
    }

    public static String getSqlOrder(OperadorParametros parametros) {
        String orden = "operadores.usuario ";
        if (parametros.ordenSQL != null) {
            orden = parametros.ordenSQL;
        }
        return "ORDER BY " + orden;
    }
}
