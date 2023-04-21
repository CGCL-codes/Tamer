package zerbitzaria.kudeatzaileak;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Ordutegiarekin erlazionaturik dauden datu-basearen aurkako eskaerak
 * gauzatzeko klasea.
 * 
 * @author 5. TALDEA
 * 
 */
public class OrdutegiKud {

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
    public OrdutegiKud(Connection kon) throws SQLException {
        this.konexioa = kon;
        this.agindua = (Statement) konexioa.createStatement();
    }

    /**
	 * Profil baten baimenentzako ordutegi berri bat sortzen du
	 * 
	 * @param jaiEguna Ordutegi hori jai egun batekoa bada true izan behar da, bestela false
	 * @param hasOrdua Ordutegiaren hasiera ordua
	 * @param bukOrdua Ordutegiaren bukaera ordua
	 * @param profId Baimen hau esleitu nahi zaion profilaren id-a
	 * @param guneId Ordutegi honetan zehar zeharkatu ahal izango den gunearen id-a
	 * @throws SQLException
	 */
    public void createOrdutegia(boolean jaiEguna, String hasOrdua, String bukOrdua, int profId, int guneId) throws SQLException {
        String query = "INSERT INTO ordutegia (jaiEguna,hasieraOrdua,bukaeraOrdua,proId,gunId) VALUES (" + String.valueOf(jaiEguna) + ",'" + hasOrdua + "','" + bukOrdua + "'," + profId + ", " + guneId + ")";
        agindua.executeUpdate(query);
    }
}
