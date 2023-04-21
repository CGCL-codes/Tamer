package pe.com.bn.sach.dao.datacom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pe.com.bn.sach.domain.MantenimientoHost;

public class Bnch06SQL {

    Connection cnx = null;

    public Bnch06SQL(Connection cnx) {
        this.cnx = cnx;
    }

    protected final Log logger = LogFactory.getLog(getClass());

    public List ListarXCHBNCHF06(MantenimientoHost bnchf02) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List result = new ArrayList();
        try {
            String sql = "" + "  select" + "  tarifa.F06_COD_CENTRO_HIP," + "  tarifa.F06_COD_TARIFA ," + "  tarifa.F06_TIMESTAMP," + "  tarifa.F06_DES_TARIFA, " + "  tarifa.F06_COD_ESTADO," + "  tarifa.F06_SEG_INMBL," + "  tarifa.F06_PORTES ," + "  tarifa.F06_COMISION_EVAL," + "  tarifa.F06_COSTO_CHQ_GER," + "  tarifa.F06_ITF," + "  tarifa.F06_COD_USUARIO ," + "  ch.F50_DES_LARGA as F15_NOM_CENT_HIPOTCR " + "  from " + "  BNCHF06 tarifa , BNCHF50 ch" + "  where" + "  (ch.F50_COD_ELEMENTO = tarifa.F06_COD_CENTRO_HIP and  F50_COD_TABLA='F15' ) and	(tarifa.F06_COD_TARIFA=?  or  ?='0') ";
            String stq1 = " and  tarifa.F06_COD_CENTRO_HIP='" + bnchf02.getF06CodCentroHip() + "'	";
            String stq2 = " and  tarifa.F06_DES_TARIFA like '" + bnchf02.getF06DesTarifa() + "%' 	";
            if (bnchf02.getF06CodCentroHip() == null) bnchf02.setF06CodCentroHip("");
            if (bnchf02.getF06DesTarifa() == null) bnchf02.setF06DesTarifa("");
            if (!bnchf02.getF06CodCentroHip().equals("")) sql = sql + stq1;
            if (!bnchf02.getF06DesTarifa().equals("")) sql = sql + stq2;
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchf02.getF06CodTarifa());
            stmt.setString(2, bnchf02.getF06CodTarifa());
            rs = stmt.executeQuery();
            while (rs.next()) {
                MantenimientoHost item = new MantenimientoHost();
                item.setF06CodCentroHip((String) rs.getString(1));
                item.setF06CodTarifa((String) rs.getString(2));
                item.setF06TimestampStr((String) rs.getString(3));
                item.setF06DesTarifa((String) rs.getString(4));
                item.setF06CodEstado((String) rs.getString(5));
                item.setF06SegInmbl(new Double(rs.getString(6)));
                item.setF06Portes(new Double(rs.getString(7)));
                item.setF06MtoComiEval(new Double("" + rs.getString(8)));
                item.setF06CostoChqGer(new Double("" + rs.getString(9)));
                item.setF06Itf(new Double("" + rs.getString(10)));
                item.setF06CodUsuario(("" + rs.getString(11)));
                item.setF06CentHipotecario(("" + (String) rs.getString(12)));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ;
            if (rs != null) {
                rs.close();
                rs = null;
            }
            ;
        }
        return (result.size() == 0) ? null : result;
    }

    public List ListarXBNCHF06CONSULTA(MantenimientoHost bnchf02) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List result = new ArrayList();
        try {
            String sql = "" + "  select" + "  tarifa.F06_COD_CENTRO_HIP," + "  tarifa.F06_COD_TARIFA ," + "  tarifa.F06_TIMESTAMP," + "  tarifa.F06_DES_TARIFA, " + "  tarifa.F06_COD_ESTADO," + "  tarifa.F06_SEG_INMBL," + "  tarifa.F06_PORTES ," + "  tarifa.F06_COMISION_EVAL," + "  tarifa.F06_COSTO_CHQ_GER," + "  tarifa.F06_ITF," + "  tarifa.F06_COD_USUARIO ," + "  ch.F50_DES_LARGA as F15_NOM_CENT_HIPOTCR " + "  from " + "  BNCHF06 tarifa , BNCHF50 ch" + "  where " + "  ((ch.F50_COD_ELEMENTO  = tarifa.F06_COD_CENTRO_HIP)  and   (F50_COD_TABLA='F15')  )" + "  and	tarifa.F06_COD_TARIFA=? and tarifa.F06_COD_CENTRO_HIP=? ";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchf02.getF06CodTarifa());
            stmt.setString(2, bnchf02.getF06CodCentroHip());
            rs = stmt.executeQuery();
            while (rs.next()) {
                MantenimientoHost item = new MantenimientoHost();
                item.setF06CodCentroHip(rs.getString(1));
                item.setF06CodTarifa(rs.getString(2));
                item.setF06TimestampStr(rs.getString(3));
                item.setF06DesTarifa("" + rs.getString(4));
                item.setF06CodEstado(rs.getString(5));
                item.setF06SegInmbl(new Double(rs.getString(6)));
                item.setF06Portes(new Double(rs.getString(7)));
                item.setF06MtoComiEval(new Double("" + rs.getString(8)));
                item.setF06CostoChqGer(new Double("" + rs.getString(9)));
                item.setF06Itf(new Double("" + rs.getString(10)));
                item.setF06CodUsuario(("" + rs.getString(11)));
                item.setF06CentHipotecario(("" + rs.getString(12)));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ;
            if (rs != null) {
                rs.close();
                rs = null;
            }
            ;
        }
        return (result.size() == 0) ? null : result;
    }

    public List EncontrarBNCHF06(MantenimientoHost bnchf02) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List result = new ArrayList();
        try {
            String sql = "" + "  select" + "  tarifa.F06_COD_CENTRO_HIP," + "  tarifa.F06_COD_TARIFA ," + "  tarifa.F06_TIMESTAMP," + "  tarifa.F06_DES_TARIFA, " + "  tarifa.F06_COD_ESTADO," + "  tarifa.F06_SEG_INMBL," + "  tarifa.F06_PORTES ," + "  tarifa.F06_COMISION_EVAL," + "  tarifa.F06_COSTO_CHQ_GER," + "  tarifa.F06_ITF," + "  tarifa.F06_COD_USUARIO ," + "  ch.F50_DES_LARGA as F15_NOM_CENT_HIPOTCR " + "  from " + "  BNCHF06 tarifa , BNCHF50 ch" + "  where" + "  ch.F15_COD_CENT_HIPOTCR = tarifa.F06_COD_CENTRO_HIP" + "  AND tarifa.F06_COD_TARIFA=? " + "  and tarifa.F06_COD_CENTRO_HIP=?  ";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchf02.getF06CodTarifa());
            stmt.setString(2, bnchf02.getF06CodCentroHip());
            rs = stmt.executeQuery();
            while (rs.next()) {
                MantenimientoHost item = new MantenimientoHost();
                item.setF06CodCentroHip(rs.getString(1));
                item.setF06CodTarifa(rs.getString(2));
                item.setF06TimestampStr(rs.getString(3));
                item.setF06DesTarifa("" + rs.getString(4));
                item.setF06CodEstado(rs.getString(5));
                item.setF06SegInmbl(new Double(rs.getString(6)));
                item.setF06Portes(new Double(rs.getString(7)));
                item.setF06MtoComiEval(new Double("" + rs.getString(8)));
                item.setF06CostoChqGer(new Double("" + rs.getString(9)));
                item.setF06Itf(new Double("" + rs.getString(10)));
                item.setF06CodUsuario(("" + rs.getString(11)));
                item.setF06CentHipotecario(("" + rs.getString(12)));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ;
            if (rs != null) {
                rs.close();
                rs = null;
            }
            ;
        }
        return (result.size() == 0) ? null : result;
    }

    public void guardar(MantenimientoHost mnt) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "" + "insert into BNCHF06" + "(" + " F06_COD_CENTRO_HIP," + " F06_COD_TARIFA," + " F06_TIMESTAMP," + " F06_DES_TARIFA," + " F06_COD_ESTADO," + " F06_SEG_INMBL," + " F06_PORTES," + " F06_COMISION_EVAL," + " F06_COSTO_CHQ_GER," + " F06_ITF," + " F06_COD_USUARIO," + " F06_INTERES_M" + " )" + " values (" + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ?," + " ? " + ")";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, mnt.getF06CodCentroHip());
            stmt.setString(2, (mnt.getF06CodTarifa() + ""));
            stmt.setString(3, mnt.getF06Timestamp() + "");
            stmt.setString(4, mnt.getF06DesTarifa() + "");
            stmt.setString(5, (String) "0");
            stmt.setDouble(6, Double.parseDouble(mnt.getF06SegInmbl() + ""));
            stmt.setDouble(7, Double.parseDouble(mnt.getF06Portes() + ""));
            stmt.setDouble(8, Double.parseDouble(mnt.getF06MtoComiEval() + ""));
            stmt.setDouble(9, Double.parseDouble(mnt.getF06CostoChqGer() + ""));
            stmt.setDouble(10, Double.parseDouble(mnt.getF06Itf() + ""));
            stmt.setString(11, mnt.getF06CodUsuario());
            stmt.setDouble(12, Double.parseDouble(mnt.getF06InteresesM() + ""));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ;
        }
    }

    public String nextBNCHF06(MantenimientoHost mnt) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String total = "";
        try {
            String sql = "SELECT MAX(F06_COD_TARIFA)  from  BNCHF06 ";
            stmt = cnx.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                total = (String) rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ;
            if (rs != null) {
                rs.close();
                rs = null;
            }
            ;
        }
        return total;
    }
}
