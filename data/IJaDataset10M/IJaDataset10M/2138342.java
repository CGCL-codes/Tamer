package zerbitzaria.kudeatzaileak;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Ateekin erlazionaturik dauden datu-basearen aurkako eskaerak
 * gauzatzeko klasea.
 * 
 * @author 5. TALDEA
 * 
 */
public class AteKud {

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
    public AteKud(Connection kon) throws SQLException {
        this.konexioa = kon;
        this.agindua = (Statement) konexioa.createStatement();
    }

    /**
	 * Ate guztien egoera irekia bezala jartzen du.
	 * 
	 * @throws SQLException
	 */
    public void IrekiAteak() throws SQLException {
        String query = "UPDATE ateak SET egoera = 'irekita'";
        this.agindua.executeUpdate(query);
    }

    /**
	 * Ate guztien egoera itxita bezala jaztzen du.
	 * 
	 * @throws SQLException
	 */
    public void ItxiAteak() throws SQLException {
        String query = "UPDATE ateak SET egoera = 'itxita'";
        this.agindua.executeUpdate(query);
    }
}
