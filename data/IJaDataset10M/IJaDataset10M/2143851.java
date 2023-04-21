package ligueBaseball;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Joueur {

    private PreparedStatement stmtExiste;

    private PreparedStatement stmtMaxId;

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtId;

    private Connexion cx;

    Joueur(Connexion cx) throws SQLException {
        this.cx = cx;
        stmtExiste = cx.getConnection().prepareStatement("SELECT * FROM joueur WHERE joueurnom = ? AND joueurprenom = ?");
        stmtInsert = cx.getConnection().prepareStatement("INSERT INTO joueur (joueurid, joueurnom, joueurprenom)" + "values (?,?,?)");
        stmtMaxId = cx.getConnection().prepareStatement("SELECT max(joueurid) FROM joueur;");
    }

    public int supprimer(String nom, String prenom) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int creer(String nom, String prenom) throws SQLException {
        int id = getMaxId();
        stmtInsert.setInt(1, id);
        stmtInsert.setString(2, nom);
        stmtInsert.setString(3, prenom);
        stmtInsert.executeUpdate();
        return id;
    }

    public boolean existe(String nom, String prenom) throws SQLException {
        stmtExiste.setString(1, nom);
        stmtExiste.setString(2, prenom);
        ResultSet rset = stmtExiste.executeQuery();
        boolean membreExiste = rset.next();
        rset.close();
        return membreExiste;
    }

    public Connexion getCx() {
        return cx;
    }

    public void setCx(Connexion cx) {
        this.cx = cx;
    }

    public int getJoueurId(String joueurnom, String joueurprenom) throws SQLException {
        stmtExiste.setString(1, joueurnom);
        stmtExiste.setString(2, joueurprenom);
        ResultSet rset = stmtExiste.executeQuery();
        rset.next();
        int id = rset.getInt(1);
        rset.close();
        return id;
    }

    public TupleJoueur getJoueur(int joueurId) throws SQLException {
        stmtExiste.setInt(1, joueurId);
        ResultSet rset = stmtExiste.executeQuery();
        TupleJoueur tupleJoueur = new TupleJoueur();
        if (rset.next()) {
            tupleJoueur.setId(joueurId);
            tupleJoueur.setNom(rset.getString(2));
            tupleJoueur.setPrenom(rset.getString(3));
            return tupleJoueur;
        }
        rset.close();
        return null;
    }

    private int getMaxId() throws SQLException {
        ResultSet rset = stmtMaxId.executeQuery();
        rset.next();
        int id = rset.getInt(1) + 1;
        rset.close();
        return id;
    }
}
