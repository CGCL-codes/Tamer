package buchungen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuchungDAOImpl implements BuchungDAO {

    private Connection connection;

    public BuchungDAOImpl() throws DBException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/test", "SA", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DBException("kann Datenbank-Treiber nicht laden");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("keine Verbindung zur Datenbank");
        }
    }

    public List<Buchung> load() throws DBException {
        List<Buchung> liste = new ArrayList<Buchung>();
        String sql = "SELECT id, datum, buchungstext, betrag " + " FROM buchungen";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Buchung b = new Buchung(rs.getInt("id"));
                b.setBetrag(rs.getFloat("betrag"));
                b.setDatum(rs.getDate("datum"));
                b.setText(rs.getString("buchungstext"));
                liste.add(b);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Fehler beim laden der Buchungen");
        }
        return liste;
    }

    @Override
    public List<Buchung> update(List<Buchung> liste) throws DBException {
        List<Buchung> aktualisiert = new ArrayList<Buchung>();
        String sql = "UPDATE buchungen SET " + "datum = ?, " + "buchungstext = ?, " + "betrag = ? " + "WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for (Buchung b : liste) {
                java.sql.Date sqld = new java.sql.Date(b.getDatum().getTime());
                pstmt.setDate(1, sqld);
                pstmt.setString(2, b.getText());
                pstmt.setFloat(3, b.getBetrag());
                pstmt.setInt(4, b.getId());
                if (pstmt.executeUpdate() == 1) aktualisiert.add(b);
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Fehler bei Datenbank-Update");
        }
        return aktualisiert;
    }

    @Override
    public void close() throws DBException {
        if (connection != null) try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBException("Fehler beim Schlie�en der Datenbankverbindung");
        }
    }
}
