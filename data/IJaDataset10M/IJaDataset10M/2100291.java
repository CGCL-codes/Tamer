package net.mlw.fball.loader.yahoo;

import java.util.Iterator;
import java.util.List;
import net.mlw.fball.BaseTestCase;
import net.mlw.fball.bo.Team;
import net.mlw.fball.dao.TeamDao;
import net.mlw.fball.gui.AppContext;

/**
 * @author Matthew L. Wilson
 * @version $Revision: 1.1 $ $Date: 2004/03/19 21:44:17 $
 */
public class TeamLoaderTest extends BaseTestCase {

    /**
    * @see junit.framework.TestCase#setUp()
    */
    protected void doSetUp() throws Exception {
        System.out.println("Loading teams....");
        TeamLoader teamLoader = (TeamLoader) AppContext.getBean("loader.yahooTeamLoader");
        teamLoader.doLoad();
    }

    public void testInsertAllTeams() throws Exception {
        TeamDao teamDao = (TeamDao) AppContext.getBean("teamDao");
        List teams = teamDao.findByMap(null);
        assertEquals("32 Teams where expected.", 32, teams.size());
    }

    public void testTeamsNamesAreAsExpected() throws Exception {
        int count = 0;
        TeamDao teamDao = (TeamDao) AppContext.getBean("teamDao");
        List teams = teamDao.findByMap(null);
        for (Iterator iter = teams.iterator(); iter.hasNext(); ) {
            Team team = (Team) iter.next();
            assertEquals(this.teams[count++], team.getName());
        }
    }

    private final String[] teams = { "Arizona Cardinals", "Atlanta Falcons", "Baltimore Ravens", "Buffalo Bills", "Carolina Panthers", "Chicago Bears", "Cincinnati Bengals", "Cleveland Browns", "Dallas Cowboys", "Denver Broncos", "Detroit Lions", "Green Bay Packers", "Houston Texans", "Indianapolis Colts", "Jacksonville Jaguars", "Kansas City Chiefs", "Miami Dolphins", "Minnesota Vikings", "New England Patriots", "New Orleans Saints", "New York Giants", "New York Jets", "Oakland Raiders", "Philadelphia Eagles", "Pittsburgh Steelers", "San Diego Chargers", "San Francisco 49ers", "Seattle Seahawks", "St. Louis Rams", "Tampa Bay Buccaneers", "Tennessee Titans", "Washington Redskins" };
}
