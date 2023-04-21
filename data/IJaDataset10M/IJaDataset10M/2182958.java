package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Diese Klasse dient zum Zugriff auf die einzelnen
 * Tags aus der Datenbank zur Kategorisierung
 * der Straftaten
 * 
 * @see db.TagsDTO
 * @author Thomas Leimbach
 */
public class TagsDB {

    /**
	   * Query, um alle Tags aus der Datenbank zu erhalten
	   */
    private static String query_all = "SELECT * FROM tags";

    /**
	   * Log-Element
	   */
    private static Log log = LogFactory.getLog(TagsDB.class);

    /**
	   * Methode, die dazu dient, alle Tags aus der
	   * Datenbank zu erhalten
	   * 
	   * @param cn
	   * @return Collection
	   */
    public static Collection findAll(Connection cn) {
        LinkedList erg = new LinkedList();
        try {
            PreparedStatement stmt = cn.prepareStatement(query_all);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) {
                TagsDTO dto = new TagsDTO(rset);
                erg.add(dto);
            }
            rset.close();
        } catch (SQLException e) {
            if (log.isErrorEnabled()) log.error("Hier ist ein Fehler in der Datenbank aufgetreten: " + e);
        }
        return erg;
    }
}
