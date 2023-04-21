package com.billing.dao;

import com.billing.connection.DBConnection;
import com.billing.domain.Transaksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author javamaniac < mrt.itnewbies@gmail.com >
 */
public class TransaksiDaoImpl implements TransaksiDao {

    private Connection conn = null;

    private Statement statement = null;

    private ResultSet resultSet = null;

    private DBConnection billingConnect;

    private PreparedStatement pStatement = null;

    private PreparedStatement updateMember = null;

    private PreparedStatement pSelectId = null;

    private boolean querySukses = false;

    private int transaksiID;

    public TransaksiDaoImpl(DBConnection billingConnect) {
        this.billingConnect = billingConnect;
    }

    /** Proses insert transaksi baru pasti meng-update kolom STATUS pada
     * table member menjadi LOGIN.
     * @param newTransaksi
     */
    public void insertNewTransaksi(final Transaksi newTransaksi, final boolean isNonMember) {
        conn = billingConnect.getConnection();
        try {
            conn.setAutoCommit(false);
            final String sqlMember = "UPDATE tblMember set status='LOGIN'" + "where idMember=\'" + newTransaksi.getIdMember() + "\'";
            final String sql = "insert into tblTransaksi(idOperator,idMember," + "waktuMulai,idClient, userName)" + "values(?,?,?,?,?)";
            pStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            updateMember = conn.prepareStatement(sqlMember);
            pStatement.setInt(1, newTransaksi.getIdOperator());
            pStatement.setInt(2, newTransaksi.getIdMember());
            pStatement.setString(3, newTransaksi.getWaktuMulai().toString());
            pStatement.setInt(4, newTransaksi.getIdClient());
            pStatement.setString(5, newTransaksi.getUserName());
            final int sukses = pStatement.executeUpdate();
            if (!isNonMember) {
                updateMember.executeUpdate();
            }
            conn.setAutoCommit(true);
            pStatement.close();
            updateMember.close();
            conn.close();
            querySukses = true;
        } catch (SQLException sqle) {
            querySukses = false;
        } finally {
            try {
                if (pStatement != null || updateMember != null) {
                    pStatement.close();
                    updateMember.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sql) {
                System.out.println(sql);
            }
        }
    }

    public boolean getStatusQuery() {
        return querySukses;
    }

    public void updateTransaksi(final Transaksi uptTransaksi, final boolean isNonMember) {
        conn = billingConnect.getConnection();
        final String sql = "UPDATE tblTransaksi SET idOperator=\'" + uptTransaksi.getIdOperator() + "\', idMember =\'" + uptTransaksi.getIdMember() + "\', tglTransaksi=\'" + uptTransaksi.getTglTransaksi() + "\', waktuMulai=\'" + uptTransaksi.getWaktuMulai() + "\', waktuBerhenti=\'" + uptTransaksi.getWaktuBerhenti() + "\', durasi=\'" + uptTransaksi.getDurasi() + "\', harga=\'" + uptTransaksi.getHarga() + "\', idClient=\'" + uptTransaksi.getIdClient() + "\', userName=\'" + uptTransaksi.getUserName() + "\'" + "WHERE userName=\'" + uptTransaksi.getUserName() + "\' AND" + " idClient=\'" + uptTransaksi.getIdClient() + "\' AND" + " waktuMulai=\'" + uptTransaksi.getWaktuMulai() + "\' AND" + " tglTransaksi IS NULL";
        System.out.println("\nsql qry " + sql);
        final String sqlMember = "UPDATE tblMember set status='NOTLOGIN'" + "where idMember=\'" + uptTransaksi.getIdMember() + "\'";
        final String selectID = "SELECT * FROM tblTransaksi WHERE userName= ? " + "AND idClient= ? AND waktuMulai=? AND tglTransaksi=?";
        try {
            conn.setAutoCommit(false);
            pStatement = conn.prepareStatement(sql);
            updateMember = conn.prepareStatement(sqlMember);
            pSelectId = conn.prepareStatement(selectID);
            pStatement.executeUpdate();
            if (!isNonMember) {
                updateMember.executeUpdate();
            }
            pSelectId.setString(1, uptTransaksi.getUserName());
            pSelectId.setInt(2, uptTransaksi.getIdClient());
            pSelectId.setString(3, uptTransaksi.getWaktuMulai().toString());
            pSelectId.setDate(4, uptTransaksi.getTglTransaksi());
            ResultSet rs = pSelectId.executeQuery();
            if (rs.next()) {
                transaksiID = rs.getInt(1);
            }
            conn.commit();
            conn.setAutoCommit(true);
            pStatement.close();
            updateMember.close();
            pSelectId.close();
            conn.close();
            querySukses = true;
        } catch (SQLException sqle) {
            System.out.println("Exception Caught : " + sqle);
            querySukses = false;
        } finally {
            try {
                if (pStatement != null || updateMember != null || pSelectId != null) {
                    pStatement.close();
                    updateMember.close();
                    pSelectId.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println(sqle);
            }
        }
    }

    public void deleteTransaksi(final Integer idTransaksi) {
        conn = billingConnect.getConnection();
        try {
            conn.setAutoCommit(false);
            final String sql = "DELETE FROM tblTransaksi " + "where idTransaksi=\'" + idTransaksi + "\'";
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.commit();
            conn.setAutoCommit(true);
            statement.close();
            conn.close();
            querySukses = true;
        } catch (SQLException sqle) {
            System.out.println("Exception Caught : " + sqle);
            querySukses = false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sql) {
                querySukses = false;
                System.out.println(sql);
            }
        }
    }

    public ArrayList<Object> selectAllTransaksi() {
        final ArrayList<Object> recordTransaksi = new ArrayList<Object>();
        final String query = "SELECT * FROM tblTransaksi";
        conn = billingConnect.getConnection();
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                recordTransaksi.add(resultSet.getString("idTransaksi") + "%%" + resultSet.getString("namaTransaksi") + "%%" + resultSet.getString("ipTransaksi"));
            }
            statement.close();
            resultSet.close();
            conn.close();
            querySukses = true;
        } catch (SQLException sqle) {
            querySukses = false;
            System.out.println("Exception : " + sqle.toString());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sql) {
                System.out.println("Exception : " + sql.toString());
            }
        }
        return recordTransaksi;
    }

    public int getTransaksiID() {
        return transaksiID;
    }
}
