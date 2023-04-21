package ossobook.client.base.codelists;

import java.sql.Connection;
import java.util.HashMap;
import ossobook.client.io.database.modell.KodierungsListe;

/**
 * Animal list from Server. Class gets Animal table from Main Server
 * 
 * @author ali
 */
public class BruchkantenListe extends KodierungsListe {

    public BruchkantenListe(Connection connection) {
        tabellenname = "bruchkante";
        namenColumne = "BruchkanteName";
        codeColumne = "BruchkanteCode";
        teile = new HashMap<Integer, String>();
        codes = new HashMap<String, Integer>();
        fillHashmap(getList(connection, tabellenname));
    }
}
