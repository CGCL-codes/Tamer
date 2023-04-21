package ligueBaseball;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Match {

    private PreparedStatement stmtInsert;

    private PreparedStatement stmtInsertScore;

    private PreparedStatement stmtGetAllResults;

    private PreparedStatement stmtMaxId;

    private PreparedStatement stmtPresentementJoue;

    private Connexion cx;

    public Match(Connexion cx) throws SQLException {
        this.cx = cx;
        stmtPresentementJoue = cx.getConnection().prepareStatement("select * from match where matchdate = ? and matchheure = ?");
        stmtInsert = cx.getConnection().prepareStatement("insert into match (matchid, equipelocal, equipevisiteur, terrainid, matchdate, matchheure)" + "values (?,?,?,?,to_date(?,'YYYY-MM-DD'),to_date(?,'HH:MI'))");
        stmtInsertScore = cx.getConnection().prepareStatement("update match set pointslocal = ?, pointsvisiteur = ? where matchdate = ?, matchheure = ?, equipelocal = ?, equipevisiteur = ?");
        stmtGetAllResults = cx.getConnection().prepareStatement("");
        stmtMaxId = cx.getConnection().prepareStatement("select max(matchid) from match");
    }

    public boolean matchPossible(String matchDate, String matchheure, int idEquipeLocal, int idEquipeVisiteur) throws SQLException {
        ResultSet rset = getMatchPresentementJoue(matchDate, matchheure);
        boolean matchPossible = true;
        while (rset.next() && matchPossible) {
            matchPossible = !((rset.getInt(2) == idEquipeLocal) || (rset.getInt(3) == idEquipeLocal) || (rset.getInt(2) == idEquipeVisiteur) || (rset.getInt(3) == idEquipeVisiteur));
        }
        rset.close();
        return matchPossible;
    }

    public String afficherResultats(ResultSet rset) throws SQLException {
        String tous = null;
        if (rset.next()) {
            tous = "La liste d'équipes est la suivante : \n";
            do {
                tous += rset.getInt(1);
                tous += " ";
                tous += rset.getString(2);
                tous += "\n";
            } while (rset.next());
        } else {
            tous = "Il n'y a pas d'équipe dans la liste.";
        }
        return tous;
    }

    public String afficherResultatsNom(String equipeNom) throws SQLException {
        ResultSet rset = getAllResults();
        return afficherResultats(rset);
    }

    public String afficherResultatsDate(Date datematch) throws SQLException {
        ResultSet rset = getAllResults();
        return afficherResultats(rset);
    }

    private ResultSet getAllResults() throws SQLException {
        stmtGetAllResults.setInt(1, 2);
        return stmtGetAllResults.executeQuery();
    }

    public int inscrirePointage(String readDate, String readTime, int idEquipeLocal, int idEquipeVisiteur, int pointsLocal, int pointsVisiteur) throws SQLException {
        stmtInsertScore.setInt(1, pointsLocal);
        stmtInsertScore.setInt(2, pointsVisiteur);
        stmtInsertScore.setString(3, readDate);
        stmtInsertScore.setString(4, readTime);
        stmtInsertScore.setInt(5, idEquipeLocal);
        stmtInsertScore.setInt(6, idEquipeVisiteur);
        return stmtInsertScore.executeUpdate();
    }

    private ResultSet getMatchPresentementJoue(String matchdate, String matchheure) throws SQLException {
        stmtPresentementJoue.setString(1, matchdate);
        stmtPresentementJoue.setString(2, matchheure);
        return stmtPresentementJoue.executeQuery();
    }

    public void inscrire(String date, String heure, int idEquipeLocal, int idEquipeVisiteur, int idTerrain) throws SQLException {
        stmtInsert.setInt(1, getMaxId());
        stmtInsert.setInt(2, idEquipeLocal);
        stmtInsert.setInt(3, idEquipeVisiteur);
        stmtInsert.setInt(4, idTerrain);
        stmtInsert.setString(5, date);
        stmtInsert.setString(6, heure);
        stmtInsert.executeUpdate();
    }

    public Connexion getConnexion() {
        return cx;
    }

    private int getMaxId() throws SQLException {
        ResultSet rset = stmtMaxId.executeQuery();
        rset.next();
        int id = rset.getInt(1) + 1;
        rset.close();
        return id;
    }
}
