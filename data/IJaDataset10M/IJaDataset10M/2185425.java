package mvc.model;

import com.gesturn2.comercio.Punto_de_Vigilancia;
import com.gesturn2.comercio.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MAN
 */
public class Necesidad_vigi_Dao extends Model implements DaoGenerico {

    public boolean existe(Object o) throws SQLException {
        Integer id = (Integer) o;
        boolean sw = true;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = getCon().prepareStatement("SELECT COUNT(*) FROM necesidad_vigilancia WHERE id_necesidad =?");
            pstmt.setInt(1, id.intValue());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString(1).equals("0")) {
                    sw = false;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        System.out.print("Valor sw:" + sw);
        return sw;
    }

    public Object load(ResultSet rs) throws SQLException {
        NesesidadVigilancia u = new NesesidadVigilancia();
        u.setId_necesidad(rs.getString(1));
        u.setDuracion_servicio(rs.getInt(2));
        Object o = extrae(rs.getString(3));
        Punto_de_Vigilancia pv = (Punto_de_Vigilancia) o;
        u.setPtv(pv);
        u.setFecha_ini(rs.getString(4));
        u.setFecha_fin(rs.getString(5));
        u.setHora_inicial(rs.getString(6));
        u.setHora_final(rs.getString(7));
        Object ob = extraeIrregularidad(Integer.valueOf(rs.getInt(8)));
        Irregularidad irreg = (Irregularidad) ob;
        u.setIrregularidad(irreg);
        return u;
    }

    public Object extrae(Object id) throws SQLException {
        String id1 = (String) id;
        Connection conex = getCon();
        if (!isConnect()) throw new SQLException("no hay conexcion");
        Servicio u = new Servicio();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conex.prepareStatement("SELECT * FROM necesidad_vigilancia WHERE id_necesidad=?");
            pstmt.setString(1, id1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                u = (Servicio) load(rs);
            } else {
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return u;
    }

    public void crear(Object o) throws SQLException {
        NesesidadVigilancia u = (NesesidadVigilancia) o;
        Connection conex = getCon();
        Integer id = new Integer(u.getId_necesidad());
        if (existe(id)) throw new SQLException("Ya existe esta nesecidad de vigilancia");
        PreparedStatement pstmt = null;
        try {
            pstmt = conex.prepareStatement("INSERT INTO necesidad_vigilancia VALUES (?,?,?,?,?,?,?,?)");
            pstmt.setString(1, u.getId_necesidad());
            pstmt.setInt(2, u.getDuracion_servicio());
            pstmt.setString(3, u.getPtv().getId());
            pstmt.setString(4, u.getFecha_ini());
            pstmt.setString(5, u.getFecha_fin());
            pstmt.setString(6, u.getHora_inicial());
            pstmt.setString(7, u.getHora_final());
            pstmt.setInt(8, u.getIrregularidad().getCodigo());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }

    public List listar() throws SQLException {
        Connection conex = getCon();
        if (!isConnect()) throw new SQLException("no hay conexcion");
        PreparedStatement ps = null;
        ResultSet rs = null;
        NesesidadVigilancia u = null;
        List lista = new LinkedList();
        try {
            ps = (PreparedStatement) conex.prepareStatement("select * from nesecidad_vigilancia ");
            rs = ps.executeQuery();
            while (rs.next()) {
                u = (NesesidadVigilancia) load(rs);
                lista.add(u);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return lista;
    }

    public Object loadIrregularidad(ResultSet rs) throws SQLException {
        Irregularidad u = new Irregularidad();
        u.setCodigo(rs.getInt(1));
        u.setNombre(rs.getString(2));
        return u;
    }

    public List listarIrregularidad() throws SQLException {
        Connection conex = getCon();
        if (!isConnect()) throw new SQLException("no hay conexcion");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Irregularidad u = null;
        List lista = new LinkedList();
        try {
            ps = (PreparedStatement) conex.prepareStatement("select * from irregularidades ");
            rs = ps.executeQuery();
            while (rs.next()) {
                u = (Irregularidad) loadIrregularidad(rs);
                lista.add(u);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return lista;
    }

    public Object extraeIrregularidad(Object id) throws SQLException {
        String id1 = (String) id;
        Connection conex = getCon();
        if (!isConnect()) throw new SQLException("no hay conexcion");
        Irregularidad u = new Irregularidad();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conex.prepareStatement("SELECT * FROM irregularidades WHERE id_irregularidad=?");
            pstmt.setString(1, id1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                u = (Irregularidad) loadIrregularidad(rs);
            } else {
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return u;
    }

    public Object extrae2(Object id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List listar(Object o) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Update(Object o) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
