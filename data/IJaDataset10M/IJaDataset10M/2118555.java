package interfaces.tables;

import static org.junit.Assert.*;
import interfaces.superWidgets.StaticContent;
import java.util.ArrayList;
import java.util.List;
import fileHandling.MapLoader;
import gameStates.absGamesStates.AbsIngameState;
import logic.common.game.Game;
import logic.common.game.GameMode;
import logic.common.player.Player;
import logic.common.team.Fraction;
import logic.common.team.Team;
import main.GameCycle;
import map.Map;
import org.fenggui.ScrollContainer;
import org.junit.Before;
import org.junit.Test;
import absTests.AbsGUITest;
import ai.AILevel;
import dummyClasses.DIngameState;

public class TeamTableTest extends AbsGUITest {

    private Team teamA, teamB;

    private TeamTable tableA, tableB;

    private Game game;

    private AbsIngameState ingameState;

    private Player p1, p2;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        ingameState = new DIngameState(passManagerState);
        Map map = MapLoader.createMap("clearscenario");
        teamA = new Team(Fraction.ALIEN, map.getTeamInfos(Fraction.ALIEN), 600, ingameState);
        teamB = new Team(Fraction.HUMAN, map.getTeamInfos(Fraction.HUMAN), 600, ingameState);
        List<String> scenarios = new ArrayList<String>();
        scenarios.add("clearscenario");
        GameCycle gameCycle = new GameCycle(scenarios, 5, 5, 6, false, AILevel.Easy, "a", "192.168.1.6", false, GameMode.Conquest, 0, 50, false, false);
        game = new Game(map, teamA, teamB, gameCycle);
        ingameState.setGame(game);
        tableA = new TeamTable(100, 100, 0, 0, teamA, ingameState);
        tableB = new TeamTable(100, 100, 0, 0, teamB, ingameState);
        p1 = new Player("p1", true, teamA, teamB);
    }

    public void checkTable(TeamTable table) {
        ScrollContainer scrollContent = table.getScrollContent();
        StaticContent tableContent = table.getTableContent();
        assertTrue(scrollContent.getInnerWidget() == tableContent);
        assertTrue(table.getContent().contains(scrollContent));
        assertTrue(tableContent.getY() == 0);
        assertTrue(tableContent.getX() == 0);
        assertTrue(tableContent.getWidth() > 0 && tableContent.getWidth() <= 100);
        assertTrue(tableContent.getHeight() > 0 && tableContent.getHeight() <= 100);
        List<TeamTableEntry> entries = table.getTableEntries();
        for (TeamTableEntry entry : entries) {
            int y = entry.getY();
            assertTrue(y >= 0 && y <= 100);
            assertTrue(entry.getX() == 0);
            assertTrue(entry.getWidth() == 100);
            assertTrue(entry.getHeight() > 0);
        }
    }

    @Test
    public void testAddPlayer() {
        assertTrue(tableA.getPlayerMap().containsKey(p1));
        checkTable(tableA);
        assertTrue(tableA.getTableContent().getContent().size() == 1);
    }

    @Test
    public void testRemovePlayer() {
        teamA.removePlayer(p1);
        checkTable(tableA);
        assertTrue(tableA.getTableContent().getContent().isEmpty());
    }

    @Test
    public void testSwitchPlayer() {
        p2 = new Player("p2", true, teamA, teamB);
        p2.switchTeam(teamB);
        assertTrue(!tableA.getPlayerMap().containsKey(p2));
        assertTrue(tableB.getPlayerMap().containsKey(p2));
        checkTable(tableA);
        checkTable(tableB);
    }
}
