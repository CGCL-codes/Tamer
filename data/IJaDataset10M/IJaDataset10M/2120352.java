package zerbitzaria.kudeatzaileak;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import com.mysql.jdbc.Statement;

/**
 * Txartelekin erlazionaturik dauden datu-basearen aurkako eskaerak gauzatzeko
 * klasea.
 * 
 * @author 5. TALDEA
 * 
 */
public class TxartelKud {

    private Statement agindua;

    private Connection konexioa;

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
    public TxartelKud(Connection kon) throws SQLException {
        konexioa = kon;
        agindua = (Statement) konexioa.createStatement();
    }

    /**
	 * Erabiltzaile baten identifikazioa emanda, orain dela 2 urte baino denbora
	 * tarte gutxiagoan gaituak izan direnak eta gaur egun gaituta jarraitzen
	 * dutenak itzultzen ditu.
	 * 
	 * @param erabId
	 *            erabiltzaile baten identifikazioa daukan zenbaki oso bat
	 * @return txartelaren identifikazioak
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
    public int getTxartel2Urte(int erabId) throws SQLException {
        Integer biUrte = null;
        String c2 = "SELECT id " + "FROM txartelak " + "WHERE erabId= " + erabId + " AND gaituData<=DATE_SUB(NOW(), " + "INTERVAL 2 YEAR) AND desgaituData IS NULL";
        ResultSet q = agindua.executeQuery(c2);
        if (q.next()) biUrte = (Integer) q.getInt("id"); else biUrte = -1;
        return biUrte.intValue();
    }

    /**
	 * Erabiltzaile baten uneko kokapena itzultzen du
	 * 
	 * @param txId
	 *            Txartel identifikadore bat, zenbakizkoa.
	 * @return Erabiltzailea dagoen kokapenari buruzko informazioa itzultzen du,
	 *         Objektuz osatutako bektorezko bektore batean, non Azpibektore
	 *         bakoitzak datu-lerro bat adierazten duen.
	 * @throws SQLException
	 */
    public Vector<Vector<Object>> getErabiltzaileKokapena(String txId) throws SQLException {
        String c3 = "SELECT e.izena,e.id, t.id, g.id, g.izena, s.data " + "FROM (((guneak g INNER JOIN txartelirakurgailuak ti ON g.id=ti.guneId) " + "INNER JOIN sarbideeskaerak s ON ti.id=s.txIrakurId) " + "INNER JOIN txartelak t ON s.txId=t.id) " + "INNER JOIN erabiltzaileak e ON t.erabId=e.id " + "WHERE s.txId=" + txId + " AND s.data >= ALL (SELECT data " + "FROM sarbideeskaerak " + "WHERE txId=" + txId + ")";
        ResultSet q = this.agindua.executeQuery(c3);
        Vector<Vector<Object>> erabKokapena = new Vector<Vector<Object>>();
        while (q.next()) {
            Vector<Object> row = new Vector<Object>();
            for (int i = 1; i <= q.getMetaData().getColumnCount(); i++) row.addElement(q.getObject(i));
            erabKokapena.addElement(row);
        }
        return erabKokapena;
    }

    /**
	 * Txartel identifikadore bati dagokion erabiltzailearen izena lortzeko
	 * 
	 * @param txId
	 *            Txartel identifikadore bat, zenbakizkoa.
	 * @return Erabiltzailearen izena adierazten duen String bat itzultzen du,
	 *         txartel identifikadorea aurkitu bada, bestela null itzuliko du.
	 * @throws SQLException
	 */
    public String getErabIzena(int txId) throws SQLException {
        String query = "SELECT E.izena " + "FROM erabiltzaileak AS E INNER JOIN txartelak AS T ON E.id = T.erabId " + "WHERE T.id = " + txId;
        ResultSet r = agindua.executeQuery(query);
        if (r.next()) return r.getString("izena"); else return null;
    }

    /**
	 * Emandako erabiltzaileari txartel bat sortzen dio
	 * 
	 * @param nan Txartela sortu nahi diogun erabiltzailearen identifikazioa
	 * @throws SQLException
	 */
    public void createTxartela(int nan) throws SQLException {
        String txart = "INSERT INTO txartelak (gaituData,desgaituData,erabId) " + "VALUES (NOW(),null," + nan + ")";
        this.agindua.executeUpdate(txart);
    }

    /**
	 * Emandako erabiltzailearen txartela desgaitzen du
	 * 
	 * @param nan Txartela desgaitu nahi diogun erabiltzailearen identifikazioa
	 * @return Desgaitu dituen txartel kopurua
	 * @throws SQLException
	 */
    public int desgaituTxartela(int nan) throws SQLException {
        String txart = "UPDATE txartelak SET desgaituData=NOW()WHERE erabId='" + nan + "'";
        return this.agindua.executeUpdate(txart);
    }

    /**
	 * Emandako erabiltzailearen txartela gaitzen du
	 * 
	 * @param nan Txartela gaitu nahi diogun erabiltzailearen identifikazioa
	 * @return Gaitu dituen txartel kopurua
	 * @throws SQLException
	 */
    public int gaituTxartela(int nan) throws SQLException {
        String txart = "UPDATE txartelak SET gaituData=NOW(), desgaituData=null WHERE erabId='" + nan + "'";
        return this.agindua.executeUpdate(txart);
    }
}
