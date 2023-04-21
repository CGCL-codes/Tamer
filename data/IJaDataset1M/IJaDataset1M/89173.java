package scamsoft.squadleader.rules;

import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import scamsoft.squadleader.setup.Battalion;
import scamsoft.squadleader.setup.Scenario;
import scamsoft.squadleader.setup.ScenarioAlpha;

/**
 * User: Andreas Mross
 * Date: Sep 13, 2003
 * Time: 7:37:45 AM
 */
public class GameTest extends TestCase {

    Game game;

    public void testGetCurrentStack() {
        Game game = getGame();
        game.getCurrentStack(Player.unowned);
        game.getCurrentStack(Player.observer);
    }

    private Game getGame() {
        if (game == null) {
            Scenario scenario = new ScenarioAlpha();
            game = new Game(scenario);
        }
        return game;
    }

    public void testGetUnitsBattalion() {
        Game game = getGame();
        List battalions = game.getScenario().getBattalions();
        for (Iterator e = battalions.iterator(); e.hasNext(); ) {
            Battalion nextBattalion = (Battalion) e.next();
            UnitGroup units = game.getBattalionsUnits(nextBattalion);
            for (Iterator f = units.iterator(); f.hasNext(); ) {
                Unit unit = (Unit) f.next();
                assertEquals(nextBattalion, game.getBattalion(unit));
            }
        }
        UnitGroup russians = game.getUnits().getAllBySide(Team.ALLIES);
        Battalion russianBattalion = (Battalion) game.getScenario().getBattalions().get(3);
        for (Iterator e = russians.iterator(); e.hasNext(); ) {
            Unit unit = (Unit) e.next();
            assertEquals(russianBattalion, game.getBattalion(unit));
        }
    }
}
