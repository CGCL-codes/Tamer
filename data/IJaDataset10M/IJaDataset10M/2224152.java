package fr.crim.lexique.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import fr.crim.lexique.common.Candidate;
import fr.crim.lexique.common.LexiqueOrgReader;

/**
 * Une classe qui sert un lexique stocké dans une base de données SQLite
 * et qui "cache" les résultats en mémoire
 * @author Pierre
 */
public class LexiqueOrgSQLiteReader implements LexiqueOrgReader {

    private Connection conn;

    private PreparedStatement stmt;

    private Hashtable<String, List<Candidate>> cache = new Hashtable<String, List<Candidate>>();

    public LexiqueOrgSQLiteReader(File dbFile) throws Exception {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        stmt = conn.prepareStatement("SELECT lemme, type FROM lexique WHERE forme=? ORDER BY freq DESC");
    }

    /**
	 * Recherche une liste de lemmes candidats avec leur type à partir d'une
	 * forme passée en paramètre 
	 * @param form La forme à chercher
	 * @return La liste des lemmes
	 */
    public List<Candidate> lookupForm(String form) {
        if (cache.contains(form)) {
            return cache.get(form);
        }
        List<Candidate> results = new ArrayList<Candidate>();
        try {
            stmt.setString(1, form);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Candidate(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        cache.put(form, results);
        return results;
    }

    /**
	 * Clôt la connexion
	 */
    public void close() {
        try {
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
