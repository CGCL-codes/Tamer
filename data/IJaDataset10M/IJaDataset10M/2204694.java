package zerbitzaria.kudeatzaileak;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Fakultatearekin erlazionaturik dauden datu-basearen aurkako eskaerak
 * gauzatzeko klasea.
 * 
 * @author 5. TALDEA
 * 
 */
public class FakulKud {

    private Connection konexioa;

    private Statement agindua;

    /**
	 * Programaren exekuzioan zehar erabiliko den datu-basearen aurkako konexioa
	 * esleituko du. Konexioa {@link Connection} klaseko objektu bat da.
	 * 
	 * @param kon
	 *            Datu-basearen aurkako konexioaren parametroak gordetzen dituen
	 *            klasea da. Negozio logika egikaritzean sortuko da eta honek
	 *            egikarituriko klase guztiek erabiliko dute.
	 * @throws SQLException 
	 */
    public FakulKud(Connection kon) throws SQLException {
        this.konexioa = kon;
        this.agindua = (Statement) konexioa.createStatement();
    }

    /**
	 * Eraikinean dauden pertsonen kopurua itziltzen du. 
	 * @return int batean eraikinean dauden pertsonen kopurua.
	 * @throws SQLException 
	 * @throws SQLException
	 */
    public int getEraikinekoPertsonKop() throws SQLException {
        int kop = 0;
        String query = "SELECT Count(erId) AS kopuru FROM fakultatea";
        ResultSet rs;
        rs = this.agindua.executeQuery(query);
        if (rs.next()) kop = rs.getInt("kopuru");
        return kop;
    }

    /**
	 * Fakultatea taula ustu egiten du
	 * 
	 * @throws SQLException
	 */
    public void pertsonakAteraEraikinetik() throws SQLException {
        String query = "DELETE FROM fakultatea";
        this.agindua.executeUpdate(query);
    }

    /**
	 * Emandako erabiltzailea fakultatea taulan dagoen edo ez esaten du
	 * 
	 * @param erabId Fakultatean dagoen jakin nahi dugun erabiltzalearen identifikazioa
	 * @return Erabiltzailea fakultatean badago true itzuliko du, bestela false
	 * @throws SQLException
	 */
    public boolean erabiltzaileaFakultatean(String erabId) throws SQLException {
        String query1 = "SELECT * FROM fakultatea WHERE erId= " + erabId;
        ResultSet rs;
        rs = this.agindua.executeQuery(query1);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    /**
	 * Emandako erabiltzailearen gunea emandakoagatik aldatzen du.
	 * 
	 * @param erabId Gunez aldatu nahi dugun erabiltzailearen id-a
	 * @param guneId Erabiltzilea aldatu nahi dugun gunearen id-a
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
    public void kokapenaEguneratu(String erabId, String guneId) throws IllegalStateException, SQLException {
        String query2 = "UPDATE fakultatea SET guId= " + guneId + " WHERE erId= " + erabId;
        this.agindua.executeUpdate(query2);
    }

    /**
	 * Emandako erabiltzailea emandako fakultateko gunean sartzen du
	 * 
	 * @param erabId Fakultatean sartu nahi dugun erabiltzailearen id-a
	 * @param guneId Erabiltzailea sartu nahi dugun gunearen id-a
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
    public void kokapenaSartu(String erabId, String guneId) throws IllegalStateException, SQLException {
        String query3 = "INSERT INTO fakultatea (erId, guId, inId) VALUES( " + erabId + " , " + guneId + " ,null )";
        this.agindua.executeUpdate(query3);
    }
}
