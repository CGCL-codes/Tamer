package MDtaxsimula;

import MNtaxsimula.*;
import java.sql.*;
import java.util.*;

/**
 * Esta clase se encarga de realizar el manejo de conecciones con la Base de
 * datos del proyecto TaxSiMuLa.
 * @author Edgar Adolfo Granada Buitrago.
 * @version 1 06/06/2009
 */
public class conexionMysql {

    private Connection myConnection;

    public conexionMysql() {
    }

    public ArrayList cargarTaxis() {
        Connection con;
        ArrayList lista = new ArrayList();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/taxsimula", "root", "admin");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("call taxisSimulacion()");
            while (rs.next()) {
                Taxi t = new Taxi();
                t.setPlaca(rs.getString(1));
                t.setPropietario(rs.getString(2));
                t.setCalleActual(rs.getShort(3));
                t.setCarreraActual(rs.getShort(4));
                t.setCantidadCarrerasAtendidas(rs.getInt(5));
                int val1 = rs.getInt(6);
                if (val1 == 1) t.setEstadoVehiculo(true); else t.setEstadoVehiculo(false);
                int val2 = rs.getInt(7);
                if (val2 == 3) t.setEstadoServicio(true); else t.setEstadoServicio(false);
                lista.add(t);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insertarTaxi(String pl, String pr, int ca, int cra, int ev, int es, int ns) {
        Connection con;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://localhost/taxsimula", "root", "admin");
            if (con != null) {
                int row_update = 0;
                PreparedStatement st = con.prepareStatement("INSERT INTO taxi (idTaxi, Propietario , CalleActual ,CarreraActual,idEstado,idEstado2,NumeroCarreras) " + "VALUES  (?,?,?,?,?,?,?)");
                st.setString(1, pl);
                st.setString(2, pr);
                st.setInt(3, ca);
                st.setInt(4, cra);
                st.setInt(5, ev);
                st.setInt(6, es);
                st.setInt(7, ns);
                row_update = st.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
