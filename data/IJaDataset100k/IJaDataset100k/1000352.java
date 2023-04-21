package hoplugins.teamAnalyzer.dao;

import hoplugins.Commons;
import hoplugins.teamAnalyzer.vo.Team;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Access Manager for Favourite Teams
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class FavoritesDAO {

    /**
     * Creates a new FavoritesDAO object.
     */
    public FavoritesDAO() {
        checkTable();
    }

    /**
     * check if a team is a favourite team
     *
     * @param teamId
     *
     * @return true if team is a favourite
     */
    public boolean isFavourite(int teamId) {
        String query = "select * from TEAMANALYZER_FAVORITES where TEAMID=" + teamId;
        ResultSet rs = Commons.getModel().getAdapter().executeQuery(query);
        try {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    /**
     * Returns all favourite teams
     *
     * @return List of Teams Object
     */
    public List<Team> getTeams() {
        List<Team> list = new ArrayList<Team>();
        String query = "select * from TEAMANALYZER_FAVORITES";
        ResultSet rs = Commons.getModel().getAdapter().executeQuery(query);
        try {
            while (rs.next()) {
                Team team = new Team();
                team.setTeamId(rs.getInt(1));
                team.setName(rs.getString(2));
                list.add(team);
            }
        } catch (SQLException e) {
            return new ArrayList<Team>();
        }
        return list;
    }

    /**
     * Add a new Favourite team
     *
     * @param team the new favourite team
     */
    public void addTeam(Team team) {
        Commons.getModel().getAdapter().executeUpdate("insert into TEAMANALYZER_FAVORITES (TEAMID, NAME) values (" + team.getTeamId() + ", '" + team.getName() + "')");
    }

    /**
     * Remove a Favourite team
     *
     * @param teamId the favourite team to be removed
     */
    public void removeTeam(int teamId) {
        String query = "delete from TEAMANALYZER_FAVORITES where TEAMID=" + teamId;
        Commons.getModel().getAdapter().executeUpdate(query);
    }

    /**
     * Check if the table exists, if not create it  with default values
     */
    private void checkTable() {
        try {
            ResultSet rs = Commons.getModel().getAdapter().executeQuery("select * from TEAMANALYZER_FAVORITES");
            rs.next();
        } catch (Exception e) {
            Commons.getModel().getAdapter().executeUpdate("CREATE TABLE TEAMANALYZER_FAVORITES(TEAMID integer,NAME varchar(20))");
        }
    }
}
