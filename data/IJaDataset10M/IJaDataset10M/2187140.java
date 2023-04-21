package pe.com.bn.mq.persistencia.despachador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import pe.com.bn.mq.persistencia.dao.Conexionoracle;

/**
 * @author llavado
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class DUbigeo {

    private static Logger log = Logger.getLogger(DUbigeo.class.getName());

    private String[] getDepartamento(Connection con, String dep) throws SQLException, Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String[] CodDep = new String[1];
        String s_sql = "SELECT DISTINCT  UBI.F02_CDEPARTAMENTO DEP " + "FROM BN_TABLAS.BNTGF02_UBIGEO UBI " + "WHERE TRIM(UPPER(UBI.F02_DEPARTAMENTO)) = ? ";
        try {
            pst = con.prepareStatement(s_sql);
            pst.setString(1, dep.toUpperCase());
            rs = pst.executeQuery();
            while (rs.next()) {
                CodDep[0] = rs.getString("DEP");
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        }
        return CodDep;
    }

    private String[] getProvincia(Connection con, String dep, String prov) throws SQLException, Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String[] CodProv = new String[2];
        String s_sql = "SELECT DISTINCT  UBI.F02_CDEPARTAMENTO DEP,UBI.F02_CPROVINCIA PRO " + "FROM BN_TABLAS.BNTGF02_UBIGEO UBI " + "WHERE TRIM(UPPER(UBI.F02_DEPARTAMENTO)) = ? " + "AND TRIM(UPPER(UBI.F02_PROVINCIA)) = ? ";
        try {
            pst = con.prepareStatement(s_sql);
            pst.setString(1, dep.toUpperCase());
            pst.setString(2, prov.toUpperCase());
            rs = pst.executeQuery();
            while (rs.next()) {
                CodProv[0] = rs.getString("DEP");
                CodProv[1] = rs.getString("PRO");
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        }
        return CodProv;
    }

    private String[] getDistrito(Connection con, String dep, String prov, String Dist) throws SQLException, Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String CodDist[] = new String[3];
        String s_sql = "SELECT DISTINCT  UBI.F02_CDEPARTAMENTO DEP,UBI.F02_CPROVINCIA PRO,UBI.F02_CDISTRITO DIS " + "FROM BN_TABLAS.BNTGF02_UBIGEO UBI " + "WHERE TRIM(UPPER(UBI.F02_DEPARTAMENTO)) = ? " + "AND TRIM(UPPER(UBI.F02_PROVINCIA)) = ? " + "AND TRIM(UPPER(UBI.F02_DISTRITO)) = ? ";
        try {
            pst = con.prepareStatement(s_sql);
            pst.setString(1, dep.toUpperCase());
            pst.setString(2, prov.toUpperCase());
            pst.setString(3, Dist.toUpperCase());
            rs = pst.executeQuery();
            while (rs.next()) {
                CodDist[0] = rs.getString("DEP");
                CodDist[1] = rs.getString("PRO");
                CodDist[2] = rs.getString("DIS");
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (pst != null) {
                pst.close();
                pst = null;
            }
            throw e;
        }
        return CodDist;
    }

    public String[] ExtraerUbigeo(String dep, String prov, String dist) {
        String[] codUbigeo = new String[3];
        Connection cn = null;
        ResourceBundle rb = ResourceBundle.getBundle("parametro");
        String jdni = rb.getString("JDNIOracleSACH");
        try {
            cn = (new Conexionoracle()).getConection(jdni);
            codUbigeo[0] = getDepartamento(cn, dep)[0];
            codUbigeo[1] = getProvincia(cn, dep, prov)[1];
            codUbigeo[2] = getDistrito(cn, dep, prov, dist)[2];
            if (cn != null) {
                cn.close();
                cn = null;
            }
        } catch (Exception e) {
            codUbigeo[0] = "00";
            codUbigeo[1] = "00";
            codUbigeo[2] = "00";
            log.error(e.getMessage());
            try {
                if (cn != null) {
                    cn.close();
                    cn = null;
                }
            } catch (Exception e1) {
            }
        }
        return codUbigeo;
    }
}
