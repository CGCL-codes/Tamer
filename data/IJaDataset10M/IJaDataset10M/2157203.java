package lto;

import static org.junit.Assert.assertEquals;
import robocode.control.testing.RobotTestBed;
import robocode.BattleResults;
import robocode.control.events.BattleCompletedEvent;

/**
 * Illustrates JUnit testing of Robocode robots.
 * This test simply verifies that ToyBot always beats SittingDuck.
 * 
 * Also illustrates the overriding of a set of methods from RobotTestBed to show how the testing
 * behavior can be customized and controlled. 
 * 
 * @author Philip Johnson
 */
public class TestToyBotVersusTracker extends RobotTestBed {

    /**
   * Specifies that SittingDuck and ToyBot are to be matched up in this test case.
   * @return The comma-delimited list of robots in this match.
   */
    @Override
    public String getRobotNames() {
        return "sample.Tracker,lto.ToyBot";
    }

    /**
   * This test runs for 10 rounds.
   * @return The number of rounds. 
   */
    @Override
    public int getNumRounds() {
        return 10;
    }

    /**
   * The actual test, which asserts that ToyBot has won every round against SittingDuck.
   * @param event Details about the completed battle.
   */
    @Override
    public void onBattleCompleted(BattleCompletedEvent event) {
        BattleResults[] battleResults = event.getIndexedResults();
        BattleResults ToyBotResults = battleResults[1];
        String robotName = ToyBotResults.getTeamLeaderName();
        assertEquals("Check that results[1] is ToyBot", robotName, "lto.ToyBot*");
        assertEquals("Check ToyBot winner", ToyBotResults.getFirsts(), getNumRounds());
    }
}
