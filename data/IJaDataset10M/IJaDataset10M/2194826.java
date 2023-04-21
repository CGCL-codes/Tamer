package subfamilia2.dao;

import java.util.*;
import java.sql.*;
import utilidad.*;
import utilidad.dao.*;
import subfamilia2.*;
import dao.factory.*;

/**
 *
 * @author Sergio
 */
public class MySQLSubfamilia2DAO implements Subfamilia2DAO {

    public ArrayList<Subfamilia2VO> listadoSubfamilias2(Subfamilia2Parametros parametros) {
        ArrayList<Subfamilia2VO> listaSubfamilias2 = new ArrayList<Subfamilia2VO>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sqlSelect = "";
            sqlSelect += "SELECT ";
            sqlSelect += "subfamilias2.*, ";
            sqlSelect += "subfamilias1.nombre AS nombreSubfamilia1, ";
            sqlSelect += "familias.nombre AS nombreFamilia ";
            sqlSelect += "FROM ";
            sqlSelect += "subfamilias2 ";
            sqlSelect += "INNER JOIN subfamilias1 ON subfamilias2.idSubfamilia1 = subfamilias1.id ";
            sqlSelect += "INNER JOIN familias ON subfamilias1.idFamilia = familias.id ";
            sqlSelect += BDSubfamilia2Util.getSqlWhere(parametros);
            sqlSelect += BDSubfamilia2Util.getSqlOrder(parametros);
            con = inicio.InicioApp.factoriaDAO.getConexion();
            st = con.createStatement();
            rs = st.executeQuery(sqlSelect);
            listaSubfamilias2 = BDSubfamilia2Util.getListaSubfamilias2FromRS(rs);
        } catch (Exception e) {
            System.out.println("EXCEPCION EN listadoSubfamilia2 : MySQLDAOSubfamilia2 " + e.toString());
        } finally {
            ConexionBD.cerrarConexiones(rs, st, con);
        }
        return listaSubfamilias2;
    }

    public int insertar(Subfamilia2VO subfamilia2) {
        Connection con = null;
        PreparedStatement pstInsertar = null;
        try {
            String sqlInsert = null;
            sqlInsert = "INSERT INTO subfamilias2 ";
            sqlInsert += "( ";
            sqlInsert += "idSubfamilia1,nombre,notas,estado,posicion, ";
            sqlInsert += BDUtil.sqlInsertarDatosUsuario();
            sqlInsert += ")";
            sqlInsert += "VALUES ";
            sqlInsert += "( ?,?,?,?,?, ?,?,?,? ) ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstInsertar = con.prepareStatement(sqlInsert);
            pstInsertar.setInt(1, subfamilia2.getIdSubfamilia1());
            pstInsertar.setString(2, subfamilia2.getNombre());
            pstInsertar.setString(3, subfamilia2.getNotas());
            pstInsertar.setString(4, subfamilia2.getEstado());
            pstInsertar.setInt(5, subfamilia2.getPosicion());
            BDUtil.asignarPSInsertarDatosUsuario(subfamilia2, pstInsertar, 6);
            int insertados = pstInsertar.executeUpdate();
            subfamilia2.setId(ConexionBD.ultimoIdInsertado(pstInsertar));
            return insertados;
        } catch (Exception e) {
            System.out.println("EXCEPCION EN Insertar Subfamilia2 : MySQLDAOSubfamilia2 " + e.toString());
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
            sqlDelete = "DELETE FROM subfamilias2 ";
            sqlDelete += "WHERE id = ? ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstEliminar = con.prepareStatement(sqlDelete);
            pstEliminar.setInt(1, id);
            return pstEliminar.executeUpdate();
        } catch (Exception e) {
            System.out.println("EXCEPCION EN eliminar Subfamilia2 : MySQLDAOSubfamilia2 " + e.toString());
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstEliminar, con);
        }
    }

    public int actualizar(Subfamilia2VO subfamilia2) {
        Connection con = null;
        PreparedStatement pstActualizar = null;
        try {
            String sqlUpdate = null;
            sqlUpdate = "UPDATE subfamilias2 ";
            sqlUpdate += "SET ";
            sqlUpdate += "idSubfamilia1 = ?, nombre = ?, notas = ?, estado = ?, posicion = ?, ";
            sqlUpdate += BDUtil.sqlActualizarDatosUsuario();
            sqlUpdate += "WHERE ";
            sqlUpdate += "id = ? ";
            con = inicio.InicioApp.factoriaDAO.getConexion();
            pstActualizar = con.prepareStatement(sqlUpdate);
            pstActualizar.setInt(1, subfamilia2.getIdSubfamilia1());
            pstActualizar.setString(2, subfamilia2.getNombre());
            pstActualizar.setString(3, subfamilia2.getNotas());
            pstActualizar.setString(4, subfamilia2.getEstado());
            pstActualizar.setInt(5, subfamilia2.getPosicion());
            BDUtil.asignarPSModificarDatosUsuario(subfamilia2, pstActualizar, 6);
            pstActualizar.setInt(8, subfamilia2.getId());
            return pstActualizar.executeUpdate();
        } catch (Exception e) {
            System.out.println("EXCEPCION EN actualizar Subfamilia2 : MySQLDAOSubfamilia2 " + e.toString());
            return -1;
        } finally {
            ConexionBD.cerrarConexiones(pstActualizar, con);
        }
    }
}
