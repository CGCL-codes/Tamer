package ossobook.client.base.codelists;

import java.sql.Connection;
import java.util.HashMap;
import ossobook.client.io.database.modell.KodierungsListe;

/**
 * SkelCodeList from MainServer.
 * 
 * @author ali
 */
public class SkelettteilListe extends KodierungsListe {

    public SkelettteilListe(Connection connection) {
        tabellenname = "skelteil";
        namenColumne = "SkelName";
        codeColumne = "SkelCode";
        teile = new HashMap<Integer, String>();
        codes = new HashMap<String, Integer>();
        fillHashmap(getList(connection, tabellenname));
    }
}
