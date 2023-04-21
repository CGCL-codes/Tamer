package proyecto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import proyecto.excepcion.DAOExcepcion;
import proyecto.modelo.Cuota;
import proyecto.modelo.enumeracion.CuotaEstado;
import proyecto.util.ConexionBD;
import proyecto.modelo.enumeracion.*;

public class CuotaDAO extends BaseDAO {

    public Cuota insertar(Cuota vo) throws DAOExcepcion {
        System.out.println("CuotaDAO: insertar(Cuota vo)");
        String query = "INSERT INTO cuota(moneda, importe, fechaVencimiento, estado, idPropiedad, periodo) VALUES (?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.obtenerConexion();
            stmt = con.prepareStatement(query);
            stmt.setString(1, vo.getMoneda());
            stmt.setDouble(2, vo.getImporte());
            stmt.setString(3, vo.getFechaVencimiento());
            stmt.setInt(4, vo.getEstado().value());
            stmt.setInt(5, vo.getIdPropiedad());
            stmt.setString(6, vo.getPeriodo());
            int i = stmt.executeUpdate();
            if (i != 1) {
                throw new SQLException("No se pudo insertar");
            }
            int idp = 0;
            query = "SELECT LAST_INSERT_ID()";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                idp = rs.getInt(1);
            }
            vo.setIdCuota(idp);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
        return vo;
    }

    public Collection<Cuota> GetByPropiedad(int idPropiedad) throws DAOExcepcion {
        System.out.println("CuotaDAO: GetByPropiedad(int idPropiedad)");
        Collection<Cuota> c = new ArrayList<Cuota>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.obtenerConexion();
            String query = "SELECT idCuota,moneda,importe,fechaVencimiento,estado,idPropiedad,periodo from cuota";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cuota vo = new Cuota();
                vo.setIdCuota(Integer.parseInt(rs.getString("idCuota")));
                vo.setMoneda(rs.getString("moneda"));
                vo.setImporte(Double.parseDouble(rs.getString("importe")));
                vo.setFechaVencimiento(rs.getString("fechaVencimiento"));
                vo.setEstado(CuotaEstado.lookup(rs.getInt("estado")));
                vo.setIdPropiedad(Integer.parseInt(rs.getString("idPropiedad")));
                vo.setPeriodo(rs.getString("periodo"));
                c.add(vo);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
        return c;
    }

    public Collection<Cuota> listarMorosos() throws DAOExcepcion {
        System.out.println("CuotaDAO: listarMorosos()");
        Collection<Cuota> c = new ArrayList<Cuota>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.obtenerConexion();
            String query = "SELECT idCuota,moneda,importe,fechaVencimiento,estado,idPropiedad,periodo from cuota where estado = 1";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cuota vo = new Cuota();
                vo.setIdCuota(Integer.parseInt(rs.getString("idCuota")));
                vo.setMoneda(rs.getString("moneda"));
                vo.setImporte(Double.parseDouble(rs.getString("importe")));
                vo.setFechaVencimiento(rs.getString("fechaVencimiento"));
                vo.setEstado(CuotaEstado.lookup(rs.getInt("estado")));
                vo.setIdPropiedad(Integer.parseInt(rs.getString("idPropiedad")));
                vo.setPeriodo(rs.getString("periodo"));
                c.add(vo);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
        return c;
    }

    public Collection<Cuota> getCuotasPendientesDeUsuario(String usuario) throws DAOExcepcion {
        System.out.println("CuotaDAO: getCuotasPendientesDeUsuario()");
        Collection<Cuota> c = new ArrayList<Cuota>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ConexionBD.obtenerConexion();
            String query = "select c.idCuota, c.moneda, c.importe, c.fechaVencimiento, c.estado, c.periodo, p.idpropietario, c.idpropiedad " + "from cuota c " + "inner join propiedad p on p.idpropiedad=c.idpropiedad " + "inner join persona e on e.idpersona=p.idpropietario " + "inner join usuario u on u.usuario=e.usuario " + "left join pago g on g.idcuota=c.idcuota " + "where u.usuario=? and c.estado=1 or g.idPago is null ";
            stmt = con.prepareStatement(query);
            stmt.setString(1, usuario);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cuota vo = new Cuota();
                vo.setIdCuota(Integer.parseInt(rs.getString("idCuota")));
                vo.setMoneda(rs.getString("moneda"));
                vo.setImporte(Double.parseDouble(rs.getString("importe")));
                vo.setFechaVencimiento(rs.getString("fechaVencimiento"));
                vo.setEstado(CuotaEstado.lookup(rs.getInt("estado")));
                vo.setIdPropiedad(Integer.parseInt(rs.getString("idPropiedad")));
                vo.setPeriodo(rs.getString("periodo"));
                c.add(vo);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
        return c;
    }

    public Cuota getCuota(int idCuota) throws DAOExcepcion {
        System.out.println("CuotaDAO: getCuota()");
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cuota vo = new Cuota();
        try {
            con = ConexionBD.obtenerConexion();
            String query = "select * from cuota where idcuota=? ";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, idCuota);
            rs = stmt.executeQuery();
            if (rs.next()) {
                vo.setIdCuota(Integer.parseInt(rs.getString("idCuota")));
                vo.setMoneda(rs.getString("moneda"));
                vo.setImporte(Double.parseDouble(rs.getString("importe")));
                vo.setFechaVencimiento(rs.getString("fechaVencimiento"));
                vo.setEstado(CuotaEstado.lookup(rs.getInt("estado")));
                vo.setIdPropiedad(Integer.parseInt(rs.getString("idPropiedad")));
                vo.setPeriodo(rs.getString("periodo"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
        return vo;
    }

    public void pagarCuota(int idCuota) throws DAOExcepcion {
        System.out.println("CuotaDAO: pagarCuota");
        Connection con = null;
        PreparedStatement stmt = null;
        Cuota cuotaVO = getCuota(idCuota);
        try {
            String query = "INSERT INTO pago (moneda, importe, tipopago, fechapago, idcuota) " + "VALUES (?,?,?,now(),?)";
            con = ConexionBD.obtenerConexion();
            stmt = con.prepareStatement(query);
            stmt.setString(1, cuotaVO.getMoneda());
            stmt.setDouble(2, cuotaVO.getImporte());
            stmt.setString(3, "1");
            stmt.setInt(4, cuotaVO.getIdCuota());
            int i = stmt.executeUpdate();
            if (i != 1) {
                throw new SQLException("No se pudo actualizar el estado");
            }
            query = "UPDATE cuota SET estado=2 " + "WHERE idcuota=" + cuotaVO.getIdCuota();
            con = null;
            stmt = null;
            con = ConexionBD.obtenerConexion();
            stmt = con.prepareStatement(query);
            i = stmt.executeUpdate();
            if (i != 1) {
                throw new SQLException("No se pudo actualizar el estado");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOExcepcion(e.getMessage());
        } finally {
            this.cerrarStatement(stmt);
            this.cerrarConexion(con);
        }
    }
}
