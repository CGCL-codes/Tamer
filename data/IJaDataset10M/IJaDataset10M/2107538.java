package pe.com.bn.sach.dao.datacom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pe.com.bn.sach.common.Util;
import pe.com.bn.sach.domain.BnchfTarProd;

/**
 * @author ce_dpcreditos08
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class Bnchf08TarifaProductoSQL {

    Connection cnx = null;

    public Bnchf08TarifaProductoSQL(Connection cnx) {
        this.cnx = cnx;
    }

    public BnchfTarProd select(BnchfTarProd bnchfTarProd) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BnchfTarProd item = null;
        try {
            String sql = "SELECT" + "			F08_COD_CENTRO_HIP," + "			F08_COD_PROD," + "			F08_NRO_SECUENCIA," + "			F08_COD_TARIFA," + "			F08_TIMESTAMP," + "			F08_COD_USUARIO" + "		FROM" + "			BNCHF08" + "		WHERE" + "			F08_COD_CENTRO_HIP=? AND" + "			F08_COD_PROD=? AND" + "			F08_NRO_SECUENCIA=? AND" + "			F08_COD_TARIFA=?";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchfTarProd.getF08CodCentroHip());
            stmt.setString(2, bnchfTarProd.getF08CodProd());
            stmt.setLong(3, Util.parseLong(bnchfTarProd.getF08NroSecuencia()).longValue());
            stmt.setString(4, bnchfTarProd.getF08CodTarifa());
            rs = stmt.executeQuery();
            if (rs.next()) {
                item = new BnchfTarProd();
                item.setF08CodCentroHip(rs.getString(1));
                item.setF08CodProd(rs.getString(2));
                item.setF08NroSecuencia(new Long(rs.getLong(3)));
                item.setF08CodTarifa(rs.getString(4));
                item.setF08Timestamp(rs.getString(5));
                item.setF08CodUsuario(rs.getString(6));
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
        return item;
    }

    public void insertar(BnchfTarProd bnchfTarProd) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into BNCHF08" + "		(	F08_COD_CENTRO_HIP," + "			F08_COD_PROD," + "			F08_NRO_SECUENCIA," + "			F08_COD_TARIFA," + "			F08_TIMESTAMP," + "			F08_COD_USUARIO" + "		)values (" + "			?," + "			?," + "			?," + "			?," + "			?," + "			?" + "		)";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchfTarProd.getF08CodCentroHip());
            stmt.setString(2, bnchfTarProd.getF08CodProd());
            stmt.setLong(3, Util.parseLong(bnchfTarProd.getF08NroSecuencia()).longValue());
            stmt.setString(4, bnchfTarProd.getF08CodTarifa());
            stmt.setString(5, bnchfTarProd.getF08Timestamp());
            stmt.setString(6, bnchfTarProd.getF08CodUsuario());
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

    public void update(BnchfTarProd bnchfTarProd) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "update BNCHF08" + "		set F08_TIMESTAMP=?," + "			F08_COD_USUARIO=?" + "		where" + "			F08_COD_CENTRO_HIP=? 	AND" + "			F08_COD_PROD=? 			AND" + "			F08_NRO_SECUENCIA=? 	AND" + "			F08_COD_TARIFA=?";
            stmt = cnx.prepareStatement(sql);
            stmt.setString(1, bnchfTarProd.getF08Timestamp());
            stmt.setString(2, bnchfTarProd.getF08CodUsuario());
            stmt.setString(3, bnchfTarProd.getF08CodCentroHip());
            stmt.setString(4, bnchfTarProd.getF08CodProd());
            stmt.setLong(5, Util.parseLong(bnchfTarProd.getF08NroSecuencia()).longValue());
            stmt.setString(6, bnchfTarProd.getF08CodTarifa());
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
}
