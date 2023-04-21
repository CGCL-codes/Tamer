package org.ctor.dev.llrps2.model;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Transaction;
import org.junit.Test;

public class ResultTest extends AbstractTest {

    @Test
    public void testRandom() throws Exception {
        final Transaction tx = session.beginTransaction();
        clean();
        int agentCount = 5;
        int roundCount = 2;
        int gameCount = 100;
        final Contest contest = doContest(new TestContest() {

            public MovePair move(int gameNumber) {
                final Move left = Move.values()[randomIndex(Move.values().length)];
                final Move right = Move.values()[randomIndex(Move.values().length)];
                return MovePair.create(left, right);
            }
        }, agentCount, roundCount, gameCount);
        for (Round round : contest.getRounds()) {
            final RoundResult result = round.getResult();
            assertTrue(result.getLeftGames() + result.getRightGames() + result.getDrawGames() != 0);
            assertTrue(result.getMaxLeftStraightGames() + result.getMaxRightStraightGames() != 0);
        }
        tx.commit();
    }

    @Test
    public void testAllLeft() throws Exception {
        final Transaction tx = session.beginTransaction();
        clean();
        int agentCount = 100;
        int roundCount = 2;
        int gameCount = 20;
        final Contest contest = doContest(new TestContest() {

            public MovePair move(int gameNumber) {
                return MovePair.create(Move.Rock, Move.Scissors);
            }
        }, agentCount, roundCount, gameCount);
        for (Round round : contest.getRounds()) {
            final RoundResult result = round.getResult();
            assertEquals(gameCount, result.getLeftGames());
            assertEquals(0, result.getRightGames());
            assertEquals(0, result.getDrawGames());
            assertEquals(gameCount, result.getMaxLeftStraightGames());
            assertEquals(0, result.getMaxRightStraightGames());
        }
        tx.commit();
    }

    @Test
    public void testAllRight() throws Exception {
        final Transaction tx = session.beginTransaction();
        clean();
        int agentCount = 5;
        int roundCount = 2;
        int gameCount = 20;
        final Contest contest = doContest(new TestContest() {

            public MovePair move(int gameNumber) {
                return MovePair.create(Move.Rock, Move.Paper);
            }
        }, agentCount, roundCount, gameCount);
        for (Round round : contest.getRounds()) {
            final RoundResult result = round.getResult();
            assertEquals(0, result.getLeftGames());
            assertEquals(gameCount, result.getRightGames());
            assertEquals(0, result.getDrawGames());
            assertEquals(0, result.getMaxLeftStraightGames());
            assertEquals(gameCount, result.getMaxRightStraightGames());
        }
        tx.commit();
    }

    @Test
    public void testHalf() throws Exception {
        final Transaction tx = session.beginTransaction();
        clean();
        int agentCount = 5;
        int roundCount = 2;
        int gameCount = 19;
        final Contest contest = doContest(new TestContest() {

            public MovePair move(int gameNumber) {
                if (gameNumber < 10) {
                    return MovePair.create(Move.NotAMove, Move.Rock);
                } else if (gameNumber == 10) {
                    return MovePair.create(Move.NotAMove, Move.NotAMove);
                } else {
                    return MovePair.create(Move.Scissors, Move.NotAMove);
                }
            }
        }, agentCount, roundCount, gameCount);
        for (Round round : contest.getRounds()) {
            final RoundResult result = round.getResult();
            assertEquals(9, result.getLeftGames());
            assertEquals(9, result.getRightGames());
            assertEquals(1, result.getDrawGames());
            assertEquals(9, result.getMaxLeftStraightGames());
            assertEquals(9, result.getMaxRightStraightGames());
        }
        tx.commit();
    }

    interface TestContest {

        MovePair move(int gameNumber);
    }

    private Contest doContest(TestContest moveCallback, int agentCount, int roundCount, int gameCount) {
        final GameRule gameRule = GameRule.Normal;
        createAgents(agentCount);
        final Contest contest = Contest.create("testResult");
        session.save(contest);
        contest.start();
        final List<Agent> agents = session.createCriteria(Agent.class).list();
        for (int roundIdx = 0; roundIdx < roundCount; ++roundIdx) {
            final Agent left = agents.get(randomIndex(agents.size()));
            final Agent right = agents.get(randomIndex(agents.size()));
            final RoundRule rule = RoundRule.create(gameCount, null, gameRule);
            final String roundName = String.format("%s - %s #%d", left.getIpAddress(), right.getIpAddress(), roundIdx + 1);
            final Round round = Round.create(contest, roundName, left, right, rule);
            session.save(round);
            round.setStartDateTime(new GregorianCalendar());
            for (int gameIdx = 0; gameIdx < gameCount; ++gameIdx) {
                final Game game = Game.create(gameIdx + 1, round);
                final MovePair pair = moveCallback.move(gameIdx + 1);
                game.setLeftMove(pair.getFirst());
                game.setRightMove(pair.getSecond());
            }
            round.setFinishDateTime(new GregorianCalendar());
            round.count();
        }
        contest.finish();
        return contest;
    }
}
