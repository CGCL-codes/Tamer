package vinculacionGrupo.dao;

import java.util.*;
import java.sql.*;
import utilidad.*;
import utilidad.dao.BDUtil;
import vinculacionGrupo.*;

public class MySQLVinculacionGrupoDAO implements VinculacionGrupoDAO {

    public ArrayList<VinculacionGrupoVO> listadoVinculacionesGrupo(VinculacionGrupoParametros parametros) {
        ArrayList<VinculacionGrupoVO> listaVinculacionesGrupo = new ArrayList<VinculacionGrupoVO>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sqlSelect = "";
            sqlSelect += "SELECT ";
            sqlSelect += "vinculacion_grupos.*, ";
            sqlSelect += "grupos.nombre AS nombreGrupo, ";
            sqlSelect += "actividades.nombre AS nombreActividad, actividades.estado AS estadoActividad, ";
            sqlSelect += "actividades.temporada AS temporadaActividad ";
            sqlSelect += "FROM ";
            sqlSelect += "vinculacion_grupos ";
            sqlSelect += "INNER JOIN grupos ON vinculacion_grupos.idGrupo = grupos.id ";
            sqlSelect += "INNER JOIN actividades ON grupos.idActividad = actividades.id ";
            sqlSelect += BDVinculacionGrupoUtil.getSqlWhere(parametros);
            sqlSelect += BDVinculacionGrupoUtil.getSqlOrder(parametros);
            con = inicio.InicioApp.factoriaDAO.getConexion();
            st = con.createStatement();
            rs = st.executeQuery(sqlSelect);
            listaVinculacionesGrupo = BDVinculacionGrupoUtil.getListaVinculacionesGrupoFromRS(rs);
        } catch (Exception e) {
            System.out.println("EXCEPCION EN listadoVinculacionesGrupo : MySQLDAOVinculacionGrupo " + e.toString());
        } finally {
            ConexionBD.cerrarConexiones(rs, st, con);
        }
        return listaVinculacionesGrupo;
    }

    public int insertar(VinculacionGrupoVO vinculacionGrupo) {
        Connection con = null;
        PreparedStatement pstInsertar = null;
        try {
            String sqlInsert = null;
            sqlInsert = "INSERT INTO vinculacion_grupos ";
            sqlInsert += "( ";
            sqlInsert += "idGrupo,entidad,idEntidad,notas,estado,posicion, ";
            sqlInsert += BDUtil.sqlInsertarDatosUsuario();
            sqlInsert += ")";
            sqlInsert += "VALUES ";
            sqlInsert += "( ?,?,?,?,?,?,   ?,?,?,? ) ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstInsertar = con.prepareStatement(sqlInsert);
            pstInsertar.setInt(1, vinculacionGrupo.getIdGrupo());
            pstInsertar.setString(2, vinculacionGrupo.getEntidad());
            pstInsertar.setInt(3, vinculacionGrupo.getIdEntidad());
            pstInsertar.setString(4, vinculacionGrupo.getNotas());
            pstInsertar.setString(5, vinculacionGrupo.getEstado());
            pstInsertar.setInt(6, vinculacionGrupo.getPosicion());
            BDUtil.asignarPSInsertarDatosUsuario(vinculacionGrupo, pstInsertar, 7);
            int insertados = pstInsertar.executeUpdate();
            vinculacionGrupo.setId(ConexionBD.ultimoIdInsertado(pstInsertar));
            return insertados;
        } catch (Exception e) {
            Util.mostrarExcepcion("Ya Existe Ese Grupo Para Este Bono", e);
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstInsertar, con);
        }
    }

    public int eliminar(int id) {
        Connection con = null;
        PreparedStatement pstEliminar = null;
        try {
            String sqlDelete = null;
            sqlDelete = "DELETE FROM vinculacion_grupos ";
            sqlDelete += "WHERE id = ? ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstEliminar = con.prepareStatement(sqlDelete);
            pstEliminar.setInt(1, id);
            return pstEliminar.executeUpdate();
        } catch (Exception e) {
            System.out.println("EXCEPCION EN eliminar VinculacionGrupo : MySQLDAOVinculacionGrupo " + e.toString());
            Util.mostrarExcepcion("", e);
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstEliminar, con);
        }
    }

    public int eliminar(String entidad, int idEntidad) {
        Connection con = null;
        PreparedStatement pstEliminar = null;
        try {
            String sqlDelete = null;
            sqlDelete = "DELETE FROM vinculacion_grupos ";
            sqlDelete += "WHERE ";
            sqlDelete += "entidad = ? ";
            sqlDelete += "AND idEntidad = ? ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstEliminar = con.prepareStatement(sqlDelete);
            pstEliminar.setString(1, entidad);
            pstEliminar.setInt(2, idEntidad);
            return pstEliminar.executeUpdate();
        } catch (Exception e) {
            System.out.println("EXCEPCION EN eliminar VinculacionGrupo : MySQLDAOVinculacionGrupo " + e.toString());
            Util.mostrarExcepcion("", e);
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstEliminar, con);
        }
    }

    public int actualizar(VinculacionGrupoVO vinculacionGrupo) {
        Connection con = null;
        PreparedStatement pstActualizar = null;
        try {
            String sqlUpdate = null;
            sqlUpdate = "UPDATE vinculacion_actividades ";
            sqlUpdate += "SET ";
            sqlUpdate += "idGrupo = ?, entidad = ?, idEntidad = ?, notas = ?, estado = ?, posicion = ?, ";
            sqlUpdate += BDUtil.sqlActualizarDatosUsuario();
            sqlUpdate += "WHERE ";
            sqlUpdate += "id = ? ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstActualizar = con.prepareStatement(sqlUpdate);
            pstActualizar.setInt(1, vinculacionGrupo.getIdGrupo());
            pstActualizar.setString(2, vinculacionGrupo.getEntidad());
            pstActualizar.setInt(3, vinculacionGrupo.getIdEntidad());
            pstActualizar.setString(4, vinculacionGrupo.getNotas());
            pstActualizar.setString(5, vinculacionGrupo.getEstado());
            pstActualizar.setInt(6, vinculacionGrupo.getPosicion());
            BDUtil.asignarPSModificarDatosUsuario(vinculacionGrupo, pstActualizar, 7);
            pstActualizar.setInt(9, vinculacionGrupo.getId());
            return pstActualizar.executeUpdate();
        } catch (Exception e) {
            System.out.println("EXCEPCION EN actualizar VinculacionGrupo : MySQLDAOVinculacionGrupo " + e.toString());
            Util.mostrarExcepcion("Ya Existe Un VinculacionGrupo Con Ese Codigo", e);
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstActualizar, con);
        }
    }
}
