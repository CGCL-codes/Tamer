package fondefitco.Controlador;

import fondefitco.Modelo.RegistroCajaMenor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rodolfo Carcamo
 */
public class ControlRegistroCajaMenor {

    private static List registro;

    private static Connection con;

    public static RegistroCajaMenor load(ResultSet rs) throws SQLException {
        RegistroCajaMenor p = new RegistroCajaMenor();
        p.setFecha(rs.getDate(1));
        p.setMonto_Dinero(rs.getFloat(2));
        p.setObservacion(rs.getString(3));
        p.setNombreTercero(rs.getString(4));
        p.setIdentificacion_Tercero(rs.getString(5));
        p.setResponsableEntrega(rs.getString(6));
        return p;
    }

    public static void agregarEntregaDinero(RegistroCajaMenor e) throws SQLException, Exception {
        ManejadorBaseDatos mbd = ManejadorBaseDatos.getInstancia();
        mbd.conectar();
        con = mbd.getConexion();
        if (con == null) {
            throw new SQLException(" no hay conexion ");
        }
        PreparedStatement pst = null;
        try {
            pst = con.prepareStatement("Insert Into registrocajamenor values(?,?,?,?,?,?)");
            java.sql.Date fechas = new java.sql.Date(e.getFecha().getTime());
            pst.setDate(1, fechas);
            pst.setFloat(2, e.getMonto_Dinero());
            pst.setString(3, e.getObservacion());
            pst.setString(4, e.getNombreTercero());
            pst.setString(5, e.getIdentificacion_Tercero());
            pst.setString(6, e.getResponsableEntrega());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }
}
