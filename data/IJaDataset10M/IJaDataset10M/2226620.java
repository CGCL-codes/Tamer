package dataAccessLayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Beheerder;
import dataAccessLayer.interfaces.IBeheerderDAO;

/**
 * Concrete klasse voor het defini�ren van de database commando's
 * @author Chris, Bart, Wannes
 *
 */
public class SQLBeheerderDAO implements IBeheerderDAO {

    private static SQLBeheerderDAO instance = null;

    Connection con;

    ResultSet rs;

    Statement stmt;

    String sql;

    private SQLBeheerderDAO() {
        con = SQLDAOFactory.createConnection();
    }

    /**
	 * Methode om de private constructor van de klasse aan te roepen vermits dit een singleton is
	 * @return instance
	 */
    public static SQLBeheerderDAO getinstance() {
        if (instance != null) {
            return instance;
        }
        instance = new SQLBeheerderDAO();
        return instance;
    }

    public void deleteBeheerder(int Stamnummer) throws Exception {
        sql = "DELETE FROM dbo.Gebruiker WHERE Stamnummer = " + Stamnummer;
        stmt = con.createStatement();
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public Beheerder getBeheerder(int Stamnummer) throws Exception {
        sql = "select * from dbo.Gebruiker WHERE Stamnummer = " + Stamnummer;
        stmt = con.createStatement();
        Beheerder tempBeheerder;
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            int stamnummer = rs.getInt(1);
            String familienaam = rs.getString(2);
            String voornaam = rs.getString(3);
            String paswoord = rs.getString(4);
            tempBeheerder = new Beheerder(stamnummer, familienaam, voornaam, paswoord);
            return tempBeheerder;
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            return null;
        }
    }

    public void insertBeheerder(Beheerder beheerder) throws Exception {
        sql = "INSERT INTO dbo.Gebruiker VALUES('" + beheerder.getStamnummer() + "','" + beheerder.getFamilienaam() + "','" + beheerder.getVoornaam() + "','" + beheerder.getPaswoord() + "','Beheerder')";
        stmt = con.createStatement();
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }
}
